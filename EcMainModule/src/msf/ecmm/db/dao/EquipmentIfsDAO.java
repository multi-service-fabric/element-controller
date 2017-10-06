
package msf.ecmm.db.dao;

import static msf.ecmm.db.DBAccessException.*;
import msf.ecmm.db.DBAccessException;

import org.hibernate.Query;
import org.hibernate.Session;

public class EquipmentIfsDAO extends BaseDAO {

	public EquipmentIfsDAO(Session session) {
		this.session = session;
	}

	public void delete(String equipment_type_id) throws DBAccessException {
		try {
			Query query = session.getNamedQuery("deleteEquipmentIfs");
			query.setString("key", equipment_type_id);
			query.executeUpdate();
		} catch (Throwable e) {
			logger.debug("equipment_ifs delete failed.", e);
			this.errorMessage(DELETE_FAILURE, EQUIPMENT_IFS, e);
		}
	}
}
