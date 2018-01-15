/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.traffic;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.CommonUtil;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.config.EcConfiguration;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.BreakoutIfs;
import msf.ecmm.db.pojo.Equipments;
import msf.ecmm.db.pojo.LagIfs;
import msf.ecmm.db.pojo.LagMembers;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.db.pojo.PhysicalIfs;
import msf.ecmm.devctrl.pojo.SnmpIfTraffic;
import msf.ecmm.traffic.pojo.NodeKeySet;
import msf.ecmm.traffic.pojo.TrafficData;

/**
 * Traffic Information Collection Management Class Definition. Manages traffic information collection. Also, retains traffic information.
 */
public class TrafficDataGatheringManager {

  /**
   * Logger
   */
  private static final Logger logger = LogManager.getLogger(CommonDefinitions.EC_LOGGER);

  /** Running Thread List */
  private ConcurrentHashMap<NodeKeySet, DataGatheringExecutor> executeThreadHolder;

  /** Traffic Information */
  private HashMap<NodeKeySet, ArrayList<TrafficData>> trafficData;

  /** Traffic Information (Before Conversion) */
  private ConcurrentHashMap<NodeKeySet, ArrayList<SnmpIfTraffic>> TrafficRawData;

  /** Traffic Information Collection Monitoring Thread Instance */
  private GatheringExecuterWatchDog watchDogThreadHolder;

  /** Traffic Information Last Update Date and Time */
  private Timestamp lastGathering;

  /** Traffic Information Collection Periodic Execution Termination Flag */
  private boolean stopFlug;

  /** Traffic Information Collection Management Instance */
  private static TrafficDataGatheringManager instance = null;

  /** Job Scheduler */
  private Scheduler scheduler = null;

  /**
   * Constructor (Initialization Process)
   */
  private TrafficDataGatheringManager() {
    this.executeThreadHolder = new ConcurrentHashMap<NodeKeySet, DataGatheringExecutor>();
    this.trafficData = new HashMap<NodeKeySet, ArrayList<TrafficData>>();
    this.TrafficRawData = new ConcurrentHashMap<NodeKeySet, ArrayList<SnmpIfTraffic>>();
    this.lastGathering = new Timestamp((new Date()).getTime());
    this.stopFlug = false;
    this.watchDogThreadHolder = null;
  }

