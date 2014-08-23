package am.hour.unknown.listView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import am.hour.unknown.Fragment.LikedFragment;
import am.hour.unknown.model.JobShortInfo;
import am.hour.unknown.utils.DBUtil4LikedList;
import am.hour.unknown.utils.JsonUtil;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.util.Log;

public class JobListLoader {

	//数据库操作类
    DBUtil4LikedList dbUtil4LikedList = null;
    private Queue<String> jobsToLoad=new LinkedList<String>();
    public JobsLoader jobLoaderThread=new JobsLoader();
    public JobListLoader(){
        //Make the background thead low priority. This way it will not affect the UI performance
    	    
    	dbUtil4LikedList=new DBUtil4LikedList();
    }
          
    public void queueJob(String url)
    {
        //This ImageView may be used for other images before. So there may be some old tasks in the queue. We need to discard them.  
        synchronized(jobsToLoad){
        	jobsToLoad.offer(url);
        	jobsToLoad.notifyAll();
        }
        
        //start thread if it's not started yet
        if(jobLoaderThread.getStatus()==Status.PENDING)
        	jobLoaderThread.execute("");
    }
    //从该数据库中读取工作信息或者从网络上下载
    private JobShortInfo getJobShortInfo(String url) 
    {
    	String[] strings = url.split("&");
    	int jobId = Integer.parseInt(strings[6]);
    	JobShortInfo jsi =  dbUtil4LikedList.getOneJobFromDB(jobId);
    	if(jsi.getJobId()!=null){
    		
    	}else{
    		//from web
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
    				while ((length = is.read(buf)) != -1) {
    					baos.write(buf, 0, length);
    				}
    			String result =	new String(baos.toByteArray(), "utf-8");
    			String temp[] = result.split("&");
    			jsi = JsonUtil.json2Class4LikedFragment(temp[2]);			
    			dbUtil4LikedList.saveJobInDB(jsi);	
    			return jsi;
                } 
            }catch (Exception ex){
                ex.printStackTrace();
            }
    	}
    	return null;
    }

    public void stopThread()
    {
    	jobLoaderThread.cancel(true);
    }


    public class JobsLoader extends AsyncTask<String, JobShortInfo, String> {

		@Override
		protected String doInBackground(String... params) {
			try {
                while(true)
                {
                    //thread waits until there are any images to load in the queue
//                    if(jobsToLoad.size()==0)
//                        synchronized(jobsToLoad){
//                            jobsToLoad.wait();
//                        }
                    if(jobsToLoad.size()!=0)
                    {
                        String url;
                        synchronized(jobsToLoad){
                        	//弹出一个下载任务
                        	url=jobsToLoad.poll();
                        }
                        JobShortInfo jsi = getJobShortInfo(url);
                        publishProgress(jsi);
                    }
                    if(isCancelled()||jobsToLoad.size()==0)
                        break;
                }
            } catch (Exception e) {
                //allow thread to exit
            }
			
			return null;
		}
		
		@Override
    	protected void onProgressUpdate(JobShortInfo... progresses) {
			if(null!=progresses[0])
				//将下载的结果返回给列表刷新
			     LikedFragment.refreshData(progresses[0]);
    	}
		
		@Override
		protected void onPostExecute(String result) {

			}
			
		}
    


}