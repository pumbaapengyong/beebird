package am.hour.beebird.utils;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import am.hour.beebird.Activity.MainApplication;
import android.content.Context;
import android.os.Environment;

public class ICUtil4LikedList {
	private static String mSdRootPath = Environment.getExternalStorageDirectory().getPath();

	private static String mDataRootPath  = MainApplication.getContext().getCacheDir().getPath();;

	private final static String FOLDER_NAME = "/BeeBird/LikedList";
	
    private File cacheDir;
    
    public ICUtil4LikedList(){
        //Find the dir to save cached images
        cacheDir = new File(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?
				mSdRootPath + FOLDER_NAME : mDataRootPath + FOLDER_NAME);
        if(!cacheDir.exists())
            cacheDir.mkdirs();
    }
    
    public File getFile(String url){
        //I identify images by hashcode. Not a perfect solution, good for the demo.
        String filename=String.valueOf(url.hashCode());
        File f = new File(cacheDir, filename);
        return f;
        
    }
    
    public void clear(){
        File[] files=cacheDir.listFiles();
        for(File f:files)
            f.delete();
    }

}