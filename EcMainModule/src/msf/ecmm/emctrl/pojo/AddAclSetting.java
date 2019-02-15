/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo;

import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import msf.ecmm.emctrl.pojo.parts.DeviceLeafAcl;

/**
 * Filter Added POJO Class.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "acl-filter")
public class AddAclSetting extends AbstractMessage {

  /** Attribute data for service basic configuration. */
  @XmlAttribute
  private String xmlns = "http://www.ntt.co.jp/msf/service/acl-filter";

  /** Service name. */
  private String name = null;

  /** Leaf device information. */
  @XmlElement(name = "device-leaf")
  private ArrayList<DeviceLeafAcl> deviceLeafAclList = null;

  /**
   * Generating new instance.
   */
  public AddAclSetting() {
    super();
  }

  /**
   * Getting service name.
   *
   * @return service name
   */
  public String getName() {
    return name;
  }

  /**
   * Setting service name.
   *
   * @param name
   *          service name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Getting Leaf device information.
   *
   * @return Leaf device information
   */
  public ArrayList<DeviceLeafAcl> getDeviceLeafAclList() {
    return deviceLeafAclList;
  }

  /**
   * Setting the Leaf device information.
   *
   * @param deviceLeafList
   *          Leaf device information
   */
  public void setDeviceLeafAclList(ArrayList<DeviceLeafAcl> deviceLeafList) {
    this.deviceLeafAclList = deviceLeafList;
  }

  /**
   * XML decode.
   *
   * @return xml data
   */
  public String decode() {

    StringWriter createXml = new StringWriter();
    AddAclSettingHeader headerMessage = new AddAclSettingHeader();
    AddAclSettingBody bodyMessage = new AddAclSettingBody();
    headerMessage.setBodyMessage(bodyMessage);

    bodyMessage.setAddAclSetting(this);
    JAXB.marshal(headerMessage, createXml);

    StringBuilder delXmlHead = new StringBuilder(createXml.toString());

    int topIndex = delXmlHead.indexOf(XML_START_TAG);
    int tailIndex = delXmlHead.indexOf(XML_END_TAG);
    if (topIndex >= 0 && tailIndex >= 0) {
      delXmlHead.delete(topIndex, tailIndex + XML_END_TAG.length());
    }

    return delXmlHead.toString();
  }

  /*
   * (Non Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "AddAclSetting [xmlns=" + xmlns + ", name=" + name + ", deviceLeafList=" + deviceLeafAclList + "]";
  }

  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlRootElement(name = "config")
  static class AddAclSettingBody {
    /** Extended function. */
    @XmlElement(name = "acl-filter")
    private AddAclSetting addAclSetting = null;

    public void setAddAclSetting(AddAclSetting addAclSetting) {
      this.addAclSetting = addAclSetting;
    }
  }

  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlRootElement(name = "edit-config")
  static class AddAclSettingHeader {

    /** Target information. */
    @XmlElement(name = "target")
    private TargetMessage targetMessage = new TargetMessage();

    /** Regulation information for each service. */
    @XmlElement(name = "config")
    private AddAclSettingBody bodyMessage = null;

    public void setBodyMessage(AddAclSettingBody bodyMessage) {
      this.bodyMessage = bodyMessage;
    }
  }
}
