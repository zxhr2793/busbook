package com.ucas.busbook.network;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ucas.busbook.Configs;
import com.ucas.busbook.Utils.Callback.FailCallback;
import com.ucas.busbook.bean.Order;

public class OrderList {
	Order order;

	public OrderList(String student_id, String action, String token,
			final SuccessCallback successCallback,
			final FailCallback failCallback) {
		new NetConnection(
				Configs.SERVER_URL + "/OrderAction",
				HttpMethod.POST,
				new NetConnection.SuccessCallback() {

					@Override
					public void onSuccess(String result) {
						try {
							JSONObject jsonObject = new JSONObject(result);
							switch (jsonObject.getInt(Configs.KEY_STATUS)) {
							case Configs.RESULT_STATUS_SUCCESS: {
								order = new Order();
								JSONArray jsonArray = jsonObject
										.getJSONArray(Configs.KEY_ORDER_RESULT);
								System.out.println(jsonArray);
								List<Order> orderInfo = order
										.getOrderList(jsonArray);
								successCallback.onSuccess(orderInfo);
							}
								break;
							case Configs.RESULT_STATUS_NO_ORDER: {
								if (failCallback != null) {
									failCallback
											.onFail(Configs.RESULT_STATUS_NO_ORDER);

								}
							}
								break;

							default:
								break;
							}

						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}, new NetConnection.FailCallback() {

					@Override
					public void onFail(int errorCode) {

					}
				}, Configs.KEY_ACTION, action, Configs.KEY_STUDENT_ID,
				student_id, Configs.KEY_TOKEN, token);

	}

	public static interface SuccessCallback {
		void onSuccess(List<Order> orderInfo);
	}

}
