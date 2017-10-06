package msf.ecmm.ope.control;

public enum ECMainState {

	InService(100),
	Stop(0),
	private int value;

	private ECMainState(int n) {
		this.value = n;
	}

	public int getValue() {
		return this.value;
	}

	public static ECMainState getState(int num) {

		ECMainState statusStr = null;

		if (num == 10) {
			statusStr = StartReady;
		} else if (num == 100) {
			statusStr = InService;
		} else if (num == 90) {
			statusStr = StopReady;
		} else if (num == 0) {
			statusStr = Stop;
		} else if (num == 50) {
			statusStr = ChangeOver;
		}

		return statusStr;
	}

	public static String ecMainStateToLabel(ECMainState status) {

		String statusStr = "";

		if (status.name().equals(ECMainState.StartReady.name())) {
			statusStr = "startready";
		} else if (status.name().equals(ECMainState.InService.name())) {
			statusStr = "inservice";
		} else if (status.name().equals(ECMainState.StopReady.name())) {
			statusStr = "stopready";
		} else if (status.name().equals(ECMainState.Stop.name())) {
			statusStr = "stop";
		} else if (status.name().equals(ECMainState.ChangeOver.name())) {
			statusStr = "changeover";
		}

		return statusStr;
	}
}
