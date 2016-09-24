package com.ucas.busbook.network;

import com.ucas.busbook.Configs;
import com.ucas.busbook.Utils.Callback.FailCallback;
import com.ucas.busbook.Utils.Callback.SuccessCallback;

public class GetMessage {

	public GetMessage(String timestamp, final SuccessCallback successCallback,
			final FailCallback failCallback) {
		new NetConnection(Configs.SERVER_URL, HttpMethod.POST,
				new NetConnection.SuccessCallback() {

					@Override
					public void onSuccess(String result) {
//						try {
//							JSONObject jsonObject = new JSONObject(result);
//							switch (jsonObject.getInt(Configs.KEY_STATUS)) {
//							case Configs.RESULT_STATUS_SUCCESS:
//								JSONArray jsonArray = jsonObject.getJSONArray("message");
//								Configs.hasLastest = true;
//								successCallback.onSuccess(jsonArray);
//								break;
//							case Configs.RESULT_STATUS_NO_NEW_MESSAGE:
//								Configs.hasLastest = false;
//								break;
//
//							default:
//								break;
//							}
//						} catch (JSONException e) {
//							e.printStackTrace();
//						}
						successCallback.onSuccess(result);
					}
				}, new NetConnection.FailCallback() {

					@Override
					public void onFail(int errorCode) {
						failCallback.onFail(errorCode);
					}
				}, Configs.KEY_TIME_STAMP, timestamp);
	}
	
//	public static interface SuccessCallback{
//		public void onSuccess(JSONArray jsonArray);
//	}

}
