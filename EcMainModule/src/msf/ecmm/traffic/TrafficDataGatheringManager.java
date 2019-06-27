/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.traffic;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
import msf.ecmm.common.log.MsfLogger;
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
 * Traffic Information Collection Management Class Definition .Managing the Traffic Information Collection and retaining Traffic Information.
 */
public class TrafficDataGatheringManager {

  /**
   * Logger
   */
  private static final MsfLogger logger = new MsfLogger();

  /** Running Thread  List */
  private ConcurrentHashMap<NodeKeySet, DataGatheringExecutor> executeThreadHolder;

  /** Traffic Information */
  private HashMap<NodeKeySet, HashMap<String, TrafficData>> trafficData;

  /** Traffic Information(Before conversion) */
  private ConcurrentHashMap<NodeKeySet, ArrayList<SnmpIfTraffic>> TrafficRawData;

  /** Traffic Information Collection Monitoring Thread Instance */
  private GatheringExecuterWatchDog watchDogThreadHolder;

  /** Traffic Information Last Update Date and Time */
  private Timestamp lastGathering;

  /** Traffic Information Collection Periodic Execution Stop Flag */
  private boolean stopFlug;

  /** Traffic Information Collection Management Instance*/
  private static TrafficDataGatheringManager instance = null;

  /** Job Scheduler */
  private Scheduler scheduler = null;

  /**
   * Constructor(initialization)
   */
  private TrafficDataGatheringManager() {
    this.executeThreadHolder = new ConcurrentHashMap<NodeKeySet, DataGatheringExecutor>();
    this.trafficData = new HashMap<NodeKeySet, HashMap<String, TrafficData>>();
    this.TrafficRawData = new ConcurrentHashMap<NodeKeySet, ArrayList<SnmpIfTraffic>>();
    this.lastGathering = new Timestamp((new Date()).getTime());
    this.stopFlug = false;
    this.watchDogThreadHolder = null;
  }

  /**
   * Traffic Information Collection Start<br>
   * Traffic information collection  periodic execution is starting.
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
   * Node Information Collection Periodic Execution Start<br>
   * Trafic information collection periodic execution.
   *
   * @throws SchedulerException
   *           Scheduler start failed
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
   *  Traffic Information Collection Monitoring Thread Instance acquisition
   *
   * @return  traffic information collection monitoring thread instance
   */
  protected GatheringExecuterWatchDog getWatchDogThreadHolder() {
    return watchDogThreadHolder;
  }

  /**
   *  Traffic Information Acquisition<br>
   *  Getting traffic information.
   *
   * @return traffic information
   */
  public HashMap<NodeKeySet, HashMap<String, TrafficData>> getTrafficData() {
    return this.trafficData;
  }

  /**
   *  Traffic Information Last Update Date and Time acquisition<br>
   *  Getting traffic information last update date and time
   *
   * @return traffic information last update date and time
   */
  public Timestamp getLastGathering() {
    return this.lastGathering;
  }

  /**
   * Getting Running Thread  List.
   *
   * @return running thread  list
   */
  protected ConcurrentHashMap<NodeKeySet, DataGatheringExecutor> getExecuteThreadHolder() {
    return executeThreadHolder;
  }

  /**
   * Getting Traffic Information(Before conversion).
   *
   * @return Traffic Information(Before conversion)
   */
  protected ConcurrentHashMap<NodeKeySet, ArrayList<SnmpIfTraffic>> getTrafficRawData() {
    return TrafficRawData;
  }

  /**
   * Getting Traffic Information(Before conversion).
   *
   * @return Traffic Information(Before conversion)
   */
  public static TrafficDataGatheringManager getInstance() {
    return instance;
  }

  /**
   * Setting Traffic Information
   *
   * @param trafficData
   *          traffic information
   */
  protected void setTrafficData(HashMap<NodeKeySet, HashMap<String, TrafficData>> trafficData) {
    this.trafficData = trafficData;
  }

  /**
   * Setting Traffic Information(Before conversion)
   *
   * @param trafficRawData
   *          traffic information(before conversion)
   */
  protected void setTrafficRawData(ConcurrentHashMap<NodeKeySet, ArrayList<SnmpIfTraffic>> trafficRawData) {
    TrafficRawData = trafficRawData;
  }

  /**
   *  Setting Traffic Information Last Update Date and Time 
   *
   * @param lastGathering
   *          traffic information last update date and time 
   */
  protected void setLastGathering(Timestamp lastGathering) {
    this.lastGathering = lastGathering;
  }

  /**
   * Node Information Collection Periodic Execution Stop<br>
   * Stopping Node Information Collection Periodic Execution.
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
   * Traffic Information Collection Function Start <br>
   * Starting traffic information collection function 
   *
   * @return Success/fail of start 
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
