/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.devctrl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Counter64;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.CommonUtil;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.config.EcConfiguration;
import msf.ecmm.db.pojo.Equipments;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.devctrl.pojo.SnmpIfOperStatus;
import msf.ecmm.devctrl.pojo.SnmpIfTraffic;
import msf.ecmm.ope.receiver.pojo.parts.Varbind;

/**
 * SNMP Related Operations.
 */
public class SnmpController {

  /**
   * logger.
   */
  private final Logger logger = LogManager.getLogger(CommonDefinitions.EC_LOGGER);

  /** SNMP Port Number. */
  private static final int SNMP_PORT = 161;

  /** ifOperStatus */
  private static final String OID_ifOperStatus = ".1.3.6.1.2.1.2.2.1.8";

  /** ospfNbrState */
  private static final String OID_ospfNbrState = ".1.3.6.1.2.1.14.10.1.6";

  /** ifxEntry */
  private static final String OID_ifxEntry = ".1.3.6.1.2.1.31.1.1.1";

  /** ifHCInOctets */
  private static final String OID_ifHCInOctets = ".1.3.6.1.2.1.31.1.1.1.6";

  /** ifHCOutOctets */
  private static final String OID_ifHCOutOctets = ".1.3.6.1.2.1.31.1.1.1.10";

  /** linkDown Trap */
  @SuppressWarnings("unused")
  private static final String OID_linkDown = ".1.3.6.1.6.3.1.1.5.3";

  /** linkUp Trap */
  @SuppressWarnings("unused")
  private static final String OID_linkUp = ".1.3.6.1.6.3.1.1.5.4";

  /** ifIndex Trap */
  private static final String OID_ifIndex = ".1.3.6.1.2.1.2.2.1.1";

  /** OSPF Neibghbor State Full */
  private static final int OSPF_NEIGHBOR_FULL = 8;

  /** ifIndex not found */
  public static final int IFINDEX_NOT_FOUND = -1;

  private static String OID_HEADER = "2636.3.5.2.1.";

  /**
   * Constructor.
   */
  public SnmpController() {

  }

  /**
   * Getting SNMP Information (IF status).
   *
   * @param eq
   *          model information
   * @param node
   *          device information
   * @return list of the status of each acquired IF
   * @throws DevctrlException
   *           error has occurred in SNMP communication
   */
  public ArrayList<SnmpIfOperStatus> getIfOperStatus(Equipments eq, Nodes node) throws DevctrlException {
    logger.debug("start eq=" + eq + " node=" + node);

    HashMap<Integer, String> ifMap = getIfMap(eq, node);

    ArrayList<VariableBinding> vars = getbulk(eq, node, OID_ifOperStatus);

    ArrayList<SnmpIfOperStatus> ret = new ArrayList<>();
    for (VariableBinding varbind : vars) {
      ret.add(new SnmpIfOperStatus(ifMap.get(varbind.getOid().get(varbind.getOid().size() - 1)),
          varbind.getVariable().toInt()));
    }

    logger.trace("ret=" + ret);
    return ret;
  }

  /**
   * Getting SNMP Information (IF name).
   *
   * @param eq
   *          model information
   * @param node
   *          device information
   * @param ifIndex
   *          ifIndex
   * @return IF name
   * @throws DevctrlException
   *           error has occurred in SNMP communication
   */
  public String getIfName(Equipments eq, Nodes node, int ifIndex) throws DevctrlException {
    logger.debug("start eq=" + eq + " node=" + node + " ifIndex=" + ifIndex);
    VariableBinding varbind = get(node, toDotted(eq.getIf_name_oid()) + "." + ifIndex);
    return varbind.getVariable().toString();
  }

