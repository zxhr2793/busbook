package com.ucas.busbook.dialog;

import com.ucas.busbook.R;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class SetNetworkDialog extends Dialog implements OnClickListener {

	private Context context;

	public SetNetworkDialog(Context context) {
		super(context);
		this.context = context;
	}

	public SetNetworkDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.dialog_set_network, null);
		layout.findViewById(R.id.cancel_set_btn).setOnClickListener(this);
		layout.findViewById(R.id.set_btn).setOnClickListener(this);
		//System.out.println("dialog");
		LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		this.setContentView(layout,params);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancel_set_btn:
			dismiss();
			break;

		case R.id.set_btn: {
			Intent intent = new Intent();
			intent.setAction(Settings.ACTION_SETTINGS);
			context.startActivity(intent);
			dismiss();
		}
			break;

		default:
			break;
		}
	}

}
