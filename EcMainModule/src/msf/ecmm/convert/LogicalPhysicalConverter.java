/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.convert;

import static msf.ecmm.common.CommonDefinitions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.db.pojo.BreakoutIfs;
import msf.ecmm.db.pojo.EquipmentIfs;
import msf.ecmm.db.pojo.IfNameRules;
import msf.ecmm.db.pojo.LagIfs;
import msf.ecmm.db.pojo.LagMembers;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.db.pojo.PhysicalIfs;
import msf.ecmm.db.pojo.VlanIfs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Logical Physical Conversion Process Utilities.
 */
public class LogicalPhysicalConverter {

  /** logger. */
  private static final Logger logger = LogManager.getLogger(CommonDefinitions.EC_LOGGER);

  /**
   * Generating Slice Name.
   *
   * @param slice_id
   *          slice ID
   * @return slice name ("slice" + slice ID)
   */
  public static String toSliceName(String slice_id) {
    return new String("slice" + slice_id);
  }

  /**
   * Generating VRF Name (for EM request).
   *
   * @param vrf_id
   *          a parameter unique for each slice ID
   * @return VRF name ("vrf" + vrf_id)
   */
  public static String toVRF(Integer vrf_id) {
    return new String("vrf" + vrf_id.toString());
  }

  /**
   * Generating Route Target (for EM request).
   *
   * @param vrf_id
   *          a parameter unique for each slice ID
   * @param plane
   *          plane where the slice belongs to
   * @return Route Target value ("target:" + vrf_id + ":" + plane)
   */
  public static String toRouteTarget(int vrf_id, int plane) {
    return new String("target:" + vrf_id + ":" + plane);
  }

  /**
   * Generating Route Distinguisher (for EM request).
   *
   * @param vrf_id
   *          a parameter unique for each slice ID
   * @param clusterid
   *          cluster ID
   * @param node_id
   *          ID to which CP is created
   * @return Route Distinguisher value (vrf_id + ":" + cluster_id + node_id)
   */
  public static String toRouteDistinguisher(int vrf_id, int clusterid, String node_id) {
    return new String(vrf_id + ":" + ((clusterid * 1000) + Integer.parseInt(node_id)));
  }

  /**
   * Generating Device Name (for EM request).
   *
   * @param node_type
   *          device type
   * @param cluster_id
   *          cluster ID
   * @param node_id
   *          device ID
   * @return device name (device type (string) + cluster ID + "-" + device ID)
   */
  public static String toNodeName(String node_type, int cluster_id, String node_id) {
    return new String(node_type + cluster_id + "-" + node_id);
  }

  /**
   * LAG IF Configuration Name (for EM request and DB configuration).
   *
   * @param suffix
   *          suffix
   * @param lagifid
   *          LAG IF ID
   * @return LAG IF name (suffix + LAGIF_ID)
   */
  public static String toLagIfName(String suffix, String lagifid) {
    return new String(suffix + lagifid);
  }

  /**
   * Generating Physical IF Name (for DB configuration).
   *
   * @param suffix
   *          port name suffix
   * @param slotName
   *          IF slot name
   * @return physical IF name (port name suffix + IF slot name)
   */
  public static String toPhysicalIfName(String suffix, String slotName) {
    return new String(suffix + slotName);
  }


