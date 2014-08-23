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
import am.hour.unknown.utils.ICUtil4JobFragment;
import am.hour.unknown.utils.PicLoadUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

public class LogoAsyncTask extends AsyncTask<String, Integer, Drawable> {

    @Override
	protected void onPreExecute() {
		System.out.println("ͼ����"+"onPreExecute");
//		JobFragment.iv_middle_companylogo.setBackgroundColor(Color.YELLOW);
	}

	@Override
	protected Drawable doInBackground(String... params) {
		BitmapDrawable logo = null;
		Bitmap temp = getBitmap(params[0]);
		if(null==temp){
			temp = PicLoadUtil.LoadBitmap(MainApplication.getContext(), R.drawable.google);
		}else{
			temp = PicLoadUtil.toRoundBitmap(temp);
			
		}
		logo = new BitmapDrawable(PicLoadUtil.scaleToFit(temp,
        		(float)(MainApplication.DISAPLAY_HEIGHT)*14/100/temp.getWidth(),
        		(float)(MainApplication.DISAPLAY_HEIGHT)*14/100/temp.getHeight()));
		return logo;
	}
	
	@Override
	protected void onPostExecute(Drawable result) {
		System.out.println("logo���onPostExecut");
		if(null!=result){
			JobFragment.iv_middle_companylogo.setBackgroundDrawable(result);
		}

	}
	
	private Bitmap getBitmap(String url) 
    {
		//首次进入的时候，地址为空，返还默认图片
    	if(url.length()==0||url.isEmpty()){
    		return PicLoadUtil.LoadBitmap(MainApplication.getContext(), R.drawable.google);
    	}else{
    		File f=JobFragment.imageCache.getFile(url);
	        //from SD cache
	        Bitmap b = decodeFile(f);
	        if(b!=null){
	        	return b;
	        }
	        //from web
	        try {
	            Bitmap bitmap=null;
	            URL imageUrl = new URL(url);
	            HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
	            conn.setConnectTimeout(30000);
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
	            }
	            catch(Exception ex){}
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
            final int REQUIRED_SIZE=200;
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
            
            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }

}
