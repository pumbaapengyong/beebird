package am.hour.unknown.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import am.hour.unknown.Activity.MainApplication;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;

//这个类目前貌似还没有怎么使用，现在逻辑有点混乱
public class FileUtil {
	/**
	 *SD卡的根路径
	 */
	private static String mSdRootPath = Environment.getExternalStorageDirectory().getPath();
	/**
	 *内部存储的根路径
	 */
	private static String mDataRootPath  = MainApplication.getContext().getCacheDir().getPath();;
	/**
	 *文件夹的名称
	 */
	private final static String FOLDER_NAME = "/BeeBird/pics";

	
	/**
	 *得到存储的位置，SD卡能用，就用SD卡，不然就用手机的内部存储
	 * @return
	 */
	public static String getStorageDirectory(){
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?
				mSdRootPath + FOLDER_NAME : mDataRootPath + FOLDER_NAME;
	}
	
	/**
	 *将文件存储到相应的位置
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
	 *通过文件名得到图片
	 * @param fileName
	 * @return
	 */
	public static Bitmap getBitmap(String fileName){
		BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        o.inPreferredConfig = Bitmap.Config.RGB_565;
 	    o.inPurgeable = true;
 	    o.inInputShareable = true;
 	    if(null==BitmapFactory.decodeFile(getStorageDirectory() + File.separator + fileName.hashCode(), o)){
 	    }
 	   return BitmapFactory.decodeFile(getStorageDirectory() + File.separator + fileName.hashCode(), o);
	}
	
	/**
	 *判断是否存在
	 * @param fileName
	 * @return
	 */
	public boolean isFileExists(String fileName){
		return new File(getStorageDirectory() + File.separator + fileName).exists();
	}
	
	/**
	 *得到文件的大小
	 * @param fileName
	 * @return
	 */
	public long getFileSize(String fileName) {
		return new File(getStorageDirectory() + File.separator + fileName).length();
	}
	
	
	/**
	 *删除文件
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
