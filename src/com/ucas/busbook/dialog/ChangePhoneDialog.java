package com.ucas.busbook.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.ucas.busbook.Configs;
import com.ucas.busbook.R;
import com.ucas.busbook.Utils.TextUtil;

public class ChangePhoneDialog extends Dialog implements OnClickListener {

	private Context context;
	private EditText newPhone;
	private TextView warnning;
	private TextView curBindPhone;
	private ChangePhoneDialogListener mChangePhoneDialogListener;

	public ChangePhoneDialog(Context context,
			ChangePhoneDialogListener mChangePhoneDialogListener) {
		super(context);
		this.context = context;
		this.mChangePhoneDialogListener = mChangePhoneDialogListener;
	}

	public ChangePhoneDialog(Context context, int theme,
			ChangePhoneDialogListener mChangePhoneDialogListener) {
		super(context, theme);
		this.context = context;
		this.mChangePhoneDialogListener = mChangePhoneDialogListener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_change_phone, null);
		view.findViewById(R.id.dialog_change_phone_positive_btn)
				.setOnClickListener(this);
		view.findViewById(R.id.dialog_change_phone_negative_btn)
				.setOnClickListener(this);
		warnning = (TextView) view.findViewById(R.id.dialog_warnning);
		newPhone = (EditText) view.findViewById(R.id.change_phone_curphn);
		curBindPhone = (TextView) view.findViewById(R.id.cur_bind_phone);
		curBindPhone.setText(Configs.getCachedPhoneNum(context));
		this.setContentView(view);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_change_phone_negative_btn: {
			this.dismiss();
		}

			break;

		case R.id.dialog_change_phone_positive_btn: {
			
			if (TextUtils.isEmpty(newPhone.getText())) {
				warnning.setVisibility(View.VISIBLE);
				warnning.setText(R.string.phone_number_can_not_be_empty);
				return;
			}
			if (!TextUtil.isMobile(newPhone.getText())) {
				warnning.setVisibility(View.VISIBLE);
				warnning.setText(R.string.error_phone_number);
				return;
			}
			mChangePhoneDialogListener.onChange(newPhone.getText());
			this.dismiss();
		}
			break;

		default:
			break;
		}
	}

	public static interface ChangePhoneDialogListener {
		public void onChange(CharSequence data);
	}

}
