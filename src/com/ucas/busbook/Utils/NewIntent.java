package com.ucas.busbook.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * 管理应用程序使用的Intent
 * 
 * @author qiankunt
 *
 */
public class NewIntent {

	public NewIntent() {
	}

	public static void startNewActivity(Context context, Class<?> cls) {
		((Activity) context).startActivity(newActivity(context, cls));
	}

	public static void startNewAction(Context context, String action) {
		((Activity) context).startActivity(newAction(action));
	}

	public static Intent newActivity(Context context, Class<?> cls) {
		Intent intent = new Intent();
		intent.setClass(context, cls);
		return intent;
	}

	public static Intent newAction(String action) {
		Intent intent = new Intent();
		intent.setAction(action);
		return intent;
	}

	public static Intent newActivityWithExtra(Context context, Class<?> cls,
			String... kvs) {
		Intent intent = new Intent();
		intent.setClass(context, cls);
		for (int i = 0; i < kvs.length; i += 2) {
			intent.putExtra(kvs[i], kvs[i + 1]);
		}
		return intent;
	}

}
