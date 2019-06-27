/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * List Information of VLAN IF to be controlled with batch generation.
 */
public class CreateVlanIfs {

  /** ID of VLAN IF to be controlled. */
  private String vlanIfId = null;

  /** Device ID. */
  private String nodeId = null;

  /** VLAN IF Creation Destination IF Information. */
  private BaseIfInfo baseIf = null;

  /** VLAN ID. */
  private Integer vlanId = null;

  /** VLAN Port Mode. */
  private String portMode = null;

  /** ESI Value. */
  private String esi = null;

  /** LACP system-id. */
  private String lacpSystemId = null;

  /** Clag ID. */
  private Integer clagId = null;

  /** QoS configuration. */
  private QosValues qos = null;

  /** IRB instance configuration. */
  private IrbValue irb = null;

  /** Route Distingusher. */
  private String routeDistinguisher = null;

  /** dummy VLAN flag. */
  private Boolean isDummy = null;

  /** multihoming configuration information. */
  private MultiHomingValue multiHoming = null;

  /**
   * Getting ID information of CP to be controlled.
   *
   * @return ID information of CP to be controlled
   */
  public String getVlanIfId() {
    return vlanIfId;
  }

  /**
   * Setting ID information of CP to be controlled.
   *
   * @param vlanIfId
   *          ID information of CP to be controlled
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
   * Getting VLAN IF creation destination IF information.
   *
   * @return VLAN IF creation destination IF information
   */
  public BaseIfInfo getBaseIf() {
    return baseIf;
  }

  /**
   * Setting VLAN IF creation destination IF information.
   *
   * @param baseIf
   *          VLAN IF creation destination IF information
   */
  public void setBaseIf(BaseIfInfo baseIf) {
    this.baseIf = baseIf;
  }

  /**
   * Getting VLAN ID.
   *
   * @return VLAN ID of CP
   */
  public Integer getVlanId() {
    return vlanId;
  }

  /**
   * Setting VLAN ID.
   *
   * @param vlanId
   *          VLAN ID of CP
   */
  public void setVlanId(Integer vlanId) {
    this.vlanId = vlanId;
  }

  /**
   * Getting VLAN port mode.
   *
   * @return portMode
   */
  public String getPortMode() {
    return portMode;
  }

  /**
   * Setting VLAN port mode.
   *
   * @param mode
   *          set portMode
   */
  public void setPortMode(String mode) {
    this.portMode = mode;
  }

  /**
   * Getting ESI value.
   *
   * @return ESI value
   */
  public String getEsi() {
    return esi;
  }

  /**
   * Setting ESI value.
   *
   * @param esi
   *          ESI value
   */
  public void setEsi(String esi) {
    this.esi = esi;
  }

  /**
   * Getting LACP system-id.
   *
   * @return LACP system-id
   */
  public String getLacpSystemId() {
    return lacpSystemId;
  }

  /**
   * Setting LACP system-id.
   *
   * @param systemId
   *          LACP system-id
   */
  public void setLacpSystemId(String systemId) {
    this.lacpSystemId = systemId;
  }

  /**
   * Getting Clag Id.
   *
   * @return clagId
   */
  public Integer getClagId() {
    return clagId;
  }

  /**
   * Setting Clag Id.
   *
   * @param clagId
   *          setting clagId
   */
  public void setClagId(Integer clagId) {
    this.clagId = clagId;
  }

  /**
   * Getting QoS configuration.
   *
   * @return qos
   */
  public QosValues getQos() {
    return qos;
  }

  /**
   * Setting QoS configuration.
   *
   * @param qos
   *          Setting qos
   */
  public void setQos(QosValues qos) {
    this.qos = qos;
  }

  /**
   * Getting IRB instance configuraiton.
   *
   * @return irb
   */
  public IrbValue getIrbValue() {
    return irb;
  }

  /**
   * Setting IRB instance configuration.
   *
   * @param irb
   *          Setting irb
   */
  public void setIrbValue(IrbValue irb) {
    this.irb = irb;
  }

  /**
   * Getting Route Distingusher.
   *
   * @return routeDistingusher
   */
  public String getRouteDistingusher() {
    return routeDistinguisher;
  }

  /**
   * Setting Route Distingusher.
   *
   * @param routeDistingusher
   *          Setting routeDistingusher
   */
  public void setRouteDistingusher(String routeDistinguisher) {
    this.routeDistinguisher = routeDistinguisher;
  }

  /**
   * Getting dummy VLAN flag.
   *
   * @return isDummy
   */
  public Boolean getIsDummy() {
    return isDummy;
  }

  /**
   * Setting dummy VLAN flag.
   *
   * @param isDummy
   *          Setting isDummy
   */
  public void setIsDummy(Boolean isDummy) {
    this.isDummy = isDummy;
  }

  /**
   * Getting multihoming configuraiton information.
   *
   * @return multiHoming
   */
  public MultiHomingValue getMultiHomingValue() {
    return multiHoming;
  }

  /**
   * Setting multihoming configuration information.
   *
   * @param multiHoming
   *          Setting multiHoming
   */
  public void setMultiHomingValue(MultiHomingValue multiHoming) {
    this.multiHoming = multiHoming;
  }

  /* (Non Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "CreateVlanIfs [vlanIfId=" + vlanIfId + ", nodeId=" + nodeId + ", baseIf=" + baseIf + ", vlanId=" + vlanId
        + ", portMode=" + portMode + ", esi=" + esi + ", lacpSystemId=" + lacpSystemId + ", clagId=" + clagId
        + ", qos=" + qos + ", irb=" + irb + ", routeDistinguisher=" + routeDistinguisher + ", isDummy=" + isDummy
        + ", multiHoming=" + multiHoming + "]";
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
    if (vlanIfId == null) {
      throw new CheckDataException();
    }
    if (nodeId == null) {
      throw new CheckDataException();
    }
    if (baseIf == null && isDummy == false) {
      throw new CheckDataException();
    } else if (baseIf != null) {
      baseIf.check(ope);
    }
    if (vlanId == null) {
      throw new CheckDataException();
    }
    if (null != isDummy && isDummy == false && portMode == null) {
      throw new CheckDataException();
    }

    if (irb != null) {
      irb.check(ope);
    }

    if (multiHoming != null) {
      multiHoming.check(ope);
    }
  }

}
