/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * Generating Device Information.
 */
public class CreateNode {

  /** Device ID. */
  private String nodeId;

  /** Host Name. */
  @SerializedName("host_name")
  private String hostname;

  /** Device Type. */
  private String nodeType;

  /** Login User Name. */
  private String username;

  /** Login Password. */
  private String password;

  /** MAC Address. */
  @SerializedName("mac_addr")
  private String macAddress;

  /** Device Configuration Necessity Flag. */
  private Boolean provisioning;

  /** NTP Server Address. */
  private String ntpServerAddress;

  /** Management IF Information. */
  private AddressInfo managementInterface;

  /** Loopback IF Information. */
  private AddressInfo loopbackInterface;

  /** Belonging Plane. */
  private String plane;

  /** SNMP Community Name. */
  private String snmpCommunity;

  /** IF Information. */
  @SerializedName("if")
  private NodeInterface ifInfo;

  /** Opposing Device IF Information. */
  private List<OppositeNodesInterface> oppositeNodes = new ArrayList<>();

  /** VPN Configuration Information. */
  private Vpn vpn;

  /** OSPF Area in Multicluster. */
  private String clusterArea;

  /** VirtualLink Configuration. */
  private VirtualLink virtualLink;

  /** OSPF Route Aggregation. */
  private AddressInfo range;

  /**
   * Getting device ID.
   *
   * @return device ID
   */
  public String getNodeId() {
    return nodeId;
  }

  /**
   * Setting device ID.
   *
   * @param nodeId
   *          device ID
   */
  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  /**
   * Getting host name.
   *
   * @return host name
   */
  public String getHostname() {
    return hostname;
  }

  /**
   * Setting host name.
   *
   * @param hostname
   *          host name
   */
  public void setHostname(String hostname) {
    this.hostname = hostname;
  }

  /**
   * Getting device type.
   *
   * @return device type
   */
  public String getNodeType() {
    return nodeType;
  }

  /**
   * Setting device type.
   *
   * @param nodeType
   *          device type
   */
  public void setNodeType(String nodeType) {
    this.nodeType = nodeType;
  }

  /**
   * Getting login user name.
   *
   * @return login user name
   */
  public String getUsername() {
    return username;
  }

  /**
   * Setting login user name.
   *
   * @param username
   *          login user name
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Getting login password.
   *
   * @return login password
   */
  public String getPassword() {
    return password;
  }

  /**
   * Setting login password.
   *
   * @param password
   *          login password
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Getting MAC address.
   *
   * @return MAC address
   */
  public String getMacAddress() {
    return macAddress;
  }

  /**
   * Setting MAC address.
   *
   * @param macAddress
   *          MAC address
   */
  public void setMacAddress(String macAddress) {
    this.macAddress = macAddress;
  }

  /**
   * Getting device configuration necessity flag.
   *
   * @return device configuration necessity flag
   */
  public Boolean getProvisioning() {
    return provisioning;
  }

  /**
   * Setting device configuration necessigy flag.
   *
   * @param provisioning
   *          device configuration necessity flag
   */
  public void setProvisioning(Boolean provisioning) {
    this.provisioning = provisioning;
  }

  /**
   * Getting NTP server address.
   *
   * @return NTP server address
   */
  public String getNtpServerAddress() {
    return ntpServerAddress;
  }

  /**
   *Setting NTP server address.
   *
   * @param ntpServerAddress
   *          NTP server address
   */
  public void setNtpServerAddress(String ntpServerAddress) {
    this.ntpServerAddress = ntpServerAddress;
  }

  /**
   * Getting management IF information.
   *
   * @return management IF information
   */
  public AddressInfo getManagementInterface() {
    return managementInterface;
  }

  /**
   * Setting management IF information.
   *
   * @param managementInterface
   *          management IF information
   */
  public void setManagementInterface(AddressInfo managementInterface) {
    this.managementInterface = managementInterface;
  }

  /**
   * Getting loopback IF information.
   *
   * @return loopback IF information
   */
  public AddressInfo getLoopbackInterface() {
    return loopbackInterface;
  }

  /**
   * Setting loopback IF information.
   *
   * @param loopbackInterface
   *          loopback IF information
   */
  public void setLoopbackInterface(AddressInfo loopbackInterface) {
    this.loopbackInterface = loopbackInterface;
  }

  /**
   * Getting belonging plane.
   *
   * @return belonging plane
   */
  public String getPlane() {
    return plane;
  }

  /**
   * Setting belonging plane.
   *
   * @param plane
   *          belonging plane
   */
  public void setPlane(String plane) {
    this.plane = plane;
  }

  /**
   * Getting SNMP community name.
   *
   * @return SNMP community name
   */
  public String getSnmpCommunity() {
    return snmpCommunity;
  }

  /**
   * Setting SNMP community name.
   *
   * @param snmpCommunity
   *          SNMP community name
   */
  public void setSnmpCommunity(String snmpCommunity) {
    this.snmpCommunity = snmpCommunity;
  }

