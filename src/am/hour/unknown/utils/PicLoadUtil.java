package am.hour.unknown.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import am.hour.unknown.Activity.MainApplication;
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

	
	
	//生成一个空白的图层给actionbar使用
	public static Drawable getActionBarDrawable(){
		
		Bitmap actionbar_back = Bitmap.createBitmap(100, 100, Config.ARGB_8888 );
		Canvas canvas = new Canvas (actionbar_back);
        canvas.drawARGB(00, 00, 00, 00);
		
		return new BitmapDrawable(actionbar_back);
		}

   //从资源中加载一幅图片
   public static Bitmap LoadBitmap(Context context,int picID)
   {
	   
//	   Bitmap result=BitmapFactory.decodeResource(res, picId);
//	   return result;
	   
	   BitmapFactory.Options opt = new BitmapFactory.Options();

	   opt.inPreferredConfig = Bitmap.Config.RGB_565;

	   opt.inPurgeable = true;

	   opt.inInputShareable = true;

	   //获取资源图片

	   InputStream is = context.getResources().openRawResource(picID);

	   Bitmap bitmap = BitmapFactory.decodeStream(is,null, opt);

	   try {
		is.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	   return bitmap;
   }
   
   public static BitmapDrawable LoadBitmapDrawable(Context context,int resID){
	   BitmapFactory.Options opt = new BitmapFactory.Options();

	   opt.inPreferredConfig = Bitmap.Config.RGB_565;

	   opt.inPurgeable = true;

	   opt.inInputShareable = true;

	   //获取资源图片

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
   

   
// 裁剪Bitmap图片为圆形
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
   
   //缩放旋转图片的方法
   public static Bitmap scaleToFit(Bitmap bm,float ratio)//缩放图片的方法
   {
	   float width = bm.getWidth(); //图片宽度
	   float height = bm.getHeight();//图片高度	
	   	
	   Matrix m1 = new Matrix(); 
	   m1.postScale(ratio, ratio);   	
	   Bitmap bmResult = Bitmap.createBitmap(bm, 0, 0, (int)width, (int)height, m1, true);//声明位图    
	   return bmResult;
   }
   
   //切割图片的方法
   /*
    * position=1,切割上面的部分，position=2，切割下面的部分
    */
   public static Bitmap getPartOfBitmap(Bitmap bm,int position)//缩放图片的方法
   {
	   float width = bm.getWidth(); //图片宽度
	   float height = bm.getHeight();//图片高度	
	   Bitmap bmResult = null;
	   Matrix m1 = new Matrix(); 
	   float temp = (float)Constant.SCREEN_SCALE[1]/(Constant.SCREEN_SCALE[1]+Constant.SCREEN_SCALE[2]);
	   m1.postScale(1, 1); 
	   if(position==1){
		   bmResult = Bitmap.createBitmap(bm, 0, 0, (int)width, (int)(height*temp), m1, true);//声明位图    
	   }else if(position==2){
		   bmResult = Bitmap.createBitmap(bm, 0, (int)(height*temp), (int)width, (int)(height*(1-temp)), m1, true);//声明位图    
	   }
	   return bmResult;
   }
   
   
   public static Bitmap getPartOfBitmap_Width(Bitmap bm,int position){
	   float width = bm.getWidth(); //图片宽度
	   float height = bm.getHeight();//图片高度	
	   Bitmap bmResult = null;
	   Matrix m1 = new Matrix(); 
	   m1.postScale(1, 1); 
	   bmResult = Bitmap.createBitmap(bm, position, 0, (int)MainApplication.SCREEN_WIDTH, (int)height, m1, true);//声明位图    
	   return bmResult;
	   
   }
   
   //两个维度进行缩放，不等比缩放
   public static Bitmap scaleToFit(Bitmap bm,float wRatio,float hRatio)//缩放图片的方法
   {
	   
	   float width = bm.getWidth(); //图片宽度
	   float height = bm.getHeight();//图片高度	
	   	
	   Matrix m1 = new Matrix(); 
	   m1.postScale(wRatio, hRatio);   	
	   Bitmap bmResult = Bitmap.createBitmap(bm, 0, 0, (int)width, (int)height, m1, true);//声明位图        	
	   return bmResult;
   }
   //缩放旋转图片的方法,使图片全屏,不等比缩放
   public static Bitmap scaleToFitFullScreen(Bitmap bm,float wRatio,float hRatio)//缩放图片的方法
   {
	   
	   float width = bm.getWidth(); //图片宽度
	   float height = bm.getHeight();//图片高度	
	   	
	   Matrix m1 = new Matrix(); 
	   m1.postScale(wRatio, hRatio);   	
	   Bitmap bmResult = Bitmap.createBitmap(bm, 0, 0, (int)width, (int)height, m1, true);//声明位图        	
	   return bmResult;
   }
}
