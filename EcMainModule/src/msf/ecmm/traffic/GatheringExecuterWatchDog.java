package msf.ecmm.traffic;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.config.EcConfiguration;
import msf.ecmm.devctrl.pojo.SnmpIfTraffic;
import msf.ecmm.traffic.pojo.NodeKeySet;
import msf.ecmm.traffic.pojo.TrafficData;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GatheringExecuterWatchDog extends Thread {

	private static final Logger logger = LogManager.getLogger(CommonDefinitions.EC_LOGGER);

	private Timestamp completeTime;

	public GatheringExecuterWatchDog(){
		this.trafficData = new HashMap<NodeKeySet,ArrayList<SnmpIfTraffic>>();
	}

	public void run(){
	}

	protected void setTrafficData(NodeKeySet node, ArrayList<SnmpIfTraffic> trafficData) {

		logger.trace(CommonDefinitions.START);
		logger.debug("node=" + node + ", trafficData=" + trafficData);

		this.trafficData.put(node, trafficData);

		TrafficDataGatheringManager.getInstance().getExecuteThreadHolder().remove(node);

		if(TrafficDataGatheringManager.getInstance().getExecuteThreadHolder().isEmpty()){
			calcTrafficData();
			TrafficDataGatheringManager.getInstance().setTrafficRawData(this.trafficData);
			TrafficDataGatheringManager.getInstance().setLastGathering(this.completeTime);
		}else{
		}

		logger.trace(CommonDefinitions.END);

		return;
	}

	private void calcTrafficData() {

		logger.trace(CommonDefinitions.START);

		HashMap<NodeKeySet,ArrayList<TrafficData>> calcdTd = new HashMap<NodeKeySet,ArrayList<TrafficData>>();

		HashMap<NodeKeySet,ArrayList<SnmpIfTraffic>> lastTrafficData =  TrafficDataGatheringManager.getInstance().getTrafficRawData();

		long getheringCycle = Long.parseLong(EcConfiguration.getInstance().get(String.class, EcConfiguration.TRAFFIC_MIB_INTERVAL)) * 60;

		if(getheringCycle == 0){
			getheringCycle = 1;
		}else{
		}

		completeTime = new Timestamp((new Date()).getTime());

		for(NodeKeySet tdl:trafficData.keySet()){

			if(lastTrafficData.get(tdl) != null){
				calcdTd.put(tdl,new ArrayList<TrafficData>());

				if(trafficData.get(tdl) != null){
					for(SnmpIfTraffic td:trafficData.get(tdl)){

						TrafficData calc = new TrafficData();
						calc.setIfname(td.getIfName());
						calc.setIfHclnOctets(0.0);
						calc.setIfHcOutOctets(0.0);

						for(SnmpIfTraffic oldtd:lastTrafficData.get(tdl)){
							if(td.getIfName().equals(oldtd.getIfName())){
								double inOct = ((double)(td.getInOctets() - oldtd.getInOctets()) * ((double)8) / (double)1000000000);
								double outOct = ((double)(td.getOutOctets() - oldtd.getOutOctets()) * ((double)8) / (double)1000000000);

								if(inOct < 0.0){
									inOct += Math.pow(2, 64);
								}else{
								}

								if(outOct < 0.0){
									outOct += Math.pow(2, 64);
								}else{
								}

								calc.setIfHclnOctets(inOct / ((double) getheringCycle));
								calc.setIfHcOutOctets(outOct / ((double) getheringCycle));

								calcdTd.get(tdl).add(calc);
								break;
							}else{
							}
						}
					}

					if(!calcdTd.get(tdl).isEmpty()){
						for(SnmpIfTraffic td:trafficData.get(tdl)){
							boolean existFlug = false;
							for(TrafficData tdc:calcdTd.get(tdl)){
								if(td.getIfName().equals(tdc.getIfname())){
									existFlug = true;
									break;
								}else{
								}
							}

							if(!existFlug){
								TrafficData calc = new TrafficData();
								calc.setIfname(td.getIfName());
								calc.setIfHclnOctets(0.0);
								calc.setIfHcOutOctets(0.0);

								calcdTd.get(tdl).add(calc);
							}else{
							}
						}

						for(SnmpIfTraffic td:lastTrafficData.get(tdl)){
							boolean existFlug = false;
							for(TrafficData tdc:calcdTd.get(tdl)){
								if(td.getIfName().equals(tdc.getIfname())){
									existFlug = true;
									break;
								}else{
								}
							}

							if(!existFlug){
								TrafficData calc = new TrafficData();
								calc.setIfname(td.getIfName());
								calc.setIfHclnOctets(0.0);
								calc.setIfHcOutOctets(0.0);

								calcdTd.get(tdl).add(calc);
							}else{
							}
						}
					}else{
					}
				}else{
				}
			}else{
				logger.debug("Skip adding data cause of data lacking in last traffic data.");
			}
		}

		TrafficDataGatheringManager.getInstance().setTrafficData(calcdTd);

		logger.trace(CommonDefinitions.END);

		return;
	}

	@Override
	public String toString() {
		return "GatheringExecuterWatchDog [trafficData=" + trafficData
				+ ", completeTime=" + completeTime + "]";
	}
}
