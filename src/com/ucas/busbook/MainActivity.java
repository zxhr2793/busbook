package com.ucas.busbook;

import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ucas.busbook.Activity.LoginActivity;
import com.ucas.busbook.Activity.NoticeCenterActivity;
import com.ucas.busbook.Activity.UpdateManager;
import com.ucas.busbook.Activity.UsercenterActivity;
import com.ucas.busbook.Utils.Callback.FailCallback;
import com.ucas.busbook.Utils.Callback.SuccessCallback;
import com.ucas.busbook.Utils.DateTimeUtils;
import com.ucas.busbook.Utils.ErrorUtils;
import com.ucas.busbook.Utils.NewIntent;
import com.ucas.busbook.Utils.TextUtil;
import com.ucas.busbook.dialog.APPDialog;
import com.ucas.busbook.dialog.CommonDialog;
import com.ucas.busbook.dialog.CustomDatePickerDialog;
import com.ucas.busbook.dialog.LoadingDialog;
import com.ucas.busbook.network.BookTicket;
import com.ucas.busbook.network.GetSeverConfig;
import com.ucas.busbook.network.NetConnection;
import com.ucas.busbook.network.QueryRestTicket;

public class MainActivity extends Activity implements View.OnClickListener {

	private RotateAnimation ra;
	private TextView source;
	private TextView desti;
	private Button okButton;
	private TextView title;
	private ImageButton account;
	private ImageButton closebar;
	private TextView takeDate;
	private TextView weekDay;
	private TextView restNum;
	private EditText phoneNum;
	final int LOGIN_CODE = 0x01;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		initData();
		queryTicket();
	}

	private void initView() {
		title = (TextView) findViewById(R.id.title_name);
		account = (ImageButton) findViewById(R.id.right_btn);
		source = (TextView) findViewById(R.id.source);
		desti = (TextView) findViewById(R.id.destination);
		okButton = (Button) findViewById(R.id.book);
		takeDate = (TextView) findViewById(R.id.tv_date);
		weekDay = (TextView) findViewById(R.id.tv_week);
		phoneNum = (EditText) findViewById(R.id.ph_number);
		restNum = (TextView) findViewById(R.id.ticketnum);
		closebar = (ImageButton) findViewById(R.id.notice_bar_close);

		title.setText("车票预约");
		account.setVisibility(View.VISIBLE);

		okButton.setOnClickListener(this);
		account.setOnClickListener(this);
		closebar.setOnClickListener(this);
		findViewById(R.id.exchange).setOnClickListener(this);
		findViewById(R.id.date_layout).setOnClickListener(this);
		findViewById(R.id.notice_message).setOnClickListener(this);
	}

	public void initData() {
		if (Configs.isLogin()) {
			account.setImageResource(R.drawable.login_account_selector);
		} else {
			account.setImageResource(R.drawable.account_selector);
		}

		if (Configs.checkUpdate() && Configs.firstCheckUp()) {
			new UpdateManager(MainActivity.this, false).checkUpdate();
			Configs.isFirstCheckUp = false;
		}

		String cachedNum = Configs.getCachedPhoneNum(MainActivity.this);
		if (cachedNum != null) {
			phoneNum.setText(cachedNum);
		}

		String severDate = Configs.getSeverDate(MainActivity.this);

		takeDate.setText(severDate);
		weekDay.setText(DateTimeUtils.getWeekDay(DateTimeUtils
				.String2YearDate(severDate)));
		boolean show = Configs.getShowNotice(this);
		// System.out.println("---->>>>show message" + show);
		// System.out.println(Configs.hasLastest());
		if (show && Configs.hasLastest()) {
			findViewById(R.id.notice_bar).setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.notice_message:
			NewIntent.startNewActivity(MainActivity.this,
					NoticeCenterActivity.class);
			Configs.cacheShowNotice(MainActivity.this, false);
			findViewById(R.id.notice_bar).setVisibility(View.GONE);
			break;
		case R.id.notice_bar_close:
			Configs.cacheShowNotice(MainActivity.this, false);
			findViewById(R.id.notice_bar).setVisibility(View.GONE);
			break;
		case R.id.exchange: {
			ra = new RotateAnimation(0, 179, Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			ra.setDuration(200);
			ra.setFillAfter(true);// the state will be persist when the
									// animation
									// finish;
			v.startAnimation(ra);
			ra.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					String tempString;
					tempString = source.getText().toString();
					source.setText(desti.getText().toString());
					desti.setText(tempString);
					queryTicket();
				}
			});
		}
			break;
		case R.id.book: {
			if (Configs.getCachedStudentId(MainActivity.this) == null) {
				askForLogin();
				return;
			}

			if (!NetConnection.isNetWorkConnected(MainActivity.this)) {
				APPDialog.showSetNetworkDialog(MainActivity.this);
				return;
			}

			if (TextUtils.isEmpty(phoneNum.getText())) {
				Toast.makeText(MainActivity.this,
						R.string.phone_number_can_not_be_empty,
						Toast.LENGTH_LONG).show();
				return;
			}
			if (!TextUtil.isMobile(phoneNum.getText().toString())) {
				Toast.makeText(MainActivity.this, R.string.error_phone_number,
						Toast.LENGTH_LONG).show();
				return;
			}

			bookTicket();

		}
			break;

		case R.id.right_btn: {
			NewIntent.startNewActivity(MainActivity.this,
					UsercenterActivity.class);
			// MainActivity.this.finish();
		}
			break;

		case R.id.date_layout: {
			GetSeverConfig.getSeverConfig(this);

			CustomDatePickerDialog dialog = new CustomDatePickerDialog(
					MainActivity.this, R.style.customDialog,
					new CustomDatePickerDialog.CustomDialogEventListener() {
						@Override
						public void getSelectedDate(String selectedDate) {
							if (!selectedDate.equals("")) {
								Date date = DateTimeUtils
										.String2YearDate(selectedDate);
								takeDate.setText(selectedDate);
								weekDay.setText(DateTimeUtils.getWeekDay(date));
								queryTicket();
							}
						}
					});
			dialog.show();
		}
			break;
		default:
			break;
		}

	}

	public void askForLogin() {
		Intent intent = new Intent(MainActivity.this, LoginActivity.class);
		intent.putExtra("caller", "MAIN");
		MainActivity.this.startActivity(intent);
		// MainActivity.this.finish();
	}

	public void bookTicket() {
		final String tDate = takeDate.getText().toString();
		final String routeName = source.getText().toString() + "至"
				+ desti.getText().toString();
		final String token = Configs.getCachedToken(MainActivity.this);
		final String studentId = Configs.getCachedStudentId(MainActivity.this);
		if (restNum.getText().equals("0")) {
			ErrorUtils.networkOrActionError(Configs.RESULT_STATUS_BOOK_FAIL,
					MainActivity.this);
			return;
		}
		if (token == null || studentId == null) {
			askForLogin();
		}
		String tWeek = weekDay.getText().toString();
		Date date = DateTimeUtils.String2YearDate(tDate);
		String bDate = DateTimeUtils.YearDate2String_zh(date);
		String warnning = getResources().getString(R.string.book_warnning);
		String content = MainActivity.this.getString(R.string.book_warnning, bDate, tWeek, routeName);
		final CommonDialog dialog = new CommonDialog(MainActivity.this,
				R.style.customDialog);
		dialog.show();
		dialog.setMessage(Html.fromHtml(content));
		dialog.setPositiveButton(
				getResources().getString(R.string.dialog_ok_btn),
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
						bookNow(token, studentId, routeName, tDate);
					}
				});
		dialog.setNegativeButton(getResources().getString(R.string.cancel),
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});

	}

	private void bookNow(String token, String studentId, String routeName,
			String tDate) {
		String phone = phoneNum.getText().toString();
		String stuname = Configs.getCachedStudentName(MainActivity.this);
		final LoadingDialog dialog = APPDialog
				.showLoadingDialog(MainActivity.this);
		dialog.setMessage(getResources().getString(R.string.booking_ticket));
		new BookTicket(token, studentId, routeName, tDate, phone, stuname,
				new SuccessCallback() {

					@Override
					public void onSuccess(String... result) {
						dialog.dismiss();
						APPDialog.showWarnningDialog(MainActivity.this,
								R.string.success_book_ticket,
								R.string.dialog_ok_btn);
						int num = Integer
								.parseInt(restNum.getText().toString());
						restNum.setText(Integer.toString(num - 1));
					}
				}, new FailCallback() {
					@Override
					public void onFail(int errorCode) {
						dialog.dismiss();
						ErrorUtils.networkOrActionError(errorCode,
								MainActivity.this);
					}
				});
	}

	public void queryTicket() {
		String routeName = source.getText().toString() + "至"
				+ desti.getText().toString();
		String date = takeDate.getText().toString();
		System.out.println(routeName);
		System.out.println(date);
		final LoadingDialog dialog = APPDialog
				.showLoadingDialog(MainActivity.this);
		dialog.setMessage(getResources().getString(R.string.query_now));
		dialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				if (NetConnection.mytask != null) {
					NetConnection.mytask.cancel(true);
					// System.out.println("dialog cancel!");
				}
			}
		});
		new QueryRestTicket(routeName, date, new SuccessCallback() {

			@Override
			public void onSuccess(String... result) {
				dialog.dismiss();
				restNum.setText(result[0]);
			}
		}, new FailCallback() {
			@Override
			public void onFail(int errorCode) {
				dialog.dismiss();
				//restNum.setText("0");
				ErrorUtils.networkOrActionError(errorCode, MainActivity.this);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == LOGIN_CODE && resultCode == RESULT_OK) {
			System.out.println(requestCode);
			bookTicket();
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			APPDialog.showExitDialog(MainActivity.this);
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		mHandler.sendEmptyMessage(1);
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				initData();
			}
		}

	};

	@Override
	public Resources getResources() {
		Resources res = super.getResources();    
	    Configuration config=new Configuration();    
	    config.setToDefaults();    
	    res.updateConfiguration(config,res.getDisplayMetrics() );  
	    return res; 
	}	

}