  /**
   * OSPF Neighbor UP Confirmation.
   *
   * @param eq
   *          model information
   * @param node
   *          device information
   * @param neighbors
   *          list of neighbors
   * @return result of UP -> true: UP
   * @throws DevctrlException
   *           error has occurred in SNMP communication
   */
  public boolean isOspfNeighborFull(Equipments eq, Nodes node, ArrayList<String> neighbors) throws DevctrlException {
    logger.debug("start eq=" + eq + " node=" + node + " neighbors=" + neighbors);

    int retryNum = EcConfiguration.getInstance().get(Integer.class, EcConfiguration.OSPF_NEIGHBOR_RETRY_NUM);
    int retryInt = EcConfiguration.getInstance().get(Integer.class, EcConfiguration.OSPF_NEIGHBOR_RETRY_INTERVAL);

    for (int i = 0; i <= retryNum; i++) {
      @SuppressWarnings("unchecked")
      ArrayList<String> chkList = (ArrayList<String>) neighbors.clone();

      ArrayList<VariableBinding> vars = null;
      try {
        vars = getbulk(eq, node, OID_ospfNbrState);
      } catch (DevctrlException de) {
        logger.debug("getbulk() fail.", de);
        CommonUtil.sleep(retryInt);
        continue;
      }

      for (VariableBinding varbind : vars) {

        if (varbind.getVariable().toInt() == OSPF_NEIGHBOR_FULL) {
          String addr = varbind.getOid().toString().substring(OID_ospfNbrState.length(),
              varbind.getOid().toString().length() - 2);
          chkList.remove(addr);
        }
      }

      if (chkList.size() == 0) {
        logger.debug("OspfNeighborFull complete.");
        return true;
      }

      CommonUtil.sleep(retryInt);

    }
    return false;
  }

  /**
   * Getting Traffic Information.
   *
   * @param eq
   *          model information
   * @param node
   *          device information
   * @return Traffic information
   * @throws DevctrlException
   *           error has occurred in SNMP communication
   */
  public ArrayList<SnmpIfTraffic> getTraffic(Equipments eq, Nodes node) throws DevctrlException {
    logger.debug("start eq=" + eq + " node=" + node);
    logger.trace("Traffic  regular collection Start");
    HashMap<Integer, String> ifMap = getIfMap(eq, node);

    ArrayList<VariableBinding> vars = getbulk(eq, node, OID_ifxEntry);

    HashMap<Integer, SnmpIfTraffic> traffic = new HashMap<>();
    for (VariableBinding varbind : vars) {

      if (varbind.getOid().toString().startsWith(OID_ifHCInOctets.substring(1))) {
        int ifIndex = varbind.getOid().get(varbind.getOid().size() - 1);
        SnmpIfTraffic data = traffic.get(ifIndex);
        if (data == null) {
          data = new SnmpIfTraffic(ifMap.get(ifIndex), 0, 0);
          traffic.put(ifIndex, data);
        }
        data.setInOctets(((Counter64) varbind.getVariable()).toLong());
      }

      if (varbind.getOid().toString().startsWith(OID_ifHCOutOctets.substring(1))) {
        int ifIndex = varbind.getOid().get(varbind.getOid().size() - 1);
        SnmpIfTraffic data = traffic.get(ifIndex);
        if (data == null) {
          data = new SnmpIfTraffic(ifMap.get(ifIndex), 0, 0);
          traffic.put(ifIndex, data);
        }
        data.setOutOctets(((Counter64) varbind.getVariable()).toLong());
      }
    }

    ArrayList<SnmpIfTraffic> ret = new ArrayList<>();
    traffic.forEach((k, v) -> ret.add(v));
    logger.trace("Traffic  regular collection is complete.");
    logger.debug("traffic : " + ret);

    return ret;
  }

  /**
   * Getting ifIndex from Trap.
   *
   * @param varBinds
   *          list of VarBind in Trap
   * @return ifIndex -1 was returned but couldn't find ifIndex
   */
  public static int getIfIndexForTrap(List<Varbind> varBinds) {
    for (Varbind vb : varBinds) {
      if (vb.getOid().startsWith(OID_ifIndex)) {
        return Integer.parseInt(vb.getValue());
      }
    }
    return IFINDEX_NOT_FOUND;
  }

  /**
   * Getting IfName from Trap.
   *
   * @param varBinds
   *          list of VarBind in Trap
   * @param ifNameOid
   *          IF name acquisition MIB information in SNMPTrap
   * @return ifName
   */
  public static String getIfNameForTrap(List<Varbind> varBinds, String ifNameOid) {
    for (Varbind vb : varBinds) {
      if (vb.getOid().startsWith(toDotted(ifNameOid))) {
        return vb.getValue();
      }
    }
    return null;
  }

  /**
   * Generating CommunityTarget.
   *
   * @param node
   *          device information
   * @return instance
   */
  private CommunityTarget init(Nodes node) {
    CommunityTarget target = new CommunityTarget();
    target.setAddress(new UdpAddress(node.getManagement_if_address() + "/" + SNMP_PORT));
    target.setCommunity(new OctetString(node.getSnmp_community()));
    target.setTimeout(EcConfiguration.getInstance().get(Integer.class, EcConfiguration.DEVICE_SNMP_TIMEOUT) * 1000);
    target.setRetries(1);
    target.setVersion(SnmpConstants.version2c);
    return target;
  }

