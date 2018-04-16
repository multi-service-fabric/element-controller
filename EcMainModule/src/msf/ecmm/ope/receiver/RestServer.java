/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver;

import java.net.BindException;
import java.net.InetSocketAddress;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ThreadPool;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.CommonUtil;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.config.EcConfiguration;

/**
 * REST Server Start-up Class
 */
public class RestServer {

  /**
   * Logger
   */
  private static final Logger logger = LogManager.getLogger(CommonDefinitions.EC_LOGGER);

  /**
   * Server Instance
   */
  static Server jettyServer = null;

  /**
   * Starting up Operation Request Reception Function
   *
   * @return start-up result
   */
  public static synchronized boolean initialize() {

    logger.trace(CommonDefinitions.START);

    if (jettyServer != null) {
      logger.debug("Already started");
      logger.error(LogFormatter.out.format(LogFormatter.MSG_501034));
      return false;
    }

    EcConfiguration config = EcConfiguration.getInstance();
    String restIpaddr = config.get(String.class, EcConfiguration.REST_SERVER_ADDRESS); 
    int restPort = config.get(Integer.class, EcConfiguration.REST_SERVER_PORT); 
    int restThreadMax = config.get(Integer.class, EcConfiguration.REST_THREAD_MAX); 
    String requestLogPath = config.get(String.class, EcConfiguration.REQUEST_LOG_PATH); 
    int requestLogKeep = config.get(Integer.class, EcConfiguration.REQUEST_KEEP_PERIOD); 
    int retryNumMax = config.get(Integer.class, EcConfiguration.REST_SERVER_BIND_NG_RETRY_NUM); 
    int retryWait = 1000; 

    InetSocketAddress inetSocketAddress = new InetSocketAddress(restIpaddr, restPort);
    jettyServer = new Server(inetSocketAddress);

    ThreadPool threadPool = jettyServer.getThreadPool();
    int executeThreadNumMax = restThreadMax + 3;
    ((QueuedThreadPool) threadPool).setMaxThreads(executeThreadNumMax);

    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath(ReceiverDefinitions.CONTEXT_PATH); 
    jettyServer.setHandler(context);
    ServletHolder jerseyServlet = context.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, "/*");
    jerseyServlet.setInitOrder(0);
    jerseyServlet.setInitParameter("jersey.config.server.provider.packages", "msf.ecmm.ope.receiver.resources");

    NCSARequestLog requestLog = new NCSARequestLog(requestLogPath + "/jetty-yyyy_mm_dd.request.log");
    requestLog.setRetainDays(requestLogKeep);
    requestLog.setAppend(true);
    requestLog.setExtended(true);
    requestLog.setLogTimeZone("JST");
    requestLog.setLogLatency(true);
    jettyServer.setRequestLog(requestLog);

    boolean result = false;
    Throwable printE = null;
    for (int retryNum = 0; retryNum <= retryNumMax; retryNum++) {
      try {
        jettyServer.start();
        result = true;
        break;
      } catch (BindException e) {
        logger.debug("Bind NG");
        if (retryNum != retryNumMax) {
          CommonUtil.sleep(retryWait);
        }
        printE = e;
        continue;
      } catch (Exception e) {
        printE = e;
        break;
      }
    }
    if (result == false) {
      logger.error(LogFormatter.out.format(LogFormatter.MSG_501034), printE);
    }

    logger.trace(CommonDefinitions.END);

    return result;
  }
}
