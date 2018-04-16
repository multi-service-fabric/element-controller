/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute;

import java.util.HashMap;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.ope.execute.constitution.allinfo.AllBreakoutIfInfoAcquisition;
import msf.ecmm.ope.execute.constitution.allinfo.AllDeviceInfoAcquisition;
import msf.ecmm.ope.execute.constitution.allinfo.AllDeviceTypeInfoAcquisition;
import msf.ecmm.ope.execute.constitution.allinfo.AllIfInfoAcquisition;
import msf.ecmm.ope.execute.constitution.allinfo.AllLagInfoAcquisition;
import msf.ecmm.ope.execute.constitution.allinfo.AllPhysicalIfInfoAcquisition;
import msf.ecmm.ope.execute.constitution.device.AcceptNodeRecover;
import msf.ecmm.ope.execute.constitution.device.BLeafAddition;
import msf.ecmm.ope.execute.constitution.device.BLeafRemove;
import msf.ecmm.ope.execute.constitution.device.DeviceInfoAcquisition;
import msf.ecmm.ope.execute.constitution.device.DeviceInfoRegistration;
import msf.ecmm.ope.execute.constitution.device.DeviceInfoRemove;
import msf.ecmm.ope.execute.constitution.device.LeafAddition;
import msf.ecmm.ope.execute.constitution.device.LeafChange;
import msf.ecmm.ope.execute.constitution.device.LeafRemove;
import msf.ecmm.ope.execute.constitution.device.NodeAddedNotification;
import msf.ecmm.ope.execute.constitution.device.NodeInfoAcquisition;
import msf.ecmm.ope.execute.constitution.device.NodeInfoRegistration;
import msf.ecmm.ope.execute.constitution.device.NodeRecover;
import msf.ecmm.ope.execute.constitution.device.SpineAddition;
import msf.ecmm.ope.execute.constitution.device.SpineRemove;
import msf.ecmm.ope.execute.constitution.interfaces.BetweenClustersLinkCreate;
import msf.ecmm.ope.execute.constitution.interfaces.BetweenClustersLinkDelete;
import msf.ecmm.ope.execute.constitution.interfaces.BreakoutIfCreate;
import msf.ecmm.ope.execute.constitution.interfaces.BreakoutIfDelete;
import msf.ecmm.ope.execute.constitution.interfaces.BreakoutIfInfoAcquisition;
import msf.ecmm.ope.execute.constitution.interfaces.LagCreate;
import msf.ecmm.ope.execute.constitution.interfaces.LagInfoAcquisition;
import msf.ecmm.ope.execute.constitution.interfaces.LagRemove;
import msf.ecmm.ope.execute.constitution.interfaces.PhysicalIfInfoAcquisition;
import msf.ecmm.ope.execute.constitution.interfaces.PhysicalIfInfoChange;
import msf.ecmm.ope.execute.controllerstatemanagement.ECMainLogAcquisition;
import msf.ecmm.ope.execute.controllerstatemanagement.ECMainStateSendNotification;
import msf.ecmm.ope.execute.controllerstatemanagement.ECStateManagement;
import msf.ecmm.ope.execute.cp.AllL2VlanIfChange;
import msf.ecmm.ope.execute.cp.AllL2VlanIfCreate;
import msf.ecmm.ope.execute.cp.AllL2VlanIfRemove;
import msf.ecmm.ope.execute.cp.AllL3VlanIfChange;
import msf.ecmm.ope.execute.cp.AllL3VlanIfCreate;
import msf.ecmm.ope.execute.cp.AllL3VlanIfRemove;
import msf.ecmm.ope.execute.cp.AllVlanIfInfoAcquisition;
import msf.ecmm.ope.execute.cp.VlanIfChange;
import msf.ecmm.ope.execute.cp.VlanIfInfoAcquisition;
import msf.ecmm.ope.execute.ecstate.ECMainStopper;
import msf.ecmm.ope.execute.ecstate.ObstructionStateController;
import msf.ecmm.ope.execute.notification.AllTrafficDataAcquisition;
import msf.ecmm.ope.execute.notification.SNMPTrapSignalRecieveNotification;
import msf.ecmm.ope.execute.notification.TrafficDataAcquisition;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Operation Generation Class Definition<br>
 * Generating instance for executing operation.
 *
 */
public class OperationFactory {

  /**
   * logger.
   */
  private static final Logger logger = LogManager.getLogger(CommonDefinitions.EC_LOGGER);

