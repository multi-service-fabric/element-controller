/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
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
  private ArrayList<UpdateStaticRoute> staticRoutes = null;

  /** QoS Configuration Infomation. */
  private QosUpdateVlanIf qos = null;

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
   * Getting QoS Configuration Infomation.
   *
   * @return qos
   */
  public QosUpdateVlanIf getQos() {
    return qos;
  }

  /**
   * Setting QoS Configuration Infomation.
   *
   * @param qos
   *          setting qos
   */
  public void setQos(QosUpdateVlanIf qos) {
    this.qos = qos;
  }

  /*
   * Stringizing Instance.
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "L3VlanOption [vrfId=" + vrfId + ", staticRoutes=" + staticRoutes + ", qos=" + qos + "]";
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
    if (staticRoutes == null && qos == null) {
      throw new CheckDataException();
    }

    if (staticRoutes != null) {
      for (UpdateStaticRoute sr : staticRoutes) {
        sr.check(ope);
      }
    }

    if (qos != null) {
      qos.check(ope);
    }
  }

}
