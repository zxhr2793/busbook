package com.ucas.busbook.calendar;

import java.util.Calendar;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;

public class SimpleMonthAdapter extends BaseAdapter implements OnTouchListener {

	private int MONTH_IN_YEAR = 12;
	private int mFocusedMonth;

	private GestureDetector mGestureDetector;

	private final Context mContext;

	private Calendar mSelectedDay = Calendar.getInstance();

	private MyCalendarView calendarView;

	private int daysPerMonth;

	public SimpleMonthAdapter(Context context) {
		mContext = context;
		mGestureDetector = new GestureDetector(mContext,
				new CalendarGestureListener());// ??????
		init();
	}

	private void init() {
		daysPerMonth = DaysOfPerMonth(mSelectedDay.get(Calendar.YEAR),
				mSelectedDay.get(Calendar.MONTH));
	}

	@Override
	public int getCount() {
		return daysPerMonth;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SimpleMonthView monthView = null;
		if (convertView != null) {
			monthView = (SimpleMonthView) convertView;
		} else {
			monthView = new SimpleMonthView(mContext);
			android.widget.AbsListView.LayoutParams params = new android.widget.AbsListView.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			monthView.setLayoutParams(params);
			monthView.setClickable(true);
			monthView.setOnTouchListener(this);
		}
		int selectedWeekDay = mSelectedDay.get(Calendar.DAY_OF_WEEK);
		monthView.init(position, selectedWeekDay, mFocusedMonth);
		return monthView;
	}

	/**
	 * Return the selected day
	 * 
	 * @return selected day;
	 */
	public Calendar getSelectedDay() {

		return mSelectedDay;
	}

	public void setSelectedDay(Calendar calendar) {
		if (calendar.get(Calendar.DAY_OF_YEAR) == mSelectedDay
				.get(Calendar.DAY_OF_YEAR)
				&& calendar.get(Calendar.YEAR) == mSelectedDay
						.get(Calendar.YEAR)) {
			return;
		}
		mSelectedDay.setTimeInMillis(calendar.getTimeInMillis());
		mFocusedMonth = mSelectedDay.get(Calendar.MONTH);
		notifyDataSetChanged();
	}

	/*
	 * private boolean isSelectedDayInMonth(int year,int month){ return }
	 */
	public int DaysOfPerMonth(int year, int month) {

		switch (month) {
		case java.util.Calendar.JANUARY:
		case java.util.Calendar.MARCH:
		case java.util.Calendar.MAY:
		case java.util.Calendar.JULY:
		case java.util.Calendar.AUGUST:
		case java.util.Calendar.OCTOBER:
		case java.util.Calendar.DECEMBER:
			return 31;
		case java.util.Calendar.FEBRUARY:
			if (year % 4 == 0 && year % 100 == 0)
				return 29;
			return 28;
		default:
			return 30;
		}
	}

	public void setFocusMonth(int month) {
		if (mFocusedMonth == month) {
			return;
		}
		mFocusedMonth = month;
		notifyDataSetChanged();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return false;
	}

	class CalendarGestureListener extends
			GestureDetector.SimpleOnGestureListener {
		public boolean onSingleTapUp(MotionEvent e) {
			return true;
		}
	}

}
