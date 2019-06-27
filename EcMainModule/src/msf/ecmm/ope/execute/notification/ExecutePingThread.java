/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.notification;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.log.MsfLogger;
import msf.ecmm.devctrl.ExtendScriptController;
import msf.ecmm.devctrl.pojo.PingData;

/**
 * The definition of the class excuting ping between nodes<br>
 * The thread process for the ping between nodes is executed.
 */
public class ExecutePingThread extends Thread {

  /** Logger. */
  protected static final MsfLogger logger = new MsfLogger();

  /** IP address of the logged-in node. */
  private String loginIp;

  /** Password  for  the logged-in node. */
  private String pass;

  /** User name  for  the logged-in node. */
  private String user;

  /** The list of IP addresses for the ping execution.  */
  private String pingList;

  /** The name of sh executing the ping(The platform name (OS name)) . */
  private String pingShFile;

  /** The instance of the parent thread. */	
  private ExecutePing instance;

  /**
   * The thread is executed.
   */
  public void run() {

    logger.trace(CommonDefinitions.START);
    logger.debug("thread start. loginIp=" + loginIp);

    ExtendScriptController extendScriptController = new ExtendScriptController();
    PingData result = extendScriptController.executePing(loginIp, pingList, pass, user, pingShFile);
    instance.setResult(result);

    logger.debug("ExecutePingResult=" + result);
    logger.trace(CommonDefinitions.END);

  }

  /**
   * The IP address for the login the node is acquired.
   *
   * @return The IP address for the login to the node
   */
  public String getLoginIp() {
    return loginIp;
  }

  /**
   * The IP address for the login  the node is set.
   *
   * @param loginIp
   *          The IP address for the login to  the node
   */
  public void setLoginIp(String loginIp) {
    this.loginIp = loginIp;
  }

  /**
   * The login password for the node is acquired.
   *
   * @return The login password for the node
   */
  public String getPass() {
    return pass;
  }

  /**
   * The login password for the node is set.
   *
   * @param pass
   *          The login password for the node
   */
  public void setPass(String pass) {
    this.pass = pass;
  }

  /**
   * The login user name for the node is acquired.
   *
   * @return The login user name for the node
   */
  public String getUser() {
    return user;
  }

  /**
   * The login user name for the node is set.
   *
   * @param user
   *          The login user name for the node
   */
  public void setUser(String user) {
    this.user = user;
  }

  /**
   * The IP address list for the ping is acquired.
   *
   * @return The IP address list for the ping
   */
  public String getPingList() {
    return pingList;
  }

  /**
   * The IP address list for the ping is set.
   *
   * @param pingList
   *          The IP address list for the ping
   */
  public void setPingList(String pingList) {
    this.pingList = pingList;
  }

  /**
   * The sh script name for the ping is acquired.
   *
   * @return The sh script name for the ping
   */
  public String getPingShFile() {
    return pingShFile;
  }

  /**
   * The sh script name for the ping is set.
   *
   * @param pingShFile
   *          The sh script name for the ping
   */
  public void setPingShFile(String pingShFile) {
    this.pingShFile = pingShFile;
  }

  /**
   * The instance of the parent thread is acquired.
   *
   * @return The instance of the parent thread
   */
  public ExecutePing getInstance() {
    return instance;
  }

  /**
   * The instance of the parent thread is set.
   *
   * @param instance
   *          The instance of the parent thread
   */
  public void setInstance(ExecutePing instance) {
    this.instance = instance;
  }

}
