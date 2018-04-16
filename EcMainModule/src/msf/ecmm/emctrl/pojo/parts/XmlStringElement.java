/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

/**
 * The Class to use when setting the operation attribute in XML tag(String).
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlStringElement {

  @XmlAttribute
  String operation = null;

  @XmlValue
  String value = null;

  /**
   * Getting operation.
   *
   * @return operation
   */
  public String getOperation() {
    return operation;
  }

  /**
   * Setting operation.
   *
   * @param operation
   *          operation
   */
  public void setOperation(String operation) {
    this.operation = operation;
  }

  /**
   * Getting value.
   * @return value
   */
  public String getValue() {
    return value;
  }

  /**
   * Setting value.
   * @param value set value
   */
  public void setValue(String value) {
    this.value = value;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "XmlStringElement [operation=" + operation + ", value=" + value + "]";
  }
}
