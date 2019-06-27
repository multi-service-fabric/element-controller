/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo.parts;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Setting the ACL that you want to add.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "filter")
public class AclFilter {

  /** Attribute data of ACL. */
  @XmlAttribute
  String operation = null;

  /** Identifier for ACL configuration. */
  @XmlElement(name = "filter_id")
  private Integer filterId = null;

  /** Details of ACL configuration information. */
  private ArrayList<Term> term = null;

  /**
   * Getting identifier for ACL configuration.
   *
   * @return filterId
   */
  public Integer getFilterId() {
    return filterId;
  }

  /**
   * Setting identifier for ACL configuration.
   *
   * @param filterId
   *          setting filterId
   */
  public void setFilterId(Integer filterId) {
    this.filterId = filterId;
  }

  /**
   * Getting ACL advanced setting.
   *
   * @return term
   */
  public ArrayList<Term> getTerm() {
    return term;
  }

  /**
   * Setting ACL advanced setting.
   *
   * @param term
   *          set filterId
   */
  public void setTerm(ArrayList<Term> term) {
    this.term = term;
  }

  /**
   *Getting attribute data of ACL.
   *
   * @return operation
   */
  public String getOperation() {
    return operation;
  }

  /**
  * Setting attribute data of ACL.
  *
  * @param operation
  *          set filterId
  */
  public void setOperation(String operation) {
    this.operation = operation;
  }

  @Override
  public String toString() {
    return "AclFilter [operation=" + operation + ", filterId=" + filterId + ", term=" + term + "]";
  }
}
