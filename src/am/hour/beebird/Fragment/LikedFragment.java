package am.hour.beebird.Fragment;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import am.hour.beebird.Activity.MainActivity;
import am.hour.beebird.Activity.MainApplication;
import am.hour.beebird.listView.JobListLoader;
import am.hour.beebird.listView.MyListAdapter;
import am.hour.beebird.model.JobShortInfo;
import am.hour.beebird.utils.Constant;
import am.hour.beebird.utils.DBUtil4LikedList;
import am.hour.unknown.R;
import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LikedFragment extends Fragment implements OnItemClickListener{

	JobListLoader jobListLoader = null;
	public static MyListAdapter adapter ;
	public View rootView = null;
	public ListView likedList = null;
	public LayoutParams lp = null;
    public static DBUtil4LikedList dbUtil4LikedList = null;
    public LikedFragment() {
        // Empty constructor required for fragment subclasses
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
   	
        rootView = inflater.inflate(R.layout.fragment_likedlist, container, false);
        int i = getArguments().getInt(Constant.ARG_FRAGMENT_INFO);
        dbUtil4LikedList = new DBUtil4LikedList();
        adapter = new MyListAdapter(getActivity());
        
        getandsetViews();
        initData4Adapter();         
        ListAsyncTask listAsyncTask = new ListAsyncTask();
        listAsyncTask.execute("http://115.28.232.254:10000/&userfavorjob&getid&email&huoxing001@sohu.com&123456&1&");
        return rootView;
    }
    
    private void getandsetViews(){
    	likedList = (ListView)rootView.findViewById(R.id.list_liked);
               
        likedList.setAdapter(adapter);  
        likedList.setOnItemClickListener(this);
        lp = likedList.getLayoutParams();    	 
   	    ((MarginLayoutParams) lp).setMargins(0,MainApplication.ACTIONBAR_HEIGHT, 0, 0);
    	 likedList.setLayoutParams(lp);	 
    	int titleId = Resources.getSystem().getIdentifier(
					"action_bar_title", "id", "android");
        TextView actionbarTitle = (TextView) getActivity().findViewById(titleId);
        actionbarTitle.setTextColor(Color.BLACK);
    }
    
    
	//读取数据库中的列表
    public void initData4Adapter(){  	
    	HashMap<Integer,JobShortInfo> hashMap = dbUtil4LikedList.getJobsFromDB();
    	if(hashMap.size()!=adapter.getMapSize()){
    		for(int i=0;i<hashMap.size();i++){
        		JobShortInfo jsi = new JobShortInfo();
        		jsi = hashMap.get(i+1);
        		adapter.putItemInMap(jsi);
        	}
    	}
    	
    }
    //每次下载成功一个职位，就更新一次数据
    public static void refreshData(JobShortInfo jsi){
    	adapter.putItemInMap(jsi);
    }
    
    
    @Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub  
    	jobListLoader.jobLoaderThread.cancel(true);
    	dbUtil4LikedList.closeDB();
        ((MainActivity)getActivity()).selectItem(1,Constant.ABNORMAL_ENTRY+Integer.parseInt(MyListAdapter.likedListData.get(arg2).getJobId()));
    		
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		jobListLoader.jobLoaderThread.cancel(true);
		dbUtil4LikedList.closeDB();
		super.onDestroy();
	}
    
	private class ListAsyncTask extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... params) {
			
			try {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(params[0]);
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
					return new String(baos.toByteArray(), "utf-8");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(String result) {
			jobListLoader = new JobListLoader();
			String[] strings = result.split("&");
			strings = strings[2].split("\\|");
			int temp;
			String url = null;
			for(int i=0;i<(strings.length-1)/2;i++){
				//������3��ʱ���ܳ�����7����Ч�ֶ���1,3,5
				temp = Integer.parseInt(strings[2*i+1]);
				url = "http://115.28.232.254:10000/&userfavorjob&" +
						"getinfor&email&huoxing001@sohu.com&123456&"+
						temp+"&";
				jobListLoader.queueJob(url);
			}
			
		}
    }

	
    
}
