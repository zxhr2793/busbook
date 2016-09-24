package com.ucas.busbook.calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Service;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ucas.busbook.R;

public class MyCalendarView extends FrameLayout {

	private String DATE_FORMAT = "MM/dd/yyyy";
	private boolean DEFAULT_SHOW_WEEK_NUMBER = false;
	private int DEFAULT_FIRST_DAY_OF_WEEK = 1;
	private int DEFAULT_SHOWN_WEEK_COUNT = 6;
	private int UNSCALED_BOTTOM_BUFFER = 20;
	private int UNSCALED_LIST_SCROLL_TOP_OFFSET = 2;
	private int UNSCALED_WEEK_MIN_VISIBLE_HEIGHT = 12;
	private float DEFAULT_WEEK_NAMES_TEXT_SIZE = 16;
	private String DEFAULT_START_DATE = "04/01/2015";
	private String DEFAULT_END_DATE = "01/01/2030";

	private int mFocusedMonthDateColor;
	private int mUnFocusedMonthDateColor;
	private int mWeekNumberColor;
	private int mDateTextSize;

	private boolean mShowWeekNumber;

	private int mFirstDayOfWeek = 1;

	private int mShownWeekCount;

	private int mCurrentMonthDisplayed;

	private int mListScrollTopOffset = 2;

	private int mWeekMinVisibleHeight = 12;

	private int mBottomBuffer = 20;

	private int mDaysPerWeek = 7;

	private float mFriction = .05f;

	private float mVelocityScale = 0.333f;

	private ListView mListView;

	private ImageView prev, next;

	private ViewGroup mWeekNamesHeader;

	private String[] mWeekLabels;

	private TextView mMonthName;

	private Locale mlocale;

	private Calendar mTempDate;

	private Calendar mStartDate;

	private Calendar mEndDate;

	private Context mContext;

	private SimpleMonthAdapter monthAdapter;

	private OnDateChangeListener mOnDateChangeListener;

	private java.text.DateFormat mDateFormat = new SimpleDateFormat(DATE_FORMAT);

	// private SimpleMonthAdapter.CalendarDay mSelectedDay = new
	// SimpleMonthAdapter.CalendarDay();

	public MyCalendarView(Context context) {
		this(context, null);
	}

