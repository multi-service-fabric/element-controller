/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * Failure informatin class.
 */
public class FailureInfo {

  /** CPU. */
  private Cpu cpu;

  /** memory. */
  private Memory memory;

  /** Disk usage. */
  private Disk disk;

  /**
   * Getting CPU.
   *
   * @return CPU
   */
  public Cpu getCpu() {
    return cpu;
  }

  /**
   * Setting CPU.
   *
   * @param cpu
   *          CPU
   */
  public void setCpu(Cpu cpu) {
    this.cpu = cpu;
  }

  /**
   * Getting disk usage.
   *
   * @return disk usage
   */
  public Memory getMemory() {
    return memory;
  }

  /**
   * Setting memory.
   *
   * @param memory
   *          memory
   */
  public void setMemory(Memory memory) {
    this.memory = memory;
  }

  /**
   * Getting disk usage.
   *
   * @return disk usage
   */
  public Disk getDisk() {
    return disk;
  }

  /**
   * Setting disk usage.
   *
   * @param disk
   *          disk usage.
   */
  public void setDisk(Disk disk) {
    this.disk = disk;
  }

  /**
   * Input paramter check.
   *
   * @param ope
   *           operation type
   * @throws CheckDataException
   *           inpt paramter error
   */
  public void check(OperationType ope) throws CheckDataException {
    if (cpu == null && memory == null && disk == null) {
      throw new CheckDataException();
    }
    if (cpu != null) {
      if (cpu.getUseRate() == null) {
        throw new CheckDataException();
      }
    }
    if (memory != null) {
      if (memory.getUsed() == null) {
        throw new CheckDataException();
      }
    }
    if (disk != null) {
      if (disk.getDevices() == null || disk.getDevices().isEmpty()) {
        throw new CheckDataException();
      }
    }
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "FailureInfo  [cpu =" + cpu + ", memory =" + memory + ", disk =" + disk + "]";
  }
}
