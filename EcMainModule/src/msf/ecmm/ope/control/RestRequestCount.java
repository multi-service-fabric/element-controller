/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.control;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.config.EcConfiguration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Number of REST Transmission Management Class Definition. Managing the number of transmissions on REST communication.
 */
public class RestRequestCount {

  /**
   * Logger.
   */
  private static final Logger logger = LogManager.getLogger(CommonDefinitions.EC_LOGGER);

  /** REST Sending Time List. */
  private static ArrayList<Calendar> restTransmissionList = new ArrayList<Calendar>();

  /** REST Receiving Time List. */
  private static ArrayList<Calendar> restReceptionList = new ArrayList<Calendar>();

  /** Request Counter Transmission Type: Send. */
  public static final int REST_TRANSMISSION = 0;
  /** Request Counter Transmission Type: Receive. */
  public static final int REST_RECEPTION = 1;

  private static Object transmissionLock = new Object();
  private static Object receptionLock = new Object();


  /**
   * Number of REST Transmissions Counting Process<br>
   * Doing calculation of the number of REST transmissions and receives, and removing the count out of the scope of aggrigation condition.
   *
   * @param requestType
   *          whether it is sending or receiving of REST communication * 0:send, 1:receive
   */
  public static void requestcount(int requestType) {

    logger.trace(CommonDefinitions.START + ",requesttype=" + requestType);

    Object lockKey = null;
    if (requestType == (REST_TRANSMISSION)) {
      lockKey = RestRequestCount.transmissionLock;
    } else {
      lockKey = RestRequestCount.receptionLock;
    }
    synchronized (lockKey) {

      Date restdate = new Date();
      Calendar nowcalendar = Calendar.getInstance();
      nowcalendar.setTime(restdate);
      String requestaverage = EcConfiguration.getInstance().get(String.class, EcConfiguration.REST_REQUEST_AVERAGE);
      Calendar averagecalendar = Calendar.getInstance();
      averagecalendar.setTime(restdate);
      averagecalendar.add(Calendar.SECOND, -Integer.parseInt(requestaverage));

      if (requestType == (REST_TRANSMISSION)) {
        restTransmissionList.add(nowcalendar);
        restTransmissionList = (ArrayList<Calendar>) restTransmissionList.stream()
            .filter(countdate -> (countdate.compareTo(averagecalendar)) != -1).collect(Collectors.toList());

      } else if (requestType == (REST_RECEPTION)) {
        restReceptionList.add(nowcalendar);
        restReceptionList = (ArrayList<Calendar>) restReceptionList.stream()
            .filter(countdate -> (countdate.compareTo(averagecalendar)) != -1).collect(Collectors.toList());
      } else {
      }
    }
  }

  /**
   * Getting the count in unit time<br>
   * Returning the number of REST requests to be applicable.
   *
   * @param requestType
   *          request type (send/receive) * 0:send, 1:receive
   * @param requestAverage
   *          unit time (sec)
   * @return the number of requests
   */
  public static int getRestRequestCount(int requestType, int requestAverage) {

    int requestCount = 0;
	try {
      logger.trace(CommonDefinitions.START);
      Object lockKey = null;
      if (requestType == (REST_TRANSMISSION)) {
        lockKey = RestRequestCount.transmissionLock;
      } else {
        lockKey = RestRequestCount.receptionLock;
      }
      synchronized (lockKey) {
        logger.debug("requestType=" + requestType);
        Date restdate = new Date();
        Calendar averagecalendar = Calendar.getInstance();
        averagecalendar.setTime(restdate);
        averagecalendar.add(Calendar.SECOND, -requestAverage);

        if (requestType == (REST_TRANSMISSION)) {
          List<Calendar> requestcountlist = restTransmissionList.stream()
              .filter(countdate -> (countdate.compareTo(averagecalendar)) != -1).collect(Collectors.toList());
          requestCount = requestcountlist.size();

        } else if (requestType == (REST_RECEPTION)) {
          List<Calendar> requestcountlist = restReceptionList.stream()
              .filter(countdate -> (countdate.compareTo(averagecalendar)) != -1).collect(Collectors.toList());
          requestCount = requestcountlist.size();
        } else {
        	requestCount = -1;
        }
        return requestCount;
      }
    } catch (Exception e) {
      return -1;
  }

 }

}
