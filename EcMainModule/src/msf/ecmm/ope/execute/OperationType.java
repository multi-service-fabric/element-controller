package msf.ecmm.ope.execute;

public enum OperationType {

	ECMainStarter(1),
	ECMainStateConfirm(3),
	L3CPChange(1001),
	AllL2CPCreate(1003),
	AllL2CPRemove(1005),

	TrafficDataAcquisition(2002),

	DeviceInfoAcquisition(3002),
	LeafInfoRegistration(3004),
	LeafAddition(3008),
	LeafInfoAcquisition(3010),
	LeafRemove(3012),
	NodeAddedNotification(3014),

	PhysicalIfInfoChange(3102),
	LagCreate(3104),
	LagRemove(3106),

	AllDeviceInfoAcquisition(3302),
	AllSpineInfoAcquisition(3304),
	AllPhysicalIfInfoAcquisition(3306),
	AllLagInfoAcquisition(3308),

	IFStateIntegrity(4002),;


   private OperationType(int n) {
	   this.value = n;
   }

   public int getValue() {
	   return this.value;
   }

   public static String operationTypeToLabel(OperationType status) {

	   String statusStr = "";

	   if (status.name().equals(OperationType.None.name())) {
		   statusStr = "未分類";
	   } else if (status.name().equals(OperationType.ECMainStarter.name())) {
		   statusStr = "ECメインモジュール起動";
	   } else if (status.name().equals(OperationType.ECMainStopper.name())) {
		   statusStr = "ECメインモジュール停止";
	   } else if (status.name().equals(OperationType.ECMainStateConfirm.name())) {
		   statusStr = "ECメインモジュール状態確認";
	   } else if (status.name().equals(OperationType.ObstructionStateController.name())) {
		   statusStr = "ECメインモジュール閉塞状態変更";
	   } else if (status.name().equals(OperationType.L3CPChange.name())) {
		   statusStr = "L3CP変更";
	   } else if (status.name().equals(OperationType.AllL3CPCreate.name())) {
		   statusStr = "L3CP一括生成";
	   } else if (status.name().equals(OperationType.AllL2CPCreate.name())) {
		   statusStr = "L2CP一括生成";
	   } else if (status.name().equals(OperationType.AllL3CPRemove.name())) {
		   statusStr = "L3CP一括削除";
	   } else if (status.name().equals(OperationType.AllL2CPRemove.name())) {
		   statusStr = "L2CP一括削除";
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
	   } else if (status.name().equals(OperationType.LeafInfoRegistration.name())) {
		   statusStr = "Leaf増設情報登録";
	   } else if (status.name().equals(OperationType.SpineInfoRegistration.name())) {
		   statusStr = "Spine増設情報登録";
	   } else if (status.name().equals(OperationType.LeafAddition.name())) {
		   statusStr = "Leaf装置増設";
	   } else if (status.name().equals(OperationType.SpineAddition.name())) {
		   statusStr = "Spine装置増設";
	   } else if (status.name().equals(OperationType.LeafInfoAcquisition.name())) {
		   statusStr = "Leaf情報取得";
	   } else if (status.name().equals(OperationType.SpineInfoAcquisition.name())) {
		   statusStr = "Spine情報取得";
	   } else if (status.name().equals(OperationType.LeafRemove.name())) {
		   statusStr = "Leaf装置減設";
	   } else if (status.name().equals(OperationType.SpineRemove.name())) {
		   statusStr = "Spine装置減設";
	   } else if (status.name().equals(OperationType.NodeAddedNotification.name())) {
		   statusStr = "装置起動通知";
	   } else if (status.name().equals(OperationType.PhysicalIfInfoAcquisition.name())) {
		   statusStr = "物理IF情報取得";
	   } else if (status.name().equals(OperationType.PhysicalIfInfoChange.name())) {
		   statusStr = "物理IF情報変更";
	   } else if (status.name().equals(OperationType.InternalIfInfoAcquisition.name())) {
		   statusStr = "内部リンクIF情報取得";
	   } else if (status.name().equals(OperationType.LagCreate.name())) {
		   statusStr = "LagIF生成";
	   } else if (status.name().equals(OperationType.LagInfoAcquisition.name())) {
		   statusStr = "LagIF情報取得";
	   } else if (status.name().equals(OperationType.LagRemove.name())) {
		   statusStr = "LagIF削除";
	   } else if (status.name().equals(OperationType.AllDeviceTypeInfoAcquisition.name())) {
		   statusStr = "機種一覧情報取得";
	   } else if (status.name().equals(OperationType.AllDeviceInfoAcquisition.name())) {
		   statusStr = "装置情報一覧取得";
	   } else if (status.name().equals(OperationType.AllLeafInfoAcquisition.name())) {
		   statusStr = "Leaf情報一覧取得";
	   } else if (status.name().equals(OperationType.AllSpineInfoAcquisition.name())) {
		   statusStr = "Spine情報一覧取得";
	   } else if (status.name().equals(OperationType.AllIfInfoAcquisition.name())) {
		   statusStr = "IF情報一覧取得";
	   } else if (status.name().equals(OperationType.AllPhysicalIfInfoAcquisition.name())) {
		   statusStr = "物理IF情報一覧取得";
	   } else if (status.name().equals(OperationType.AllInternalIfInfoAcquisition.name())) {
		   statusStr = "内部リンクIF情報一覧取得";
	   } else if (status.name().equals(OperationType.AllLagInfoAcquisition.name())) {
		   statusStr = "LagIF情報一覧取得";
	   }else if (status.name().equals(OperationType.TrafficDataGathering.name())) {
		   statusStr = "トラヒックデータ収集定期実行";
	   } else if (status.name().equals(OperationType.IFStateIntegrity.name())) {
		   statusStr = "IF状態整合定期実行";
	   }


	   return statusStr;
   }
}
