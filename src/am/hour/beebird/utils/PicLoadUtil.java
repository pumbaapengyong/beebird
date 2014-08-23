package am.hour.beebird.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import am.hour.beebird.Activity.MainApplication;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class PicLoadUtil 
{

	
	
	//得到一个透明的图层，给actionbar使用
	public static Drawable getActionBarDrawable(){
		
		Bitmap actionbar_back = Bitmap.createBitmap(100, 100, Config.ARGB_8888 );
		Canvas canvas = new Canvas (actionbar_back);
        canvas.drawARGB(00, 00, 00, 00);
		
		return new BitmapDrawable(actionbar_back);
		}

   //加载图片，能减少内存的占用
   public static Bitmap LoadBitmap(Context context,int picID)
   {
	   
	   BitmapFactory.Options opt = new BitmapFactory.Options();

	   opt.inPreferredConfig = Bitmap.Config.RGB_565;

	   opt.inPurgeable = true;

	   opt.inInputShareable = true;

	   InputStream is = context.getResources().openRawResource(picID);

	   Bitmap bitmap = BitmapFactory.decodeStream(is,null, opt);

	   try {
		is.close();
	} catch (IOException e) {
		e.printStackTrace();
	}

	   return bitmap;
   }
   
   public static BitmapDrawable LoadBitmapDrawable(Context context,int resID){
	   BitmapFactory.Options opt = new BitmapFactory.Options();

	   opt.inPreferredConfig = Bitmap.Config.RGB_565;

	   opt.inPurgeable = true;

	   opt.inInputShareable = true;
	   InputStream is = context.getResources().openRawResource(resID);

	   Bitmap bitmap = BitmapFactory.decodeStream(is,null, opt);

	   try {
		is.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	   return new BitmapDrawable(context.getResources(),bitmap);
   }
   

   
//裁剪成为圆形的图片
	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);

		paint.setAntiAlias(true);

		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}
   
   //图片的等比缩放
   public static Bitmap scaleToFit(Bitmap bm,float ratio)
   {
	   float width = bm.getWidth();
	   float height = bm.getHeight();
	   	
	   Matrix m1 = new Matrix(); 
	   m1.postScale(ratio, ratio);   	
	   Bitmap bmResult = Bitmap.createBitmap(bm, 0, 0, (int)width, (int)height, m1, true);//����λͼ    
	   return bmResult;
   }
   
   //将图片一分为二，首页上面使用
   /*
    * position=1,上面的部分，position=2，下面的部分
    */
   public static Bitmap getPartOfBitmap(Bitmap bm,int position)
   {
	   float width = bm.getWidth();
	   float height = bm.getHeight();
	   Bitmap bmResult = null;
	   Matrix m1 = new Matrix(); 
	   float temp = (float)Constant.SCREEN_SCALE[1]/(Constant.SCREEN_SCALE[1]+Constant.SCREEN_SCALE[2]);
	   m1.postScale(1, 1); 
	   if(position==1){
		   bmResult = Bitmap.createBitmap(bm, 0, 0, (int)width, (int)(height*temp), m1, true);//����λͼ    
	   }else if(position==2){
		   bmResult = Bitmap.createBitmap(bm, 0, (int)(height*temp), (int)width, (int)(height*(1-temp)), m1, true);//����λͼ    
	   }
	   return bmResult;
   }

   
   //不等比缩放
   public static Bitmap scaleToFit(Bitmap bm,float wRatio,float hRatio)//����ͼƬ�ķ���
   {
	   
	   float width = bm.getWidth();
	   float height = bm.getHeight();
	   	
	   Matrix m1 = new Matrix(); 
	   m1.postScale(wRatio, hRatio);   	
	   Bitmap bmResult = Bitmap.createBitmap(bm, 0, 0, (int)width, (int)height, m1, true);//����λͼ        	
	   return bmResult;
   }
}
