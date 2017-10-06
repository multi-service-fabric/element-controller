package msf.ecmm.traffic.pojo;

import java.util.HashMap;

import msf.ecmm.db.pojo.Equipments;
import msf.ecmm.db.pojo.InternalLinkIfs;
import msf.ecmm.db.pojo.L2Cps;
import msf.ecmm.db.pojo.L3Cps;
import msf.ecmm.db.pojo.Nodes;

public class DeviceInformationSet {

	private Nodes equipmentsData = null;

	private HashMap<SliceKeySet,L3Cps> l3SliceData = null;

	public DeviceInformationSet() {
		super();
	}

	public Equipments getEquipmentsType() {
		return equipmentsType;
	}

	public void setEquipmentsType(Equipments equipmentsType) {
		this.equipmentsType = equipmentsType;
	}

	public Nodes getEquipmentsData() {
		return equipmentsData;
	}

	public void setEquipmentsData(Nodes equipmentsData) {
		this.equipmentsData = equipmentsData;
	}

	public HashMap<SliceKeySet, L2Cps> getL2SliceData() {
		return l2SliceData;
	}

	public void setL2SliceData(HashMap<SliceKeySet, L2Cps> l2SliceData) {
		this.l2SliceData = l2SliceData;
	}

	public HashMap<SliceKeySet, L3Cps> getL3SliceData() {
		return l3SliceData;
	}

	public void setL3SliceData(HashMap<SliceKeySet, L3Cps> l3SliceData) {
		this.l3SliceData = l3SliceData;
	}

	public HashMap<Integer, InternalLinkIfs> getInternalLinkIfData() {
		return internalLinkIfData;
	}

	public void setInternalLinkIfData(
			HashMap<Integer, InternalLinkIfs> internalLinkIfData) {
		this.internalLinkIfData = internalLinkIfData;
	}

	@Override
	public String toString() {
		return "DeviceInformationSet [equipmentsType=" + equipmentsType
				+ ", equipmentsData=" + equipmentsData + ", l2SliceData="
				+ l2SliceData + ", l3SliceData=" + l3SliceData
				+ ", internalLinkIfData=" + internalLinkIfData + "]";
	}



}
