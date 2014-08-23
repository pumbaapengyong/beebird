package am.hour.unknown.listView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.Stack;
import java.util.WeakHashMap;

import am.hour.unknown.R;
import am.hour.unknown.utils.ICUtil4LikedList;
import am.hour.unknown.utils.PicLoadUtil;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

public class ImageLoader {
    /*
     * ͼƬ�Ļ�����󣬰���get��put��clear����
     * */
	
    MemoryCache memoryCache=new MemoryCache();
    /*
     *将图片缓存至手机内存卡的类
     * */
    ICUtil4LikedList imageCache;
    //默认的显示图
    final int stub_id=R.drawable.fakelogo;

    PhotosQueue photosQueue=new PhotosQueue();
    //下载图片的线程
    PhotosLoader photoLoaderThread=new PhotosLoader();
    //保存地址和imageview的对应关系
    private Map<ImageView, String> imageViews=Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    //构造函数
    public ImageLoader(Context context){
        //Make the background thead low priority. This way it will not affect the UI performance
        photoLoaderThread.setPriority(Thread.NORM_PRIORITY-1);
        
        imageCache=new ICUtil4LikedList();
    }
    
    
    public void DisplayImage(String url, Activity activity, ImageView imageView)
    {

    	
        imageViews.put(imageView, url);
        Bitmap bitmap=memoryCache.get(url);
        //如果从内存里面读取的内容不是空
        if(bitmap!=null){
            imageView.setImageBitmap(bitmap);
            
        }else{
            //加入到下载队列中
            queuePhoto(url, activity, imageView);
            imageView.setImageResource(stub_id); 
        }
          
    }
        
    private void queuePhoto(String url, Activity activity, ImageView imageView)
    {
        //This ImageView may be used for other images before. So there may be some old tasks in the queue. We need to discard them. 
        photosQueue.Clean(imageView);
        PhotoToLoad p=new PhotoToLoad(url, imageView);
        synchronized(photosQueue.photosToLoad){
            photosQueue.photosToLoad.push(p);
            photosQueue.photosToLoad.notifyAll();
        }
        
        //start thread if it's not started yet
        if(photoLoaderThread.getState()==Thread.State.NEW)
            photoLoaderThread.start();
    }
    //从文件或者网络中读取图片
    private Bitmap getBitmap(String url) 
    {
        File f=imageCache.getFile(url);
        
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
            //将图片保存到文件系统中
            Utils.CopyStream(is, os);
            os.close();
            bitmap = decodeFile(f);
            return bitmap;
        } catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    //decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f){
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);
            
            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE=70;
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
            while(true){
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }
            
            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
//            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
            //裁剪成为圆形的图片
            return PicLoadUtil.toRoundBitmap(BitmapFactory.decodeStream(new FileInputStream(f), null, o2));
        } catch (FileNotFoundException e) {}
        return null;
    }
    
    public void stopThread()
    {
        photoLoaderThread.interrupt();
    }
    
    public void clearCache() {
        memoryCache.clear();
        imageCache.clear();
    }
    
    
    
    //下载任务的记录类
    private class PhotoToLoad
    {
        public String url;
        public ImageView imageView;
        public PhotoToLoad(String u, ImageView i){
            url=u; 
            imageView=i;
        }
    }
    
    //stores list of photos to download
    class PhotosQueue
    {
        private Stack<PhotoToLoad> photosToLoad=new Stack<PhotoToLoad>();
        
        //removes all instances of this ImageView
        public void Clean(ImageView image)
        {
            for(int j=0 ;j<photosToLoad.size();){
                if(photosToLoad.get(j).imageView==image)
                    photosToLoad.remove(j);
                else
                    ++j;
            }
        }
        
        
        
    }
    
    class PhotosLoader extends Thread {
        public void run() {
            try {
                while(true)
                {
                    //thread waits until there are any images to load in the queue
                    if(photosQueue.photosToLoad.size()==0)
                        synchronized(photosQueue.photosToLoad){
                            photosQueue.photosToLoad.wait();
                        }
                    if(photosQueue.photosToLoad.size()!=0)
                    {
                        PhotoToLoad photoToLoad;
                        synchronized(photosQueue.photosToLoad){
                        	//弹出一个下载任务
                            photoToLoad=photosQueue.photosToLoad.pop();
                        }
                        //从文件或者网络中读取图片
                        Bitmap bmp=getBitmap(photoToLoad.url);
//                        bmp = PicLoadUtil.toRoundBitmap(bmp);
                        memoryCache.put(photoToLoad.url, bmp);
                        
                        String tag=imageViews.get(photoToLoad.imageView);
                        if(tag!=null && tag.equals(photoToLoad.url)){
                            BitmapDisplayer bd=new BitmapDisplayer(bmp, photoToLoad.imageView);
                            Activity a=(Activity)photoToLoad.imageView.getContext();
                            a.runOnUiThread(bd);
                        }
                    }
                    if(Thread.interrupted())
                        break;
                }
            } catch (InterruptedException e) {
                //allow thread to exit
            }
        }
  
    }
    
    
    
    //Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable
    {
        Bitmap bitmap;
        ImageView imageView;
        public BitmapDisplayer(Bitmap b, ImageView i){bitmap=b;imageView=i;}
        public void run()
        {
            if(bitmap!=null)
                imageView.setImageBitmap(bitmap);
            else
                imageView.setImageResource(stub_id);
        }
    }



}
