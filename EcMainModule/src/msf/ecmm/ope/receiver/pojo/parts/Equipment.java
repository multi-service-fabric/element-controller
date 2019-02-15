/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import java.util.ArrayList;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * Model Information.
 */
public class Equipment {

  /** Device ID. */
  private String equipmentTypeId;

  /** LagIF Name Prefix. */
  private String lagPrefix;

  /** Unit IF Connector. */
  private String unitConnector;

  /** IF Name Acquisition MIB Information. */
  private String ifNameOid;

  /** IF Name Acquisition MIB Information in SNMPTrap. */
  private String snmptrapIfNameOid;

  /** Max. number of pieces of information acquired with GETBULK at a time. */
  private Integer maxRepetitions;

  /** Platform Name. */
  private String platform;

  /** OS Name. */
  private String os;

  /** Firmware Version. */
  private String firmware;

  /** Router Type. */
  private String routerType;

  /** Physical IF Name Generation Format. */
  private String physicalIfNameSyntax;

  /** IF Name Generation Format after breakout. */
  private String breakoutIfNameSyntax;

  /** IF Name Suffix List after breakout. */
  private String breakoutIfNameSuffixList;

  /** Physical IF Naming Convention Information. */
  private ArrayList<IfNameRule> ifNameRules = new ArrayList<IfNameRule>();

  /** Device IF Information. */
  private ArrayList<EquipmentIf> equipmentIfs = new ArrayList<EquipmentIf>();

  /** Information for ztp. */
  private Ztp ztp;

  /** Type of configurable device. */
  private Capabilities capabilities;

  /** QoS Capability. */
  private QosRegisterEquipment qos;

  /** Total value exixtance flag for the same VLAN number traffic acquisition. */
  private Boolean sameVlanNumberTrafficTotalValueFlag;

  /** VLAN traffic information acquisition method. */
  private String vlanTrafficCapability;

  /** VLAN traffic counter name acquisition extension MIB information. */
  private String vlanTrafficCounterNameMibOid;

  /** VLAN traffic counter value acquisition extension MIB information. */
  private String vlanTrafficCounterValueMibOid;

  /** CLI command execution and result analysis shell script path. */
  private String cliExecPath;

  /**
   * Acquiring model ID.
   *
   * @return Model ID
   */
  public String getEquipmentTypeId() {
    return equipmentTypeId;
  }

  /**
   * Setting device ID.
   *
   * @param equipmentTypeId
   *          device ID
   */
  public void setEquipmentTypeId(String equipmentTypeId) {
    this.equipmentTypeId = equipmentTypeId;
  }

  /**
   * Getting LagIF name prefix.
   *
   * @return LagIF name prefix
   */
  public String getLagPrefix() {
    return lagPrefix;
  }

  /**
   * Setting LagIF name prefix.
   *
   * @param lagPrefix
   *          LagIF name prefix
   */
  public void setLagPrefix(String lagPrefix) {
    this.lagPrefix = lagPrefix;
  }

  /**
   * Getting unit IF connector.
   *
   * @return unit IF connector
   */
  public String getUnitConnector() {
    return unitConnector;
  }

  /**
   * Setting unit IF connector.
   *
   * @param unitConnector
   *          unit IF connector
   */
  public void setUnitConnector(String unitConnector) {
    this.unitConnector = unitConnector;
  }

  /**
   * Getting IF name acquisition MIB information.
   *
   * @return IF anem acquisition MIB information
   */
  public String getIfNameOid() {
    return ifNameOid;
  }

  /**
   * Setting IF name acquisition MIB information.
   *
   * @param ifNameOid
   *          IF name acquisition MIB information
   */
  public void setIfNameOid(String ifNameOid) {
    this.ifNameOid = ifNameOid;
  }

  /**
   * Getting IF name acquisition MIB information in SNMPTrap.
   *
   * @return IF name acquisition MIB information in SNMPTrap
   */
  public String getSnmptrapIfNameOid() {
    return snmptrapIfNameOid;
  }