  /**
   * Generating Physical IF Name (for DB configuration).
   *
   * @param physicalIfId
   *          physical IFID
   * @param speed
   *          physical IF speed
   * @param nodes
   *          device information
   * @return physical IF name (port name suffix + IF slot name)
   * @throws IllegalArgumentException
   *           IllegalArgumentException
   */
  public static String toPhysicalIfName(String physicalIfId, String speed, Nodes nodes)
      throws IllegalArgumentException {
    logger.trace(CommonDefinitions.START);
    logger.debug("physicalIfId=" + physicalIfId + " speed=" + speed + " nodes=" + nodes);

    String physicalIfName = null;

    if (nodes.getEquipments().getRouter_type() == CommonDefinitions.ROUTER_TYPE_NORMAL) {
      String prefix = null;
      for (IfNameRules ifNameRules : nodes.getEquipments().getIfNameRulesList()) {
        if (ifNameRules.getSpeed().equals(speed)) {
          prefix = ifNameRules.getPort_prefix();
          break;
        }
      }

      String slot = null;
      for (EquipmentIfs equipmentIfs : nodes.getEquipments().getEquipmentIfsList()) {
        if (equipmentIfs.getPhysical_if_id().equals(physicalIfId) && equipmentIfs.getPort_speed().equals(speed)) {
          slot = equipmentIfs.getIf_slot();
          break;
        }
      }

      if (prefix != null && slot != null) {
        physicalIfName = toPhysicalIfName(prefix, slot);
      } else {
        logger.warn("Create physical IF name error. prefix=%s, slot=%s", prefix, slot);
        throw new IllegalArgumentException();
      }
    } else if (nodes.getEquipments().getRouter_type() == CommonDefinitions.ROUTER_TYPE_COREROUTER) {
      physicalIfName = toCoreRouterIfName(nodes, physicalIfId, speed);
      if (physicalIfName.isEmpty()) {
        logger.warn("Create physical IF name error.");
        throw new IllegalArgumentException();
      }
    } else {
      logger.warn("Router type invalid. RouterType=%d", nodes.getEquipments().getRouter_type());
      throw new IllegalArgumentException();
    }

    logger.debug("physicalIfName=" + physicalIfName);
    logger.trace(CommonDefinitions.END);
    return physicalIfName;
  }

  /**
   * Device Type Format Conversion (Integer)<br>
   * Convert device type from string to integer.<br>
   * (converting the entry from FC into numelic values for the internal management)
   *
   * @param type
   *          device type (string)
   * @return device type (integer)
   */
  public static int toIntegerNodeType(String type) {
    int ret = 0;
    if (type.equals("leafs") || type.equals("leaf")) {
      ret = CommonDefinitions.NODE_TYPE_LEAF;
    } else {
      ret = CommonDefinitions.NODE_TYPE_SPINE;
    }
    return ret;
  }

  /**
   * Device Type Format Conversion (String)<br>
   * Convert device type from integer to string.<br>
   * (converting numeric value into text string for EM)
   *
   * @param type
   *          device type (integer)
   * @return device type (string)
   */
  public static String toStringNodeType(int type) {
    String ret = "";
    if (type == CommonDefinitions.NODE_TYPE_LEAF) {
      ret = "leaf";
    } else {
      ret = "spine";
    }
    return ret;
  }

  /**
   * EC Block Status Format Conversion (Integer)<br>
   * Convert EC block status from boolean to integer.
   *
   * @param state
   *          EC block status (boolean)
   * @return EC block status (integer)
   */
  public static int toIntegerECObstructionState(boolean state) {
    int ret = 0;
    if (state) {
      ret = CommonDefinitions.EC_BUSY_VALUE;
    } else {
      ret = CommonDefinitions.EC_IN_SERVICE_VALUE;
    }
    return ret;
  }

  /**
   * EC Block Status Format Conversion (boolean)<br>
   * Convert EC block status from integer to boolean.
   *
   * @param state
   *          EC block status (integer)
   * @return EC block status (boolean)
   */
  public static boolean toBooleanECObstructionState(int state) {
    boolean ret = false;
    if (state == CommonDefinitions.EC_BUSY_VALUE) {
      ret = true;
    } else {
      ret = false;
    }
    return ret;
  }

  /**
   * IF Status Format Conversion (Integer)<br>
   * Convert IF status from string to integer.
   *
   * @param state
   *          IF status (string)
   * @return IF status (integer)
   */
  public static int toIntegerIFState(String state) {
    int ret = CommonDefinitions.IF_STATE_UNKNOWN;

    if (state.equals(CommonDefinitions.IF_STATE_OK_STRING)) {
      ret = CommonDefinitions.IF_STATE_OK;
    } else if (state.equals(CommonDefinitions.IF_STATE_NG_STRING)) {
      ret = CommonDefinitions.IF_STATE_NG;
    } else {
    }
    return ret;
  }

