package com.ucas.busbook.Activity;

import org.json.JSONException;
import org.json.JSONObject;

import com.ucas.busbook.Configs;
import com.ucas.busbook.R;
import com.ucas.busbook.Utils.ErrorUtils;
import com.ucas.busbook.Utils.TextUtil;
import com.ucas.busbook.dialog.APPDialog;
import com.ucas.busbook.dialog.LoadingDialog;
import com.ucas.busbook.network.HttpMethod;
import com.ucas.busbook.network.NetConnection;
import com.ucas.busbook.network.NetConnection.FailCallback;
import com.ucas.busbook.network.NetConnection.SuccessCallback;
import com.ucas.busbook.widget.APPToast;
import com.ucas.busbook.widget.ExtendedEditText;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.UserInterruptException;
import cn.smssdk.utils.SMSLog;

public class InputCertActivity extends Activity implements OnClickListener {
	private ImageButton back;
	private TextView title;
	private Button btn_certno_next, btn_phone_next;
	private ExtendedEditText et_cert, et_phone;
	private EventHandler eventHandler;
	private String phoneNum;
	private String code = "86";
	private String stuid;
	private LoadingDialog progress;
	public static InputCertActivity instance = null;
	// private ProgressBar progress;
//	private OnSendMessageHandler osmHandler;
//	public void setOnSendMessageHandler(OnSendMessageHandler h) {
//		osmHandler = h;
//	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_input_cert);
		instance = this;
		initView();
		initData();
	}

	private void initView() {
		back = (ImageButton) findViewById(R.id.left_btn);
		title = (TextView) findViewById(R.id.title_name);
		// webView = (WebView) findViewById(R.id.webView);
		// progress = (ProgressBar) findViewById(R.id.loading_progress);
		btn_certno_next = (Button) findViewById(R.id.input_certno_btn_next);
		btn_phone_next = (Button) findViewById(R.id.input_phone_btn_next);
		et_cert = (ExtendedEditText) findViewById(R.id.et_input_certno);
		et_phone = (ExtendedEditText) findViewById(R.id.et_input_phone);
		back.setVisibility(View.VISIBLE);
		title.setVisibility(View.VISIBLE);
		title.setText("找回密码");
		btn_certno_next.setOnClickListener(this);
		btn_phone_next.setOnClickListener(this);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		
	}

	private void initData() {
		// 初始化短信SDK
		SMSSDK.initSDK(this, Configs.MOB_APP_KEY, Configs.MOB_APP_SECRET);

		eventHandler = new EventHandler() {
			public void afterEvent(int event, int result, Object data) {
				if (result == SMSSDK.RESULT_COMPLETE) {
					if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
						// 请求验证码后，跳转到验证码填写页面
						if (progress!=null) {
							progress.dismiss();
						}
						afterVerificationCodeRequested(code,phoneNum);
					}
				} else {
					if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE
							&& data != null
							&& (data instanceof UserInterruptException)) {
						// 由于此处是开发者自己决定要中断发送的，因此什么都不用做
						return;
					}

					// 根据服务器返回的网络错误，给toast提示
					try {
						((Throwable) data).printStackTrace();
						Throwable throwable = (Throwable) data;

						JSONObject object = new JSONObject(
								throwable.getMessage());
						String des = object.optString("detail");
						if (!TextUtils.isEmpty(des)) {
							Toast.makeText(InputCertActivity.this, des,
									Toast.LENGTH_SHORT).show();
							return;
						}
					} catch (Exception e) {
						SMSLog.getInstance().w(e);
					}

				}
			}
		};
		SMSSDK.registerEventHandler(eventHandler);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.input_certno_btn_next: {
			String certno = et_cert.getText().toString().trim();
			if (TextUtil.isEmpty(certno)) {
				APPToast.showToastShort(InputCertActivity.this,
						R.string.student_id_can_not_be_empty);
				return;
			}
			stuid = certno;
			// final LoadingDialog dialog =
			// APPDialog.showLoadingDialog(InputCertActivity.this, "请稍后...");
			progress = APPDialog.showLoadingDialog(InputCertActivity.this,
					getResources().getString(R.string.please_waitting));
			// dialog.show();
			new NetConnection(
					Configs.SERVER_URL + "/UserAction",
					HttpMethod.POST,
					new SuccessCallback() {

						@Override
						public void onSuccess(String result) {
							// dialog.dismiss();
							try {
								JSONObject jsonObject = new JSONObject(result);
								switch (jsonObject.getInt(Configs.KEY_STATUS)) {
								case Configs.RESULT_STATUS_SUCCESS: {
									String phone = jsonObject
											.getString("phone");
									if (TextUtil.isEmpty(phone)) {
										if (progress != null
												&& progress.isShowing()) {
											progress.dismiss();
										}
										findViewById(R.id.line_input_certno)
												.setVisibility(View.GONE);
										findViewById(R.id.line_input_phone_warn)
												.setVisibility(View.VISIBLE);
										findViewById(R.id.line_input_phone)
												.setVisibility(View.VISIBLE);
										findViewById(R.id.input_certno_btn_next)
												.setVisibility(View.GONE);
										findViewById(R.id.input_phone_btn_next)
												.setVisibility(View.VISIBLE);
										return;
									}
									phoneNum = phone;
									// progress =
									// APPDialog.showLoadingDialog(InputCertActivity.this,
									// "请稍后...");
									SMSSDK.getVerificationCode(code,
											phone.trim());
								}
									break;

								default:
									if (progress != null
											&& progress.isShowing()) {
										progress.dismiss();
									}
									ErrorUtils.networkOrActionError(jsonObject
											.getInt(Configs.KEY_STATUS),
											InputCertActivity.this);
									break;
								}
							} catch (JSONException e) {
								if (progress != null && progress.isShowing()) {
									progress.dismiss();
								}
								e.printStackTrace();
							}
						}
					}, new FailCallback() {

						@Override
						public void onFail(int errorCode) {
							if (progress != null && progress.isShowing()) {
								progress.dismiss();
							}
							ErrorUtils.networkOrActionError(errorCode,
									InputCertActivity.this);
						}
					}, Configs.KEY_ACTION, Configs.ACTION_GET_PHNOE,
					Configs.KEY_ACCOUNT_ID, certno);
		}

			break;
		case R.id.input_phone_btn_next: {
			String phone = et_phone.getText().toString().trim();
			if (TextUtil.isEmpty(phone)) {
				APPToast.showToastShort(InputCertActivity.this,
						R.string.phone_number_can_not_be_empty);
				return;
			}
			if (!TextUtil.isMobile(phone)) {
				APPToast.showToastShort(InputCertActivity.this,
						R.string.error_phone_number);
				return;
			}
			phoneNum = phone;
			progress = APPDialog.showLoadingDialog(InputCertActivity.this,
					getResources().getString(R.string.please_waitting));
			SMSSDK.getVerificationCode(code, phone.trim());
		}
			break;

		default:
			break;
		}
	}

	/**
	 * Handler handler = new Handler() {
	 * 
	 * @Override public void handleMessage(Message msg) {
	 *           super.handleMessage(msg); int event = msg.arg1; int result =
	 *           msg.arg2; Object data = msg.obj; Log.e("event", "event=" +
	 *           event); if (result == SMSSDK.RESULT_COMPLETE) { if (event ==
	 *           SMSSDK.EVENT_GET_VERIFICATION_CODE) { // 请求验证码后，跳转到验证码填写页面
	 *           afterVerificationCodeRequested(code,phoneNum); } } else { if
	 *           (event == SMSSDK.EVENT_GET_VERIFICATION_CODE && data != null &&
	 *           (data instanceof UserInterruptException)) { //
	 *           由于此处是开发者自己决定要中断发送的，因此什么都不用做 return; }
	 * 
	 *           // 根据服务器返回的网络错误，给toast提示 try { ((Throwable)
	 *           data).printStackTrace(); Throwable throwable = (Throwable)
	 *           data;
	 * 
	 *           JSONObject object = new JSONObject(throwable.getMessage());
	 *           String des = object.optString("detail"); if
	 *           (!TextUtils.isEmpty(des)) {
	 *           Toast.makeText(InputCertActivity.this, des,
	 *           Toast.LENGTH_SHORT).show(); return; } } catch (Exception e) {
	 *           SMSLog.getInstance().w(e); }
	 * 
	 *           } }
	 * 
	 *           };
	 */

	/** 请求验证码后，跳转到验证码填写页面 */
	private void afterVerificationCodeRequested(String code, String phone) {
		if (code.startsWith("+")) {
			code = code.substring(1);
		}
		String formatedPhone = "+" + code + " " + TextUtil.splitPhoneNum(phone);
		// 验证码页面
		// IdentifyNumPage page = new IdentifyNumPage();
		// page.setPhone(phone, code, formatedPhone);
		// page.showForResult(activity, null, this);
		Intent intent = new Intent(InputCertActivity.this,
				IdentifyCodeActivity.class);
		intent.putExtra("phone", phoneNum);
		intent.putExtra("code", code);
		intent.putExtra("formatedPhone", formatedPhone);
		intent.putExtra("certno", stuid);
		InputCertActivity.this.startActivity(intent);
		// InputCertActivity.this.finish();
	}

	public void onResume() {
		SMSSDK.registerEventHandler(eventHandler);
		super.onResume();
	}

	public void onPause() {
		SMSSDK.unregisterEventHandler(eventHandler);
		super.onPause();
	}

}
