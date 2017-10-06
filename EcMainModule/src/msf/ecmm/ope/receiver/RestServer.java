
package msf.ecmm.ope.receiver;

import java.net.BindException;
import java.net.InetSocketAddress;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.CommonUtil;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.config.EcConfiguration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ThreadPool;

public class RestServer {

	private static final Logger logger = LogManager.getLogger(CommonDefinitions.EC_LOGGER);

	static Server jettyServer = null;

	public static synchronized boolean initialize() {

		logger.trace(CommonDefinitions.START);

		if (jettyServer != null) {
			logger.debug("Already started");
			logger.error(LogFormatter.out.format(LogFormatter.MSG_501034));
			return false;
		}

		EcConfiguration config = EcConfiguration.getInstance();

		InetSocketAddress inetSocketAddress = new InetSocketAddress(restIpaddr, restPort);
		jettyServer = new Server(inetSocketAddress);

		ThreadPool threadPool = jettyServer.getThreadPool();
