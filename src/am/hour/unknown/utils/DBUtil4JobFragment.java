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
	 * type������������Ǵ洢ͼƬ���Ǵ洢����
	 * */
	public DBUtil4JobFragment(){
		this.context=MainApplication.getContext();
		myJobSQLiteOpenHelper = new JobSQLiteOpenHelper(context, "jobdetailstable.db", null,
				1);
		// ����һ���ɶ�д�����ݿ�
		mydb = myJobSQLiteOpenHelper.getWritableDatabase();
		
	}
	

	//temp=0;˵��������ʱ�Ե�ְλ
	//temp=1;˵������ʱ�Ե�ְλ
    public void saveJobDetailInDB(JobInfo ji){

    	
		ContentValues cv=new ContentValues();	
		//��������������֣�����һ���ַ���
		cv.put("_temp", 0);
		cv.put("_jobId", Integer.parseInt(ji.getJobId()));   
		cv.put("_jobName", ji.getJobName()); 
		cv.put("_salaryType", ji.getSalaryType());
		cv.put("_salary", ji.getSalary());
		cv.put("_workPlace", ji.getWorkPlace());
		cv.put("_highLights", ji.getHighLights());
		//��������������֣�����һ���ַ���
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
	 * �����ݿ��е�ĳһ�����ݶ�ȡ�������жϴ治����
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
    	System.out.println("�����������ص����ݽ������ݿ���");
    	//�ж����ݿ�����û�������Ϣ��
    	try{
    		System.out.println("ɾ�����ݿ����Ѿ����ڵ�����");
        	deleteTempJobFromDB();
        	//���滹Ҫ�ѱ������ֻ��е��ļ�ɾ����
    	}catch(Exception e){
    		System.out.println("ɾ�����ݿ��е�temp���ݳ���");
    		e.printStackTrace();
    	}
    	
    	
		ContentValues cv=new ContentValues();	
		//��������������֣�����һ���ַ���
		cv.put("_temp", 1);
		cv.put("_jobId", Integer.parseInt(ji.getJobId()));   
		cv.put("_jobName", ji.getJobName()); 
		cv.put("_salaryType", ji.getSalaryType());
		cv.put("_salary", ji.getSalary());
		cv.put("_workPlace", ji.getWorkPlace());
		cv.put("_highLights", ji.getHighLights());
		//��������������֣�����һ���ַ���
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
	 * �����ݿ��е�ĳһ�����ݶ�ȡ�������жϴ治����
	 * */
	public JobInfo getTempJobFromDB(){
		System.out.println("�����ݿ��л�ȡ������");
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
            
            System.out.println("���������ݿ�ķ��ؽ��"+jobTemp.toString());
            if(jobTemp.getJobName()!=null){
            	System.out.println("���طǿ�ֵ");
            	return jobTemp;
            }else{
            	System.out.println("���ؿ�ֵ");
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
		// ��д���췽��
		public JobSQLiteOpenHelper(Context context, String name,
			CursorFactory cursor, int version) {
			super(context, name, cursor, version);
		}
		
		// �������ݿ�ķ���
		public void onCreate(SQLiteDatabase db) {
			// ����һ�����ݿ⣬������jobdetailstable
			db.execSQL("CREATE TABLE jobdetailstable (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
					//����ֶα�ʾ��������ǲ�����ʱ�ģ������Ѿ��ղع��ˣ���ʱ�Ŀ�������һ�ν������ʱ�õ�
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
		
		// �������ݿ�ķ���
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
			db.execSQL("DROP TABLE IF EXISTS "+"jobdetailstable");     
	        onCreate(db);  
		}
	}
	
}

