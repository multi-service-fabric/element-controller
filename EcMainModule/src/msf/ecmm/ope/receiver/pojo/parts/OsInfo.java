/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

/**
 * Server Machine Resource Information
 */
public class OsInfo {
  /**
   * CPU Usage Information Per Unit Time
   */
  private Cpu cpu;

  /**
   * Memory Usage Information at Specific Time
   */
  private Memory memory;

  /**
   * HDD Usage Information at Specific Time
   */
  private Disk disk;

  /**
   * Traffic Volume Information Per Unit Time
   */
  private Traffic traffic;

  /**
   * Getting CPU usage information per unit time.
   * @return CPU usage information per unit time
   */
  public Cpu getCpu() {
    return cpu;
  }

  /**
   * Setting CPU usage information per unit time.
   * @param cpu CPU usage information per unit time
   */
  public void setCpu(Cpu cpu) {
    this.cpu = cpu;
  }

  /**
   * Getting memory usage information at specific time.
   * @return memory usage information at specific time
   */
  public Memory getMemory() {
    return memory;
  }

  /**
   * Setting momory usage information at specific time.
   * @param memory memory usage information at specific time
   */
  public void setMemory(Memory memory) {
    this.memory = memory;
  }

  /**
   * Getting HDD usage information at specific time.
   * @return HDD usage information at specific time
   */
  public Disk getDisk() {
    return disk;
  }

  /**
   * Setting HDD usage information at specific time.
   * @param disk HDD usage information at specific time
   */
  public void setDisk(Disk disk) {
    this.disk = disk;
  }

  /**
   * Getting traffic volume information per unit time.
   * @return traffic volume information per unit time
   */
  public Traffic getTraffic() {
    return traffic;
  }

  /**
   * Setting traffic volume information per unit time.
   * @param traffic traffic volume information per unit time
   */
  public void setTraffic(Traffic traffic) {
    this.traffic = traffic;
  }

  @Override
  public String toString() {
    return "OsInfo [cpu=" + cpu + ", memory=" + memory + ", disk=" + disk + ", traffic=" + traffic + "]";
  }
}
