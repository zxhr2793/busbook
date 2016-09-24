package com.ucas.busbook.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ucas.busbook.Utils.DateTimeUtils;

@SuppressWarnings("serial")
public class Message implements Serializable {

	private String messageId;
	private String messageTitle;
	private String messageContent;
	private String messageTime;

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getMessageTitle() {
		return messageTitle;
	}

	public void setMessageTitle(String messageTitle) {
		this.messageTitle = messageTitle;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	public String getMessageTime() {
		return messageTime;
	}

	public void setMessageTime(String messageTime) {
		this.messageTime = messageTime;
	}

	public Message parse(JSONObject jsonObject) {
		Message message = new Message();
		try {
			String timeString = jsonObject.getString("time");
			long time = Long.parseLong(timeString);
			Date date = DateTimeUtils.getDate(time);
			message.setMessageTime(DateTimeUtils.YearDate2String_zh(date));
			message.setMessageId(jsonObject.getString("id"));
			message.setMessageContent(jsonObject.getString("content"));
			message.setMessageTitle(jsonObject.getString("title"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return message;
	}

	public List<Message> getMessages(JSONArray jsonArray) {
		List<Message> list = new ArrayList<Message>();
		try {
			for (int i = 0; i < jsonArray.length(); i++) {
				Message message = new Message();
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				list.add(message.parse(jsonObject));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

}
