/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.db.dao;

import static msf.ecmm.db.DBAccessException.*;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.pojo.LagMembers;

/**
 * The class in which LAG Member Information related DB process is performed.
 */
public class LagMembersDAO extends BaseDAO {

  /**
   * LAG Member Information Class Constructor.
   *
   * @param session
   *          data base session
   */
  public LagMembersDAO(Session session) {
    this.session = session;
  }

  /**
   * LAG Member Information Table INSERT.
   *
   * @param lagMembers
   *          LAG member information
   * @throws DBAccessException
   *           data base exception
   */
  public void save(LagMembers lagMembers) throws DBAccessException {
    try {
      LagMembers reglagMembers = this.search(lagMembers.getNode_id(), lagMembers.getLag_if_id(),
          lagMembers.getPhysical_if_id(), lagMembers.getBreakout_if_id());
      if (reglagMembers != null) {
        this.errorMessage(DOUBLE_REGISTRATION, LAG_MEMBERS, null);
      } else {
        session.save(lagMembers);
      }
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("lag_members insert failed.", e2);
      this.errorMessage(INSERT_FAILURE, LAG_MEMBERS, e2);
    }
  }

  /**
   * LAG Member Information Table DELETE.
   *
   * @param node_id
   *          device ID
   * @param lag_if_id
   *          LAGIF ID
   * @throws DBAccessException
   *           database exception
   */
  public void delete(String node_id, String lag_if_id) throws DBAccessException {
    try {
      LagMembers lagMembers = this.search(node_id, lag_if_id, null, null);
      if (lagMembers == null) {
        this.errorMessage(NO_DELETE_TARGET, LAG_MEMBERS, null);
      } else {
        Query query = session.getNamedQuery("deleteLagMembers");
        query.setString("key1", node_id);
        query.setString("key2", lag_if_id);
        query.executeUpdate();
      }
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("lag_members delete failed.", e2);
      this.errorMessage(DELETE_FAILURE, LAG_MEMBERS, e2);
    }
  }

  /**
   * LAG Member Information Table DELETE.
   *
   * @param members
   *          member to be deleted
   * @throws DBAccessException
   *           database exception
   */
  public void delete(LagMembers members) throws DBAccessException {
    try {
      LagMembers lagMembers = this.search(members.getNode_id(), members.getLag_if_id(), members.getPhysical_if_id(),
          members.getBreakout_if_id());
      if (lagMembers == null) {
        this.errorMessage(NO_DELETE_TARGET, LAG_MEMBERS, null);
      } else {
        if (null != members.getPhysical_if_id()) {
          Query query = session.getNamedQuery("deletePhysicalLagMembers");
          query.setString("key1", members.getNode_id());
          query.setString("key2", members.getLag_if_id());
          query.setString("key3", members.getPhysical_if_id());
          query.executeUpdate();
        } else {
          Query query = session.getNamedQuery("deleteBreakoutLagMembers");
          query.setString("key1", members.getNode_id());
          query.setString("key2", members.getLag_if_id());
          query.setString("key3", members.getBreakout_if_id());
          query.executeUpdate();
        }
      }
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("lag_members delete failed.", e2);
      this.errorMessage(DELETE_FAILURE, LAG_MEMBERS, e2);
    }
  }

  /**
   * LAG Member Information table SELECT.
   *
   * @param node_id
   *          device ID (primary key 1)
   * @param lag_if_id
   *          LAGIF ID (primary key 2)
   * @param physical_if_id
   *          Physical IF ID
   * @param breakout_if_id
   *          breakout IF ID
   * @return lagMembers LAGMember information
   * @throws DBAccessException
   *           database exception
   */
  @SuppressWarnings("unchecked")
  public LagMembers search(String node_id, String lag_if_id, String physical_if_id, String breakout_if_id)
      throws DBAccessException {
    LagMembers lagMembers = null;
    try {
      if (physical_if_id == null && breakout_if_id == null) {
        Query query = session.getNamedQuery("selectLagMembers");
        query.setString("key1", node_id);
        query.setString("key2", lag_if_id);
        List<LagMembers> lagMembersList = query.list();
        if ((lagMembersList != null) && (lagMembersList.size() != 0)) {
          lagMembers = lagMembersList.get(0);
        }
      } else if (physical_if_id != null) {
        Query query = session.getNamedQuery("selectPhysicalLagMembers");
        query.setString("key1", node_id);
        query.setString("key2", lag_if_id);
        query.setString("key3", physical_if_id);
        List<LagMembers> lagMembersList = query.list();
        if (!(lagMembersList == null) && lagMembersList.size() == 1) {
          lagMembers = lagMembersList.get(0);
        }
      } else {
        Query query = session.getNamedQuery("selectBreakoutLagMembers");
        query.setString("key1", node_id);
        query.setString("key2", lag_if_id);
        query.setString("key3", breakout_if_id);
        List<LagMembers> lagMembersList = query.list();
        if (!(lagMembersList == null) && lagMembersList.size() == 1) {
          lagMembers = lagMembersList.get(0);
        }
      }
    } catch (Throwable e1) {
      logger.debug("lag_members select failed.", e1);
      this.errorMessage(SEARCH_FAILURE, LAG_MEMBERS, e1);
    }
    return lagMembers;

  }
}
