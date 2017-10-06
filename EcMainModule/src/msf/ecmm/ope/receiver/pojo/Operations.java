
package msf.ecmm.ope.receiver.pojo;

public class Operations extends AbstractRestMessage {

	private BulkCreateL2cp createL2cpsOption;

	private BulkCreateL3cp createL3cpsOption;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public BulkCreateL2cp getCreateL2cpsOption() {
		return createL2cpsOption;
	}

	public void setCreateL2cpsOption(BulkCreateL2cp createL2cpsOption) {
		this.createL2cpsOption = createL2cpsOption;
	}

	public BulkDeleteL2cp getDeleteL2cpsOption() {
		return deleteL2cpsOption;
	}

	public void setDeleteL2cpsOption(BulkDeleteL2cp deleteL2cpsOption) {
		this.deleteL2cpsOption = deleteL2cpsOption;
	}

	public BulkCreateL3cp getCreateL3cpsOption() {
		return createL3cpsOption;
	}

	public void setCreateL3cpsOption(BulkCreateL3cp createL3cpsOption) {
		this.createL3cpsOption = createL3cpsOption;
	}

	public BulkDeleteL3cp getDeleteL3cpsOption() {
		return deleteL3cpsOption;
	}

	public void setDeleteL3cpsOption(BulkDeleteL3cp deleteL3cpsOption) {
		this.deleteL3cpsOption = deleteL3cpsOption;
	}

	@Override
	public String toString() {
		return "Operations [action=" + action + ", createL2cpsOption=" + createL2cpsOption + ", deleteL2cpsOption="
				+ deleteL2cpsOption + ", createL3cpsOption=" + createL3cpsOption + ", deleteL3cpsOption="
				+ deleteL3cpsOption + "]";
	}

}
