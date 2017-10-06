
package msf.ecmm.ope.receiver.pojo;

import msf.ecmm.ope.execute.OperationType;

public class UpdatePhysicalInterface extends AbstractRestMessage {

	private String speed;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	@Override
	public String toString() {
		return "UpdatePhysicalInterface [action=" + action + ", speed=" + speed + "]";
	}


	public void check(OperationType ope) throws CheckDataException {
		if(action == null){
			throw new CheckDataException();
		}
		if((!action.equals("speed_set")) && (!action.equals("speed_delete"))){
			throw new CheckDataException();
		}
	}
}
