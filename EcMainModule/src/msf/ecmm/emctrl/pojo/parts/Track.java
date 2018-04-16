/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo.parts;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Tracking IF Configuration Information
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Track {

  /** Tracking IF Information List */
  @XmlElement(name = "interface")
  List<TrackInterface> TrackInterfaceList = null;

  /**
   * Generating new instance.
   */
  public Track() {
    super();
  }

  /**
   * Getting tracking IF information list.
   *
   * @return tracking IF information list
   */
  public List<TrackInterface> getTrackInterfaceList() {
    return TrackInterfaceList;
  }

  /**
   * Setting tracking IF information list.
   *
   * @param TrackInterfaceList
   *          tracking IF information list
   */
  public void setTrackInterfaceList(List<TrackInterface> TrackInterfaceList) {
    this.TrackInterfaceList = TrackInterfaceList;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "Track [TrackInterfaceList=" + TrackInterfaceList + "]";
  }
}
