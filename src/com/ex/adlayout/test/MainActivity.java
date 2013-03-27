package com.ex.adlayout.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.menue.adlayout.ad.AdLayout;
import com.menue.adlayout.ad.util.Logger;

public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";

	Button bannerBtn, popBtn, IconBtn,fullscreenBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		bannerBtn = (Button) findViewById(R.id.BannerBtn);
		bannerBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				Intent intent = new Intent(MainActivity.this, BannerActivity.class);
				intent.putExtra("start_new", true);
				startActivity(intent);

			}
		});
		popBtn = (Button) findViewById(R.id.PopupBtn);
		popBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				Intent intent = new Intent(MainActivity.this, PopupActivity.class);
				intent.putExtra("start_new", true);
				startActivity(intent);
			}
		});
		IconBtn = (Button) findViewById(R.id.IconBtn);
		IconBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				Intent intent = new Intent(MainActivity.this, IconActivity.class);
				intent.putExtra("start_new", true);
				startActivity(intent);
			}
		});
		fullscreenBtn = (Button) findViewById(R.id.FullscreenBtn);
		fullscreenBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View paramView) {
				Intent intent = new Intent(MainActivity.this, FullscreenActivity.class);
				intent.putExtra("start_new", true);
				startActivity(intent);
			}
		});
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Logger.d(TAG, "MainActivity onOptionsItemSelected", null);
		finish();
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Logger.d(TAG, "MainActivity onKeyDown", null);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		Logger.d(TAG, "MainActivity onDestroy", null);
		super.onDestroy();
		AdLayout.exit();
	}

	@Override
	public void onDetachedFromWindow() {
		Logger.d(TAG, "MainActivity onDetachedFromWindow", null);
		super.onDetachedFromWindow();

	}

}

/*
 * $ echo $PATH
 * /usr/local/bin:/usr/bin:/cygdrive/c/Windows/system32:/cygdrive/c/
 * Windows:/cygdrive/c/Windows/System32/Wbem:
 * /cygdrive/c/Windows/System32/WindowsPowerShell/v1.0:/cygdrive/c/Program
 * Files/Java/jdk1.7.0_09/bin:
 * /cygdrive/d/job/android-sdk/android-sdk/tools:/cygdrive
 * /d/job/android-sdk/android-sdk/platform-tools: /cygdrive/c/Program
 * Files/TortoiseSVN/bin:/cygdrive/c/Program
 * Files/TortoiseSVN/bin:/cygdrive/d/job/apache-ant-1.8.4/bin:
 * /cygdrive/c/Program
 * Files/SourceGear/Common/DiffMerge:/cygdrive/d/job/apache-maven-3.0.4/bin:
 * /cygdrive
 * /d/job/android-sdk/android-sdk/platform-tools:/cygdrive/d/download/apktool:
 * /usr/lib/lapack: /cygdrive/D/job/android-sdk/android-sdk/tools:
 * /cygdrive/D/job/android-sdk/android-sdk/platform-tools:
 * /cygdrive/D/job/android-sdk/Menue_Android_SDK/sdk/android_NDK
 */