  /**
   * Setting IF name acquisition MIB information in SNMPTrap.
   *
   * @param snmptrapIfNameOid
   *          IF name acquisition MIB information in SNMPTrap
   */
  public void setSnmptrapIfNameOid(String snmptrapIfNameOid) {
    this.snmptrapIfNameOid = snmptrapIfNameOid;
  }

  /**
   * Getting Max. number of pieces of information acquired with GETBULK at a time.
   *
   * @return Max. number of pieces of information acquired with GETBULK at a time
   */
  public Integer getMaxRepetitions() {
    return maxRepetitions;
  }

  /**
   * Setting Max. number of pieces of information acquired with GETBULK at a time.
   *
   * @param maxRepetitions
   *          Max. number of pieces of information acquired with GETBULK at a time
   */
  public void setMaxRepetitions(Integer maxRepetitions) {
    this.maxRepetitions = maxRepetitions;
  }

  /**
   * Getting platform name.
   *
   * @return platform name
   */
  public String getPlatform() {
    return this.platform;
  }

  /**
   * Setting platform name.
   *
   * @param platform
   *          platform name
   */
  public void setPlatform(String platform) {
    this.platform = platform;
  }

  /**
   * Getting OS name.
   *
   * @return OS name
   */
  public String getOs() {
    return this.os;
  }

  /**
   * Setting OS name.
   *
   * @param os
   *          OS name
   */
  public void setOs(String os) {
    this.os = os;
  }

  /**
   * Getting firmware version.
   *
   * @return firmware version
   */
  public String getFirmware() {
    return this.firmware;
  }

  /**
   * Setting firmware version.
   *
   * @param firmware
   *          firmware version
   */
  public void setFirmware(String firmware) {
    this.firmware = firmware;
  }

  /**
   * Getting router type.
   *
   * @return router type
   */
  public String getRouterType() {
    return this.routerType;
  }

  /**
   * Setting router type.
   *
   * @param routerType
   *          router type
   */
  public void setRouterType(String routerType) {
    this.routerType = routerType;
  }

  /**
   * Gettin physical IF name generation format.
   *
   * @return physical IF name generation format
   */
  public String getPhysicalIfNameSyntax() {
    return this.physicalIfNameSyntax;
  }

  /**
   * Setting physical IF name generation format.
   *
   * @param physicalIfNameSyntax
   *          physical IF name generation format
   */
  public void setPhysicalIfNameSyntax(String physicalIfNameSyntax) {
    this.physicalIfNameSyntax = physicalIfNameSyntax;
  }

  /**
   * Getting IF name generation format after breakout.
   *
   * @return IF name generation format after breakout
   */
  public String getBreakoutIfNameSyntax() {
    return this.breakoutIfNameSyntax;
  }

  /**
   * Setting IF name generation format after breakout.
   *
   * @param breakoutIfNameSyntax
   *          IF name generation format after breakout
   */
  public void setBreakoutIfNameSyntax(String breakoutIfNameSyntax) {
    this.breakoutIfNameSyntax = breakoutIfNameSyntax;
  }

  /**
   * Getting IF name suffix list after breakout.
   *
   * @return IF name suffix list after breakout
   */
  public String getBreakoutIfNameSuffixList() {
    return this.breakoutIfNameSuffixList;
  }

  /**
   * Setting IF name suffix list after breakout.
   *
   * @param breakoutIfNameSuffixList
   *          IF name suffix list after breakout
   */
  public void setBreakoutIfNameSuffixList(String breakoutIfNameSuffixList) {
    this.breakoutIfNameSuffixList = breakoutIfNameSuffixList;
  }

  /**
   * Getting physical IF naming convention information.
   *
   * @return physical IF naming convention information
   */
  public ArrayList<IfNameRule> getIfNameRules() {
    return ifNameRules;
  }

  /**
   * Setting physical IF naming convention information.
   *
   * @param ifNameRules
   *          physical IF naming convention information
   */
  public void setIfNameRules(ArrayList<IfNameRule> ifNameRules) {
    this.ifNameRules = ifNameRules;
  }

  /**
   * Getting model IF information.
   *
   * @return model IF information
   */
  public ArrayList<EquipmentIf> getEquipmentIfs() {
    return equipmentIfs;
  }

