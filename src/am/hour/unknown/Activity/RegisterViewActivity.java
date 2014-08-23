package am.hour.unknown.Activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.Buffer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import am.hour.unknown.R;
import am.hour.unknown.my.*;
import am.hour.unknown.utils.DBUtil4Image;
import am.hour.unknown.utils.PicLoadUtil;
import am.hour.unknown.utils.SharedPreferenceUtil;

public class RegisterViewActivity extends Activity implements OnClickListener{
	
	private LinearLayout ll_register_view;
	private ImageButton imb_choose_pic;
	private Button bt_confirm_register;
	private EditText username,userpassword,userpassword_confirm;
	
	String string_username_register;
	String string_userpassword;
	String string_userpassword_confirm;
	
	private DBUtil4Image DBUtil = null;
	private Bitmap cameraBitmap;
	
	SharedPreferenceUtil spu = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		setContentView(R.layout.register_view);
        DBUtil = new DBUtil4Image();
        spu = new SharedPreferenceUtil();
		getandsetviews();
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setBackgroundDrawable(PicLoadUtil.getActionBarDrawable());
	}
	
	private void getandsetviews(){
		ll_register_view = (LinearLayout)findViewById(R.id.ll_register_view);
		ll_register_view.setBackgroundDrawable
		     (PicLoadUtil.LoadBitmapDrawable(this,R.drawable.regist_background));

		bt_confirm_register = (Button)findViewById(R.id.confirm_register);
		bt_confirm_register.setOnClickListener(this);
		imb_choose_pic = (ImageButton)findViewById(R.id.bt_choose_pic);
		imb_choose_pic.setOnClickListener(this);
		
		username = (EditText)findViewById(R.id.et_user_name_register);
		userpassword = (EditText)findViewById(R.id.et_user_password_register);
		userpassword_confirm = (EditText)findViewById(R.id.et_user_passwordconfirm_register);
	}
	
	public void toast(String toast){
		Toast.makeText(this, toast, 500).show();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.confirm_register)
		{
			string_username_register = username.getText().toString().trim();
			string_userpassword = userpassword.getText().toString().trim();
			string_userpassword_confirm = userpassword_confirm.getText().toString().trim();
			if(string_username_register == null || string_username_register.isEmpty()){
				toast("没有输入用户名");
			}else{
				//�������붼�ǿյ�
				if((string_userpassword == null 
						|| string_userpassword.isEmpty())
						&&(string_userpassword_confirm == null 
						|| string_userpassword_confirm.isEmpty())){
					toast("密码为空哦");
				}else if(string_userpassword.equals(string_userpassword_confirm)){
					RegisterAsyncTaskUtil mTask = new RegisterAsyncTaskUtil();  
					String register_url = "http://115.28.232.254:10000/&register&user&loginname&"
					+string_username_register+"&"+string_userpassword+"&";				
		            mTask.execute(register_url); 
				}else{
					toast("两次密码不相同");
				}
			}
		}
		if(v.getId()==R.id.bt_choose_pic)
		{
			getImageFromPhone();
		}
	}
	
	public void getImageFromPhone(){
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);  
        intent.addCategory(Intent.CATEGORY_OPENABLE);  
        intent.setType("image/*");  
        intent.putExtra("crop", "true");  
        intent.putExtra("aspectX",1);  
        intent.putExtra("aspectY",1);  
        intent.putExtra("outputX", 160);  
        intent.putExtra("outputY", 160);  
        intent.putExtra("return-data",true);  
        startActivityForResult(intent, 11); 
	}
	
	
	@Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 11 && resultCode == Activity.RESULT_OK) {
	        System.out.println(resultCode);  
	        cameraBitmap = (Bitmap) data.getExtras().get("data");  
	        cameraBitmap = PicLoadUtil.toRoundBitmap(cameraBitmap);
	        this.imb_choose_pic.setBackgroundDrawable(new BitmapDrawable(cameraBitmap));
	        super.onActivityResult(requestCode, resultCode, data); 
        }
    } 
	
	public void goToTheMain(){
		Intent intent = new Intent();
		intent.setClass(RegisterViewActivity.this,MainActivity.class);
		startActivity(intent);
		this.finish();
	}
	
	
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()) {
        case android.R.id.home:
        	
			this.finish();
			
            return true;
            default:
            	return super.onOptionsItemSelected(item);
            
		}
	
	}
	public void onDestroy(){
		super.onDestroy();
		//关闭数据库
		DBUtil.closeDB();
		
	}
	
	//登陆的异步处理类
	
	private class RegisterAsyncTaskUtil extends AsyncTask<String, Integer, String> {
	    private static final String TAG = "ASYNC_TASK"; 
    	
    	@Override
    	protected void onPreExecute() {

    	}
    	
    	
		@Override
		protected String doInBackground(String... params) {
			Log.i(TAG, "doInBackground(Params... params) called");
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
				Log.e(TAG, e.getMessage());
			}
			return null;
		}
		@Override
		protected void onPostExecute(String result) {

			toast("ע��ɹ�");
			spu.saveStringInSP("USERNAME", string_username_register);
			spu.saveStringInSP("USERPASSWORD", string_userpassword);
			spu.saveIntInSP("LOGINORNOT", 1);
			DBUtil.deletePicFromDB();
			DBUtil.savePicInDB(cameraBitmap);
			goToTheMain();
			
		}

    }
	
	

}