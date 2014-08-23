package am.hour.beebird.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

import am.hour.beebird.model.JobInfo;
import am.hour.beebird.model.JobShortInfo;
import android.util.Log;

public class JsonUtil{
	
	public static JobInfo json2Class4JobFragment(String jsonData){
		Gson gson = new Gson();
        JobInfo user = gson.fromJson(jsonData, JobInfo.class);
        return user;
	}

	public static JobShortInfo json2Class4LikedFragment(String jsonData){
		Gson gson = new Gson();
		JobShortInfo user = gson.fromJson(jsonData, JobShortInfo.class);
        return user;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*************************************¥�¶��Ǵ����ϸ�����***********************************/
	
	/**
	 * ��ȡ"������ʽ"��JSON��ݣ�
	 * �����ʽ��[{"id":1,"name":"С��"},{"id":2,"name":"Сè"}]
	 * @param path	��ҳ·��
	 * @return	����List
	 * @throws Exception
	 */
	public static List<Map<String, String>> getJSONArray(String path) throws Exception {
		String json = null;
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map = null;
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();// ����HttpURLConnection����,���ǿ��Դ������л�ȡ��ҳ���.
		conn.setConnectTimeout(5 * 1000); 	// ��λ�Ǻ��룬���ó�ʱʱ��Ϊ5��
		conn.setRequestMethod("GET");		// HttpURLConnection��ͨ��HTTPЭ������path·���ģ�������Ҫ��������ʽ,���Բ����ã���ΪĬ��ΪGET
		if (conn.getResponseCode() == 200) {// �ж��������Ƿ���200�룬����ʧ��
			InputStream is = conn.getInputStream(); // ��ȡ������
			byte[] data = readStream(is); 	// ��������ת�����ַ�����
			json = new String(data); 		// ���ַ�����ת�����ַ�
			
			//�����ʽ��[{"id":1,"name":"С��","age":22},{"id":2,"name":"Сè","age":23}]
			JSONArray jsonArray = new JSONArray(json); //���ֱ��Ϊһ��������ʽ�����Կ���ֱ�� ��android�ṩ�Ŀ��JSONArray��ȡJSON��ݣ�ת����Array

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject item = jsonArray.getJSONObject(i); //ÿ����¼���ɼ���Object�������
				int id = item.getInt("id"); 	// ��ȡ�����Ӧ��ֵ
				String name = item.getString("name");

				map = new HashMap<String, String>(); // ��ŵ�MAP����
				map.put("id", id + "");
				map.put("name", name);
				list.add(map);
			}
		}

		// ***********�������******************
		for (Map<String, String> list2 : list) {
			String id = list2.get("id");
			String name = list2.get("name");
			Log.i("abc", "id:" + id + " | name:" + name);
		}

		return list;
	}

	/**
	 * ��ȡ"������ʽ"��JSON��ݣ�
	 * �����ʽ��{"total":2,"success":true,"arrayData":[{"id":1,"name":"С��"},{"id":2,"name":"Сè"}]}
	 * @param path	��ҳ·��
	 * @return	����List
	 * @throws Exception
	 */
	public static List<Map<String, String>> getJSONObject(String path) throws Exception {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map = null;
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();// ����HttpURLConnection����,���ǿ��Դ������л�ȡ��ҳ���.
		conn.setConnectTimeout(5 * 1000); 	// ��λ�Ǻ��룬���ó�ʱʱ��Ϊ5��
		conn.setRequestMethod("GET");		// HttpURLConnection��ͨ��HTTPЭ������path·���ģ�������Ҫ��������ʽ,���Բ����ã���ΪĬ��ΪGET
		if (conn.getResponseCode() == 200) {// �ж��������Ƿ���200�룬����ʧ��
			InputStream is = conn.getInputStream(); // ��ȡ������
			byte[] data = readStream(is); 	// ��������ת�����ַ�����
			String json = new String(data); // ���ַ�����ת�����ַ�
			
			
			//�����ʽ��{"total":2,"success":true,"arrayData":[{"id":1,"name":"С��"},{"id":2,"name":"Сè"}]}
			JSONObject jsonObject=new JSONObject(json);		//���ص������ʽ��һ��Object���ͣ����Կ���ֱ��ת����һ��Object
			int total=jsonObject.getInt("total");
			Boolean success=jsonObject.getBoolean("success");
			Log.i("abc", "total:" + total + " | success:" + success);	//�������
			
			JSONArray jsonArray = jsonObject.getJSONArray("arrayData");//������һ��������ݣ�������getJSONArray��ȡ����
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject item = jsonArray.getJSONObject(i); // �õ�ÿ������
				int id = item.getInt("id"); 	// ��ȡ�����Ӧ��ֵ
				String name = item.getString("name");

				map = new HashMap<String, String>(); // ��ŵ�MAP����
				map.put("id", id + "");
				map.put("name", name);
				list.add(map);
			}
		}

