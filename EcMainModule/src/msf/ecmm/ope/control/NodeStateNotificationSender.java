/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.control;

/**
 * Device Start-up Notification Management Class Definition. Mange the device start-up notification which is done when EC starting-up.
 */
public class NodeStateNotificationSender extends Thread {

  @Override
  public void run() {
    OperationControlManager.getInstance().sendUnsentNodeStateNotification();

    OperationControlManager.getInstance().setNodeStateNotificationSenderHolder(null);
  }
}
