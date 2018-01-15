/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

/**
 * Memory Usage Information at a Specific Time
 */
public class Memory {

  /**
   * Using Memory Size
   */
  private Float used;

  /**
   * Free Memory Size
   */
  private Float free;

  /**
   * Memory Size Allocated for Buffer Cache and Page Cache
   */
  private Float buffCache;

  /**
   * Using Virtual Memory Size
   */
  private Float swpd;

  /**
   * Getting using memory size.
   * @return using memory size
   */
  public Float getUsed() {
    return used;
  }

  /**
   * Setting using memory size.
   * @param used2 using memory size
   */
  public void setUsed(Float used2) {
    this.used = used2;
  }

  /**
   * Getting free memory size.
   * @return free memory size
   */
  public Float getFree() {
    return free;
  }

  /**
   * Setting free memory size.
   * @param free2 free memory size
   */
  public void setFree(Float free2) {
    this.free = free2;
  }

  /**
   * Getting memory size allocated for buffer cache and page cache.
   * @return memory size allocated for buffer cache and page cache
   */
  public Float getBuffCache() {
    return buffCache;
  }

  /**
   * Setting memory size allocated for buffer cache and page cache.
   * @param buffers memory size allocated for buffer cache and page cache
   */
  public void setBuffCache(Float buffers) {
    this.buffCache = buffers;
  }

  /**
   * Getting using virtual memory size.
   * @return using virtual memory size
   */
  public Float getSwpd() {
    return swpd;
  }

  /**
   * Setting using virtual memory size.
   * @param swapUsed using virtual memory size
   */
  public void setSwpd(Float swapUsed) {
    this.swpd = swapUsed;
  }

  @Override
  public String toString() {
    return "Memory [used=" + used + ", free=" + free + ", buffCache=" + buffCache + ", swpd=" + swpd + "]";
  }
}
