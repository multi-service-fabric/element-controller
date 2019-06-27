/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

/**
 * Class which is used for setting the value of Integer type that requires setting of "operation" attribute within XML tag.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlIntegerElement {

  @XmlAttribute
  String operation = null;

  @XmlValue
  Integer value = null;

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
  public Integer getValue() {
    return value;
  }

  /**
   * Setting value.
   * @param value Setting value
   */
  public void setValue(Integer value) {
    this.value = value;
  }

  /* (Non Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "XmlStringElement [operation=" + operation + ", value=" + value + "]";
  }
}