  /**
   * Setting model IF information.
   *
   * @param equipmentIfs
   *          model IF information
   */
  public void setEquipmentIfs(ArrayList<EquipmentIf> equipmentIfs) {
    this.equipmentIfs = equipmentIfs;
  }

  /**
   * Getting information for Ztp.
   *
   * @return information for Ztp
   */
  public Ztp getZtp() {
    return this.ztp;
  }

  /**
   * Setting information for Ztp.
   *
   * @param ztp
   *          information for Ztp
   */
  public void setZtp(Ztp ztp) {
    this.ztp = ztp;
  }

  /**
   * Getting configurable device type.
   *
   * @return configurable device type
   */
  public Capabilities getCapabilities() {
    return capabilities;
  }

  /**
   * Setting configurable device type.
   *
   * @param capabilities
   *          configurable device type
   */
  public void setCapabilities(Capabilities capabilities) {
    this.capabilities = capabilities;
  }

  /**
   * Getting QoS Capability.
   *
   * @return qos
   */
  public QosRegisterEquipment getQos() {
    return qos;
  }

  /**
   * Setting QoS Capability.
   *
   * @param qos
   *          setting  qos
   */
  public void setQos(QosRegisterEquipment qos) {
    this.qos = qos;
  }

  /**
   * Getting total value existance flag for the same VLAN number traffic.
   *
   * @return sameVlanNumberTrafficTotalValueFlag
   */
  public Boolean getSameVlanNumberTrafficTotalValueFlag() {
    return sameVlanNumberTrafficTotalValueFlag;
  }

  /**
   * Setting total value existance flag for the same VLAN number traffic.
   *
   * @param sameVlanNumberTrafficTotalValueFlag
   *          Setting sameVlanNumberTrafficTotalValueFlag
   */
  public void setSameVlanNumberTrafficTotalValueFlag(Boolean sameVlanNumberTrafficTotalValueFlag) {
    this.sameVlanNumberTrafficTotalValueFlag = sameVlanNumberTrafficTotalValueFlag;
  }

  /**
   * Getting VLAN traffic information acquisition method.
   *
   * @return vlanTrafficCapability
   */
  public String getVlanTrafficCapability() {
    return vlanTrafficCapability;
  }

  /**
   * Setting VLAN traffic information acquisition method.
   *
   * @param vlanTrafficCapability
   *          Setting vlanTrafficCapability
   */
  public void setVlanTrafficCapability(String vlanTrafficCapability) {
    this.vlanTrafficCapability = vlanTrafficCapability;
  }

  /**
   * Getting VLAN traffic counter name acquisition extension MIB information.
   *
   * @return vlanTrafficCounterNameMibOid
   */
  public String getVlanTrafficCounterNameMibOid() {
    return vlanTrafficCounterNameMibOid;
  }

  /**
   * Setting VLAN traffic counter name acquisition extention MIB information.
   *
   * @param vlanTrafficCounterNameMibOid
   *          Setting vlanTrafficCounterNameMibOid
   */
  public void setVlanTrafficCounterNameMibOid(String vlanTrafficCounterNameMibOid) {
    this.vlanTrafficCounterNameMibOid = vlanTrafficCounterNameMibOid;
  }

  /**
   * Getting VLAN traffic counter value acquisition extension MIB information.
   *
   * @return vlanTrafficCounterValueMibOid
   */
  public String getVlanTrafficCounterValueMibOid() {
    return vlanTrafficCounterValueMibOid;
  }

  /**
   * Setting VLAN traffic counter value acquisition extension MIB information.
   *
   * @param vlanTrafficCounterValueMibOid
   *          Setting vlanTrafficCounterValueMibOid
   */
  public void setVlanTrafficCounterValueMibOid(String vlanTrafficCounterValueMibOid) {
    this.vlanTrafficCounterValueMibOid = vlanTrafficCounterValueMibOid;
  }

  /**
   * Getting CLI command execution and result analysis shell script path.
   *
   * @return cliExecPath
   */
  public String getCliExecPath() {
    return cliExecPath;
  }

