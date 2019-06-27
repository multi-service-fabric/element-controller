/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

/**
 * Process Execution Request.
 */
public class ControlLogicalInterface extends AbstractRestMessage {

  /** Process Request Type. */
  private String action;

  /** Optional information for inter-cluster link generation request. */
  private CreateBetweenClustersLink addInterClusterLinkOption;

  /** Optional information for inter-cluster link deletion request. */
  private DeleteBetweenClustersLink delInterClusterLinkOption;

  /**
   * Getting process request type.
   *
   * @return process request type
   */
  public String getAction() {
    return action;
  }

  /**
   * Setting process request type.
   *
   * @param action
   *          process request type
   */
  public void setAction(String action) {
    this.action = action;
  }

  /**
   * Getting optional information for inter-cluster link generation request.
   *
   * @return optional information for inter-cluster link generation request
   */
  public CreateBetweenClustersLink getAddInterClusterLinkOption() {
    return addInterClusterLinkOption;
  }

  /**
   * Setting optional information for inter-cluster link generation request.
   *
   * @param option
   *          optional information for inter-cluster link generation request
   */
  public void setAddInterClusterLinkOption(CreateBetweenClustersLink option) {
    this.addInterClusterLinkOption = option;
  }

  /**
   * Getting optional information for inter-cluster link deletion request.
   *
   * @return optional information for inter-cluster link deletion request
   */
  public DeleteBetweenClustersLink getDelInterClusterLinkOption() {
    return delInterClusterLinkOption;
  }

  /**
   * Setting optional information for inter-cluster link deletion request.
   *
   * @param option
   *          optional information for inter-cluster link deletion request
   */
  public void setDelInterClusterLinkOption(DeleteBetweenClustersLink option) {
    this.delInterClusterLinkOption = option;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "ControlLogicalInterface [action=" + action + ", addInterClusterLinkOption=" + addInterClusterLinkOption
        + ", delInterClusterLinkOption=" + delInterClusterLinkOption + "]";
  }

}
