/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.fcctrl.pojo.parts;

import java.util.ArrayList;
import java.util.List;

/**
 * Disk usage Class.
 */
public class DiskToFc {

  /** Infomation on each disk. */
  private List<DevicesToFc> devices = new ArrayList<DevicesToFc>();

  /**
   * Getting infomation on each disk.
   *
   * @return infomation on each disk
   */
  public List<DevicesToFc> getDevices() {
    return devices;
  }

  /**
   * Setting infomation on each disk.
   *
   * @param devices
   *          infomation on each disk
   */
  public void setDevices(List<DevicesToFc> devices) {
    this.devices = devices;
  }

  /**
   * Stringizing Instance..
   *
   * @return istance string
   */
  @Override
  public String toString() {
    return "DiskToFc [devices=" + devices + "]";
  }

}
