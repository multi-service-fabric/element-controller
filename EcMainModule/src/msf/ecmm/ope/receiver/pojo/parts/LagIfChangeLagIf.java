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
 * Changing LagIF.
 */
public class LagIfChangeLagIf {

  /** LagIF link type. */
  private String linkType;

  /** Physical IF list( part in generated LagIF is reused) */
  private List<PhysicalIfsCreateLagIf> physicalIfs = new ArrayList<PhysicalIfsCreateLagIf>();

  /**
   * Getting LagIF link type.
   *
   * @return  LagIF link type
   */
  public String getLinkType() {
    return linkType;
  }

  /**
   * Setting LagIF link type.
   *
   * @param linkType
   *          LagIF link type.
   */
  public void setLinkType(String linkType) {
    this.linkType = linkType;
  }

  /**
   * Getting physical IF list.
   *
   * @return physical IF list
   */
  public List<PhysicalIfsCreateLagIf> getPhysicalIfs() {
    return physicalIfs;
  }

  /**
   * Setting  physical IF list.
   *
   * @param physicalIfs
   *          physical IF list
   */
  public void setPhysicalIfs(List<PhysicalIfsCreateLagIf> physicalIfs) {
    this.physicalIfs = physicalIfs;
  }

  /**
   * Stringizing Instance
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "LagIfChangeLagIf [linkType=" + linkType + ", physicalIfs=" + physicalIfs + "]";
  }

  /**
   * Input paramter check.
   *
   * @param ope
   *           operation type
   * @throws CheckDataException
   *           inpt paramter error
   */
  public void check(OperationType ope) throws CheckDataException {

    if (linkType == null || physicalIfs.isEmpty() || (!(linkType.equals(CommonDefinitions.LINK_TYPE_INTERNAL_LINK))
        && !(linkType.equals(CommonDefinitions.LINK_TYPE_OTHER)))) {
      throw new CheckDataException();
    } else {
      for (PhysicalIfsCreateLagIf physi : physicalIfs) {
        if (physi == null) {
          throw new CheckDataException();
        } else {
          physi.check(ope);
        }
      }
    }
  }
}