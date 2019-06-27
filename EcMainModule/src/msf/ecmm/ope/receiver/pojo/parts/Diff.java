/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

/**
 * Difference information.
 */
public class Diff {

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
   * Setting Config difference information.
   *
   * @param diffDataUnified
   *          Config difference information
   */
  public void setDiffDataUnified(String diffDataUnified) {
    this.diffDataUnified = diffDataUnified;
  }

  @Override
  public String toString() {
    return "Diff [diffDataUnified=" + diffDataUnified + "]";
  }

}
