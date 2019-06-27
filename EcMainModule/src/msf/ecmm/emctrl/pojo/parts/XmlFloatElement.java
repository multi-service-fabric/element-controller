/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

/**
*   The Class to use when setting the operation attribute in XML tag(Float).
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlFloatElement {

  @XmlAttribute
  String operation = null;

  @XmlValue
  Float value = null;

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
  public Float getValue() {
    return value;
  }

  /**
   * Setting value.
   * @param value Setting value
   */
  public void setValue(Float value) {
    this.value = value;
  }

  /* (Non Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "XmlFloatElement [operation=" + operation + ", value=" + value + "]";
  }
}
