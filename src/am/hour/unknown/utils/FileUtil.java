package am.hour.unknown.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import am.hour.unknown.Activity.MainApplication;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class FileUtil {
	/**
	 * sd卡的根目录
	 */
	private static String mSdRootPath = Environment.getExternalStorageDirectory().getPath();
	/**
	 * 手机的缓存根目录
	 */
	private static String mDataRootPath  = MainApplication.getContext().getCacheDir().getPath();;
	/**
	 * 保存Image的目录名
	 */
	private final static String FOLDER_NAME = "/BeeBird/pics";

	
	/**
	 * 获取储存Image的目录
	 * @return
	 */
	public static String getStorageDirectory(){
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?
				mSdRootPath + FOLDER_NAME : mDataRootPath + FOLDER_NAME;
	}
	
	/**
	 * 保存Image的方法，有sd卡存储到sd卡，没有就存储到手机目录
	 * @param fileName 
	 * @param bitmap   
	 * @throws IOException
	 */
	public static void savaBitmap(String fileName, Bitmap bitmap) throws IOException{
		if(bitmap == null){
			return;
		}
		String path = getStorageDirectory();
		File folderFile = new File(path);
		if(!folderFile.exists()){
			folderFile.mkdir();
		}
		File file = new File(path + File.separator + fileName.hashCode());
		file.createNewFile();
		FileOutputStream fos = new FileOutputStream(file);
		bitmap.compress(CompressFormat.JPEG, 100, fos);
		fos.flush();
		fos.close();
	}
	
	/**
	 * 从手机或者sd卡获取Bitmap
	 * @param fileName
	 * @return
	 */
	public static Bitmap getBitmap(String fileName){
		System.out.println("开始在文件系统中寻找照片");
		BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        o.inPreferredConfig = Bitmap.Config.RGB_565;
 	    o.inPurgeable = true;
 	    o.inInputShareable = true;
 	    if(null==BitmapFactory.decodeFile(getStorageDirectory() + File.separator + fileName.hashCode(), o)){
 	    	System.out.println("找到的照片为空");
 	    }
 	   return BitmapFactory.decodeFile(getStorageDirectory() + File.separator + fileName.hashCode(), o);
	}
	
	/**
	 * 判断文件是否存在
	 * @param fileName
	 * @return
	 */
	public boolean isFileExists(String fileName){
		return new File(getStorageDirectory() + File.separator + fileName).exists();
	}
	
	/**
	 * 获取文件的大小
	 * @param fileName
	 * @return
	 */
	public long getFileSize(String fileName) {
		return new File(getStorageDirectory() + File.separator + fileName).length();
	}
	
	
	/**
	 * 删除SD卡或者手机的缓存图片和目录
	 */
	public static void deleteFile() {
		File dirFile = new File(getStorageDirectory());
		if(! dirFile.exists()){
			return;
		}
		if (dirFile.isDirectory()) {
			String[] children = dirFile.list();
			for (int i = 0; i < children.length; i++) {
				new File(dirFile, children[i]).delete();
			}
		}
		
		dirFile.delete();
	}

	
	/* Checks if external storage is available for read and write */
	public static boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}

	/* Checks if external storage is available to at least read */
	public static boolean isExternalStorageReadable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state) ||
	        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        return true;
	    }
	    return false;
	}

}
