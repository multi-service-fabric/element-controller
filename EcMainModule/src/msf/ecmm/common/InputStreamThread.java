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

public class InputStreamThread extends Thread {

	private final Logger logger = LogManager.getLogger(CommonDefinitions.EC_LOGGER);

	private BufferedReader br;

	private List<String> list = new ArrayList<String>();

	public InputStreamThread(InputStream is, String charset) {
		logger.trace(CommonDefinitions.START);
		try {
			br = new BufferedReader(new InputStreamReader(is, charset));
		} catch (UnsupportedEncodingException ioex) {
			ioex.printStackTrace();
			throw new RuntimeException(ioex);
		}
	}

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

	public List<String> getStringList() {
		return list;
	}
}
