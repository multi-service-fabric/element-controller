/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * EM Device Body Configuration POJO Class.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "config")
public class BodyMessage extends AbstractMessage {

  /** L2VLAN IF Batch Generation/Deletion POJO Class. */
  @XmlElement(name = "l2-slice")
  private L2SliceAddDelete l2SliceAddDelete = null;

  /** L3VLAN IF Batch Generation/Deletion POJO Class. */
  @XmlElement(name = "l3-slice")
  private L3SliceAddDelete l3SliceAddDelete = null;

  /** Leaf Extention/Removal POJO Class. */
  @XmlElement(name = "leaf")
  private LeafAddDelete leafAddDelete = null;

  /** Spin Extention/Removal POJO Class. */
  @XmlElement(name = "spine")
  private SpineAddDelete spineAddDelete = null;

  /** LAG Add/Delete for Internal Link POJO Class. */
  @XmlElement(name = "internal-link")
  private InternalLinkAddDelete internalLinkLagAddDelete = null;

  /** Recover Update Node POJO Class. */
  @XmlElement(name = "recover-node")
  private RecoverUpdateNode recoverUpdateNode = null;

	/** Recover Update Node (servise reconfiguration) POJO Class. */
  @XmlElement(name = "recover-service")
  private RecoverUpdateService recoverUpdateService = null;

  /** LAG Add/Delete for CE POJO Class. */
  @XmlElement(name = "ce-lag")
  private CeLagAddDelete ceLagAddDelete = null;

  /** B-Leaf Add/Delete/Update POJO Class. */
  @XmlElement(name = "b-leaf")
  private BLeafAddDelete bLeafAddDelete = null;

  /** POJO Class for Inter-Cluster Link Generation/Deletion. */
  @XmlElement(name = "cluster-link")
  private BetweenClustersLinkAddDelete betweenClustersLinkAddDelete = null;

  /** breakoutIF Registration/Deletion POJO Class. */
  @XmlElement(name = "breakout")
  private BreakoutIfAddDelete breakoutIfAddDelete = null;

  /**
   * Generating new instance.
   */
  public BodyMessage() {
    super();
  }

  /**
   * Getting L2VLAN IF batch generation/deletion POJO class.
   *
   * @return L2VLAN IF batch generation/deletion POJO class
   */
  public L2SliceAddDelete getL2SliceAddDelete() {
    return l2SliceAddDelete;
  }

  /**
   * Setting L2VLAN IF batch generation/deletion POJO class.
   *
   * @param l2SliceAddDelete
   *          L2VLAN IF batch generation/deletion POJO class
   */
  public void setL2SliceAddDelete(L2SliceAddDelete l2SliceAddDelete) {
    this.l2SliceAddDelete = l2SliceAddDelete;
  }

  /**
   * Getting L3VLAN IF batch generation/deletion POJO class.
   *
   * @return L3VLAN IF batch generation/deletion POJO class
   */
  public L3SliceAddDelete getL3SliceAddDelete() {
    return l3SliceAddDelete;
  }

  /**
   * Setting L3VLAN IF batch generation/deletion POJO class.
   *
   * @param l3SliceAddDelete
   *          L3VLAN IF batch generation/deletion POJO class
   */
  public void setL3SliceAddDelete(L3SliceAddDelete l3SliceAddDelete) {
    this.l3SliceAddDelete = l3SliceAddDelete;
  }

  /**
   * Getting Leaf extention/removal POJO class.
   *
   * @return Leaf extention/removal POJO class
   */
  public LeafAddDelete getLeafAddDelete() {
    return leafAddDelete;
  }

  /**
   * Setting Leaf extention/removal POJO class.
   *
   * @param leafAddDelete
   *          Leaf extention/removal POJO class
   */
  public void setLeafAddDelete(LeafAddDelete leafAddDelete) {
    this.leafAddDelete = leafAddDelete;
  }

  /**
   * Getting Spin extention/removal POJO class.
   *
   * @return Spin extention/removal POJO class
   */
  public SpineAddDelete getSpineAddDelete() {
    return spineAddDelete;
  }

  /**
   * Setting Spin extention/removal POJO class.
   *
   * @param spineAddDelete
   *          Spin extention/removal POJO class
   */
  public void setSpineAddDelete(SpineAddDelete spineAddDelete) {
    this.spineAddDelete = spineAddDelete;
  }

  /**
   * Getting LAG add/delete for internal Link POJO class.
   *
   * @return LAG add/delete for internal Link POJO class
   */
  public InternalLinkAddDelete getInternalLinkLagAddDelete() {
    return internalLinkLagAddDelete;
  }