  /**
   * Traffic Information Collection Start<br>
   * Starting traffic information periodic collection.
   */
  protected void startGathering() {

    logger.info(LogFormatter.out.format(LogFormatter.MSG_307058));
    if (!stopFlug) {

      watchDogThreadHolder = new GatheringExecuterWatchDog();
      watchDogThreadHolder.start();
      List<Equipments> equipmentList = new ArrayList<>();
      Map<Equipments, List<Nodes>> nodesMap = new HashMap<>();

      try (DBAccessManager session = new DBAccessManager()) {

        equipmentList = session.getEquipmentsList();
        for (Equipments eq : equipmentList) {
          nodesMap.put(eq, session.searchNodesByEquipmentId(eq.getEquipment_type_id()));
        }
        equipmentList.toString();
        nodesMap.toString();
        for (Equipments eq : nodesMap.keySet()) {
          for (Nodes node : nodesMap.get(eq)) {
            for (PhysicalIfs pf : node.getPhysicalIfsList()) {
              for (BreakoutIfs bf : pf.getBreakoutIfsList()) {
                bf.toString();
              }
            }
            for (LagIfs lf : node.getLagIfsList()) {
              for (LagMembers lm : lf.getLagMembersList()) {
                 lm.toString();
              }
            }
          }
        }
      } catch (Throwable e) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_407044), e);
        return;
      }
      try {
        for (Equipments eq : nodesMap.keySet()) {
          if (nodesMap.get(eq) != null && !nodesMap.get(eq).isEmpty()) {
            if (eq.getRouter_type() == CommonDefinitions.ROUTER_TYPE_COREROUTER) {
              NodeKeySet nks = new NodeKeySet();
              nks.setEquipmentsData(nodesMap.get(eq).get(0));
              nks.setEquipmentsType(eq);
              executeThreadHolder.put(nks, new DataGatheringExecutor(eq.getMax_repetitions(), nks, nodesMap));
              executeThreadHolder.get(nks).start();
              break;
            }

            for (Nodes node : nodesMap.get(eq)) {
              NodeKeySet nks = new NodeKeySet();
              nks.setEquipmentsData(node);
              nks.setEquipmentsType(eq);
              executeThreadHolder.put(nks, new DataGatheringExecutor(eq.getMax_repetitions(), nks, null));
            }
            for (Nodes node : nodesMap.get(eq)) {
              NodeKeySet nks = new NodeKeySet();
              nks.setEquipmentsData(node);
              nks.setEquipmentsType(eq);

              executeThreadHolder.get(nks).start();
            }
          } else {
          }
        }

      } catch (Exception e) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_407044), e);
        executeThreadHolder.clear();
      }

    } else {
      logger.debug("Gathering traffic data stop.");
    }
  }

  /**
   * Device Information Collection Periodic Execution Start<br>
   * Controlling traffic information periodic collection.
   *
   * @throws SchedulerException
   *           scheduler start-up failed
   */
  public void controlGatheringCycle() throws SchedulerException {

    logger.trace(CommonDefinitions.START);

    watchDogThreadHolder = new GatheringExecuterWatchDog();
    watchDogThreadHolder.start();

    int getheringCycle = Integer
        .parseInt(EcConfiguration.getInstance().get(String.class, EcConfiguration.TRAFFIC_MIB_INTERVAL));

    if (getheringCycle > 0) {
      StdSchedulerFactory factory = new StdSchedulerFactory();
      scheduler = factory.getScheduler();

      JobDetail job1 = JobBuilder.newJob(TrafficGatherJob.class).withIdentity("trafficGatherJob", "trafficGather")
          .build();

      Trigger cron1 = TriggerBuilder.newTrigger().withIdentity("trafficGatherTrigger", "trafficGather").startNow()
          .withSchedule(
              SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(getheringCycle * 60).repeatForever())
          .build();

      scheduler.scheduleJob(job1, cron1);
      scheduler.start();
    } else {
    }

    logger.trace(CommonDefinitions.END);
  }

  /**
   * Getting traffic information collection monitoring thread instance.
   *
   * @return traffic information collection monitoring thread instance
   */
  protected GatheringExecuterWatchDog getWatchDogThreadHolder() {
    return watchDogThreadHolder;
  }

  /**
   * Traffic Informatoin Acquisition<br>
   * Getting traffic information.
   *
   * @return traffic information
   */
  public HashMap<NodeKeySet, ArrayList<TrafficData>> getTrafficData() {
    return this.trafficData;
  }

  /**
   * Traffic Information Last Update Date and Time Acquisition<br>
   * Gettin traffic information last update date and time.
   *
   * @return traffic information last update date and time
   */
  public Timestamp getLastGathering() {
    return this.lastGathering;
  }

  /**
   * Getting running thread list.
   *
   * @return running thread list
   */
  protected ConcurrentHashMap<NodeKeySet, DataGatheringExecutor> getExecuteThreadHolder() {
    return executeThreadHolder;
  }

  /**
   * Getting traffic information (before conversion).
   *
   * @return traffic information (before conversion)
   */
  protected ConcurrentHashMap<NodeKeySet, ArrayList<SnmpIfTraffic>> getTrafficRawData() {
    return TrafficRawData;
  }

  /**
   * Getting traffic information collection management instance.
   *
   * @return traffic information collection management instance
   */
  public static TrafficDataGatheringManager getInstance() {
    return instance;
  }

  /**
   * Setting traffic information.
   *
   * @param trafficData
   *          traffic information
   */
  protected void setTrafficData(HashMap<NodeKeySet, ArrayList<TrafficData>> trafficData) {
    this.trafficData = trafficData;
  }

  /**
   * Setting traffic information (before conversion).
   *
   * @param trafficRawData
   *          traffic information (before conversion)
   */
  protected void setTrafficRawData(ConcurrentHashMap<NodeKeySet, ArrayList<SnmpIfTraffic>> trafficRawData) {
    TrafficRawData = trafficRawData;
  }

  /**
   * Setting traffic information last update date and time.
   *
   * @param lastGathering
   *          traffic information last update date and time acquisition
   */
  protected void setLastGathering(Timestamp lastGathering) {
    this.lastGathering = lastGathering;
  }

  /**
   * Device Information Collection Periodic Execution Termination<br>
   * Terminating device information collection periodic execution.
   */
  public void stopGetheringCycle() {

    this.stopFlug = true;

    long timeoutLimit = Long
        .parseLong(EcConfiguration.getInstance().get(String.class, EcConfiguration.GATHER_MIB_STOP_TIMEOUT));
    long checkInterval = Long
        .parseLong(EcConfiguration.getInstance().get(String.class, EcConfiguration.GATHER_MIB_STOP_INTERVAL));

    long timeCheker = 0;

    while (true) {
      if (executeThreadHolder.isEmpty() || (timeCheker > timeoutLimit * 1000)) {
        break;
      } else {
        CommonUtil.sleep(checkInterval);
        timeCheker += checkInterval;
      }
    }

    if (scheduler != null) {
      try {
        scheduler.clear();
      } catch (SchedulerException e) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_407065), e);
      }
    } else {
    }
    watchDogThreadHolder = null;
  }

  /**
   * Traffic Information Collection Function Start-up<br>
   * Starting up traffic information collection function.
   *
   * @return start-up success/fail
   */
  public static synchronized boolean boot() {
    logger.trace(CommonDefinitions.START);

    if (instance == null) {
      instance = new TrafficDataGatheringManager();

      try {
        instance.controlGatheringCycle();
        CommonUtil.sleep(1000);
      } catch (SchedulerException e) {
        logger.error(LogFormatter.out.format(LogFormatter.MSG_507063), e);
      }

      logger.trace(CommonDefinitions.END);

      return true;
    } else { 
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_407057));
      return false;
    }
  }

  @Override
  public String toString() {
    return "TrafficDataGatheringManager [trafficData=" + trafficData + ", TrafficRawData=" + TrafficRawData
        + ", lastGathering=" + lastGathering + ", stopFlug=" + stopFlug + "]";
  }

}
