package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * IRB Physical IF IP address configuration information class.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "physical-ip-address")
public class PhysicalIpAddress {
  /** IP address configuration. */
  private String address = null;

  /** Prefix configuration. */
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
   * @return prefix
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
    return "PhysicalIpAddress [address=" + address + ", prefix=" + prefix + "]";
  }

}
