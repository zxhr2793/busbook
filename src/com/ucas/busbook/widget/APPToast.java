package com.ucas.busbook.widget;

import android.content.Context;
import android.widget.Toast;

public class APPToast {

	public static void showToastLong(Context context, int message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

	public static void showToastShort(Context context, int message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

}
