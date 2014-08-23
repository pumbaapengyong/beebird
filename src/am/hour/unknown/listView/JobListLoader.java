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

	//这是和数据库交互的工具类
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
    //从存储空间或网络上下载图像的方法，应该在线程中调用
    //下载之后就直接存储到了存储空间中，通过Utils中的方法将下载的文件保存了
    private JobShortInfo getJobShortInfo(String url) 
    {
    	String[] strings = url.split("&");
    	int jobId = Integer.parseInt(strings[6]);
    	System.out.println("工作ID是："+jobId);
    	JobShortInfo jsi =  dbUtil4LikedList.getOneJobFromDB(jobId);
    	if(jsi.getJobId()!=null){
    		System.out.println("在数据库中找到了这个记录,工作ID是："+jsi.getJobId());
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
    			System.out.println("读取列表中的内容如下"+result);
    			String temp[] = result.split("&");
    			jsi = JsonUtil.json2Class4LikedFragment(temp[2]);
    			System.out.println("列表中的内容解析出的内容是："+jsi.toString());
    			System.out.println("向数据库存数据咯");  			
    			dbUtil4LikedList.saveJobInDB(jsi);	
    			return jsi;
                } 
            }catch (Exception ex){
            	System.out.println("读取列表中的内容失败,这是第"+Utils.findURL(url)+"个item");
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
		
		

    	
    	//doInBackground方法内部执行后台任务,不可在此方法内修改UI
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
                        	//将最新的下载对象弹出
                        	url=jobsToLoad.poll();
                        }
                        //这个方法中有具体的下载过程，方法的执行时间较长，所以在线程中调用
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
			     LikedFragment.refreshData(progresses[0]);
    	}
		
		//onPostExecute方法用于在执行完后台任务后更新UI,显示结果
		@Override
		protected void onPostExecute(String result) {

			}
			
		}
    


}