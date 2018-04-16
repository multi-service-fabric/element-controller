/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * Class which is used when configuring operation to non-object tags.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class AttributeOperation {

  @XmlAttribute
  String operation = null;

  /**
   * Getting operation.
   *
   * @return operation
   */
  public String getOperation() {
    return operation;
  }

  /**
   * Settomg operation.
   *
   * @param operation
   *          operation
   */
  public void setOperation(String operation) {
    this.operation = operation;
  }

  @Override
  public String toString() {
    return "AttributeOperation [operation=" + operation + "]";
  }
}
