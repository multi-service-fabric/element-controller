
package msf.ecmm.db.dao;

import static msf.ecmm.common.LogFormatter.*;
import static msf.ecmm.db.DBAccessException.*;
import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.db.DBAccessException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

public class BaseDAO {

	protected Session session;

	protected static final int EQUIPMENT_IFS = 2;
	protected static final int NODES = 4;
	protected static final int LAG_IFS = 6;
	protected static final int L2_CPS = 8;
	protected static final int NODES_STARTUP_NOTICE = 10;
	public void errorMessage(int code, int tableName, Throwable e) throws DBAccessException {

		int errorMessage = -1;

		switch (code) {

		case DOUBLE_REGISTRATION:

			switch (tableName) {
			case EQUIPMENTS:
			case EQUIPMENT_IFS:
			case IF_NAME_RULES:
				errorMessage = MSG_509001;
				break;

			case NODES:
				errorMessage = MSG_509002;
				break;

			case PHYSICAL_IFS:
				errorMessage = MSG_509003;
				break;

			case LAG_IFS:
			case INTERNAL_LINK_IFS:
				errorMessage = MSG_509004;
				break;

			case L2_CPS:
				errorMessage = MSG_509006;
				break;

			case L3_CPS:
				errorMessage = MSG_509007;
				break;

			case NODES_STARTUP_NOTICE:
				errorMessage = MSG_509061;
				break;

			case SYSTEM_STATUS:
				errorMessage = MSG_509069;
				break;

			default:
				errorMessage = MSG_509019;
				break;
			}

			break;

		case NO_UPDATE_TARGET:

			switch (tableName) {
			case PHYSICAL_IFS:
				errorMessage = MSG_509008;
				break;

			case NODES_STARTUP_NOTICE:
				errorMessage = MSG_509046;
				break;

			default:
				errorMessage = MSG_509022;
				break;
			}
			break;

		case NO_DELETE_TARGET:

			switch (tableName) {
			case EQUIPMENTS:
			case EQUIPMENT_IFS:
			case IF_NAME_RULES:
				errorMessage = MSG_509010;
				break;

			case NODES:
				errorMessage = MSG_509011;
				break;

			case PHYSICAL_IFS:
				errorMessage = MSG_509012;
				break;

			case LAG_IFS:
				errorMessage = MSG_509013;
				break;

			case INTERNAL_LINK_IFS:
				errorMessage = MSG_509014;
				break;

			case L2_CPS:
				errorMessage = MSG_509015;
				break;

			case L3_CPS:
				errorMessage = MSG_509016;
				break;

			case NODES_STARTUP_NOTICE:
				errorMessage = MSG_509047;
				break;

			default:
				errorMessage = MSG_509020;
				break;

			}

			break;

		case RELATIONSHIP_UNCONFORMITY:

			switch (tableName) {
			case NODES:
				errorMessage = MSG_509017;
				break;
			default:
				errorMessage = -1;
				break;
			}

			break;

		case SERCH_FAILURE:
			errorMessage = MSG_509021;
			break;

		case INSERT_FAILURE:
			errorMessage = MSG_509019;
			break;

		case UPDATE_FAILURE:
			errorMessage = MSG_509022;
			break;

		case DELETE_FAILURE:
			errorMessage = MSG_509020;
			break;

		case SYSTEM_FAILURE:
			errorMessage = MSG_509054;
			break;

		default:
			errorMessage = -1;
			break;

		}

		if (e != null) {
			throw new DBAccessException(code, errorMessage, e);
		} else {
			throw new DBAccessException(code, errorMessage);
		}

	}
}
