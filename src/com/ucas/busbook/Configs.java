package com.ucas.busbook;

import java.io.File;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Environment;

public class Configs {

	public static final String SERVER_URL = "http://210.77.0.5:8080/TestSever";
	// public static final String SERVER_URL =
	// "http://124.16.79.144:8080/TestSever";

	public static final String KEY_TOKEN = "token";
	public static final String KEY_ACTION = "action";
	public static final String KEY_STUDENT_ID = "sid";
	public static final String KEY_CURRENT_PASSWORD = "cpswd";
	public static final String KEY_NEW_PASSWORD = "npswd";
	public static final String KEY_STATUS = "status";
	public static final String KEY_REAL_NAME = "name";
	public static final String KEY_PHONE_NUM = "phonenum";

	public static final String KEY_ADD_ORDER = "addorder";
	public static final String KEY_ORDER_RESULT = "result";
	public static final String KEY_ORDER_SOURCE = "source";
	public static final String KEY_ORDER_STATE = "Jflag";
	public static final String KEY_DATE_DIFF = "datediff";
	public static final String KEY_UNAVALIBLE_DATE = "unavalibledate";
	public static final String KEY_SEVER_DATE = "severdate";
	public static final String KEY_ACCOUNT_ID = "accountId";
	public static final String KEY_UPDATE_INFO = "updateInfo";
	public static final String KEY_ROUTE_NAME = "rname";
	public static final String KEY_QUERY_DATE = "rdate";
	public static final String KEY_TICKET_NUM = "tnumber";
	public static final String KEY_TAKE_DATE = "rdate";
	public static final String KEY_GET_SEVER_CONFIG = "getConfig";
	public static final String KEY_LASTEST_TIME = "lastest_time";
	public static final String KEY_SHOW_BAR = "show";
	public static final String KEY_TIME_STAMP = "timestamp";
	public static final String KEY_RESET_PSWD = "reset";

	// public static final String KEY_ORDER_RESULT = "result";

	public static final int RESULT_STATUS_SUCCESS = 1;
	public static final int RESULT_STATUS_FAIL = 2;
	public static final int RESULT_STATUS_INVALID_TOKEN = 3;
	public static final int RESULT_STATUS_INVALID_ID = 4;
	public static final int RESULT_STATUS_INVALID_PASSWORD = 5;
	public static final int RESULT_STATUS_NO_ORDER = 10;
	public static final int RESULT_STATUS_BOOK_FAIL = 11;
	public static final int RESULT_STATUS_WEEK_FAIL = 12;
	public static final int RESULT_STATUS_YEAR_FAIL = 13;
	public static final int RESULT_STATUS_TICKET_REPEAT = 14;
	public static final int RESULT_STATUS_INVALID_DATE = 15;
	public static final int RESULT_STATUS_NO_ENOUGH_TICKET = 16;
	public static final int RESULT_STATUS_NO_NEW_MESSAGE = 18;
	public static final int RESULT_STATUS_INVALID_TIME = 19;

	public static final int ERROR_NETWORK_NOT_CONNECT = 6;
	public static final int ERROR_HTTP_EXCEPTION = 7;
	public static final int ERROR_SOCKET_EXCEPTION = 8;
	public static final int ERROR_JSON_EXCEPTION = 9;
	public static final int ERROR_FAIL_TO_CONNECT_SEVER = 17;

	public static final int dateDiff = 2;

	public static final String APP_ID = "com.ucas.busbook";
	public static final String DATE_INFO = "dateinfo";
	public static final String USER_INFO = "userinfo";
	public static final String NOTICE_INFO = "notice";
	public static final String CHARSET = "utf-8";

	public static final String QQ_APP_ID = "1104719225";
	public static final String QQ_APP_KEY = "fiBUJeHcGkTlWnsG";
	public static final String WEIXIN_APP_ID = "wxfaa84530c3065517";
	public static final String WEIXIN_APP_SECRET = "eeae796690025cff953e418e18944c60";
	public static final String MOB_APP_KEY="947ff18bab7e";
	public static final String MOB_APP_SECRET="0cfa990927e340819630be4b0e15722b";

	public static final String ACTION_LOGIN = "login";
	public static final String ACTION_CHANGE_PASSWORD = "changePswd";
	public static final String ACTION_CHANGE_PHONE = "changePhone";
	public static final String ACTION_CHECK_UPDATE = "checkUpdate";
	public static final String ACTION_QUERY_REST_TICKET = "queryTicket";
	public static final String ACTION_ORDER_LIST = "orderlist";
	public static final String ACTION_ORDER_LIST_HISTORY = "historyorder";
	public static final String ACTION_BOOK_TICKET = "bookTicket";
	public static final String ACTION_GET_NOTICE = "get";
	public static final String ACTION_QUERY_NOTICE = "query";
	public static final String ACTION_GET_PHNOE = "getPhone";

