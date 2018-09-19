package com.example.demo_lxl;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
/**
 * 首页
 * Created by dawnling on 2016/08/02.
 * http://my.csdn.net/?ref=toolbar
 * http://www.jianshu.com/u/8fd63a0d4c4c
 */
public class MainActivity extends FragmentActivity implements OnClickListener{

	private Class[] clas = new Class[] {BlankFragment.class, BlankFragment.class, BlankFragment.class, BlankFragment.class,
			BlankFragment.class };
	private int images[] = new int[] {R.drawable.tab_1_selector, R.drawable.tab_2_selector,1, R.drawable.tab_4_selector,
			R.drawable.tab_5_selector };
	private ImageView main_image_center;
	private TextView main_tv_final;
	private TextView mBottom_center;
	private FragmentTabHost mTabHost;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initUI();
	}

	private void initUI() {
		//底部中间按钮控件
		main_image_center = (ImageView) findViewById(R.id.main_image_center);
		main_tv_final = (TextView) findViewById(R.id.main_tv_final);
		main_image_center.setImageResource(R.drawable.circle);
		mBottom_center = (TextView) findViewById(R.id.main_tv_final);
		main_image_center.setOnClickListener(this);
		mBottom_center.setOnClickListener(this);	
		main_tv_final.setOnClickListener(this);	
		
		String[] tabIndicatorArray = getResources().getStringArray(R.array.arr_tab_indicator);
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		LayoutInflater inflater = getLayoutInflater();
		for (int i = 0; i < images.length; i++) {
			View indicatorView = inflater.inflate(R.layout.list_item_viewpagerindicator, null);
			TextView tvIndicator = (TextView) indicatorView.findViewById(R.id.tv_title_indicator);
			tvIndicator.setText(tabIndicatorArray[i]);
			ImageView imageView = (ImageView) indicatorView.findViewById(R.id.ima_indicator);
			imageView.setImageResource(images[i]);
			//tabhost添加tab切换事件
			mTabHost.addTab(mTabHost.newTabSpec("tab"+i).setIndicator(indicatorView), clas[i], null);
			mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
				
				@Override
				public void onTabChanged(String tabId) {
					switch (tabId)
					{
					case "tab2":
						main_image_center.setImageResource(R.drawable.circle_pre);
						mBottom_center.setTextColor(Color.parseColor("#af0000"));
						break;
					default:
						main_image_center.setImageResource(R.drawable.circle);
						mBottom_center.setTextColor(Color.parseColor("#999999"));
						break;
					}
				}
			});
		} 
	}

	@Override
	public void onClick(View v) {
		switch (v.getId())
		{
		case R.id.main_image_center:
			mTabHost.setCurrentTab(2);
			break;
		case R.id.main_tv_final:
			mTabHost.setCurrentTab(2);
			break;
		default:
			break;
		}
	}
}
