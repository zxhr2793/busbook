package com.ucas.busbook.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ucas.busbook.R;
import com.ucas.busbook.Utils.NewIntent;
import com.ucas.busbook.fragment.UnUsedTicketFragment;
import com.ucas.busbook.fragment.UsedTicketFragment;

public class OrderManageActivity extends FragmentActivity {

	private ImageButton backButton;
	private TextView title_name;

	private RadioGroup ticketGroup;
	private FragmentManager fragmentManager;
	private UnUsedTicketFragment unused;
	private UsedTicketFragment used;
	RelativeLayout relativeLayout;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_manage);
		initView();
	}

	private void initView() {
		backButton = (ImageButton) findViewById(R.id.left_btn);
		title_name = (TextView) findViewById(R.id.title_name);

		ticketGroup = (RadioGroup) findViewById(R.id.order_manage_ticket_info_rg);

		fragmentManager = getSupportFragmentManager();

		unused = new UnUsedTicketFragment();
		fragmentManager.beginTransaction().replace(R.id.content, unused)
				.commit();

		// mFragments[0] = fragmentManager
		// .findFragmentById(R.id.order_manage_unused_fragment);
		// mFragments[1] = fragmentManager
		// .findFragmentById(R.id.order_manage_used_fragment);
		// mFragments[1].setUserVisibleHint(false);
		// mFragments[1].onStop();
		// fragmentTransaction = fragmentManager.beginTransaction()
		// .hide(mFragments[0]).hide(mFragments[1]);
		// fragmentTransaction.show(mFragments[0]).commit();

		ticketGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						checkedChange(checkedId);
					}
				});

		backButton.setVisibility(View.VISIBLE);

		title_name.setText(R.string.booked_ticket);
		title_name.setVisibility(View.VISIBLE);

		backButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
//				Intent intent = new Intent();
//				intent.setClass(getApplicationContext(),
//						UsercenterActivity.class);
//				startActivity(intent);
//				OrderManageActivity.this.finish();
				onBackPressed();
			}
		});
		relativeLayout = (RelativeLayout) findViewById(R.id.delete_order_rl);
		unused.setRelativeLayout(relativeLayout);
	}

	protected void checkedChange(int checkedId) {

		// fragmentTransaction = fragmentManager.beginTransaction()
		// .hide(mFragments[0]).hide(mFragments[1]);

		switch (checkedId) {
		case R.id.order_manage_unused_order_rb:
			fragmentManager.beginTransaction().replace(R.id.content, unused)
					.commit();
			// mFragments[0].setUserVisibleHint(true);
			// mFragments[1].setUserVisibleHint(false);
			// mFragments[1].onStop();
			// mFragments[0].onStart();
			// fragmentTransaction.show(mFragments[0]).commit();
			
			break;
		case R.id.order_manage_used_order_rb:
			if (used == null) {
				used = new UsedTicketFragment();
			}
			fragmentManager.beginTransaction().replace(R.id.content, used)
					.commit();
			// mFragments[1].setUserVisibleHint(true);
			// mFragments[0].setUserVisibleHint(false);
			// mFragments[0].onStop();
			// mFragments[1].onStart();
			// fragmentTransaction.show(mFragments[1]).commit();
			relativeLayout.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			NewIntent.startNewActivity(OrderManageActivity.this,
//					UsercenterActivity.class);
//			OrderManageActivity.this.finish();
//			return true;
//		} else {
//			return super.onKeyDown(keyCode, event);
//		}
//	}

	
}
