package am.hour.beebird.Activity;

import java.util.ArrayList;

import am.hour.beebird.my.*;
import am.hour.beebird.utils.Constant;
import am.hour.beebird.utils.PicLoadUtil;
import am.hour.beebird.utils.SharedPreferenceUtil;
import am.hour.unknown.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * 
 */
public class GuidingViewActivity extends Activity {
	
	static int initTime;
	// 欢迎页ViewPager
	private ViewPager viewPager;

	// ViewPager的适配器
	private MyViewPagerAdapter vpAdapter;

	// ArrayList保存view的容器
	private ArrayList<View> views;

	private View view1,view2,view3;
	
	// 页面上的小点
	private ImageView pointImage1, pointImage2, pointImage3;
	
	//具体的三个欢迎页面的布局
	private RelativeLayout rl_guidingview_1,rl_guidingview_2,rl_guidingview_3;
	
	//最后一个页面进入主界面的按钮
	private Button startBt;
	
	private SharedPreferenceUtil spu = null;

	//判断当前页面，从而能够使小点变化ֵ
	private int currIndex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guiding_view);
		spu = new SharedPreferenceUtil();
		DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);    
        //
        MainApplication.initConst(dm.widthPixels, dm.heightPixels);	
		spu.saveIntInSP("SCREEN_WIDTH", dm.widthPixels);
		spu.saveIntInSP("SCREEN_HEIGHT", dm.heightPixels);
		
		

	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);	
		Rect frame = new Rect();
		getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		if(frame.top!=0){
			MainApplication.setDisaplayHeight(frame.top); 
			spu.saveIntInSP("TOP_HEIGHT", frame.top);
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
			getandsetviews();
		}

	}
	
	public void getandsetviews(){
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		LayoutInflater mLi = LayoutInflater.from(this);
		view1 = mLi.inflate(R.layout.guide_view01, null);
		view2 = mLi.inflate(R.layout.guide_view02, null);
		view3 = mLi.inflate(R.layout.guide_view03, null);		
		
		views = new ArrayList<View>();
	
		views.add(view1);
		views.add(view2);
		views.add(view3);

		vpAdapter = new MyViewPagerAdapter(views);

		viewPager.setAdapter(vpAdapter);
		
		Bitmap bitmap_1 = null;
		Bitmap bitmap_2 = null;
		Bitmap bitmap_3 = null;

		pointImage1 = (ImageView) findViewById(R.id.page0);
		pointImage2 = (ImageView) findViewById(R.id.page1);
		pointImage3 = (ImageView) findViewById(R.id.page2);

		startBt = (Button) view3.findViewById(R.id.startBtn);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());

		startBt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				 startbutton();
			}
		});
		
		bitmap_1 = PicLoadUtil.LoadBitmap(this,R.drawable.guiding_pic_1);
		bitmap_2 = PicLoadUtil.LoadBitmap(this,R.drawable.guiding_pic_2);
		bitmap_3 = PicLoadUtil.LoadBitmap(this,R.drawable.guiding_pic_3);
		
		bitmap_1 = PicLoadUtil.scaleToFit(bitmap_1, 
				(float)MainApplication.SCREEN_WIDTH/bitmap_1.getWidth(),
				(float)MainApplication.SCREEN_HEIGHT/bitmap_1.getHeight());
		bitmap_2 = PicLoadUtil.scaleToFit(bitmap_2, 
				(float)MainApplication.SCREEN_WIDTH/bitmap_2.getWidth(),
				(float)MainApplication.SCREEN_HEIGHT/bitmap_2.getHeight());
		bitmap_3 = PicLoadUtil.scaleToFit(bitmap_3, 
				(float)MainApplication.SCREEN_WIDTH/bitmap_3.getWidth(),
				(float)MainApplication.SCREEN_HEIGHT/bitmap_3.getHeight());
		
		rl_guidingview_1 = (RelativeLayout)view1.findViewById(R.id.rl_guidingview_1);
		rl_guidingview_2 = (RelativeLayout)view2.findViewById(R.id.rl_guidingview_2);
		rl_guidingview_3 = (RelativeLayout)view3.findViewById(R.id.rl_guidingview_3);
			
		rl_guidingview_1.setBackgroundDrawable(new BitmapDrawable(bitmap_1));
		rl_guidingview_2.setBackgroundDrawable(new BitmapDrawable(bitmap_2));
		rl_guidingview_3.setBackgroundDrawable(new BitmapDrawable(bitmap_3));
		
		
			
	}
	



	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int position) {
			switch (position) {
			case 0:
				pointImage1.setImageDrawable(getResources().getDrawable(R.drawable.page_indicator_focused));
				pointImage2.setImageDrawable(getResources().getDrawable(R.drawable.page_indicator_unfocused));
				break;
			case 1:
				pointImage2.setImageDrawable(getResources().getDrawable(R.drawable.page_indicator_focused));
				pointImage1.setImageDrawable(getResources().getDrawable(R.drawable.page_indicator_unfocused));
				pointImage3.setImageDrawable(getResources().getDrawable(R.drawable.page_indicator_unfocused));
				break;
			case 2:
				pointImage3.setImageDrawable(getResources().getDrawable(R.drawable.page_indicator_focused));
				pointImage2.setImageDrawable(getResources().getDrawable(R.drawable.page_indicator_unfocused));
				break;
			}
			currIndex = position;
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
	}
	
	/**
	 * 按钮的点击响应函数
	 */
	private void startbutton() {  
      	Intent intent = new Intent();
		intent.setClass(GuidingViewActivity.this,MainActivity.class);
		startActivity(intent);
		this.finish();
		
    }
	
}
