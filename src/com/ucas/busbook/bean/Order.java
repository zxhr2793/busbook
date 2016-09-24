package com.ucas.busbook.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ucas.busbook.Configs;
import com.ucas.busbook.Utils.DateTimeUtils;

/**
 * 预约车票的订单信息
 * 
 * @author qiankunt
 * 
 */
public class Order implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id = null;
	private String source = null;
	private String desti = null;
	private String takeOffDate = null;
	private String takeOffWeekDay = null;
	private String takeOffTime = null;
	private String ticketState = null;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDesti() {
		return desti;
	}

	public void setDesti(String desti) {
		this.desti = desti;
	}

	public String getTakeOffDate() {
		return takeOffDate;
	}

	public void setTakeOffDate(String takeOffDate) {
		this.takeOffDate = takeOffDate;
	}

	public String getTakeOffWeekDay() {
		return takeOffWeekDay;
	}

	public void setTakeOffWeekDay(String takeOffWeekDay) {
		this.takeOffWeekDay = takeOffWeekDay;
	}

	public String getTakeOffTime() {
		return takeOffTime;
	}

	public void setTakeOffTime(String takeOffTime) {
		this.takeOffTime = takeOffTime;
	}

	public String getTicketState() {
		return ticketState;
	}

	public void setTicketState(String ticketState) {
		this.ticketState = ticketState;
	}

	public List<Order> getOrderList(JSONArray jsonArray) {
		List<Order> orderInfo = new ArrayList<Order>();
		JSONObject orderObject;
		try {
			for (int i = 0; i < jsonArray.length(); i++) {
				Order order = new Order();
				orderObject = jsonArray.getJSONObject(i);
				orderInfo.add(order.parse(orderObject));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return orderInfo;

	}

	public Order parse(JSONObject jsonObject) {

		Order order = new Order();
		String dateString;
		try {
			String oid = jsonObject.getString("ID");
			dateString = jsonObject.getString("BespeakTime");
			Date date = DateTimeUtils.String2DateTime(dateString);
			String route = jsonObject.getString("RouteName");
			String[] temp = route.split("\\(");
			String[] str = temp[0].split("至");
			if (jsonObject.getString(Configs.KEY_ORDER_STATE).equals("5"))
				order.setTicketState("预约成功");
			else {
				order.setTicketState("已出票");
			}
			order.setId(oid);
			order.setSource(str[0]);
			order.setDesti(str[1]);
			order.setTakeOffDate(DateTimeUtils.YearDate2String(date));
			order.setTakeOffTime(DateTimeUtils.Date2Time(date));
			order.setTakeOffWeekDay(DateTimeUtils.getWeekDay(date));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return order;
	}

}
