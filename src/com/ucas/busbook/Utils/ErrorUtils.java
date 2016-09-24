package com.ucas.busbook.Utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.ucas.busbook.Configs;
import com.ucas.busbook.R;
import com.ucas.busbook.dialog.APPDialog;

public class ErrorUtils {
	private static Context mContext;

	public ErrorUtils() {
	}

	public static void networkOrActionError(int errorCode, final Context context) {
		mContext = context;
		Activity activity = (Activity) mContext;
		switch (errorCode) {
		case Configs.ERROR_NETWORK_NOT_CONNECT: {
			activity.runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(context, R.string.network_not_connected,
							Toast.LENGTH_LONG).show();
				}
			});
		}
			break;
		case Configs.ERROR_HTTP_EXCEPTION: {
			activity.runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(context, R.string.error_http_exception,
							Toast.LENGTH_LONG).show();
				}
			});
		}
			break;

		case Configs.ERROR_SOCKET_EXCEPTION: {
			activity.runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(context, R.string.error_socket_exception,
							Toast.LENGTH_LONG).show();
				}
			});
		}
			break;

		case Configs.ERROR_FAIL_TO_CONNECT_SEVER: {
			activity.runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(context,
							R.string.error_fail_to_connect_sever,
							Toast.LENGTH_LONG).show();
				}
			});
		}
			break;
		case Configs.RESULT_STATUS_INVALID_ID:
			Toast.makeText(context, R.string.invalid_student_id,
					Toast.LENGTH_LONG).show();
			break;
		case Configs.RESULT_STATUS_INVALID_PASSWORD: {
			Toast.makeText(context, R.string.invalid_password,
					Toast.LENGTH_LONG).show();
		}
			break;
		case Configs.RESULT_STATUS_INVALID_DATE:
			APPDialog.showWarnningDialog(context, R.string.invalid_date,
					R.string.dialog_ok_btn);
			break;
		case Configs.RESULT_STATUS_INVALID_TIME:
			APPDialog.showWarnningDialog(context, R.string.invalid_time,
					R.string.dialog_ok_btn);
			break;

		case Configs.ERROR_JSON_EXCEPTION: {
			Toast.makeText(context, R.string.error_json_exception,
					Toast.LENGTH_LONG).show();
		}
			break;

		case Configs.RESULT_STATUS_BOOK_FAIL:
			APPDialog.showWarnningDialog(context,
					R.string.fail_message_no_ticket, R.string.dialog_ok_btn);
			break;
		case Configs.RESULT_STATUS_TICKET_REPEAT:
			APPDialog.showWarnningDialog(context, R.string.fail_repeat_book,
					R.string.dialog_ok_btn);
			break;
		case Configs.RESULT_STATUS_WEEK_FAIL:
			APPDialog.showWarnningDialog(context, R.string.fail_message_week,
					R.string.dialog_ok_btn);
			break;
		case Configs.RESULT_STATUS_YEAR_FAIL:
			APPDialog.showWarnningDialog(context, R.string.fail_message_year,
					R.string.dialog_ok_btn);
			break;

		default:
			Toast.makeText(context, R.string.fail_to_operate, Toast.LENGTH_LONG)
					.show();
			break;
		}
	}

}
