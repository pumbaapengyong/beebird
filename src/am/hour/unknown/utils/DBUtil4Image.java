package am.hour.unknown.utils;

import java.io.ByteArrayOutputStream;

import am.hour.unknown.Activity.MainApplication;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class DBUtil4Image {
	private PicSQLiteOpenHelper myPicSQLiteOpenHelper = null;
	private SQLiteDatabase mydb = null;
	private Context context;
	/*
	 * type就是这个东西是存储图片还是存储工作
	 * */
	public DBUtil4Image(){
		this.context=MainApplication.getContext();
		myPicSQLiteOpenHelper = new PicSQLiteOpenHelper(context, "saveimage.db", null,
				1);
		// 创建一个可读写的数据库
		mydb = myPicSQLiteOpenHelper.getWritableDatabase();
		
	}
	

	
    public void savePicInDB(Bitmap bitmap1){
    	try {
    		
            //将图片转化为位图
    		//Bitmap bitmap1=BitmapFactory.decodeResource(getResources(), R.drawable.erweima);	
    		int size=bitmap1.getWidth()*bitmap1.getHeight()*4;		
    		//创建一个字节数组输出流,流的大小为size
    		ByteArrayOutputStream baos=new ByteArrayOutputStream(size);    
    		//设置位图的压缩格式，质量为100%，并放入字节数组输出流中    
    		bitmap1.compress(Bitmap.CompressFormat.PNG, 100, baos);
    		//将字节数组输出流转化为字节数组byte[]    
    		byte[] imagedata1=baos.toByteArray(); 
    		//将字节数组保存到数据库中    
    		ContentValues cv=new ContentValues();
    		cv.put("_id", 1);   
    		cv.put("image", imagedata1);   
    		mydb.insert("imagetable", null, cv);
    		//关闭字节数组输出流
    		baos.close();
    		 
    		} catch (Exception e) {
                 e.printStackTrace();
    		}
    }
	
	/*
	 * 从数据库中读取用户头像，从而后期能够显示
	 * */
	public Bitmap getPicFromDB(){
		
		Cursor cur=mydb.query("imagetable", new String[]{"_id","image"}, null, null, null, null, null);

		if(cur==null)
			return null;
		else{
			byte[] imagequery=null;
			if(cur.moveToNext()){
				//将Blob数据转化为字节数组
				imagequery=cur.getBlob(cur.getColumnIndex("image"));
			}
			//将字节数组转化为位图
			if(imagequery==null)
				return null;
			Bitmap imagebitmap=BitmapFactory.decodeByteArray(imagequery, 0, imagequery.length);
			return imagebitmap;
		}
	
		
	}
	
	public void deletePicFromDB(){
		mydb.delete(  
				"imagetable", "_id" + " = " + 1, null);  
	}
	
	
	public void closeDB(){
		mydb.close();
		myPicSQLiteOpenHelper = null;
	}
	
	private class PicSQLiteOpenHelper extends SQLiteOpenHelper {
		// 重写构造方法
		public PicSQLiteOpenHelper(Context context, String name,
			CursorFactory cursor, int version) {
			super(context, name, cursor, version);
		}
		
		// 创建数据库的方法
		public void onCreate(SQLiteDatabase db) {
			// 创建一个数据库，表名：imagetable，字段：_id、image。
			db.execSQL("CREATE TABLE imagetable (_id INTEGER PRIMARY KEY AUTOINCREMENT,image BLOB)");
		}
		
		// 更新数据库的方法
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		}


	}
	
	
}
