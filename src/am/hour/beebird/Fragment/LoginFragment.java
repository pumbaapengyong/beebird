package am.hour.beebird.Fragment;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.InvalidKeyException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import am.hour.beebird.Activity.MainActivity;
import am.hour.beebird.Activity.MainApplication;
import am.hour.beebird.Activity.RegisterViewActivity;
import am.hour.beebird.utils.AESUtil;
import am.hour.beebird.utils.Constant;
import am.hour.beebird.utils.PicLoadUtil;
import am.hour.unknown.R;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginFragment extends Fragment implements OnClickListener{

	public View rootView = null;
	private LinearLayout ll_login_view;
	private TextView tv_righttothemain;
	private Button bt_register;
	private Button bt_login;	
	private EditText username;
	private EditText userpassword;	
	
	String string_username;
	String string_userpassword;
	
    public LoginFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
        rootView = inflater.inflate(R.layout.login_view, container, false);
        int i = getArguments().getInt(Constant.ARG_FRAGMENT_INFO);
        
        getandsetviews();

        return rootView;
    }
    
	/*
	 *AES加密操作
	 * 
	 * */
	public void testAES() throws InvalidKeyException{
		
	      String content_2 = "pengyongshihuaidan";
	      String password_2 = "12345678";
	      
	      byte[] encryptResult_2 = AESUtil.encrypt(content_2, password_2);
	      String encryptResultStr_2 = AESUtil.parseByte2HexStr(encryptResult_2);
	      
	      byte[] decryptFrom_2 = AESUtil.parseHexStr2Byte(encryptResultStr_2);
	      byte[] decryptResult_2 = AESUtil.decrypt(decryptFrom_2,password_2);
	      
		
	}
	
	public void getandsetviews(){
		Bitmap bitmap_login_background = null;
		bitmap_login_background = PicLoadUtil.LoadBitmap(getActivity(),R.drawable.login_background);
		bitmap_login_background = PicLoadUtil.scaleToFit(bitmap_login_background, 
				(float)MainApplication.SCREEN_WIDTH/bitmap_login_background.getWidth(),
				(float)MainApplication.DISAPLAY_HEIGHT/bitmap_login_background.getHeight());
		ll_login_view = (LinearLayout)rootView.findViewById(R.id.ll_login_view);
		ll_login_view.setBackgroundDrawable(new BitmapDrawable(bitmap_login_background));
			
		username = (EditText)rootView.findViewById(R.id.user_name);
		userpassword = (EditText)rootView.findViewById(R.id.user_password);
			
		tv_righttothemain = (TextView)rootView.findViewById(R.id.righttothemain);
		tv_righttothemain.setVisibility(View.GONE);
		bt_register = (Button)rootView.findViewById(R.id.register);
		bt_register.setOnClickListener(this);
		bt_login = (Button)rootView.findViewById(R.id.login);
		bt_login.setOnClickListener(this);
		
		
		int titleId = Resources.getSystem().getIdentifier(
				"action_bar_title", "id", "android");
        TextView actionbarTitle = (TextView) getActivity().findViewById(titleId);
        actionbarTitle.setTextColor(Color.BLACK);
	}
	

	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if(v.getId()==R.id.register)
		{
			Intent intent = new Intent();
			intent.setClass(getActivity(),RegisterViewActivity.class);
			startActivity(intent);
//			getActivity().finish();
		}
		if(v.getId()==R.id.login)
		{
			string_username = username.getText().toString().trim();
			string_userpassword = userpassword.getText().toString().trim();
			if(string_username == null || string_username.isEmpty()){
				toast("用户名为空");
			}else if(string_userpassword == null || string_userpassword.isEmpty()){
				toast("密码为空");
			}else{
				LoginAsyncTaskUtil mTask = new LoginAsyncTaskUtil();  
				String url = "http://115.28.232.254:10000/&login&user&loginname&"
				+string_username+"&"+string_userpassword+"&";
	            mTask.execute(url); 
			}

		}
		
	}
	
	public void toast(String toast){
		Toast.makeText(getActivity(), toast, 500).show();
	}
	

	private class LoginAsyncTaskUtil extends AsyncTask<String, Integer, String> {
    	@Override
    	protected void onPreExecute() {
    		
    		toast("登陆中ing");
    	}
    	
		@Override
		protected String doInBackground(String... params) {
			
			try {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(params[0]);
				HttpResponse response = client.execute(get);
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					HttpEntity entity = response.getEntity();
					InputStream is = entity.getContent();
					long total = entity.getContentLength();
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					byte[] buf = new byte[1024];
					int count = 0;
					int length = -1;
					while ((length = is.read(buf)) != -1) {
						baos.write(buf, 0, length);
						count += length;
					}
					return new String(baos.toByteArray(), "utf-8");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
	
//			if(result.equals("&success&����&")){
				MainActivity.spu.saveStringInSP("USERNAME", string_username);
				MainActivity.spu.saveStringInSP("USERPASSWORD", string_userpassword);
				MainActivity.spu.saveIntInSP("LOGINORNOT", 1);
				Intent intent = new Intent();
				intent.setClass(getActivity(),MainActivity.class);
				startActivity(intent);
				getActivity().finish();
//			}else{
//				toast("登陆失败");
//			}
			
		}
		
    }
    
    
}
