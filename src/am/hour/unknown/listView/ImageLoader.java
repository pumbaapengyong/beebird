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
     * 图片的缓存对象，包括get，put，clear方法
     * */
	
    MemoryCache memoryCache=new MemoryCache();
    /*
     * 文件存储对象，包括getfile，clear方法
     * */
    ICUtil4LikedList imageCache;
    //这个是默认的图片，没有加载成功的时候使用这张图片
    final int stub_id=R.drawable.fakelogo;

    PhotosQueue photosQueue=new PhotosQueue();
    //从栈中取出要下载的内容，并进行下载的线程
    PhotosLoader photoLoaderThread=new PhotosLoader();
    //保存imageview和URL的对应关系，这个是弱引用，有可能被操作系统回收掉
    private Map<ImageView, String> imageViews=Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    //使下载线程的优先级变高，优先执行，并且生成文件对象
    //这个也是adapter的一个对象
    public ImageLoader(Context context){
        //Make the background thead low priority. This way it will not affect the UI performance
        photoLoaderThread.setPriority(Thread.NORM_PRIORITY-1);
        
        imageCache=new ICUtil4LikedList();
    }
    
    //Adapter中的ImageLoader对象调用的方法
    //如果对内存中有缓存，就直接使用，不然就加到下载队列里面
    //这个地方修改下，若能够在文件中，就直接显示，不要经过栈的调整，栈的调整中，
    //可能在别的任务都下载之后才会从文件读取这个,也不知道从文件中读取文件的时间是不是很长
    public void DisplayImage(String url, Activity activity, ImageView imageView)
    {

    	
        imageViews.put(imageView, url);
        Bitmap bitmap=memoryCache.get(url);
        //先在内存中找
        if(bitmap!=null){
//        	System.out.println("居然能在！！内存！！中找到，太尼玛神奇了,这是第"+Utils.findURL(url)+"个item");
            imageView.setImageBitmap(bitmap);
            
        }else{
        	
        	//内存没有，加入到下载队列中
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
    //从存储空间或网络上下载图像的方法，应该在线程中调用
    //下载之后就直接存储到了存储空间中，通过Utils中的方法将下载的文件保存了
    private Bitmap getBitmap(String url) 
    {
        File f=imageCache.getFile(url);
        
        //from SD cache
        Bitmap b = decodeFile(f);
        if(b!=null){
//        	System.out.println("居然能在！！文件！！中找到，太尼玛神奇了,这是第"+Utils.findURL(url)+"个item");
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
            //将网络上的文件流保存到本地
            Utils.CopyStream(is, os);
            os.close();
            bitmap = decodeFile(f);
//            System.out.println("居然能在！！网络！！中找到，太尼玛神奇了,这是第"+Utils.findURL(url)+"个item");
            return bitmap;
        } catch (Exception ex){
        	System.out.println("网络读取失败,这是第"+Utils.findURL(url)+"个item");
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
            //将图像裁剪成圆形
            return PicLoadUtil.toRoundBitmap(BitmapFactory.decodeStream(new FileInputStream(f), null, o2));
        } catch (FileNotFoundException e) {}
        return null;
    }
    
    public void stopThread()
    {
        photoLoaderThread.interrupt();
    }
    
    public void clearCache() {
    	System.out.println("清空所有的存储");
        memoryCache.clear();
        imageCache.clear();
    }
    
    
    
    //Task for the queue
    //保存了一个要去下载的地址和对应的imageview信息
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
    //这是一个保存了要去下载的图像的线程的栈，栈中有push方法和clean方法
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
                        	//将最新的下载对象弹出
                            photoToLoad=photosQueue.photosToLoad.pop();
                        }
                        //这个方法中有具体的下载过程，方法的执行时间较长，所以在线程中调用
                        Bitmap bmp=getBitmap(photoToLoad.url);
//                        bmp = PicLoadUtil.toRoundBitmap(bmp);
                        memoryCache.put(photoToLoad.url, bmp);
                        
                        //imageView中保存着imageview到url的映射，这个是弱引用关系，可能会被系统回收
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
    //一个设置图像的线程，生成这个线程的作用是为了能够在主线程中使用此线程更新界面
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
