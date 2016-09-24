package com.ucas.busbook.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.http.NoHttpResponseException;

import com.ucas.busbook.Configs;
import com.ucas.busbook.bean.Order;

import android.os.AsyncTask;

public class CancelOrder {
	public static AsyncTask<Void, Void, String> mytask;

	public CancelOrder(final String url, final HttpMethod method, final SuccessCallback successCallback,
			final FailCallback failCallback, final List<Order> list) {

		mytask = new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {

				StringBuffer paramsStr = new StringBuffer();
				ObjectOutputStream oos = null;
				BufferedReader br = null;
				URLConnection uc;
				try {
					switch (method) {
					case POST:
						uc = new URL(url).openConnection();
						uc.setConnectTimeout(10000);
						uc.setDoOutput(true);//
						uc.setDoInput(true);
						uc.setRequestProperty("Content-type", "application/x-java-serialized-object");
						oos = new ObjectOutputStream(uc.getOutputStream());
						oos.writeObject(list);
						oos.flush();
						break;

					default:
						uc = new URL(url + "?" + paramsStr.toString()).openConnection();
						break;
					}

					br = new BufferedReader(new InputStreamReader(uc.getInputStream(), Configs.CHARSET));
					String line = null;
					StringBuffer result = new StringBuffer();
					while ((line = br.readLine()) != null) {
						result.append(line);
					}

					System.out.println(result);

					return result.toString();

				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (Exception e) {
					if (e instanceof UnknownHostException || e instanceof ConnectException) {
						if (failCallback != null)
							failCallback.onFail(Configs.ERROR_FAIL_TO_CONNECT_SEVER);
					} else if (e instanceof NoHttpResponseException) {
						if (failCallback != null)
							failCallback.onFail(Configs.ERROR_HTTP_EXCEPTION);
					} else if (e instanceof SocketTimeoutException) {
						if (failCallback != null)
							failCallback.onFail(Configs.ERROR_SOCKET_EXCEPTION);
					}
					e.printStackTrace();
				}

				finally {
					try {
						if (oos != null) {
							oos.close();
						}
						if (br != null) {
							br.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return null;
			}

			@Override
			protected void onPostExecute(String result) {

				if (result != null) {
					if (successCallback != null) {
						successCallback.onSuccess(result);
					}
				}
				super.onPostExecute(result);
			}

		}.execute();
	}

	public static interface SuccessCallback {
		void onSuccess(String result);
	}

	public static interface FailCallback {
	    void onFail(int errorCode);
	}
}
