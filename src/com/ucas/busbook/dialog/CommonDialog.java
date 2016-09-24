package com.ucas.busbook.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.ucas.busbook.R;

public class CommonDialog extends Dialog {

	private Context context;
	private TextView dialogContent;
	private TextView dialogTitle;
	private Button positiveBtn;
	private Button negativeBtn;
	private View divider;

	public CommonDialog(Context context) {
		super(context);
		this.context = context;
	}

	public CommonDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	public CommonDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	@SuppressLint("InflateParams")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_common, null);
		dialogTitle = (TextView) view.findViewById(R.id.dialog_title);
		dialogContent = (TextView) view.findViewById(R.id.dialog_content);
		positiveBtn = (Button) view.findViewById(R.id.dialog_positive_btn);
		negativeBtn = (Button) view.findViewById(R.id.dialog_negative_btn);
		divider = view.findViewById(R.id.dialog_divider);
		this.setContentView(view);
	}

	public void setPositiveButton(CharSequence text,
			View.OnClickListener onClickListener) {
		if (TextUtils.isEmpty(text)) {
			return;
		}
		if (negativeBtn.getVisibility() == View.VISIBLE) {
			divider.setVisibility(View.VISIBLE);
		}
		positiveBtn.setVisibility(View.VISIBLE);
		positiveBtn.setText(text);
		positiveBtn.setOnClickListener(onClickListener);
	}

	public void setNegativeButton(CharSequence text,
			View.OnClickListener onClickListener) {
		if (TextUtils.isEmpty(text)) {
			return;
		}
		if (positiveBtn.getVisibility() == View.VISIBLE) {
			divider.setVisibility(View.VISIBLE);
		}
		negativeBtn.setVisibility(View.VISIBLE);
		negativeBtn.setText(text);
		negativeBtn.setOnClickListener(onClickListener);
	}

	public void setMessage(CharSequence text) {
		dialogContent.setText(text);
	}
	
	public void setTitle(CharSequence title){
		dialogTitle.setText(title);
	}
	
	public void setTextSize(float size){
		dialogContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
	}

}