	public MyCalendarView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MyCalendarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, 0);
		if (isInEditMode()) {
			return;
		}
		setCurrentLocale(Locale.getDefault());

		TypedArray attributesArray = context.obtainStyledAttributes(attrs,
				R.styleable.MyCalendarView);
		mShowWeekNumber = attributesArray.getBoolean(
				R.styleable.MyCalendarView_showWeekNumber,
				DEFAULT_SHOW_WEEK_NUMBER);
		mFirstDayOfWeek = attributesArray.getInt(
				R.styleable.MyCalendarView_firstDayOfWeek,
				DEFAULT_FIRST_DAY_OF_WEEK);

		String startDate = attributesArray
				.getString(R.styleable.MyCalendarView_startDate);
		if (TextUtils.isEmpty(startDate) || !parseDate(startDate, mStartDate)) {
			parseDate(DEFAULT_START_DATE, mStartDate);
		}
		String endDate = attributesArray
				.getString(R.styleable.MyCalendarView_endDate);
		if (TextUtils.isEmpty(endDate) || !parseDate(endDate, mEndDate)) {
			parseDate(DEFAULT_START_DATE, mEndDate);
		}
		if (mEndDate.before(mStartDate)) {
			throw new IllegalArgumentException(
					"End date cannot be before start date.");
		}
		mShownWeekCount = attributesArray.getInt(
				R.styleable.MyCalendarView_shownWeekCount,
				DEFAULT_SHOWN_WEEK_COUNT);
		mFocusedMonthDateColor = attributesArray.getColor(
				R.styleable.MyCalendarView_focusedMonthDateColor, 0);
		mUnFocusedMonthDateColor = attributesArray.getColor(
				R.styleable.MyCalendarView_unfocusedMonthDateColor, 0);
		mWeekNumberColor = attributesArray.getColor(
				R.styleable.MyCalendarView_weekNumberColor, 0);

		attributesArray.recycle();

		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		mBottomBuffer = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, UNSCALED_BOTTOM_BUFFER,
				displayMetrics);
		mListScrollTopOffset = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, UNSCALED_LIST_SCROLL_TOP_OFFSET,
				displayMetrics);
		mWeekMinVisibleHeight = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, UNSCALED_WEEK_MIN_VISIBLE_HEIGHT,
				displayMetrics);

		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
		View content = layoutInflater.inflate(R.layout.my_calendarview, null,
				false);
		addView(content);

		mListView = (ListView) content.findViewById(R.id.datelist);
		mMonthName = (TextView) content.findViewById(R.id.year_month);
		mWeekNamesHeader = (ViewGroup) content.findViewById(R.id.week_names);
		prev = (ImageView) content.findViewById(R.id.prev);
		next = (ImageView) content.findViewById(R.id.next);

		mTempDate.setTimeInMillis(System.currentTimeMillis());
		setUpHeader();
		setUpListView();
		setUpAdapter();

		setMonthDisplayed(mTempDate);
		invalidate();
	}

	private boolean parseDate(String date, Calendar outDate) {
		try {
			outDate.setTime(mDateFormat.parse(date));
			return true;
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}

	}

	private void setUpAdapter() {
		if (monthAdapter == null) {
			monthAdapter = new SimpleMonthAdapter(getContext());
			mListView.setAdapter(monthAdapter);
		}
		monthAdapter.notifyDataSetChanged();
	}

	private void setUpListView() {
		mListView.setDivider(null);
		mListView.setItemsCanFocus(true);
		mListView.setFriction(mFriction);
		mListView.setVelocityScale(mVelocityScale);
	}

	private void setUpHeader() {
		mWeekLabels = new String[mDaysPerWeek];
		for (int i = mFirstDayOfWeek, count = mFirstDayOfWeek + mDaysPerWeek; i < count; i++) {
			int calendarDay = (i > Calendar.SATURDAY) ? i - Calendar.SATURDAY
					: i;
			mWeekLabels[i - mFirstDayOfWeek] = DateUtils.getDayOfWeekString(
					calendarDay, DateUtils.LENGTH_SHORTEST);
		}

		// TextView label = (TextView) mWeekNamesHeader.getChildAt(0);
		// label.setVisibility(View.VISIBLE);

		TextView label;

		for (int i = 0, count = mWeekNamesHeader.getChildCount(); i < count; i++) {
			label = (TextView) mWeekNamesHeader.getChildAt(i);
			if (i < mDaysPerWeek) {
				label.setText(mWeekLabels[i]);
				label.setTextSize(DEFAULT_WEEK_NAMES_TEXT_SIZE);
				label.setVisibility(View.VISIBLE);
			} else {
				label.setVisibility(View.GONE);
			}
		}
		mWeekNamesHeader.invalidate();
	}

	private void setCurrentLocale(Locale locale) {
		if (locale.equals(mlocale)) {
			return;
		}
		mlocale = locale;
		mTempDate = getCalendarForLocale(mTempDate, locale);
	}

	private Calendar getCalendarForLocale(Calendar oldCalendar, Locale locale) {
		if (oldCalendar == null) {
			return Calendar.getInstance(locale);
		}
		{
			final long currentTimeMills = oldCalendar.getTimeInMillis();
			Calendar newCalendar = Calendar.getInstance(locale);
			newCalendar.setTimeInMillis(currentTimeMills);
			return newCalendar;
		}

	}

	private void setMonthDisplayed(Calendar calendar) {
		final int newMonthDisplayed = calendar.get(Calendar.MONTH);
		if (mCurrentMonthDisplayed != newMonthDisplayed) {
			mCurrentMonthDisplayed = newMonthDisplayed;
			monthAdapter.setFocusMonth(mCurrentMonthDisplayed);
			final int flags = DateUtils.FORMAT_SHOW_DATE
					| DateUtils.FORMAT_NO_MONTH_DAY
					| DateUtils.FORMAT_SHOW_YEAR;
			final long millis = calendar.getTimeInMillis();
			String newMonthName = DateUtils.formatDateRange(mContext, millis,
					millis, flags);
			mMonthName.setText(newMonthName);
			mMonthName.setTextColor(Color.BLACK);
			mMonthName.invalidate();
		}
	}

	public Calendar getStartDate() {
		return mStartDate;
	}

	public Calendar getEndDate() {
		return mEndDate;
	}

	public Calendar getSelectedDate() {
		return monthAdapter.getSelectedDay();
	}

}
