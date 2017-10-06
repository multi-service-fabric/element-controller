package msf.ecmm.common;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandExecutor {

	public static int exec(String[] params, List<String> stdList, List<String> errList) {

		logger.trace(CommonDefinitions.START);

		final int error = 1;

		if ((params == null) || (params.length == 0)) {
			return error;
		}
		for (int i = 0; i < params.length; i++) {
			logger.debug("args[" + params[i] + "]");
		}

		ProcessBuilder pb = new ProcessBuilder(params);

		int retVal = 0;
		Process process = null;
		InputStreamThread it = null;
		InputStreamThread et = null;

		String charCode = "Shift_JIS";
		try {

			process = pb.start();

			it = new InputStreamThread(process.getInputStream(), charCode);
			it.start();

			et = new InputStreamThread(process.getErrorStream(), charCode);
			et.start();

			process.waitFor();

			it.join();
			et.join();

			for (String s : it.getStringList()) {
				stdList.add(s);
			}
			for (String s : et.getStringList()) {
				errList.add(s);
			}

			retVal = process.exitValue();

		} catch (IOException ioex) {
			logger.error("IOException", ioex);
			errList.add(ioex.getLocalizedMessage());
			retVal = error;
		} catch (InterruptedException iex) {
			logger.error("InterruptedException", iex);
			errList.add(iex.getLocalizedMessage());
			retVal = error;
		}

		logger.debug("retVal=" + retVal + ", ret=" + stdList);
		return retVal;
	}

	public static boolean contain(List<String> strs, String key) {
		for (String str : strs) {
			if (str.matches(".*" + key + ".*"))
				return true;
		}
		return false;
	}

}
