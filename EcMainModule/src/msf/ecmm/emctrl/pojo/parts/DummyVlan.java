
package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Dummy VLANIF information configuration class.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "dummy_vlan")
public class DummyVlan {
  /** Attribute data of DummyVlan. */
  @XmlAttribute
  private String operation = null;

  /** VLAN ID. */
  @XmlElement(name = "vlan-id")
  private Long vlanId = null;

  /** VNI value. */
  private Long vni = null;

  /** IRB instance information. */
  private Irb irb = null;

  /**
   * Acquring attribute data of dummy_vlan.
   *
   * @return Attribute data of device
   */
  public String getOperation() {
    return operation;
  }

  /**
   * Setting attribute data of dummy_vlan.
   *
   * @param operation
   *          attribute data of device
   */
  public void setOperation(String operation) {
    this.operation = operation;
  }

  /**
   * Acquiring VLAN ID.
   *
   * @return VLAN ID
   */
  public Long getVlanId() {
    return vlanId;
  }

  /**
   * Acquiring VLAN ID.
   *
   * @param vlanId
   *          VLAN ID
   */
  public void setVlanId(Long vlanId) {
    this.vlanId = vlanId;
  }

  /**
   * Acquiring VNI value.
   *
   * @return VNI
   */
  public Long getVni() {
    return vni;
  }

  /**
   * Setting VNI value.
   *
   * @param vni
   *          VNI value
   */
  public void setVni(Long vni) {
    this.vni = vni;
  }

  /**
   * Acquiring IRB information.
   *
   * @return VNI
   */
  public Irb getIrb() {
    return irb;
  }

  /**
   * Setting IRB information.
   *
   * @param irb
   *          VNI value
   */
  public void setIrb(Irb irb) {
    this.irb = irb;
  }

  /*
   * (Non Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "DummyVlan [operation=" + operation + ", vlanId=" + vlanId + ", vni=" + vni + ", irb=" + irb + "]";
  }

}
