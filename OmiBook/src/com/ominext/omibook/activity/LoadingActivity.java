package com.ominext.omibook.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.widget.Toast;

import com.ominext.omibook.utils.Utils;

public class LoadingActivity extends Activity {
	private Context mContext = this;
	private Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading);
		if(Utils.is3gOrWifiAvaiable(this.mContext)){
		this.mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
                LoadingActivity.this.nextMainActivity();
			}
		}, 3000);
		}
		else {
			Toast.makeText(getApplicationContext(), "Network is error", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void nextMainActivity() {
		startActivity(new Intent(getApplicationContext(), MainActivity.class));
		finish();
	}
}
