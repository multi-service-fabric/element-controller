/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.fcctrl.pojo.parts;

/**
 * Memory usage Class.
 */
public class MemoryToFc {

  /** Memory usage. */
  private Integer used;

  /** Free Memory. */
  private Integer free;

  /**
   * Getting Memory usage.
   *
   * @return Memory usage
   */
  public Integer getUsed() {
    return used;
  }

  /**
   * Setting Memory usage.
   *
   * @param used
   *          Memory usage
   */
  public void setUsed(Integer used) {
    this.used = used;
  }

  /**
   * Getting Free Memory.
   *
   * @return Free Memory
   */
  public Integer getFree() {
    return free;
  }

  /**
   * Setting Free Memory.
   *
   * @param free
   *          Free Memory
   */
  public void setFree(Integer free) {
    this.free = free;
  }

  /**
   * Stringizing Instance..
   *
   * @return istance string
   */
  @Override
  public String toString() {
    return "MemoryToFc [used=" + used + ", free=" + free + "]";
  }
}
