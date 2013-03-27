package com.ex.adlayout.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.menue.adlayout.ad.AdLayout;

public class BannerActivity extends Activity {
	Button bannerBtn, popBtn, IconBtn,fullscreenBtn;
	// private BannerView bannerview;
	private AdLayout adLayout;
	Context context ;
	
	private boolean startNew(){
		Intent intent = getIntent();
		return intent.getBooleanExtra("start_new", false);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bannerlayout);
		this.context = this ;
		
		
		adLayout = (AdLayout) findViewById(R.id.adlayout);
		
		bannerBtn = (Button) findViewById(R.id.BannerBtn);
		bannerBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				startActivity(new Intent(context, BannerActivity.class));

			}
		});
		popBtn = (Button) findViewById(R.id.PopupBtn);
		popBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				startActivity(new Intent(context, PopupActivity.class));
			}
		});
		
		
		IconBtn = (Button) findViewById(R.id.IconBtn);
		IconBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				startActivity(new Intent(context, IconActivity.class));
			}
		});
		
		fullscreenBtn = (Button) findViewById(R.id.FullscreenBtn);
		fullscreenBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View paramView) {
				Intent intent = new Intent(context, FullscreenActivity.class);
				startActivity(intent);
			}
		});
		if(!startNew()){
			bannerBtn.setVisibility(View.INVISIBLE);
			popBtn.setVisibility(View.INVISIBLE);
			IconBtn.setVisibility(View.INVISIBLE);
			fullscreenBtn.setVisibility(View.INVISIBLE);
			TextView readMe = (TextView) findViewById(R.id.read_me);
			readMe.setText("这是程序的第二个页面，按back键销毁当前页面广告");
		}else{
			TextView readMe = (TextView) findViewById(R.id.read_me);
			readMe.setText("点击下面的按钮模拟一个程序红添加多个广告，点击back模拟退出程序");
			
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		adLayout.destroyAd();
		if(startNew()){
			AdLayout.exit();
		}
		
		
	}
}
