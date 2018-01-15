## element controller building guide

**Version 1.0**
**December 26, 2017**
**Copyright(c) 2017 Nippon Telegraph and Telephone Corporation**

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
- This script also includes a process of deleting generated class files existing under mfs/ directory as preprocessing at the time of execution.

### directories
```
・★msf/
  |--ecmm/
  |  |--common/
  |  |  |--CommandExecutor.java
  |  |  |--CommonDefinitions.java
  |  |  |--CommonUtil.java
  |  |  |--InputStreamThread.java
  |  |  |--LogFormatter.java
  |  |  |--package-info.java
  |  |--config/
  |  |  |--EcConfiguration.java
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
  |  |  |--bgpOptions.hbm.xml
  |  |  |--bootErrorMessages.hbm.xml
  |  |  |--breakoutIfs.hbm.xml
  |  |  |--dao/
  |  |  |  |--BGPOptionsDAO.java
  |  |  |  |--BaseDAO.java
  |  |  |  |--BootErrorMessagesDAO.java
  |  |  |  |--BreakoutIfsDAO.java
  |  |  |  |--EquipmentIfsDAO.java
  |  |  |  |--EquipmentsDAO.java
  |  |  |  |--IfNameRulesDAO.java
  |  |  |  |--LagIfsDAO.java
  |  |  |  |--LagMembersDAO.java
  |  |  |  |--NodesDAO.java
  |  |  |  |--NodesStartupNotificationDAO.java
  |  |  |  |--PhysicalIfsDAO.java
  |  |  |  |--StaticRouteOptionsDAO.java
  |  |  |  |--SystemStatusDAO.java
  |  |  |  |--VRRPOptionsDAO.java
  |  |  |  |--VlanIfsDAO.java
  |  |  |  |--package-info.java
  |  |  |--equipmentIfs.hbm.xml
  |  |  |--equipments.hbm.xml
  |  |  |--ifNameRules.hbm.xml
  |  |  |--lagIfs.hbm.xml
  |  |  |--lagMembers.hbm.xml
  |  |  |--nodes.hbm.xml
  |  |  |--nodesStartupNotification.hbm.xml
  |  |  |--package-info.java
  |  |  |--physicalIfs.hbm.xml
  |  |  |--pojo/
  |  |  |  |--BGPOptions.java
  |  |  |  |--BootErrorMessages.java
  |  |  |  |--BreakoutIfs.java
  |  |  |  |--EquipmentIfs.java
  |  |  |  |--Equipments.java
  |  |  |  |--IfNameRules.java
  |  |  |  |--InterfacesList.java
  |  |  |  |--LagIfs.java
  |  |  |  |--LagMembers.java
  |  |  |  |--Nodes.java
  |  |  |  |--NodesStartupNotification.java
  |  |  |  |--PhysicalIfs.java
  |  |  |  |--StaticRouteOptions.java
  |  |  |  |--SystemStatus.java
  |  |  |  |--VRRPOptions.java
  |  |  |  |--VlanIfs.java
  |  |  |  |--package-info.java
  |  |  |--staticRouteOptions.hbm.xml
  |  |  |--systemStatus.hbm.xml
  |  |  |--vlanIfs.hbm.xml
  |  |  |--vrrpOptions.hbm.xml
  |  |--devctrl/
  |  |  |--DevctrlCommon.java
  |  |  |--DevctrlException.java
  |  |  |--DhcpController.java
  |  |  |--SnmpController.java
  |  |  |--SyslogController.java
  |  |  |--package-info.java
  |  |  |--pojo/
  |  |  |  |--DhcpInfo.java
  |  |  |  |--SnmpIfOperStatus.java
  |  |  |  |--SnmpIfTraffic.java
  |  |  |  |--package-info.java
  |  |--emctrl/
  |  |  |--EmController.java
  |  |  |--EmctrlException.java
  |  |  |--RequestQueueEntry.java
  |  |  |--RestClientToEm.java
  |  |  |--package-info.java
  |  |  |--pojo/
  |  |  |  |--AbstractMessage.java
  |  |  |  |--BLeafAddDelete.java
  |  |  |  |--BetweenClustersLinkAddDelete.java
  |  |  |  |--BodyMessage.java
  |  |  |  |--BreakoutIfAddDelete.java
  |  |  |  |--CeLagAddDelete.java
  |  |  |  |--HeaderMessage.java
  |  |  |  |--InternalLinkAddDelete.java
  |  |  |  |--L2SliceAddDelete.java
  |  |  |  |--L3SliceAddDelete.java
  |  |  |  |--LeafAddDelete.java
  |  |  |  |--SpineAddDelete.java
  |  |  |  |--TargetMessage.java
  |  |  |  |--package-info.java
  |  |  |  |--parts/
  |  |  |  |  |--AttributeOperation.java
  |  |  |  |  |--BreakoutIf.java
  |  |  |  |  |--CeInterface.java
  |  |  |  |  |--CeLagInterface.java
  |  |  |  |  |--ClusterLink.java
  |  |  |  |  |--Cp.java
  |  |  |  |  |--Device.java
  |  |  |  |  |--DeviceLeaf.java
  |  |  |  |  |--Equipment.java
  |  |  |  |  |--InternalInterface.java
  |  |  |  |  |--InternalInterfaceMember.java
  |  |  |  |  |--InternalLag.java
  |  |  |  |  |--L2L3VpnAs.java
  |  |  |  |  |--L2L3VpnBgp.java
  |  |  |  |  |--L2L3VpnNeighbor.java
  |  |  |  |  |--L2Vpn.java
  |  |  |  |  |--L2VpnPim.java
  |  |  |  |  |--L3SliceBgp.java
  |  |  |  |  |--L3SliceOspf.java
  |  |  |  |  |--L3SliceStatic.java
  |  |  |  |  |--L3Vpn.java
  |  |  |  |  |--LagMemberIf.java
  |  |  |  |  |--LeafDevice.java
  |  |  |  |  |--LeafInterface.java
  |  |  |  |  |--LoopbackInterface.java
  |  |  |  |  |--ManagementInterface.java
  |  |  |  |  |--Msdp.java
  |  |  |  |  |--MsdpPeer.java
  |  |  |  |  |--Ntp.java
  |  |  |  |  |--Ospf.java
  |  |  |  |  |--OspfAddNode.java
  |  |  |  |  |--OspfVirtualLink.java
  |  |  |  |  |--Range.java
  |  |  |  |  |--Route.java
  |  |  |  |  |--Snmp.java
  |  |  |  |  |--Track.java
  |  |  |  |  |--TrackInterface.java
  |  |  |  |  |--Vrf.java
  |  |  |  |  |--Vrrp.java
  |  |  |  |  |--package-info.java
  |  |  |--restpojo/
  |  |  |  |--AbstractRequest.java
  |  |  |  |--AbstractResponse.java
  |  |  |  |--CommonRequestToEm.java
  |  |  |  |--ControllerLog.java
  |  |  |  |--ControllerStatusFromEm.java
  |  |  |  |--package-info.java
  |  |  |  |--parts/
  |  |  |  |  |--LogConditions.java
  |  |  |  |  |--LogData.java
  |  |  |  |  |--LogInformation.java
  |  |--fcctrl/
  |  |  |--RestClient.java
  |  |  |--RestClientException.java
  |  |  |--package-info.java
  |  |  |--pojo/
  |  |  |  |--AbstractRequest.java
  |  |  |  |--AbstractResponse.java
  |  |  |  |--CommonResponseFromFc.java
  |  |  |  |--ControllerStatusToFc.java
  |  |  |  |--NotifyNodeStartUpToFc.java
  |  |  |  |--Operations.java
  |  |  |  |--UpdateLogicalIfStatus.java
  |  |  |  |--package-info.java
  |  |  |  |--parts/
  |  |  |  |  |--Controller.java
  |  |  |  |  |--IfsLogical.java
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
  |  |  |  |  |  |--AllDeviceInfoAcquisition.java
  |  |  |  |  |  |--AllDeviceTypeInfoAcquisition.java
  |  |  |  |  |  |--AllIfInfoAcquisition.java
  |  |  |  |  |  |--AllLagInfoAcquisition.java
  |  |  |  |  |  |--AllPhysicalIfInfoAcquisition.java
  |  |  |  |  |  |--package-info.java
  |  |  |  |  |--device/
  |  |  |  |  |  |--AddNodeException.java
  |  |  |  |  |  |--BLeafAddition.java
  |  |  |  |  |  |--BLeafRemove.java
  |  |  |  |  |  |--DeviceInfoAcquisition.java
  |  |  |  |  |  |--DeviceInfoRegistration.java
  |  |  |  |  |  |--DeviceInfoRemove.java
  |  |  |  |  |  |--LeafAddition.java
  |  |  |  |  |  |--LeafChange.java
  |  |  |  |  |  |--LeafRemove.java
  |  |  |  |  |  |--NodeAddedNotification.java
  |  |  |  |  |  |--NodeAddition.java
  |  |  |  |  |  |--NodeAdditionThread.java
  |  |  |  |  |  |--NodeChange.java
  |  |  |  |  |  |--NodeInfoAcquisition.java
  |  |  |  |  |  |--NodeInfoRegistration.java
  |  |  |  |  |  |--NodeRemove.java
  |  |  |  |  |  |--SpineAddition.java
  |  |  |  |  |  |--SpineRemove.java
  |  |  |  |  |  |--package-info.java
  |  |  |  |  |--interfaces/
  |  |  |  |  |  |--BetweenClustersLinkCreate.java
  |  |  |  |  |  |--BetweenClustersLinkDelete.java
  |  |  |  |  |  |--BreakoutIfCreate.java
  |  |  |  |  |  |--BreakoutIfDelete.java
  |  |  |  |  |  |--BreakoutIfInfoAcquisition.java
  |  |  |  |  |  |--LagCreate.java
  |  |  |  |  |  |--LagInfoAcquisition.java
  |  |  |  |  |  |--LagRemove.java
  |  |  |  |  |  |--PhysicalIfInfoAcquisition.java
  |  |  |  |  |  |--PhysicalIfInfoChange.java
  |  |  |  |  |  |--package-info.java
  |  |  |  |  |--package-info.java
  |  |  |  |--controllerstatemanagement
  |  |  |  |  |--ECMainLogAcquisition.java
  |  |  |  |  |--ECMainStateSendNotification.java
  |  |  |  |  |--ECStateManagement.java
  |  |  |  |  |--package-info.java
  |  |  |  |--cp/
  |  |  |  |  |--AllL2VlanIfCreate.java
  |  |  |  |  |--AllL2VlanIfRemove.java
  |  |  |  |  |--AllL3VlanIfCreate.java
  |  |  |  |  |--AllL3VlanIfRemove.java
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
  |  |  |  |  |--AddDeleteNode.java
  |  |  |  |  |--AddNode.java
  |  |  |  |  |--BulkCreateL2VlanIf.java
  |  |  |  |  |--BulkCreateL3VlanIf.java
  |  |  |  |  |--BulkDeleteL2VlanIf.java
  |  |  |  |  |--BulkDeleteL3VlanIf.java
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
  |  |  |  |  |--GetAllNodeTraffic.java
  |  |  |  |  |--GetBreakoutInterface.java
  |  |  |  |  |--GetBreakoutInterfaceList.java
  |  |  |  |  |--GetControllerLog.java
  |  |  |  |  |--GetEquipmentType.java
  |  |  |  |  |--GetEquipmentTypeList.java
  |  |  |  |  |--GetInterfaceList.java
  |  |  |  |  |--GetLagInterface.java
  |  |  |  |  |--GetLagInterfaceList.java
  |  |  |  |  |--GetNode.java
  |  |  |  |  |--GetNodeList.java
  |  |  |  |  |--GetNodeTraffic.java
  |  |  |  |  |--GetPhysicalInterface.java
  |  |  |  |  |--GetPhysicalInterfaceList.java
  |  |  |  |  |--GetVlanInterface.java
  |  |  |  |  |--GetVlanInterfaceList.java
  |  |  |  |  |--NotifyNodeStartUp.java
  |  |  |  |  |--NotifyReceiveSnmpTrap.java
  |  |  |  |  |--Operations.java
  |  |  |  |  |--RegisterEquipmentType.java
  |  |  |  |  |--UpdatePhysicalInterface.java
  |  |  |  |  |--UpdateVlanIf.java
  |  |  |  |  |--package-info.java
  |  |  |  |  |--parts
  |  |  |  |  |  |--AddressInfo.java
  |  |  |  |  |  |--As.java
  |  |  |  |  |  |--BaseIf.java
  |  |  |  |  |  |--BaseIfCreateVlanIf.java
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
  |  |  |  |  |  |--Cpu.java
  |  |  |  |  |  |--CreateNode.java
  |  |  |  |  |  |--CreateVlanIfs.java
  |  |  |  |  |  |--DeviceInfo.java
  |  |  |  |  |  |--Disk.java
  |  |  |  |  |  |--EcEmLog.java
  |  |  |  |  |  |--EcStatus.java
  |  |  |  |  |  |--EmStatus.java
  |  |  |  |  |  |--Equipment.java
  |  |  |  |  |  |--EquipmentAddNode.java
  |  |  |  |  |  |--EquipmentIf.java
  |  |  |  |  |  |--EquipmentRegisterNode.java
  |  |  |  |  |  |--IfNameRule.java
  |  |  |  |  |  |--IfSearchIf.java
  |  |  |  |  |  |--Informations.java
  |  |  |  |  |  |--InterfaceInfo.java
  |  |  |  |  |  |--InterfaceInfoTraffic.java
  |  |  |  |  |  |--InternalLinkIf.java
  |  |  |  |  |  |--InternalLinkIfsDeleteNode.java
  |  |  |  |  |  |--InternalLinkInfo.java
  |  |  |  |  |  |--L2L3vpn.java
  |  |  |  |  |  |--L3VlanOption.java
  |  |  |  |  |  |--L3vpn.java
  |  |  |  |  |  |--LagIf.java
  |  |  |  |  |  |--LagIfCreateLagIf.java
  |  |  |  |  |  |--LagMember.java
  |  |  |  |  |  |--LagMembersBreakoutIfs.java
  |  |  |  |  |  |--LagMembersPhysicalIfs.java
  |  |  |  |  |  |--LogConditions.java
  |  |  |  |  |  |--LogData.java
  |  |  |  |  |  |--LogInformation.java
  |  |  |  |  |  |--Memory.java
  |  |  |  |  |  |--Neighbor.java
  |  |  |  |  |  |--Node.java
  |  |  |  |  |  |--NodeChangeNode.java
  |  |  |  |  |  |--NodeDeleteNode.java
  |  |  |  |  |  |--NodeInterface.java
  |  |  |  |  |  |--NodeRegisterNode.java
  |  |  |  |  |  |--OppositeNodesDeleteNode.java
  |  |  |  |  |  |--OppositeNodesInterface.java
  |  |  |  |  |  |--OsInfo.java
  |  |  |  |  |  |--PairNode.java
  |  |  |  |  |  |--PhysicalIf.java
  |  |  |  |  |  |--PhysicalIfsCreateLagIf.java
  |  |  |  |  |  |--StaticRoute.java
  |  |  |  |  |  |--SwitchTraffic.java
  |  |  |  |  |  |--TargetIf.java
  |  |  |  |  |  |--TrackingIf.java
  |  |  |  |  |  |--Traffic.java
  |  |  |  |  |  |--TrafficValue.java
  |  |  |  |  |  |--UnusePhysicalIf.java
  |  |  |  |  |  |--UpdateDeleteNode.java
  |  |  |  |  |  |--UpdateNode.java
  |  |  |  |  |  |--UpdateOption.java
  |  |  |  |  |  |--UpdateStaticRoute.java
  |  |  |  |  |  |--UpdateVlanIfs.java
  |  |  |  |  |  |--Varbind.java
  |  |  |  |  |  |--VirtualLink.java
  |  |  |  |  |  |--VirtualLinkCluster.java
  |  |  |  |  |  |--VlanIf.java
  |  |  |  |  |  |--VlanIfUpdateOption.java
  |  |  |  |  |  |--VlanIfsCreateL3VlanIf.java
  |  |  |  |  |  |--VlanIfsDeleteVlanIf.java
  |  |  |  |  |  |--Vpn.java
  |  |  |  |  |  |--Vrrp.java
  |  |  |  |  |  |--VrrpCreateVlanIf.java
  |  |  |  |  |  |--Ztp.java
  |  |  |  |  |  |--package-info.java
  |  |  |  |--resources
  |  |  |  |  |--BaseResource.java
  |  |  |  |  |--exceptionmapper
  |  |  |  |  |  |--RestServerExceptionMapper.java
  |  |  |  |  |--package-info.java
  |  |  |  |  |--v1
  |  |  |  |  |  |--ClusterLink.java
  |  |  |  |  |  |--Cps.java
  |  |  |  |  |  |--EcMainModule.java
  |  |  |  |  |  |--EquipmentType.java
  |  |  |  |  |  |--Interface.java
  |  |  |  |  |  |--Node.java
  |  |  |  |  |  |--NodeTraffic.java
  |  |  |  |  |  |--VlanInterface.java
  |  |  |  |  |  |--package-info.java
  |  |--traffic
  |  |  |--DataGatheringExecutor.java
  |  |  |--GatheringExecuterWatchDog.java
  |  |  |--IntegrityExecuterWatchDog.java
  |  |  |--IntegrityExecutor.java
  |  |  |--IntegrityJob.java
  |  |  |--InterfaceIntegrityValidationManager.java
  |  |  |--TrafficDataGatheringManager.java
  |  |  |--TrafficGatherJob.java
  |  |  |--package-info.java
  |  |  |--pojo
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
