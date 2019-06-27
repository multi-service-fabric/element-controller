/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * EM Device Header Configuration POJO Class
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "edit-config")
public class HeaderMessage extends AbstractMessage {

  /** Target Information */
  @XmlElement(name = "target")
  private TargetMessage targetMessage = new TargetMessage();

  /** Each Service Provision Information */
  @XmlElement(name = "config")
  private BodyMessage bodyMessage = null;

  /**
   * Generating new instance.
   */
  public HeaderMessage() {
    super();
  }

  /**
   * Getting target information.
   *
   * @return target information
   */
  public TargetMessage getTargetMessage() {
    return targetMessage;
  }

  /**
   * Setting target information.
   *
   * @param targetMessage
   *          target information
   */
  public void setTargetMessage(TargetMessage targetMessage) {
    this.targetMessage = targetMessage;
  }

  /**
   * Getting each service provision information.
   *
   * @return each service provision information
   */
  public BodyMessage getBodyMessage() {
    return bodyMessage;
  }

  /**
   * Setting each service provision information.
   *
   * @param bodyMessage
   *          each service provision information
   */
  public void setBodyMessage(BodyMessage bodyMessage) {
    this.bodyMessage = bodyMessage;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "HeaderMessage [targetMessage=" + targetMessage + ", bodyMessage=" + bodyMessage + "]";
  }
}
