package com.ucas.busbook.network;

import org.json.JSONException;
import org.json.JSONObject;

import com.ucas.busbook.Configs;
import com.ucas.busbook.Utils.Callback.FailCallback;
import com.ucas.busbook.Utils.Callback.SuccessCallback;

public class BookTicket {

	public BookTicket(String token, String studentId, String routeName,
			String date, String phone, String name,
			final SuccessCallback successCallback,
			final FailCallback failCallback) {
		new NetConnection(Configs.SERVER_URL + "/BookAction", HttpMethod.POST,
				new NetConnection.SuccessCallback() {
					@Override
					public void onSuccess(String result) {
						try {
							JSONObject jsonObject = new JSONObject(result);
							switch (jsonObject.getInt(Configs.KEY_STATUS)) {
							case Configs.RESULT_STATUS_SUCCESS:
								successCallback.onSuccess();
								break;
							default:
								failCallback.onFail(jsonObject
										.getInt(Configs.KEY_STATUS));
								break;
							}
						} catch (JSONException e) {
							failCallback.onFail(Configs.ERROR_JSON_EXCEPTION);
							e.printStackTrace();
						}
					}
				}, new NetConnection.FailCallback() {

					@Override
					public void onFail(int errorCode) {
						failCallback.onFail(errorCode);
					}
				}, Configs.KEY_ACTION, Configs.ACTION_BOOK_TICKET,
				Configs.KEY_TOKEN, token, Configs.KEY_STUDENT_ID, studentId,
				Configs.KEY_ROUTE_NAME, routeName, Configs.KEY_TAKE_DATE, date,
				Configs.KEY_PHONE_NUM, phone, Configs.KEY_REAL_NAME, name);
	}
}
