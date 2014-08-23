package am.hour.unknown.Activity;

import am.hour.unknown.utils.Constant;
import am.hour.unknown.utils.SharedPreferenceUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class FirstActivity extends Activity{
	
	
	SharedPreferenceUtil spu = null;

	@Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);   
        spu = new SharedPreferenceUtil();
        int SCREEN_WIDTH = spu.getIntFromSP("SCREEN_WIDTH", 0);
        int SCREEN_HEIGHT = spu.getIntFromSP("SCREEN_HEIGHT", 0);
        int TOP_HEIGHT = spu.getIntFromSP("TOP_HEIGHT", 0);
        int ACTIONBAR_HEIGHT = spu.getIntFromSP("ACTIONBAR_HEIGHT", 0);
      
        MainApplication.initConst(SCREEN_WIDTH,SCREEN_HEIGHT,TOP_HEIGHT,ACTIONBAR_HEIGHT);
//		判断是不是第一次进入
        int FirstTimeorNot = spu.getIntFromSP("FirstTimeorNot", 0);  
		 if(FirstTimeorNot==0){
			 gotoGuidingView();
		 }else{
			 gotoWelcomeView();
		 }

        
        
    }
		
	//不是第一次进入
    private void gotoWelcomeView()
    {
      	Intent intent = new Intent();
		intent.setClass(FirstActivity.this,WelcomeViewActivity.class);
		startActivity(intent);
		this.finish();
    	
    }
    //是第一次进入
    private void gotoGuidingView()
    {
    	spu.saveIntInSP("FirstTimeorNot", 1);
    	
      	Intent intent = new Intent();
		intent.setClass(FirstActivity.this,GuidingViewActivity.class);
		startActivity(intent);
		this.finish();

    }

}
