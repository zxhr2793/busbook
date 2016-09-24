package com.ucas.busbook.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import org.apache.http.NoHttpResponseException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.ucas.busbook.Configs;

public class NetConnection {

	public static final int NETTYPE_WIFI = 0x01;
	public static final int NETTYPE_MOBILE = 0x02;
	public static AsyncTask<Void, Void, String> mytask;

	public NetConnection(final String url, final HttpMethod method,
			final SuccessCallback successCallback,
			final FailCallback failCallback, final String... kvs) {

		mytask = new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {

				StringBuffer paramsStr = new StringBuffer();
				BufferedWriter bw = null;
				BufferedReader br = null;
				URLConnection uc;
				for (int i = 0; i < kvs.length; i += 2) {
					paramsStr.append(kvs[i]).append("=").append(kvs[i + 1])
							.append("&");
				}
				try {
					switch (method) {
					case POST:
						uc = new URL(url).openConnection();
						uc.setConnectTimeout(10000);
						uc.setDoOutput(true);//
						bw = new BufferedWriter(new OutputStreamWriter(
								uc.getOutputStream(), Configs.CHARSET));
						bw.write(paramsStr.toString());
						bw.flush();
						break;

					default:
						uc = new URL(url + "?" + paramsStr.toString())
								.openConnection();
						break;
					}

					br = new BufferedReader(new InputStreamReader(
							uc.getInputStream(), Configs.CHARSET));
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
					if (e instanceof UnknownHostException
							|| e instanceof ConnectException) {
						if (failCallback != null)
							failCallback
									.onFail(Configs.ERROR_FAIL_TO_CONNECT_SEVER);
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
						if (bw != null) {
							bw.close();
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

	public static boolean isNetWorkConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}

	public static int getNetworkType(Context context) {
		int netType = 0;
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {

			netType = NETTYPE_MOBILE;

		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = NETTYPE_WIFI;
		}
		return netType;
	}
	
	public static boolean isWifiOpen(Context context) {
		boolean isWifiConnect = false;
		ConnectivityManager cm = (ConnectivityManager)
			context.getSystemService(Context.CONNECTIVITY_SERVICE);
		// check the networkInfos numbers
		NetworkInfo[] networkInfos = cm.getAllNetworkInfo();
		for (int i = 0; i < networkInfos.length; i++) {
		    if (networkInfos[i].getState() == NetworkInfo.State.CONNECTED) {
			if (networkInfos[i].getType() == ConnectivityManager.TYPE_MOBILE) {
			    isWifiConnect = false;
			}
			if (networkInfos[i].getType() == ConnectivityManager.TYPE_WIFI) {
			    isWifiConnect = true;
			}
		    }
		}
		return isWifiConnect;
	    }

}
