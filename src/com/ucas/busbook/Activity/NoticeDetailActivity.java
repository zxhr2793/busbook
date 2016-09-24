package com.ucas.busbook.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ucas.busbook.R;

public class NoticeDetailActivity extends Activity {
	private TextView activity_title;
	private ImageButton back;
	private TextView title;
	private TextView time;
	private TextView content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notice_details);
		initViews();
		initData();
	}

	private void initData() {
		String mTitle = getIntent().getStringExtra("title");
		String mTime = getIntent().getStringExtra("time");
		String mContent = getIntent().getStringExtra("content");
		activity_title.setText(R.string.message_view);
		title.setText(mTitle);
		time.setText(mTime);
		content.setText(mContent);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	private void initViews() {
		activity_title = (TextView) findViewById(R.id.title_name);
		back = (ImageButton) findViewById(R.id.left_btn);
		title = (TextView) findViewById(R.id.selected_message_title);
		time = (TextView) findViewById(R.id.selected_message_time);
		content = (TextView) findViewById(R.id.selected_message_content);
		activity_title.setVisibility(View.VISIBLE);
		back.setVisibility(View.VISIBLE);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

}
