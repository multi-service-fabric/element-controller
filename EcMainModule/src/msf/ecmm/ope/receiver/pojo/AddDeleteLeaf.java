
package msf.ecmm.ope.receiver.pojo;

public class AddDeleteLeaf extends AbstractRestMessage {

	private AddLeaf addNodeOption;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public AddLeaf getAddNodeOption() {
		return addNodeOption;
	}

	public void setAddNodeOption(AddLeaf addNodeOption) {
		this.addNodeOption = addNodeOption;
	}

	public DeleteLeaf getDelNodeOption() {
		return delNodeOption;
	}

	public void setDelNodeOption(DeleteLeaf delNodeOption) {
		this.delNodeOption = delNodeOption;
	}

	@Override
	public String toString() {
		return "AddDeleteLeaf [action=" + action + ", addNodeOption=" + addNodeOption + ", delNodeOption="
				+ delNodeOption + "]";
	}

}
