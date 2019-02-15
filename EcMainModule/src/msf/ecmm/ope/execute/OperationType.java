/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute;

import java.util.HashMap;
import java.util.Map;

import msf.ecmm.config.ExpandOperation;
import msf.ecmm.config.ExpandOperationDetailInfo;

/**
 * Operation Type Class Definition. Defining the enumeration type of operation type.
 */
public class OperationType {

  /** Not classified. */
  public static int None = 0;


 /** EC Start-up. */
  public static final int ECMainStarter = 1;
  /** EC Termination. */
  public static final int ECMainStopper = 2;
  /** EC Status Confirmation. */
  public static final int ECMainStateConfirm = 3;
  /** EC Blockage Status Change. */
  public static final int ObstructionStateController = 4;
  /** EC Log Acquisition. */
  public static final int ECMainLogAcquisition = 5;
  /** Controller Status Notification. */
  public static final int ControllerStateSendNotification = 6;

  /** VLAN IF Change. */
  public static final int VlanIfChange = 1001;
  /** L3VLAN IF Batch Generation. */
  public static final int AllL3VlanIfCreate = 1002;
  /** L2VLAN IF Batch Generation/Change. */
  public static final int AllL2VlanIfCreate = 1003;
  /** L3VLAN IF Batch Deletion. */
  public static final int AllL3VlanIfRemove = 1004;
   /** L2VLAN IF Batch Deletion/Change. */
  public static final int AllL2VlanIfRemove = 1005;
 /** L3VLAN IF Batch Change. */
  public static final int AllL3VlanIfChange = 1006;
  /** L2VLAN IF Batch Change. */
  public static final int AllL2VlanIfChange = 1007;

   /** SNMPTrap Reception Notification. */
  public static final int SNMPTrapSignalRecieveNotification = 2001;
 /** Traffic Information Acquisition. */
  public static final int TrafficDataAcquisition = 2002;
  /** Traffic Information List Acquisition. */
  public static final int TrafficDataAllAcquisition = 2003;

  /** Model Information Registration. */
  public static final int DeviceInfoRegistration = 3001;
  /** Model Information Acquisition. */
  public static final int DeviceInfoAcquisition = 3002;
  /** Model Information Deletion. */
  public static final int DeviceInfoRemove = 3003;
   /** Leaf Device Extention. */
  public static final int LeafAddition = 3008;
  /** Spine Device Extention. */
  public static final int SpineAddition = 3009;
  /** Leaf Device Removal. */
  public static final int LeafRemove = 3012;
  /** Spine Device Removal. */
  public static final int SpineRemove = 3013;
  /** Device Start-up Notification. */
  public static final int NodeAddedNotification = 3014;
  /** B-Leaf Device Extention. */
  public static final int BLeafAddition = 3015;
  /** B-Leaf Device Removal. */
  public static final int BLeafRemove = 3016;
   /** Device Information Acquisition. */
  public static final int NodeInfoAcquisition = 3017;
  /** Device Extention/Change. */
  public static final int NodeInfoRegistration = 3018;
  /** Device Change. */
  public static final int LeafChange = 3019;
   /** Additional service recovery. */
  public static final int NodeRecover = 3020;
 /** Acception Additional service recovery. */
  public static final int AcceptNodeRecover = 3021;

  /** Physical IF Information Acquisition. */
  public static final int PhysicalIfInfoAcquisition = 3101;
 /** Physical IF Information Change. */
  public static final int PhysicalIfInfoChange = 3102;
  /** LagIF Generation. */
  public static final int LagCreate = 3104;
  /** LagIF Information Acquisition. */
  public static final int LagInfoAcquisition = 3105;
  /** LagIF Deletion. */
  public static final int LagRemove = 3106;
  /** breakoutIF Addition. */
  public static final int BreakoutIfCreate = 3107;
  /** breakoutIF Deletion. */
  public static final int BreakoutIfDelete = 3108;
  /** Inter-Cluster Link Generation. */
  public static final int BetweenClustersLinkCreate = 3109;
  /** Inter-Cluster Link Deletion. */
  public static final int BetweenClustersLinkDelete = 3110;
  /** VLANIF Information Acquisition. */
  public static final int VlanIfInfoAcquisition = 3112;
   /** BreakoutIF Information Acquisition. */
  public static final int BreakoutIfInfoAcquisition = 3113;

   /** Model List Information Acqisition. */
  public static final int AllDeviceTypeInfoAcquisition = 3301;
  /** Device Information List Acquisition. */
  public static final int AllDeviceInfoAcquisition = 3302;
  /** IF Information List Acquisition. */
  public static final int AllIfInfoAcquisition = 3305;
 /** Physical IF Information List Acquisition. */
  public static final int AllPhysicalIfInfoAcquisition = 3306;
   /** LagIF Information List Acquisition. */
  public static final int AllLagInfoAcquisition = 3308;
  /** VLANIF Information List Acquisition. */
  public static final int AllVlanIfInfoAcquisition = 3309;
  /** BreakoutIF Information List Acquisition. */
  public static final int AllBreakoutIfInfoAcquisition = 3310;

  /** Traffic Data Collection Periodic Execution. */
  public static final int TrafficDataGathering = 4001;
  /** IF Status Integration Periodic Execution. */
  public static final int IFStateIntegrity = 4002;

  /** Extension operation (priority). */
  public static final int __ExpandPrimaryOperation = -1;
  /** Extension operation (normal). */
  public static final int __ExpandNormalOperation = -2;
  /** Extension operaton (lock necessary). */
  public static final int __ExpandNeedLockOperation = -3;


