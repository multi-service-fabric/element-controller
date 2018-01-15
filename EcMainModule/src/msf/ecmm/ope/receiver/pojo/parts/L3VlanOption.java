/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import java.util.ArrayList;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * Optional Information for L3VLAN IF.
 */
public class L3VlanOption {

  /** VRF ID. */
  private String vrfId;

  /** Static Route Information List. */
  private ArrayList<UpdateStaticRoute> staticRoutes = new ArrayList<UpdateStaticRoute>();

  /**
   * Getting VRF ID.
   *
   * @return vrfId
   */
  public String getVrfId() {
    return vrfId;
  }

  /**
   * Setting VRF ID.
   *
   * @param vrfId
   *          set vrfId
   */
  public void setVrfId(String vrfId) {
    this.vrfId = vrfId;
  }

  /**
   * Getting static route information list.
   *
   * @return staticRoutes
   */
  public ArrayList<UpdateStaticRoute> getStaticRoutes() {
    return staticRoutes;
  }

  /**
   * Setting static route information list.
   *
   * @param staticRoutes
   *          set staticRoutes
   */
  public void setStaticRoutes(ArrayList<UpdateStaticRoute> staticRoutes) {
    this.staticRoutes = staticRoutes;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "L3VlanOption [vrfId=" + vrfId + ", staticRoutes=" + staticRoutes + "]";
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
    if (vrfId == null) {
      throw new CheckDataException();
    }
    if (staticRoutes.isEmpty()) {
      throw new CheckDataException();
    } else {
      for (UpdateStaticRoute sr : staticRoutes) {
        sr.check(ope);
      }
    }
  }

}
