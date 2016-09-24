package com.ucas.busbook.widget;

import java.io.File;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;

public class APPInfo extends Application {
	private static PackageManager packageManager;
	private static String packageName;
	public APPInfo(Context context) {
		packageManager = context.getPackageManager();
		packageName = context.getPackageName();
	}

	public static int getVersionCode(String packageName) {
		int versionCode = 0;
		//System.out.println(packageName);
		//PackageManager packageManager = context.getPackageManager();
		try {
			versionCode = packageManager.getPackageInfo(packageName, 0).versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			versionCode = 0;
		}
		return versionCode;
	}

	public int getVersionCode() {
		int versionCode = 0;
		versionCode = getVersionCode(packageName);
		return versionCode;
	}

	public static String getVersionName() {
		String versionName = getVersionName(packageName);
		return versionName;
	}

	private static String getVersionName(String packageName) {
		String versionName = "";
		try {
			versionName = packageManager.getPackageInfo(packageName, 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionName;
	}

	public static void installAPK(Context context, File file) {
		if (file == null || !file.exists())
			return;
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}

}
