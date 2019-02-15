/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.db.pojo;

import java.io.Serializable;
import java.util.Set;

/**
 * Model Information Table POJO Class.
 */
public class Equipments implements Serializable {

  /** Model ID. */
  private String equipment_type_id = null;
  /** LagIF Name Prefix. */
  private String lag_prefix = null;
  /** Unit IF Connector. */
  private String unit_connector = null;
  /** IF Name Acquisition MIB Information. */
  private String if_name_oid = null;
  /** IF Name Acquisition MIB Information in SNMPTrap. */
  private String snmptrap_if_name_oid = null;
  /** The Max. Number of Acquiring SNMP GETBULK. */
  private int max_repetitions = 0;
  /** Platform Name. */
  private String platform_name = null;
  /** OS Name. */
  private String os_name = null;
  /** Firmware Version Information. */
  private String firmware_version = null;
  /** Router Type. */
  private int router_type = 0;
  /** Core Router's Physical IF Name Information. */
  private String core_router_physical_if_name_format = null;
  /** IF Name Generation After Breakout Format. */
  private String breakout_if_name_syntax = null;
  /** IF Name Prefix After Breakout List. */
  private String breakout_if_name_suffix_list = null;
  /** dhcp.conf Template File Path. */
  private String dhcp_template = null;
  /** Device Initial Injection Configuration Template File Path. */
  private String config_template = null;
  /** Device Initial Injection Configuration File Path. */
  private String initial_config = null;
  /** Start-up Complete Determination Message. */
  private String boot_complete_msg = null;
  /** EVPN Response Feasibility. */
  private Boolean evpn_capability;
  /** L2VPN Configuration Feasibility. */
  private Boolean l2vpn_capability;
  /** L3VPN Configuration Feasibility. */
  private Boolean l3vpn_capability;
  /** QoS in/out-flow shaping rate Configuration Feasibility. */
  private Boolean qos_shaping_flg = null;
  /** QoS Remark menu Configuration Feasibility. */
  private Boolean qos_remark_flg = null;
  /** Remark menu default value. */
  private String default_remark_menu = null;
  /** Egress queue menu default menu. */
  private String default_egress_queue_menu = null;
  /** Identical VLAN number traffic aquisition total value existence flag. */
  private Boolean same_vlan_number_traffic_total_value_flag;
  /** VLAN traffic information acquisition method. */
  private String vlan_traffic_capability = null;
  /** VLAN traffic counter name acquisition extension MIB information. */
  private String vlan_traffic_counter_name_mib_oid = null;
  /** VLAN traffic counter value acquisition extension MIB information. */
  private String vlan_traffic_counter_value_mib_oid = null;
  /** CLI command execution and result analysis shell script path. */
  private String cli_exec_path = null;
  /** IRB Asymmetric setting feasibility. */
  private Boolean irb_asymmetric_capability;
  /** IRB Symmetric setting feasibility. */
  private Boolean irb_symmetric_capability;
  /** Model IF information list. */
  private Set<EquipmentIfs> equipmentIfsList;
  /** Physical IF Naming Convention Information. */
  private Set<IfNameRules> ifNameRulesList;
  /** Start-up Failure Determination Message Information. */
  private Set<BootErrorMessages> bootErrorMessagesList;
  /** Remark menu Information. */
  private Set<RemarkMenus> remarkMenusList;
  /** Egress queue menu Information. */
  private Set<EgressQueueMenus> egressQueueMenusList;

  /**
   * Generating new instance.
   */
  public Equipments() {
    super();
  }

  /**
   * Getting model ID.
   *
   * @return model ID
   */
  public String getEquipment_type_id() {
    return equipment_type_id;
  }

  /**
   * Setting model ID.
   *
   * @param equipment_type_id
   *          model ID
   */
  public void setEquipment_type_id(String equipment_type_id) {
    this.equipment_type_id = equipment_type_id;
  }

  /**
   * Getting LagIF name prefix.
   *
   * @return LagIF name prefix
   */
  public String getLag_prefix() {
    return lag_prefix;
  }

  /**
   * Setting LagIF name prefix.
   *
   * @param lag_prefix
   *          LagIF name prefix
   */
  public void setLag_prefix(String lag_prefix) {
    this.lag_prefix = lag_prefix;
  }

  /**
   * Getting unit IF connecor.
   *
   * @return unit IF connector
   */
  public String getUnit_connector() {
    return unit_connector;
  }

  /**
   * Setting unit IF connector.
   *
   * @param unit_connector
   *          unit IF connector
   */
  public void setUnit_connector(String unit_connector) {
    this.unit_connector = unit_connector;
  }

