package msf.ecmm.devctrl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.CommonUtil;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.config.EcConfiguration;
import msf.ecmm.db.pojo.Equipments;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.devctrl.pojo.SnmpIfOperStatus;
import msf.ecmm.devctrl.pojo.SnmpIfTraffic;
import msf.ecmm.ope.receiver.pojo.parts.Varbind;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Counter64;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class SnmpController {

	private final Logger logger = LogManager.getLogger(CommonDefinitions.EC_LOGGER);

	private static final String OID_ifOperStatus = ".1.3.6.1.2.1.2.2.1.8";

	private static final String OID_ifxEntry = ".1.3.6.1.2.1.31.1.1.1";

	private static final String OID_ifHCOutOctets = ".1.3.6.1.2.1.31.1.1.1.10";

	@SuppressWarnings("unused")
	private static final String OID_linkUp = ".1.3.6.1.6.3.1.1.5.4";

	private static final int OSPF_NEIGHBOR_FULL = 8;

	public SnmpController() {

	}

	public ArrayList<SnmpIfOperStatus> getIfOperStatus(Equipments eq, Nodes node) throws DevctrlException {
		logger.debug("start eq=" + eq + " node=" + node);

		HashMap<Integer, String> ifMap = getIfMap(eq, node);

		ArrayList<VariableBinding> vars = getbulk(eq, node, OID_ifOperStatus);

		ArrayList<SnmpIfOperStatus> ret = new ArrayList<>();
		for (VariableBinding varbind : vars) {
			ret.add(new SnmpIfOperStatus(ifMap.get(varbind.getOid().get(varbind.getOid().size()-1)),
					varbind.getVariable().toInt()));
		}

		logger.trace("ret=" + ret);
		return ret;
	}

	public String getIfName(Equipments eq, Nodes node, int ifIndex) throws DevctrlException {
		logger.debug("start eq=" + eq + " node=" + node + " ifIndex=" + ifIndex);
		VariableBinding varbind = get(node, toDotted(eq.getIf_name_oid()) + "." + ifIndex);
		return varbind.getVariable().toString();
	}

	public boolean isOspfNeighborFull(Equipments eq, Nodes node, ArrayList<String> neighbors) throws DevctrlException {
		logger.debug("start eq=" + eq + " node=" + node + " neighbors=" + neighbors);

		int retryNum = EcConfiguration.getInstance().get(Integer.class, EcConfiguration.OSPF_NEIGHBOR_RETRY_NUM);
		int retryInt = EcConfiguration.getInstance().get(Integer.class, EcConfiguration.OSPF_NEIGHBOR_RETRY_INTERVAL);

		for (int i = 0; i <= retryNum; i++) {
			@SuppressWarnings("unchecked")
			ArrayList<String> chkList = (ArrayList<String>) neighbors.clone();

			ArrayList<VariableBinding> vars = null ;
			try{
				vars = getbulk(eq, node, OID_ospfNbrState);
			}catch(DevctrlException de){
				logger.debug("getbulk() fail.",de);
				CommonUtil.sleep(retryInt);
				continue ;
			}

			for (VariableBinding varbind : vars) {

				if (varbind.getVariable().toInt() == OSPF_NEIGHBOR_FULL) {
					String addr = varbind.getOid().toString().substring(OID_ospfNbrState.length(),varbind.getOid().toString().length()-2);
					chkList.remove(addr);
				}
			}

			if (chkList.size() == 0) {
				logger.debug("OspfNeighborFull complete.");
				return true;
			}

			CommonUtil.sleep(retryInt);

		}
		return false;
	}

	public ArrayList<SnmpIfTraffic> getTraffic(Equipments eq, Nodes node) throws DevctrlException {
		logger.debug("start eq=" + eq + " node=" + node);

		HashMap<Integer, String> ifMap = getIfMap(eq, node);

		ArrayList<VariableBinding> vars = getbulk(eq, node, OID_ifxEntry);

		HashMap<Integer, SnmpIfTraffic> traffic = new HashMap<>();
		for (VariableBinding varbind : vars) {

			if (varbind.getOid().toString().startsWith(OID_ifHCInOctets.substring(1))) {
				int ifIndex = varbind.getOid().get(varbind.getOid().size()-1);
				SnmpIfTraffic data = traffic.get(ifIndex);
				if (data == null) {
					data = new SnmpIfTraffic(ifMap.get(ifIndex), 0, 0);
					traffic.put(ifIndex, data);
				}
				data.setInOctets(((Counter64) varbind.getVariable()).toLong());
			}

			if (varbind.getOid().toString().startsWith(OID_ifHCOutOctets.substring(1))) {
				int ifIndex = varbind.getOid().get(varbind.getOid().size()-1);
				SnmpIfTraffic data = traffic.get(ifIndex);
				if (data == null) {
					data = new SnmpIfTraffic(ifMap.get(ifIndex), 0, 0);
					traffic.put(ifIndex, data);
				}
				data.setOutOctets(((Counter64) varbind.getVariable()).toLong());
			}
		}

		ArrayList<SnmpIfTraffic> ret = new ArrayList<>();
		traffic.forEach((k, v) -> ret.add(v));

		logger.debug("traffic : " + ret);

		return ret;
	}

	public static int getIfIndexForTrap(List<Varbind> varBinds){
		for(Varbind vb : varBinds){
			if(vb.getOid().startsWith(OID_ifIndex)){
				return Integer.parseInt(vb.getValue());
			}
		}
		return IFINDEX_NOT_FOUND ;
	}

	public static String getIfNameForTrap(List<Varbind> varBinds , String ifNameOid){
		for(Varbind vb : varBinds){
			if(vb.getOid().startsWith(toDotted(ifNameOid))){
				return vb.getValue();
			}
		}
		return null ;
	}

	private CommunityTarget init(Nodes node) {
		CommunityTarget target = new CommunityTarget();
		target.setAddress(new UdpAddress(node.getManagement_if_address() + "/" + SNMP_PORT));
		target.setCommunity(new OctetString(node.getSnmp_community()));
		target.setTimeout(EcConfiguration.getInstance().get(Integer.class, EcConfiguration.DEVICE_SNMP_TIMEOUT) * 1000);
		target.setRetries(1);
		target.setVersion(SnmpConstants.version2c);
		return target;
	}

	private ArrayList<VariableBinding> getbulk(Equipments eq, Nodes node, String oid) throws DevctrlException {
		logger.debug("start eq=" + eq + " node" + node + " oid=" + oid);

		ArrayList<VariableBinding> ret = new ArrayList<>();

		CommunityTarget target = init(node);

		OID nextOID = new OID(oid);

		Snmp snmp = null;
		try {
			DefaultUdpTransportMapping utm = new DefaultUdpTransportMapping();
			snmp = new Snmp(utm);
			snmp.listen();

			for (;;) {

				PDU pdu = new PDU();
				pdu.setType(PDU.GETBULK);
				pdu.setMaxRepetitions(eq.getMax_repetitions());
				pdu.setNonRepeaters(0);
				pdu.add(new VariableBinding(nextOID));

				ResponseEvent response = snmp.getBulk(pdu, target);
				PDU resPdu = response.getResponse();

				if (resPdu != null && resPdu.getErrorStatus() == SnmpConstants.SNMP_ERROR_NO_SUCH_NAME) {
					logger.debug("SNMP GETBULK(NoSuchName)={"+ret+"}");
					return ret;
				}

				if (resPdu != null && resPdu.getErrorStatus() == SnmpConstants.SNMP_ERROR_SUCCESS) {
					for (int i = 0; i < resPdu.size(); i++) {
							ret.add(resPdu.get(i));
						} else {
							logger.debug("SNMP GETBULK={"+ret+"}");
							return ret;
						}
					}

					if (resPdu.size() ==0) {
						logger.debug("SNMP GETBULK (size=0)={"+ret+"}");
						return ret;
					}

					nextOID = resPdu.get(resPdu.size() - 1).getOid();

				} else {
					logger.error(LogFormatter.out.format(LogFormatter.MSG_505027, resPdu));
					throw new DevctrlException("GetBulk fail.");
				}
			}

		} catch (IOException e) {
			logger.error(LogFormatter.out.format(LogFormatter.MSG_505027, e));
			throw new DevctrlException("GetBulk fail.");
		} finally {
			try {
				if (snmp != null)
					snmp.close();
			} catch (IOException e) {
			}
		}

	}

	private HashMap<Integer, String> getIfMap(Equipments eq, Nodes node) throws DevctrlException {
		HashMap<Integer, String> map = new HashMap<>();
		ArrayList<VariableBinding> vars = getbulk(eq, node, toDotted(eq.getIf_name_oid()));
		for (VariableBinding varbind : vars) {
			map.put(varbind.getOid().get(varbind.getOid().size()-1), varbind.getVariable().toString());
		}
		return map;
	}

	private VariableBinding get(Nodes node, String oid) throws DevctrlException {
		logger.debug("start node" + node + " oid=" + oid);

		CommunityTarget target = init(node);

		PDU pdu = new PDU();
		pdu.setType(PDU.GET);
		pdu.add(new VariableBinding(new OID(oid)));

		Snmp snmp = null;

		try {
			DefaultUdpTransportMapping utm = new DefaultUdpTransportMapping();
			snmp = new Snmp(utm);
			snmp.listen();

			ResponseEvent response = snmp.get(pdu, target);

			PDU resPdu = response.getResponse();
			if (resPdu != null && resPdu.getErrorStatus() == SnmpConstants.SNMP_ERROR_SUCCESS) {
				logger.debug("SNMP GET={"+resPdu.get(0)+"}");
				return resPdu.get(0);
			} else {
				logger.error(LogFormatter.out.format(LogFormatter.MSG_505027, resPdu));
				throw new DevctrlException("Get fail.");
			}
		} catch (IOException e) {
			logger.error(LogFormatter.out.format(LogFormatter.MSG_505027, e));
			throw new DevctrlException("Get fail.");
		} finally {
			try {
				if (snmp != null)
					snmp.close();
			} catch (IOException e) {
			}
		}

	}

	static private String toDotted(String val) {
		if(!val.startsWith(".")){
			return "."+val ;
		}else{
			return val ;
		}
	}
}
