
package msf.ecmm.db.dao;

import static msf.ecmm.db.DBAccessException.*;
import msf.ecmm.db.DBAccessException;

import org.hibernate.Query;
import org.hibernate.Session;

public class IfNameRulesDAO extends BaseDAO {

	public IfNameRulesDAO(Session session) {
		this.session = session;
	}

	public void delete(String equipment_type_id) throws DBAccessException {
		try {
			Query query = session.getNamedQuery("deleteIfNameRules");
			query.setString("key", equipment_type_id);
			query.executeUpdate();
		} catch (Throwable e) {
			logger.debug("if_name_rules delete failed.", e);
			this.errorMessage(DELETE_FAILURE, IF_NAME_RULES, e);
		}
	}
}
