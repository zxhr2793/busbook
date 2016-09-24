package com.ucas.busbook.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ruifang
 *
 */

public class TextUtil {

	/**
	 * 判断输入的input是否是合法的手机号码，
	 * 
	 * @param input
	 * @return
	 */
	public static boolean isMobile(CharSequence input) {
		Pattern pattern = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher matcher = pattern.matcher(input);
		return matcher.matches();
	}

	/**
	 * 判断输入的字符串是否全是数字且有六位
	 * 
	 * @param input
	 * @return
	 */
	public static boolean isNumber(CharSequence input) {
		Pattern pattern = Pattern.compile("^\\d{6}$");
		Matcher matcher = pattern.matcher(input);
		return matcher.matches();
	}

	/**
	 * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
	 * 
	 * @param input
	 * @return boolean
	 */
	public static boolean isEmpty(String input) {
		if (input == null || "".equals(input))
			return true;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}

	/** 分割电话号码 */
	public static String splitPhoneNum(String phone) {
		StringBuilder builder = new StringBuilder(phone);
		builder.reverse();
		for (int i = 4, len = builder.length(); i < len; i += 5) {
			builder.insert(i, ' ');
		}
		builder.reverse();
		return builder.toString();
	}

}
