package msf.ecmm.ope.control;

public class NodeStateNotificationSender extends Thread {

	@Override
	public void run(){
		OperationControlManager.getInstance().sendUnsentNodeStateNotification();

		OperationControlManager.getInstance().setNodeStateNotificationSenderHolder(null);
	}
}
