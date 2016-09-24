package com.ucas.busbook.Utils;

public class Callback {

	public interface SuccessCallback {
		public void onSuccess(String ... result);

		//public void onSuccess(String result);
	}

	public interface FailCallback {
		public void onFail(int errorCode);
	}
}