  /**
   * IF Status Format Conversion (Integer)<br>
   * Convert IF status from integer to string.
   *
   * @param state
   *          IF status (integer)
   * @return IF status (string)
   */
  public static String toStringIFState(int state) {
    String ret = CommonDefinitions.IF_STATE_UNKNOWN_STRING;

    if (state == CommonDefinitions.IF_STATE_OK) {
      ret = CommonDefinitions.IF_STATE_OK_STRING;
    } else if ((state == CommonDefinitions.IF_STATE_NG) || (state == CommonDefinitions.IF_STATE_NG_L3CP)) {
      ret = CommonDefinitions.IF_STATE_NG_STRING;
    } else {
    }

    return ret;
  }

  /**
   * Generating IF Name After breakout.
   *
   * @param nodesDb
   *          device information DB
   * @param physicalIfId
   *          physical IF ID
   * @param breakoutIfId
   *          breakoutIF ID
   * @param ifSpeed
   *          port speed
   * @param num
   *          number
   * @return IF name after breakout
   */
  public static String toBreakoutIfName(Nodes nodesDb, String physicalIfId, String breakoutIfId, String ifSpeed,
      int num) {
    logger.trace(CommonDefinitions.START);
    logger.debug(nodesDb);
    logger.debug(physicalIfId);
    logger.debug(breakoutIfId);
    logger.debug(ifSpeed);
    logger.debug(num);

    String ret = "";

    try {
      if (nodesDb.getEquipments().getBreakout_if_name_syntax() != null) {
        String syntaxStr = nodesDb.getEquipments().getBreakout_if_name_syntax();

        while (syntaxStr.indexOf('<') > -1) {
          String keywordStr = syntaxStr.substring(syntaxStr.indexOf('<') + 1, syntaxStr.indexOf('>'));
          String convStr = convertKeywordToValue(keywordStr, nodesDb, physicalIfId, breakoutIfId, ifSpeed, num);
          if (convStr != null) {
            syntaxStr = syntaxStr.replaceFirst("<" + keywordStr + ">", convStr);
          } else {
            syntaxStr = "";
            logger.debug("convertKeywordToValue ret null");
            break;
          }
        }
        ret = syntaxStr;
      }
    } catch (IndexOutOfBoundsException iobe) {
      logger.debug("toBreakoutIfName error", iobe);
      ret = "";
    }

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;

  }

  /**
   * Generating Core Router Physical IF Name.
   *
   * @param nodesDb
   *          device information
   * @param physicalIfId
   *          physical IF ID
   * @param ifSpeed
   *          port speed
   * @return core router physical IF name
   */
  public static String toCoreRouterIfName(Nodes nodesDb, String physicalIfId, String ifSpeed) {

    String ret = "";
    try {
      if (nodesDb.getEquipments().getCore_router_physical_if_name_format() != null) {
        String syntaxStr = nodesDb.getEquipments().getCore_router_physical_if_name_format();

        while (syntaxStr.indexOf('<') > -1) {
          String keywordStr = syntaxStr.substring(syntaxStr.indexOf('<') + 1, syntaxStr.indexOf('>'));
          String convStr = convertKeywordToValue(keywordStr, nodesDb, physicalIfId, null, ifSpeed, 0);
          if (convStr != null) {
            syntaxStr = syntaxStr.replaceFirst("<" + keywordStr + ">", convStr);
          } else {
            syntaxStr = "";
            break;
          }

        }
        ret = syntaxStr;
      }
    } catch (IndexOutOfBoundsException iobe) {
      ret = "";
    }

    return ret;

  }

