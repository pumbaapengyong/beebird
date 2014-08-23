package am.hour.beebird.my;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;
/**
 *这个类也是用在主界面，可以让滑动到一定位置是，让上面的actionbar消失，需要在jobFragment中使用
 */
public class MyScrollView extends ScrollView {
	private OnScrollListener onScrollListener;
	/**
	 *
	 */
	private int lastScrollY;
	
	public MyScrollView(Context context) {
		this(context, null);
	}
	
	public MyScrollView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	/**
	 *
	 * @param onScrollListener
	 */
	public void setOnScrollListener(OnScrollListener onScrollListener) {
		this.onScrollListener = onScrollListener;
	}


	/**
	 *
	 */
	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			int scrollY = MyScrollView.this.getScrollY();
			
			//��ʱ�ľ���ͼ�¼�µľ��벻��ȣ��ڸ�5�����handler������Ϣ
			if(lastScrollY != scrollY){
				lastScrollY = scrollY;
				handler.sendMessageDelayed(handler.obtainMessage(), 5);  
			}
			if(onScrollListener != null){
				onScrollListener.onScroll(scrollY);
			}
			
		};

	}; 

	/**
	 *
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if(onScrollListener != null){
			onScrollListener.onScroll(lastScrollY = this.getScrollY());
		}
		switch(ev.getAction()){
		case MotionEvent.ACTION_UP:
	         handler.sendMessageDelayed(handler.obtainMessage(), 5);  
			break;
		}
		return super.onTouchEvent(ev);
	}


	/**
	 * 
	 *
	 */
	public interface OnScrollListener{
		/**
		 * @param scrollY
		 * 
		 */
		public void onScroll(int scrollY);
	}
	
	

}

