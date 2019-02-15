/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.cp;

import static msf.ecmm.db.DBAccessException.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.dao.BaseDAO;
import msf.ecmm.db.pojo.AclConf;
import msf.ecmm.db.pojo.DummyVlanIfs;
import msf.ecmm.db.pojo.VlanIfs;
import msf.ecmm.ope.receiver.pojo.parts.VlanIfsDeleteVlanIf;

/**
 * ACL Utility Class(CP).
 */
public class AclUtilityCp {

  /** Logger. */
  private static final Logger logger = LogManager.getLogger(CommonDefinitions.EC_LOGGER);

  /**
   * Judging whether VLAN delete is possible/ impossible, at using ACL.
   *
   * @param allVlanIfsMap
   *          Entire VLAN information which is held by such device having the VLAN to be deleted
   * @param delVlans
   *          the VLAN to be deleted
   * @return judgement results
   * @throws DBAccessException
   *           In case abnormality has occurred in DB
   */
  protected static boolean checkRemoveVlan(Map<String, List<VlanIfs>> allVlanIfsMap, List<VlanIfsDeleteVlanIf> delVlans)
      throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    boolean ret = true;


    try (DBAccessManager session = new DBAccessManager()) {

      Map<String, Map<String, List<String>>> allPhysVlanListMapMap = new HashMap<String, Map<String, List<String>>>();
      Map<String, Map<String, List<String>>> delPhysVlanListMapMap = new HashMap<String, Map<String, List<String>>>();

      NG_FOUND: do {

        for (VlanIfsDeleteVlanIf vlanInfo : delVlans) {
          String nodeId = vlanInfo.getNodeId();
          String ifId = vlanInfo.getVlanIfId();
          String ifType = CommonDefinitions.IF_TYPE_VLAN_IFS;
          AclConf aclConf = session.getAclConf(nodeId, ifId, ifType);
          if (aclConf != null) {
            logger.debug("delete vlan if is set ACL. NODEID=" + nodeId + " IFTYPE=" + ifType + " IFID=" + ifId);
            ret = false;
            break NG_FOUND;
          }

          VlanIfs vlanIfs = session.searchVlanIfs(nodeId, ifId);
          DummyVlanIfs dummy = session.searchDummyVlanIfsInfo(nodeId, ifId);
          if (vlanIfs == null && dummy == null) {
            logger.debug("not found delete target vlan. NODEID=" + nodeId + " VLANIFID=" + ifId);
            throw new DBAccessException(NO_DELETE_TARGET, BaseDAO.VLAN_IFS);
          }
          if (vlanIfs != null) {
            createPhysVlanListMapMap(delPhysVlanListMapMap, vlanIfs, nodeId);
          }
        }

        for (Map.Entry<String, List<VlanIfs>> allVlans : allVlanIfsMap.entrySet()) {
          String nodeId = allVlans.getKey();
          for (VlanIfs vlanIfs : allVlans.getValue()) {
            createPhysVlanListMapMap(allPhysVlanListMapMap, vlanIfs, nodeId);
          }
        }

        ret = checkLastPhysicalBaseVlnaSetAcl(allPhysVlanListMapMap, delPhysVlanListMapMap);
      }
      while (false);

      logger.trace(CommonDefinitions.END);
      return ret;
    }
  }

  /**
   * ListMapMap Generation. Device ID:{Physical IFID：[VLANIFID1, VLANIFID2]}
   *
   * @param physVlanListMapMap
   *          ListMapMap（IN/OUT）
   * @param vlanIfs
   *          VLAN Information
   * @param nodeId
   *          Device ID
   */
  protected static void createPhysVlanListMapMap(Map<String, Map<String, List<String>>> physVlanListMapMap,
      VlanIfs vlanIfs, String nodeId) {
    logger.trace(CommonDefinitions.START);
    logger.debug("[IN ] physVlanListMapMap=" + physVlanListMapMap);

    if (vlanIfs.getPhysical_if_id() != null) {
      if (physVlanListMapMap.containsKey(nodeId)) {
        Map<String, List<String>> delPhysVlanListMap = physVlanListMapMap.get(nodeId);
        if (delPhysVlanListMap.containsKey(vlanIfs.getPhysical_if_id())) {
          delPhysVlanListMap.get(vlanIfs.getPhysical_if_id()).add(vlanIfs.getVlan_if_id());
        } else {
          List<String> delPhysVlanList = new ArrayList<>();
          delPhysVlanList.add(vlanIfs.getVlan_if_id());
          delPhysVlanListMap.put(vlanIfs.getPhysical_if_id(), delPhysVlanList);
        }
      } else {
        Map<String, List<String>> delPhysVlanListMap = new HashMap<String, List<String>>();
        List<String> delPhysVlanList = new ArrayList<>();
        delPhysVlanList.add(vlanIfs.getVlan_if_id());
        delPhysVlanListMap.put(vlanIfs.getPhysical_if_id(), delPhysVlanList);
        physVlanListMapMap.put(nodeId, delPhysVlanListMap);
      }
    }
    logger.debug("[OUT] physVlanListMapMap=" + physVlanListMapMap);
    logger.trace(CommonDefinitions.END);
  }

  /**
   * Judging whether it is applicable to the case where the last VLAN of the relevant physical IF is to be deleted and also such physical IF is provided with ACL configuraiton（LAG is allowed).
   *
   * @param allPhysVlanListMapMap
   *          Mapping of information list map of physical VLAN to be deleted
   * @param delPhysVlanListMapMap
   *               Mapping of the entire VLAN information ListMap held by the device having the VLAN to be deleted
   * @return  Judgment Results
   */
  protected static boolean checkLastPhysicalBaseVlnaSetAcl(
      Map<String, Map<String, List<String>>> allPhysVlanListMapMap,
      Map<String, Map<String, List<String>>> delPhysVlanListMapMap) throws DBAccessException {
    logger.trace(CommonDefinitions.START);

    try (DBAccessManager session = new DBAccessManager()) {

      boolean ret = true;
      NG_FOUND: for (Map.Entry<String, Map<String, List<String>>> entry : delPhysVlanListMapMap.entrySet()) {
        String nodeId = entry.getKey();
        Map<String, List<String>> delPhysVlanListMap = entry.getValue();
        if (!allPhysVlanListMapMap.containsKey(nodeId)) {
          continue; 
        }
        Map<String, List<String>> allPhysVlanListMap = allPhysVlanListMapMap.get(nodeId);
        for (Map.Entry<String, List<String>> entry2 : delPhysVlanListMap.entrySet()) {
          String physIfId = entry2.getKey();
          List<String> delPhysVlanList = entry2.getValue();
          if (!allPhysVlanListMap.containsKey(physIfId)) {
            continue; 
          }
          List<String> allPhysVlanList = allPhysVlanListMap.get(physIfId);
          for (String delPhysVlan : delPhysVlanList) {
            allPhysVlanList.remove(allPhysVlanList.indexOf(delPhysVlan));
          }
          if (allPhysVlanList.isEmpty()) {
            AclConf aclConf = session.getAclConf(nodeId, physIfId, CommonDefinitions.IF_TYPE_PHYSICAL_IFS);
            if (aclConf != null) {
              logger.debug("delete vlan base physical last if is set ACL. NODEID=" + nodeId + " IFTYPE="
                  + CommonDefinitions.IF_TYPE_PHYSICAL_IFS + " IFID=" + physIfId);
              ret = false;
              break NG_FOUND;
            }
          }
        }
      }
      logger.trace(CommonDefinitions.END);
      return ret;
    } catch (DBAccessException dbae) {
      throw dbae;
    }
  }
}
