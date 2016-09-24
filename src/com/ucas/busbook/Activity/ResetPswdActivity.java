package com.ucas.busbook.Activity;

import org.json.JSONException;
import org.json.JSONObject;

import com.ucas.busbook.Configs;
import com.ucas.busbook.R;
import com.ucas.busbook.Utils.CyptoUtils;
import com.ucas.busbook.Utils.ErrorUtils;
import com.ucas.busbook.Utils.TextUtil;
import com.ucas.busbook.dialog.APPDialog;
import com.ucas.busbook.dialog.CommonDialog;
import com.ucas.busbook.dialog.LoadingDialog;
import com.ucas.busbook.network.HttpMethod;
import com.ucas.busbook.network.NetConnection;
import com.ucas.busbook.network.NetConnection.FailCallback;
import com.ucas.busbook.network.NetConnection.SuccessCallback;
import com.ucas.busbook.widget.APPToast;
import com.ucas.busbook.widget.ExtendedEditText;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class ResetPswdActivity extends Activity implements OnClickListener {
	private ImageButton back;
	private Button btn_reset;
	private TextView title;
	private ExtendedEditText et_newpswd, et_verifypswd;
	private String phone, certno;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reset_pswd);
		initView();
		initData();
	}

	private void initView() {
		back = (ImageButton) findViewById(R.id.left_btn);
		title = (TextView) findViewById(R.id.title_name);
		btn_reset = (Button) findViewById(R.id.reset_btn_submit);
		et_newpswd = (ExtendedEditText) findViewById(R.id.reset_account_newpswd);
		et_verifypswd = (ExtendedEditText) findViewById(R.id.reset_account_newpswd_verify);
		back.setVisibility(View.VISIBLE);
		title.setText("重置密码");
	}

	private void initData() {
		phone = getIntent().getStringExtra("phone");
		certno = getIntent().getStringExtra("certno");
		btn_reset.setOnClickListener(this);
		back.setOnClickListener(this);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_btn:
			onBackPressed();
			break;
		case R.id.reset_btn_submit: {
			String npswd = et_newpswd.getText().toString().trim();
			String npswdverify = et_verifypswd.getText().toString().trim();
			if (TextUtil.isEmpty(npswd)) {
				APPToast.showToastShort(ResetPswdActivity.this, R.string.input_new_pswd);
				et_newpswd.requestFocus();
				return;
			}
			if (TextUtil.isEmpty(npswdverify)) {
				APPToast.showToastShort(ResetPswdActivity.this, R.string.input_new_pswd_verify);
				et_verifypswd.requestFocus();
				return;
			}
			if (!TextUtil.isNumber(npswd)) {
				APPToast.showToastShort(ResetPswdActivity.this, R.string.error_pswd_format);
				et_newpswd.requestFocus();
				return;
			}
			if (!npswd.equals(npswdverify)) {
				APPToast.showToastShort(ResetPswdActivity.this, R.string.error_pswd_disconsist);
				et_verifypswd.requestFocus();
				return;
			}
			String encodepswd = CyptoUtils.encode("tqkcmt4ever", npswd);
			final LoadingDialog loadingDialog = APPDialog.showLoadingDialog(ResetPswdActivity.this,
					getResources().getString(R.string.change_submitting));
//			loadingDialog.show();
			System.out.println(phone);
			new NetConnection(Configs.SERVER_URL + "/UserAction", HttpMethod.POST, new SuccessCallback() {

				@Override
				public void onSuccess(String result) {
					loadingDialog.dismiss();
					try {
						JSONObject jsonObject = new JSONObject(result);
						switch (jsonObject.getInt(Configs.KEY_STATUS)) {
						case Configs.RESULT_STATUS_SUCCESS: {
							showLoginDialog();
						}

							break;

						default:
							ErrorUtils.networkOrActionError(Configs.RESULT_STATUS_FAIL, ResetPswdActivity.this);
							break;
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
			}, new FailCallback() {

				@Override
				public void onFail(int errorCode) {
					loadingDialog.dismiss();
					ErrorUtils.networkOrActionError(errorCode, ResetPswdActivity.this);
				}
			}, Configs.KEY_ACTION, Configs.KEY_RESET_PSWD, Configs.KEY_STUDENT_ID, certno, Configs.KEY_PHONE_NUM, phone,
					Configs.KEY_NEW_PASSWORD, encodepswd);
		}
			break;
		default:
			break;
		}
	}

	private void showLoginDialog() {
		final CommonDialog dialog = new CommonDialog(ResetPswdActivity.this, R.style.customDialog);
		dialog.show();
		dialog.setMessage(getResources().getString(R.string.ask_login_content));
		dialog.setPositiveButton(getResources().getString(R.string.login_right_now), new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if (!InputCertActivity.instance.isFinishing()) {
					InputCertActivity.instance.finish();
				}
				Configs.isLogin = false;
				onBackPressed();
			}
		});
		dialog.setNegativeButton(getResources().getString(R.string.cancel), new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if (!InputCertActivity.instance.isFinishing()) {
					InputCertActivity.instance.finish();
				}
				if (!LoginActivity.instance.isFinishing()) {
					LoginActivity.instance.finish();
				}
				onBackPressed();
			}
		});
	}

}
