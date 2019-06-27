/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.common.log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.config.EcConfiguration;

/**
 * Logger exclusively for the MSF.
 */
public class MsfLogger {
  /** Logger. */
  private static final Logger logger = LogManager.getLogger(CommonDefinitions.EC_LOGGER);

  /** true: to be notified */
  public static final String NOTICE = "true";
  /** false. not to be notified*/
  public static final String NOT_NOTICE = "false";

  /** Log level is info. */
  public static final String LOG_LV_INFO = "INFO";
  /** Log level is warn. */
  public static final String LOG_LV_WARN = "WARNING";
  /** Log level is error. */
  public static final String LOG_LV_ERROR = "ERROR";

  /** Log level is fatal. */
  public static final String LOG_LV_FATAL_LOGGER = "FATAL";
  /** Log level is trace. */
  public static final String LOG_LV_TRACE_LOGGER = "TRACE";
  /** Log level is debug. */
  public static final String LOG_LV_DEBUG_LOGGER = "DEBUG";
  /** Log level is info. */
  public static final String LOG_LV_INFO_LOGGER = "INFO ";
  /** Log level is warn. */
  public static final String LOG_LV_WARN_LOGGER = "WARN ";
  /** Log level is error. */
  public static final String LOG_LV_ERROR_LOGGER = "ERROR";

  /** Log output flag. **/
  private boolean logOutput = CommonDefinitions.OUTPUT_LOG;

  private static final String CLASS_NAME = "msf.ecmm.common.log.MsfLogger";

  private static final String NOT_AVAIL = "?";

  private static final String FORMAT = "[%s] [%s] [tid=%s] (%s::%s():%s):%s";

  /**
   * MsfLogger constructor.
   *
   */
  public MsfLogger() {

  }

  /**
   * MsfLogger constructor.
   *
   * @param logOutput
   *          Flag of stopping notification.
   */
  public MsfLogger(boolean logOutput) {
    this.logOutput = logOutput;
  }

  /**
   * Log level is trace.
   *
   * @param paramString
   *          detailed message
   */
  public void trace(String paramString) {
    if (logOutput == CommonDefinitions.OUTPUT_LOG) {
      StackTraceElement element = getTraceElement(Thread.currentThread().getStackTrace());
      logger.trace(createLogString(element, paramString, LOG_LV_TRACE_LOGGER));
    }
  }

  /**
   * Log level = trace.
   *
   * @param paramString
   *          detailed message
   * @param paramThrowable
   *          cause
   */
  public void trace(String paramString, Throwable paramThrowable) {
    if (logOutput == CommonDefinitions.OUTPUT_LOG) {
      StackTraceElement element = getTraceElement(Thread.currentThread().getStackTrace());
      logger.trace(createLogString(element, paramString, LOG_LV_TRACE_LOGGER), paramThrowable);
    }
  }

  /**
   * Log level =debug.
   *
   * @param paramString
   *          detailed message
   */
  public void debug(String paramString) {
    if (logOutput == CommonDefinitions.OUTPUT_LOG) {
      StackTraceElement element = getTraceElement(Thread.currentThread().getStackTrace());
      logger.debug(createLogString(element, paramString, LOG_LV_DEBUG_LOGGER));
    }
  }

  /**
   * Log level = debug.
   *
   * @param paramObject
   *          detailed message
   */
  public void debug(Object paramObject) {
    if (logOutput == CommonDefinitions.OUTPUT_LOG) {
      StackTraceElement element = getTraceElement(Thread.currentThread().getStackTrace());
      if (paramObject != null) {
        logger.debug(createLogString(element, paramObject.toString(), LOG_LV_DEBUG_LOGGER));
      } else {
        logger.debug(createLogString(element, null, LOG_LV_DEBUG_LOGGER));
      }
    }
  }

  /**
   * Log level = debug.
   *
   * @param paramThrowable
   *          Cause
   */
  public void debug(Throwable paramThrowable) {
    if (logOutput == CommonDefinitions.OUTPUT_LOG) {
      StackTraceElement element = getTraceElement(Thread.currentThread().getStackTrace());
      logger.debug(createLogString(element, null, LOG_LV_DEBUG_LOGGER), paramThrowable);
    }
  }

