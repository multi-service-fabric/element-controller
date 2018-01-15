/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute;

/**
 * Operation Type Class Definition. Defining the enumeration type of operation type.
 */
public enum OperationType {

  /** Not Classified. */
  None(0),

  /** EC Start-up. */
  ECMainStarter(1),
  /** EC Termination. */
  ECMainStopper(2),
  /** EC Status Confirmation. */
  ECMainStateConfirm(3),
  /** EC Blockage Status Change. */
  ObstructionStateController(4),
  /** EC Log Acquisition. */
  ECMainLogAcquisition(5),
  /** Controller Status Notification. */
  ControllerStateSendNotification(6),

  /** VLAN IF Change. */
  VlanIfChange(1001),
  /** L3VLAN IF Batch Generation. */
  AllL3VlanIfCreate(1002),
  /** L2VLAN IF Batch Generation/Change. */
  AllL2VlanIfCreate(1003),
  /** L3VLAN IF Batch Deletion. */
  AllL3VlanIfRemove(1004),
  /** L2VLAN IF Batch Deletion/Change. */
  AllL2VlanIfRemove(1005),

  /** SNMPTrap Reception Notification. */
  SNMPTrapSignalRecieveNotification(2001),
  /** Traffic Information Acquisition. */
  TrafficDataAcquisition(2002),
  /** Traffic Information List Acquisition. */
  TrafficDataAllAcquisition(2003),

  /** Model Information Registration. */
  DeviceInfoRegistration(3001),
  /** Model Information Acquisition. */
  DeviceInfoAcquisition(3002),
  /** Model Information Deletion. */
  DeviceInfoRemove(3003),
  /** Leaf Device Extention. */
  LeafAddition(3008),
  /** Spine Device Extention. */
  SpineAddition(3009),
  /** Leaf Device Removal. */
  LeafRemove(3012),
  /** Spine Device Removal. */
  SpineRemove(3013),
  /** Device Start-up Notification. */
  NodeAddedNotification(3014),
  /** B-Leaf Device Extention. */
  BLeafAddition(3015),
  /** B-Leaf Device Removal. */
  BLeafRemove(3016),
  /** Device Information Acquisition. */
  NodeInfoAcquisition(3017),
  /** Device Extention/Change. */
  NodeInfoRegistration(3018),
  /** Device Change. */
  LeafChange(3019),

  /** Physical IF Information Acquisition. */
  PhysicalIfInfoAcquisition(3101),
  /** Physical IF Information Change. */
  PhysicalIfInfoChange(3102),
  /** LagIF Generation. */
  LagCreate(3104),
  /** LagIF Information Acquisition. */
  LagInfoAcquisition(3105),
  /** LagIF Deletion. */
  LagRemove(3106),
  /** breakoutIF Addition. */
  BreakoutIfCreate(3107),
  /** breakoutIF Deletion. */
  BreakoutIfDelete(3108),
  /** Inter-Cluster Link Generation. */
  BetweenClustersLinkCreate(3109),
  /** Inter-Cluster Link Deletion. */
  BetweenClustersLinkDelete(3110),
  /** VLANIF Information Acquisition. */
  VlanIfInfoAcquisition(3112),
  /** BreakoutIF Information Acquisition. */
  BreakoutIfInfoAcquisition(3113),

  /** Model List Information Acqisition. */
  AllDeviceTypeInfoAcquisition(3301),
  /** Device Information List Acquisition. */
  AllDeviceInfoAcquisition(3302),
  /** IF Information List Acquisition. */
  AllIfInfoAcquisition(3305),
  /** Physical IF Information List Acquisition. */
  AllPhysicalIfInfoAcquisition(3306),
  /** LagIF Information List Acquisition. */
  AllLagInfoAcquisition(3308),
  /** VLANIF Information List Acquisition. */
  AllVlanIfInfoAcquisition(3309),
  /** BreakoutIF Information List Acquisition. */
  AllBreakoutIfInfoAcquisition(3310),

  /** Traffic Data Collection Periodic Execution. */
  TrafficDataGathering(4001),
  /** IF Status Integration Periodic Execution. */
  IFStateIntegrity(4002);

  /** Japanese Name Label. */
  private int value;

  /**
   * Constructor.
   *
   * @param typeNum
   *          Japanese Name Label
   */
  private OperationType(int typeNum) {
    this.value = typeNum;
  }

  /**
   * Getting OperationType.
   *
   * @return numeric value
   */
  public int getValue() {
    return this.value;
  }

