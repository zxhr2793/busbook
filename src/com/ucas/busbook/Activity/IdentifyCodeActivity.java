package com.ucas.busbook.Activity;

import org.json.JSONException;
import org.json.JSONObject;

import com.ucas.busbook.Configs;
import com.ucas.busbook.R;
import com.ucas.busbook.Utils.TextUtil;
import com.ucas.busbook.dialog.APPDialog;
import com.ucas.busbook.dialog.CommonDialog;
import com.ucas.busbook.dialog.LoadingDialog;
import com.ucas.busbook.widget.APPToast;
import com.ucas.busbook.widget.ExtendedEditText;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.SMSReceiver;
import cn.smssdk.utils.SMSLog;

public class IdentifyCodeActivity extends Activity implements OnClickListener {
	private static final int RETRY_INTERVAL = 60;
	private String phone;
	private String code;
	private String formatedPhone;
	private String certno;
	private int time = RETRY_INTERVAL;
	private EventHandler handler;

	private ImageButton back;
	private ExtendedEditText etIdentifyNum;
	private TextView title;
	private TextView tvPhone;
	private TextView tvIdentifyNotify;
	private TextView tvUnreceiveIdentify, tvWrongphone;
	private Button btnSubmit;
	private BroadcastReceiver smsReceiver;
	private LoadingDialog loading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_identify_code);
		initView();
		initData();
	}

	private void initData() {
		phone = getIntent().getStringExtra("phone");
		code = getIntent().getStringExtra("code");
		formatedPhone = getIntent().getStringExtra("formatedPhone");
		certno = getIntent().getStringExtra("certno");
		tvIdentifyNotify.setText(Html.fromHtml(getResources().getString(R.string.smssdk_send_mobile_detail)));
		title.setText("填写验证码");
		tvPhone.setText(formatedPhone);
		String unReceive = IdentifyCodeActivity.this.getString(R.string.smssdk_receive_msg, time);
		tvUnreceiveIdentify.setText(Html.fromHtml(unReceive));

		back.setOnClickListener(this);
		btnSubmit.setOnClickListener(this);
		tvUnreceiveIdentify.setOnClickListener(this);
		tvWrongphone.setOnClickListener(this);
		tvUnreceiveIdentify.setEnabled(false);

		SMSSDK.initSDK(this, Configs.MOB_APP_KEY, Configs.MOB_APP_SECRET);
		handler = new EventHandler() {
			public void afterEvent(int event, int result, Object data) {
				if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
					/** 提交验证码 进行验证 */
					afterSubmit(result, data);
				} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
					/** 获取验证码成功后的执行动作 */
					afterGet(result, data);
				}
			}
		};
		SMSSDK.registerEventHandler(handler);
		countDown();
		smsReceiver = new SMSReceiver(new SMSSDK.VerifyCodeReadListener() {
			@Override
			public void onReadVerifyCode(final String verifyCode) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						etIdentifyNum.setText(verifyCode);
					}
				});
			}
		});
		IdentifyCodeActivity.this.registerReceiver(smsReceiver,
				new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
	}

	private void initView() {
		back = (ImageButton) findViewById(R.id.left_btn);
		title = (TextView) findViewById(R.id.title_name);
		etIdentifyNum = (ExtendedEditText) findViewById(R.id.et_put_identify);
		tvPhone = (TextView) findViewById(R.id.tv_phone);
		tvIdentifyNotify = (TextView) findViewById(R.id.tv_identify_notify);
		tvUnreceiveIdentify = (TextView) findViewById(R.id.tv_unreceive_identify);
		btnSubmit = (Button) findViewById(R.id.btn_submit);
		tvWrongphone = (TextView) findViewById(R.id.tv_phone_wrong);
		back.setVisibility(View.VISIBLE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_btn:
			showNotifyDialog();
			break;
		case R.id.btn_submit: {
			String verificationCode = etIdentifyNum.getText().toString().trim();
			if (TextUtil.isEmpty(verificationCode)) {
				APPToast.showToastShort(IdentifyCodeActivity.this, R.string.verificationCode_cannot_be_empty);
				return;
			}
			loading = APPDialog.showLoadingDialog(IdentifyCodeActivity.this,getResources().getString(R.string.verifing));
//			loading.show();
			SMSSDK.submitVerificationCode(code, phone, verificationCode);
		}
			break;

		case R.id.tv_unreceive_identify: {
			tvUnreceiveIdentify.setEnabled(false);
			loading = APPDialog.showLoadingDialog(IdentifyCodeActivity.this,getResources().getString(R.string.please_waitting));
			// 重新获取验证码短信
			SMSSDK.getVerificationCode(code, phone.trim(), null);
		}
			break;
		case R.id.tv_phone_wrong: {
			APPDialog.showWarnningDialog(IdentifyCodeActivity.this, R.string.contact_service, R.string.dialog_ok_btn);
		}
			break;

		default:
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		SMSSDK.unregisterEventHandler(handler);
		IdentifyCodeActivity.this.unregisterReceiver(smsReceiver);
	}

	/** 倒数计时 */
	private void countDown() {
		new Handler().postDelayed(new Runnable() {
			public void run() {
				time--;
				if (time == 0) {
					String unReceive = IdentifyCodeActivity.this.getString(R.string.smssdk_unreceive_identify_code,
							time);
					tvUnreceiveIdentify.setText(Html.fromHtml(unReceive));
					tvUnreceiveIdentify.setEnabled(true);
					time = RETRY_INTERVAL;
				} else {
					String unReceive = IdentifyCodeActivity.this.getString(R.string.smssdk_receive_msg, time);
					tvUnreceiveIdentify.setText(Html.fromHtml(unReceive));
					tvUnreceiveIdentify.setEnabled(false);
					new Handler().postDelayed(this, 1000);
				}
			}
		}, 1000);
	}

	/**
	 * 提交验证码成功后的执行事件
	 *
	 * @param result
	 * @param data
	 */
	private void afterSubmit(final int result, final Object data) {
		runOnUiThread(new Runnable() {
			public void run() {
				if (loading != null && loading.isShowing()) {
					loading.dismiss();
				}
				if (result == SMSSDK.RESULT_COMPLETE) {
					Intent intent = new Intent(IdentifyCodeActivity.this, ResetPswdActivity.class);
					intent.putExtra("phone", phone);
					intent.putExtra("certno", certno);
					IdentifyCodeActivity.this.startActivity(intent);
					IdentifyCodeActivity.this.finish();
				} else {
					((Throwable) data).printStackTrace();
					// 验证码不正确
					APPToast.showToastShort(IdentifyCodeActivity.this, R.string.smssdk_virificaition_code_wrong);
				}
			}
		});
	}

	/**
	 * 获取验证码成功后,的执行动作
	 *
	 * @param result
	 * @param data
	 */
	private void afterGet(final int result, final Object data) {
		runOnUiThread(new Runnable() {
			public void run() {
				if (loading != null && loading.isShowing()) {
					loading.dismiss();
				}
				if (result == SMSSDK.RESULT_COMPLETE) {
					Toast.makeText(IdentifyCodeActivity.this, R.string.smssdk_virificaition_code_sent,
							Toast.LENGTH_SHORT).show();
					String unReceive = IdentifyCodeActivity.this.getString(R.string.smssdk_receive_msg, time);
					tvUnreceiveIdentify.setText(Html.fromHtml(unReceive));
					time = RETRY_INTERVAL;
					countDown();
				} else {
					((Throwable) data).printStackTrace();
					Throwable throwable = (Throwable) data;
					// 根据服务器返回的网络错误，给toast提示
					try {
						JSONObject object = new JSONObject(throwable.getMessage());
						String des = object.optString("detail");
						if (!TextUtils.isEmpty(des)) {
							Toast.makeText(IdentifyCodeActivity.this, des, Toast.LENGTH_SHORT).show();
							return;
						}
					} catch (JSONException e) {
						SMSLog.getInstance().w(e);
					}
					// / 如果木有找到资源，默认提示
					APPToast.showToastShort(IdentifyCodeActivity.this, R.string.smssdk_network_error);
				}
			}
		});
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showNotifyDialog();
			return true;
		} else {
			return false;
		}
	}	

	private void showNotifyDialog() {
		final CommonDialog dialog = new CommonDialog(IdentifyCodeActivity.this, R.style.customDialog);
		dialog.show();
		dialog.setMessage(getResources().getString(R.string.smssdk_close_identify_page_dialog));
		dialog.setPositiveButton(getResources().getString(R.string.smssdk_wait), new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				
			}
		});
		dialog.setNegativeButton(getResources().getString(R.string.smssdk_back), new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				onBackPressed();
				IdentifyCodeActivity.this.finish();
			}
		});
	}

}