  /**
   * Getting IF name acquisition MIB information.
   *
   * @return IF name acquisition MIB information
   */
  public String getIf_name_oid() {
    return if_name_oid;
  }

  /**
   * Setting IF name acquisition MIB information.
   *
   * @param if_name_oid
   *          IF name acquisition MIB information
   */
  public void setIf_name_oid(String if_name_oid) {
    this.if_name_oid = if_name_oid;
  }

  /**
   * Getting IF name acquisition MIB information in SNMPTrap.
   *
   * @return IF name acquisition MIB information in SNMPTrap
   */
  public String getSnmptrap_if_name_oid() {
    return snmptrap_if_name_oid;
  }

  /**
   * Setting IF name acquisition MIB information in SNMPTrap.
   *
   * @param snmptrap_if_name_oid
   *          IF name acquisition MIB information in SNMPTrap
   */
  public void setSnmptrap_if_name_oid(String snmptrap_if_name_oid) {
    this.snmptrap_if_name_oid = snmptrap_if_name_oid;
  }

  /**
   * Getting the max. number of acquiring SNMP GETBULK.
   *
   * @return the max. number of acquiring SNMP GETBULK
   */
  public int getMax_repetitions() {
    return max_repetitions;
  }

  /**
   * Setting the max. number of acquiring SNMP GETBULK.
   *
   * @param max_repetitions
   *          the max. number of acquiring SNMP GETBULK
   */
  public void setMax_repetitions(int max_repetitions) {
    this.max_repetitions = max_repetitions;
  }

  /**
   * Getting platform name.
   *
   * @return platform name
   */
  public String getPlatform_name() {
    return platform_name;
  }

  /**
   * Setting platform name.
   *
   * @param platform_name
   *          platform name
   */
  public void setPlatform_name(String platform_name) {
    this.platform_name = platform_name;
  }

  /**
   * Getting OS name.
   *
   * @return OS name
   */
  public String getOs_name() {
    return os_name;
  }

  /**
   * Setting OS name.
   *
   * @param os_name
   *          OS name
   */
  public void setOs_name(String os_name) {
    this.os_name = os_name;
  }

  /**
   * Getting firmware version information.
   *
   * @return firmware version information
   */
  public String getFirmware_version() {
    return firmware_version;
  }

  /**
   * Setting firmware version information.
   *
   * @param firmware_version
   *          firmware version information
   */
  public void setFirmware_version(String firmware_version) {
    this.firmware_version = firmware_version;
  }

  /**
   * Getting router model information.
   *
   * @return router model information
   */
  public int getRouter_type() {
    return router_type;
  }

  /**
   * Setting router model information.
   *
   * @param router_type
   *          router model information
   */
  public void setRouter_type(int router_type) {
    this.router_type = router_type;
  }

  /**
   * Getting core router's physical IF name information.
   *
   * @return core router's physical IF name information
   */
  public String getCore_router_physical_if_name_format() {
    return core_router_physical_if_name_format;
  }

  /**
   * Setting core router's physical IF name information.
   *
   * @param core_router_physical_if_name_format
   *          core router's physical IF name information
   */
  public void setCore_router_physical_if_name_format(String core_router_physical_if_name_format) {
    this.core_router_physical_if_name_format = core_router_physical_if_name_format;
  }

  /**
   * Getting BreakoutIF syntax name.
   *
   * @return BreakoutIF syntax name
   */
  public String getBreakout_if_name_syntax() {
    return breakout_if_name_syntax;
  }

  /**
   * Setting BreakoutIF syntax name.
   *
   * @param breakout_if_name_syntax
   *          BreakoutIF syntax name
   */
  public void setBreakout_if_name_syntax(String breakout_if_name_syntax) {
    this.breakout_if_name_syntax = breakout_if_name_syntax;
  }

  /**
   * Getting BreakoutIF suffix name list.
   *
   * @return BreakoutIF suffix name list
   */
  public String getBreakout_if_name_suffix_list() {
    return breakout_if_name_suffix_list;
  }

  /**
   * Setting BreakoutIF suffix name list.
   *
   * @param breakout_if_name_suffix_list
   *          BreakoutIF suffix name list
   */
  public void setBreakout_if_name_suffix_list(String breakout_if_name_suffix_list) {
    this.breakout_if_name_suffix_list = breakout_if_name_suffix_list;
  }

  /**
   * Getting DHCP type information.
   *
   * @return DHCP type information
   */
  public String getDhcp_template() {
    return dhcp_template;
  }

