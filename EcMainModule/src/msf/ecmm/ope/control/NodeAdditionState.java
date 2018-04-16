/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.control;

/**
 * Device Extention Status Class Definition.
 * Defining the enumeration type of device extention status.
 */
public enum NodeAdditionState {

  /** Running (Complete). */
  Complete(0),

  /** During startup (Not Configured). */
  UnInitilized(1),

  /** During startup (Configuring Device). */
  Ready(2),

  /** During startup (Infrastructure Configuration Completed). */
  InfraSettingComplete(3),

  /** Out of service (Extention Failed) */
  Failed(4),

  /** Out of service (Infrastructure Configuration Failed) */
  FailedInfraSetting(5),

  /** Out of service (Recovery Service Failed) */
  ServiceRecoverFailed(6),

  /** Out of service (Other) */
  FailedOther(7),

  /** Out of service (Recovery Node Failed) */
  NodeRecoverFailed(8),

  ;

  /** Japanese Name Label. */
  private int value;

  /**
   * Constructor.
   *
   * @param number
   *          Japanese Name Label
   */
  private NodeAdditionState(int number) {
    this.value = number;
  }

  /**
   * @return numeric value.
   */
  public int getValue() {
    return this.value;
  }

  /**
   * Getting device extention status.
   *
   * @param number
   *          device extention status (numeric value)
   * @return device extention status (enum)
   */
  public static NodeAdditionState getState(int number) {

    NodeAdditionState statusStr = null;

    if (number == 1) {
      statusStr = UnInitilized;
    } else if (number == 2) {
      statusStr = Ready;
    } else if (number == 3) {
      statusStr = InfraSettingComplete;
    } else if (number == 0) {
      statusStr = Complete;
    } else if (number == 4) {
      statusStr = Failed;
    } else if (number == 6) {
      statusStr = ServiceRecoverFailed;
    } else if (number == 7) {
      statusStr = FailedOther;
    } else if (number == 8) {
      statusStr = NodeRecoverFailed;
    } else if (number == 5) {
      statusStr = FailedInfraSetting;
    }

    return statusStr;
  }

  /**
   * Converting label to enum type.
   *
   * @param status
   *          device extention status
   * @return label
   */
  public static String nodeAdditionStateToLabel(NodeAdditionState status) {
    String statusStr = "";

    if (status.name().equals(NodeAdditionState.UnInitilized.name())) {
      statusStr = "uninitilized";
    } else if (status.name().equals(NodeAdditionState.Ready.name())) {
      statusStr = "ready";
    } else if (status.name().equals(NodeAdditionState.InfraSettingComplete.name())) {
      statusStr = "infrasettingcomplete";
    } else if (status.name().equals(NodeAdditionState.Complete.name())) {
      statusStr = "complete";
    } else if (status.name().equals(NodeAdditionState.Failed.name())) {
      statusStr = "failed";
    } else if (status.name().equals(NodeAdditionState.FailedOther.name())) {
      statusStr = "recoverfailed";
    } else if (status.name().equals(NodeAdditionState.NodeRecoverFailed.name())) {
      statusStr = "noderecoverfailed";
    } else if (status.name().equals(NodeAdditionState.ServiceRecoverFailed.name())) {
      statusStr = "servicerecoverfailed";
    } else if (status.name().equals(NodeAdditionState.FailedInfraSetting.name())) {
      statusStr = "failedinfrasetting";
    }

    return statusStr;
  }
}
