package msf.ecmm.convert;

import msf.ecmm.common.CommonDefinitions;

public class LogicalPhysicalConverter {

	public static String toSliceName(String slice_id) {
		return new String("slice" + slice_id);
	}

	public static String toVRF(Integer vrf_id) {
		return new String("vrf" + vrf_id.toString());
	}

	public static String toRouteTarget(int vrf_id, int plane) {
		return new String("target:" + vrf_id + ":" + plane);
	}

	public static String toRouteDistinguisher(int vrf_id, int clusterid, String node_id) {
		return new String(vrf_id + ":" + ((clusterid * 1000) + Integer.parseInt(node_id)));
	}

	public static String toNodeName(String node_type, int cluster_id, String node_id) {
		return new String(node_type + cluster_id + "-" + node_id);
	}

	public static String toLagIfName(String suffix, String lagifid) {
		return new String(suffix + lagifid);
	}

	public static String toPhysicalIfName(String suffix, String slotName) {
		return new String(suffix + slotName);
	}

	public static int toIntegerNodeType(String type) {
		int ret = 0;
		if (type.equals("leafs") || type.equals("leaf")) {
			ret = CommonDefinitions.NODE_TYPE_LEAF;
		} else {
			ret = CommonDefinitions.NODE_TYPE_SPINE;
		}
		return ret;
	}

	public static String toStringNodeType(int type) {
		String ret = "";
		if (type == CommonDefinitions.NODE_TYPE_LEAF) {
			ret = "leaf";
		} else {
			ret = "spine";
		}
		return ret;
	}

	public static int toIntegerECObstructionState(boolean state) {
		int ret = 0;
		if (state) {
			ret = CommonDefinitions.EC_BUSY_VALUE;
		} else {
			ret = CommonDefinitions.EC_IN_SERVICE_VALUE;
		}
		return ret;
	}

	public static boolean toBooleanECObstructionState(int state) {
		boolean ret = false;
		if (state == CommonDefinitions.EC_BUSY_VALUE) {
			ret = true;
		} else {
			ret = false;
		}
		return ret;
	}

	public static int toIntegerIFState(String state) {
		int ret = CommonDefinitions.IF_STATE_UNKNOWN;

		if (state.equals(CommonDefinitions.IF_STATE_OK_STRING)) {
			ret = CommonDefinitions.IF_STATE_OK;
		} else if (state.equals(CommonDefinitions.IF_STATE_NG_STRING)) {
			ret = CommonDefinitions.IF_STATE_NG;
		} else {
		}
		return ret;
	}

	public static String toStringIFState(int state) {
		String ret = CommonDefinitions.IF_STATE_UNKNOWN_STRING;

		if (state == CommonDefinitions.IF_STATE_OK) {
			ret = CommonDefinitions.IF_STATE_OK_STRING;
		} else if ((state == CommonDefinitions.IF_STATE_NG)||(state == CommonDefinitions.IF_STATE_NG_L3CP)) {
			ret = CommonDefinitions.IF_STATE_NG_STRING;
		} else {
		}

		return ret;
	}
}
