/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Common Utility.
 */
public class CommonUtil {

  /** logger. */
  private static final Logger logger = LogManager.getLogger(CommonDefinitions.EC_LOGGER);

  /**
   * load mitigation of loop or SpinLocks.
   */
  public static void sleep() {
    sleep(50);
  }

  /**
   * wait for specified time.
   *
   * @param mills
   *          waiting time (msec)
   */
  public static void sleep(long mills) {
    try {
      TimeUnit.MILLISECONDS.sleep(mills);
    } catch (InterruptedException exp) {
      logger.debug("Interrupt occured");
    }
  }

  /**
   * Deep Copy.
   *
   * @param obj
   *          copy source instance
   * @return copy instance
   * @throws IllegalArgumentException
   *           fail
   */
  public static Object deepCopy(Serializable obj) throws IllegalArgumentException {
    try {
      if (obj == null) {
        return null;
      }
      ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
      ObjectOutputStream out = new ObjectOutputStream(byteOut);
      out.writeObject(obj);
      ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
      ObjectInputStream in = new ObjectInputStream(byteIn);
      return in.readObject();
    } catch (Throwable exp) {
      logger.debug("Error occured", exp);
      throw new IllegalArgumentException();
    }
  }
}
