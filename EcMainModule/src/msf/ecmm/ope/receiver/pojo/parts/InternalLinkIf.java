/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import java.util.ArrayList;
import java.util.List;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * IF Information for Internal Link.
 */
public class InternalLinkIf {

  /** IF Type. */
  private String ifType;

  /** IF ID. */
  private String ifId;

  /** LAG Composing Member. */
  private List<InterfaceInfo> lagMember = new ArrayList<>();

  /** Line Speed of IF (with unit). */
  private String linkSpeed;

  /** IF (own device side) IP Address. */
  private String linkIpAddress;

  /** IF (own device side) IP Address Prefix Value. */
  private Integer prefix;

  /** Cost value. */
  private Integer cost;

  /**
   * Getting IF type.
   *
   * @return IF type.
   */
  public String getIfType() {
    return ifType;
  }

  /**
   * Setting IF type.
   *
   * @param ifType
   *          IF type.
   */
  public void setIfType(String ifType) {
    this.ifType = ifType;
  }

  /**
   * Getting IF ID.
   *
   * @return IF ID.
   */
  public String getIfId() {
    return ifId;
  }

  /**
   * Setting IF ID.
   *
   * @param ifId
   *          IF ID.
   */
  public void setIfId(String ifId) {
    this.ifId = ifId;
  }

  /**
   * Getting LAG composing member.
   *
   * @return LAG composing member
   */
  public List<InterfaceInfo> getLagMember() {
    return lagMember;
  }

  /**
   * Setting LAG composing member.
   *
   * @param lagMember
   *          LAG composing member
   */
  public void setLagMember(List<InterfaceInfo> lagMember) {
    this.lagMember = lagMember;
  }

  /**
   * Getting IF line speed (with unit).
   *
   * @return IF line speed (with unit).
   */
  public String getLinkSpeed() {
    return linkSpeed;
  }

  /**
   * Setting IF line speed (with unit).
   *
   * @param linkSpeed
   *          IF line speed (with unit).
   */
  public void setLinkSpeed(String linkSpeed) {
    this.linkSpeed = linkSpeed;
  }

  /**
   * Getting IF (own device side) IP address.
   *
   * @return IF (own device side) IP address.
   */
  public String getLinkIpAddress() {
    return linkIpAddress;
  }

  /**
   * Setting IF (own device side) IP address.
   *
   * @param linkIpAddress
   *          IF (own device side) IP address.
   */
  public void setLinkIpAddress(String linkIpAddress) {
    this.linkIpAddress = linkIpAddress;
  }

  /**
   * Getting IF (own device side) IP address prefix value.
   *
   * @return IF (own device side) IP address prefix value.
   */
  public Integer getPrefix() {
    return prefix;
  }

  /**
   * Setting IF (own device side) IP address prefix value.
   *
   * @param prefix
   *          IF (own device side) IP address prefix value.
   */
  public void setPrefix(Integer prefix) {
    this.prefix = prefix;
  }

  /**
   * Geetting cost value.
   * @return Cost Value.
   */
  public Integer getCost() {
      return cost;
  }

  /**
   * Setting cost value.
   * @param cost Cost value.
   */
  public void setCost(Integer cost) {
      this.cost = cost;
  }

  /* (Non Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "InternalLinkIf [ifType=" + ifType + ", ifId=" + ifId + ", lagMember=" + lagMember + ", linkSpeed="
        + linkSpeed + ", linkIpAddress=" + linkIpAddress + ", prefix=" + prefix + ", cost=" + cost + "]";
  }

  /**
   * Input Parameter Check.
   *
   * @param ope
   *          operation type
   * @throws CheckDataException
   *           input check error
   */
  public void check(OperationType ope) throws CheckDataException {

    if (ifType == null) {
      throw new CheckDataException();
    } else if (!ifType.equals(CommonDefinitions.IF_TYPE_PHYSICAL_IF)
        && !ifType.equals(CommonDefinitions.IF_TYPE_LAG_IF)
        && !ifType.equals(CommonDefinitions.IF_TYPE_BREAKOUT_IF)) {
      throw new CheckDataException();
    }
    if (ifId == null) {
      throw new CheckDataException();
    }
    if (!lagMember.isEmpty()) {
      for (InterfaceInfo ifInfo : lagMember) {
        ifInfo.check(ope);
      }
    }
    if (linkIpAddress == null) {
      throw new CheckDataException();
    }
    if (prefix != 30) {
      throw new CheckDataException();
    }
  }


}
