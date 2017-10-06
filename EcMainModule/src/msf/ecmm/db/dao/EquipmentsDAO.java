
package msf.ecmm.db.dao;

import static msf.ecmm.db.DBAccessException.*;

import java.util.List;

import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.pojo.Equipments;

import org.hibernate.Query;
import org.hibernate.Session;

public class EquipmentsDAO extends BaseDAO {

	public EquipmentsDAO(Session session) {
		this.session = session;
	}

	public void save(Equipments ePojo) throws DBAccessException {
		try {
			Equipments equipments = session.get(Equipments.class, ePojo.getEquipment_type_id());
			if (equipments != null) {
				this.errorMessage(DOUBLE_REGISTRATION, EQUIPMENTS, null);
			} else {
				session.save(ePojo);
			}
		} catch (DBAccessException e) {
			throw e;
		} catch (Throwable e) {
			logger.debug("equipments insert failed.", e);
			this.errorMessage(INSERT_FAILURE, EQUIPMENTS, e);
		}
	}

	public Equipments search(String equipment_type_id) throws DBAccessException {
		Equipments equipments = null;
		try {
			equipments = session.get(Equipments.class, equipment_type_id);
		} catch (Throwable e) {
			logger.debug("equipments select failed.", e);
			this.errorMessage(SERCH_FAILURE, EQUIPMENTS, e);
		}
		return equipments;
	}

	@SuppressWarnings("unchecked")
	public List<Equipments> getList() throws DBAccessException {
		List<Equipments> list = null;
		try {
			list = session.createCriteria(Equipments.class).list();
		} catch (Throwable e) {
			logger.debug("equipments all select failed.", e);
			this.errorMessage(SERCH_FAILURE, EQUIPMENTS, e);
		}
		return list;
	}

	public void delete(String equipment_type_id) throws DBAccessException {
		try {
			Equipments equipments = session.get(Equipments.class, equipment_type_id);
			if (equipments == null) {
				this.errorMessage(NO_DELETE_TARGET, EQUIPMENTS, null);
			} else {
				Query query = session.getNamedQuery("deleteEquipments");
				query.setString("key", equipment_type_id);
				query.executeUpdate();
			}
		} catch (DBAccessException e) {
			throw e;
		} catch (Throwable e) {
			logger.debug("equipments delete failed.", e);
			this.errorMessage(DELETE_FAILURE, EQUIPMENTS, e);
		}
	}
}
