
package msf.ecmm.db;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DBAccessException extends Exception {

	private final Logger logger = LogManager.getLogger(CommonDefinitions.EC_LOGGER);

	public static final int DOUBLE_REGISTRATION = 1;
	public static final int NO_DELETE_TARGET = 3;
	public static final int INSERT_FAILURE = 5;
	public static final int DELETE_FAILURE = 7;
	public static final int SYSTEM_FAILURE = 9;

	public DBAccessException(int code, int errorMessage) {
		super();

		logger.error(LogFormatter.out.format(errorMessage));

		if (code == SYSTEM_FAILURE) {
			logger.fatal(LogFormatter.out.format(errorMessage));
		}

		this.code = code;
	}

	public DBAccessException(int code, int errorMessage, Throwable e) {
		super(e);

		logger.error(LogFormatter.out.format(errorMessage, e.getMessage()), e);

		this.code = code;

	}

	public int getCode() {
		return code;
	}

}
