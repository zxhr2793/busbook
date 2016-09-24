package com.ucas.busbook.network;

import org.json.JSONException;
import org.json.JSONObject;

import com.ucas.busbook.Configs;
import com.ucas.busbook.Utils.Callback.FailCallback;
import com.ucas.busbook.Utils.Callback.SuccessCallback;

public class ChangePhone {

	public ChangePhone(String studentId, String token, String phoneNum,
			final SuccessCallback successCallback,
			final FailCallback failCallback) {
		new NetConnection(
				Configs.SERVER_URL + "/UserAction",
				HttpMethod.POST,
				new NetConnection.SuccessCallback() {

					@Override
					public void onSuccess(String result) {
						try {
							JSONObject jsonObject = new JSONObject(result);
							switch (jsonObject.getInt(Configs.KEY_STATUS)) {
							case Configs.RESULT_STATUS_SUCCESS:
								if (successCallback != null) {
									successCallback.onSuccess();
									System.out.println(jsonObject);
								}
								break;
							default:
								if (failCallback != null) {
									failCallback
											.onFail(Configs.RESULT_STATUS_FAIL);
								}
								break;
							}
						} catch (JSONException e) {
							e.printStackTrace();

							if (failCallback != null) {
								failCallback
										.onFail(Configs.ERROR_JSON_EXCEPTION);
							}
						}
					}
				}, new NetConnection.FailCallback() {

					@Override
					public void onFail(int errorCode) {
						failCallback.onFail(errorCode);
					}
				}, Configs.KEY_ACTION, Configs.ACTION_CHANGE_PHONE,
				Configs.KEY_STUDENT_ID, studentId, Configs.KEY_TOKEN, token,
				Configs.KEY_PHONE_NUM, phoneNum);
	}

//	public static interface SuccessCallback {
//		public void onSuccess();
//	}

//	public static interface FailCallback {
//		public void onFail(int errorCode);
//	}

}
