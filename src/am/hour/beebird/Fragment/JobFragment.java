package am.hour.beebird.Fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import am.hour.beebird.Activity.MainActivity;
import am.hour.beebird.Activity.MainApplication;
import am.hour.beebird.AsyncTask.DeleteAsyncTask;
import am.hour.beebird.AsyncTask.LoveAsyncTask;
import am.hour.beebird.AsyncTask.NextJobAsyncTask;
import am.hour.beebird.listView.MyListAdapter;
import am.hour.beebird.listView.Utils;
import am.hour.beebird.model.JobInfo;
import am.hour.beebird.my.MyLinearLayout;
import am.hour.beebird.my.MyScrollView;
import am.hour.beebird.my.MyScrollView.OnScrollListener;
import am.hour.beebird.utils.AESUtil;
import am.hour.beebird.utils.Constant;
import am.hour.beebird.utils.DBUtil4JobFragment;
import am.hour.beebird.utils.FastBlur;
import am.hour.beebird.utils.ICUtil4JobFragment;
import am.hour.beebird.utils.JsonUtil;
import am.hour.beebird.utils.PicLoadUtil;
import am.hour.unknown.R;
import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.ScaleAnimation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Fragment that appears in the "content_frame"
 */
public class JobFragment extends Fragment implements OnScrollListener,OnClickListener{	
	MyScrollView myScrollView;
	
	Animation scaleAnimation_1 = null;
	Animation scaleAnimation_2 = null;
    public static ImageView iv_bottom_up = null;
    public static ImageView iv_bottom_below = null;
    public static ImageView iv_middle_companylogo = null;
    public static ImageView iv_top_funnypoint_top = null;
    public static ImageView iv_top_funnypoint_bottom = null;
    public static ImageButton ib_thefirstbutton = null;
    public static ImageButton ib_thesecondbutton = null;
    public static ImageButton ib_thethirdbutton = null;
    public static TextView tv_companyname = null;
    public static TextView tv_middle_jobname = null;
    public static TextView tv_middle_jobplace = null;
    public static TextView tv_middle_salary = null;
    public static TextView tv_middle_keypoint = null;
    public static TextView tv_top_funnypointinpic = null;
    public static TextView tv_weHave = null;
    public static TextView tv_weHope = null; 
    public static LinearLayout ll_buttons = null;
    public static am.hour.beebird.my.MyLinearLayout ll_keypoints = null;
    LinearLayout ll_middlelayer = null;
    LinearLayout ll_informationlayer = null;
     
    DeleteAsyncTask deleteTask = null;
    LoveAsyncTask loveTask = null;
    NextJobAsyncTask nextjob = null;

    public static DBUtil4JobFragment dbUtil4JobFragment = null;
    public static ICUtil4JobFragment imageCache = null;
    
