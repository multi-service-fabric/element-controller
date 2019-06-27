/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */
package msf.ecmm.emctrl.restpojo;

import java.util.ArrayList;

import msf.ecmm.ope.receiver.pojo.parts.Informations;


/**
 * Controller Status Acquisition
 */
public class ControllerStatusFromEm extends AbstractResponse {

  /**
   * Service Start-up Status
   */
  private String status;

  /**
   * Acquired Information of OS or Controller
   */
  private ArrayList<Informations> informations = new ArrayList<Informations>();

  /**
   * Getting service start-up status.
   * @return service start-up status
   */
  public String getStatus() {
    return status;
  }

  /**
   * Setting service start-up status.
   * @param status service start-up status
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * Getting acquired information of OS or controller.
   * @return acquired information of OS or controller
   */
  public ArrayList<Informations> getInformations() {
    return informations;
  }

  /**
   * Setting acquired information of OS or controller.
   * @param informations acquired information of OS or controller
   */
  public void setInformations(ArrayList<Informations> informations) {
    this.informations = informations;
  }

  /* (Non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "ControllerStatusFromEm [status=" + status + ", informations=" + informations + "]";
  }
}
