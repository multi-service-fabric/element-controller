/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import java.util.ArrayList;
import java.util.List;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.parts.UpdateInternalLinkIfs;

/**
 * Changing Internal Link IF.
 */
public class InternalLinkChange extends AbstractRestMessage {

  /** List of Information of Internal Link to be changed. */
  private List<UpdateInternalLinkIfs> updateInternalLinkIfs = new ArrayList<UpdateInternalLinkIfs>();

  /**
   * Getting list of information of internal link to be changed.
   *
   * @return List of information of internal link to be changed
   */
  public List<UpdateInternalLinkIfs> getUpdateInternalLinkIfsList() {
    return updateInternalLinkIfs;
  }

  /**
   * Setting List of information of internal link to be changed.
   *
   * @param updateInternalLinkIfs
   *          List of information of internal link to be changed
   */
  public void setUpdateInternalLinkIfsList(ArrayList<UpdateInternalLinkIfs> updateInternalLinkIfs) {
    this.updateInternalLinkIfs = updateInternalLinkIfs;
  }

  @Override
  public String toString() {
    return "InternalLinkChange [updateInternalLinkIfs=" + updateInternalLinkIfs + "]";
  }

  /**
   * Input parameter check.
   *
   * @param ope
   *          Operation type
   * @throws CheckDataException
   *           Input check error
   */
  public void check(OperationType ope) throws CheckDataException {
    if (updateInternalLinkIfs.isEmpty()) {
      throw new CheckDataException();
    } else {
      for (UpdateInternalLinkIfs internalLinkIfs : updateInternalLinkIfs) {
        if (internalLinkIfs == null) {
          throw new CheckDataException();
        } else {
          internalLinkIfs.check(ope);
        }
      }
    }
  }
}
