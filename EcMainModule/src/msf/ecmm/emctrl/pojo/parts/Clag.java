package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Clag Bridge IF set class.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "clag")
public class Clag {

  /** Setting backup-IP address. */
  @XmlElement(name = "backup")
  private ClagBackup backup = null;

  /** Setting Clag Bridge IF IP address. */
  @XmlElement(name = "peer")
  private ClagPeer peer = null;

  /**
   * Acquring backup-IP address set.
   *
   * @return  backup-IP address set
   */
  public ClagBackup getBackup() {
    return backup;
  }

  /**
   * Providing backup-IP address setting.
   *
   * @param backup
   *           backup-IP address setting
   */
  public void setBackup(ClagBackup backup) {
    this.backup = backup;
  }

  /**
   * Acquring Clag Bridge IF IP address setting.
   *
   * @return Clag Bridge IF IP address setting
   */
  public ClagPeer getPeer() {
    return peer;
  }

  /**
   * Setting Clag Bridge IF IP address set.
   *
   * @param peer
   *           Clag Bridge IF IP address set
   */
  public void setPeer(ClagPeer peer) {
    this.peer = peer;
  }

  /*
   * (Non Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "Clag [backup=" + backup + ", peer=" + peer + "]";
  }

}