		// ***********�������******************
		
		for (Map<String, String> list2 : list) {
			String id = list2.get("id");
			String name = list2.get("name");
			Log.i("abc", "id:" + id + " | name:" + name);
		}

		return list;
	}
	
	
	/**
	 * ��ȡ���͸��ӵ�JSON���
	 *�����ʽ��
		{"name":"С��",
		 "age":23,
		 "content":{"questionsTotal":2,
					"questions": [ { "question": "what's your name?", "answer": "С��"},{"question": "what's your age", "answer": "23"}]
			       }
		}
	 * @param path	��ҳ·��
	 * @return	����List
	 * @throws Exception
	 */
	public static List<Map<String, String>> getJSON(String path) throws Exception {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Map<String, String> map = null;
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();// ����HttpURLConnection����,���ǿ��Դ������л�ȡ��ҳ���.
		conn.setConnectTimeout(5 * 1000); 	// ��λ�Ǻ��룬���ó�ʱʱ��Ϊ5��
		conn.setRequestMethod("GET");		// HttpURLConnection��ͨ��HTTPЭ������path·���ģ�������Ҫ��������ʽ,���Բ����ã���ΪĬ��ΪGET
		if (conn.getResponseCode() == 200) {// �ж��������Ƿ���200�룬����ʧ��
			InputStream is = conn.getInputStream(); // ��ȡ������
			byte[] data = readStream(is); 	// ��������ת�����ַ�����
			String json = new String(data); // ���ַ�����ת�����ַ�
			
			
			/*�����ʽ��
				{"name":"С��",
				 "age":23,
				 "content":{"questionsTotal":2,
							"questions": [ { "question": "what's your name?", "answer": "С��"},{"question": "what's your age", "answer": "23"}]
					       }
				}
			*/	
			JSONObject jsonObject=new JSONObject(json);		//���ص������ʽ��һ��Object���ͣ����Կ���ֱ��ת����һ��Object
			String name=jsonObject.getString("name");		
			int age=jsonObject.getInt("age");
			Log.i("abc", "name:" + name + " | age:" + age);	//�������
			
			JSONObject contentObject=jsonObject.getJSONObject("content");		//��ȡ�����еĶ���
			String questionsTotal=contentObject.getString("questionsTotal");	//��ȡ�����е�һ��ֵ
			Log.i("abc", "questionsTotal:" + questionsTotal);	//�������
			
			JSONArray contentArray=contentObject.getJSONArray("questions");		//��ȡ�����е�����
			for (int i = 0; i < contentArray.length(); i++) {
				JSONObject item = contentArray.getJSONObject(i); // �õ�ÿ������
				String question = item.getString("question"); 	// ��ȡ�����Ӧ��ֵ
				String answer = item.getString("answer");

				map = new HashMap<String, String>(); // ��ŵ�MAP����
				map.put("question", question);
				map.put("answer", answer);
				list.add(map);
			}
		}

		// ***********�������******************
		
		for (Map<String, String> list2 : list) {
			String question = list2.get("question");
			String answer = list2.get("answer");
			Log.i("abc", "question:" + question + " | answer:" + answer);
		}

		return list;
	}
	
	
	
	
	/**
	 * ��������ת�����ַ�����
	 * @param inputStream	������
	 * @return	�ַ�����
	 * @throws Exception
	 * �������������ǽ�������ת��ΪByteArray���������json�����������
	 */
	public static byte[] readStream(InputStream inputStream) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inputStream.read(buffer)) != -1) {
			baos.write(buffer, 0, len);
		}
		baos.close();
		inputStream.close();

		return baos.toByteArray();
	}
	
	

}
