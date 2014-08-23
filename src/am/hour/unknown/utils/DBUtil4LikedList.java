package am.hour.unknown.utils;

import java.util.HashMap;

import am.hour.unknown.Activity.MainApplication;
import am.hour.unknown.model.JobShortInfo;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;


public class DBUtil4LikedList {
	private JobSQLiteOpenHelper myJobSQLiteOpenHelper = null;
	private SQLiteDatabase mydb = null;
	private Context context;
	/*
	 * type就是这个东西是存储图片还是存储工作
	 * */
	public DBUtil4LikedList(){
		this.context=MainApplication.getContext();
		myJobSQLiteOpenHelper = new JobSQLiteOpenHelper(context, "jobstable.db", null,
				1);
		// 创建一个可读写的数据库
		mydb = myJobSQLiteOpenHelper.getWritableDatabase();
		
	}
	

	
    public void saveJobInDB(JobShortInfo jsi){

		ContentValues cv=new ContentValues();
		//这货的属性是数字，不是一个字符串
		cv.put("_jobId", Integer.parseInt(jsi.getJobId()));   
		cv.put("_jobName", jsi.getJobName()); 
		cv.put("_likeNumber", jsi.getLikeNumber());
		cv.put("_salary", jsi.getSalary());
		cv.put("_workPlace", jsi.getWorkPlace());
		cv.put("_companyLogo", jsi.getCompanyLogo());
		mydb.insert("jobstable", null, cv);
    		
    }
	
	/*
	 * 将数据库中的信息全部读取出来
	 * */
	public HashMap<Integer,JobShortInfo> getJobsFromDB(){
		
		Cursor cur=mydb.query("jobstable", new String[]{"_id","_jobId","_jobName","_likeNumber",
				"_salary","_workPlace","_companyLogo"}, null, null, null, null, null);

		HashMap<Integer,JobShortInfo> tempList = null;
		if(cur==null)
			return null;
		else{
			tempList = new HashMap<Integer,JobShortInfo>();
			int id;
			String jobId;
			String jobName = null;
			String likeNumber = null;
			String salary = null;
			String workPlace = null;
			String companyLogo = null;
			while(cur.moveToNext()){
				System.out.println();
				JobShortInfo jobTemp = new JobShortInfo();
				id = cur.getInt(cur.getColumnIndex("_id"));
				jobId = cur.getInt(cur.getColumnIndex("_jobId"))+"";
				jobName = cur.getString(cur.getColumnIndex("_jobName"));
				likeNumber = cur.getString(cur.getColumnIndex("_likeNumber"));
				salary = cur.getString(cur.getColumnIndex("_salary"));
				workPlace = cur.getString(cur.getColumnIndex("_workPlace"));
				companyLogo = cur.getString(cur.getColumnIndex("_companyLogo"));
				jobTemp.setJobId(jobId);
				jobTemp.setJobName(jobName);
				jobTemp.setLikeNumber(likeNumber);
				jobTemp.setSalary(salary);
				jobTemp.setWorkPlace(workPlace);
				jobTemp.setCompanyLogo(companyLogo);
				tempList.put(id, jobTemp);
			}

			return tempList;
		}	
	}
	
	/*
	 * 将数据库中的某一条数据读取出来，判断存不存在
	 * */
	public JobShortInfo getOneJobFromDB(int jobId1){
		
		Cursor cur=mydb.rawQuery("SELECT * FROM jobstable WHERE _jobId = ?", new String[]{jobId1+""});

		if(cur==null)
			return null;
		else{
			JobShortInfo jobTemp = new JobShortInfo();
			int id;
			String jobId;
			String jobName = null;
			String likeNumber = null;
			String salary = null;
			String workPlace = null;
			String companyLogo = null;
			if(cur.moveToNext()){
				jobTemp = new JobShortInfo();
				id = cur.getInt(cur.getColumnIndex("_id"));
				jobId = cur.getInt(cur.getColumnIndex("_jobId"))+"";
				jobName = cur.getString(cur.getColumnIndex("_jobName"));
				likeNumber = cur.getString(cur.getColumnIndex("_likeNumber"));
				salary = cur.getString(cur.getColumnIndex("_salary"));
				workPlace = cur.getString(cur.getColumnIndex("_workPlace"));
				companyLogo = cur.getString(cur.getColumnIndex("_companyLogo"));
				jobTemp.setJobId(jobId);
				jobTemp.setJobName(jobName);
				jobTemp.setLikeNumber(likeNumber);
				jobTemp.setSalary(salary);
				jobTemp.setWorkPlace(workPlace);
				jobTemp.setCompanyLogo(companyLogo);
			}

			return jobTemp;
		}	
	}
	
	public void deleteJobFromDB(int JobId){
		mydb.delete(  
				"jobstable", "_jobId" + " = " + JobId, null);  
	}
	
	
	public void closeDB(){
		mydb.close();
		myJobSQLiteOpenHelper = null;
	}
	
	
	
	private class JobSQLiteOpenHelper extends SQLiteOpenHelper {
		// 重写构造方法
		public JobSQLiteOpenHelper(Context context, String name,
			CursorFactory cursor, int version) {
			super(context, name, cursor, version);
		}
		
		// 创建数据库的方法
		public void onCreate(SQLiteDatabase db) {
			// 创建一个数据库，表名：jobstable，字段："_id","_jobName","_likeNumber",
			//"_salary","_workPlace","_companyLogo"
			db.execSQL("CREATE TABLE jobstable (_id INTEGER PRIMARY KEY AUTOINCREMENT,_jobId INTEGER,_jobName NVARCHAR(20)," +
					"_likeNumber NVARCHAR(10),_salary NVARCHAR(20),_workPlace NVARCHAR(10)," +
					"_companyLogo NVARCHAR(30))");
		}
		
		// 更新数据库的方法
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
			db.execSQL("DROP TABLE IF EXISTS "+"jobstable");     
	        onCreate(db);  
		}
	}
	
}
