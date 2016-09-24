package com.ucas.busbook.network;

import org.json.JSONException;
import org.json.JSONObject;

import com.ucas.busbook.Configs;
import com.ucas.busbook.Utils.Callback.FailCallback;
import com.ucas.busbook.Utils.Callback.SuccessCallback;

public class Login {

	public Login(String student_id, String password,
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
									successCallback.onSuccess(
											jsonObject
													.getString(Configs.KEY_TOKEN),
											jsonObject
													.getString(Configs.KEY_REAL_NAME),
											jsonObject
													.getString(Configs.KEY_PHONE_NUM));
									System.out.println(jsonObject);
								}
								break;

							case Configs.RESULT_STATUS_INVALID_ID: {
								if (failCallback != null) {
									failCallback
											.onFail(Configs.RESULT_STATUS_INVALID_ID);
								}
							}
								break;
							case Configs.RESULT_STATUS_INVALID_PASSWORD: {
								if (failCallback != null) {
									failCallback
											.onFail(Configs.RESULT_STATUS_INVALID_PASSWORD);
								}
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
						if (failCallback != null) {
							failCallback.onFail(errorCode);
						}
					}
				}, Configs.KEY_ACTION, Configs.ACTION_LOGIN,
				Configs.KEY_STUDENT_ID, student_id,
				Configs.KEY_CURRENT_PASSWORD, password);

	}

	// public static interface SuccessCallback {
	// void onSuccess(String token, String realName, String phoneNum);
	// }

	// public static interface FailCallback {
	// void onFail(int errorCode);
	// }

}
