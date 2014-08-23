package am.hour.unknown.Fragment;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import am.hour.unknown.Activity.MainActivity;
import am.hour.unknown.utils.Constant;
import am.hour.unknown.R;
import am.hour.unknown.utils.SharedPreferenceUtil;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MyDialogFragment extends DialogFragment
{
	
	

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		int i = getArguments().getInt(Constant.ARG_FRAGMENT_INFO);
		/*
		 *反馈的dialog
		 * */
		if(i==Constant.FEEDBACK_DIALOG){
			final View view = inflater.inflate(R.layout.dialog_feedback, null);
			builder.setView(view).setPositiveButton("发送",  
                    new DialogInterface.OnClickListener()  
                    {  
                        @Override  
                        public void onClick(DialogInterface dialog, int id)  
                        {  
                        	EditText et_feedback = (EditText)view.findViewById(R.id.id_txt_feedback);
                        	String string_feedback = et_feedback.getText().toString().trim();
                        	String username = MainActivity.spu.getStringFromSP("USERNAME", "");
                        	String feedback_url = "http://115.28.232.254:10000/&bugreport&"
                        	        +string_feedback+"&loginname&"+username+"&";
                        	DialogAsyncTask feedBackTask = new DialogAsyncTask();
                        	feedBackTask.execute("1",feedback_url);
                        	
                        }  
                    }).setNegativeButton("取消", null);
			
			return builder.create();
		}
		/*
		 *关于按钮
		 * */
		if(i==Constant.ABOUT_DIALOG){
			View view = inflater.inflate(R.layout.dialog_about, null);
			builder.setView(view).setNegativeButton("关于", null);
			
			return builder.create();
		}
		/*
		 *更新按钮
		 * */
		if(i==Constant.UPDATE_DIALOG){
			final View view = inflater.inflate(R.layout.dialog_update, null);
			builder.setView(view).setPositiveButton("�������",  
                    new DialogInterface.OnClickListener()  
                    {  
                        @Override  
                        public void onClick(DialogInterface dialog, int id)  
                        {  
//                        	String string_feedback = et_feedback.getText().toString().trim();
//                        	String username = MainActivity.spu.getStringFromSP("USERNAME", "");
//                        	String feedback_url = "http://115.28.232.254:10000/&bugreport&"
//                        	        +string_feedback+"&loginname&"+username+"&";
//                        	DialogAsyncTask feedBackTask = new DialogAsyncTask();
//                        	feedBackTask.execute("1",feedback_url);
                        	
                        }});
			return builder.create();
		}
		/*
		 *更改密码
		 * */
		if(i==Constant.NEW_PASSWORD_DIALOG){
			final View view = inflater.inflate(R.layout.dialog_new_password, null);
			builder.setView(view).setPositiveButton("确认修改",  
                    new DialogInterface.OnClickListener()  
                    {  
                        @Override  
                        public void onClick(DialogInterface dialog, int id)  
                        {  
                        	EditText et_old_password = (EditText)view.findViewById(R.id.et_old_password);
                        	EditText et_new_password = (EditText)view.findViewById(R.id.et_new_password);
                        	EditText et_new_password_confirm = (EditText)view.findViewById(R.id.et_new_password_confirm);
                        	String string_old_password = et_old_password.getText().toString().trim();
                        	String string_new_password = et_new_password.getText().toString().trim();
                        	String string_new_password_confirm = et_new_password_confirm.getText().toString().trim();
                        	String string_old_password_sp = MainActivity.spu.getStringFromSP("PASSWORD", "");
                        	if(string_new_password == null 
            						|| string_new_password.isEmpty()){
                        		toast("新密码为空");
                        	}else if(string_new_password_confirm == null 
            						|| string_new_password_confirm.isEmpty()){
                        		toast("密码确认为空");
                        	}else if(!string_new_password_confirm.equals(string_new_password)){
                        		toast("两次输入的密码不相同");
                        	}else if(!string_old_password.equals(string_old_password_sp)){
                        		toast("密码输入错误");
                        	}else{
                        		
                        		/*
                        		 *联网的进程
                        		 * 
                        		 * */
                        		
                        		//spu.saveStringInSP("PASSWORD", string_new_password);
                        	}
                        	
                        	
                        	
                        	
                        }  
                    }).setNegativeButton("取消", null);
			return builder.create();
		}
		return null;
	}
	
	public void toast(String toast){
		Toast.makeText(getActivity(), toast, 500).show();
	}
	
	   public class DialogAsyncTask extends AsyncTask<String, Integer, String> { 
		    int i;
			@Override
			protected String doInBackground(String... params) {
				i = Integer.parseInt(params[0]);
				try {
					HttpClient client = new DefaultHttpClient();
					HttpGet get = new HttpGet(params[1]);
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
				switch (i){
				
				case 1:
					if(result.equals("&sucess&"))
					toast("Great!密码修改成功");
				}
			}
			

	    }

	
	
	
}