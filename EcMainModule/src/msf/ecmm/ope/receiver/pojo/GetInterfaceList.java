
package msf.ecmm.ope.receiver.pojo;

import msf.ecmm.ope.receiver.pojo.parts.IfSearchIf;

public class GetInterfaceList extends AbstractResponseMessage {

	public IfSearchIf getIfs() {
		return ifs;
	}

	public void setIfs(IfSearchIf ifs) {
		this.ifs = ifs;
	}

	@Override
	public String toString() {
		return "GetInterfaceList [ifs=" + ifs + "]";
	}

}
