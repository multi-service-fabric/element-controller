/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */
package msf.ecmm.ope.receiver.pojo.parts;

import java.util.ArrayList;

/**
 * HDD Usage at Specific Time
 */
public class Disk {

  /**
   * Information for each Device
   */
  private ArrayList<DeviceInfo> devices = new ArrayList<DeviceInfo>();

  /**
   * Getting information for each device.
   * @return information for each device
   */
  public ArrayList<DeviceInfo> getDevices() {
    return devices;
  }

  /**
   * Setting information for each device
   * @param devices information for each device
   */
  public void setDevices(ArrayList<DeviceInfo> devices) {
    this.devices = devices;
  }

}