  /**
   * Converting Japanese label to enum type.
   *
   * @param status
   *          operation type
   * @return Japanese name label
   */
  public static String operationTypeToLabel(OperationType status) {

    String statusStr = "";

    if (status.name().equals(OperationType.None.name())) {
      statusStr = "未分類";
    } else if (status.name().equals(OperationType.ECMainStarter.name())) {
      statusStr = "EC起動";
    } else if (status.name().equals(OperationType.ECMainStopper.name())) {
      statusStr = "EC停止";
    } else if (status.name().equals(OperationType.ECMainStateConfirm.name())) {
      statusStr = "EC状態確認";
    } else if (status.name().equals(OperationType.ObstructionStateController.name())) {
      statusStr = "EC閉塞状態変更";
    } else if (status.name().equals(OperationType.VlanIfChange.name())) {
      statusStr = "VLAN IF変更";
    } else if (status.name().equals(OperationType.AllL3VlanIfCreate.name())) {
      statusStr = "L3VLAN IF一括生成";
    } else if (status.name().equals(OperationType.AllL2VlanIfCreate.name())) {
      statusStr = "L2VLAN IF一括生成・変更";
    } else if (status.name().equals(OperationType.AllL3VlanIfRemove.name())) {
      statusStr = "L3VLAN IF一括削除";
    } else if (status.name().equals(OperationType.AllL2VlanIfRemove.name())) {
      statusStr = "L2VLAN IF一括削除・変更";
    } else if (status.name().equals(OperationType.SNMPTrapSignalRecieveNotification.name())) {
      statusStr = "SNMPTrap受信通知";
    } else if (status.name().equals(OperationType.TrafficDataAcquisition.name())) {
      statusStr = "トラヒック情報取得";
    } else if (status.name().equals(OperationType.DeviceInfoRegistration.name())) {
      statusStr = "機種情報登録";
    } else if (status.name().equals(OperationType.DeviceInfoAcquisition.name())) {
      statusStr = "機種情報取得";
    } else if (status.name().equals(OperationType.DeviceInfoRemove.name())) {
      statusStr = "機種情報削除";
    } else if (status.name().equals(OperationType.LeafAddition.name())) {
      statusStr = "Leaf装置増設";
    } else if (status.name().equals(OperationType.SpineAddition.name())) {
      statusStr = "Spine装置増設";
    } else if (status.name().equals(OperationType.LeafRemove.name())) {
      statusStr = "Leaf装置減設";
    } else if (status.name().equals(OperationType.SpineRemove.name())) {
      statusStr = "Spine装置減設";
    } else if (status.name().equals(OperationType.NodeInfoRegistration.name())) {
      statusStr = "装置増設・変更";
    } else if (status.name().equals(OperationType.NodeAddedNotification.name())) {
      statusStr = "装置起動通知";
    } else if (status.name().equals(OperationType.PhysicalIfInfoAcquisition.name())) {
      statusStr = "物理IF情報取得";
    } else if (status.name().equals(OperationType.PhysicalIfInfoChange.name())) {
      statusStr = "物理IF情報変更";
    } else if (status.name().equals(OperationType.LagCreate.name())) {
      statusStr = "LagIF生成";
    } else if (status.name().equals(OperationType.LagInfoAcquisition.name())) {
      statusStr = "LagIF情報取得";
    } else if (status.name().equals(OperationType.LagRemove.name())) {
      statusStr = "LagIF削除";
    } else if (status.name().equals(OperationType.BreakoutIfCreate.name())) {
      statusStr = "breakoutIF追加";
    } else if (status.name().equals(OperationType.BreakoutIfDelete.name())) {
      statusStr = "breakoutIF削除";
    } else if (status.name().equals(OperationType.BreakoutIfInfoAcquisition.name())) {
      statusStr = "breakoutIF情報取得";
    } else if (status.name().equals(OperationType.AllBreakoutIfInfoAcquisition.name())) {
      statusStr = "breakoutIF情報一覧取得";
    } else if (status.name().equals(OperationType.BetweenClustersLinkCreate.name())) {
      statusStr = "クラスタ間リンク生成";
    } else if (status.name().equals(OperationType.BetweenClustersLinkDelete.name())) {
      statusStr = "クラスタ間リンク削除";
    } else if (status.name().equals(OperationType.AllDeviceTypeInfoAcquisition.name())) {
      statusStr = "機種一覧情報取得";
    } else if (status.name().equals(OperationType.NodeInfoAcquisition.name())) {
      statusStr = "装置情報取得";
    } else if (status.name().equals(OperationType.AllDeviceInfoAcquisition.name())) {
      statusStr = "装置情報一覧取得";
    } else if (status.name().equals(OperationType.AllIfInfoAcquisition.name())) {
      statusStr = "IF情報一覧取得";
    } else if (status.name().equals(OperationType.AllPhysicalIfInfoAcquisition.name())) {
      statusStr = "物理IF情報一覧取得";
    } else if (status.name().equals(OperationType.AllLagInfoAcquisition.name())) {
      statusStr = "LagIF情報一覧取得";
    } else if (status.name().equals(OperationType.TrafficDataGathering.name())) {
      statusStr = "トラヒックデータ収集定期実行";
    } else if (status.name().equals(OperationType.IFStateIntegrity.name())) {
      statusStr = "IF状態整合定期実行";
    } else if (status.name().equals(OperationType.VlanIfInfoAcquisition.name())) {
      statusStr = "VLANIF情報取得";
    } else if (status.name().equals(OperationType.AllVlanIfInfoAcquisition.name())) {
      statusStr = "VLANIF情報一覧取得";
    } else if (status.name().equals(OperationType.ECMainLogAcquisition.name())) {
      statusStr = "ECログ取得";
    } else if (status.name().equals(OperationType.ControllerStateSendNotification.name())) {
      statusStr = "コントローラ状態通知";
    } else if (status.name().equals(OperationType.TrafficDataAllAcquisition.name())) {
      statusStr = "トラヒック情報一覧取得";
    }

    return statusStr;
  }
}
