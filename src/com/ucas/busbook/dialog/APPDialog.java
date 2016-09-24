package com.ucas.busbook.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;

import com.ucas.busbook.R;
import com.ucas.busbook.Activity.LoginActivity;
import com.ucas.busbook.Utils.NewIntent;

/**
 * 应用程序的dialog
 * 
 * @author qiankunt
 * 
 */
public class APPDialog {
	private static Context mContext;

	public APPDialog() {
	}

	public static void showExitDialog(Context context) {
		showDialog(context, R.string.dialog_exit_content,
				R.string.dialog_ok_btn, null, true, true);
	}

	public static LoadingDialog showLoadingDialog(Context context) {
		return showLoadingDialog(context, null);
	}

	public static LoadingDialog showLoadingDialog(Context context,
			CharSequence text) {
		final LoadingDialog loading = new LoadingDialog(context,
				R.style.customDialog);
		loading.show();
		if (text != null) {
			loading.setMessage(text);
		}
		return loading;
	}

	public static void showSetNetworkDialog(Context context) {
		Intent intent = NewIntent.newAction(Settings.ACTION_SETTINGS);
		showDialog(context, R.string.set_network_content,
				R.string.set_network_right_now, intent, false);
	}

	public static void showLoginDialog(Context context) {
		Intent intent = NewIntent.newActivityWithExtra(context,
				LoginActivity.class, "caller", "CHANGPASSWORD");
		showDialog(context, R.string.ask_login_content,
				R.string.login_right_now, intent, true);
	}

	public static void showDialog(Context context, int resource, int button,
			final Intent intent, final boolean finish) {
		showDialog(context, resource, button, intent, finish, false);
	}

	/**
	 * @param context
	 * @param resource
	 * @param button
	 * @param intent
	 * @param exit
	 */
	public static void showDialog(Context context, int resource, int button,
			final Intent intent, final boolean finish, final boolean exit) {
		mContext = context;
		final CommonDialog dialog = new CommonDialog(context,
				R.style.customDialog);
		dialog.show();
		dialog.setMessage(context.getResources().getString(resource));
		dialog.setPositiveButton(context.getResources().getString(button),
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
						if (intent != null) {
							((Activity) mContext).startActivity(intent);
						}
						if (finish) {
							((Activity) mContext).finish();
							return;
						}
						if (exit) {
							android.os.Process.killProcess(android.os.Process
									.myPid());
							System.exit(0);
						}

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

	public static void showWarnningDialog(Context context, int resource,
			int button) {
		final CommonDialog dialog = new CommonDialog(context,
				R.style.customDialog);
		dialog.show();
		dialog.setMessage(context.getResources().getString(resource));
		dialog.setPositiveButton(context.getResources().getString(button),
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
	}

}
