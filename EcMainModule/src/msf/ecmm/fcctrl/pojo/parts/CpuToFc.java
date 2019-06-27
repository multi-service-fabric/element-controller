/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.fcctrl.pojo.parts;

/**
 * CPU class.
 */
public class CpuToFc {

  /** CPU usage. */
  private Float useRate;

  /**
   * Getting CPU usage.
   *
   * @return CPU usage
   */
  public Float getUseRate() {
    return useRate;
  }

  /**
   * Setting CPU usage.
   *
   * @param useRate
   *          CPU usage
   */
  public void setUseRate(Float useRate) {
    this.useRate = useRate;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "CpuToFc [useRate=" + useRate + "]";
  }
}
