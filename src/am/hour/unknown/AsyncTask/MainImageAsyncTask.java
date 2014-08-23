package am.hour.unknown.AsyncTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import am.hour.unknown.R;
import am.hour.unknown.Activity.MainApplication;
import am.hour.unknown.Fragment.JobFragment;
import am.hour.unknown.utils.Constant;
import am.hour.unknown.utils.FastBlur;
import am.hour.unknown.utils.ICUtil4JobFragment;
import am.hour.unknown.utils.PicLoadUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.Toast;

public class MainImageAsyncTask extends AsyncTask<String, Integer, Drawable[]> {
	
	
	@Override
	protected Drawable[] doInBackground(String... params) {
		Drawable background = null;
		Drawable background_blur_up = null;
		Drawable background_blur_below = null;
		Drawable[] drawables = new BitmapDrawable[3];
		
		Bitmap pic_background=getBitmap(params[0]);
		if(null==pic_background){
			pic_background = PicLoadUtil.LoadBitmap(MainApplication.getContext(), R.drawable.example2);
		}
    	Bitmap pic_background_1 = PicLoadUtil.scaleToFit(pic_background,(float)0.1);
        Canvas canvas = new Canvas (pic_background_1);
        canvas.drawARGB(70, 0, 0, 0);
        if(!isCancelled()){
        	Bitmap pic_background_3 = FastBlur.doBlur(pic_background_1,5,true);
        	Bitmap pic_background_blur_up = PicLoadUtil.getPartOfBitmap(pic_background_3, 1);
        	Bitmap pic_background_blur_below = PicLoadUtil.getPartOfBitmap(pic_background_3, 2);
        	background = new BitmapDrawable(PicLoadUtil.scaleToFit(pic_background,
            		(float)(MainApplication.SCREEN_WIDTH)/pic_background.getWidth(),
            		(float)(MainApplication.DISAPLAY_HEIGHT)*Constant.SCREEN_SCALE[0]/100/pic_background.getHeight()));
            background_blur_up = new BitmapDrawable(PicLoadUtil.scaleToFit(pic_background_blur_up,
            		(float)(MainApplication.SCREEN_WIDTH)/pic_background_blur_up.getWidth(),
            		(float)(MainApplication.DISAPLAY_HEIGHT)*Constant.SCREEN_SCALE[1]/100/pic_background_blur_up.getHeight()));
            background_blur_below = new BitmapDrawable(PicLoadUtil.scaleToFit(pic_background_blur_below,
            		(float)(MainApplication.SCREEN_WIDTH)/pic_background_blur_below.getWidth(),
            		(float)(MainApplication.DISAPLAY_HEIGHT)*Constant.SCREEN_SCALE[2]/100/pic_background_blur_below.getHeight()));		
			drawables[0] = background;
			drawables[1] = background_blur_up;
			drawables[2] = background_blur_below;
			return drawables;
        }
    	return null;
			

		
	}
	
	@Override
	protected void onPostExecute(Drawable[] result) {
		if(null==result){
			Toast.makeText(MainApplication.getContext(), "���Ϊ�գ�����ͼƬ����", 100).show();
		}else{
			JobFragment.iv_bottom_up.setBackgroundDrawable(result[0]);
			JobFragment.iv_bottom_below.setBackgroundDrawable(result[1]);
			JobFragment.ll_buttons.setBackgroundDrawable(result[2]);
		}
		
		
	}
	
	private Bitmap getBitmap(String url) 
    {
    	
    	if(url.length()==0||url.isEmpty()){
    		
    		return PicLoadUtil.LoadBitmap(MainApplication.getContext(), R.drawable.example2);
    	}else{
    		File f=JobFragment.imageCache.getFile(url);
	        Bitmap b = decodeFile(f);
	        if(b!=null){
	        	
	        	return b;
	        }
	        //from web
	        try {
	            Bitmap bitmap=null;
	            URL imageUrl = new URL(url);
	            HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
	            conn.setConnectTimeout(20000);
	            conn.setReadTimeout(30000);
	            InputStream is=conn.getInputStream();
	            OutputStream os = new FileOutputStream(f);
	            final int buffer_size=1024;
	            try
	            {
	                byte[] bytes=new byte[buffer_size];
	                for(;;)
	                {
	                  int count=is.read(bytes, 0, buffer_size);
	                  if(count==-1||isCancelled())
	                      break;
	                  os.write(bytes, 0, count);
	                }
	            }catch(Exception e){}
	            os.close();
	            bitmap = decodeFile(f);
	            return bitmap;
	        } catch (Exception ex){
	            ex.printStackTrace();
	            return null;
	        }
    	}
        
    }
    
    private Bitmap decodeFile(File f){
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inPreferredConfig = Bitmap.Config.RGB_565;
     	    o.inPurgeable = true;
     	    o.inInputShareable = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);
            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE=700;
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
            while(true){
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
                if(isCancelled())
                	break;
            }
            
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }
	
}