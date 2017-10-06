package msf.ecmm.ope.execute;

import java.util.HashMap;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.ope.execute.constitution.allinfo.AllDeviceTypeInfoAcquisition;
import msf.ecmm.ope.execute.constitution.allinfo.AllIfInfoAcquisition;
import msf.ecmm.ope.execute.constitution.allinfo.AllLagInfoAcquisition;
import msf.ecmm.ope.execute.constitution.allinfo.AllPhysicalIfInfoAcquisition;
import msf.ecmm.ope.execute.constitution.device.DeviceInfoAcquisition;
import msf.ecmm.ope.execute.constitution.device.DeviceInfoRegistration;
import msf.ecmm.ope.execute.constitution.device.DeviceInfoRemove;
import msf.ecmm.ope.execute.constitution.device.LeafAddition;
import msf.ecmm.ope.execute.constitution.device.LeafInfoRegistration;
import msf.ecmm.ope.execute.constitution.device.LeafRemove;
import msf.ecmm.ope.execute.constitution.device.NodeAddedNotification;
import msf.ecmm.ope.execute.constitution.device.SpineAddition;
import msf.ecmm.ope.execute.constitution.device.SpineInfoRegistration;
import msf.ecmm.ope.execute.constitution.device.SpineRemove;
import msf.ecmm.ope.execute.constitution.interfaces.LagCreate;
import msf.ecmm.ope.execute.constitution.interfaces.LagInfoAcquisition;
import msf.ecmm.ope.execute.constitution.interfaces.LagRemove;
import msf.ecmm.ope.execute.constitution.interfaces.PhysicalIfInfoAcquisition;
import msf.ecmm.ope.execute.constitution.interfaces.PhysicalIfInfoChange;
import msf.ecmm.ope.execute.cp.AllL2CPCreate;
import msf.ecmm.ope.execute.cp.AllL2CPRemove;
import msf.ecmm.ope.execute.cp.AllL3CPCreate;
import msf.ecmm.ope.execute.cp.AllL3CPRemove;
import msf.ecmm.ope.execute.cp.L3CPChange;
import msf.ecmm.ope.execute.ecstate.ECMainStateConfirm;
import msf.ecmm.ope.execute.ecstate.ECMainStopper;
import msf.ecmm.ope.execute.ecstate.ObstructionStateController;
import msf.ecmm.ope.execute.notification.SNMPTrapSignalRecieveNotification;
import msf.ecmm.ope.execute.notification.TrafficDataAcquisition;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OperationFactory {

	private static final Logger logger = LogManager.getLogger(CommonDefinitions.EC_LOGGER);

	public static Operation create(OperationType opeType,HashMap<String,String> ukm,AbstractRestMessage idt){

		logger.trace(CommonDefinitions.START);
		logger.debug("opeType=" + opeType + ", ukm=" + ukm + ", idt=" + idt);

		Operation ret = null;

		if(opeType == OperationType.ECMainStopper){
			ret = new ECMainStopper(idt,ukm);
		}else if(opeType == OperationType.ECMainStateConfirm){
			ret = new ECMainStateConfirm(idt,ukm);
		}else if(opeType == OperationType.ObstructionStateController){
			ret = new ObstructionStateController(idt,ukm);
		}else if(opeType == OperationType.SNMPTrapSignalRecieveNotification){
			ret = new SNMPTrapSignalRecieveNotification(idt,ukm);
		}else if(opeType == OperationType.TrafficDataAcquisition){
			ret = new TrafficDataAcquisition(idt,ukm);
		}else if(opeType == OperationType.AllL2CPCreate){
			ret = new AllL2CPCreate(idt,ukm);
		}else if(opeType == OperationType.AllL2CPRemove){
			ret = new AllL2CPRemove(idt,ukm);
		}else if(opeType == OperationType.AllL3CPCreate){
			ret = new AllL3CPCreate(idt,ukm);
		}else if(opeType == OperationType.AllL3CPRemove){
			ret = new AllL3CPRemove(idt,ukm);
		}else if(opeType == OperationType.L3CPChange){
			ret = new L3CPChange(idt,ukm);
		}else if(opeType == OperationType.DeviceInfoRegistration){
			ret = new DeviceInfoRegistration(idt,ukm);
		}else if(opeType == OperationType.DeviceInfoAcquisition){
			ret = new DeviceInfoAcquisition(idt,ukm);
		}else if(opeType == OperationType.DeviceInfoRemove){
			ret = new DeviceInfoRemove(idt,ukm);
		}else if(opeType == OperationType.LeafInfoRegistration){
			ret = new LeafInfoRegistration(idt,ukm);
		}else if(opeType == OperationType.SpineInfoRegistration){
			ret = new SpineInfoRegistration(idt,ukm);
		}else if(opeType == OperationType.NodeAddedNotification){
			ret = new NodeAddedNotification(idt,ukm);
		}else if(opeType == OperationType.LeafAddition){
			ret = new LeafAddition(idt,ukm);
		}else if(opeType == OperationType.SpineAddition){
			ret = new SpineAddition(idt,ukm);
		}else if(opeType == OperationType.LeafRemove){
			ret = new LeafRemove(idt,ukm);
		}else if(opeType == OperationType.SpineRemove){
			ret = new SpineRemove(idt,ukm);
		}else if(opeType == OperationType.LagCreate){
			ret = new LagCreate(idt,ukm);
		}else if(opeType == OperationType.LagInfoAcquisition){
			ret = new LagInfoAcquisition(idt,ukm);
		}else if(opeType == OperationType.LagRemove){
			ret = new LagRemove(idt,ukm);
		}else if(opeType == OperationType.PhysicalIfInfoAcquisition){
			ret = new PhysicalIfInfoAcquisition(idt,ukm);
		}else if(opeType == OperationType.PhysicalIfInfoChange){
			ret = new PhysicalIfInfoChange(idt,ukm);
		}else if(opeType == OperationType.AllDeviceTypeInfoAcquisition){
			ret = new AllDeviceTypeInfoAcquisition(idt,ukm);
		}else if(opeType == OperationType.AllIfInfoAcquisition){
			ret = new AllIfInfoAcquisition(idt,ukm);
		}else if(opeType == OperationType.AllPhysicalIfInfoAcquisition){
			ret = new AllPhysicalIfInfoAcquisition(idt,ukm);
		}else if(opeType == OperationType.AllLagInfoAcquisition){
			ret = new AllLagInfoAcquisition(idt,ukm);
		}else{
			ret = null;
		}

		logger.trace(CommonDefinitions.END + ", return=" + ret);

		return ret;
	}
}
