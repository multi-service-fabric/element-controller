package msf.ecmm.traffic;

import java.util.ArrayList;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.devctrl.SnmpController;
import msf.ecmm.devctrl.pojo.SnmpIfTraffic;
import msf.ecmm.traffic.pojo.NodeKeySet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DataGatheringExecutor extends Thread {

	private final Logger logger = LogManager.getLogger(CommonDefinitions.EC_LOGGER);

	private int maxGetBulk;

	public DataGatheringExecutor(int maxGetBulk,NodeKeySet deviceDetail){
		this.maxGetBulk = maxGetBulk;
		this.deviceDetail = deviceDetail;
		this.trafficData = new ArrayList<SnmpIfTraffic>();
	}

	public void run(){

		logger.trace(CommonDefinitions.START);

		SnmpController snmp = new SnmpController();
		try {
			trafficData = snmp.getTraffic(deviceDetail.getEquipmentsType(), deviceDetail.getEquipmentsData());
		} catch (Exception e) {
			   logger.warn(LogFormatter.out.format(LogFormatter.MSG_407051,deviceDetail.getEquipmentsData().getNode_id(),deviceDetail.getEquipmentsType().getEquipment_type_id()),e);
		}

		TrafficDataGatheringManager.getInstance().getWatchDogThreadHolder().setTrafficData(deviceDetail,trafficData);

		logger.trace(CommonDefinitions.END);
	}

	@Override
	public String toString() {
		return "DataGatheringExecutor [trafficData=" + trafficData
				+ ", maxGetBulk=" + maxGetBulk + ", deviceDetail="
				+ deviceDetail + "]";
	}

}
