package msf.ecmm.common;

import java.util.concurrent.TimeUnit;

public class CommonUtil {

	public static void sleep() {
	}

	public static void sleep(long mills) {
		try {
			TimeUnit.MILLISECONDS.sleep(mills);
		} catch (InterruptedException e) {
		}
	}
}
