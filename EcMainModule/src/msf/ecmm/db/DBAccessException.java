/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;

/**
 * DB Exception Class.
 */
public class DBAccessException extends Exception {

  /** Serial Version UID. */
  private static final long serialVersionUID = 1L;

  /** Log Output Instance. */
  private final Logger logger = LogManager.getLogger(CommonDefinitions.EC_LOGGER);

  /** Internal Error Code. */
  private int code;

  /**
   * BD Internal Error Code.
   */
  /** Common error. */
  public static final int DB_COMMON_ERROR = 0;
  /** Registered. */
  public static final int DOUBLE_REGISTRATION = 1;
  /** No update target. */
  public static final int NO_UPDATE_TARGET = 2;
  /** No deletion target. */
  public static final int NO_DELETE_TARGET = 3;
  /** DB search failed. */
  public static final int SEARCH_FAILURE = 4;
  /** DB registration failed. */
  public static final int INSERT_FAILURE = 5;
  /** DB update failed. */
  public static final int UPDATE_FAILURE = 6;
  /** DB deletion failed. */
  public static final int DELETE_FAILURE = 7;
  /** DB deletion failed (relation invalid). */
  public static final int RELATIONSHIP_UNCONFORMITY = 8;
  /** System error. */
  public static final int SYSTEM_FAILURE = 9;
  /** Commitment error. */
  public static final int COMMIT_FAILURE = 10;

  /**
   * Data Base Exception.
   *
   * @param code
   *          internal error code
   * @param errorMessage
   *          message numberO
   */

  public DBAccessException(int code, int errorMessage) {
    super();

    logger.error(LogFormatter.out.format(errorMessage));

    if (code == SYSTEM_FAILURE) {
      logger.fatal(LogFormatter.out.format(errorMessage));
    }

    this.code = code;
  }

  /**
   * Data Base Exception.
   *
   * @param code
   *          internal error code
   * @param errorMessage
   *          message number
   * @param e1
   *          Exeception information
   */
  public DBAccessException(int code, int errorMessage, Throwable e1) {
    super(e1);

    logger.error(LogFormatter.out.format(errorMessage, e1.getMessage()), e1);

    this.code = code;

  }

  /**
   * Internal Error Code Acquisition.
   *
   * @return internal error code
   */
  public int getCode() {
    return code;
  }

}