  /**
   * Operation type MAP.  Map for searchig operation name by using operation ID
   * Allowing this map to include the extension function operation defined in the extension function config. 
   */
  private static Map<Integer, String> data = new HashMap<>();

  /** Operation type. */
  private int opeType = None;

  /** Operation name. */
  private String opeName = "";

  /**
   * Initilization. Calling after loading the extension function config.
   */
  public static void init() {

    data.put(None, "None");
    data.put(ECMainStarter, "ECMainStarter");
    data.put(ECMainStopper, "ECMainStopper");
    data.put(ECMainStateConfirm, "ECMainStateConfirm");
    data.put(ObstructionStateController, "ObstructionStateController");
    data.put(ECMainLogAcquisition, "ECMainLogAcquisition");
    data.put(ControllerStateSendNotification, "ControllerStateSendNotification");
    data.put(VlanIfChange, "VlanIfChange");
    data.put(AllL3VlanIfCreate, "AllL3VlanIfCreate");
    data.put(AllL2VlanIfCreate, "AllL2VlanIfCreate");
    data.put(AllL3VlanIfRemove, "AllL3VlanIfRemove");
    data.put(AllL2VlanIfRemove, "AllL2VlanIfRemove");
    data.put(AllL3VlanIfChange, "AllL3VlanIfChange");
    data.put(AllL2VlanIfChange, "AllL2VlanIfChange");
    data.put(SNMPTrapSignalRecieveNotification, "SNMPTrapSignalRecieveNotification");
    data.put(TrafficDataAcquisition, "TrafficDataAcquisition");
    data.put(TrafficDataAllAcquisition, "TrafficDataAllAcquisition");
    data.put(DeviceInfoRegistration, "DeviceInfoRegistration");
    data.put(DeviceInfoAcquisition, "DeviceInfoAcquisition");
    data.put(DeviceInfoRemove, "DeviceInfoRemove");
    data.put(LeafAddition, "LeafAddition");
    data.put(SpineAddition, "SpineAddition");
    data.put(LeafRemove, "LeafRemove");
    data.put(SpineRemove, "SpineRemove");
    data.put(NodeAddedNotification, "NodeAddedNotification");
    data.put(BLeafAddition, "BLeafAddition");
    data.put(BLeafRemove, "BLeafRemove");
    data.put(NodeInfoAcquisition, "NodeInfoAcquisition");
    data.put(NodeInfoRegistration, "NodeInfoRegistration");
    data.put(LeafChange, "LeafChange");
    data.put(NodeRecover, "NodeRecover");
    data.put(AcceptNodeRecover, "AcceptNodeRecover");
    data.put(PhysicalIfInfoAcquisition, "PhysicalIfInfoAcquisition");
    data.put(PhysicalIfInfoChange, "PhysicalIfInfoChange");
    data.put(LagCreate, "LagCreate");
    data.put(LagInfoAcquisition, "LagInfoAcquisition");
    data.put(LagRemove, "LagRemove");
    data.put(BreakoutIfCreate, "BreakoutIfCreate");
    data.put(BreakoutIfDelete, "BreakoutIfDelete");
    data.put(BetweenClustersLinkCreate, "BetweenClustersLinkCreate");
    data.put(BetweenClustersLinkDelete, "BetweenClustersLinkDelete");
    data.put(VlanIfInfoAcquisition, "VlanIfInfoAcquisition");
    data.put(BreakoutIfInfoAcquisition, "BreakoutIfInfoAcquisition");
    data.put(AllDeviceTypeInfoAcquisition, "AllDeviceTypeInfoAcquisition");
    data.put(AllDeviceInfoAcquisition, "AllDeviceInfoAcquisition");
    data.put(AllIfInfoAcquisition, "AllIfInfoAcquisition");
    data.put(AllPhysicalIfInfoAcquisition, "AllPhysicalIfInfoAcquisition");
    data.put(AllLagInfoAcquisition, "AllLagInfoAcquisition");
    data.put(AllVlanIfInfoAcquisition, "AllVlanIfInfoAcquisition");
    data.put(AllBreakoutIfInfoAcquisition, "AllBreakoutIfInfoAcquisition");
    data.put(TrafficDataGathering, "TrafficDataGathering");
    data.put(IFStateIntegrity, "IFStateIntegrity");

    for (String expandOpeName : ExpandOperation.getInstance().getExpandOperationList()) {
      ExpandOperationDetailInfo expandOpe = ExpandOperation.getInstance().get(expandOpeName);
      data.put(expandOpe.getOperationTypeId(), expandOpe.getOperationTypeName());
    }
  }

  /**
   * Operation ID-> name conversion.
   *
   * @param opeType
   *          Operation ID
   * @return Operation name
   */
  public static String name(int opeType) {
    return data.get(opeType);
  }

  /**
   * Constructor
   *
   * @param opeType
   *          Operation ID
   */
  public OperationType(int opeType) {
    String opeName = data.get(opeType);
    if (opeName == null || opeName.isEmpty()) {
      throw new IllegalArgumentException();
    }
    this.opeType = opeType;
    this.opeName = opeName;
  }

  /**
   * Getting operation name.
   *
   * @return operation name
   */
  public String name() {
    return opeName;
  }

  /**
   * Getting operation type(numeric value).
   *
   * @return numeric value
   */
  public int getValue() {
    return this.opeType;
  }
}
