/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.emctrl.EmctrlException;

/**
 * Message Operation for EM Class.
 */
public class AbstractMessage {

  /** Logger Instance. */
  private final Logger logger = LogManager.getLogger(CommonDefinitions.EC_LOGGER);

  /** String Operation Parts (XML Declaration Top). */
  private static final String XML_START_TAG = "<?xml";
  /** String Operation Parts (Close Tag). */
  private static final String XML_END_TAG = "?>\n";
  /** Process Success Response Tag. */
  private static final String SUCCESS_TAG = "ok";
  /** Process Error Response Tag. */
  private static final String ERROR_TAG = "rpc-error";
  /** Error Type Tag. */
  private static final String ERROR_TYPE = "error-type";
  /** Error Message Tag. */
  private static final String ERROR_MESSAGE = "error-message";

  /** Process Result. */
  private boolean result = false;

  /** Error Message. */
  private String errorMessage = "";

  /** Error Type. */
  private String errorType = "";

  /**
   * Generating new instance.
   */
  public AbstractMessage() {
    super();
  }

  /**
   * Getting process result.
   *
   * @return process result
   */
  @XmlTransient
  public boolean isResult() {
    return result;
  }

  /**
   * Setting process result.
   *
   * @param result
   *          process result
   */
  public void setResult(boolean result) {
    this.result = result;
  }

  /**
   * Getting error message.
   *
   * @return error message
   */
  @XmlTransient
  public String getErrorMessage() {
    return errorMessage;
  }

  /**
   * Setting error message.
   *
   * @param errorMessage
   *          error message
   */
  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  /**
   * Getting error type.
   *
   * @return error type
   */
  @XmlTransient
  public String getErrorType() {
    return errorType;
  }

  /**
   * Setting error type.
   *
   * @param errorType
   *          error type
   */
  public void setErrorType(String errorType) {
    this.errorType = errorType;
  }

  /**
   * XML decode.
   *
   * @return xml data
   * @throws EmctrlException
   *           Instance of which XML decode is impossible was specified.
   */
  public String decode() throws EmctrlException {

    StringWriter createXml = new StringWriter();
    HeaderMessage headerMessage = new HeaderMessage();
    BodyMessage bodyMessage = new BodyMessage();
    headerMessage.setBodyMessage(bodyMessage);
    if (this instanceof L2SliceAddDelete) {
      bodyMessage.setL2SliceAddDelete((L2SliceAddDelete) this);
      JAXB.marshal(headerMessage, createXml);
    } else if (this instanceof L3SliceAddDelete) {
      bodyMessage.setL3SliceAddDelete((L3SliceAddDelete) this);
      JAXB.marshal(headerMessage, createXml);
    } else if (this instanceof LeafAddDelete) {
      bodyMessage.setLeafAddDelete((LeafAddDelete) this);
      JAXB.marshal(headerMessage, createXml);
    } else if (this instanceof SpineAddDelete) {
      bodyMessage.setSpineAddDelete((SpineAddDelete) this);
      JAXB.marshal(headerMessage, createXml);
    } else if (this instanceof InternalLinkAddDelete) {
      bodyMessage.setInternalLinkLagAddDelete((InternalLinkAddDelete) this);
      JAXB.marshal(headerMessage, createXml);
    } else if (this instanceof RecoverUpdateNode) {
      bodyMessage.setRecoverUpdateNode((RecoverUpdateNode) this);
      JAXB.marshal(headerMessage, createXml);
    } else if (this instanceof RecoverUpdateService) {
      bodyMessage.setRecoverUpdateService((RecoverUpdateService) this);
      JAXB.marshal(headerMessage, createXml);
    } else if (this instanceof CeLagAddDelete) {
      bodyMessage.setCeLagAddDelete((CeLagAddDelete) this);
      JAXB.marshal(headerMessage, createXml);
    } else if (this instanceof BLeafAddDelete) {
      bodyMessage.setBLeafAddDelete((BLeafAddDelete) this);
      JAXB.marshal(headerMessage, createXml);
    } else if (this instanceof BetweenClustersLinkAddDelete) {
      bodyMessage.setBetweenClustersLinkAddDelete((BetweenClustersLinkAddDelete) this);
      JAXB.marshal(headerMessage, createXml);
    } else if (this instanceof BreakoutIfAddDelete) {
      bodyMessage.setBreakoutIfAddDelete((BreakoutIfAddDelete) this);
      JAXB.marshal(headerMessage, createXml);

    } else {
      logger.error(LogFormatter.out.format(LogFormatter.MSG_504025, "instance illegal"));
      throw new EmctrlException("unknown instance.");
    }

    StringBuilder delXmlHead = new StringBuilder(createXml.toString());

    int topIndex = delXmlHead.indexOf(XML_START_TAG);
    int tailIndex = delXmlHead.indexOf(XML_END_TAG);
    if (topIndex >= 0 && tailIndex >= 0) {
      delXmlHead.delete(topIndex, tailIndex + XML_END_TAG.length());
    }

    return delXmlHead.toString();
  }

  /**
   * XML encode.
   *
   * @param xml
   *          xml data
   * @throws EmctrlException
   *           Received data of which XML decode fails.
   */
  public void encode(String xml) throws EmctrlException {
    try {
      InputSource inputSource = new InputSource(new StringReader(xml));

      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document doc = builder.parse(inputSource);

      if (0 < doc.getElementsByTagName(SUCCESS_TAG).getLength()) {
        this.result = true;
      } else if (0 < doc.getElementsByTagName(ERROR_TAG).getLength()) {
        this.result = false;

        NodeList nodeList = doc.getElementsByTagName(ERROR_TYPE);
        Node node = nodeList.item(0);
        Node content = node.getFirstChild();
        this.errorType = content.getNodeValue();

        nodeList = doc.getElementsByTagName(ERROR_MESSAGE);
        node = nodeList.item(0);
        content = node.getFirstChild();
        this.errorMessage = content.getNodeValue();
      } else {
        this.result = false;
      }
    } catch (Exception exp) {
      this.result = false;
      logger.debug("xml decode failed. xml=" + xml);
      logger.error(LogFormatter.out.format(LogFormatter.MSG_504026, xml), exp);
      throw new EmctrlException("xml encode failed.");
    }
    return;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "AbstractMessage [result=" + result + ", errorMessage=" + errorMessage + ", errorType=" + errorType + "]";
  }
}