  /**
   * getbulk
   *
   * @param eq
   *          model information.
   * @param node
   *          device information
   * @param oid
   *          OID
   * @return list of VarBind
   * @throws DevctrlException
   *           error has occurred in SNMP communication
   */
  private ArrayList<VariableBinding> getbulk(Equipments eq, Nodes node, String oid) throws DevctrlException {
    logger.trace("start eq=" + eq + " node" + node + " oid=" + oid);
    logger.debug("start eq=" + "Abbreviation" + " node=" + "Abbreviation" + " oid=" + oid);

    ArrayList<VariableBinding> ret = new ArrayList<>();

    CommunityTarget target = init(node);

    OID nextOID = new OID(oid);

    Snmp snmp = null;
    try {
      DefaultUdpTransportMapping utm = new DefaultUdpTransportMapping();
      snmp = new Snmp(utm);
      snmp.listen();

      for (;;) {

        PDU pdu = new PDU();
        pdu.setType(PDU.GETBULK);
        pdu.setMaxRepetitions(eq.getMax_repetitions());
        pdu.setNonRepeaters(0);
        pdu.add(new VariableBinding(nextOID));

        ResponseEvent response = snmp.getBulk(pdu, target);
        PDU resPdu = response.getResponse();

        if (resPdu != null && resPdu.getErrorStatus() == SnmpConstants.SNMP_ERROR_NO_SUCH_NAME) {
          logger.debug("SNMP GETBULK(NoSuchName)={" + ret + "}");
          return ret;
        }

        if (resPdu != null && resPdu.getErrorStatus() == SnmpConstants.SNMP_ERROR_SUCCESS) {
          for (int i = 0; i < resPdu.size(); i++) {
            if (resPdu.get(i).getOid().toString().startsWith(oid.substring(1))) { 
              ret.add(resPdu.get(i));
            } else {
              logger.debug("SNMP GETBULK={" + ret + "}");
              return ret;
            }
          }

          if (resPdu.size() == 0) {
            logger.debug("SNMP GETBULK (size=0)={" + ret + "}");
            return ret;
          }

          nextOID = resPdu.get(resPdu.size() - 1).getOid();

        } else {
          logger.error(LogFormatter.out.format(LogFormatter.MSG_505027, resPdu));
          throw new DevctrlException("GetBulk fail.");
        }
      }

    } catch (IOException e) {
      logger.error(LogFormatter.out.format(LogFormatter.MSG_505027, e));
      throw new DevctrlException("GetBulk fail.");
    } finally {
      try {
        if (snmp != null) {
          snmp.close();
        }
      } catch (IOException e) {
        logger.trace("snmp end processing error");
      }
    }

  }

  /**
   * Creating hash map of ifIndex and ifName.
   *
   * @param eq
   *          model information
   * @param node
   *          device information
   * @return hash map
   * @throws DevctrlException
   */
  private HashMap<Integer, String> getIfMap(Equipments eq, Nodes node) throws DevctrlException {
    HashMap<Integer, String> map = new HashMap<>();
    ArrayList<VariableBinding> vars = getbulk(eq, node, toDotted(eq.getIf_name_oid()));
    for (VariableBinding varbind : vars) {
      map.put(varbind.getOid().get(varbind.getOid().size() - 1), varbind.getVariable().toString());
    }
    return map;
  }

  /**
   * get
   *
   * @param node
   *          device information
   * @param oid
   * @return VarBind
   * @throws DevctrlException
   *           error has occurred in SNMP communication
   */
  private VariableBinding get(Nodes node, String oid) throws DevctrlException {
    logger.debug("start node" + node + " oid=" + oid);

    CommunityTarget target = init(node);

    PDU pdu = new PDU();
    pdu.setType(PDU.GET);
    pdu.add(new VariableBinding(new OID(oid)));

    Snmp snmp = null;

    try {
      DefaultUdpTransportMapping utm = new DefaultUdpTransportMapping();
      snmp = new Snmp(utm);
      snmp.listen();

      ResponseEvent response = snmp.get(pdu, target);

      PDU resPdu = response.getResponse();
      if (resPdu != null && resPdu.getErrorStatus() == SnmpConstants.SNMP_ERROR_SUCCESS) {
        logger.debug("SNMP GET={" + resPdu.get(0) + "}");
        return resPdu.get(0);
      } else {
        logger.error(LogFormatter.out.format(LogFormatter.MSG_505027, resPdu));
        throw new DevctrlException("Get fail.");
      }
    } catch (IOException e) {
      logger.error(LogFormatter.out.format(LogFormatter.MSG_505027, e));
      throw new DevctrlException("Get fail.");
    } finally {
      try {
        if (snmp != null) {
          snmp.close();
        }
      } catch (IOException e) {
        logger.trace("snmp end processing error");
      }
    }

  }

