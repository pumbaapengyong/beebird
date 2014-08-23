package am.hour.beebird.utils;

import java.io.ByteArrayOutputStream;

import am.hour.beebird.Activity.MainApplication;
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
	 *将头像存入数据中
	 * */
	public DBUtil4Image(){
		this.context=MainApplication.getContext();
		myPicSQLiteOpenHelper = new PicSQLiteOpenHelper(context, "saveimage.db", null,
				1);
		mydb = myPicSQLiteOpenHelper.getWritableDatabase();
		
	}
	

	
    public void savePicInDB(Bitmap bitmap1){
    	try {
    			
    		int size=bitmap1.getWidth()*bitmap1.getHeight()*4;		
    		ByteArrayOutputStream baos=new ByteArrayOutputStream(size);    
    		bitmap1.compress(Bitmap.CompressFormat.PNG, 100, baos);
    		byte[] imagedata1=baos.toByteArray(); 
    		ContentValues cv=new ContentValues();
    		cv.put("_id", 1);   
    		cv.put("image", imagedata1);   
    		mydb.insert("imagetable", null, cv);
    		baos.close();
    		 
    		} catch (Exception e) {
                 e.printStackTrace();
    		}
    }
	
	/*
	 *从数据库中读取头像
	 * */
	public Bitmap getPicFromDB(){
		
		Cursor cur=mydb.query("imagetable", new String[]{"_id","image"}, null, null, null, null, null);

		if(cur==null)
			return null;
		else{
			byte[] imagequery=null;
			if(cur.moveToNext()){
				imagequery=cur.getBlob(cur.getColumnIndex("image"));
			}
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
		public PicSQLiteOpenHelper(Context context, String name,
			CursorFactory cursor, int version) {
			super(context, name, cursor, version);
		}
		
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE imagetable (_id INTEGER PRIMARY KEY AUTOINCREMENT,image BLOB)");
		}
		
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		}


	}
	
	
}
