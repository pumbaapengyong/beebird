package am.hour.unknown.utils;

import am.hour.unknown.Activity.MainApplication;
import am.hour.unknown.model.JobInfo;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;


public class DBUtil4JobFragment {
	private JobSQLiteOpenHelper myJobSQLiteOpenHelper = null;
	private SQLiteDatabase mydb = null;
	private Context context;
	/*
	 *将首页的工作的详细信息保存到数据库中
	 * */
	public DBUtil4JobFragment(){
		this.context=MainApplication.getContext();
		myJobSQLiteOpenHelper = new JobSQLiteOpenHelper(context, "jobdetailstable.db", null,
				1);
		mydb = myJobSQLiteOpenHelper.getWritableDatabase();
		
	}
	

	//temp=0;不是暂时性的存储，对应于喜欢的工作的详细信息
	//temp=1;暂时性的存储，对应于首页下一个工作的详细信息
    public void saveJobDetailInDB(JobInfo ji){

    	
		ContentValues cv=new ContentValues();	
		//喜欢的工作
		cv.put("_temp", 0);
		cv.put("_jobId", Integer.parseInt(ji.getJobId()));   
		cv.put("_jobName", ji.getJobName()); 
		cv.put("_salaryType", ji.getSalaryType());
		cv.put("_salary", ji.getSalary());
		cv.put("_workPlace", ji.getWorkPlace());
		cv.put("_highLights", ji.getHighLights());
		cv.put("_companyId", Integer.parseInt(ji.getCompanyId())); 
		cv.put("_companyName", ji.getCompanyName());
		cv.put("_companyLogo", ji.getCompanyLogo());
		cv.put("_jobPic", ji.getJobPic());
		cv.put("_picIntroduce", ji.getPicIntroduce());
		cv.put("_weHave", ji.getWeHave());
		cv.put("_weHope", ji.getWeHope());
		cv.put("_jobDescripCoord", ji.getJobDescripCoord());
		cv.put("_jobLink", ji.getJobLink());
		mydb.insert("jobdetailstable", null, cv); 		
    }
	
	/*
	 *从数据中读取喜欢的工作
	 * */
	public JobInfo getOneJobDetailFromDB(int jobId1){
		
		Cursor cur=mydb.rawQuery("SELECT * FROM jobdetailstable WHERE _jobId = ?", new String[]{jobId1+""});

		if(cur==null)
			return null;
		else{
			JobInfo jobTemp = new JobInfo();
			int id;
			String jobId;
			String jobName = null;
			String salaryType = null;
			String salary = null;
			String workPlace = null;
			String highLights = null;
			String companyId = null;
			String companyName = null;
			String companyLogo = null;
			String jobPic = null;
			String picIntroduce = null;
			String weHave = null;
			String weHope = null;
			String jobDescripCoord = null;
			String jobLink = null;

			if(cur.moveToNext()){
				jobTemp = new JobInfo();
				id = cur.getInt(cur.getColumnIndex("_id"));
				jobId = cur.getInt(cur.getColumnIndex("_jobId"))+"";
				salaryType = cur.getString(cur.getColumnIndex("_salaryType"));
				jobName = cur.getString(cur.getColumnIndex("_jobName"));
				salary = cur.getString(cur.getColumnIndex("_salary"));
				workPlace = cur.getString(cur.getColumnIndex("_workPlace"));
				highLights = cur.getString(cur.getColumnIndex("_highLights"));
				companyId = cur.getInt(cur.getColumnIndex("_companyId"))+"";
				companyName = cur.getString(cur.getColumnIndex("_companyName"));
				companyLogo = cur.getString(cur.getColumnIndex("_companyLogo"));
				jobPic = cur.getString(cur.getColumnIndex("_jobPic"));
				picIntroduce = cur.getString(cur.getColumnIndex("_picIntroduce"));
				weHave = cur.getString(cur.getColumnIndex("_weHave"));
				weHope = cur.getString(cur.getColumnIndex("_weHope"));
				jobDescripCoord = cur.getString(cur.getColumnIndex("_jobDescripCoord"));
				jobLink = cur.getString(cur.getColumnIndex("_jobLink"));
				jobTemp.setJobId(jobId);
				jobTemp.setJobName(jobName);
				jobTemp.setSalaryType(salaryType);
				jobTemp.setSalary(salary);
				jobTemp.setWorkPlace(workPlace);
				jobTemp.setHighLights(highLights);
				jobTemp.setCompanyId(companyId);
				jobTemp.setCompanyName(companyName);
				jobTemp.setCompanyLogo(companyLogo);
				jobTemp.setJobPic(jobPic);
				jobTemp.setPicIntroduce(picIntroduce);
				jobTemp.setWeHave(weHave);
				jobTemp.setWeHope(weHope);
				jobTemp.setJobDescripCoord(jobDescripCoord);
				jobTemp.setJobLink(jobLink);
				
			}

			return jobTemp;
		}	
	}
	
