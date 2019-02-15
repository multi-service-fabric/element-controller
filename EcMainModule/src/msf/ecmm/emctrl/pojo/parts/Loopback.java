package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * VRF loopback configuration information class.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "loopback")
public class Loopback {
  /** Setting IP address of loofback IF. */
  private String address = null;

  /** Setting prefix */
  private Long prefix = null;

  /**
   * Getting IP address.
   *
   * @return IP address 
   */
  public String getAddress() {
    return address;
  }

  /**
   * Setting IP address.
   *
   * @param address
   *          IP address 
   */
  public void setAddress(String address) {
    this.address = address;
  }

  /**
   * Getting prefix.
   *
   * @return Prefix
   */
  public Long getPrefix() {
    return prefix;
  }

  /**
   * Setting prefix.
   *
   * @param prefix
   *          Prefix
   */
  public void setPrefix(Long prefix) {
    this.prefix = prefix;
  }

  /*
   * (Non Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "Loopback [address=" + address + ", prefix=" + prefix + "]";
  }
}