  /**
   * Setting DHCP type information.
   *
   * @param dhcp_template
   *          DHCP type information
   */
  public void setDhcp_template(String dhcp_template) {
    this.dhcp_template = dhcp_template;
  }

  /**
   * Getting config type information.
   *
   * @return config type information
   */
  public String getConfig_template() {
    return config_template;
  }

  /**
   * Setting config type information.
   *
   *
   * @param config_template
   *          config type information
   */
  public void setConfig_template(String config_template) {
    this.config_template = config_template;
  }

  /**
   * Getting device initial injection configuration file path.
   *
   * @return device initial injection configuration file path.
   */
  public String getInitial_config() {
    return initial_config;
  }

  /**
   * Setting device initial injection configuration file path.
   *
   * @param initial_config
   *          device initial injection configuration file path
   */
  public void setInitial_config(String initial_config) {
    this.initial_config = initial_config;
  }

  /**
   * Getting start-up completion determination message.
   *
   * @return start-up completion determination message
   */
  public String getBoot_complete_msg() {
    return boot_complete_msg;
  }

  /**
   * Setting start-up completion determination message.
   *
   * @param boot_complete_msg
   *          start-up completion determination message
   */
  public void setBoot_complete_msg(String boot_complete_msg) {
    this.boot_complete_msg = boot_complete_msg;
  }

  /**
   * Getting EVPN application information.
   *
   * @return EVPN application information
   */
  public Boolean getEvpn_capability() {
    return evpn_capability;
  }

  /**
   * Setting EVPN application information.
   *
   * @param evpn_capability
   *          EVPN application information
   */
  public void setEvpn_capability(Boolean evpn_capability) {
    this.evpn_capability = evpn_capability;
  }

  /**
   * Getting L2VPN application information.
   *
   * @return L2VPN application information
   */
  public Boolean getL2vpn_capability() {
    return l2vpn_capability;
  }

  /**
   * Setting L2VPN application information.
   *
   * @param l2vpn_capability
   *          L2VPN application information
   */
  public void setL2vpn_capability(Boolean l2vpn_capability) {
    this.l2vpn_capability = l2vpn_capability;
  }

  /**
   * Getting L3VPN application information.
   *
   * @return L3VPN application information
   */
  public Boolean getL3vpn_capability() {
    return l3vpn_capability;
  }

  /**
   * Setting L3VPN application information.
   *
   * @param l3vpn_capability
   *          L3VPN application information
   */
  public void setL3vpn_capability(Boolean l3vpn_capability) {
    this.l3vpn_capability = l3vpn_capability;
  }

  /**
   * Getting QoS in/out-flow shaping rate Configuration Feasibility information.
   *
   * @return QoS in/out-flow shaping rate Configuration Feasibility information
   */
  public Boolean getQos_shaping_flg() {
    return qos_shaping_flg;
  }

  /**
   * Setting QoS in/out-flow shaping rate Configuration Feasibility information.
   *
   * @param qos_shaping_flg
   *          QoS in/out-flow shaping rate Configuration Feasibility information
   */
  public void setQos_shaping_flg(Boolean qos_shaping_flg) {
    this.qos_shaping_flg = qos_shaping_flg;
  }

  /**
   * Getting QoS Remark menu Configuration Feasibility information.
   *
   * @return QoS Remark menu Configuration Feasibility information
   */
  public Boolean getQos_remark_flg() {
    return qos_remark_flg;
  }

  /**
   * Setting QoS Remark menu Configuration Feasibility information.
   *
   * @param qos_remark_flg
   *          QoS Remark menu Configuration Feasibility information
   */
  public void setQos_remark_flg(Boolean qos_remark_flg) {
    this.qos_remark_flg = qos_remark_flg;
  }

  /**
   * Getting Remark menu default value information.
   *
   * @return Remark menu default value information
   */
  public String getDefault_remark_menu() {
    return default_remark_menu;
  }

  /**
   * Setting Remark menu default value information.
   *
   * @param default_remark_menu
   *          Remark menu default value information
   */
  public void setDefault_remark_menu(String default_remark_menu) {
    this.default_remark_menu = default_remark_menu;
  }

  /**
   * Getting Egress queue menu default menu information.
   *
   * @return Egress queue menu default menu information
   */
  public String getDefault_egress_queue_menu() {
    return default_egress_queue_menu;
  }

  /**
   * Setting Egress queue menu default menu information.
   *
   * @param default_egress_queue_menu
   *          Egress queue menu default menu information
   */
  public void setDefault_egress_queue_menu(String default_egress_queue_menu) {
    this.default_egress_queue_menu = default_egress_queue_menu;
  }

