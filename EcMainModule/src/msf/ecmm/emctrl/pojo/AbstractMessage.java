
package msf.ecmm.emctrl.pojo;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.emctrl.EmctrlException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class AbstractMessage {

	private final String XML_START_TAG = "<?xml";
	private final String SUCCESS_TAG = "ok";
	private final String ERROR_TYPE = "error-type";
	private boolean result = false;

	private String errorType = "";

	public AbstractMessage() {
		super();
	}

	@XmlTransient
	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	@XmlTransient
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@XmlTransient
	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

	public String decode() throws EmctrlException {

		StringWriter createXml = new StringWriter();
		HeaderMessage headerMessage = new HeaderMessage();
		BodyMessage bodyMessage = new BodyMessage();
		headerMessage.setBodyMessage(bodyMessage);
		if (this instanceof L2SliceAddDelete) {
			bodyMessage.setL2SliceAddDelete((L2SliceAddDelete) this);
			JAXB.marshal(headerMessage, createXml);
		}
		else if (this instanceof L3SliceAddDelete) {
			bodyMessage.setL3SliceAddDelete((L3SliceAddDelete) this);
			JAXB.marshal(headerMessage, createXml);
		}
		else if (this instanceof LeafAddDelete) {
			bodyMessage.setLeafAddDelete((LeafAddDelete) this);
			JAXB.marshal(headerMessage, createXml);
		}
		else if (this instanceof SpineAddDelete) {
			bodyMessage.setSpineAddDelete((SpineAddDelete) this);
			JAXB.marshal(headerMessage, createXml);
		}
		else if (this instanceof InternalLinkLagAddDelete) {
			bodyMessage.setInternalLinkLagAddDelete((InternalLinkLagAddDelete) this);
			JAXB.marshal(headerMessage, createXml);
		}
		else if (this instanceof CeLagAddDelete) {
			bodyMessage.setCeLagAddDelete((CeLagAddDelete) this);
			JAXB.marshal(headerMessage, createXml);
		}
		else {
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

	public void encode(String xml) throws EmctrlException {
		try {
			InputSource inputSource = new InputSource(new StringReader(xml));

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(inputSource);

			if (0 < doc.getElementsByTagName(SUCCESS_TAG).getLength()) {
				this.result = true;
			}
			else if (0 < doc.getElementsByTagName(ERROR_TAG).getLength()) {
				this.result = false;

				NodeList nodeList = doc.getElementsByTagName(ERROR_TYPE);
				Node node = nodeList.item(0);
				Node content = node.getFirstChild();
				this.errorType = content.getNodeValue();

				nodeList = doc.getElementsByTagName(ERROR_MESSAGE);
				node = nodeList.item(0);
				content = node.getFirstChild();
				this.errorMessage = content.getNodeValue();
			}
			else {
				this.result = false;
			}
		} catch (Exception e) {
			this.result = false;
			logger.debug("xml decode failed. xml=" + xml);
			logger.error(LogFormatter.out.format(LogFormatter.MSG_504026, e),e);
			throw new EmctrlException("xml encode failed.");
		}
		return;
	}

	@Override
	public String toString() {
		return "AbstractMessage [result=" + result + ", errorMessage=" + errorMessage + ", errorType=" + errorType
				+ "]";
	}
}
