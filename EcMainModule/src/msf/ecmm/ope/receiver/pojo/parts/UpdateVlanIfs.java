/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * List Information of VLANIF of which ESI value is to be changed.
 */
public class UpdateVlanIfs {

  /** ID of VLAN IF to be controlled. */
  private String vlanIfId = null;

  /** Device ID. */
  private String nodeId = null;

  /** IF information to which VLAN IF is created */
  private BaseIfCreateVlanIf baseIf = null;

  /** ESI value. */
  private String esi = null;

  /** LACP system-id. */
  private String lacpSystemId = null;

  /** clag-id value to be configured. */
  private Integer clagId = null;

  /** VLAN port mode. */
  private String portMode = null;

  /** QOS configuration get set. */
  private QosValues qos = null;

  /** IRB instance configuration. */
  private IrbUpdateValue irb = null;

  /**
   * Getting the ID of VLAN IF to be controlled.
   *
   * @return vlanIfId
   */
  public String getVlanIfId() {
    return vlanIfId;
  }

  /**
   * Setting the ID of VLAN IF to be controlled.
   *
   * @param vlanIfId
   *          set vlanIfId
   */
  public void setVlanIfId(String vlanIfId) {
    this.vlanIfId = vlanIfId;
  }

  /**
   * Getting device ID.
   *
   * @return nodeId
   */
  public String getNodeId() {
    return nodeId;
  }

  /**
   * Setting device ID.
   *
   * @param nodeId
   *          set nodeId
   */
  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  /**
   * Getting IF information to which VLAN IF is created.
   *
   * @return baseIf
   */
  public BaseIfCreateVlanIf getBaseIf() {
    return baseIf;
  }

  /**
   * Setting IF information to which VLAN IF is created.
   *
   * @param baseIf
   *          Setting baseIf
   */
  public void setBaseIf(BaseIfCreateVlanIf baseIf) {
    this.baseIf = baseIf;
  }

  /**
   * Getting ESI value.
   *
   * @return esi
   */
  public String getEsi() {
    return esi;
  }

  /**
   * Setting ESI value.
   *
   * @param esi
   *          set esi
   */
  public void setEsi(String esi) {
    this.esi = esi;
  }

  /**
   * Getting LACP system-id.
   *
   * @return lacpSystemId
   */
  public String getLacpSystemId() {
    return lacpSystemId;
  }

  /**
   * Setting LACP system-id.
   *
   * @param lacpSystemId
   *          set lacpSystemId
   */
  public void setLacpSystemId(String lacpSystemId) {
    this.lacpSystemId = lacpSystemId;
  }

  /**
   * Getting clag-id value.
   *
   * @return clagId
   */
  public Integer getClagId() {
    return clagId;
  }

  /**
   * Setting clag-id value.
   *
   * @param clagId
   *          Setting clagId
   */
  public void setClagId(Integer clagId) {
    this.clagId = clagId;
  }

  /**
   * Getting portMode.
   *
   * @return portMode
   */
  public String getPortMode() {
    return portMode;
  }

  /**
   * Setting portMode.
   *
   * @param portMode
   *          Setting portMode
   */
  public void setPortMode(String portMode) {
    this.portMode = portMode;
  }

  /**
   * Getting QOS configuration.
   *
   * @return qos
   */
  public QosValues getQos() {
    return qos;
  }

  /**
   * Setting QOS configuration.
   *
   * @param qos
   *          Setting qos
   */
  public void setQos(QosValues qos) {
    this.qos = qos;
  }

  /**
   * Getting IRB instance information.
   *
   * @return irb
   */
  public IrbUpdateValue getIrbValue() {
    return irb;
  }

  /**
   * Setting IRB instance information.
   *
   * @param irb
   *          Setting irb
   */
  public void setIrbValue(IrbUpdateValue irb) {
    this.irb = irb;
  }

  /* (Non Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "UpdateVlanIfs [vlanIfId=" + vlanIfId + ", nodeId=" + nodeId + ", baseIf=" + baseIf + ", esi=" + esi
        + ", lacpSystemId=" + lacpSystemId + ", clagId=" + clagId + ", portMode=" + portMode + ", qos=" + qos
        + ", irb=" + irb + "]";
  }

  /**
   * Input parameter check.
   *
   * @param ope
   *          Operation type
   * @throws CheckDataException
   *           Input check error
   */
  public void check(OperationType ope) throws CheckDataException {
    if (vlanIfId == null) {
      throw new CheckDataException();
    }
    if (nodeId == null) {
      throw new CheckDataException();
    }
    if (baseIf != null) {
      baseIf.check(ope);
    }
    if (irb != null) {
      irb.check(ope);
    }
  }

}
