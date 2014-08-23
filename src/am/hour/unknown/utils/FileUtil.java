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
	 * sd���ĸ�Ŀ¼
	 */
	private static String mSdRootPath = Environment.getExternalStorageDirectory().getPath();
	/**
	 * �ֻ��Ļ����Ŀ¼
	 */
	private static String mDataRootPath  = MainApplication.getContext().getCacheDir().getPath();;
	/**
	 * ����Image��Ŀ¼��
	 */
	private final static String FOLDER_NAME = "/BeeBird/pics";

	
	/**
	 * ��ȡ����Image��Ŀ¼
	 * @return
	 */
	public static String getStorageDirectory(){
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?
				mSdRootPath + FOLDER_NAME : mDataRootPath + FOLDER_NAME;
	}
	
	/**
	 * ����Image�ķ�������sd���洢��sd����û�оʹ洢���ֻ�Ŀ¼
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
	 * ���ֻ�����sd����ȡBitmap
	 * @param fileName
	 * @return
	 */
	public static Bitmap getBitmap(String fileName){
		System.out.println("��ʼ���ļ�ϵͳ��Ѱ����Ƭ");
		BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        o.inPreferredConfig = Bitmap.Config.RGB_565;
 	    o.inPurgeable = true;
 	    o.inInputShareable = true;
 	    if(null==BitmapFactory.decodeFile(getStorageDirectory() + File.separator + fileName.hashCode(), o)){
 	    	System.out.println("�ҵ�����ƬΪ��");
 	    }
 	   return BitmapFactory.decodeFile(getStorageDirectory() + File.separator + fileName.hashCode(), o);
	}
	
	/**
	 * �ж��ļ��Ƿ����
	 * @param fileName
	 * @return
	 */
	public boolean isFileExists(String fileName){
		return new File(getStorageDirectory() + File.separator + fileName).exists();
	}
	
	/**
	 * ��ȡ�ļ��Ĵ�С
	 * @param fileName
	 * @return
	 */
	public long getFileSize(String fileName) {
		return new File(getStorageDirectory() + File.separator + fileName).length();
	}
	
	
	/**
	 * ɾ��SD�������ֻ��Ļ���ͼƬ��Ŀ¼
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
