package com.ucas.busbook.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ucas.busbook.Configs;
import com.ucas.busbook.R;
import com.ucas.busbook.Utils.Callback.FailCallback;
import com.ucas.busbook.Utils.Callback.SuccessCallback;
import com.ucas.busbook.Utils.ErrorUtils;
import com.ucas.busbook.Utils.MD5Tool;
import com.ucas.busbook.Utils.NewIntent;
import com.ucas.busbook.Utils.TextUtil;
import com.ucas.busbook.dialog.APPDialog;
import com.ucas.busbook.dialog.LoadingDialog;
import com.ucas.busbook.network.Login;
import com.ucas.busbook.network.NetConnection;

public class LoginActivity extends Activity implements OnClickListener {

	private EditText studentId;
	private EditText password;
	private ImageButton backButton;
	private TextView title;
	private String caller;

	public static LoginActivity instance = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		instance = this;
		initView();
	}

	public void initView() {
		title = (TextView) findViewById(R.id.title_name);
		backButton = (ImageButton) findViewById(R.id.left_btn);

		studentId = (EditText) findViewById(R.id.login_account_id);
		password = (EditText) findViewById(R.id.login_account_pswd);

		backButton.setVisibility(View.VISIBLE);
		title.setText(R.string.login_title);
		title.setVisibility(View.VISIBLE);
		backButton.setOnClickListener(this);

		findViewById(R.id.login_btn).setOnClickListener(this);
		findViewById(R.id.forget_pswd).setOnClickListener(this);

		Intent intent = getIntent();
		caller = intent.getStringExtra("caller");
		System.out.println(caller);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_btn: {
			if (TextUtils.isEmpty(studentId.getText().toString())) {
				Toast.makeText(LoginActivity.this, R.string.student_id_can_not_be_empty, Toast.LENGTH_LONG).show();
				studentId.requestFocus();
				return;
			}
			if (TextUtils.isEmpty(password.getText())) {
				Toast.makeText(LoginActivity.this, R.string.password_can_not_be_empty, Toast.LENGTH_LONG).show();
				password.requestFocus();
				return;
			}
			if (!TextUtil.isNumber(password.getText().toString())) {
				Toast.makeText(LoginActivity.this, R.string.password_format_is_wrong, Toast.LENGTH_LONG).show();
				password.requestFocus();
				return;
			}
			if (!NetConnection.isNetWorkConnected(LoginActivity.this)) {
				ErrorUtils.networkOrActionError(Configs.ERROR_NETWORK_NOT_CONNECT, LoginActivity.this);
				return;
			}
			final LoadingDialog loading = APPDialog.showLoadingDialog(LoginActivity.this,
					getResources().getString(R.string.logining));
			new Login(studentId.getText().toString(), MD5Tool.md5(password.getText().toString()),
					new SuccessCallback() {

						@Override
						public void onSuccess(String... result) {
							loading.dismiss();
							Configs.cacheStudentId(LoginActivity.this, studentId.getText().toString());
							Configs.cacheToken(LoginActivity.this, result[0]);
							Configs.cacheStudentName(LoginActivity.this, result[1]);
							Configs.cachePhoneNum(LoginActivity.this, result[2]);
							Configs.isLogin = true;
							// if (caller.equals("MAIN")) {
							// Intent intent = new Intent(LoginActivity.this,
							// MainActivity.class);
							//
							// LoginActivity.this.setResult(RESULT_OK, intent);
							// LoginActivity.this.startActivity(intent);
							// LoginActivity.this.finish();
							// } else {
							// Intent intent = new Intent(LoginActivity.this,
							// UsercenterActivity.class);
							// LoginActivity.this.startActivity(intent);
							// LoginActivity.this.finish();
							// }
							onBackPressed();
						}
					}, new FailCallback() {

						@Override
						public void onFail(int errorCode) {
							loading.dismiss();
							ErrorUtils.networkOrActionError(errorCode, LoginActivity.this);
						}
					});

		}

			break;
		case R.id.left_btn: {
			onBackPressed();
			// if (caller.equals("MAIN")) {
			// Intent intent = new Intent();
			// intent.setClass(LoginActivity.this, MainActivity.class);
			// LoginActivity.this.startActivity(intent);
			// LoginActivity.this.finish();
			// } else {
			// Intent intent = new Intent();
			// intent.setClass(LoginActivity.this, UsercenterActivity.class);
			// LoginActivity.this.startActivity(intent);
			// LoginActivity.this.finish();
			// }

		}
			break;
		case R.id.forget_pswd: {
			NewIntent.startNewActivity(LoginActivity.this, InputCertActivity.class);
		}
			break;

		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_BACK) {
	// Intent intent = new Intent();
	// intent.setClass(LoginActivity.this, UsercenterActivity.class);
	// startActivity(intent);
	// LoginActivity.this.finish();
	// return true;
	// } else {
	// return super.onKeyDown(keyCode, event);
	// }
	//
	// }

}