  /**
   * Log level = debug.
   *
   * @param paramString
   *          detailed message
   * @param paramObject
   *          The detailed informtion
   */
  public void debug(String paramString, Object paramObject) {
    if (logOutput == CommonDefinitions.OUTPUT_LOG) {
      StackTraceElement element = getTraceElement(Thread.currentThread().getStackTrace());
      logger.debug(createLogString(element, String.format(paramString, paramObject), LOG_LV_DEBUG_LOGGER));
    }
  }

  /**
   * Log level = debug.
   *
   * @param paramString
   *          detailed message
   * @param paramThrowable
   *          Cause
   */
  public void debug(String paramString, Throwable paramThrowable) {
    if (logOutput == CommonDefinitions.OUTPUT_LOG) {
      StackTraceElement element = getTraceElement(Thread.currentThread().getStackTrace());
      logger.debug(createLogString(element, paramString, LOG_LV_DEBUG_LOGGER), paramThrowable);
    }
  }

  /**
   * Log level = debug.
   *
   * @param paramString
   *          detailed message
   * @param param1
   *          The detailed informtion
   * @param param2
   *          The detailed informtion
   */
  public void debug(String paramString, Object param1, Object param2) {
    if (logOutput == CommonDefinitions.OUTPUT_LOG) {
      StackTraceElement element = getTraceElement(Thread.currentThread().getStackTrace());
      logger.debug(createLogString(element, String.format(paramString, param1, param2), LOG_LV_DEBUG_LOGGER));
    }
  }

  /**
   * Log level = info.
   *
   * @param paramString
   *          detailed message
   */
  public void info(String paramString) {
    String noticeInfo = EcConfiguration.getInstance().getFromMsfLogger(String.class,
        EcConfiguration.NOTICE_LOG_LEVEL_INFO);
    if (logOutput == CommonDefinitions.OUTPUT_LOG) {
      StackTraceElement element = getTraceElement(Thread.currentThread().getStackTrace());
      String logMessage = createLogString(element, paramString, LOG_LV_INFO_LOGGER);
      logger.info(logMessage);
      if (noticeInfo.equals(NOTICE)) {
        if (noticeInfo.equals(NOTICE)) {
          LogNotice logNotice = new LogNotice();
          logNotice.setLogLv(LOG_LV_INFO);
          logNotice.setLogMessage(logMessage);
          logNotice.start();
        }
      }
    }
  }

  /**
   * Log level = info.
   *
   * @param paramString
   *          detailed message
   * @param paramThrowable
   *          Cause
   */
  public void info(String paramString, Throwable paramThrowable) {
    String noticeInfo = EcConfiguration.getInstance().getFromMsfLogger(String.class,
        EcConfiguration.NOTICE_LOG_LEVEL_INFO);
    if (logOutput == CommonDefinitions.OUTPUT_LOG) {
      StackTraceElement element = getTraceElement(Thread.currentThread().getStackTrace());
      String logMessage = createLogString(element, paramString, LOG_LV_INFO_LOGGER);
      logger.info(logMessage, paramThrowable);
      if (noticeInfo.equals(NOTICE)) {
        StringBuilder buff = new StringBuilder();
        buff.append(logMessage);
        buff.append(paramThrowable.getMessage());
        for (StackTraceElement trace : paramThrowable.getStackTrace()) {
          buff.append(trace.toString());
        }
        LogNotice logNotice = new LogNotice();
        logNotice.setLogLv(LOG_LV_INFO);
        logNotice.setLogMessage(buff.toString());
        logNotice.start();
      }
    }
  }

  /**
   * Log level =warn.
   *
   * @param paramString
   *          detailed message
   */
  public void warn(String paramString) {
    String noticeWarn = EcConfiguration.getInstance().getFromMsfLogger(String.class,
        EcConfiguration.NOTICE_LOG_LEVEL_WARN);
    if (logOutput == CommonDefinitions.OUTPUT_LOG) {
      StackTraceElement element = getTraceElement(Thread.currentThread().getStackTrace());
      String logMessage = createLogString(element, paramString, LOG_LV_WARN_LOGGER);
      logger.warn(logMessage);
      if (noticeWarn.equals(NOTICE)) {
        LogNotice logNotice = new LogNotice();
        logNotice.setLogLv(LOG_LV_WARN);
        logNotice.setLogMessage(logMessage);
        logNotice.start();
      }
    }
  }