  /**
   * Convert the specified keyword in parameter into a prescribed value.
   *
   * @param keywordStr
   *          keyword
   * @param nodesDb
   *          model information DB
   * @param physicalIfId
   *          physical IF ID
   * @param breakoutIfId
   *          breakoutIF ID
   * @param ifSpeed
   *          port speed
   * @param num
   *          number
   * @return IF name after breakout
   */
  private static String convertKeywordToValue(String keywordStr, Nodes nodes, String physicalIfId, String breakoutIfId,
      String ifSpeed, int num) {
    logger.trace(CommonDefinitions.START);
    logger.debug(keywordStr);
    logger.debug(nodes);
    logger.debug(physicalIfId);
    logger.debug(breakoutIfId);
    logger.debug(ifSpeed);
    logger.debug(num);

    String ret = null;

    OK: switch (keywordStr) {
      case "PHYSICALIFID":
        ret = physicalIfId;
        break;
      case "PHYSICALIFNAME":
        for (PhysicalIfs ifs : nodes.getPhysicalIfsList()) {
          if (ifs.getPhysical_if_id().equals(physicalIfId)) {
            ret = ifs.getIf_name();
            break OK;
          }
        }
        logger.debug("unmatch physicalIfId " + physicalIfId);
        break;
      case "IFSLOTNAME":
      case "SLOT":
        for (EquipmentIfs ifs : nodes.getEquipments().getEquipmentIfsList()) {
          if (ifs.getPhysical_if_id().equals(physicalIfId)) {
            ret = ifs.getIf_slot();
            break OK;
          }
        }
        logger.debug("unmatch slot " + ifSpeed);
        break;
      case "BREAKOUTIFSUFFIX":
        String[] suffixArray = nodes.getEquipments().getBreakout_if_name_suffix_list().split(":");
        if (suffixArray.length > num) {
          ret = suffixArray[num];
        }
        break;
      case "BREAKOUTIFID":
        ret = breakoutIfId;
        break;
      case "PORTPREFIX":
        for (IfNameRules rules : nodes.getEquipments().getIfNameRulesList()) {
          if (rules.getSpeed().equals(ifSpeed)) {
            ret = rules.getPort_prefix();
            break OK;
          }
        }
        logger.debug("unmatch portSpeed " + ifSpeed);
        break;
      case "NODEID":
        ret = nodes.getNode_id();
        break;
      case "EQUIPMENTTYPEID":
        ret = nodes.getEquipment_type_id();
        break;
      default:
        logger.debug("unknown keywordStr " + keywordStr);
        break;
    }
    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;

  }

  /**
   * Lag IFID's payout function.
   *
   * @param nodesDb
   *          device information(DB)
   * @return LagIFID
   */
  public static String getLagIfId(Nodes nodesDb) {
    logger.trace(START);
    logger.debug("nodesDb:" + nodesDb);

    String ret = null;
    List<Integer> lagIfIdList = new ArrayList<Integer>();
    if (nodesDb.getLagIfsList() != null && nodesDb.getLagIfsList().isEmpty() == false) {
      for (LagIfs lagIfs : nodesDb.getLagIfsList()) {
        lagIfIdList.add(Integer.parseInt(lagIfs.getLag_if_id()));
      }
      Collections.sort(lagIfIdList);
      for (int i = 1; i < lagIfIdList.size() + 1; i++) {
        if ((lagIfIdList.get(i - 1)) != i) {
          ret = String.valueOf(i);
          break;
        } else if (lagIfIdList.get(lagIfIdList.size() - 1) == i) {
          ret = String.valueOf(i + 1);
        }
      }

    } else {
      ret = "1";
    }

    logger.debug("LAG IF ID: " + ret);
    logger.trace(END);
    return ret;

  }

