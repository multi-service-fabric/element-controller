/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.control;

/**
 * EC Status Class Definition. Defining enumerated type of EC status.
 *
 */
public enum ECMainState {

  /** Preparing for Start-up */
  StartReady(10),
  /** Starting-up */
  InService(100),
  /** Preparing for Termination */
  StopReady(90),
  /** Terminating */
  Stop(0),
  /** Switching Systems */
  ChangeOver(50),;

  /** Japanese Name Label */
  private int value;

  /**
   * Constractor.
   *
   * @param n
   *          japanese name label
   */
  private ECMainState(int n) {
    this.value = n;
  }

  /**
   * @return numeric value
   */
  public int getValue() {
    return this.value;
  }

  /**
   * @param num
   *          ECMain status (numeric value)
   * @return ECMain status (enum)
   */
  public static ECMainState getState(int num) {

    ECMainState statusStr = null;

    if (num == 10) {
      statusStr = StartReady;
    } else if (num == 100) {
      statusStr = InService;
    } else if (num == 90) {
      statusStr = StopReady;
    } else if (num == 0) {
      statusStr = Stop;
    } else if (num == 50) {
      statusStr = ChangeOver;
    }

    return statusStr;
  }

  /**
   * Converting label to enum type.
   *
   * @param status
   *          EC status
   * @return label
   */
  public static String ecMainStateToLabel(ECMainState status) {

    String statusStr = "";

    if (status.name().equals(ECMainState.StartReady.name())) {
      statusStr = "startready";
    } else if (status.name().equals(ECMainState.InService.name())) {
      statusStr = "inservice";
    } else if (status.name().equals(ECMainState.StopReady.name())) {
      statusStr = "stopready";
    } else if (status.name().equals(ECMainState.Stop.name())) {
      statusStr = "stop";
    } else if (status.name().equals(ECMainState.ChangeOver.name())) {
      statusStr = "changeover";
    }

    return statusStr;
  }
}
