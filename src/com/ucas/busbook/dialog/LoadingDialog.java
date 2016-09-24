package com.ucas.busbook.dialog;

import com.ucas.busbook.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LoadingDialog extends Dialog {

	private Context context;
	private TextView content;
	private LinearLayout linearLayout;

	public LoadingDialog(Context context) {
		super(context);
		this.context = context;
		// setMessage(message);
	}

	public LoadingDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
		// setMessage(message);

	}

	public LoadingDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_loading, null);
		content = (TextView) view.findViewById(R.id.loading_text);
		linearLayout = (LinearLayout) view.findViewById(R.id.line_loading_text);
		this.setContentView(view);
	}

	public void setMessage(CharSequence text) {
		linearLayout.setVisibility(View.VISIBLE);
		content.setText(text);
	}

}
