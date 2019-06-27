/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

/**
 * CPU Usage Per Unit Time Information
 */
public class Cpu {

  /**
   * CPU Usage of Entire OS
   */
  private Float useRate;

  /**
   * Getting CPU usage of entire OS.
   * @return CPU usage of entire OS
   */
  public Float getUseRate() {
    return useRate;
  }

  /**
   * Setting CPU usage of entire OS.
   * @param useRate CPU usage of entire OS
   */
  public void setUseRate(Float useRate) {
    this.useRate = useRate;
  }

  @Override
  public String toString() {
    return "Cpu [useRate=" + useRate + "]";
  }
}
