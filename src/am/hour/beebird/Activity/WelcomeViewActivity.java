package am.hour.beebird.Activity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.InvalidKeyException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import am.hour.beebird.my.*;
import am.hour.beebird.utils.AESUtil;
import am.hour.beebird.utils.Constant;
import am.hour.beebird.utils.PicLoadUtil;
import am.hour.unknown.R;

public class WelcomeViewActivity extends Activity{
		
	private ImageView iv_welcomeView;

	private static int initTime = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_view);

        getandsetviews();
        timerStart(1500);

	}
	
		
	public void getandsetviews(){
		Bitmap bitmap_welcome_background = null;
		bitmap_welcome_background = PicLoadUtil.LoadBitmap(this,R.drawable.welcomeview_background);
		bitmap_welcome_background = PicLoadUtil.scaleToFit(bitmap_welcome_background, 
				(float)MainApplication.SCREEN_WIDTH/bitmap_welcome_background.getWidth(),
				(float)MainApplication.SCREEN_HEIGHT/bitmap_welcome_background.getHeight());

		iv_welcomeView = (ImageView)findViewById(R.id.iv_welcomeview);
		iv_welcomeView.setImageBitmap(bitmap_welcome_background);

	}


	
	public void goToTheMain(){
		Intent intent = new Intent();
		intent.setClass(WelcomeViewActivity.this,MainActivity.class);
		startActivity(intent);
		this.finish();
	}
		

	private void timerStart(int time){
	CountDownTimer timer = new CountDownTimer(time,time) {  
    
       @Override  
       public void onTick(long millisUntilFinished) {  
//           System.out.println("seconds remaining: " + millisUntilFinished / 10);                
       }  
        
       @Override  
       public void onFinish() {  
//         倒计时结束，进入主界面  
    	   goToTheMain();
           this.cancel();
       }  
    };

    timer.start(); 
}
		
}
