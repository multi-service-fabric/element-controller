/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import java.util.ArrayList;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * Neighbor Configuration
 */
public class Neighbor {

  /** Neighbor IPv4 Address List */
  private ArrayList<String> addresses = new ArrayList<String>();

  /**
   * Getting neighbor IPv4 address list.
   *
   * @return neighbor IPv4 address list
   */
  public ArrayList<String> getAddresses() {
    return addresses;
  }

  /**
   * Setting neighbor IPv4 address list.
   *
   * @param addresses
   *          neighbor IPv4 address list
   */
  public void setAddresses(ArrayList<String> addresses) {
    this.addresses = addresses;
  }

  /**
   * Stringizing Instance
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "Neighbor [addresses=" + addresses + "]";
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
    if (addresses.isEmpty()) {
      throw new CheckDataException();
    } else {
      for (String addr : addresses) {
        if (addr == null) {
          throw new CheckDataException();
        }
      }
    }
  }

}