	public static boolean isLogin = false;
	public static boolean isFirstCheckUp = true;
	public static boolean hasLastest = false;
	public static int index = 0;

	public final static String DEFAULT_SAVE_FILE_PATH = Environment.getExternalStorageDirectory() + File.separator;


	/**
	 * @param context
	 * @return token
	 */
	public static String getCachedToken(Context context) {
//		context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE).
		return context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE).getString(KEY_TOKEN, null);
	}

	public static void cacheToken(Context context, String token) {
		Editor editor = context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE).edit();
		editor.putString(KEY_TOKEN, token);
		editor.commit();
	}

	/**
	 * @param context
	 * @return studentid
	 */
	public static String getCachedStudentId(Context context) {
		return context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE).getString(KEY_STUDENT_ID, null);
	}

	public static void cacheStudentId(Context context, String studentId) {
		Editor editor = context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE).edit();
		editor.putString(KEY_STUDENT_ID, studentId);
		editor.commit();
	}

	/**
	 * @param context
	 * @param studentName
	 */
	public static void cacheStudentName(Context context, String studentName) {
		Editor editor = context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE).edit();
		editor.putString(KEY_REAL_NAME, studentName);
		editor.commit();
	}

	public static String getCachedStudentName(Context context) {
		return context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE).getString(KEY_REAL_NAME, null);
	}

	/**
	 * @param context
	 * @param phoneNum
	 */
	public static void cachePhoneNum(Context context, String phoneNum) {
		Editor editor = context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE).edit();
		editor.putString(KEY_PHONE_NUM, phoneNum);
		editor.commit();
	}

	public static String getCachedPhoneNum(Context context) {
		return context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE).getString(KEY_PHONE_NUM, null);
	}

	public static void clearUserInfo(Context context) {
		Editor editor = context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE).edit();
		editor.clear();
		editor.commit();
	}

	/**
	 * 保存从服务器获取到的预售日期和不可预订日期
	 * 
	 * @param context
	 * @param phoneNum
	 */
	public static void cacheDateDiff(Context context, int dataDiff) {
		Editor editor = context.getSharedPreferences(DATE_INFO, Context.MODE_PRIVATE).edit();
		editor.putInt(KEY_DATE_DIFF, dataDiff);
		editor.commit();
	}

	public static int getDateDiff(Context context) {
		return context.getSharedPreferences(DATE_INFO, Context.MODE_PRIVATE).getInt(KEY_DATE_DIFF, 2);
	}

	public static void cacheUnavalibleDate(Context context, String unavalibleDate) {
		Editor editor = context.getSharedPreferences(DATE_INFO, Context.MODE_PRIVATE).edit();
		editor.putString(KEY_UNAVALIBLE_DATE, unavalibleDate);
		editor.commit();
	}

	public static String getUnavalibleDate(Context context) {
		return context.getSharedPreferences(DATE_INFO, Context.MODE_PRIVATE).getString(KEY_UNAVALIBLE_DATE, null);
	}

	public static void cacheSeverDate(Context context, String dataDiff) {
		Editor editor = context.getSharedPreferences(DATE_INFO, Context.MODE_PRIVATE).edit();
		editor.putString(KEY_SEVER_DATE, dataDiff);
		editor.commit();
	}

	public static String getSeverDate(Context context) {
		return context.getSharedPreferences(DATE_INFO, Context.MODE_PRIVATE).getString(KEY_SEVER_DATE, null);
	}

	public static void cacheMessageTime(Context context, String time) {
		Editor editor = context.getSharedPreferences(NOTICE_INFO, Context.MODE_PRIVATE).edit();
		editor.putString(KEY_LASTEST_TIME, time);
		editor.commit();
	}

	public static String getCachedMessageTime(Context context) {
		return context.getSharedPreferences(NOTICE_INFO, Context.MODE_PRIVATE).getString(KEY_LASTEST_TIME, "0");
	}

	public static void cacheShowNotice(Context context, boolean flag) {
		Editor editor = context.getSharedPreferences(NOTICE_INFO, Context.MODE_PRIVATE).edit();
		editor.putBoolean(KEY_SHOW_BAR, flag);
		editor.commit();
	}

	public static boolean getShowNotice(Context context) {
		return context.getSharedPreferences(NOTICE_INFO, Context.MODE_PRIVATE).getBoolean(KEY_SHOW_BAR, false);
	}

	public static boolean checkUpdate() {
		return true;
	}

	public static boolean firstCheckUp() {
		return isFirstCheckUp;
	}

	public static boolean isLogin() {
		return isLogin;
	}

	public static boolean hasLastest() {
		return hasLastest;
	}

}