    public static WebView wv; 
    View rootView = null;
    public JobFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_jobhunt, container, false);
        dbUtil4JobFragment = new DBUtil4JobFragment();
        imageCache=new ICUtil4JobFragment();
		getandsetView();
        setViewGroupParams();
        
		//获取进入的途径，是正常的浏览还是从列表点击，列表点击的情况下，这个字段中包含有jobIdֵ
        int entry = getArguments().getInt(Constant.ARG_FRAGMENT_INFO);
        nextjob = null;
        System.gc();
        nextjob = new NextJobAsyncTask();
		nextjob.execute(entry+"");
        gogogo();
        return rootView;
    }

    private void getandsetView(){
    	iv_bottom_up = (ImageView)rootView.findViewById(R.id.iv_bottomlayer_up);
    	iv_bottom_up.setOnClickListener(this);
    	iv_bottom_below = (ImageView)rootView.findViewById(R.id.iv_bottomlayer_below);
        iv_middle_companylogo = (ImageView)rootView.findViewById(R.id.iv_middlelayer_companylogo);
        iv_top_funnypoint_top = (ImageView)rootView.findViewById(R.id.iv_funnypoint_top);
        iv_top_funnypoint_bottom = (ImageView)rootView.findViewById(R.id.iv_funnypoint_bottom);
      
        tv_companyname = (TextView)rootView.findViewById(R.id.tv_middlelayer_companyname);
        tv_middle_jobname = (TextView)rootView.findViewById(R.id.tv_middlelayer_jobname);
        tv_middle_jobplace = (TextView)rootView.findViewById(R.id.tv_middlelayer_jobplace);
        tv_middle_salary = (TextView)rootView.findViewById(R.id.tv_middlelayer_salary);
        tv_middle_keypoint = (TextView)rootView.findViewById(R.id.tv_middlelayer_keypoint);
        tv_top_funnypointinpic = (TextView)rootView.findViewById(R.id.tv_funnypoint_left);
        
        ll_middlelayer = (LinearLayout)rootView.findViewById(R.id.ll_middlelayer);
        ll_informationlayer = (LinearLayout)rootView.findViewById(R.id.ll_informationlayer);
        ll_buttons = (LinearLayout)rootView.findViewById(R.id.the_bottom_buttons);
        ll_keypoints = (MyLinearLayout)rootView.findViewById(R.id.ll_key_points);
   
        ib_thefirstbutton = (ImageButton)rootView.findViewById(R.id.thefirstbutton);
        ib_thesecondbutton = (ImageButton)rootView.findViewById(R.id.thesecondbutton);
        ib_thethirdbutton = (ImageButton)rootView.findViewById(R.id.thethirdbutton);
        ib_thefirstbutton.setOnClickListener(this);
        ib_thesecondbutton.setOnClickListener(this);
        ib_thethirdbutton.setOnClickListener(this);
        
        myScrollView = (am.hour.beebird.my.MyScrollView)rootView.findViewById(R.id.scrollView);
        myScrollView.setOnScrollListener(this); 
        
        tv_weHave = (TextView)rootView.findViewById(R.id.tv_wehave);
        tv_weHope = (TextView)rootView.findViewById(R.id.tv_wehope);
        
        int titleId = Resources.getSystem().getIdentifier(
				"action_bar_title", "id", "android");
        TextView actionbarTitle = (TextView) getActivity().findViewById(titleId);
        actionbarTitle.setTextColor(Color.WHITE);
        
        wv = (WebView) rootView.findViewById(R.id.myWeb);
		WebSettings ws = wv.getSettings();
		ws.setJavaScriptEnabled(true);//

    }
     
    private void setViewGroupParams(){
    	LayoutParams lp = null;
    	/*
    	 * 滚动区域
    	 * 
    	 */
    	 lp = myScrollView.getLayoutParams();
   	     lp.height=MainApplication.DISAPLAY_HEIGHT*(Constant.SCREEN_SCALE[0]+
   	    		 Constant.SCREEN_SCALE[1])/100;
   	     myScrollView.setLayoutParams(lp);
   	    /*
     	 * 背景图片
     	 * 
     	 */
    	
    	 lp = iv_bottom_up.getLayoutParams();
         lp.height=MainApplication.DISAPLAY_HEIGHT*(Constant.SCREEN_SCALE[0])/100;
    	 iv_bottom_up.setLayoutParams(lp);
        /*
      	 * 背景模糊图片
      	 */
    	 lp = iv_bottom_below.getLayoutParams();    	 
    	 lp.height=MainApplication.DISAPLAY_HEIGHT*Constant.SCREEN_SCALE[1]/100;
    	 ((MarginLayoutParams) lp).setMargins(0, 
    			 MainApplication.DISAPLAY_HEIGHT*Constant.SCREEN_SCALE[0]/100, 0, 0);
    	 iv_bottom_below.setLayoutParams(lp);
        /*
     	 * 第二个图层的公司logo和名称的显示区域
     	 * 
     	 */
    	 lp = ll_middlelayer.getLayoutParams();
    	 ((MarginLayoutParams) lp).setMargins(0, 
    			 MainApplication.DISAPLAY_HEIGHT*(Constant.SCREEN_SCALE[0]-9)/100, 0, 0);
    	 ll_middlelayer.setLayoutParams(lp);
    	 /*
      	 * 
      	 */
    	 lp = iv_middle_companylogo.getLayoutParams();
    	 lp.height=MainApplication.DISAPLAY_HEIGHT*14/100;
    	 lp.width=MainApplication.DISAPLAY_HEIGHT*14/100;
    	 ((MarginLayoutParams) lp).setMargins(MainApplication.SCREEN_WIDTH*8/100,
    			 0, 0, 0);
    	 iv_middle_companylogo.setLayoutParams(lp);
    	 /*
       	 * 公司名字显示区域
       	 */
    	 lp = tv_companyname.getLayoutParams();
    	 ((MarginLayoutParams) lp).setMargins(0,
    			 0, MainApplication.SCREEN_WIDTH*8/100, 0);
    	 tv_companyname.setLayoutParams(lp);
    	 /*
      	 * 下面的四个区域
      	 */
     	 lp = ll_informationlayer.getLayoutParams();
     	 ((MarginLayoutParams) lp).setMargins(0, 
     			MainApplication.DISAPLAY_HEIGHT*(Constant.SCREEN_SCALE[0]+3)/100, 0, 0);
     	 
     	 ll_informationlayer.setLayoutParams(lp);
    	 /*
       	 *职位名称
       	 */
    	 lp = tv_middle_jobname.getLayoutParams();
    	 lp.height=MainApplication.DISAPLAY_HEIGHT*(Constant.SCREEN_SCALE[1]-3)/100/4;
    	 tv_middle_jobname.setLayoutParams(lp);
    	 /*
          *工作地点
          */
    	 lp = tv_middle_jobplace.getLayoutParams();
    	 lp.height=MainApplication.DISAPLAY_HEIGHT*(Constant.SCREEN_SCALE[1]-3)/100/4;
    	 tv_middle_jobplace.setLayoutParams(lp);
    	 /*
     	 *工作薪水
     	 */
     	 lp = tv_middle_salary.getLayoutParams();
     	 lp.height=MainApplication.DISAPLAY_HEIGHT*(Constant.SCREEN_SCALE[1]-3)/100/4;
     	 tv_middle_salary.setLayoutParams(lp);
    	 /*
    	 *关键点
    	 */
    	 lp = tv_middle_keypoint.getLayoutParams();
    	 lp.height=MainApplication.DISAPLAY_HEIGHT*(Constant.SCREEN_SCALE[1]-3)/100/4;
    	 tv_middle_keypoint.setLayoutParams(lp);
    	 
    	 /*
     	 *底部下面
     	 */
     	 lp = ll_buttons.getLayoutParams();
     	 lp.height=MainApplication.DISAPLAY_HEIGHT
     			 -MainApplication.DISAPLAY_HEIGHT*Constant.SCREEN_SCALE[0]/100
     			-MainApplication.DISAPLAY_HEIGHT*Constant.SCREEN_SCALE[1]/100;
     	 ll_buttons.setLayoutParams(lp);
    	 
    	 /*
          *第一个按钮
          */
    	 lp = ib_thefirstbutton.getLayoutParams();
    	 lp.height=MainApplication.DISAPLAY_HEIGHT*Constant.BUTTON_HEIGHT_SCALE/100;
    	 lp.width=MainApplication.DISAPLAY_HEIGHT*Constant.BUTTON_HEIGHT_SCALE/100;
    	 ((MarginLayoutParams) lp).setMargins((MainApplication.SCREEN_WIDTH
    			 -3*MainApplication.DISAPLAY_HEIGHT*Constant.BUTTON_HEIGHT_SCALE/100
    			 -2*MainApplication.SCREEN_WIDTH*Constant.BUTTON_GAP_SCALE/100)/2,
    			 MainApplication.DISAPLAY_HEIGHT*(Constant.SCREEN_SCALE[2]-Constant.BUTTON_HEIGHT_SCALE)/200, 0, 0);
    	 ib_thefirstbutton.setLayoutParams(lp);
    	 /*
          *第二个按钮
          */
    	 lp = ib_thesecondbutton.getLayoutParams();
    	 lp.height=MainApplication.DISAPLAY_HEIGHT*Constant.BUTTON_HEIGHT_SCALE/100;
    	 lp.width=MainApplication.DISAPLAY_HEIGHT*Constant.BUTTON_HEIGHT_SCALE/100;
    	 ((MarginLayoutParams) lp).setMargins(MainApplication.SCREEN_WIDTH*Constant.BUTTON_GAP_SCALE/100,
    			 MainApplication.DISAPLAY_HEIGHT*(Constant.SCREEN_SCALE[2]-Constant.BUTTON_HEIGHT_SCALE)/200, 0, 0);
    	 ib_thesecondbutton.setLayoutParams(lp);
    	 /*
          *第三个按钮
          */
    	 lp = ib_thethirdbutton.getLayoutParams();
    	 lp.height=MainApplication.DISAPLAY_HEIGHT*Constant.BUTTON_HEIGHT_SCALE/100;
    	 lp.width=MainApplication.DISAPLAY_HEIGHT*Constant.BUTTON_HEIGHT_SCALE/100;
    	 ((MarginLayoutParams) lp).setMargins(MainApplication.SCREEN_WIDTH*Constant.BUTTON_GAP_SCALE/100,
    			 MainApplication.DISAPLAY_HEIGHT*(Constant.SCREEN_SCALE[2]-Constant.BUTTON_HEIGHT_SCALE)/200, 0, 0);
    	 ib_thethirdbutton.setLayoutParams(lp);
 
    }
	
    public void toast(String toast){
		Toast.makeText(getActivity(), toast, 50).show();
	}
    
    private void gogogo(){
    	scaleAnimation_1= new ScaleAnimation(
    			0.5f,1f,0.5f,1f,Animation.RELATIVE_TO_SELF,
                0.5f,Animation.RELATIVE_TO_SELF,0.5f); 
    	scaleAnimation_1.setDuration(500); 
    	scaleAnimation_1.setRepeatCount(Animation.INFINITE);
    	scaleAnimation_1.setRepeatMode(Animation.INFINITE);
    	AnticipateInterpolator ai = new AnticipateInterpolator();
    	scaleAnimation_1.setInterpolator(ai);
    	iv_top_funnypoint_top.setAnimation(scaleAnimation_1);
    	
    	scaleAnimation_2= new   
   		     ScaleAnimation(0.4f,0.7f,0.4f,0.7f,Animation.RELATIVE_TO_SELF,
   		    		 0.5f,Animation.RELATIVE_TO_SELF,0.5f); 
   	    scaleAnimation_2.setDuration(1000);
   	    scaleAnimation_2.setRepeatCount(Animation.INFINITE);
   	    scaleAnimation_2.setRepeatMode(Animation.INFINITE);
    	
    	iv_top_funnypoint_bottom.setAnimation(scaleAnimation_2);
   	    scaleAnimation_1.startNow();
        scaleAnimation_2.startNow();
    }

    @Override
    public void onScroll(int scrollY) {
		// TODO Auto-generated method stub
		if(scrollY >= MainApplication.DISAPLAY_HEIGHT*(Constant.SCREEN_SCALE[0]-12)/100){  
            getActivity().getActionBar().hide(); 
            //setTextColor(Color.argb(35, 0, 255, 0));
            
        }else if(scrollY <= MainApplication.DISAPLAY_HEIGHT*(Constant.SCREEN_SCALE[0]-12)/100){  
        	getActivity().getActionBar().show();  
        } 
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.iv_bottomlayer_up:

			if(iv_top_funnypoint_top.getVisibility()==View.GONE){
				iv_top_funnypoint_top.setVisibility(View.VISIBLE);
				iv_top_funnypoint_bottom.setVisibility(View.VISIBLE);
				tv_top_funnypointinpic.setVisibility(View.VISIBLE);
				gogogo();
			}else{
				scaleAnimation_1.cancel();
				scaleAnimation_2.cancel();
				iv_top_funnypoint_top.setVisibility(View.GONE);
				iv_top_funnypoint_bottom.setVisibility(View.GONE);
				tv_top_funnypointinpic.setVisibility(View.GONE);
			}
			break;
		case R.id.thefirstbutton:
			/*
			 *删除的按钮
			 * */
			//http://115.28.232.254:10000/&addhatejob&logintype&username&password&hatejobid&
			String hatejobId = dbUtil4JobFragment.getTempJobFromDB().getJobId();
			String hateurl = "http://115.28.232.254:10000/" +
					"&addhatejob&loginname&" +
					"huoxing001" +
					"&" +
					"123456" +
					"&"+hatejobId+"&";
			deleteTask = null;
			deleteTask = new DeleteAsyncTask();
			deleteTask.execute(hateurl);
			break;
		case R.id.thesecondbutton:
			/*
			 *喜欢的按钮
			 * */
			//http://115.28.232.254:10000/&addfavorjob&logintype&username&password&favorjobid&
			String favorjobId = dbUtil4JobFragment.getTempJobFromDB().getJobId();
			String favorurl = "http://115.28.232.254:10000/" +
					"&addhatejob&loginname&" +
					"huoxing001" +
					"&" +
					"123456" +
					"&"+favorjobId+"&";
			loveTask = null;
			loveTask = new LoveAsyncTask();
			loveTask.execute(favorurl);
			break;
		case R.id.thethirdbutton:
			/*
			 *下一个的按钮
			 * */
			wv.stopLoading();

			nextjob = null;
	        System.gc();
	        nextjob = new NextJobAsyncTask();
			nextjob.execute("http://115.28.232.254:10000/&recomjob&loginname&huoxing001&123456&null&");
			ib_thefirstbutton.setEnabled(false);
	        ib_thesecondbutton.setEnabled(false);
	        ib_thethirdbutton.setEnabled(false);			
			break;
			
		}
	}
	
	@Override
	public void onDestroyView() {
		dbUtil4JobFragment.closeDB();
		super.onDestroy();
	}    
}