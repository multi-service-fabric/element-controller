/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import java.util.ArrayList;

/**
 * Traffic Information for each Device.
 */
public class SwitchTraffic {

  /** Device ID. */
  private String nodeId;

  /** Traffic Information. */
  private ArrayList<TrafficValue> trafficValues = new ArrayList<>();

  /**
   * Getting device ID.
   * @return device ID
   */
  public String getNodeId() {
    return nodeId;
  }

  /**
   * Setting device ID.
   * @param nodeId device ID
   */
  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  /**
   * Getting traffic information.
   * @return traffic information
   */
  public ArrayList<TrafficValue> getTrafficValue() {
    return trafficValues;
  }

  /**
   * Setting traffic information.
   * @param trafficValue traffic information
   */
  public void setTrafficValue(ArrayList<TrafficValue> trafficValue) {
    this.trafficValues = trafficValue;
  }

  /**
   * Stringizing Instance.
   * @return instance string
   */
  @Override
  public String toString() {
    return "SwitchTraffic [nodeId=" + nodeId + ", trafficValue=" + trafficValues + "]";
  }

}
