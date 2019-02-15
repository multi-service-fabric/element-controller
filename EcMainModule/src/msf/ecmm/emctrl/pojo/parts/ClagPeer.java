package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Clag peer setting information class.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "peer")
public class ClagPeer {
  /** Clag Bridge IF peerIP address. */
  private String address = null;

  /**
   * Acquiring Clag Bridge IF peerIP address.
   *
   * @return Clag Bridge IF peerIP address
   */
  public String getAddress() {
    return address;
  }

  /**
   * Setting Clag Bridge IF peerIP address.
   *
   * @param address
   *          IP address
   */
  public void setAddress(String address) {
    this.address = address;
  }

  /*
   * (Non Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "ClagPeer [address=" + address + "]";
  }
}
