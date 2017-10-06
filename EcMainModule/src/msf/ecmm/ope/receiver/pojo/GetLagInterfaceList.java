
package msf.ecmm.ope.receiver.pojo;

import java.util.ArrayList;

import msf.ecmm.ope.receiver.pojo.parts.LagIf;

public class GetLagInterfaceList extends AbstractResponseMessage {

	public ArrayList<LagIf> getLagIfs() {
		return lagIfs;
	}

	public void setLagIfs(ArrayList<LagIf> lagIfs) {
		this.lagIfs = lagIfs;
	}

	@Override
	public String toString() {
		return "GetLagInterfaceList [lagIfs=" + lagIfs + "]";
	}

}
