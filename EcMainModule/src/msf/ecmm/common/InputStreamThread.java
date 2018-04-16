/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * thread to read InputStream.
 */
public class InputStreamThread extends Thread {

  /**
   * logger.
   */
  private final Logger logger = LogManager.getLogger(CommonDefinitions.EC_LOGGER);

  /**
   * read buffer.
   */
  private BufferedReader br;

  /**
   * list for store read Stream.
   */
  private List<String> list = new ArrayList<String>();

  /**
   * constructor (to specify text code).
   *
   * @param is
   *          InputStream
   * @param charset
   *          text code set
   */
  public InputStreamThread(InputStream is, String charset) {
    logger.trace(CommonDefinitions.START);
    try {
      br = new BufferedReader(new InputStreamReader(is, charset));
    } catch (UnsupportedEncodingException ioex) {
      ioex.printStackTrace();
      throw new RuntimeException(ioex);
    }
  }

  /**
   * thread execution.
   */
  @Override
  public void run() {
    logger.trace(CommonDefinitions.START);
    try {

      for (;;) {

        String line = br.readLine();

        if (line == null) {
          break;
        }
        logger.trace(String.format("OUTPUT : %s", line));
        list.add(line);
      }
    } catch (IOException ioex) {
      logger.error("IOException", ioex);
      throw new RuntimeException(ioex);
    } finally {
      try {
        br.close();
      } catch (IOException ioex) {
        logger.error("IOException", ioex);
      }
    }
  }

  /**
   * acquiring the read string list.
   *
   * @return the string list which has been read
   */
  public List<String> getStringList() {
    return list;
  }
}