	public void deleteJobDetailFromDB(int JobId){
		mydb.delete(  
				"jobdetailstable", "_jobId" + " = " + JobId, null);  
	}
	
	
    public void saveTempJobInDB(JobInfo ji){
    	System.out.println("�����������ص���ݽ�����ݿ���");
    	try{
    		System.out.println("ɾ����ݿ����Ѿ����ڵ����");
        	deleteTempJobFromDB();
    	}catch(Exception e){
    		System.out.println("ɾ����ݿ��е�temp��ݳ���");
    		e.printStackTrace();
    	}
    	
    	
		ContentValues cv=new ContentValues();	
		cv.put("_temp", 1);
		cv.put("_jobId", Integer.parseInt(ji.getJobId()));   
		cv.put("_jobName", ji.getJobName()); 
		cv.put("_salaryType", ji.getSalaryType());
		cv.put("_salary", ji.getSalary());
		cv.put("_workPlace", ji.getWorkPlace());
		cv.put("_highLights", ji.getHighLights());
		cv.put("_companyId", Integer.parseInt(ji.getCompanyId())); 
		cv.put("_companyName", ji.getCompanyName());
		cv.put("_companyLogo", ji.getCompanyLogo());
		cv.put("_jobPic", ji.getJobPic());
		cv.put("_picIntroduce", ji.getPicIntroduce());
		cv.put("_weHave", ji.getWeHave());
		cv.put("_weHope", ji.getWeHope());
		cv.put("_jobDescripCoord", ji.getJobDescripCoord());
		cv.put("_jobLink", ji.getJobLink());
		mydb.insert("jobdetailstable", null, cv); 		
    }
	
	/*
	 *
	 * */
	public JobInfo getTempJobFromDB(){
		Cursor cur = null;
		try{
			cur=mydb.rawQuery("SELECT * FROM jobdetailstable WHERE _temp = ?", new String[]{1+""});

		}catch(Exception e){
			e.printStackTrace();
		}
		
		if(cur==null){
			return null;
		}
			
		else{
			JobInfo jobTemp = new JobInfo();
			int id;
			String jobId= null;
			String jobName = null;
			String salaryType = null;
			String salary = null;
			String workPlace = null;
			String highLights = null;
			String companyId = null;
			String companyName = null;
			String companyLogo = null;
			String jobPic = null;
			String picIntroduce = null;
			String weHave = null;
			String weHope = null;
			String jobDescripCoord = null;
			String jobLink = null;

			if(cur.moveToNext()){
				jobTemp = new JobInfo();
				id = cur.getInt(cur.getColumnIndex("_id"));
				jobId = cur.getInt(cur.getColumnIndex("_jobId"))+"";
				salaryType = cur.getString(cur.getColumnIndex("_salaryType"));
				jobName = cur.getString(cur.getColumnIndex("_jobName"));
				salary = cur.getString(cur.getColumnIndex("_salary"));
				workPlace = cur.getString(cur.getColumnIndex("_workPlace"));
				highLights = cur.getString(cur.getColumnIndex("_highLights"));
				companyId = cur.getInt(cur.getColumnIndex("_companyId"))+"";
				companyName = cur.getString(cur.getColumnIndex("_companyName"));
				companyLogo = cur.getString(cur.getColumnIndex("_companyLogo"));
				jobPic = cur.getString(cur.getColumnIndex("_jobPic"));
				picIntroduce = cur.getString(cur.getColumnIndex("_picIntroduce"));
				weHave = cur.getString(cur.getColumnIndex("_weHave"));
				weHope = cur.getString(cur.getColumnIndex("_weHope"));
				jobDescripCoord = cur.getString(cur.getColumnIndex("_jobDescripCoord"));
				jobLink = cur.getString(cur.getColumnIndex("_jobLink"));
				jobTemp.setJobId(jobId);
				jobTemp.setJobName(jobName);
				jobTemp.setSalaryType(salaryType);
				jobTemp.setSalary(salary);
				jobTemp.setWorkPlace(workPlace);
				jobTemp.setHighLights(highLights);
				jobTemp.setCompanyId(companyId);
				jobTemp.setCompanyName(companyName);
				jobTemp.setCompanyLogo(companyLogo);
				jobTemp.setJobPic(jobPic);
				jobTemp.setPicIntroduce(picIntroduce);
				jobTemp.setWeHave(weHave);
				jobTemp.setWeHope(weHope);
				jobTemp.setJobDescripCoord(jobDescripCoord);
				jobTemp.setJobLink(jobLink);
				
			}
            if(jobTemp.getJobName()!=null){
            	return jobTemp;
            }else{
            	return null;
            }
			
		}	
	}
	
	public void deleteTempJobFromDB(){
		mydb.delete(  
				"jobdetailstable", "_temp" + " = " + 1, null);  
	}
	
	
	
	
	public void closeDB(){
		mydb.close();
		myJobSQLiteOpenHelper = null;
	}
	
	
	
	private class JobSQLiteOpenHelper extends SQLiteOpenHelper {
		public JobSQLiteOpenHelper(Context context, String name,
			CursorFactory cursor, int version) {
			super(context, name, cursor, version);
		}
		
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE jobdetailstable (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					"_temp INTEGER," +
					"_jobId INTEGER," +
					"_jobName NVARCHAR," +
					"_salaryType NVARCHAR," +
					"_salary NVARCHAR," +
					"_workPlace NVARCHAR," +
					"_highLights NVARCHAR," +
					"_companyId INTEGER," +
					"_companyName NVARCHAR," +
					"_companyLogo NVARCHAR," +
					"_jobPic NVARCHAR," +
					"_picIntroduce NVARCHAR," +
					"_weHave NVARCHAR," +
					"_weHope NVARCHAR," +
					"_jobDescripCoord NVARCHAR," +
					"_jobLink NVARCHAR)");
		}
		
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
			db.execSQL("DROP TABLE IF EXISTS "+"jobdetailstable");     
	        onCreate(db);  
		}
	}
	
}

