package com.ucas.busbook.network;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.ucas.busbook.Configs;
import com.ucas.busbook.Utils.Callback.FailCallback;
import com.ucas.busbook.Utils.Callback.SuccessCallback;
import com.ucas.busbook.Utils.DateTimeUtils;

public class GetSeverConfig {

	public GetSeverConfig(String time, final SuccessCallback successCallback,
			final FailCallback failCallback) {
		new NetConnection(Configs.SERVER_URL + "/GetConfigs", HttpMethod.POST,
				new NetConnection.SuccessCallback() {

					@Override
					public void onSuccess(String result) {
						try {
							JSONObject jsonObject = new JSONObject(result);
							switch (jsonObject.getInt(Configs.KEY_STATUS)) {
							case Configs.RESULT_STATUS_SUCCESS:
								successCallback.onSuccess(
										jsonObject.getString("datediff"),
										jsonObject.getString("severdate"),
										jsonObject.getString("lastest"));
								break;
							default:
								successCallback.onSuccess(
										jsonObject.getString("datediff"),
										jsonObject.getString("severdate"));
								break;
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new NetConnection.FailCallback() {

					@Override
					public void onFail(int errorCode) {
						failCallback.onFail(errorCode);
					}
				}, Configs.KEY_ACTION, Configs.KEY_GET_SEVER_CONFIG,
				Configs.KEY_TIME_STAMP, time);
	}

	public static void getSeverConfig(final Context context) {
		String timestamp = Configs.getCachedMessageTime(context);
		new GetSeverConfig(timestamp, new SuccessCallback() {

			@Override
			public void onSuccess(String... result) {
				Configs.cacheDateDiff(context, Integer.parseInt(result[0]));
				Configs.cacheSeverDate(context, result[1]);
				if (result.length>2) {
					Configs.cacheMessageTime(context, result[2]);
					Configs.cacheShowNotice(context, true);
					Configs.hasLastest = true;
				}
			}
		}, new FailCallback() {

			@Override
			public void onFail(int errorCode) {
				Configs.cacheDateDiff(context, Configs.dateDiff);
				Configs.cacheSeverDate(context,
						DateTimeUtils.YearDate2String(new Date()));
				Configs.hasLastest = false;
			}

		});
	}

}
