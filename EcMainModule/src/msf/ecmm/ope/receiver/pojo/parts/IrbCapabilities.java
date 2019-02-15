/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */
package msf.ecmm.ope.receiver.pojo.parts;

/**
 * IRB configurability.
 */

public class IrbCapabilities {
  /** Asymmetric Configurability. */
  private Boolean asymmetric = null;

  /** Symmetric Configurability. */
  private Boolean symmetric = null;

  /**
   * Getting Asymmetric Configurability.
   *
   * @return vni
   */
  public Boolean getAsymmetric() {
    return asymmetric;
  }

  /**
   * Setting Asymmetric Configurability.
   *
   * @param asymmetric
   *          Setting asymmetric
   */
  public void setAsymmetric(Boolean asymmetric) {
    this.asymmetric = asymmetric;
  }

  /**
   * Getting Asymmetric Configurability.
   *
   * @return vni
   */
  public Boolean getSymmetric() {
    return symmetric;
  }

  /**
   * Setting Asymmetric Configurability.
   *
   * @param symmetric
   *          Setting asymmetric
   */
  public void setSymmetric(Boolean symmetric) {
    this.symmetric = symmetric;
  }

  /**
   * Stringizing Instance.
   *
   * @return Instance string
   */
  @Override
  public String toString() {
    return "IrbCapability [asymmetric=" + asymmetric + ", symmetric=" + symmetric + "]";
  }

}
