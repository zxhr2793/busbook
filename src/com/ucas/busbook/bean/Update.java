package com.ucas.busbook.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Update {

	private int versionCode;
	private String versionName;
	private String updateLog;
	private String downloadUrl;
	private String appName;

	public Update() {
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getUpdateLog() {
		return updateLog;
	}

	public void setUpdateLog(String updateLog) {
		this.updateLog = updateLog;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public Update parse(JSONArray jsonArray) {
		Update update = new Update();
		try {
			JSONObject jsonObject = null;
			for (int i = 0; i < jsonArray.length(); i++) {

				jsonObject = jsonArray.getJSONObject(i);
				update.setVersionCode(jsonObject.getInt("versionCode"));
				update.setVersionName(jsonObject.getString("versionName"));
				update.setDownloadUrl(jsonObject.getString("downloadUrl"));
				update.setUpdateLog(jsonObject.getString("updateLog"));
				update.setAppName(jsonObject.getString("appName"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return update;
	}

}
