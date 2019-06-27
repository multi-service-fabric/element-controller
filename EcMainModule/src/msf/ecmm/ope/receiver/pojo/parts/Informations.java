/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

/**
 * Acquired Information of OS or Controller
 */
public class Informations {

  /**
   * Controller Type.
   */
  private String controllerType;

  /**
   * Controller Host Name
   */
  private String hostName;

  /**
   * Controller Management IP Address
   */
  private String managementIpAddress;

  /**
   * Server Machine Resource Information
   */
  private OsInfo os;

  /**
   * Controller Related Information
   */
  private ControllerInfo controller;

  /**
   * Getting controller type.
   * @return controller type.
   */
  public String getControllerType() {
      return controllerType;
  }

  /**
   * Setting controller type.
   * @param controllerType controller type.
   */
  public void setControllerType(String controllerType) {
      this.controllerType = controllerType;
  }

  /**
   * Getting controller host name.
   * @return controller host name
   */
  public String getHostName() {
    return hostName;
  }

  /**
   * Setting controller host name.
   * @param hostName controller host name
   */
  public void setHostName(String hostName) {
    this.hostName = hostName;
  }

  /**
   * Getting controller management IP address.
   * @return controller management IP address
   */
  public String getManagementIpAddress() {
    return managementIpAddress;
  }

  /**
   * Setting controller management IP address.
   * @param managementIpAddress controller management IP address
   */
  public void setManagementIpAddress(String managementIpAddress) {
    this.managementIpAddress = managementIpAddress;
  }

  /**
   * Getting server machine resource information.
   * @return server machine resource information
   */
  public OsInfo getOs() {
    return os;
  }

  /**
   * Setting server machine resource information.
   * @param os server machine resource information
   */
  public void setOs(OsInfo os) {
    this.os = os;
  }

  /**
   * Getting controller related information.
   * @return controller related information
   */
  public ControllerInfo getController() {
    return controller;
  }

  /**
   * Setting controller related information.
   * @param controller controller related information
   */
  public void setController(ControllerInfo controller) {
    this.controller = controller;
  }

  @Override
  public String toString() {
    return "Informations [controllerType=" + controllerType + ", hostName=" + hostName + ", managementIpAddress="
        + managementIpAddress + ", os=" + os + ", controller=" + controller + "]";
  }
}
