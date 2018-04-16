/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * VLANIF Change QoS Configuration Class.
 */
public class QosUpdateVlanIf {

  /** Operation Type. */
  private String operationType = null;

  /** ShapingRate (inflow). */
  private Float inflowShapingRate = null;

  /** ShapingRate (outflow). */
  private Float outflowShapingRate = null;

  /** Egress Queue Ratio Menu. */
  private String egressQueue = null;

  /**
   *  Getting Operation Type.
   *
   * @return operationType
   */
  public String getOperationType() {
    return operationType;
  }

  /**
   * Setting Operation Type.
   *
   * @param operationType
   *          setting operationType
   */
  public void setOperationType(String operationType) {
    this.operationType = operationType;
  }

  /**
   *  Getting ShapingRate (inflow).
   *
   * @return inflowShapingRate
   */
  public Float getInflowShapingRate() {
    return inflowShapingRate;
  }

  /**
   * Setting ShapingRate (inflow).
   *
   * @param inflowShapingRate
   *          set inflowShapingRate
   */
  public void setInflowShapingRate(Float inflowShapingRate) {
    this.inflowShapingRate = inflowShapingRate;
  }

  /**
   * Getting ShapingRate (outflow).
   *
   * @return outflowShapingRate
   */
  public Float getOutflowShapingRate() {
    return outflowShapingRate;
  }

  /**
   * Setting ShapingRate (outflow).
   *
   * @param outflowShapingRate
   *          setting outflowShapingRate
   */
  public void setOutflowShapingRate(Float outflowShapingRate) {
    this.outflowShapingRate = outflowShapingRate;
  }

  /**
   * Getting Egress Queue Ratio Menu.
   *
   * @return egressQueue
   */
  public String getEgressQueue() {
    return egressQueue;
  }

  /**
   * Setting Egress Queue Ratio Menu.
   *
   * @param egressQueue
   *          setting egressQueue
   */
  public void setEgressQueue(String egressQueue) {
    this.egressQueue = egressQueue;
  }

  /*
   * Stringizing Instance
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "QosUpdateVlanIf [operationType=" + operationType + ", inflowShapingRate=" + inflowShapingRate
        + ", outflowShapingRate=" + outflowShapingRate + ", egressQueue=" + egressQueue + "]";
  }

  /**
   * Input Parameter Check.
   *
   * @param ope
   *          operation type
   * @throws CheckDataException
   *          input check error
   */
  public void check(OperationType ope) throws CheckDataException {
    if (operationType == null) {
      throw new CheckDataException();
    }
    if (!operationType.equals(CommonDefinitions.OPERATION_TYPE_ADD)) {
      throw new CheckDataException();
    }
  }

}
