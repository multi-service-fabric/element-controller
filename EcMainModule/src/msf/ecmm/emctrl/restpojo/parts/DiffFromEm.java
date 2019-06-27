/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.restpojo.parts;

/**
 * Difference information(EM receives).
 */
public class DiffFromEm {

  /** Config difference information. */
  private String diffDataUnified;

  /**
   * Getting Config difference information.
   *
   * @return Config difference information
   */
  public String getDiffDataUnified() {
    return diffDataUnified;
  }

  /**
   * Getting Config difference information.
   *
   * @param diffDataUnified
   *           Config difference information
   */
  public void setDiffDataUnified(String diffDataUnified) {
    this.diffDataUnified = diffDataUnified;
  }

  @Override
  public String toString() {
    return "DiffFromEm [diffDataUnified=" + diffDataUnified + "]";
  }

}
