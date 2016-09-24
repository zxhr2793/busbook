package com.ucas.busbook.Activity;

import com.ucas.busbook.Configs;
import com.ucas.busbook.R;
import com.ucas.busbook.Utils.Callback.FailCallback;
import com.ucas.busbook.Utils.Callback.SuccessCallback;
import com.ucas.busbook.Utils.CyptoUtils;
import com.ucas.busbook.Utils.ErrorUtils;
import com.ucas.busbook.Utils.MD5Tool;
import com.ucas.busbook.Utils.NewIntent;
import com.ucas.busbook.Utils.TextUtil;
import com.ucas.busbook.dialog.APPDialog;
import com.ucas.busbook.dialog.LoadingDialog;
import com.ucas.busbook.network.ChangePassword;
import com.ucas.busbook.network.NetConnection;
import com.ucas.busbook.widget.APPToast;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class ChangePswdActivity extends Activity implements OnClickListener {

	private TextView title;
	private ImageButton backButton;
	private EditText curPswd, newPswd, verifyPswd;
	private Button okButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_pswd);
		initView();
	}

	public void initView() {
		title = (TextView) findViewById(R.id.title_name);
		curPswd = (EditText) findViewById(R.id.change_account_curpswd);
		newPswd = (EditText) findViewById(R.id.change_account_newpswd);
		verifyPswd = (EditText) findViewById(R.id.change_account_newpswd_verify);

		okButton = (Button) findViewById(R.id.change_btn);
		backButton = (ImageButton) findViewById(R.id.left_btn);

		title.setVisibility(View.VISIBLE);
		title.setText("修改密码");
		backButton.setVisibility(View.VISIBLE);

		backButton.setOnClickListener(this);
		okButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_btn: {
			Intent intent = new Intent(ChangePswdActivity.this, UsercenterActivity.class);
			startActivity(intent);
			ChangePswdActivity.this.finish();
		}

			break;
		case R.id.change_btn: {
			if (TextUtils.isEmpty(curPswd.getText())) {
				APPToast.showToastShort(ChangePswdActivity.this, R.string.input_current_pswd);
				curPswd.requestFocus();
				return;
			}
			if (TextUtils.isEmpty(newPswd.getText())) {
				APPToast.showToastShort(ChangePswdActivity.this, R.string.input_new_pswd);
				newPswd.requestFocus();
				return;
			}
			if (TextUtils.isEmpty(verifyPswd.getText())) {
				APPToast.showToastShort(ChangePswdActivity.this, R.string.input_new_pswd_verify);
				verifyPswd.requestFocus();
				return;
			}
			if (!TextUtil.isNumber(newPswd.getText())) {
				APPToast.showToastShort(ChangePswdActivity.this, R.string.error_pswd_format);
				newPswd.requestFocus();
				return;
			}
			if (!verifyPswd.getText().toString().equals(newPswd.getText().toString())) {
				APPToast.showToastShort(ChangePswdActivity.this, R.string.error_pswd_disconsist);
				verifyPswd.requestFocus();
				return;
			}

			if (!NetConnection.isNetWorkConnected(ChangePswdActivity.this)) {
				ErrorUtils.networkOrActionError(Configs.ERROR_NETWORK_NOT_CONNECT, ChangePswdActivity.this);
				return;
			}
			String curpswd = curPswd.getText().toString();
			String newpswd = newPswd.getText().toString();
			String studentId = Configs.getCachedStudentId(ChangePswdActivity.this);
			String token = Configs.getCachedToken(ChangePswdActivity.this);
			final LoadingDialog loadingDialog = APPDialog.showLoadingDialog(ChangePswdActivity.this,
					getResources().getString(R.string.change_submitting));
			new ChangePassword(studentId, token, MD5Tool.md5(curpswd), CyptoUtils.encode("tqkcmt4ever", newpswd),
					new SuccessCallback() {

						@Override
						public void onSuccess(String... result) {
							loadingDialog.dismiss();
							Configs.clearUserInfo(ChangePswdActivity.this);
							Configs.isLogin = false;
							APPDialog.showLoginDialog(ChangePswdActivity.this);
						}
					}, new FailCallback() {

						@Override
						public void onFail(int errorCode) {
							loadingDialog.dismiss();
							if (errorCode == Configs.RESULT_STATUS_INVALID_PASSWORD) {
								APPToast.showToastShort(ChangePswdActivity.this, R.string.cur_invalid_password);
								curPswd.requestFocus();
							} else {
								ErrorUtils.networkOrActionError(errorCode, ChangePswdActivity.this);
							}
						}
					});
		}

		default:
			break;
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			NewIntent.startNewActivity(ChangePswdActivity.this, UsercenterActivity.class);
			ChangePswdActivity.this.finish();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}

}
