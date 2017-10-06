
package msf.ecmm.emctrl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.CommonUtil;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.config.EcConfiguration;
import msf.ecmm.emctrl.pojo.AbstractMessage;
import net.juniper.netconf.Device;
import net.juniper.netconf.NetconfException;
import net.juniper.netconf.XML;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

public class EmController {

	private final String XML_NETCONF_HEADER = "<rpc message-id=\"";
	private final String XML_NETCONF_HEADER_FIN = "</rpc>";
	private final Logger logger = LogManager.getLogger(CommonDefinitions.EC_LOGGER);

	private static EmController me = new EmController();

	private String address;

	private int port;

	private int emTimeout;

	private int count = 0;

	private EmController() {

	}

	public static EmController getInstance() {
		return me;
	}

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

	public AbstractMessage request(AbstractMessage message, boolean autoLock) throws EmctrlException {
		return requestAtManualLock(message, autoLock);
	}

	public AbstractMessage request(AbstractMessage message) throws EmctrlException {
		return requestAtManualLock(message, true);
	}

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

				break;
			}

			CommonUtil.sleep();

		}

		logger.trace(CommonDefinitions.END);

		return entry;
	}

	public void unlock(RequestQueueEntry entry) {
		logger.trace(CommonDefinitions.START);
		queue.remove(entry);
		logger.trace(CommonDefinitions.END);
	}

	private long getMessageId() {
		return this.count++ % Integer.MAX_VALUE;
	}

	public void close() {
		logger.trace(CommonDefinitions.START);
		device.close();
		logger.trace(CommonDefinitions.END);
	}

}
