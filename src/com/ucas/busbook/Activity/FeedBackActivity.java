package com.ucas.busbook.Activity;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ucas.busbook.Configs;
import com.ucas.busbook.R;
import com.ucas.busbook.Utils.ErrorUtils;
import com.ucas.busbook.network.HttpMethod;
import com.ucas.busbook.network.NetConnection;
import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.SyncListener;
import com.umeng.fb.model.Conversation;
import com.umeng.fb.model.Reply;

public class FeedBackActivity extends Activity implements View.OnClickListener,
		SyncListener {

	private TextView title;
	private ImageButton back;
	private EditText et_message;
	private FeedbackAgent agent;
	private Conversation defaultConversation;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);

		initView();
	}

	private void initView() {
		title = (TextView) findViewById(R.id.title_name);
		back = (ImageButton) findViewById(R.id.left_btn);

		et_message = (EditText) findViewById(R.id.feedback_message);
		findViewById(R.id.feedback_clear_btn).setOnClickListener(this);
		findViewById(R.id.feedback_commit_btn).setOnClickListener(this);

		title.setText(getResources().getString(R.string.feedback));

		back.setVisibility(View.VISIBLE);
		back.setOnClickListener(this);
		agent = new FeedbackAgent(this);
		// et_message.setSelection(1);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_btn: {
			// Intent intent = new Intent();
			// intent.setClass(this, UsercenterActivity.class);
			// startActivity(intent);
			// FeedBackActivity.this.finish();
			onBackPressed();
		}

			break;
		case R.id.feedback_clear_btn: {
			et_message.setText("");
		}
			break;

		case R.id.feedback_commit_btn: {
			if (et_message.length() == 0) {
				findViewById(R.id.feedback_warning).setVisibility(View.VISIBLE);
				return;
			} 
				findViewById(R.id.feedback_warning).setVisibility(
						View.INVISIBLE);


			
			String content = et_message.getText().toString();
			String model = android.os.Build.MODEL;
			String version = "Android " + android.os.Build.VERSION.RELEASE;
			System.out.println(model + " " + version);
			new NetConnection(Configs.SERVER_URL + "/FadebackAction", HttpMethod.POST,
					new NetConnection.SuccessCallback() {

						@Override
						public void onSuccess(String result) {
							try {
								JSONObject jsonObject = new JSONObject(result);
								switch (jsonObject.getInt(Configs.KEY_STATUS)) {
								case Configs.RESULT_STATUS_SUCCESS:
									defaultConversation = agent.getDefaultConversation();
									
									defaultConversation.addUserReply(et_message.getText()
											.toString());// 用户反馈意见
									
									defaultConversation.sync(FeedBackActivity.this);
									System.out.println("success");
									break;
								default:
									ErrorUtils.networkOrActionError(
											Configs.RESULT_STATUS_FAIL,
											FeedBackActivity.this);
									break;
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}

						}
					}, new NetConnection.FailCallback() {

						@Override
						public void onFail(int errorCode) {
							ErrorUtils.networkOrActionError(errorCode,
									FeedBackActivity.this);
						}
					}, "content", content, "model", model, "version", version);
		}
			break;

		default:
			break;
		}
	}

	@Override
	public void onReceiveDevReply(List<Reply> arg0) {

	}

	@Override
	public void onSendUserReply(List<Reply> arg0) {
		et_message.setText("");
		Toast.makeText(this,
				getResources().getString(R.string.feedback_success),
				Toast.LENGTH_LONG).show();
	}

	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_BACK) {
	// NewIntent.startNewActivity(FeedBackActivity.this,
	// UsercenterActivity.class);
	//
	// FeedBackActivity.this.finish();
	// return true;
	// } else {
	// return super.onKeyDown(keyCode, event);
	// }
	// }

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

}