  /**
   * Setting CLI command execution and result analysis shell script path.
   *
   * @param cliExecPath
   *          Setting cliExecPath
   */
  public void setCliExecPath(String cliExecPath) {
    this.cliExecPath = cliExecPath;
  }

  /* (Non Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "Equipment [equipmentTypeId=" + equipmentTypeId + ", lagPrefix=" + lagPrefix + ", unitConnector="
        + unitConnector + ", ifNameOid=" + ifNameOid + ", snmptrapIfNameOid=" + snmptrapIfNameOid + ", maxRepetitions="
        + maxRepetitions + ", platform=" + platform + ", os=" + os + ", firmware=" + firmware + ", routerType="
        + routerType + ", physicalIfNameSyntax=" + physicalIfNameSyntax + ", breakoutIfNameSyntax="
        + breakoutIfNameSyntax + ", breakoutIfNameSuffixList=" + breakoutIfNameSuffixList + ", ifNameRules="
        + ifNameRules + ", equipmentIfs=" + equipmentIfs + ", ztp=" + ztp + ", capabilities=" + capabilities + ", qos="
        + qos + ", sameVlanNumberTrafficTotalValueFlag=" + sameVlanNumberTrafficTotalValueFlag
        + ", vlanTrafficCapability=" + vlanTrafficCapability + ", vlanTrafficCounterNameMibOid="
        + vlanTrafficCounterNameMibOid + ", vlanTrafficCounterValueMibOid=" + vlanTrafficCounterValueMibOid
        + ", cliExecPath=" + cliExecPath + "]";
  }

  /**
   * Input Parametr Check.
   *
   * @param operationType
   *          operation type
   * @throws CheckDataException
   *           input check error
   */
  public void check(OperationType operationType) throws CheckDataException {

    if (equipmentTypeId == null) {
      throw new CheckDataException();
    }
    if (lagPrefix == null) {
      throw new CheckDataException();
    }
    if (unitConnector == null) {
      throw new CheckDataException();
    }
    if (ifNameOid == null) {
      throw new CheckDataException();
    }
    if (maxRepetitions == null) {
      throw new CheckDataException();
    }
    if (platform == null) {
      throw new CheckDataException();
    }
    if (os == null) {
      throw new CheckDataException();
    }
    if (firmware == null) {
      throw new CheckDataException();
    }
    if (routerType == null) {
      throw new CheckDataException();
    } else {
      if (!(routerType.equals(CommonDefinitions.ROUTER_TYPE_NORMAL_STRING))
          && !(routerType.equals(CommonDefinitions.ROUTER_TYPE_COREROUTER_STRING))) {
        throw new CheckDataException();
      }
    }
    if (routerType.equals(CommonDefinitions.ROUTER_TYPE_COREROUTER_STRING) && physicalIfNameSyntax == null) {
      throw new CheckDataException();
    }

    if (ztp != null) {
      ztp.check(operationType);
    }

    if (qos == null) {
      throw new CheckDataException();
    } else {
      qos.check(operationType);
    }

    if ((ifNameRules == null) || ifNameRules.isEmpty()) {
      throw new CheckDataException();
    } else {
      for (IfNameRule ifNameRule : ifNameRules) {
        ifNameRule.check(operationType);
      }
    }

    if ((equipmentIfs == null) || equipmentIfs.isEmpty()) {
      throw new CheckDataException();
    } else {
      for (EquipmentIf equipmentIf : equipmentIfs) {
        equipmentIf.check(operationType);
      }
    }

    if (null != vlanTrafficCapability) {
      if (vlanTrafficCapability.equals(CommonDefinitions.VLAN_TRAFFIC_TYPE_MIB)) {
        if (null == vlanTrafficCounterNameMibOid || null == vlanTrafficCounterValueMibOid) {
          throw new CheckDataException();
        }
      }

      if (vlanTrafficCapability.equals(CommonDefinitions.VLAN_TRAFFIC_TYPE_CLI)) {
        if (null == cliExecPath) {
          throw new CheckDataException();
        }
      }
    }

  }
}
