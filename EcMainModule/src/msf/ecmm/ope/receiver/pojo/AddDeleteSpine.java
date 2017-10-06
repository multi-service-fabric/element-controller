
package msf.ecmm.ope.receiver.pojo;

public class AddDeleteSpine extends AbstractRestMessage {

	private AddSpine addNodeOption;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public AddSpine getAddNodeOption() {
		return addNodeOption;
	}

	public void setAddNodeOption(AddSpine addNodeOption) {
		this.addNodeOption = addNodeOption;
	}

	public DeleteSpine getDelNodeOption() {
		return delNodeOption;
	}

	public void setDelNodeOption(DeleteSpine delNodeOption) {
		this.delNodeOption = delNodeOption;
	}

	@Override
	public String toString() {
		return "AddDeleteSpine [action=" + action + ", addNodeOption=" + addNodeOption + ", delNodeOption="
				+ delNodeOption + "]";
	}

}