  /**
   * Getting model IF information list.
   *
   * @return same_vlan_number_traffic_total_value_flag
   */
  public Boolean getSame_vlan_number_traffic_total_value_flag() {
    return same_vlan_number_traffic_total_value_flag;
  }

  /**
   * Setting the same VLAN number traffic acquisition sum value existence flag.
   *
   * @param same_vlan_number_traffic_total_value_flag
   *          Setting same_vlan_number_traffic_total_value_flag
   */
  public void setSame_vlan_number_traffic_total_value_flag(Boolean same_vlan_number_traffic_total_value_flag) {
    this.same_vlan_number_traffic_total_value_flag = same_vlan_number_traffic_total_value_flag;
  }

  /**
   * Getting VLAN traffic information acquisition method.
   *
   * @return vlan_traffic_capability
   */
  public String getVlan_traffic_capability() {
    return vlan_traffic_capability;
  }

  /**
   * Setting VLAN traffic information acquisitioin method.
   *
   * @param vlan_traffic_capability
   *          Setting vlan_traffic_capability
   */
  public void setVlan_traffic_capability(String vlan_traffic_capability) {
    this.vlan_traffic_capability = vlan_traffic_capability;
  }

  /**
   * Getting VLAN traffic counter name acquisition extension MIB information.
   *
   * @return vlan_traffic_counter_name_mib_oid
   */
  public String getVlan_traffic_counter_name_mib_oid() {
    return vlan_traffic_counter_name_mib_oid;
  }

  /**
   * Setting VLAN traffic counter name acquisitioin extension information.
   *
   * @param vlan_traffic_counter_name_mib_oid
   *          Setting vlan_traffic_counter_name_mib_oid
   */
  public void setVlan_traffic_counter_name_mib_oid(String vlan_traffic_counter_name_mib_oid) {
    this.vlan_traffic_counter_name_mib_oid = vlan_traffic_counter_name_mib_oid;
  }

  /**
   * Getting VLAN traffic counter value acquisition extension MIB information.
   *
   * @return vlan_traffic_counter_value_mib_oid
   */
  public String getVlan_traffic_counter_value_mib_oid() {
    return vlan_traffic_counter_value_mib_oid;
  }

  /**
   * Setting VLAN traffic counter value acquisition extension MIB information.
   *
   * @param vlan_traffic_counter_value_mib_oid
   *          Setting vlan_traffic_counter_value_mib_oid
   */
  public void setVlan_traffic_counter_value_mib_oid(String vlan_traffic_counter_value_mib_oid) {
    this.vlan_traffic_counter_value_mib_oid = vlan_traffic_counter_value_mib_oid;
  }

  /**
   * Getting CLI command execution and result analysis shell script path.
   *
   * @return cli_exec_path
   */
  public String getCli_exec_path() {
    return cli_exec_path;
  }

  /**
   * Setting CLI command execution and result analysis shell script path.
   *
   * @param cli_exec_path
   *          Setting cli_exec_path
   */
  public void setCli_exec_path(String cli_exec_path) {
    this.cli_exec_path = cli_exec_path;
  }

  /**
   * Getting IRB Asymmetric configurability.
   *
   * @return irb_asymmetric_capability
   */
  public Boolean getIrb_asymmetric_capability() {
    return irb_asymmetric_capability;
  }

  /**
   * Setting IRB Asymmetric configurability.
   *
   * @param irb_asymmetric_capability
   *          Setting irb_asymmetric_capability
   */
  public void setIrb_asymmetric_capability(Boolean irb_asymmetric_capability) {
    this.irb_asymmetric_capability = irb_asymmetric_capability;
  }

  /**
   * Getting IRB Symmetric configurabillity.
   *
   * @return irb_symmetric_capability
   */
  public Boolean getIrb_symmetric_capability() {
    return irb_symmetric_capability;
  }

  /**
   * Setting IRB Symmetric configurability.
   *
   * @param irb_symmetric_capability
   *          Setting irb_symmetric_capability
   */
  public void setIrb_symmetric_capability(Boolean irb_symmetric_capability) {
    this.irb_symmetric_capability = irb_symmetric_capability;
  }

  /**
   * Getting model IF information list.
   *
   * @return Model IF information list
   */
  public Set<EquipmentIfs> getEquipmentIfsList() {
    return equipmentIfsList;
  }

  /**
   * Setting model IF information list.
   *
   * @param equipmentIfsList
   *          Model IF information list
   */
  public void setEquipmentIfsList(Set<EquipmentIfs> equipmentIfsList) {
    this.equipmentIfsList = equipmentIfsList;
  }

