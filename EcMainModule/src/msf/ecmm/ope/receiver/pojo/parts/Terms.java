/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * Condition Information.
 */
public class Terms {

  /** Opeartion type. */
  private String operation;

  /** Conditon Name. */
  private String termName;

  /** Action. */
  private String action;

  /** Direction. */
  private String direction;

  /** Transmission Source MAC Address. */
  private String sourceMacAddress;

  /** Transmission Destination MAC Address. */
  private String destMacAddress;

  /** Transmission Source IP Address. */
  private String sourceIpAddress;

  /** Transmission Destination IP Address. */
  private String destIpAddress;

  /** Protocol. */
  private String protocol;

  /** Transmission Source Port Number. */
  private String sourcePort;

  /** Tranmission Destination Port Number. */
  private String destPort;

  /** Action at the time of filter addition(discard). */
  private final String AddAction = "discard";

  /** Direction at the time of filter addition(in). */
  private final String AddDirection = "in";

  /**
   * Getting operation type.
   *
   * @return  Operation Type
   */
  public String getOperation() {
    return operation;
  }

  /**
   * Setting operation type.
   *
   * @param operation
   *          Operation Type
   */
  public void setOperation(String operation) {
    this.operation = operation;
  }

  /**
   * Getting condition name.
   *
   * @return condition name
   */
  public String getTermName() {
    return termName;
  }

  /**
   * Setting condition name.
   *
   * @param termName
   *          condition name
   */
  public void setTermName(String termName) {
    this.termName = termName;
  }

  /**
   * Getting action.
   *
   * @return  Action
   */
  public String getAction() {
    return action;
  }

  /**
   * Setting action.
   *
   * @param action
   *          Action
   */
  public void setAction(String action) {
    this.action = action;
  }

  /**
   * Getting direction.
   *
   * @return  Direction
   */
  public String getDirection() {
    return direction;
  }

  /**
   * Setting direction.
   *
   * @param direction
   *          Direction
   */
  public void setDirection(String direction) {
    this.direction = direction;
  }

  /**
   * Getting Transmission Source MAC Address.
   *
   * @return  Transmission Source MAC Address
   */
  public String getSourceMacAddress() {
    return sourceMacAddress;
  }

  /**
   * Setting Transmission Source MAC Address.
   *
   * @param sourceMacAddress
   *          Transmission Source MAC Address
   */
  public void setSourceMacAddress(String sourceMacAddress) {
    this.sourceMacAddress = sourceMacAddress;
  }

  /**
   * Getting Transmission Destination MAC Address.
   *
   * @return  Getting Transmission Destination MAC Address.
   */
  public String getDestMacAddress() {
    return destMacAddress;
  }

  /**
   * Setting Transmission Destination MAC Address.
   *
   * @param destMacAddress
   *          Acquire Transmission Destination MAC Address.
   */
  public void setDestMacAddress(String destMacAddress) {
    this.destMacAddress = destMacAddress;
  }

  /**
   * Getting Transmission Source IP Address.
   *
   * @return Transmission Source IP Address
   */
  public String getSourceIpAddress() {
    return sourceIpAddress;
  }

  /**
   * Setting Transmission Source IP Address.
   *
   * @param sourceIpAddress
   *          Transmission Source IP Address
   */
  public void setSourceIpAddress(String sourceIpAddress) {
    this.sourceIpAddress = sourceIpAddress;
  }

  /**
   * Getting Transmission Destination IP Address.
   *
   * @return Transmission Destination IP Address
   */
  public String getDestIpAddress() {
    return destIpAddress;
  }

  /**
   * Setting Transmission Destination IP Address.
   *
   * @param destIpAddress
   *          Transmission Destination IP Address
   */
  public void setDestIpAddress(String destIpAddress) {
    this.destIpAddress = destIpAddress;
  }

  /**
   * Getting protocol.
   *
   * @return Protocol
   */
  public String getProtocol() {
    return protocol;
  }

  /**
   * Setting protocol.
   *
   * @param protocol
   *          Protocol
   */
  public void setProtocol(String protocol) {
    this.protocol = protocol;
  }

  /**
   * Getting Transmission Source Port Number.
   *
   * @return Transmission Source Port Number
   */
  public String getSourcePort() {
    return sourcePort;
  }

  /**
   * Setting Transmission Source Port Number.
   *
   * @param sourcePort
   *          Transmission Source Port Number
   */
  public void setSourcePort(String sourcePort) {
    this.sourcePort = sourcePort;
  }

  /**
   * Getting Transmission Destination Port Number.
   *
   * @return  Transmission Destination Port Number
   */
  public String getDestPort() {
    return destPort;
  }

  /**
   * Setting Transmission Destination Port Number.
   *
   * @param destPort
   *          Transmission Destination Port Number
   */
  public void setDestPort(String destPort) {
    this.destPort = destPort;
  }

  @Override
  public String toString() {
    return "Terms [operation=" + operation + ", termName=" + termName + ", action=" + action + ", direction="
        + direction + ", sourceMacAddress=" + sourceMacAddress + ", destMacAddress=" + destMacAddress
        + ", sourceIpAddress=" + sourceIpAddress + ", destIpAddress=" + destIpAddress + ", protocol=" + protocol
        + ", sourcePort=" + sourcePort + ", destPort=" + destPort + ", AddAction=" + AddAction + ", AddDirection="
        + AddDirection + "]";
  }

  /**
   * Checking input parameter.
   *
   * @param ope
   *          Operation Type
   * @throws CheckDataException
   *           Input Check Error
   */
  public void check(OperationType ope) throws CheckDataException {
    if (operation == null) {
      throw new CheckDataException();
    } else {
      if (operation.equals(CommonDefinitions.OPERATION_TYPE_ADD)) {
        if (!(action.equals(AddAction))) {
          throw new CheckDataException();
        }
        if (!(direction.equals(AddDirection))) {
          throw new CheckDataException();
        }
      } else if (operation.equals(CommonDefinitions.OPERATION_TYPE_REPLACE)) {
        throw new CheckDataException();
      } else if (operation.equals(CommonDefinitions.OPERATION_TYPE_DELETE)) {
        if (termName == null) {
          throw new CheckDataException();
        }
      }
    }
  }

}
