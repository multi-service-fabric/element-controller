/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.CommonUtil;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.common.log.MsfLogger;
import msf.ecmm.config.EcConfiguration;
import msf.ecmm.emctrl.pojo.AbstractMessage;
import net.juniper.netconf.Device;
import net.juniper.netconf.NetconfException;
import net.juniper.netconf.XML;

/**
 * Doing transmission control to EM
 */
public class EmController {

  /** XML Header Part */
  private final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
  /** NETCONF Header Part */
  private final String XML_NETCONF_HEADER = "<rpc message-id=\"";
  /** NETCONF Header Attribute Part */
  private final String XML_NETCONF_HEADER_ATTR = "\" xmlns=\"urn:ietf:params:xml:ns:netconf:base:1.0\">";
  /** NETCONF End Tag */
  private final String XML_NETCONF_HEADER_FIN = "</rpc>";
  /** NETCONF Message End Tag */
  private final String XML_FIN = "\n]]>]]>";

  /**
   * logger.
   */
  private final MsfLogger logger = new MsfLogger();

  /** Sending Queue */
  private List<RequestQueueEntry> queue;

  /** Its Own Instance (signleton) */
  private static EmController me = new EmController();

  /** Device of Netconf */
  private Device device;

  /** EM Address */
  private String address;

  /** EM Password */
  private String passwrd;

  /** EM Port */
  private int port;

  /** Timeout Value of the Queue */
  private int queueTimeout;

  /** EM Timeout Value */
  private int emTimeout;

  /** EM Account */
  private String user;

  /** Message ID (increment on each request) */
  private int count = 0;

  /**
   * Constructor
   */
  private EmController() {

  }

  /**
   * Getting Instance
   *
   * @return self
   */
  public static EmController getInstance() {
    return me;
  }

  /**
   * Initialization
   *
   * @return Success/fail of initialization -> true: success
   */
  public boolean initialize() {

    logger.trace(CommonDefinitions.START);

    address = EcConfiguration.getInstance().get(String.class, EcConfiguration.EM_ADDRESS);
    passwrd = EcConfiguration.getInstance().get(String.class, EcConfiguration.EM_PASSWORD);
    port = EcConfiguration.getInstance().get(Integer.class, EcConfiguration.EM_PORT);
    queueTimeout = EcConfiguration.getInstance().get(Integer.class, EcConfiguration.EM_QUEUE_TIMEOUT);
    emTimeout = EcConfiguration.getInstance().get(Integer.class, EcConfiguration.EM_TIMEOUT);
    user = EcConfiguration.getInstance().get(String.class, EcConfiguration.EM_USER);

    try {
      ArrayList<String> capabilities = new ArrayList<String>();
      capabilities.add("urn:ietf:params:netconf:base:1.0");
      capabilities.add("urn:ietf:params:netconf:capability:candidate:1.0");
      capabilities.add("urn:ietf:params:netconf:capability:confirmed-commit:1.0");
      capabilities.add("urn:ietf:params:netconf:capability:validate:1.0");

      device = new Device(address, user, passwrd, null, port, capabilities);
      device.setTimeOut(emTimeout * 1000);
    } catch (NetconfException | ParserConfigurationException e) {
      logger.error(LogFormatter.out.format(LogFormatter.MSG_504023, e));
      return false;
    }

    queue = Collections.synchronizedList(new LinkedList<RequestQueueEntry>());
    logger.trace(CommonDefinitions.END);

    return true;
  }

  /**
   * EM Request (doing exclusive control by itself)
   *
   * @param message
   *          request message to be sent to EM
   * @param autoLock
   *          doing exclusive control automatically
   * @return response from EM
   * @throws EmctrlException
   *           error has occurred in accessing EM device
   */
  public AbstractMessage request(AbstractMessage message, boolean autoLock) throws EmctrlException {
    return requestAtManualLock(message, autoLock);
  }

  /**
   * EM Request (Auto Exclusive Control)
   *
   * @param message
   *          request message to be sent to EM
   * @return response from EM
   * @throws EmctrlException
   *           error has occurred in accessing EM device
   */
  public AbstractMessage request(AbstractMessage message) throws EmctrlException {
    return requestAtManualLock(message, true);
  }

  /**
   * EM Request
   *
   * @param message
   *          request message to be sent to EM
   * @param autoLock
   *          doing exclusive control automatically
   * @return response from EM
   * @throws EmctrlException
   *           error has occurred in accessing EM device
   */
  private AbstractMessage requestAtManualLock(AbstractMessage message, boolean autoLock) throws EmctrlException {
    logger.debug("EM Request :" + message);

    RequestQueueEntry entry = null;
    if (autoLock == true) {
      entry = lock(message);
    }

    try {
      device.connect();
    } catch (IOException e) {
      logger.error(LogFormatter.out.format(LogFormatter.MSG_504023, e));
      device.close();
      if (autoLock == true) {
        unlock(entry);
      }
      throw new EmctrlException(entry + " connect fail.");
    }

    try {
      StringBuilder buf = new StringBuilder();
      buf.append(XML_HEADER);
      buf.append(XML_NETCONF_HEADER);
      buf.append(getMessageId());
      buf.append(XML_NETCONF_HEADER_ATTR);
      buf.append(message.decode());
      buf.append(XML_NETCONF_HEADER_FIN);
      buf.append(XML_FIN);

      logger.debug("EM Request[XML] :" + buf.toString());

      XML xml = device.executeRPC(buf.toString());

      message.encode(xml.toString());

      logger.debug("EM Response[XML] :" + xml);

      if (!message.isResult()) {
        logger.error(LogFormatter.out.format(LogFormatter.MSG_504024, message));
      }

      try {
        device.closeSession(getMessageId());
      } catch (Throwable e) {
        logger.debug("close-session NG", e);
      }

    } catch (SAXException | IOException e) {
      logger.error(LogFormatter.out.format(LogFormatter.MSG_504026, e), e);
      throw new EmctrlException(entry + " send fail.");

    } finally {
      device.close();
      if (autoLock == true) {
        unlock(entry);
      }
    }

    return message;

  }

  /**
   * Exclusive Control with EM
   *
   * @param request
   *          REST request message
   * @return queue entry key
   * @throws EmctrlException
   *           EM access exception
   */
  public RequestQueueEntry lock(AbstractMessage request) throws EmctrlException {
    logger.trace(CommonDefinitions.START);

    RequestQueueEntry entry = new RequestQueueEntry(request);

    queue.add(entry);

    for (;;) {
      Date now = new Date();
      if (now.getTime() - entry.getRequestTime().getTime() > queueTimeout * 1000) {
        queue.remove(entry);

        logger.error(LogFormatter.out.format(LogFormatter.MSG_504023, entry));
        throw new EmctrlException(entry + " is timeout.");
      }

      if (queue.get(0) == entry) { 
        break;
      }

      CommonUtil.sleep();

    }

    logger.trace(CommonDefinitions.END);

    return entry;
  }

  /**
   * Exclusive Control with EM (Unlock)
   *
   * @param entry
   *          queue entry
   */
  public void unlock(RequestQueueEntry entry) {
    logger.trace(CommonDefinitions.START);
    queue.remove(entry);
    logger.trace(CommonDefinitions.END);
  }

  /**
   * Getting message ID (incremented)
   *
   * @return message ID
   **/
  private long getMessageId() {
    return this.count++ % Integer.MAX_VALUE;
  }

  /**
   * Terminating EM Communication
   */
  public void close() {
    logger.trace(CommonDefinitions.START);
    device.close();
    logger.trace(CommonDefinitions.END);
  }

}
