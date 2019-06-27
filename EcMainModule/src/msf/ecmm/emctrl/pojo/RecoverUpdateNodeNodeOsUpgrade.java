/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo;

import java.io.StringWriter;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import msf.ecmm.emctrl.pojo.parts.DeviceNodeOsUpgrade;

/**
 * Class for the added POJO to recover the node.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "recover-node")
public class RecoverUpdateNodeNodeOsUpgrade extends AbstractMessage {

  /** The attribute data for setting the basic service. */	
  @XmlAttribute
  private String xmlns = "http://www.ntt.co.jp/msf/service/recover-node";

  /** The service name. */
  private String name = null;

  /** The deive information. */
  private DeviceNodeOsUpgrade device = null;

  /**
   * A new instance is generated.
   */
  public RecoverUpdateNodeNodeOsUpgrade() {
    super();
  }

  /**
   * The service name is acquired.
   *
   * @return The service name
   */
  public String getName() {
    return name;
  }

  /**
   * The service name is set.
   *
   * @param name
   *          The service name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * The device information is acquired.
   *
   * @return The device information
   */
  public DeviceNodeOsUpgrade getDevice() {
    return device;
  }

  /**
   * The device information is set.
   *
   * @param device
   *          The device information
   */
  public void setDevice(DeviceNodeOsUpgrade device) {
    this.device = device;
  }

  /**
   * The instance is changed  to the string.
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "LeafAddDelete [name=" + name + ", device=" + device + "]";
  }

  /**
   * The XML data is decoded.
   *
   * @return The xml data
   */
  public String decode() {

    StringWriter createXml = new StringWriter();
    ExpandOpeHeader headerMessage = new ExpandOpeHeader();
    ExpandOpeBody bodyMessage = new ExpandOpeBody();
    headerMessage.setBodyMessage(bodyMessage);

    bodyMessage.setExpandOpeEm(this);
    JAXB.marshal(headerMessage, createXml);

    StringBuilder delXmlHead = new StringBuilder(createXml.toString());

    int topIndex = delXmlHead.indexOf(XML_START_TAG);
    int tailIndex = delXmlHead.indexOf(XML_END_TAG);
    if (topIndex >= 0 && tailIndex >= 0) {
      delXmlHead.delete(topIndex, tailIndex + XML_END_TAG.length());
    }

    return delXmlHead.toString();
  }

  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlRootElement(name = "config")
  static class ExpandOpeBody {
    /** The extended function. */
    @XmlElement(name = "recover-node")
    private RecoverUpdateNodeNodeOsUpgrade expandOpeEm = null;

    public void setExpandOpeEm(RecoverUpdateNodeNodeOsUpgrade expandOpeEm) {
      this.expandOpeEm = expandOpeEm;
    }
  }

  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlRootElement(name = "edit-config")
  static class ExpandOpeHeader {

    /** The target information. */
    @XmlElement(name = "target")
    private TargetMessage targetMessage = new TargetMessage();

    /** The infomation specifiying the each service . */
    @XmlElement(name = "config")
    private ExpandOpeBody bodyMessage = null;

    public void setBodyMessage(ExpandOpeBody bodyMessage) {
      this.bodyMessage = bodyMessage;
    }
  }
}
