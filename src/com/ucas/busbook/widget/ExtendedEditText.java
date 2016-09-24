package com.ucas.busbook.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.ucas.busbook.R;

//import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;

public class ExtendedEditText extends EditText implements TextWatcher,
		View.OnFocusChangeListener {
	private Drawable clear;
	private Drawable[] res;
	private boolean hasfocus;

	public ExtendedEditText(Context context) {
		this(context, null);
	}

	public ExtendedEditText(Context context, AttributeSet paramAttributeSet) {
		super(context, paramAttributeSet);
		clear = getCompoundDrawables()[2];
		if (!isInEditMode()) {
			if (clear == null)
				clear = getResources().getDrawable(R.drawable.delete_icon);
			clear.setBounds(0, 0, clear.getIntrinsicWidth(),
					clear.getIntrinsicHeight());
		}
		addTextChangedListener(this);
		setOnFocusChangeListener(this);
	}

	private void init() {
		if (getText().length() > 0) {
			setSelection(getText().length());
			setCancelVisible(true);
		} else {
			setCancelVisible(false);
		}
	}

	private void setCancelVisible(boolean bool) {
		if (res == null)
			res = getCompoundDrawables();
		if (bool) {
			setCompoundDrawablesWithIntrinsicBounds(res[0], res[1], clear,
					res[3]);
			return;
		}
		setCompoundDrawablesWithIntrinsicBounds(res[0], res[1], res[2], res[3]);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterTextChanged(Editable s) {
		if (hasfocus)
			init();
	}

	public void onTextChanged(CharSequence s, int st, int co, int af) {

	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		hasfocus = hasFocus;
		if (hasFocus) {
			init();
			return;
		}
		setCancelVisible(false);
	}

	//@SuppressLint("ClickableViewAccessibility")
	public boolean onTouchEvent(MotionEvent event) {
		while (true) {
			Drawable localDrawable = clear;
			if (localDrawable != null) {
				boolean bool3 = event.getX() < getWidth() - getPaddingRight()
						- clear.getIntrinsicWidth();
				if (!bool3) {
					setText("");
					setCancelVisible(false);
				}
			}
			return super.onTouchEvent(event);

		}

	}

}
