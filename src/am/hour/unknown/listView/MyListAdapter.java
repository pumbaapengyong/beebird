package am.hour.unknown.listView;

import java.util.ArrayList;
import java.util.HashMap;

import am.hour.unknown.R;
import am.hour.unknown.model.JobShortInfo;
import am.hour.unknown.utils.Constant;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 *@Author: pengyong
 *@Date: 2014-8-11
 **/
public class MyListAdapter extends BaseAdapter {
	
	private Activity activity;
	private static LayoutInflater inflater=null;
	public static HashMap<Integer,JobShortInfo> likedListData = new HashMap<Integer,JobShortInfo>();
	
	public ImageLoader imageLoader; 
	public MyListAdapter(Activity a){
		this.activity = a;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader=new ImageLoader(activity.getApplicationContext());
	}
	
	public int getMapSize(){
		return likedListData.size();
	}

	public void putItemInMap(JobShortInfo a){
		int size = likedListData.size();
		likedListData.put(size, a);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return likedListData.size();
	}

	@Override
	public Object getItem(int paramInt) {
		
		return paramInt;
	}

	@Override
	public long getItemId(int paramInt) {
		return paramInt;
	}
	/*
	 *
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
			
		ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_for_list_for_liked, null);
            holder = new ViewHolder();
            holder.companyLogo = (ImageView)convertView.findViewById(R.id.iv_item_logo);
            holder.jobName = (TextView)convertView.findViewById(R.id.tv_item_jobname);
            holder.likeNumber = (TextView)convertView.findViewById(R.id.tv_item_likenumber);
            holder.salary = (TextView)convertView.findViewById(R.id.tv_item_salary);
            holder.workPlace = (TextView)convertView.findViewById(R.id.tv_item_workplace);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.jobName.setText(likedListData.get(position).getJobName());
        holder.likeNumber.setText(likedListData.get(position).getLikeNumber());
        holder.salary.setText(likedListData.get(position).getSalary());
        holder.workPlace.setText(likedListData.get(position).getWorkPlace());
        imageLoader.DisplayImage(likedListData.get(position).getCompanyLogo(), activity, holder.companyLogo);
  
        return convertView;
		
	}
	
	public static class ViewHolder {
        
        public ImageView companyLogo;
        public TextView jobName;
        public TextView likeNumber;
        public TextView salary;
        public TextView workPlace;
    }

}

