
package msf.ecmm.ope.receiver.pojo;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.parts.UpdateOption;

public class UpdateL3cp extends AbstractRestMessage {

	public UpdateOption getUpdateOption() {
		return updateOption;
	}

	public void setUpdateOption(UpdateOption updateOption) {
		this.updateOption = updateOption;
	}

	@Override
	public String toString() {
		return "UpdateL3cp [updateOption=" + updateOption + "]";
	}

	public void check(OperationType ope) throws CheckDataException {
		if (updateOption == null) {
			throw new CheckDataException();
		} else {
			updateOption.check(ope);
		}
	}

}
