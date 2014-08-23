package am.hour.beebird.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import am.hour.beebird.Activity.MainApplication;
import am.hour.beebird.Fragment.JobFragment;
import am.hour.beebird.listView.MyListAdapter;
import am.hour.beebird.model.JobInfo;
import am.hour.beebird.utils.Constant;
import am.hour.beebird.utils.JsonUtil;
import am.hour.unknown.R;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

public class NextJobAsyncTask extends AsyncTask<String, Integer, JobInfo> {
    LogoAsyncTask logoTask = null;
    MainImageAsyncTask imageTask = null;
	
    
	@Override
	protected void onPreExecute() {
		if(null!=logoTask&&logoTask.getStatus()==AsyncTask.Status.RUNNING){
			logoTask.cancel(true);
		}
		if(null!=imageTask&&imageTask.getStatus()==AsyncTask.Status.RUNNING){
			imageTask.cancel(true);
		}
		System.gc();
		JobFragment.tv_middle_jobname.setText("waiting...");
		JobFragment.tv_middle_jobplace.setText("");
		JobFragment.tv_middle_salary.setText("");
		JobFragment.tv_middle_keypoint.setText("");
		JobFragment.tv_companyname.setText("");
		JobFragment.ll_keypoints.removeAllViews();
	}
	
	@Override
	protected JobInfo doInBackground(String... params) {
		String entry = params[0];
		if(entry.length()<=5&&Integer.parseInt(entry)==Constant.NORMAL_ENTRY){
			/*
        	 * 这是点击APP进入的情况，从本地数据库中读取缓存的第一个工作的信息
        	 */
			return JobFragment.dbUtil4JobFragment.getTempJobFromDB();
        	
        }else if(entry.length()<=5&&Integer.parseInt(entry)>=Constant.ABNORMAL_ENTRY){
        	/*
        	 * 这个是从喜欢列表中进入的情况，先读取点击的工作的jobId，之后联网读取
        	 */
        	
        	int jobId = Integer.parseInt(entry)-Constant.ABNORMAL_ENTRY;
        	String url = "http://115.28.232.254:10000/&" +
        			"jobinfdetl&loginname&huoxing001&123456&"
        			+jobId+"&";
    		return getInfoFromNet(url);
        	
        }else{
        	/*
        	 * 点击下一个按钮的情况，直接从网络读取
        	 */
        	
        	return getInfoFromNet(entry);
        }
		
	}
	@Override
	protected void onPostExecute(JobInfo jobInfo) {
		JobFragment.ib_thefirstbutton.setEnabled(true);
		JobFragment.ib_thesecondbutton.setEnabled(true);
		JobFragment.ib_thethirdbutton.setEnabled(true);
        System.out.println(logoTask);
        System.out.println(imageTask);
        logoTask = null;
        imageTask = null;
        System.gc();
        System.out.println(logoTask);
        System.out.println(imageTask);
        logoTask = new LogoAsyncTask();
        imageTask = new MainImageAsyncTask();
        
		if(null==jobInfo)
		{
			Toast.makeText(MainApplication.getContext(), "返回信息为空", 100).show();
			JobFragment.tv_middle_jobname.setText("系统-android开发工程师");
			JobFragment.tv_middle_jobplace.setText("系统-西安");
			JobFragment.tv_middle_salary.setText("系统-月薪10000");
			JobFragment.tv_middle_keypoint.setText("系统-工作一句话描述");
			JobFragment.tv_companyname.setText("系统-西安步云");
			JobFragment.tv_weHave.setText("系统-我们有");
			JobFragment.tv_weHope.setText("系统-我们希望你有");
			logoTask.execute("");
			imageTask.execute("");
			
		}else{
			JobFragment.tv_middle_jobname.setText(jobInfo.getJobName());
			JobFragment.tv_middle_jobplace.setText(jobInfo.getWorkPlace());
			JobFragment.tv_middle_salary.setText(jobInfo.getSalaryType()+jobInfo.getSalary());
			JobFragment.tv_middle_keypoint.setText("一句话描述");
			JobFragment.tv_companyname.setText(jobInfo.getCompanyName());
			JobFragment.tv_weHave.setText(jobInfo.getWeHave());
			JobFragment.tv_weHope.setText(jobInfo.getWeHope());
			JobFragment.wv.loadUrl(jobInfo.getJobLink());
			
			String[] keypoints = jobInfo.getHighLights().split("\\|");
			
			for(int i=1;i<keypoints.length;i++){
				if(!keypoints[i].equals("*")){
					TextView test_tv1 = new TextView(MainApplication.getContext());
		        	test_tv1.setText(keypoints[i]);  
		        	test_tv1.setTextSize(15);
		        	test_tv1.setTextColor(Color.BLACK);
		        	test_tv1.setBackgroundResource(R.drawable.textview_style);
		            JobFragment.ll_keypoints.addView(test_tv1);
				}
				 
			}
			if(null!=jobInfo.getCompanyLogo()){
				logoTask.execute(jobInfo.getCompanyLogo());
			}else{
				Toast.makeText(MainApplication.getContext(), "返回工作logo地址为空", 100).show();
			}
			if(null!=jobInfo.getJobPic()){
				imageTask.execute(jobInfo.getJobPic());
			}else{
				Toast.makeText(MainApplication.getContext(), "返回工作背景地址为空", 100).show();
			}
			
		}
	}
	
	
	private JobInfo getInfoFromNet(String url){
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(url);
			HttpResponse response = client.execute(get);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				InputStream is = entity.getContent();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buf = new byte[1024];
				int length = -1;
				while (!isCancelled()&&(length = is.read(buf)) != -1) {
					baos.write(buf, 0, length);						
				}
				String result =  new String(baos.toByteArray(), "utf-8");
				String[] temp = result.split("&");
				if(temp[1].equals("success")){
					String jsonData = temp[2];
					JobInfo jobInfo = JsonUtil.json2Class4JobFragment(jsonData);
					JobFragment.dbUtil4JobFragment.saveTempJobInDB(jobInfo);
					return jobInfo;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        
		return null;
	}
	
}
