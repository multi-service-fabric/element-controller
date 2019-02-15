/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

/**
 * Condition Information.
 */
public class GetTerms {

  /** Condition Name. */
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

  /** Transmission Destination Port Number. */
  private String destPort;

  /**
   * Getting condition name.
   *
   * @return  Condition Name
   */
  public String getTermName() {
    return termName;
  }

  /**
   * Setting the condition name.
   *
   * @param termName
   *          Condition Name
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
   * Getting the direction.
   *
   * @return  Direction
   */
  public String getDirection() {
    return direction;
  }

  /**
   * Setting the direction.
   *
   * @param direction
   *          Direction
   */
  public void setDirection(String direction) {
    this.direction = direction;
  }

  /**
   * Getting the Transmission Source MAC Address.
   *
   * @return  Transmission Source MAC Address
   */
  public String getSourceMacAddress() {
    return sourceMacAddress;
  }

  /**
   * Setting the Transmission Source MAC Address.
   *
   * @param sourceMacAddress
   *          Transmission Source MAC Address
   */
  public void setSourceMacAddress(String sourceMacAddress) {
    this.sourceMacAddress = sourceMacAddress;
  }

  /**
   * Getting the Transmission Destination MAC Address.
   *
   * @return  Getting the Transmission Destination MAC Address.
   */
  public String getDestMacAddress() {
    return destMacAddress;
  }

  /**
   * Setting the Transmission Destination MAC Address.
   *
   * @param destMacAddress
   *          Getting the Transmission Destination MAC Address.
   */
  public void setDestMacAddress(String destMacAddress) {
    this.destMacAddress = destMacAddress;
  }

  /**
   * Getting the Transmission Source IP Address.
   *
   * @return Transmission SourceIPAddress
   */
  public String getSourceIpAddress() {
    return sourceIpAddress;
  }

  /**
   * Setting the Transmission Source IP Address.
   *
   * @param sourceIpAddress
   *          Transmission SourceIPAddress
   */
  public void setSourceIpAddress(String sourceIpAddress) {
    this.sourceIpAddress = sourceIpAddress;
  }

  /**
   * Getting the Transmission Destination IP Address.
   *
   * @return Transmission DestinationIPAddress
   */
  public String getDestIpAddress() {
    return destIpAddress;
  }

  /**
   * Setting the Transmission Destination IP Address.
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
   * @return  Protocol
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
   * @return  Transmission Source Port Number
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
    return "Terms [" + "termName" + termName + ", action=" + action + ", direction=" + direction + ", sourceMacAddress="
        + sourceMacAddress + ", destMacAddress=" + destMacAddress + ", sourceIpAddress=" + sourceIpAddress
        + ", destIpAddress=" + destIpAddress + ", protocol=" + protocol + ", sourcePort=" + sourcePort + ", destPort="
        + destPort + "]";
  }
}
