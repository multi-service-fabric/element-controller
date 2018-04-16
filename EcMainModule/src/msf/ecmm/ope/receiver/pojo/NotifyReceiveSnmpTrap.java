/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import java.util.ArrayList;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.parts.Varbind;

/**
 * SNMPTrap Reception Notification
 */
public class NotifyReceiveSnmpTrap extends AbstractRestMessage {

	/** Sending Source Address (Management IF Address) */
  private String srcHostIp;

  /** VariableBinding (List of Notified OID and Values) */
  private ArrayList<Varbind> varbind = new ArrayList<Varbind>();

  /**
   * Getting sending source address (management IF address).
   *
   * @return sending source address (management IF address)
   */
  public String getSrcHostIp() {
    return srcHostIp;
  }

  /**
   * Setting sending source address (management IF address).
   *
   * @param srcHostIp
   *          sending source address (management IF address)
   */
  public void setSrcHostIp(String srcHostIp) {
    this.srcHostIp = srcHostIp;
  }

  /**
   * Getting VariableBinding (list of notified OID and values).
   *
   * @return VariableBinding (list of notified OID and values)
   */
  public ArrayList<Varbind> getVarbind() {
    return varbind;
  }

  /**
   * Setting VariableBinding (list of notified OID and values).
   *
   * @param varbind
   *          VariableBinding (list of notified OID and values)
   */
  public void setVarbind(ArrayList<Varbind> varbind) {
    this.varbind = varbind;
  }

  /**
   * Stringizing Instance
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "NotifyReceiveSnmpTrap [srcHostIp=" + srcHostIp + ", varbind=" + varbind + "]";
  }

  /**
   * Input Parameter Check
   *
   * @param ope
   *          operation type
   * @throws CheckDataException
   *           input check error
   */
  public void check(OperationType ope) throws CheckDataException {
    if (varbind.isEmpty()) {
      throw new CheckDataException();
    } else {
      for (Varbind snmpData : varbind) {
        snmpData.check(ope);
      }
    }
    if (srcHostIp == null) {
      throw new CheckDataException();
    } else {
    }
  }

}
