/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * EM Device Target Configuration POJO Class
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "target")
public class TargetMessage extends AbstractMessage {

  /** Target Start-up Yes/No */
  @XmlElement(name = "running")
  private String running = "";

  /**
   * Generating new instance.
   */
  public TargetMessage() {
    super();
  }

  /**
   * Getting target start-up yes/no.
   *
   * @return target start-up yes/no
   */
  public String getRunning() {
    return running;
  }

  /**
   * Setting target start-up yes/no.
   *
   * @param running
   *          target start-up yes/no
   */
  public void setRunning(String running) {
    this.running = running;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "TargetMessage [running=" + running + "]";
  }
}