  /**
   * Log level = warn.
   *
   * @param paramString1
   *          detailed message
   * @param param0
   *          The detailed informtion
   * @param param1
   *          The detailed informtion
   */
  public void warn(String paramString1, Object param0, Object param1) {
    String noticeWarn = EcConfiguration.getInstance().getFromMsfLogger(String.class,
        EcConfiguration.NOTICE_LOG_LEVEL_WARN);
    if (logOutput == CommonDefinitions.OUTPUT_LOG) {
      StackTraceElement element = getTraceElement(Thread.currentThread().getStackTrace());
      String logMessage = createLogString(element, String.format(paramString1, param0, param1), LOG_LV_WARN_LOGGER);
      logger.warn(logMessage);
      if (noticeWarn.equals(NOTICE)) {
        LogNotice logNotice = new LogNotice();
        logNotice.setLogLv(LOG_LV_WARN);
        logNotice.setLogMessage(logMessage);
        logNotice.start();
      }
    }
  }

  /**
   * Log level = warn.
   *
   * @param paramString
   *          detailed message
   * @param param0
   *          The detailed informtion
   */
  public void warn(String paramString, Object param0) {
    String noticeWarn = EcConfiguration.getInstance().getFromMsfLogger(String.class,
        EcConfiguration.NOTICE_LOG_LEVEL_WARN);
    if (logOutput == CommonDefinitions.OUTPUT_LOG) {
      StackTraceElement element = getTraceElement(Thread.currentThread().getStackTrace());
      String logMessage = createLogString(element, String.format(paramString, param0), LOG_LV_WARN_LOGGER);
      logger.warn(logMessage);
      if (noticeWarn.equals(NOTICE)) {
        LogNotice logNotice = new LogNotice();
        logNotice.setLogLv(LOG_LV_WARN);
        logNotice.setLogMessage(logMessage);
        logNotice.start();
      }
    }
  }

  /**
   * Log level = warn.
   *
   * @param paramString
   *          detailed message
   * @param paramThrowable
   *          Cause
   */
  public void warn(String paramString, Throwable paramThrowable) {
    String noticeWarn = EcConfiguration.getInstance().getFromMsfLogger(String.class,
        EcConfiguration.NOTICE_LOG_LEVEL_WARN);
    if (logOutput == CommonDefinitions.OUTPUT_LOG) {
      StackTraceElement element = getTraceElement(Thread.currentThread().getStackTrace());
      String logMessage = createLogString(element, paramString, LOG_LV_WARN_LOGGER);
      logger.warn(logMessage, paramThrowable);
      if (noticeWarn.equals(NOTICE)) {
        StringBuilder buff = new StringBuilder();
        buff.append(logMessage);
        buff.append(paramThrowable.getMessage());
        for (StackTraceElement trace : paramThrowable.getStackTrace()) {
          buff.append(trace.toString());
        }
        LogNotice logNotice = new LogNotice();
        logNotice.setLogLv(LOG_LV_WARN);
        logNotice.setLogMessage(buff.toString());
        logNotice.start();
      }
    }
  }

  /**
   * Log level = error.
   *
   * @param paramString
   *          detailed message
   */
  public void error(String paramString) {
    String noticeError = EcConfiguration.getInstance().getFromMsfLogger(String.class,
        EcConfiguration.NOTICE_LOG_LEVEL_ERROR);
    if (logOutput == CommonDefinitions.OUTPUT_LOG) {
      StackTraceElement element = getTraceElement(Thread.currentThread().getStackTrace());
      String logMessage = createLogString(element, paramString, LOG_LV_ERROR_LOGGER);
      logger.error(logMessage);
      if (noticeError.equals(NOTICE)) {
        LogNotice logNotice = new LogNotice();
        logNotice.setLogLv(LOG_LV_ERROR);
        logNotice.setLogMessage(logMessage);
        logNotice.start();
      }
    }
  }

