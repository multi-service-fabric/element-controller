/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import java.util.ArrayList;

import msf.ecmm.ope.receiver.pojo.parts.EcStatus;
import msf.ecmm.ope.receiver.pojo.parts.EmStatus;
import msf.ecmm.ope.receiver.pojo.parts.Informations;

/**
 * EC Status Confirmation
 */
public class CheckEcMainModuleStatus extends AbstractResponseMessage {

  /**
   * EC Status
   */
  private EcStatus ecStatus;

  /**
   * EM Status
   */
  private EmStatus emStatus;

  /**
   * Acquired information of OS or controller
   */
  private ArrayList<Informations> informations = new ArrayList<Informations>();

  /**
   * Getting EC status.
   *
   * @return EC status
   */
  public EcStatus getEcStatus() {
    return ecStatus;
  }

  /**
   * Setting EC status.
   *
   * @param ecStatus EC status
   */
  public void setEc_status(EcStatus ecStatus) {
    this.ecStatus = ecStatus;
  }

  /**
   *  Getting EM status.
   *
   * @return  EM status
   */
  public EmStatus getEmStatus() {
    return emStatus;
  }

  /**
   *  Setting EM status.
   *
   * @param emStatus  EM status
   */
  public void setEm_status(EmStatus emStatus) {
    this.emStatus = emStatus;
  }

  /**
   * Getting acquired information of OS or controller.
   *
   * @return acquired information of OS or controller
   */
  public ArrayList<Informations> getInformations() {
    return informations;
  }

  /**
   * Setting acquired information of OS or controller.
   *
   * @param informations
   *          acquired information of OS or controller
   */
  public void setInformations(ArrayList<Informations> informations) {
    this.informations = informations;
  }

  @Override
  public String toString() {
    return "CheckEcMainModuleStatus [ecStatus=" + ecStatus + ", emStatus=" + emStatus
      + ", informations=" + informations + "]";
  }


}
