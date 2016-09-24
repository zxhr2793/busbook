package com.ucas.busbook.Activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.ucas.busbook.Configs;
import com.ucas.busbook.R;
import com.ucas.busbook.Utils.ErrorUtils;
import com.ucas.busbook.bean.Update;
import com.ucas.busbook.dialog.APPDialog;
import com.ucas.busbook.dialog.CommonDialog;
import com.ucas.busbook.dialog.LoadingDialog;
import com.ucas.busbook.network.HttpMethod;
import com.ucas.busbook.network.NetConnection;
import com.ucas.busbook.service.DownloadService;
import com.ucas.busbook.widget.APPInfo;
import com.ucas.busbook.widget.APPToast;

public class UpdateManager {
	private Context context;
	private boolean showDialog;
	private Update mUpdate;
	private LoadingDialog loadingDialog;

	public UpdateManager(Context context, boolean showDialog) {
		this.context = context;
		this.showDialog = showDialog;
	}

	public void checkUpdate() {
		if (showDialog) {
			showCheckDialog();
		}
		checkAppUpdate();
	}

	public void checkAppUpdate() {
		new NetConnection(Configs.SERVER_URL + "/CheckUpdate", HttpMethod.POST,
				new NetConnection.SuccessCallback() {

					@Override
					public void onSuccess(String result) {
						try {
							JSONObject jsonObject = new JSONObject(result);
							switch (jsonObject.getInt(Configs.KEY_STATUS)) {
							case Configs.RESULT_STATUS_SUCCESS:
								dismissCheckDialog();
								JSONArray jsonArray = jsonObject
										.getJSONArray(Configs.KEY_UPDATE_INFO);
								Update update = new Update();
								mUpdate = update.parse(jsonArray);
								onFinishCheck();
								break;
							default:
								break;
							}
						} catch (JSONException e) {
							e.printStackTrace();

						}

					}
				}, new NetConnection.FailCallback() {

					@Override
					public void onFail(int errorCode) {
						dismissCheckDialog();
						ErrorUtils.networkOrActionError(errorCode, context);
					}
				}, Configs.KEY_ACTION, Configs.ACTION_CHECK_UPDATE);
	}

	public boolean hasNew() {
		if (this.mUpdate == null) {
			return false;
		}
		boolean hasNew = false;
		int versionCode = new APPInfo(context).getVersionCode();
		if (versionCode < mUpdate.getVersionCode()) {
			hasNew = true;
		}
		return hasNew;
	}

	public void onFinishCheck() {
		if (hasNew()) {
			showUpdateInfo();
		} else {
			APPToast.showToastLong(context, R.string.newest_version);
		}
	}

	private void showCheckDialog() {
		if (loadingDialog == null) {
			loadingDialog = APPDialog.showLoadingDialog(context, context
					.getResources().getString(R.string.checking_update));
		}
	}

	public void dismissCheckDialog() {
		if (loadingDialog != null) {
			loadingDialog.dismiss();
		}
	}

	public void showUpdateInfo() {
		if (mUpdate == null) {
			return;
		}
		final CommonDialog dialog = new CommonDialog(context,
				R.style.customDialog);
		dialog.show();
		dialog.setTitle(context.getResources().getString(
				R.string.new_version_title));
		dialog.setMessage(mUpdate.getUpdateLog());
		dialog.setPositiveButton(
				context.getResources().getString(R.string.update_now),
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						checkNetwork();
						dialog.dismiss();
					}
				});
		dialog.setNegativeButton(
				context.getResources().getString(R.string.cancel),
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
	}

	public void checkNetwork() {
		if (!NetConnection.isNetWorkConnected(context)) {
			APPDialog.showSetNetworkDialog(context);
		}
		if (NetConnection.getNetworkType(context) == NetConnection.NETTYPE_MOBILE) {
			showWarnningDialog();
		} else {
			System.out.println(mUpdate.getDownloadUrl());
			openDownLoadService(context, mUpdate.getDownloadUrl(),
					mUpdate.getAppName() + mUpdate.getVersionName());
		}
	}

	public void showWarnningDialog() {
		final CommonDialog dialog = new CommonDialog(context,
				R.style.customDialog);
		dialog.show();
		dialog.setMessage(context.getResources().getString(
				R.string.network_type_warnning));
		dialog.setPositiveButton(
				context.getResources().getString(R.string.continue_update),
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						openDownLoadService(context, mUpdate.getDownloadUrl(),
								mUpdate.getAppName() + mUpdate.getVersionName());
						dialog.dismiss();
					}
				});
		dialog.setNegativeButton(
				context.getResources().getString(R.string.cancel),
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
	}

	public static void openDownLoadService(final Context context,
			String downurl, String tilte) {
		Intent intent = new Intent(context, DownloadService.class);
		intent.putExtra(DownloadService.BUNDLE_KEY_DOWNLOAD_URL, downurl);
		intent.putExtra(DownloadService.BUNDLE_KEY_TITLE, tilte);
		context.startService(intent);
	}

}