  /**
   * Getting IF information.
   *
   * @return IF information
   */
  public NodeInterface getIfInfo() {
    return ifInfo;
  }

  /**
   * Setting IF information.
   *
   * @param ifInfo
   *          IF information
   */
  public void setIfInfo(NodeInterface ifInfo) {
    this.ifInfo = ifInfo;
  }

  /**
   * Getting opposing device IF information.
   *
   * @return opposing device IF information
   */
  public List<OppositeNodesInterface> getOppositeNodes() {
    return oppositeNodes;
  }

  /**
   * Setting opposing device IF information.
   *
   * @param oppositeNodes
   *          opposing device IF information
   */
  public void setOppositeNodes(List<OppositeNodesInterface> oppositeNodes) {
    this.oppositeNodes = oppositeNodes;
  }

  /**
   * Getting VPN configuration information.
   *
   * @return VPN configuration information
   */
  public Vpn getVpn() {
    return vpn;
  }

  /**
   * Setting VPN configuration information.
   *
   * @param vpn
   *          VPN configuration information
   */
  public void setVpn(Vpn vpn) {
    this.vpn = vpn;
  }

  /**
   * Getting OSPF Area in multicluster.
   *
   * @return OSPF Area in multicluster
   */
  public String getClusterArea() {
    return clusterArea;
  }

  /**
   * Setting OSPF Area in multicluster.
   *
   * @param clusterArea
   *          OSPF Area in multicluster
   */
  public void setClusterArea(String clusterArea) {
    this.clusterArea = clusterArea;
  }

  /**
   * Getting VirtualLink configuration.
   *
   * @return VirtualLink configuration
   */
  public VirtualLink getVirtualLink() {
    return virtualLink;
  }

  /**
   * Setting VirtualLink configuration.
   *
   * @param virtualLink
   *          VirtualLink configuration
   */
  public void setVirtualLink(VirtualLink virtualLink) {
    this.virtualLink = virtualLink;
  }

  /**
   * Getting OSPF route aggregation.
   *
   * @return OSPF route aggregation
   */
  public AddressInfo getRange() {
    return range;
  }

  /**
   * Setting OSPF route aggregation.
   *
   * @param range
   *          OSPF route aggregation
   */
  public void setRange(AddressInfo range) {
    this.range = range;
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

    if (nodeId == null) {
      throw new CheckDataException();
    }
    if (hostname == null) {
      throw new CheckDataException();
    }
    if (nodeType == null) {
      throw new CheckDataException();
    } else {
      if (!nodeType.equals(CommonDefinitions.NODETYPE_SPINE)
          && !nodeType.equals(CommonDefinitions.NODETYPE_LEAF)
          && !nodeType.equals(CommonDefinitions.NODETYPE_BLEAF)
          && !nodeType.equals(CommonDefinitions.NODETYPE_RR)) {
        throw new CheckDataException();
      }
    }
    if (username == null) {
      throw new CheckDataException();
    }
    if (password == null) {
      throw new CheckDataException();
    }
    if (macAddress == null) {
      throw new CheckDataException();
    }
    if (provisioning == null) {
      throw new CheckDataException();
    }
    if (ntpServerAddress == null) {
      throw new CheckDataException();
    }
    if (managementInterface == null) {
      throw new CheckDataException();
    } else {
      managementInterface.check(ope);
    }
    if (loopbackInterface == null) {
      throw new CheckDataException();
    } else {
      loopbackInterface.check(ope);
    }
    if (snmpCommunity == null) {
      throw new CheckDataException();
    }
    if (ifInfo == null) {
      throw new CheckDataException();
    } else {
      ifInfo.check(ope);
    }
    if (!oppositeNodes.isEmpty()) {
      for (OppositeNodesInterface oppoNodesIf : oppositeNodes) {
        oppoNodesIf.check(ope);
      }
    }
    if (vpn != null) {
      vpn.check(ope);
    }
    if (clusterArea == null) {
      throw new CheckDataException();
    }
    if (virtualLink != null) {
      virtualLink.check(ope);
    }
    if (range != null) {
      range.check(ope);
    }
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "CreateNode [nodeId=" + nodeId + ", hostname=" + hostname + ", nodeType=" + nodeType + ", username="
        + username + ", password=" + password + ", macAddress=" + macAddress + ", provisioning=" + provisioning
        + ", ntpServerAddress=" + ntpServerAddress + ", managementInterface=" + managementInterface
        + ", loopbackInterface=" + loopbackInterface + ", plane=" + plane + ", snmpCommunity=" + snmpCommunity
        + ", ifInfo=" + ifInfo + ", oppositeNodes=" + oppositeNodes + ", vpn=" + vpn + ", clusterArea=" + clusterArea
        + ", virtualLink=" + virtualLink + ", range=" + range + "]";
  }

}
