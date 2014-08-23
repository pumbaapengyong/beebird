package am.hour.beebird.utils;

import am.hour.beebird.Activity.MainApplication;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUtil {
	private Context context;
	private SharedPreferences sharedata;
	private SharedPreferences.Editor sharedata_editor;
	
	public SharedPreferenceUtil(){
		this.context = MainApplication.getContext();
		sharedata = context.getSharedPreferences("unknown", Activity.MODE_PRIVATE);
		sharedata_editor = sharedata.edit();
	}
	
	public void deleteDataInSP(String key){
		sharedata_editor.remove(key);
		sharedata_editor.commit();
	}
	
	public void saveIntInSP(String key, int value){
		sharedata_editor.putInt(key, value);
		sharedata_editor.commit();
	}
	
	public void saveStringInSP(String key, String value){
		sharedata_editor.putString(key, value);
		sharedata_editor.commit();
	}
	
	public void saveFloatInSP(String key, Float value){
		sharedata_editor.putFloat(key, value);
		sharedata_editor.commit();
	}
	
	public void saveBooleanInSP(String key, boolean value){
		sharedata_editor.putBoolean(key, value);
		sharedata_editor.commit();
	}
	
	
	public int getIntFromSP(String key, int defValue){
		return sharedata.getInt(key, defValue);
	}
	public String getStringFromSP(String key, String defValue){
		return sharedata.getString(key, defValue);
	}
	public Float getFloatFromSP(String key, Float defValue){
		return sharedata.getFloat(key, defValue);
	}
	public boolean getBooleanFromSP(String key, boolean defValue){
		return sharedata.getBoolean(key, defValue);
	}
	
	

}