  /**
   * Search from each IF information list possessed by the device information by using the IF ID as a key and obtain the IF speed (numerical value).<br>
   * physicalIF：The speed of the relevant IF<br>
   * LAGIF：The speed of the LAG (total value of the IF speeds defined as members of the LAG)<br>
   * breakoutIF：breakout Speed after breakout of the original physical IF
   *
   * @param ifType
   *          IF type
   * @param ifId
   *          IF ID
   * @param nodes
   *          device information
   * @return IF speed (numerical value)
   */
  public static int getIfSpeed(String ifType, String ifId, Nodes nodes) {
    logger.trace(START);
    logger.debug("ifType:" + ifType + ", ifId: " + ifId + ", nodes: " + nodes);

    int ret = 0;
    if (ifType.equals(CommonDefinitions.IF_TYPE_PHYSICAL_IF)) {
      for (PhysicalIfs physicalIfs : nodes.getPhysicalIfsList()) {
        if (physicalIfs.getPhysical_if_id().equals(ifId)) {
          ret = Integer.parseInt(physicalIfs.getIf_speed().replace(SPEED_UNIT_GIGA, ""));
          break;
        }
      }
    } else if (ifType.equals(CommonDefinitions.IF_TYPE_LAG_IF)) {
      for (LagIfs lagIfs : nodes.getLagIfsList()) {
        if (lagIfs.getFc_lag_if_id().equals(ifId) && lagIfs.getLagMembersList() != null) {
          for (LagMembers lagMembers : lagIfs.getLagMembersList()) {
            int lagMemberSpeed = 0;
            if (lagMembers.getPhysical_if_id() != null) {
              for (PhysicalIfs physicalIfs : nodes.getPhysicalIfsList()) {
                if (physicalIfs.getPhysical_if_id().equals(lagMembers.getPhysical_if_id())) {
                  lagMemberSpeed = Integer.parseInt(physicalIfs.getIf_speed().replace(SPEED_UNIT_GIGA, ""));
                  break;
                }
              }
            } else if (lagMembers.getBreakout_if_id() != null) {
              physiIfsLoop: for (PhysicalIfs physicalIfs : nodes.getPhysicalIfsList()) {
                if (physicalIfs.getBreakoutIfsList() != null) {
                  for (BreakoutIfs breakoutIfs : physicalIfs.getBreakoutIfsList()) {
                    if (breakoutIfs.getBreakout_if_id().equals(lagMembers.getBreakout_if_id())) {
                      lagMemberSpeed = Integer.parseInt(breakoutIfs.getSpeed().replace(SPEED_UNIT_GIGA, ""));
                      break physiIfsLoop;
                    }
                  }
                }
              }
            }
            ret += lagMemberSpeed;
          }
          break;
        }
      }
    } else if (ifType.equals(CommonDefinitions.IF_TYPE_BREAKOUT_IF)) {
      physiIfsLoop: for (PhysicalIfs physicalIfs : nodes.getPhysicalIfsList()) {
        if (physicalIfs.getBreakoutIfsList() != null) {
          for (BreakoutIfs breakoutIfs : physicalIfs.getBreakoutIfsList()) {
            if (breakoutIfs.getBreakout_if_id().equals(ifId)) {
              ret = Integer.parseInt(breakoutIfs.getSpeed().replace(SPEED_UNIT_GIGA, ""));
              break physiIfsLoop;
            }
          }
        }
      }
    }

    logger.debug("IfSpeed: " + ret);
    logger.trace(END);
    return ret;
  }

  /**
   * Acquire the speed of the real IF (physical / LAG / Breakout) for which the VLAN is set.
   *
   * @param vlanIfs
   *          VLANIF information
   * @param nodes
   *          device information
   * @return IF speed (numerical value)
   */
  public static int getVlanIfSpeed(VlanIfs vlanIfs, Nodes nodes) {

    String ifType = null;
    String ifId = null;
    if (vlanIfs.getPhysical_if_id() != null) {
      ifType = IF_TYPE_PHYSICAL_IF;
      ifId = vlanIfs.getPhysical_if_id();
    } else if (vlanIfs.getLag_if_id() != null) {
      ifType = IF_TYPE_LAG_IF;
      if (nodes.getLagIfsList() != null) {
        for (LagIfs elem : nodes.getLagIfsList()) {
          if (elem.getLag_if_id().equals(vlanIfs.getLag_if_id())) {
            ifId = elem.getFc_lag_if_id();
          }
        }
      }
    } else if (vlanIfs.getBreakout_if_id() != null) {
      ifType = IF_TYPE_BREAKOUT_IF;
      ifId = vlanIfs.getBreakout_if_id();
    }

    return getIfSpeed(ifType, ifId, nodes);
  }

}
