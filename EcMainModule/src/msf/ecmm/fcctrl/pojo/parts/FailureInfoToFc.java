/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.fcctrl.pojo.parts;

/**
 * Failure informatin class.
 */
public class FailureInfoToFc {

  /** CPU. */
  private CpuToFc cpu;

  /** memory. */
  private MemoryToFc memory;

  /** Disk usage. */
  private DiskToFc disk;

  /**
   * Getting CPU.
   *
   * @return CPU
   */
  public CpuToFc getCpu() {
    return cpu;
  }

  /**
   * Setting CPU.
   *
   * @param cpu
   *          CPU
   */
  public void setCpu(CpuToFc cpu) {
    this.cpu = cpu;
  }

  /**
   * Getting memory.
   *
   * @return memory
   */
  public MemoryToFc getMemory() {
    return memory;
  }

  /**
   * Setting memory.
   *
   * @param memory
   *          memory
   */
  public void setMemory(MemoryToFc memory) {
    this.memory = memory;
  }

  /**
   * Getting disk usage.
   *
   * @return disk usage
   */
  public DiskToFc getDisk() {
    return disk;
  }

  /**
   * Setting disk usage.
   *
   * @param disk
   *          disk usage.
   */
  public void setDisk(DiskToFc disk) {
    this.disk = disk;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "FailureInfoToFc [cpu=" + cpu + ", memory=" + memory + ", disk=" + disk + "]";
  }
}
