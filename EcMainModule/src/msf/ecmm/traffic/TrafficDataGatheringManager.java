package msf.ecmm.traffic;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.CommonUtil;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.config.EcConfiguration;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.Equipments;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.devctrl.pojo.SnmpIfTraffic;
import msf.ecmm.traffic.pojo.NodeKeySet;
import msf.ecmm.traffic.pojo.TrafficData;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class TrafficDataGatheringManager {

	private static final Logger logger = LogManager.getLogger(CommonDefinitions.EC_LOGGER);


	private HashMap<NodeKeySet,ArrayList<TrafficData>> trafficData;

	private GatheringExecuterWatchDog watchDogThreadHolder;

	private boolean stopFlug;

    private Scheduler scheduler = null;

	private TrafficDataGatheringManager() {
		this.executeThreadHolder = new HashMap<NodeKeySet,DataGatheringExecutor>();
		this.trafficData = new HashMap<NodeKeySet,ArrayList<TrafficData>>();
		this.TrafficRawData = new HashMap<NodeKeySet,ArrayList<SnmpIfTraffic>>();
		this.lastGathering = new Timestamp((new Date()).getTime());
		this.stopFlug = false;
		this.watchDogThreadHolder = null;
	}

	protected void startGathering() {

		logger.info(LogFormatter.out.format(LogFormatter.MSG_307058));

		if(!stopFlug){

			watchDogThreadHolder = new GatheringExecuterWatchDog();
			watchDogThreadHolder.start();

			List<Equipments> equipmentList = new ArrayList<>();
			Map<Equipments,List<Nodes>> nodesMap = new HashMap<>();

			try(DBAccessManager session = new DBAccessManager()){

				equipmentList = session.getEquipmentsList();
				for(Equipments eq:equipmentList){
					nodesMap.put(eq,session.searchNodesByEquipmentId(eq.getEquipment_type_id()));
				}
				equipmentList.toString();
				nodesMap.toString();
			} catch (Exception e) {
	 			   logger.warn(LogFormatter.out.format(LogFormatter.MSG_407044),e);
	 			   return;
			}

			try{
				for(Equipments eq:nodesMap.keySet()){
					if(nodesMap.get(eq) != null){
						for(Nodes node:nodesMap.get(eq)){
							NodeKeySet nks = new NodeKeySet();
							nks.setEquipmentsData(node);
							nks.setEquipmentsType(eq);

							executeThreadHolder.put(nks,new DataGatheringExecutor(eq.getMax_repetitions(),nks));
							executeThreadHolder.get(nks).start();
						}
					}else{
					}
				}

			} catch (Exception e) {
 			   logger.warn(LogFormatter.out.format(LogFormatter.MSG_407044),e);
 			   executeThreadHolder.clear();
			}

		}else{
			logger.debug("Gathering traffic data stop.");
		}
	}

	public void controlGatheringCycle() throws SchedulerException {

		logger.trace(CommonDefinitions.START);

		watchDogThreadHolder = new GatheringExecuterWatchDog();
		watchDogThreadHolder.start();

		int getheringCycle = Integer.parseInt(EcConfiguration.getInstance().get(String.class, EcConfiguration.TRAFFIC_MIB_INTERVAL));

		if(getheringCycle > 0){
	        StdSchedulerFactory factory = new StdSchedulerFactory();
			scheduler = factory.getScheduler();

			JobDetail job1 = JobBuilder.newJob(TrafficGatherJob.class)
			        .withIdentity("trafficGatherJob", "trafficGather")
			        .build();

			Trigger cron1 = TriggerBuilder
			        .newTrigger()
			        .withIdentity("trafficGatherTrigger", "trafficGather")
			        .startNow()
			        .withSchedule(
			            SimpleScheduleBuilder.simpleSchedule()
			                                 .withIntervalInSeconds(getheringCycle*60)
			                                 .repeatForever())
			        .build();

			scheduler.scheduleJob(job1, cron1);
			scheduler.start();
		}else{
		}

		logger.trace(CommonDefinitions.END);
	}



	protected GatheringExecuterWatchDog getWatchDogThreadHolder() {
		return watchDogThreadHolder;
	}

	public HashMap<NodeKeySet,ArrayList<TrafficData>> getTrafficData() {
		return this.trafficData;
	}

	public Timestamp getLastGathering() {
		return this.lastGathering;
	}

	protected HashMap<NodeKeySet,DataGatheringExecutor> getExecuteThreadHolder() {
		return executeThreadHolder;
	}

	protected HashMap<NodeKeySet, ArrayList<SnmpIfTraffic>> getTrafficRawData() {
		return TrafficRawData;
	}

	public static TrafficDataGatheringManager getInstance() {
		return instance;
	}

	protected void setTrafficData(
			HashMap<NodeKeySet, ArrayList<TrafficData>> trafficData) {
		this.trafficData = trafficData;
	}

	protected void setTrafficRawData(
			HashMap<NodeKeySet, ArrayList<SnmpIfTraffic>> trafficRawData) {
		TrafficRawData = trafficRawData;
	}

	protected void setLastGathering(Timestamp lastGathering) {
		this.lastGathering = lastGathering;
	}

	public void stopGetheringCycle() {

		this.stopFlug = true;

		long timeoutLimit = Long.parseLong(EcConfiguration.getInstance().get(String.class, EcConfiguration.GATHER_MIB_STOP_TIMEOUT));
		long checkInterval = Long.parseLong(EcConfiguration.getInstance().get(String.class, EcConfiguration.GATHER_MIB_STOP_INTERVAL));

		long timeCheker = 0;

		while(true){
			if(executeThreadHolder.isEmpty() || (timeCheker > timeoutLimit * 1000)){
				break;
			}else{
				CommonUtil.sleep(checkInterval);
				timeCheker += checkInterval;
			}
		}

		if(scheduler != null){
			try {
				scheduler.clear();
			} catch (SchedulerException e) {
				logger.warn(LogFormatter.out.format(LogFormatter.MSG_407065),e);
			}
		}else{
		}
		watchDogThreadHolder = null;
	}

	public static synchronized boolean boot() {
		logger.trace(CommonDefinitions.START);

		if(instance == null){
			instance = new TrafficDataGatheringManager();

			try{
				instance.controlGatheringCycle();
				CommonUtil.sleep(1000);
			}catch(SchedulerException e){
				logger.error(LogFormatter.out.format(LogFormatter.MSG_507063),e);
			}

			logger.trace(CommonDefinitions.END);

			return true;
			logger.warn(LogFormatter.out.format(LogFormatter.MSG_407057));
			return false;
		}
	}

	@Override
	public String toString() {
		return "TrafficDataGatheringManager [trafficData=" + trafficData
				+ ", TrafficRawData=" + TrafficRawData
				+ ", lastGathering=" + lastGathering + ", stopFlug=" + stopFlug
				+ "]";
	}

}
