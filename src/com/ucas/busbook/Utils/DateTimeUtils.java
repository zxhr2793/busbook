package com.ucas.busbook.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtils {

	private static SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss", Locale.getDefault());

	private static SimpleDateFormat yearDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd", Locale.getDefault());
	private static SimpleDateFormat yearDateFormat_zh = new SimpleDateFormat(
			"yyyy年MM月dd日", Locale.getDefault());
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd",
			Locale.getDefault());
	private static SimpleDateFormat weekFormat = new SimpleDateFormat("EE",
			Locale.getDefault());
	private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm",
			Locale.getDefault());

	private static String[] weekDayShort = { "周日", "周一", "周二", "周三", "周四",
			"周五", "周六" };

	/**
	 * 比较两个日期的大小
	 * 
	 * @param dateString1
	 * @param dateString2
	 * @return 如果dateString1在dateString2之前，返回1；之后则返回-1；相等返回0
	 */
	public static int compareDate(String dateString1, String dateString2) {
		Date date1 = String2YearDate(dateString1);
		Date date2 = String2YearDate(dateString2);
		if (date1.getTime() > date2.getTime()) {
			return 1;
		} else if (date1.getTime() < date2.getTime()) {
			return -1;
		}
		return 0;
	}

	/**
	 * 将calendar转换为String
	 * 
	 * @param calendar
	 * @return yyyy-MM-dd型的字符串
	 */
	public static String Calendar2String(Calendar calendar) {
		return yearDateFormat.format(calendar.getTime());
	}

	/**
	 * 将String转换为Calendar
	 * 
	 * @param str
	 * @return
	 */
	public static Calendar String2Calendar(String str) {
		Calendar calendar = Calendar.getInstance();
		Date date = new Date();
		try {
			date = (Date) yearDateFormat.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		calendar.setTime(date);
		return calendar;
	}

	/**
	 * 将date转换为字符串
	 * 
	 * @param date
	 * @return 字符类型的日期，格式为：MM - dd
	 */
	public static String Date2String(Date date) {
		return dateFormat.format(date);
	}

	/**
	 * 将String 格式化为日期形式
	 * 
	 * @param str
	 * @return MM-dd类型的日期
	 */
	public static Date String2Date(String str) {
		Date date = new Date();
		try {
			date = dateFormat.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return date;
	}

	/**
	 * 将date转换为字符串
	 * 
	 * @param date
	 * @return 字符类型的日期，格式为：yyyy-MM-dd
	 */
	public static String YearDate2String(Date date) {
		return yearDateFormat.format(date);
	}

	/**
	 * 将String 解析为日期形式
	 * 
	 * @param str
	 * @return yyyy-MM-dd类型的日期
	 */
	public static Date String2YearDate(String str) {
		Date date = new Date();
		try {
			date = yearDateFormat.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return date;
	}

	/**
	 * 将date转换为字符串
	 * 
	 * @param date
	 * @return 字符类型的日期，格式为：yyyy年MM月dd日
	 */
	public static String YearDate2String_zh(Date date) {
		return yearDateFormat_zh.format(date);
	}

	/**
	 * date转换为calender
	 * 
	 * @param date
	 * @return
	 */
	public static Calendar Date2Calendar(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}

	/**
	 * 将calendar 转换为 date
	 * 
	 * @param calendar
	 * @return
	 */
	public static Date Calendar2Date(Calendar calendar) {
		return calendar.getTime();
	}

	/**
	 * 将date格式化为星期
	 * 
	 * @param date
	 * @return EE格式，如：周一
	 */
	public static String getWeekDay(Date date) {
		Calendar calendar = Date2Calendar(date);
		return getWeekDay(calendar);
	}

	/**
	 * 获取calendar 对应的星期
	 * 
	 * @param calendar
	 * @return
	 */
	public static String getWeekDay(Calendar calendar) {
		int i = calendar.get(Calendar.DAY_OF_WEEK);
		return weekDayShort[i-1];
	}

	/**
	 * 将String解析成date
	 * 
	 * @param dateString
	 * @return 格式为：yyyy-MM-dd HH:mm:ss 的日期
	 */
	public static Date String2DateTime(String dateString) {
		Date date = new Date();
		try {
			date = format.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 将date格式化为time
	 * 
	 * @param date
	 * @return HH:mm格式的时间
	 */
	public static String Date2Time(Date date) {
		return timeFormat.format(date);
	}
	
	public static Date getDate(long timeInMillis){
		Date date = new Date();
		date.setTime(timeInMillis);
		return date;
	}

}
