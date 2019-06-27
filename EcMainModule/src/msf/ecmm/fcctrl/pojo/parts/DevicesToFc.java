/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.fcctrl.pojo.parts;

/**
 * Class of Information in each disk class.
 */
public class DevicesToFc {

  /** Device name at which file system is allocated. */
  private String fileSystem;

  /** Mount point. */
  private String mountedOn;

  /** Total of usable capacity in file system. */
  private Integer size;

  /** Used cpaacity. */
  private Integer used;

  /**
   * Getting device name which file system is allocated.
   *
   * @return device name which file system is allocated
   */
  public String getFileSystem() {
    return fileSystem;
  }

  /**
   * Setting device name which file system is allocated.
   *
   * @param fileSystem
   *          device name which file system is allocated
   */
  public void setFileSystem(String fileSystem) {
    this.fileSystem = fileSystem;
  }

  /**
   * Getting mount point.
   *
   * @return mount point
   */
  public String getMountedOn() {
    return mountedOn;
  }

  /**
   * Setting mount point.
   *
   * @param mountedOn
   *          mount point
   */
  public void setMountedOn(String mountedOn) {
    this.mountedOn = mountedOn;
  }

  /**
   * Getting total of usable capacity in file system.
   *
   * @return total of usable capacity in file system
   */
  public Integer getSize() {
    return size;
  }

  /**
   * Setting total of usable capacity in file system.
   *
   * @param size
   *          total of usable capacity in file system
   */
  public void setSize(Integer size) {
    this.size = size;
  }

  /**
   * Getting used capacity in file system.
   *
   * @return used capacity in file system
   */
  public Integer getUsed() {
    return used;
  }

  /**
   * Setting used capacity in file system.
   *
   * @param used
   *          used capacity in file system
   */
  public void setUsed(Integer used) {
    this.used = used;
  }

  /**
   *  Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "DevicesToFc [fileSystem=" + fileSystem + ", mountedOn=" + mountedOn + ", size=" + size + ", used=" + used
        + "]";
  }

}