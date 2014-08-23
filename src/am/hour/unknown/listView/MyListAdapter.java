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

	//适配器根据getCount()函数来确定要加载多少项
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
	 * 当列表里的每一项显示到界面时，都会调用这个方法一次，并返回一个view 所以方法里面尽量要简单，不要做没必要的动作(non-Javadoc)
	 * 我这里为了大家好理解，没有做优化
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
//        System.out.println("这是是getview方法，现在读取"+position+"个ITEM");
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

