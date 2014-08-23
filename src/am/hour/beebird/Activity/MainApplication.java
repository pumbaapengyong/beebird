package am.hour.beebird.Activity;

import android.app.Application;  
import android.content.Context;  
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
  
public class MainApplication extends Application {  
	
	public static int localVersion = 0;//判断本地的版本号
  
    /** 
     * 保存程序的上下文，application context 
     */  
    private static Context mContext;    
    
    private static String USER_NAME;
    private static String USER_PASSWORD;
    
	public static int SCREEN_WIDTH;//屏幕宽度
	public static int SCREEN_HEIGHT;//屏幕高度
	public static int TOP_HEIGHT;//顶部新西兰高度
	public static int ACTIONBAR_HEIGHT;//actionbar高度
	public static int DISAPLAY_HEIGHT;//可显示内容的区域
       
    @Override  
    public void onCreate() {  
        super.onCreate();  
          
        mContext = getApplicationContext();  
        //通过程序的配置文件，得到版本号
        try {
			PackageInfo packageInfo = getApplicationContext()
					.getPackageManager().getPackageInfo(getPackageName(), 0);
			localVersion = packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
          
    }
    
    /**返还全局上下文
     * @return 
     */ 
    public static Context getContext(){  
        return mContext;  
    } 
    
    public static void setUserInfo(String username,String userpassword){
    	USER_NAME = username;
    	USER_PASSWORD = userpassword;
    }
    
    public static String getUserName(){
    	return USER_NAME;
    }
    public static String getUserPassword(){
    	return USER_PASSWORD;
    }
    public static void setUserName(String username){
    	USER_NAME = username;
    }
    public static void setUserPassword(String userpassword){
    	USER_PASSWORD = userpassword;
    }
    
	//初始化屏幕参数
	public static void initConst(int screenWidth,int screenHeight,int theTopPart,int actionbarheight)
	{
		SCREEN_WIDTH=screenWidth;
		SCREEN_HEIGHT=screenHeight;	
		ACTIONBAR_HEIGHT = actionbarheight;
		TOP_HEIGHT = theTopPart;
		DISAPLAY_HEIGHT = SCREEN_HEIGHT-TOP_HEIGHT;

	}

	public static void initConst(int screenWidth,int screenHeight)
	{
		SCREEN_WIDTH=screenWidth;
		SCREEN_HEIGHT=screenHeight;	

	}
	
	public static void setActionbarHeight(int actionbarheight){
		ACTIONBAR_HEIGHT = actionbarheight;
	}
	
	public static void setDisaplayHeight(int theTopPart){
		TOP_HEIGHT = theTopPart;
		DISAPLAY_HEIGHT = SCREEN_HEIGHT-TOP_HEIGHT;
	}
    
    
    @Override  
    public void onLowMemory() {  
        super.onLowMemory();  
    }  
      
      
}  