  /**
   * Getting physical IF naming convention information.
   *
   * @return physical IF naming convention information
   */
  public Set<IfNameRules> getIfNameRulesList() {
    return ifNameRulesList;
  }

  /**
   * Setting physical IF naming convention information.
   *
   * @param ifNameRulesList
   *          physical IF naming convention information
   */
  public void setIfNameRulesList(Set<IfNameRules> ifNameRulesList) {
    this.ifNameRulesList = ifNameRulesList;
  }

  /**
   * Getting start-up failure determination message information.
   *
   * @return start-up failure determination message information
   */
  public Set<BootErrorMessages> getBootErrorMessagesList() {
    return bootErrorMessagesList;
  }

  /**
   * Setting start-up failure determination message information.
   *
   * @param bootErrorMessagesList
   *          start-up failure determination message information
   */
  public void setBootErrorMessagesList(Set<BootErrorMessages> bootErrorMessagesList) {
    this.bootErrorMessagesList = bootErrorMessagesList;
  }

  /**
   * Getting Remark menu Information.
   *
   * @return remarkMenusList
   */
  public Set<RemarkMenus> getRemarkMenusList() {
    return remarkMenusList;
  }

  /**
   * Setting Remark menu Information.
   *
   * @param remarkMenusList
   *          Setting remarkMenusList
   */
  public void setRemarkMenusList(Set<RemarkMenus> remarkMenusList) {
    this.remarkMenusList = remarkMenusList;
  }

  /**
   * Getting Egress queue menu Information.
   *
   * @return egressQueueMenusList
   */
  public Set<EgressQueueMenus> getEgressQueueMenusList() {
    return egressQueueMenusList;
  }

  /**
   * Setting Egress queue menu Information.
   *
   * @param egressQueueMenusList
   *          Egress queue menu Information
   */
  public void setEgressQueueMenusList(Set<EgressQueueMenus> egressQueueMenusList) {
    this.egressQueueMenusList = egressQueueMenusList;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public synchronized int hashCode() {
    int hashCode = 0;
    if (equipment_type_id != null) {
      hashCode ^= equipment_type_id.hashCode();
    }
    return hashCode;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj == null || this.hashCode() != obj.hashCode()) {
      return false;
    }

    if (getClass() != obj.getClass()) {
      return false;
    }

    Equipments target = (Equipments) obj;
    if (this.equipment_type_id.equals(target.getEquipment_type_id())) {

      return true;
    }
    return false;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "Equipments [equipment_type_id=" + equipment_type_id + ", lag_prefix=" + lag_prefix + ", unit_connector="
        + unit_connector + ", if_name_oid=" + if_name_oid + ", snmptrap_if_name_oid=" + snmptrap_if_name_oid
        + ", max_repetitions=" + max_repetitions + ", platform_name=" + platform_name + ", os_name=" + os_name
        + ", firmware_version=" + firmware_version + ", router_type=" + router_type
        + ", core_router_physical_if_name_format=" + core_router_physical_if_name_format + ", breakout_if_name_syntax="
        + breakout_if_name_syntax + ", breakout_if_name_suffix_list=" + breakout_if_name_suffix_list
        + ", dhcp_template=" + dhcp_template + ", config_template=" + config_template + ", initial_config="
        + initial_config + ", boot_complete_msg=" + boot_complete_msg + ", evpn_capability=" + evpn_capability
        + ", l2vpn_capability=" + l2vpn_capability + ", l3vpn_capability=" + l3vpn_capability + ", qos_shaping_flg="
        + qos_shaping_flg + ", qos_remark_flg=" + qos_remark_flg + ", default_remark_menu=" + default_remark_menu
        + ", default_egress_queue_menu=" + default_egress_queue_menu + ", same_vlan_number_traffic_total_value_flag="
        + same_vlan_number_traffic_total_value_flag + ", vlan_traffic_capability=" + vlan_traffic_capability
        + ", vlan_traffic_counter_name_mib_oid=" + vlan_traffic_counter_name_mib_oid
        + ", vlan_traffic_counter_value_mib_oid=" + vlan_traffic_counter_value_mib_oid + ", cli_exec_path="
        + cli_exec_path + ", irb_asymmetric_capability=" + irb_asymmetric_capability + ", irb_symmetric_capability="
        + irb_symmetric_capability + ", equipmentIfsList=" + equipmentIfsList + ", ifNameRulesList=" + ifNameRulesList
        + ", bootErrorMessagesList=" + bootErrorMessagesList + ", remarkMenusList=" + remarkMenusList
        + ", egressQueueMenusList=" + egressQueueMenusList + "]";
  }
}
