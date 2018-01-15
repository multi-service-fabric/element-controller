/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
* OSPF Configuration (Device Extention).
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ospf")
public class OspfAddNode {
  /** Attribute Data of ospf. */
  @XmlAttribute
  private String operation = null;

  /** OSPF_AREA_ID in Multicluster.(0 in single cluster) (specified only for Spine/Leaf/B-Leaf extention). */
  @XmlElement(name = "area-id")
  private String areaId = null;

  /** Virtual Link Configuration (specified only for B-Leaf extention). */
  @XmlElement(name = "virtual-link")
  private OspfVirtualLink virtualLink = null;

  /** OSPF Route Aggregation Configuration (specified only for B-Leaf extention). */
  private Range range = null;

  /** For Virtual Link Configuration Operation Configuration (specified only for B-Leaf removal). */
  @XmlElement(name = "virtual-link")
  private AttributeOperation virtualLinkOperation = null;

  /** For OSPF Route Aggregation Configuration Operation Configuration (specified only for B-Leaf removal). */
  @XmlElement(name = "range")
  private AttributeOperation rangeOperation = null;

  /**
   * Getting attribute data of ospf.
   *
   * @return attribute data of device.
   */
  public String getOperation() {
    return operation;
  }

  /**
   * Setting attribute data of ospf.
   *
   * @param operation
   *          attribute data of device.
   */
  public void setOperation(String operation) {
    this.operation = operation;
  }

  /**
   * Getting OSPF_AREA_ID in Multicluster (0 in single cluster) (specified only for Spine/Leaf/B-Leaf extention).
   *
   * @return OSPF_AREA_ID in Multicluster (0 in single cluster) (specified only for Spine/Leaf/B-Leaf extention).
   */
  public String getAreaId() {
    return areaId;
  }

  /**
   * Setting OSPF_AREA_ID in Multicluster (0 in single cluster) (specified only for Spine/Leaf/B-Leaf extention).
   *
   * @param areaId
   *          OSPF_AREA_ID in Multicluster (0 in single cluster) (specified only for Spine/Leaf/B-Leaf extention).
   */
  public void setAreaId(String areaId) {
    this.areaId = areaId;
  }

  /**
   * Getting Virtual Link Configuration (specified only for B-Leaf extention).
   *
   * @return Virtual Link Configuration (specified only for B-Leaf extention).
   */
  public OspfVirtualLink getVirtualLink() {
    return virtualLink;
  }

  /**
   * Setting Virtual Link Configuration (specified only for B-Leaf extention).
   *
   * @param virtualLink
   *          Virtual Link Configuration (specified only for B-Leaf extention).
   */
  public void setVirtualLink(OspfVirtualLink virtualLink) {
    this.virtualLink = virtualLink;
  }

  /**
   * Getting OSPF Route Aggregation Configuration (specified only for B-Leaf extention).
   *
   * @return OSPF Route Aggregation Configuration (specified only for B-Leaf extention).
   */
  public Range getRange() {
    return range;
  }

  /**
   * Setting OSPF Route Aggregation Configuration (specified only for B-Leaf extention).
   *
   * @param range
   *          OSPF Route Aggregation Configuration (specified only for B-Leaf extention).
   */
  public void setRange(Range range) {
    this.range = range;
  }

  /**
   * Getting For Virtual Link Configuration Operation Configuration (specified only for B-Leaf removal).
   *
   * @return For Virtual Link Configuration Operation Configuration (specified only for B-Leaf removal)..
   */
  public AttributeOperation getVirtualLinkOperation() {
    return virtualLinkOperation;
  }

  /**
   * Setting For Virtual Link Configuration Operation Configuration (specified only for B-Leaf removal)..
   *
   * @param virtualLinkOperation
   *          For Virtual Link Configuration Operation Configuration (specified only for B-Leaf removal)..
   */
  public void setVirtualLinkOperation(AttributeOperation virtualLinkOperation) {
    this.virtualLinkOperation = virtualLinkOperation;
  }

  /**
   * Getting For OSPF Route Aggregation Configuration Operation Configuration (specified only for B-Leaf removal).
   *
   * @return For OSPF Route Aggregation Configuration Operation Configuration (specified only for B-Leaf removal).
   */
  public AttributeOperation getRangeOperation() {
    return rangeOperation;
  }

  /**
   * Setting For OSPF Route Aggregation Configuration Operation Configuration (specified only for B-Leaf removal).
   *
   * @param rangeOperation
   *          For OSPF Route Aggregation Configuration Operation Configuration (specified only for B-Leaf removal).
   */
  public void setRangeOperation(AttributeOperation rangeOperation) {
    this.rangeOperation = rangeOperation;
  }

  @Override
  public String toString() {
    return "OspfAddNode [operation=" + operation + ", areaId=" + areaId + ", virtualLink=" + virtualLink + ", range="
        + range + ", virtualLinkOperation=" + virtualLinkOperation + ", rangeOperation=" + rangeOperation + "]";
  }
}
