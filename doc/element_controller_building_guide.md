## element controller building guide

**Version 1.0**
**March 26, 2019**
**Copyright(c) 2019 Nippon Telegraph and Telephone Corporation**

This text describes how to generate EcMainModule.jar from Java files.

### files

|No.|files|Description|
|:----|:----|:----|
|1|jar_create.sh|Script file|
|2|msf/|Target Java files; See **directories**|
|3|lib/|Required library files; See **directories**|
|4|EcMainModule.jar|jar file to create|

### execute
~~~console
sh jar_create.sh
~~~

- create class files of java files under msf/ directory, existing in the same directory as jar_create.sh.
- combine all java files and generated class files into jar files. (create EcMainModule.jar)
- This script also includes a process of deleting generated class files existing under msf/ directory as preprocessing at the time of execution.

### directories
```
・★msf/
  |--ecmm/
  |  |--common/
  |  |  |--log/
  |  |  |  |--LogNotice.java
  |  |  |  |--MsfLogger.java
  |  |  |--CommandExecutor.java
  |  |  |--CommonDefinitions.java
  |  |  |--CommonDefinitionsNodeOsUpgrade.java
  |  |  |--CommonUtil.java
  |  |  |--InputStreamThread.java
  |  |  |--LogFormatter.java
  |  |  |--package-info.java
  |  |--config/
  |  |  |--ControllerFileUpgradeConfiguration.java
  |  |  |--DiskThresholdConfiguration.java
  |  |  |--EcConfiguration.java
  |  |  |--ExpandConfigInfo.java
  |  |  |--ExpandConfigurationBase.java
  |  |  |--ExpandOperation.java
  |  |  |--ExpandOperationDetailInfo.java
  |  |  |--NodeOsUpgradeConfiguration.java
  |  |  |--PingOspfNeighborConfiguration.java
  |  |  |--package-info.java
  |  |--convert/
  |  |  |--DbMapper.java
  |  |  |--EmMapper.java
  |  |  |--LogicalPhysicalConverter.java
  |  |  |--RestMapper.java
  |  |  |--package-info.java
  |  |--db/
  |  |  |--DBAccessException.java
  |  |  |--DBAccessManager.java
  |  |  |--SessionManager.java
  |  |  |--aclConf.hbm.xml
  |  |  |--aclConfDetail.hbm.xml
  |  |  |--bgpOptions.hbm.xml
  |  |  |--bootErrorMessages.hbm.xml
  |  |  |--breakoutIfs.hbm.xml
  |  |  |--dao/
  |  |  |  |--AclConfDAO.java
  |  |  |  |--AclConfDetailDAO.java
  |  |  |  |--BGPOptionsDAO.java
  |  |  |  |--BaseDAO.java
  |  |  |  |--BootErrorMessagesDAO.java
  |  |  |  |--BreakoutIfsDAO.java
  |  |  |  |--DummyVlanIfsDAO.java
  |  |  |  |--EgressQueueMenusDAO.java
  |  |  |  |--EquipmentIfsDAO.java
  |  |  |  |--EquipmentsDAO.java
  |  |  |  |--IRBInstanceInfoDAO.java
  |  |  |  |--IfNameRulesDAO.java
  |  |  |  |--LagIfsDAO.java
  |  |  |  |--LagMembersDAO.java
  |  |  |  |--NodesDAO.java
  |  |  |  |--NodesStartupNotificationDAO.java
  |  |  |  |--PhysicalIfsDAO.java
  |  |  |  |--RemarkMenusDAO.java
  |  |  |  |--StaticRouteOptionsDAO.java
  |  |  |  |--SystemStatusDAO.java
  |  |  |  |--VRRPOptionsDAO.java
  |  |  |  |--VlanIfsDAO.java
  |  |  |  |--package-info.java
  |  |  |--dummyVlanIfs.hbm.xml
  |  |  |--egressQueueMenu.hbm.xml
  |  |  |--equipmentIfs.hbm.xml
  |  |  |--equipments.hbm.xml
  |  |  |--ifNameRules.hbm.xml
  |  |  |--irbInstanceInfo.hbm.xml
  |  |  |--lagIfs.hbm.xml
  |  |  |--lagMembers.hbm.xml
  |  |  |--nodes.hbm.xml
  |  |  |--nodesStartupNotification.hbm.xml
  |  |  |--package-info.java
  |  |  |--physicalIfs.hbm.xml
  |  |  |--pojo/
  |  |  |  |--AclConf.java
  |  |  |  |--AclConfDetail.java
  |  |  |  |--BGPOptions.java
  |  |  |  |--BootErrorMessages.java
  |  |  |  |--BreakoutIfs.java
  |  |  |  |--DummyVlanIfs.java
  |  |  |  |--EgressQueueMenus.java
  |  |  |  |--EquipmentIfs.java
  |  |  |  |--Equipments.java
  |  |  |  |--IRBInstanceInfo.java
  |  |  |  |--IfNameRules.java
  |  |  |  |--InterfacesList.java
  |  |  |  |--LagIfs.java
  |  |  |  |--LagMembers.java
  |  |  |  |--Nodes.java
  |  |  |  |--NodesStartupNotification.java
  |  |  |  |--PhysicalIfs.java
  |  |  |  |--RemarkMenus.java
  |  |  |  |--StaticRouteOptions.java
  |  |  |  |--SystemStatus.java
  |  |  |  |--VRRPOptions.java
  |  |  |  |--VlanIfs.java
  |  |  |  |--package-info.java
  |  |  |--remarkMenu.hbm.xml
  |  |  |--staticRouteOptions.hbm.xml
  |  |  |--systemStatus.hbm.xml
  |  |  |--vlanIfs.hbm.xml
  |  |  |--vrrpOptions.hbm.xml
  |  |--devctr/
  |  |  |--ControllerFileUpgradeScriptExecutor.java
  |  |--devctrl/
  |  |  |--CreateInitialDeviceConfig.java
  |  |  |--DevctrlCommon.java
  |  |  |--DevctrlException.java
  |  |  |--DhcpController.java
  |  |  |--ExtendScriptController.java
  |  |  |--HttpdController.java
  |  |  |--NodeOsUpgradeScriptExecutor.java
  |  |  |--ScriptController.java
  |  |  |--SnmpController.java
  |  |  |--SyslogController.java
  |  |  |--XinetdController.java
  |  |  |--package-info.java
  |  |  |--pojo
  |  |  |  |--DhcpInfo.java
  |  |  |  |--InitialDeviceConfig.java
  |  |  |  |--PingData.java
  |  |  |  |--SnmpIfOperStatus.java
  |  |  |  |--SnmpIfTraffic.java
  |  |  |  |--package-info.java
  |  |--emctrl/
  |  |  |--ConfigAuditCycleManager.java
  |  |  |--ConfigAuditJob.java
  |  |  |--EmController.java
  |  |  |--EmctrlException.java
  |  |  |--RequestQueueEntry.java
  |  |  |--RestClientToEm.java
  |  |  |--package-info.java
  |  |  |--pojo/
  |  |  |  |--AbstractMessage.java
  |  |  |  |--AddAclSetting.java
  |  |  |  |--BLeafAddDelete.java
  |  |  |  |--BetweenClustersLinkAddDelete.java
  |  |  |  |--BodyMessage.java
  |  |  |  |--BreakoutIfAddDelete.java
  |  |  |  |--CeLagAddDelete.java
  |  |  |  |--CeLagIfsChange.java
  |  |  |  |--HeaderMessage.java
  |  |  |  |--IfStatusUpdate.java
  |  |  |  |--InternalLinkAddDelete.java
  |  |  |  |--InternalLinkLagIfsChange.java
  |  |  |  |--L2SliceAddDelete.java
  |  |  |  |--L3SliceAddDelete.java
  |  |  |  |--LeafAddDelete.java
  |  |  |  |--RecoverUpdateNode.java
  |  |  |  |--RecoverUpdateNodeNodeOsUpgrade.java
  |  |  |  |--RecoverUpdateService.java
  |  |  |  |--SpineAddDelete.java
  |  |  |  |--TargetMessage.java
  |  |  |  |--UpdateNodeInfo.java
  |  |  |  |--package-info.java
  |  |  |  |--parts/
  |  |  |  |  |--AclFilter.java
  |  |  |  |  |--Anycast.java
  |  |  |  |  |--AttributeOperation.java
  |  |  |  |  |--BreakoutIf.java
  |  |  |  |  |--CeInterface.java
  |  |  |  |  |--CeLagInterface.java
  |  |  |  |  |--Clag.java
  |  |  |  |  |--ClagBackup.java
  |  |  |  |  |--ClagHandOverInterface.java
  |  |  |  |  |--ClagPeer.java
  |  |  |  |  |--ClusterLink.java
  |  |  |  |  |--Cp.java
  |  |  |  |  |--Device.java
  |  |  |  |  |--DeviceLeaf.java
  |  |  |  |  |--DeviceLeafAcl.java
  |  |  |  |  |--DeviceNodeOsUpgrade.java
  |  |  |  |  |--DummyVlan.java
  |  |  |  |  |--Equipment.java
  |  |  |  |  |--EquipmentWithOperation.java
  |  |  |  |  |--InterfaceNames.java
  |  |  |  |  |--InterfaceStatusUpdate.java
  |  |  |  |  |--InternalInterface.java
  |  |  |  |  |--InternalInterfaceMember.java
  |  |  |  |  |--InternalLag.java
  |  |  |  |  |--Irb.java
  |  |  |  |  |--L2L3VpnAs.java
  |  |  |  |  |--L2L3VpnBgp.java
  |  |  |  |  |--L2L3VpnNeighbor.java
  |  |  |  |  |--L2Vpn.java
  |  |  |  |  |--L2VpnPim.java
  |  |  |  |  |--L3SliceBgp.java
  |  |  |  |  |--L3SliceOspf.java
  |  |  |  |  |--L3SliceStatic.java
  |  |  |  |  |--L3Vni.java
  |  |  |  |  |--L3Vpn.java
  |  |  |  |  |--LagMemberIf.java
  |  |  |  |  |--LeafDevice.java
  |  |  |  |  |--LeafInterface.java
  |  |  |  |  |--Loopback.java
  |  |  |  |  |--LoopbackInterface.java
  |  |  |  |  |--ManagementInterface.java
  |  |  |  |  |--Msdp.java
  |  |  |  |  |--MsdpPeer.java
  |  |  |  |  |--MultiHoming.java
  |  |  |  |  |--Ntp.java
  |  |  |  |  |--Ospf.java
  |  |  |  |  |--OspfAddNode.java
  |  |  |  |  |--OspfVirtualLink.java
  |  |  |  |  |--PhysicalIpAddress.java
  |  |  |  |  |--Qos.java
  |  |  |  |  |--Range.java
  |  |  |  |  |--Route.java
  |  |  |  |  |--Snmp.java
  |  |  |  |  |--Term.java
  |  |  |  |  |--Track.java
  |  |  |  |  |--TrackInterface.java
  |  |  |  |  |--VirtualGateway.java
  |  |  |  |  |--Vrf.java
  |  |  |  |  |--Vrrp.java
  |  |  |  |  |--XmlFloatElement.java
  |  |  |  |  |--XmlIntegerElement.java
  |  |  |  |  |--XmlStringElement.java
  |  |  |  |  |--package-info.java
  |  |  |--restpojo/
  |  |  |  |--AbstractRequest.java
  |  |  |  |--AbstractResponse.java
  |  |  |  |--CommonRequestToEm.java
  |  |  |  |--ConfigAuditFromEm.java
  |  |  |  |--ControllerLog.java
  |  |  |  |--ControllerStatusFromEm.java
  |  |  |  |--package-info.java
  |  |  |  |--parts/
  |  |  |  |  |--DiffFromEm.java
  |  |  |  |  |--LatestEmConfigFromEm.java
  |  |  |  |  |--LogConditions.java
  |  |  |  |  |--LogData.java
  |  |  |  |  |--LogInformation.java
  |  |  |  |  |--NeConfigFromEm.java
  |  |--fcctrl/
  |  |  |--RestClient.java
  |  |  |--RestClientException.java
  |  |  |--package-info.java
  |  |  |--pojo/
  |  |  |  |--AbstractRequest.java
  |  |  |  |--AbstractResponse.java
  |  |  |  |--CommonResponseFromFc.java
  |  |  |  |--ConfigAuditNotification.java
  |  |  |  |--ControllerStatusToFc.java
  |  |  |  |--LogNotificationToFc.java
  |  |  |  |--NotifyNodeStartUpToFc.java
  |  |  |  |--Operations.java
  |  |  |  |--ServerNotificationToFc.java
  |  |  |  |--UpdateLogicalIfStatus.java
  |  |  |  |--UpgradeOperationToFc.java
  |  |  |  |--package-info.java
  |  |  |  |--parts
  |  |  |  |  |--Controller.java
  |  |  |  |  |--ControllerLogToFc.java
  |  |  |  |  |--ControllerServerToFc.java
  |  |  |  |  |--CpuToFc.java
  |  |  |  |  |--DevicesToFc.java
  |  |  |  |  |--DiffToFc.java
  |  |  |  |  |--DiskToFc.java
  |  |  |  |  |--FailureInfoToFc.java
  |  |  |  |  |--IfsLogical.java
  |  |  |  |  |--LatestEmConfigToFc.java
  |  |  |  |  |--MemoryToFc.java
  |  |  |  |  |--NeConfigToFc.java
  |  |  |  |  |--NodeConfigToFc.java
  |  |  |  |  |--NodeLogical.java
  |  |  |  |  |--package-info.java
  |  |--ope/
  |  |  |--control/
  |  |  |  |--AbstractQueueEntryKey.java
  |  |  |  |--ECMainState.java
  |  |  |  |--EcSession.java
  |  |  |  |--NodeAdditionState.java
  |  |  |  |--NodeStateNotificationSender.java
  |  |  |  |--OperationControlManager.java
  |  |  |  |--OperationLockKey.java
  |  |  |  |--OperationQueueEntry.java
  |  |  |  |--RestRequestCount.java
  |  |  |  |--package-info.java
  |  |  |--execute/
  |  |  |  |--Operation.java
  |  |  |  |--OperationFactory.java
  |  |  |  |--OperationType.java
  |  |  |  |--constitution
  |  |  |  |  |--allinfo/
  |  |  |  |  |  |--AllBreakoutIfInfoAcquisition.java
  |  |  |  |  |  |--AllConfigAuditAcquisition.java
  |  |  |  |  |  |--AllConfigAuditAcquisitionThread.java
  |  |  |  |  |  |--AllConfigAuditUtility.java
  |  |  |  |  |  |--AllDeviceInfoAcquisition.java
  |  |  |  |  |  |--AllDeviceTypeInfoAcquisition.java
  |  |  |  |  |  |--AllIfInfoAcquisition.java
  |  |  |  |  |  |--AllLagInfoAcquisition.java
  |  |  |  |  |  |--AllLagInterfaceFilterAcquisition.java
  |  |  |  |  |  |--AllPhysicalIfInfoAcquisition.java
  |  |  |  |  |  |--AllPhysicalInterfaceFilterAcquisition.java
  |  |  |  |  |  |--AllVlanInterfaceFilterAcquisition.java
  |  |  |  |  |  |--package-info.java
  |  |  |  |  |--device/
  |  |  |  |  |  |--AcceptNodeRecover.java
  |  |  |  |  |  |--AcceptNodeRecoverNodeOsUpgrade.java
  |  |  |  |  |  |--AclUtilityDevice.java
  |  |  |  |  |  |--AddNodeException.java
  |  |  |  |  |  |--BLeafAddition.java
  |  |  |  |  |  |--BLeafAdditionPriorityLeafSpine.java
  |  |  |  |  |  |--BLeafRemove.java
  |  |  |  |  |  |--BLeafRemoveAcl.java
  |  |  |  |  |  |--ConfigAuditAcquisition.java
  |  |  |  |  |  |--ControllerSwitch.java
  |  |  |  |  |  |--ControllerSwitchThread.java
  |  |  |  |  |  |--DeviceInfoAcquisition.java
  |  |  |  |  |  |--DeviceInfoRegistration.java
  |  |  |  |  |  |--DeviceInfoRemove.java
  |  |  |  |  |  |--LeafAddition.java
  |  |  |  |  |  |--LeafAdditionPriorityLeafSpine.java
  |  |  |  |  |  |--LeafChange.java
  |  |  |  |  |  |--LeafRemove.java
  |  |  |  |  |  |--LeafRemoveAcl.java
  |  |  |  |  |  |--NodeAddedNotification.java
  |  |  |  |  |  |--NodeAddition.java
  |  |  |  |  |  |--NodeAdditionThread.java
  |  |  |  |  |  |--NodeAdditionThreadNodeOsUpgrade.java
  |  |  |  |  |  |--NodeChange.java
  |  |  |  |  |  |--NodeInfoAcquisition.java
  |  |  |  |  |  |--NodeInfoRegistration.java
  |  |  |  |  |  |--NodeInfoRegistrationPriorityLeafSpine.java
  |  |  |  |  |  |--NodeOsUpdate.java
  |  |  |  |  |  |--NodeOsUpgrade.java
  |  |  |  |  |  |--NodeOsUpgradeNotification.java
  |  |  |  |  |  |--NodeOsUpgradeThread.java
  |  |  |  |  |  |--NodeRecover.java
  |  |  |  |  |  |--NodeRecoverNodeOsUpgrade.java
  |  |  |  |  |  |--NodeRemove.java
  |  |  |  |  |  |--PriorityLeafSpineUtility.java
  |  |  |  |  |  |--SpineAddition.java
  |  |  |  |  |  |--SpineAdditionPriorityLeafSpine.java
  |  |  |  |  |  |--SpineRemove.java
  |  |  |  |  |  |--SpineRemoveAcl.java
  |  |  |  |  |  |--package-info.java
  |  |  |  |  |--interfaces/
  |  |  |  |  |  |--AddFilter.java
  |  |  |  |  |  |--BetweenClustersLinkCreate.java
  |  |  |  |  |  |--BetweenClustersLinkDelete.java
  |  |  |  |  |  |--BetweenClustersLinkDeleteAcl.java
  |  |  |  |  |  |--BreakoutIfCreate.java
  |  |  |  |  |  |--BreakoutIfDelete.java
  |  |  |  |  |  |--BreakoutIfInfoAcquisition.java
  |  |  |  |  |  |--DeleteFilter.java
  |  |  |  |  |  |--InternalLinkIfChange.java
  |  |  |  |  |  |--LagCreate.java
  |  |  |  |  |  |--LagIfInfoChange.java
  |  |  |  |  |  |--LagInfoAcquisition.java
  |  |  |  |  |  |--LagInterfaceFilterAcquisition.java
  |  |  |  |  |  |--LagRemove.java
  |  |  |  |  |  |--LagRemoveAcl.java
  |  |  |  |  |  |--PhysicalIfInfoAcquisition.java
  |  |  |  |  |  |--PhysicalIfInfoChange.java
  |  |  |  |  |  |--PhysicalIfInfoChangeAcl.java
  |  |  |  |  |  |--PhysicalInterfaceFilterAcquisition.java
  |  |  |  |  |  |--VlanInterfaceFilterAcquisition.java
  |  |  |  |  |  |--package-info.java
  |  |  |  |  |--package-info.java
  |  |  |  |--controllerstatemanagement/
  |  |  |  |  |--ECMainLogAcquisition.java
  |  |  |  |  |--ECMainStateSendNotification.java
  |  |  |  |  |--ECStateManagement.java
  |  |  |  |  |--LogNotification.java
  |  |  |  |  |--ResourceCheckCycleManager.java
  |  |  |  |  |--ResourceCheckJob.java
  |  |  |  |  |--ServerNotification.java
  |  |  |  |  |--package-info.java
  |  |  |  |--cp/
  |  |  |  |  |--AclUtilityCp.java
  |  |  |  |  |--AllL2VlanIfChange.java
  |  |  |  |  |--AllL2VlanIfCreate.java
  |  |  |  |  |--AllL2VlanIfRemove.java
  |  |  |  |  |--AllL2VlanIfRemoveAcl.java
  |  |  |  |  |--AllL3VlanIfChange.java
  |  |  |  |  |--AllL3VlanIfCreate.java
  |  |  |  |  |--AllL3VlanIfRemove.java
  |  |  |  |  |--AllL3VlanIfRemoveAcl.java
  |  |  |  |  |--AllVlanIfInfoAcquisition.java
  |  |  |  |  |--VlanIfChange.java
  |  |  |  |  |--VlanIfInfoAcquisition.java
  |  |  |  |  |--package-info.java
  |  |  |  |--ecstate/
  |  |  |  |  |--ECMainStarter.java
  |  |  |  |  |--ECMainStopper.java
  |  |  |  |  |--ObstructionStateController.java
  |  |  |  |  |--package-info.java
  |  |  |  |--notification/
  |  |  |  |  |--AllTrafficDataAcquisition.java
  |  |  |  |  |--ExecutePing.java
  |  |  |  |  |--ExecutePingThread.java
  |  |  |  |  |--IfBlockAndOpen.java
  |  |  |  |  |--OspfNeighborAcquisition.java
  |  |  |  |  |--OspfNeighborAcquisitionThread.java
  |  |  |  |  |--SNMPTrapSignalRecieveNotification.java
  |  |  |  |  |--TrafficDataAcquisition.java
  |  |  |  |  |--package-info.java
  |  |  |  |--package-info.java
  |  |  |--package-info.java
  |  |  |--receiver/
  |  |  |  |--ReceiverDefinitions.java
  |  |  |  |--RestServer.java
  |  |  |  |--package-info.java
  |  |  |  |--pojo/
  |  |  |  |  |--AbstractResponseMessage.java
  |  |  |  |  |--AbstractRestMessage.java
  |  |  |  |  |--AddDeleteFilter.java
  |  |  |  |  |--AddDeleteNode.java
  |  |  |  |  |--AddNode.java
  |  |  |  |  |--BulkCreateL2VlanIf.java
  |  |  |  |  |--BulkCreateL3VlanIf.java
  |  |  |  |  |--BulkDeleteL2VlanIf.java
  |  |  |  |  |--BulkDeleteL3VlanIf.java
  |  |  |  |  |--BulkUpdateL2VlanIf.java
  |  |  |  |  |--BulkUpdateL3VlanIf.java
  |  |  |  |  |--ChangeNode.java
  |  |  |  |  |--CheckDataException.java
  |  |  |  |  |--CheckEcMainModuleStatus.java
  |  |  |  |  |--CommonResponse.java
  |  |  |  |  |--ControlLogicalInterface.java
  |  |  |  |  |--CreateBetweenClustersLink.java
  |  |  |  |  |--CreateBreakoutIf.java
  |  |  |  |  |--CreateLagInterface.java
  |  |  |  |  |--DeleteBetweenClustersLink.java
  |  |  |  |  |--DeleteBreakoutIf.java
  |  |  |  |  |--DeleteNode.java
  |  |  |  |  |--EmControllerReceiveStatus.java
  |  |  |  |  |--ExecutePingRequest.java
  |  |  |  |  |--ExecutePingResult.java
  |  |  |  |  |--GetAllNodeTraffic.java
  |  |  |  |  |--GetBreakoutInterface.java
  |  |  |  |  |--GetBreakoutInterfaceList.java
  |  |  |  |  |--GetConfigAudit.java
  |  |  |  |  |--GetConfigAuditList.java
  |  |  |  |  |--GetControllerLog.java
  |  |  |  |  |--GetEquipmentType.java
  |  |  |  |  |--GetEquipmentTypeList.java
  |  |  |  |  |--GetInterfaceList.java
  |  |  |  |  |--GetLagInterface.java
  |  |  |  |  |--GetLagInterfaceFilter.java
  |  |  |  |  |--GetLagInterfaceFilterList.java
  |  |  |  |  |--GetLagInterfaceList.java
  |  |  |  |  |--GetNode.java
  |  |  |  |  |--GetNodeList.java
  |  |  |  |  |--GetNodeTraffic.java
  |  |  |  |  |--GetOspfNeighborInfoRequest.java
  |  |  |  |  |--GetOspfNeighborInfoResult.java
  |  |  |  |  |--GetPhysicalInterface.java
  |  |  |  |  |--GetPhysicalInterfaceFilter.java
  |  |  |  |  |--GetPhysicalInterfaceFilterList.java
  |  |  |  |  |--GetPhysicalInterfaceList.java
  |  |  |  |  |--GetVlanInterface.java
  |  |  |  |  |--GetVlanInterfaceFilter.java
  |  |  |  |  |--GetVlanInterfaceFilterList.java
  |  |  |  |  |--GetVlanInterfaceList.java
  |  |  |  |  |--IfBlockAndOpenRequest.java
  |  |  |  |  |--InternalLinkChange.java
  |  |  |  |  |--NodeInfoUpdate.java
  |  |  |  |  |--NodeOsUpgradeRequest.java
  |  |  |  |  |--NotifyEmStatusLog.java
  |  |  |  |  |--NotifyEmStatusServer.java
  |  |  |  |  |--NotifyNodeStartUp.java
  |  |  |  |  |--NotifyOsUpgrade.java
  |  |  |  |  |--NotifyReceiveSnmpTrap.java
  |  |  |  |  |--Operations.java
  |  |  |  |  |--RecoverNodeService.java
  |  |  |  |  |--RegisterEquipmentType.java
  |  |  |  |  |--UpdateLagInterface.java
  |  |  |  |  |--UpdatePhysicalInterface.java
  |  |  |  |  |--UpdateVlanIf.java
  |  |  |  |  |--package-info.java
  |  |  |  |  |--parts/
  |  |  |  |  |  |--AddFilters.java
  |  |  |  |  |  |--AddressInfo.java
  |  |  |  |  |  |--As.java
  |  |  |  |  |  |--BaseIf.java
  |  |  |  |  |  |--BaseIfInfo.java
  |  |  |  |  |  |--Bgp.java
  |  |  |  |  |  |--BgpAddNode.java
  |  |  |  |  |  |--BgpCreateL3cp.java
  |  |  |  |  |  |--BreakoutBaseIf.java
  |  |  |  |  |  |--BreakoutIf.java
  |  |  |  |  |  |--BreakoutIfId.java
  |  |  |  |  |  |--Capabilities.java
  |  |  |  |  |  |--ClusterLink.java
  |  |  |  |  |  |--Controller.java
  |  |  |  |  |  |--ControllerInfo.java
  |  |  |  |  |  |--ControllerLog.java
  |  |  |  |  |  |--ControllerServer.java
  |  |  |  |  |  |--Cpu.java
  |  |  |  |  |  |--CreateNode.java
  |  |  |  |  |  |--CreateVlanIfs.java
  |  |  |  |  |  |--DeleteFilters.java
  |  |  |  |  |  |--DeviceInfo.java
  |  |  |  |  |  |--Diff.java
  |  |  |  |  |  |--Disk.java
  |  |  |  |  |  |--EcEmLog.java
  |  |  |  |  |  |--EcStatus.java
  |  |  |  |  |  |--Egress.java
  |  |  |  |  |  |--EmStatus.java
  |  |  |  |  |  |--Equipment.java
  |  |  |  |  |  |--EquipmentAddNode.java
  |  |  |  |  |  |--EquipmentIf.java
  |  |  |  |  |  |--EquipmentRegisterNode.java
  |  |  |  |  |  |--FailureInfo.java
  |  |  |  |  |  |--GetTerms.java
  |  |  |  |  |  |--IfFilter.java
  |  |  |  |  |  |--IfNameRule.java
  |  |  |  |  |  |--IfSearchIf.java
  |  |  |  |  |  |--Informations.java
  |  |  |  |  |  |--InterfaceInfo.java
  |  |  |  |  |  |--InterfaceInfoTraffic.java
  |  |  |  |  |  |--InternalLinkIf.java
  |  |  |  |  |  |--InternalLinkIfInternalLinkChange.java
  |  |  |  |  |  |--InternalLinkIfsDeleteNode.java
  |  |  |  |  |  |--InternalLinkInfo.java
  |  |  |  |  |  |--InternalLinkOppo.java
  |  |  |  |  |  |--IrbCapabilities.java
  |  |  |  |  |  |--IrbUpdateValue.java
  |  |  |  |  |  |--IrbValue.java
  |  |  |  |  |  |--L2L3vpn.java
  |  |  |  |  |  |--L2VlanOption.java
  |  |  |  |  |  |--L3VlanOption.java
  |  |  |  |  |  |--L3VniValue.java
  |  |  |  |  |  |--L3vpn.java
  |  |  |  |  |  |--LagIf.java
  |  |  |  |  |  |--LagIfChangeLagIf.java
  |  |  |  |  |  |--LagIfCreateLagIf.java
  |  |  |  |  |  |--LagIfFilter.java
  |  |  |  |  |  |--LagMember.java
  |  |  |  |  |  |--LagMembersBreakoutIfs.java
  |  |  |  |  |  |--LagMembersPhysicalIfs.java
  |  |  |  |  |  |--LatestEmConfig.java
  |  |  |  |  |  |--LogConditions.java
  |  |  |  |  |  |--LogData.java
  |  |  |  |  |  |--LogInformation.java
  |  |  |  |  |  |--LoopBackInterface.java
  |  |  |  |  |  |--Memory.java
  |  |  |  |  |  |--MultiHomingValue.java
  |  |  |  |  |  |--NeConfig.java
  |  |  |  |  |  |--Neighbor.java
  |  |  |  |  |  |--Node.java
  |  |  |  |  |  |--NodeChangeNode.java
  |  |  |  |  |  |--NodeConfig.java
  |  |  |  |  |  |--NodeConfigAll.java
  |  |  |  |  |  |--NodeDeleteNode.java
  |  |  |  |  |  |--NodeInterface.java
  |  |  |  |  |  |--NodeOsUpgrade.java
  |  |  |  |  |  |--NodeRecoverNode.java
  |  |  |  |  |  |--NodeRegisterNode.java
  |  |  |  |  |  |--OppositeNodesDeleteNode.java
  |  |  |  |  |  |--OppositeNodesInterface.java
  |  |  |  |  |  |--OsInfo.java
  |  |  |  |  |  |--OspfNeighborIfsResponse.java
  |  |  |  |  |  |--OspfNeighborsRequest.java
  |  |  |  |  |  |--OspfNeighborsResponse.java
  |  |  |  |  |  |--PairNode.java
  |  |  |  |  |  |--PhysicalIf.java
  |  |  |  |  |  |--PhysicalIfFilter.java
  |  |  |  |  |  |--PhysicalIfsCreateLagIf.java
  |  |  |  |  |  |--PingTargetsRequest.java
  |  |  |  |  |  |--PingTargetsResponse.java
  |  |  |  |  |  |--QInQCapabilities.java
  |  |  |  |  |  |--QosCapabilities.java
  |  |  |  |  |  |--QosConfigValues.java
  |  |  |  |  |  |--QosGetVlanIfs.java
  |  |  |  |  |  |--QosRegisterEquipment.java
  |  |  |  |  |  |--QosUpdateVlanIf.java
  |  |  |  |  |  |--QosValues.java
  |  |  |  |  |  |--Remark.java
  |  |  |  |  |  |--RemoveUpdateVlanIfs.java
  |  |  |  |  |  |--Shaping.java
  |  |  |  |  |  |--StaticRoute.java
  |  |  |  |  |  |--SwitchTraffic.java
  |  |  |  |  |  |--TargetIf.java
  |  |  |  |  |  |--Terms.java
  |  |  |  |  |  |--TrackingIf.java
  |  |  |  |  |  |--Traffic.java
  |  |  |  |  |  |--TrafficValue.java
  |  |  |  |  |  |--UnusePhysicalIf.java
  |  |  |  |  |  |--UpdateDeleteNode.java
  |  |  |  |  |  |--UpdateNode.java
  |  |  |  |  |  |--UpdateInternalLinkIfs.java
  |  |  |  |  |  |--UpdateOption.java
  |  |  |  |  |  |--UpdateStaticRoute.java
  |  |  |  |  |  |--UpdateVlanIfs.java
  |  |  |  |  |  |--Varbind.java
  |  |  |  |  |  |--VirtualLink.java
  |  |  |  |  |  |--VirtualLinkCluster.java
  |  |  |  |  |  |--VlanIf.java
  |  |  |  |  |  |--VlanIfFilter.java
  |  |  |  |  |  |--VlanIfUpdateOption.java
  |  |  |  |  |  |--VlanIfsBulkUpdate.java
  |  |  |  |  |  |--VlanIfsCreateL3VlanIf.java
  |  |  |  |  |  |--VlanIfsDeleteVlanIf.java
  |  |  |  |  |  |--Vpn.java
  |  |  |  |  |  |--Vrrp.java
  |  |  |  |  |  |--VrrpCreateVlanIf.java
  |  |  |  |  |  |--Ztp.java
  |  |  |  |  |  |--package-info.java
  |  |  |  |--resources/
  |  |  |  |  |--BaseResource.java
  |  |  |  |  |--exceptionmapper/
  |  |  |  |  |  |--RestServerExceptionMapper.java
  |  |  |  |  |--package-info.java
  |  |  |  |  |--v1/
  |  |  |  |  |  |--ClusterLink.java
  |  |  |  |  |  |--ConfigAudit.java
  |  |  |  |  |  |--Cps.java
  |  |  |  |  |  |--EcMainModule.java
  |  |  |  |  |  |--EquipmentType.java
  |  |  |  |  |  |--Filter.java
  |  |  |  |  |  |--Interface.java
  |  |  |  |  |  |--InterfacePriorityNode.java
  |  |  |  |  |  |--Node.java
  |  |  |  |  |  |--NodeOsUpgradeNotificationRecv.java
  |  |  |  |  |  |--NodeOsUpgradeRecv.java
  |  |  |  |  |  |--NodeTraffic.java
  |  |  |  |  |  |--PATCH.java
  |  |  |  |  |  |--PingOspf.java
  |  |  |  |  |  |--Switchover.java
  |  |  |  |  |  |--VlanInterface.java
  |  |  |  |  |  |--package-info.java
  |  |--traffic/
  |  |  |--DataGatheringExecutor.java
  |  |  |--GatheringExecuterWatchDog.java
  |  |  |--IntegrityExecuterWatchDog.java
  |  |  |--IntegrityExecutor.java
  |  |  |--IntegrityJob.java
  |  |  |--InterfaceIntegrityValidationManager.java
  |  |  |--TrafficDataGatheringManager.java
  |  |  |--TrafficGatherJob.java
  |  |  |--package-info.java
  |  |  |--pojo/
  |  |  |  |--DeviceInformationSet.java
  |  |  |  |--NodeKeySet.java
  |  |  |  |--TrafficData.java
  |  |  |  |--package-info.java
```