  /**
   * Log level = error.
   *
   * @param paramString
   *          detailed message
   * @param paramThrowable
   *          Cause
   */
  public void error(String paramString, Throwable paramThrowable) {
    String noticeError = EcConfiguration.getInstance().getFromMsfLogger(String.class,
        EcConfiguration.NOTICE_LOG_LEVEL_ERROR);
    if (logOutput == CommonDefinitions.OUTPUT_LOG) {
      StackTraceElement element = getTraceElement(Thread.currentThread().getStackTrace());
      String logMessage = createLogString(element, paramString, LOG_LV_ERROR_LOGGER);
      logger.error(logMessage, paramThrowable);
      if (noticeError.equals(NOTICE)) {
        StringBuilder buff = new StringBuilder();
        buff.append(logMessage);
        buff.append(paramThrowable.getMessage());
        for (StackTraceElement trace : paramThrowable.getStackTrace()) {
          buff.append(trace.toString());
        }
        LogNotice logNotice = new LogNotice();
        logNotice.setLogLv(LOG_LV_ERROR);
        logNotice.setLogMessage(buff.toString());
        logNotice.start();
      }
    }
  }

  /**
   * Log level = fatal.
   *
   * @param paramString
   *          detailed message
   */
  public void fatal(String paramString) {
    StackTraceElement element = getTraceElement(Thread.currentThread().getStackTrace());
    logger.fatal(createLogString(element, paramString, LOG_LV_FATAL_LOGGER));
  }

  /**
   * Log level = error,the log is output but it is not decided 
   * whether the notication the notication is executed or not.
   *
   * @param paramString
   *          detailed message
   */
  public void simpleLogError(String paramString) {
    StackTraceElement element = getTraceElement(Thread.currentThread().getStackTrace());
    logger.error(createLogString(element, paramString, LOG_LV_ERROR_LOGGER));
  }

  /**
   * Log level = error, the log is output but it is not decided 
   * whether the notication the notication is executed or not.
   *
   * @param paramString
   *          detailed message
   * @param paramThrowable
   *          cause
   */
  public void simpleLogError(String paramString, Throwable paramThrowable) {
    StackTraceElement element = getTraceElement(Thread.currentThread().getStackTrace());
    logger.error(createLogString(element, paramString, LOG_LV_ERROR_LOGGER), paramThrowable);
  }

  /**
   * If the log level is info, the log is output but it is not decided
   *  whether the notication the notication is executed or not.
   *
   * @param paramString
   *          detailed message
   */
  public void simpleLogInfo(String paramString) {
    StackTraceElement element = getTraceElement(Thread.currentThread().getStackTrace());
    logger.info(createLogString(element, paramString, LOG_LV_INFO_LOGGER));
  }

  /**
   * If the log level is trace, the log is output but it is not decided
   * whether the notication the notication is executed or not.
   *
   * @param paramString
   *          detailed message
   */
  public void simpleLogTrace(String paramString) {
    StackTraceElement element = getTraceElement(Thread.currentThread().getStackTrace());
    logger.trace(createLogString(element, paramString, LOG_LV_TRACE_LOGGER));
  }

  /**
   * If the log level is debug, the log is output but it is not decided
   * whether the notication the notication is executed or not.
   *
   * @param paramString
   *          detailed message
   */
  public void simpleLogDebug(String paramString) {
    StackTraceElement element = getTraceElement(Thread.currentThread().getStackTrace());
    logger.debug(createLogString(element, paramString, LOG_LV_DEBUG_LOGGER));
  }

  private StackTraceElement getTraceElement(StackTraceElement[] elements) {
    boolean next = false;
    for (final StackTraceElement eleme : elements) {
      final String className = eleme.getClassName();
      if (next && !CLASS_NAME.equals(className)) {
        return eleme;
      }
      if (CLASS_NAME.equals(className)) {
        next = true;
      } else if (NOT_AVAIL.equals(className)) {
        break;
      }
    }
    return null;
  }

  private String createLogString(StackTraceElement element, String message, String logLevel) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
    Calendar cl = Calendar.getInstance();
    return String.format(FORMAT, dateFormat.format(cl.getTime()), logLevel, Thread.currentThread().getName(),
        element.getClassName().substring(element.getClassName().lastIndexOf('.') + 1), element.getMethodName(),
        element.getLineNumber(), message);
  }

}