  /**
   * Running Instance Acquisition.
   *
   * @param opeType
   *          operation type
   * @param idt
   *          input message
   * @param ukm
   *          URI key information
   * @return instance for executing operation
   */
  public static Operation create(OperationType opeType, HashMap<String, String> ukm, AbstractRestMessage idt) {

    logger.trace(CommonDefinitions.START);
    logger.debug("opeType=" + opeType + ", ukm=" + ukm + ", idt=" + idt);

    Operation ret = null;

    if (opeType == OperationType.ECMainStopper) {
      ret = new ECMainStopper(idt, ukm);
    } else if (opeType == OperationType.ECMainStateConfirm) {
      ret = new ECStateManagement(idt, ukm);
    } else if (opeType == OperationType.ObstructionStateController) {
      ret = new ObstructionStateController(idt, ukm);
    } else if (opeType == OperationType.SNMPTrapSignalRecieveNotification) {
      ret = new SNMPTrapSignalRecieveNotification(idt, ukm);
    } else if (opeType == OperationType.TrafficDataAcquisition) {
      ret = new TrafficDataAcquisition(idt, ukm);
    } else if (opeType == OperationType.AllL2VlanIfCreate) {
      ret = new AllL2VlanIfCreate(idt, ukm);
    } else if (opeType == OperationType.AllL2VlanIfRemove) {
      ret = new AllL2VlanIfRemove(idt, ukm);
    } else if (opeType == OperationType.AllL2VlanIfChange) {
      ret = new AllL2VlanIfChange(idt, ukm);
    } else if (opeType == OperationType.AllL3VlanIfCreate) {
      ret = new AllL3VlanIfCreate(idt, ukm);
    } else if (opeType == OperationType.AllL3VlanIfRemove) {
      ret = new AllL3VlanIfRemove(idt, ukm);
    } else if (opeType == OperationType.AllL3VlanIfChange) {
      ret = new AllL3VlanIfChange(idt, ukm);
    } else if (opeType == OperationType.VlanIfChange) {
      ret = new VlanIfChange(idt, ukm);
    } else if (opeType == OperationType.DeviceInfoRegistration) {
      ret = new DeviceInfoRegistration(idt, ukm);
    } else if (opeType == OperationType.DeviceInfoAcquisition) {
      ret = new DeviceInfoAcquisition(idt, ukm);
    } else if (opeType == OperationType.DeviceInfoRemove) {
      ret = new DeviceInfoRemove(idt, ukm);
    } else if (opeType == OperationType.NodeAddedNotification) {
      ret = new NodeAddedNotification(idt, ukm);
    } else if (opeType == OperationType.LeafAddition) {
      ret = new LeafAddition(idt, ukm);
    } else if (opeType == OperationType.SpineAddition) {
      ret = new SpineAddition(idt, ukm);
    } else if (opeType == OperationType.LeafRemove) {
      ret = new LeafRemove(idt, ukm);
    } else if (opeType == OperationType.SpineRemove) {
      ret = new SpineRemove(idt, ukm);
    } else if (opeType == OperationType.NodeRecover) {
      ret = new NodeRecover(idt, ukm);
    } else if (opeType == OperationType.AcceptNodeRecover) {
      ret = new AcceptNodeRecover(idt, ukm);
    } else if (opeType == OperationType.LagCreate) {
      ret = new LagCreate(idt, ukm);
    } else if (opeType == OperationType.LagInfoAcquisition) {
      ret = new LagInfoAcquisition(idt, ukm);
    } else if (opeType == OperationType.LagRemove) {
      ret = new LagRemove(idt, ukm);
    } else if (opeType == OperationType.BreakoutIfCreate) {
      ret = new BreakoutIfCreate(idt, ukm);
    } else if (opeType == OperationType.BreakoutIfDelete) {
      ret = new BreakoutIfDelete(idt, ukm);
    } else if (opeType == OperationType.BetweenClustersLinkCreate) {
      ret = new BetweenClustersLinkCreate(idt, ukm);
    } else if (opeType == OperationType.BetweenClustersLinkDelete) {
      ret = new BetweenClustersLinkDelete(idt, ukm);
    } else if (opeType == OperationType.PhysicalIfInfoAcquisition) {
      ret = new PhysicalIfInfoAcquisition(idt, ukm);
    } else if (opeType == OperationType.PhysicalIfInfoChange) {
      ret = new PhysicalIfInfoChange(idt, ukm);
    } else if (opeType == OperationType.AllDeviceTypeInfoAcquisition) {
      ret = new AllDeviceTypeInfoAcquisition(idt, ukm);
    } else if (opeType == OperationType.AllIfInfoAcquisition) {
      ret = new AllIfInfoAcquisition(idt, ukm);
    } else if (opeType == OperationType.AllPhysicalIfInfoAcquisition) {
      ret = new AllPhysicalIfInfoAcquisition(idt, ukm);
    } else if (opeType == OperationType.AllLagInfoAcquisition) {
      ret = new AllLagInfoAcquisition(idt, ukm);
    } else if (opeType == OperationType.AllDeviceInfoAcquisition) {
      ret = new AllDeviceInfoAcquisition(idt, ukm);
    } else if (opeType == OperationType.AllBreakoutIfInfoAcquisition) {
      ret = new AllBreakoutIfInfoAcquisition(idt, ukm);
    } else if (opeType == OperationType.AllVlanIfInfoAcquisition) {
      ret = new AllVlanIfInfoAcquisition(idt, ukm);
    } else if (opeType == OperationType.NodeInfoAcquisition) {
      ret = new NodeInfoAcquisition(idt, ukm);
    } else if (opeType == OperationType.BreakoutIfInfoAcquisition) {
      ret = new BreakoutIfInfoAcquisition(idt, ukm);
    } else if (opeType == OperationType.VlanIfInfoAcquisition) {
      ret = new VlanIfInfoAcquisition(idt, ukm);
    } else if (opeType == OperationType.NodeInfoRegistration) {
      ret = new NodeInfoRegistration(idt, ukm);
    } else if (opeType == OperationType.BLeafAddition) {
      ret = new BLeafAddition(idt, ukm);
    } else if (opeType == OperationType.BLeafRemove) {
      ret = new BLeafRemove(idt, ukm);
    } else if (opeType == OperationType.LeafChange) {
      ret = new LeafChange(idt, ukm);
    } else if (opeType == OperationType.ECMainLogAcquisition) {
      ret = new ECMainLogAcquisition(idt, ukm);
    } else if (opeType == OperationType.ControllerStateSendNotification) {
      ret = new ECMainStateSendNotification(idt, ukm);
    } else if (opeType == OperationType.TrafficDataAllAcquisition) {
      ret = new AllTrafficDataAcquisition(idt, ukm);
    } else {
      ret = null;
    }

    logger.trace(CommonDefinitions.END + ", return=" + ret);

    return ret;
  }
}
