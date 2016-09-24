package com.ucas.busbook.network;

import org.json.JSONException;
import org.json.JSONObject;

import com.ucas.busbook.Configs;
import com.ucas.busbook.Utils.Callback.FailCallback;
import com.ucas.busbook.Utils.Callback.SuccessCallback;

public class QueryRestTicket {

	public QueryRestTicket(String routeName, String date,
			final SuccessCallback successCallback,
			final FailCallback failCallback) {
		new NetConnection(
				Configs.SERVER_URL + "/QueryAction",
				HttpMethod.POST,
				new NetConnection.SuccessCallback() {

					@Override
					public void onSuccess(String result) {
						try {
							JSONObject jsonObject = new JSONObject(result);
							switch (jsonObject.getInt(Configs.KEY_STATUS)) {
							case Configs.RESULT_STATUS_SUCCESS:
								successCallback.onSuccess(jsonObject
										.getString(Configs.KEY_TICKET_NUM));
								break;
							case Configs.RESULT_STATUS_INVALID_TIME:
								failCallback.onFail(Configs.RESULT_STATUS_INVALID_TIME);
								break;

							default:
								failCallback.onFail(Configs.RESULT_STATUS_INVALID_DATE);
								break;
							}

						} catch (JSONException e) {
							e.printStackTrace();
							failCallback.onFail(Configs.ERROR_JSON_EXCEPTION);
						}

					}
				}, new NetConnection.FailCallback() {

					@Override
					public void onFail(int errorCode) {
						failCallback.onFail(errorCode);
					}
				}, Configs.KEY_ACTION, Configs.ACTION_QUERY_REST_TICKET,
				Configs.KEY_ROUTE_NAME, routeName, Configs.KEY_QUERY_DATE, date);
	}

	// public static interface SuccessCallback {
	// public void onSuccess(String result);
	// }
}
