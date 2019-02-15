package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Multi-homing configuration information class.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "multi-homing")
public class MultiHoming {

  /** anycast coniguraiton. */
  private Anycast anycast = null;

  /** Setting Clag Bridge IF IP address. */
  @XmlElement(name = "interface")
  private ClagHandOverInterface clagInterface = null;

  /** Clag Bridge IF configuration. */
  private Clag clag = null;

  /**
   * Getting anycast configuration.
   *
   * @return setting anycast
   */
  public Anycast getAnycast() {
    return anycast;
  }

  /**
   * Setting anycast.
   *
   * @param anycast
   *          Setting anycast
   */
  public void setAnycast(Anycast anycast) {
    this.anycast = anycast;
  }

  /**
   * Getting Clag Bridge IF IP address configuraitn.
   *
   * @return Clag Bridge IF IP address configuration
   */
  public ClagHandOverInterface getClagInterface() {
    return clagInterface;
  }

  /**
   * Setting Clag Bridge IF IP address.
   *
   * @param clagInterface
   *          Setting Clag Bridge IF IP address
   */
  public void setClagInterface(ClagHandOverInterface clagInterface) {
    this.clagInterface = clagInterface;
  }

  /**
   * Getting Clag Bridge IF configuration.
   *
   * @return clag
   *          Setting Clag Bridge IF
   */
  public Clag getClag() {
    return clag;
  }

  /**
   *Setting Clag Bridge IF configuration.
   *
   * @param clag
   *          Setting Clag Bridge IF
   */
  public void setClag(Clag clag) {
    this.clag = clag;
  }

  /*
   * (Non Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "MultiHoming [anycast=" + anycast + ", clagInterface=" + clagInterface + ", clag=" + clag + "]";
  }

}
