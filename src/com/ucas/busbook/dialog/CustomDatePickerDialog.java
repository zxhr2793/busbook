package com.ucas.busbook.dialog;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.ucas.busbook.Configs;
import com.ucas.busbook.R;
import com.ucas.busbook.Utils.DateTimeUtils;
import com.ucas.busbook.widget.WheelView;

public class CustomDatePickerDialog extends Dialog implements
		View.OnClickListener {

	private CustomDialogEventListener mCustomDialogEventListener;
	private Context context;
	private WheelView year;
	private Calendar calendar;
	private String selectedDate = "";
	private int dateDiff = Configs.getDateDiff(getContext());
	private String[] date = new String[dateDiff + 1];

	public CustomDatePickerDialog(Context context,
			CustomDialogEventListener customDialogEventListener) {
		super(context);
		this.context = context;
		this.mCustomDialogEventListener = customDialogEventListener;
		// initView();
	}

	public CustomDatePickerDialog(Context context, int theme,
			CustomDialogEventListener listener) {
		super(context, theme);
		this.context = context;
		this.mCustomDialogEventListener = listener;
		// initView();
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.dialog_date_choose, null);
		year = (WheelView) layout.findViewById(R.id.year_picker);
		layout.findViewById(R.id.positive_btn).setOnClickListener(this);
		layout.findViewById(R.id.nagtive_btn).setOnClickListener(this);
		calendar = getCalendar();
		initYear();
		initView();
		this.setContentView(layout);
	}

	private void initView() {
		year.setOffset(2);
		year.setItems(Arrays.asList(date));
		year.setSeletion(Configs.index);
	}

	private void initYear() {
		Date date = new Date();
		for (int i = 0; i < dateDiff + 1; i++) {
			if (i > 0) {
				calendar.add(Calendar.DAY_OF_YEAR, 1);
			}
			date = DateTimeUtils.Calendar2Date(calendar);
			String dateString = DateTimeUtils.YearDate2String(date);
			String weekString = DateTimeUtils.getWeekDay(date);
			if (i == 0) {
				weekString = "今天";
			}
			this.date[i] = dateString + "(" + weekString + ")";
		}
	}

	public Calendar getCalendar() {
		Calendar calendar = Calendar.getInstance(Locale.getDefault());
		String curDate = DateTimeUtils.Calendar2String(calendar);
		String severDate = Configs.getSeverDate(getContext());
		if (severDate == null) {
			return calendar;
		}
		int result = DateTimeUtils.compareDate(curDate, severDate);
		if (result != 0) {
			calendar = DateTimeUtils.String2Calendar(severDate);
		}
		return calendar;
	}

	/**
	 * submit the selected date to activity
	 * 
	 * @author qiankunt
	 * 
	 */
	public static interface CustomDialogEventListener {
		void getSelectedDate(String selectedDate);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.positive_btn: {
			Configs.index = year.getSeletedIndex();
			String selected[] = year.getSeletedItem().split("\\(");
			selectedDate = selected[0];
			mCustomDialogEventListener.getSelectedDate(selectedDate);
			CustomDatePickerDialog.this.dismiss();
		}

			break;
		default:
			Configs.index = 0;
			CustomDatePickerDialog.this.dismiss();
			break;
		}

	}

}
