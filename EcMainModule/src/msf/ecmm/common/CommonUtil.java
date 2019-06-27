/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import msf.ecmm.common.log.MsfLogger;

/**
 * Common Utility 
 */
public class CommonUtil {

  /** logger. */
  private static final MsfLogger logger = new MsfLogger();

  /**
   * load mitigation of loop or SpinLocks.
   */
  public static void sleep() {
    sleep(50); // 50msec
  }

  /**
   * waiting for specified time.
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

  /**
   * Padding with zero. 
   * @param input target character string 
   * @param length the entire length 
   * @return target character string padded with zero 
   */
  public static String zeroPadding(String input, int length) {
    return String.format("%" + length + "s", input).replace(" ", "0");
  }

  /**
   * Checking file exestence.
   *
   * @param filename
   *          file to be checked
   * @return true：file exsist, false：file not exsist
   */
  public static boolean isFile(String filename) {
    boolean ret = false;
    File file = new File(filename);
    if (file.exists()) {
      if (!file.isDirectory()) {
        ret = true;
      }
    }
    return ret;
  }
}