```
・★lib/
  |--NetConf.jar
  |--antlr-2.7.7.jar
  |--c3p0-0.9.2.1.jar
  |--commons-io-2.5.jar
  |--dom4j-1.6.1.jar
  |--geronimo-jta_1.1_spec-1.1.1.jar
  |--gson-2.7.jar
  |--hibernate-c3p0-5.0.10.Final.jar
  |--hibernate-commons-annotations-5.0.1.Final.jar
  |--hibernate-core-5.0.10.Final.jar
  |--hibernate-jpa-2.1-api-1.0.0.Final.jar
  |--hk2-api-2.5.0-b05.jar
  |--hk2-locator-2.5.0-b05.jar
  |--hk2-utils-2.5.0-b05.jar
  |--javassist-3.18.1-GA.jar
  |--javax.annotation-api-1.2.jar
  |--javax.inject-2.5.0-b05.jar
  |--javax.ws.rs-api-2.0.1.jar
  |--jboss-logging-3.3.0.Final.jar
  |--jersey-client.jar
  |--jersey-common.jar
  |--jersey-container-servlet-core.jar
  |--jersey-guava-2.23.2.jar
  |--jersey-server.jar
  |--jetty-http-9.3.11.v20160721.jar
  |--jetty-io-9.3.11.v20160721.jar
  |--jetty-security-9.3.11.v20160721.jar
  |--jetty-server-9.3.11.v20160721.jar
  |--jetty-servlet-9.3.11.v20160721.jar
  |--jetty-util-9.3.11.v20160721.jar
  |--jsch-0.1.53.jar
  |--log4j-api-2.6.2.jar
  |--log4j-core-2.6.2.jar
  |--log4j-slf4j-impl-2.0-rc2.jar
  |--log4j-slf4j-impl-2.6.2.jar
  |--mchange-commons-java-0.2.3.4.jar
  |--org.eclipse.persistence.core.jar
  |--postgresql-9.4.1209.jre7.jar
  |--quartz-2.2.3.jar
  |--servlet-api-3.1.jar
  |--slf4j-api-1.7.21.jar
  |--slf4j-simple-1.7.21.jar
  |--snmp4j-2.5.0.jar
  |--validation-api-1.1.0.Final.jar
```
