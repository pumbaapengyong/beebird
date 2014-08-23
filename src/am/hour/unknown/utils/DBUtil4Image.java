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
	 * type������������Ǵ洢ͼƬ���Ǵ洢����
	 * */
	public DBUtil4Image(){
		this.context=MainApplication.getContext();
		myPicSQLiteOpenHelper = new PicSQLiteOpenHelper(context, "saveimage.db", null,
				1);
		// ����һ���ɶ�д�����ݿ�
		mydb = myPicSQLiteOpenHelper.getWritableDatabase();
		
	}
	

	
    public void savePicInDB(Bitmap bitmap1){
    	try {
    		
            //��ͼƬת��Ϊλͼ
    		//Bitmap bitmap1=BitmapFactory.decodeResource(getResources(), R.drawable.erweima);	
    		int size=bitmap1.getWidth()*bitmap1.getHeight()*4;		
    		//����һ���ֽ����������,���Ĵ�СΪsize
    		ByteArrayOutputStream baos=new ByteArrayOutputStream(size);    
    		//����λͼ��ѹ����ʽ������Ϊ100%���������ֽ������������    
    		bitmap1.compress(Bitmap.CompressFormat.PNG, 100, baos);
    		//���ֽ����������ת��Ϊ�ֽ�����byte[]    
    		byte[] imagedata1=baos.toByteArray(); 
    		//���ֽ����鱣�浽���ݿ���    
    		ContentValues cv=new ContentValues();
    		cv.put("_id", 1);   
    		cv.put("image", imagedata1);   
    		mydb.insert("imagetable", null, cv);
    		//�ر��ֽ����������
    		baos.close();
    		 
    		} catch (Exception e) {
                 e.printStackTrace();
    		}
    }
	
	/*
	 * �����ݿ��ж�ȡ�û�ͷ�񣬴Ӷ������ܹ���ʾ
	 * */
	public Bitmap getPicFromDB(){
		
		Cursor cur=mydb.query("imagetable", new String[]{"_id","image"}, null, null, null, null, null);

		if(cur==null)
			return null;
		else{
			byte[] imagequery=null;
			if(cur.moveToNext()){
				//��Blob����ת��Ϊ�ֽ�����
				imagequery=cur.getBlob(cur.getColumnIndex("image"));
			}
			//���ֽ�����ת��Ϊλͼ
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
		// ��д���췽��
		public PicSQLiteOpenHelper(Context context, String name,
			CursorFactory cursor, int version) {
			super(context, name, cursor, version);
		}
		
		// �������ݿ�ķ���
		public void onCreate(SQLiteDatabase db) {
			// ����һ�����ݿ⣬������imagetable���ֶΣ�_id��image��
			db.execSQL("CREATE TABLE imagetable (_id INTEGER PRIMARY KEY AUTOINCREMENT,image BLOB)");
		}
		
		// �������ݿ�ķ���
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		}


	}
	
	
}
