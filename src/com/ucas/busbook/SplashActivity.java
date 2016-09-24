package com.ucas.busbook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.ucas.busbook.network.GetSeverConfig;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_splash);
		GetSeverConfig.getSeverConfig(this);
		if (Configs.getCachedToken(this) != null) {
			Configs.isLogin = true;
		}
		startMain();
	}

	private void startMain() {
		new Handler().postDelayed(new Runnable() {
			public void run() {
				Intent intent = new Intent(SplashActivity.this,
						MainActivity.class);
				startActivity(intent);
				SplashActivity.this.finish();
			}
		}, 2900);
	}

}