  /**
   * Unify the OIDs with a period in the head.
   *
   * @param val
   *          OID string
   * @return return value (OID with a period)
   */
  static private String toDotted(String val) {
    if (!val.startsWith(".")) {
      return "." + val;
    } else {
      return val;
    }
  }

  /**
   * Traffic information acquisition.
   *
   * @param eq
   *          model information
   * @param node
   *          device information
   * @return Traffic information
   * @throws DevctrlException
   *           abnormality occurred at SNMP communication
   */
  public ArrayList<SnmpIfTraffic> getVlanTraffic(Equipments eq, Nodes node) throws DevctrlException {
    logger.trace("Vlan Traffic  regular collection is start");
    HashMap<String, String> ifNameMap = new HashMap<>();
    HashMap<String, SnmpIfTraffic> traffic = new HashMap<>();
    ArrayList<SnmpIfTraffic> ret = new ArrayList<>();

    ArrayList<VariableBinding> nameVars = getbulk(eq, node, eq.getVlan_traffic_counter_name_mib_oid());
    if (0 == nameVars.size()) {
      return ret;
    }
    String ifName = null;
    String vlanOid = null;
    for (VariableBinding nameBind : nameVars) {
      String ifNameBind = nameBind.getVariable().toString();
      String[] nameParts = ifNameBind.split("-");
      if (nameParts.length == 3) {
        String vlanId = nameParts[1].replaceAll("[^0-9]", "");
        String[] ifChekc = nameParts[2].split("_");
        if (ifChekc.length == 4) {
          ifName = ifChekc[0] + "-" + ifChekc[1] + "/" + ifChekc[2] + "/" + ifChekc[3] + "." + vlanId;
        } else {
          ifName = nameParts[2] + "." + vlanId;
        }
        String ifOid = nameBind.getOid().toString();
        vlanOid = getVlanTrafficOid(ifOid);

        ifNameMap.put(vlanOid, ifName);
      }
    }

    ArrayList<VariableBinding> trafficVars = getbulk(eq, node, eq.getVlan_traffic_counter_value_mib_oid());
    if (0 == trafficVars.size()) {
      return ret;
    }
    for (VariableBinding trafficVar : trafficVars) {
      String ifOid = trafficVar.getOid().toString();
      String trafficOid = getVlanTrafficOid(ifOid);
      SnmpIfTraffic data = traffic.get(trafficOid);
      if (null != ifNameMap.get(trafficOid)) {
        if (data == null) {
          data = new SnmpIfTraffic(ifNameMap.get(trafficOid), 0, 0);
          traffic.put(trafficOid, data);
        }
        data.setOutOctets(0);
        data.setInOctets(((Counter64) trafficVar.getVariable()).toLong());
      }
    }

    traffic.forEach((k, v) -> ret.add(v));
    logger.trace("Vlan Traffic  regular collection is complete.");
    logger.debug("traffic : " + ret);

    return ret;
  }

  /**
   * Acquiring OID for VLAN unit traffic collection.
   *
   * @param ifOid
   *          OID acquired from VariableBinding
   * @return OID
   */
  public String getVlanTrafficOid(String ifOid) {

    int index = ifOid.indexOf(OID_HEADER);
    int startPos = index + OID_HEADER.length();
    StringBuilder sb = new StringBuilder(ifOid);
    sb.delete(0, startPos);
    List<String> oidParts = Arrays.asList(sb.toString().split("\\."));

    int idPos = 1;
    StringBuilder idBuff = new StringBuilder();
    for (int i = idPos; i < oidParts.size(); i++) {
      idBuff.append(oidParts.get(i));
      idBuff.append(".");
    }

    return idBuff.toString();
  }
}
