package com.ucas.busbook.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ucas.busbook.Configs;
import com.ucas.busbook.R;
import com.ucas.busbook.Utils.Callback.FailCallback;
import com.ucas.busbook.Utils.Callback.SuccessCallback;
import com.ucas.busbook.Utils.ErrorUtils;
import com.ucas.busbook.Utils.NewIntent;
import com.ucas.busbook.dialog.ChangePhoneDialog;
import com.ucas.busbook.dialog.LoadingDialog;
import com.ucas.busbook.network.ChangePhone;
import com.ucas.busbook.widget.APPToast;

public class UsercenterActivity extends Activity implements
		View.OnClickListener {

	private TextView title;
	private ImageButton back;
	private TextView currentName;
	private TextView currentStudent;
	private TextView currentPhoneNum;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_center);
		// LayoutInflater.from(this).inflate(R.layout.user_info, null);
		initView();
	}

	public void initView() {

		title = (TextView) findViewById(R.id.title_name);
		back = (ImageButton) findViewById(R.id.left_btn);

		currentName = (TextView) findViewById(R.id.login_name);
		currentStudent = (TextView) findViewById(R.id.student_id);
		currentPhoneNum = (TextView) findViewById(R.id.phnnum);

		findViewById(R.id.user_center_exit_btn).setOnClickListener(this);
		findViewById(R.id.login_btn).setOnClickListener(this);
		findViewById(R.id.line_change_phone_number).setOnClickListener(this);
		findViewById(R.id.line_booked_ticket).setOnClickListener(this);
		findViewById(R.id.line_about).setOnClickListener(this);
		findViewById(R.id.line_feedback).setOnClickListener(this);
		findViewById(R.id.line_check_new_version).setOnClickListener(this);
		findViewById(R.id.line_change_password).setOnClickListener(this);
		findViewById(R.id.line_notice_center).setOnClickListener(this);

		title.setText(getString(R.string.account_center));
		title.setVisibility(View.VISIBLE);

		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(this);
		// findViewById(R.id.line_left_btn).setVisibility(View.VISIBLE);
		// findViewById(R.id.line_left_btn).setOnClickListener(this);

		String token = Configs.getCachedToken(UsercenterActivity.this);
		String stuid = Configs.getCachedStudentId(UsercenterActivity.this);

		if (token != null && stuid != null) {
			findViewById(R.id.login_dialog).setVisibility(View.GONE);
			findViewById(R.id.user_center_exit_btn).setVisibility(View.VISIBLE);
			findViewById(R.id.login_user_info).setVisibility(View.VISIBLE);
			currentName.setText(Configs
					.getCachedStudentName(UsercenterActivity.this));
			currentStudent.setText(Configs
					.getCachedStudentId(UsercenterActivity.this));
			currentPhoneNum.setText(Configs
					.getCachedPhoneNum(UsercenterActivity.this));
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_btn: {
//			Intent intent = new Intent();
//			intent.setClass(UsercenterActivity.this, MainActivity.class);
//			startActivity(intent);
//			UsercenterActivity.this.finish();
			onBackPressed();
		}
			break;

		case R.id.line_booked_ticket: {
			String student_id = Configs
					.getCachedStudentId(UsercenterActivity.this);
			String currentToken = Configs
					.getCachedToken(UsercenterActivity.this);
			if (currentToken != null && student_id != null) {
				Intent intent = new Intent();
				intent.putExtra(Configs.KEY_STUDENT_ID, student_id);
				intent.putExtra(Configs.KEY_TOKEN, currentToken);
				intent.setClass(UsercenterActivity.this,
						OrderManageActivity.class);
				startActivity(intent);
//				UsercenterActivity.this.finish();
			} else {
				Toast.makeText(UsercenterActivity.this,
						getResources().getString(R.string.without_login),
						Toast.LENGTH_LONG).show();
			}

		}
			break;

		case R.id.login_btn: {
			Intent intent = new Intent();
			intent.setClass(UsercenterActivity.this, LoginActivity.class);
			intent.putExtra("caller", "USERCENTER");
			startActivity(intent);
			//UsercenterActivity.this.finish();

		}
			break;

		case R.id.user_center_exit_btn: {
			Configs.clearUserInfo(UsercenterActivity.this);
			Configs.isLogin = false;
			findViewById(R.id.login_user_info).setVisibility(View.GONE);
			findViewById(R.id.login_dialog).setVisibility(View.VISIBLE);
			findViewById(R.id.user_center_exit_btn).setVisibility(View.GONE);
		}
			break;

		case R.id.line_feedback: {
			NewIntent.startNewActivity(this, FeedBackActivity.class);
			//UsercenterActivity.this.finish();
		}
			break;
		case R.id.line_change_password: {
			String student_id = Configs
					.getCachedStudentId(UsercenterActivity.this);
			String currentToken = Configs
					.getCachedToken(UsercenterActivity.this);
			if (currentToken != null && student_id != null) {
				Intent intent = new Intent();
				intent.putExtra(Configs.KEY_STUDENT_ID, student_id);
				intent.putExtra(Configs.KEY_TOKEN, currentToken);
				intent.setClass(UsercenterActivity.this,
						ChangePswdActivity.class);
				startActivity(intent);
				UsercenterActivity.this.finish();
			} else {
				Toast.makeText(UsercenterActivity.this,
						getResources().getString(R.string.without_login),
						Toast.LENGTH_LONG).show();
			}
		}
			break;
		case R.id.line_change_phone_number: {
			if (!Configs.isLogin()) {
				APPToast.showToastShort(this, R.string.without_login);
				return;
			}
			final ChangePhoneDialog changeDialog = new ChangePhoneDialog(
					UsercenterActivity.this, R.style.customDialog,
					new ChangePhoneDialog.ChangePhoneDialogListener() {

						@Override
						public void onChange(CharSequence data) {
							updatePhone(data);
						}
					});
			changeDialog.show();
		}
			break;

		case R.id.line_check_new_version: {
			new UpdateManager(UsercenterActivity.this, true).checkUpdate();
		}
			break;
		case R.id.line_about:
			NewIntent.startNewActivity(UsercenterActivity.this,
					AboutActivity.class);
			
			break;

		case R.id.line_notice_center:
			NewIntent.startNewActivity(UsercenterActivity.this,
					NoticeCenterActivity.class);
			break;
		default:
			break;

		}
	}

	protected void updatePhone(CharSequence data) {
		final LoadingDialog loading = new LoadingDialog(
				UsercenterActivity.this, R.style.customDialog);
		loading.show();
		loading.setMessage(getResources().getString(R.string.change_submitting));
		String studentId = Configs.getCachedStudentId(UsercenterActivity.this);
		String token = Configs.getCachedToken(UsercenterActivity.this);
		final String newphone = data.toString();
		new ChangePhone(studentId, token, newphone, new SuccessCallback() {
			@Override
			public void onSuccess(String... result) {
				loading.dismiss();
				Configs.cachePhoneNum(UsercenterActivity.this, newphone);
				currentPhoneNum.setText(newphone);
				Toast.makeText(UsercenterActivity.this,
						R.string.change_success, Toast.LENGTH_LONG).show();
			}
		}, new FailCallback() {

			@Override
			public void onFail(int errorCode) {
				loading.dismiss();
				ErrorUtils.networkOrActionError(errorCode,
						UsercenterActivity.this);
			}
		});
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mHandler.sendEmptyMessage(1);
	}
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				initView();
			}
		}
		
	};
	
	

//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			NewIntent.startNewActivity(UsercenterActivity.this,
//					MainActivity.class);
//			UsercenterActivity.this.finish();
//			return true;
//		} else {
//			return super.onKeyDown(keyCode, event);
//		}
//
//	}
	
	
	
}
