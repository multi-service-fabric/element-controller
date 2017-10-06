
package msf.ecmm.common;

import java.util.HashMap;

public class LogFormatter {

	public static final int MSG_301001 = 301001;
	public static final int MSG_509002 = 509002;
	public static final int MSG_509004 = 509004;
	public static final int MSG_509006 = 509006;
	public static final int MSG_509008 = 509008;
	public static final int MSG_509010 = 509010;
	public static final int MSG_509012 = 509012;
	public static final int MSG_509014 = 509014;
	public static final int MSG_509016 = 509016;
	public static final int MSG_509018 = 509018;
	public static final int MSG_509020 = 509020;
	public static final int MSG_509022 = 509022;
	public static final int MSG_504024 = 504024;
	public static final int MSG_504026 = 504026;
	public static final int MSG_505028 = 505028;
	public static final int MSG_505030 = 505030;
	public static final int MSG_508032 = 508032;
	public static final int MSG_501034 = 501034;
	public static final int MSG_301036 = 301036;
	public static final int MSG_503038 = 503038;
	public static final int MSG_403040 = 403040;
	public static final int MSG_403042 = 403042;
	public static final int MSG_407044 = 407044;
	public static final int MSG_509046 = 509046;
	public static final int MSG_402048 = 402048;
	public static final int MSG_303050 = 303050;
	public static final int MSG_407052 = 407052;
	public static final int MSG_509054 = 509054;
	public static final int MSG_402056 = 402056;
	public static final int MSG_307058 = 307058;
	public static final int MSG_407060 = 407060;
	public static final int MSG_507062 = 507062;
	public static final int MSG_407064 = 407064;
	public static final int MSG_407066 = 407066;
	public static final int MSG_503068 = 503068;
	public static final int MSG_509070 = 509070 ;
	public static final int MSG_402072 = 402072 ;
	private static HashMap<Integer, String> dictionary = new HashMap<Integer, String>() {
		{
			put(MSG_301001, "Equipments register complete");
			put(MSG_509001, "Register error:duplicate registration[equipments]");
			put(MSG_509002, "Register error:duplicate registration[nodes]");
			put(MSG_509003, "Register error:duplicate registration[physical_ifs]");
			put(MSG_509004, "Register error:duplicate registration[lag_ifs]");
			put(MSG_509005, "Register error:duplicate registration[internal_link_ifs]");
			put(MSG_509006, "Register error:duplicate registration[l2_cps]");
			put(MSG_509007, "Register error:duplicate registration[l3_cps]");
			put(MSG_509008, "Update error:not found update[physical_ifs]");
			put(MSG_509009, "Update error:not found update[system_status]");
			put(MSG_509010, "Delete error:not found delete[equipments]");
			put(MSG_509011, "Delete error:not found delete[nodes]");
			put(MSG_509012, "Delete error:not found delete[physical_ifs]");
			put(MSG_509013, "Delete error:not found delete[lag_ifs]");
			put(MSG_509014, "Delete error:not found delete[internal_link_ifs]");
			put(MSG_509015, "Delete error:not found delete[l2_cps]");
			put(MSG_509016, "Delete error:not found delete[l3_cps]");
			put(MSG_509017, "Delete error:relation illegal[nodes]");
			put(MSG_509018, "Delete error:relation illegal[equipments]");
			put(MSG_509019, "DB Register error:[%s]");
			put(MSG_509020, "DB Delete error:[%s]");
			put(MSG_509021, "DB Search error:[%s]");
			put(MSG_509022, "DB Update error:[%s]");
			put(MSG_504023, "EM Connection error:[%s]");
			put(MSG_504024, "EM Send error:[%s]");
			put(MSG_504025, "EM Decode error:[%s]");
			put(MSG_504026, "EM Encode error:[%s]");
			put(MSG_505027, "SNMP send error:[%s]");
			put(MSG_505028, "DHCP start up error:[%s]");
			put(MSG_505029, "DHCP stop error:[%s]");
			put(MSG_505030, "Rsyslog restart error:[%s]");
			put(MSG_513031, "FC Request REST massege error [%s]");
			put(MSG_508032, "Config check error [%s = %s]");
			put(MSG_508033, "Not found required parameter error [%s]");
			put(MSG_501034, "REST Server start error");
			put(MSG_301035, "Receive REST request uri:[%s] data:[%s]");
			put(MSG_301036, "Send REST response uri:[%s] responsecode:[%s] data:[%s]");
			put(MSG_501037, "Send REST error response uri:[%s] responsecode:[%s]  data:[%s]");
			put(MSG_503038, "Failed reading config file");
			put(MSG_503039, "Failed starting process : [%s]");
			put(MSG_403040, "Operation preparing failed.");
			put(MSG_403041, "Operation failed. : [%s]");
			put(MSG_403042, "Oparation closing failed.");
			put(MSG_407043, "Interface Integrity Validation failed.");
			put(MSG_407044, "Traffic Data Gathering failed.");
			put(MSG_402045, "Operation [%s] can not execute.");
			put(MSG_509046, "Update error:not found update[NodesStartupNotification]");
			put(MSG_509047, "Delete error:not found delete[NodesStartupNotification]");
			put(MSG_402048, "Error occured in sending unsent node state notification. : [%s]");
			put(MSG_509049, "DB Session error");
			put(MSG_303050, "Starting EC main module.");
			put(MSG_407051, "Fail getting traffic data : nodeId= [%s], equipmentTypeId= [%s]");
			put(MSG_407052, "InterfaceIntegrityValidationManager is already started.");
			put(MSG_307053, "Start IF state integrate.");
			put(MSG_509054, "SystemStatus Data error");
			put(MSG_402055, "IF integrity is already started.");
			put(MSG_402056, "OperationControlManager is already started.");
			put(MSG_407057, "TrafficDataGatheringManager is already started");
			put(MSG_307058, "Gathering traffic data start.");
			put(MSG_509061, "Register error:duplicate registration[nodes_startup_notification]");
			put(MSG_507062, "Integrity execute scheduler start error.");
			put(MSG_507063, "Traffic data gathering scheduler start error.");
			put(MSG_407064, "Integrity execute scheduler stop error.");
			put(MSG_407065, "Traffic data gathering scheduler stop error.");
			put(MSG_407066, "Present traffic data gathering has not finished.");
			put(MSG_303067, "Booting EC main module has finished.");
			put(MSG_503068, "EC main module state update error in starting EC main module.");
			put(MSG_509069, "Register error:duplicate registration[system_status]");
			put(MSG_509070,"DB commit error.");
			put(MSG_303071,"EC main module is stop.");
			put(MSG_402072,"EC main module has been stopping.");
			put(MSG_409073,"DB warn : OppositeNodes not found");
		}
	};

	public String format(Integer msgNo, Object... args) {

		return String.format("[" + msgNo + "] " + dictionary.get(msgNo), args);

	}

}
