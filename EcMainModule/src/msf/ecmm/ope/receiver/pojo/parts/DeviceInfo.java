/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

/**
 * Information for each Device
 */
public class DeviceInfo {

  /**
   * Name of the device where the file system is located
   */
  private String fileSystem;

  /**
   * Mount Point
   */
  private String mountedOn;

  /**
   * Total Volume Available in File System
   */
  private Integer size;

  /**
   * Volume in use
   */
  private Integer used;

  /**
   * Volume Available
   */
  private Integer avail;

  /**
   * Getting device name where the file system is located.
   *
   * @return device name where the file system is located
   */
  public String getFileSystem() {
    return fileSystem;
  }

  /**
   * Setting device name where the file system is located.
   *
   * @param fileSystem
   *           device name where the file system is located
   */
  public void setFileSystem(String fileSystem) {
    this.fileSystem = fileSystem;
  }

  /**
   * Getting volume in use.
   *
   * @return volume in use
   */
  public String getMountedOn() {
    return mountedOn;
  }

  /**
   * Setting mount point.
   *
   * @param mountedOn
   *            mount point
   */
  public void setMountedOn(String mountedOn) {
    this.mountedOn = mountedOn;
  }

  /**
   * Getting total volume available in the file system.
   *
   * @return total volume available in the file system
   */
  public Integer getSize() {
    return size;
  }

  /**
   * Setting total volume available in the file system.
   *
   * @param size
   *           total volume available in the file system
   */
  public void setSize(Integer size) {
    this.size = size;
  }

  /**
   * Getting volume in use.
   *
   * @return volume in use
   */
  public Integer getUsed() {
    return used;
  }

  /**
   * Setting volume in use.
   *
   * @param used 
   *           volume in use
   */
  public void setUsed(Integer used) {
    this.used = used;
  }

  /**
   * Getting volume available.
   *
   * @return volume available
   */
  public Integer getAvail() {
    return avail;
  }

  /**
   * Setting volume available.
   *
   * @param avail
   *           volume available
   */
  public void setAvail(Integer avail) {
    this.avail = avail;
  }

  @Override
  public String toString() {
    return "DeviceInfo [fileSystem=" + fileSystem + ", mountedOn=" + mountedOn + ", size=" + size + ", used=" + used
        + ", avail=" + avail + "]";
  }
}