  /**
   * Setting LAG add/delete for internal Link POJO class.
   *
   * @param internalLinkLagAddDelete
   *          LAG add/delete for internal Link POJO class
   */
  public void setInternalLinkLagAddDelete(InternalLinkAddDelete internalLinkLagAddDelete) {
    this.internalLinkLagAddDelete = internalLinkLagAddDelete;
  }

  /**
   * Getting Recover update node POJO class.
   *
   * @return recoverUpdateNode
   */
  public RecoverUpdateNode getRecoverUpdateNode() {
    return recoverUpdateNode;
  }

  /**
   * Setting Recover update node  POJO class.
   *
   * @param recoverUpdateNode
   *          setting recoverUpdateNode 
   */
  public void setRecoverUpdateNode(RecoverUpdateNode recoverUpdateNode) {
    this.recoverUpdateNode = recoverUpdateNode;
  }

  /**
   * Getting Recover Update Node (servise reconfiguration) POJO class.
   *
   * @return recoverUpdateService
   */
  public RecoverUpdateService getRecoverUpdateService() {
    return recoverUpdateService;
  }

  /**
   * Setting Recover Update Node (servise reconfiguration) POJO class.
   *
   * @param recoverUpdateService
   *          set recoverUpdateService
   */
  public void setRecoverUpdateService(RecoverUpdateService recoverUpdateService) {
    this.recoverUpdateService = recoverUpdateService;
  }

  /**
   * Getting LAG add/delete for CE POJO class.
   *
   * @return LAG add/delete for CE POJO class
   */
  public CeLagAddDelete getCeLagAddDelete() {
    return ceLagAddDelete;
  }

  /**
   * Setting LAG add/delete for CE POJO class.
   *
   * @param ceLagAddDelete
   *          LAG add/delete for CE POJO class
   */
  public void setCeLagAddDelete(CeLagAddDelete ceLagAddDelete) {
    this.ceLagAddDelete = ceLagAddDelete;
  }

  /**
   * Getting B-Leaf add/delete/update POJO class.
   *
   * @return B-Leaf add/delete/update POJO class
   */
  public BLeafAddDelete getBLeafAddDelete() {
    return bLeafAddDelete;
  }

  /**
   * Setting B-Leaf add/delete/update POJO class.
   *
   * @param bLeafAddDelete
   *          B-Leaf add/delete/update POJO class
   */
  public void setBLeafAddDelete(BLeafAddDelete bLeafAddDelete) {
    this.bLeafAddDelete = bLeafAddDelete;
  }

  /**
   * Creating POJO class for inter-cluster link generation/deletion.
   *
   * @return POJO class for inter-cluster link generation/deletion
   */
  public BetweenClustersLinkAddDelete getBetweenClustersLinkAddDelete() {
    return betweenClustersLinkAddDelete;
  }

  /**
   * Setting POJO class for inter-cluster link generation/deletion.
   *
   * @param betweenClustersLinkAddDelete
   *          POJO class for inter-cluster link generation/deletion
   */
  public void setBetweenClustersLinkAddDelete(BetweenClustersLinkAddDelete betweenClustersLinkAddDelete) {
    this.betweenClustersLinkAddDelete = betweenClustersLinkAddDelete;
  }

 /**
   * Getting breakoutIF registration/deletion POJO class.
   *
   * @return breakoutIF registration/deletion POJO class
   */
  public BreakoutIfAddDelete getBreakoutIfAddDelete() {
    return breakoutIfAddDelete;
  }

  /**
   * Setting breakoutIF registration/deletion POJO class.
   *
   * @param breakoutIfAddDelete
   *          breakoutIF registration/deletion POJO class
   */
  public void setBreakoutIfAddDelete(BreakoutIfAddDelete breakoutIfAddDelete) {
    this.breakoutIfAddDelete = breakoutIfAddDelete;
  }

  /*
   * Stringizing Instance.
   */
  @Override
  public String toString() {
    return "BodyMessage [l2SliceAddDelete=" + l2SliceAddDelete + ", l3SliceAddDelete=" + l3SliceAddDelete
        + ", leafAddDelete=" + leafAddDelete + ", spineAddDelete=" + spineAddDelete + ", internalLinkLagAddDelete="
        + internalLinkLagAddDelete + ", recoverUpdateNode=" + recoverUpdateNode + ", recoverUpdateService="
        + recoverUpdateService + ", ceLagAddDelete=" + ceLagAddDelete + ", bLeafAddDelete=" + bLeafAddDelete
        + ", betweenClustersLinkAddDelete=" + betweenClustersLinkAddDelete + ", breakoutIfAddDelete="
        + breakoutIfAddDelete + "]";
  }
}
