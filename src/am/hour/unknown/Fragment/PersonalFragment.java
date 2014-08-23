package am.hour.unknown.Fragment;

import am.hour.unknown.utils.Constant;
import am.hour.unknown.R;
import am.hour.unknown.Activity.MainActivity;
import am.hour.unknown.Activity.MainApplication;
import am.hour.unknown.utils.DBUtil4Image;
import am.hour.unknown.utils.PicLoadUtil;
import am.hour.unknown.utils.SharedPreferenceUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PersonalFragment extends Fragment implements OnClickListener{
	
	public View rootView = null;
	public LinearLayout ll_personal_fragment = null;
	public ImageView iv_personal_pic;
	public ImageButton ib_personal_choosepic;
	public TextView tv_username;
	public Button bt_personal_changepassword;
	public Button bt_personal_loginout;
	
	
	
    public PersonalFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
        rootView = inflater.inflate(R.layout.fragment_personal, container, false);

        int i = getArguments().getInt(Constant.ARG_FRAGMENT_INFO);      
        getandsetViews();
        return rootView;
    }
    
    public void getandsetViews(){
    	LayoutParams lp = null;
    	ll_personal_fragment = (LinearLayout)rootView.findViewById(R.id.ll_personal_fragment);         
        lp = ll_personal_fragment.getLayoutParams();    	 
   	    ((MarginLayoutParams) lp).setMargins(0,MainApplication.ACTIONBAR_HEIGHT, 0, 0);
   	    ll_personal_fragment.setLayoutParams(lp);	 
    	int titleId = Resources.getSystem().getIdentifier(
					"action_bar_title", "id", "android");
        TextView actionbarTitle = (TextView) getActivity().findViewById(titleId);
        actionbarTitle.setTextColor(Color.BLACK);
        
        iv_personal_pic = (ImageView)rootView.findViewById(R.id.iv_personal_pic);
    	ib_personal_choosepic = (ImageButton)rootView.findViewById(R.id.ib_personal_newpic);
    	tv_username = (TextView)rootView.findViewById(R.id.tv_personal_username);
    	bt_personal_changepassword = (Button)rootView.findViewById(R.id.bt_changepassword);
    	bt_personal_loginout = (Button)rootView.findViewById(R.id.bt_loginout);
    	
    	iv_personal_pic.setBackgroundDrawable(new BitmapDrawable(MainActivity.DBUtil.getPicFromDB()));
    	tv_username.setText(MainActivity.spu.getStringFromSP("USERNAME", ""));
    	
    	ib_personal_choosepic.setOnClickListener(this); 
    	bt_personal_changepassword.setOnClickListener(this); 
    	bt_personal_loginout.setOnClickListener(this); 
			
    	
    }
    
	public void toast(String toast){
		Toast.makeText(getActivity(), toast, 100).show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.ib_personal_newpic:
			getImageFromPhone();
			break;
		case R.id.bt_changepassword:
			MyDialogFragment newPassworddialog = new MyDialogFragment();
			Bundle args = new Bundle();
			args.putInt(Constant.ARG_FRAGMENT_INFO, Constant.NEW_PASSWORD_DIALOG);
			newPassworddialog.setArguments(args);
			newPassworddialog.show(getFragmentManager(), "UpdateDialog");
			break;
		case R.id.bt_loginout:
			MainActivity.spu.deleteDataInSP("USERNAME");
			MainActivity.spu.deleteDataInSP("USERPASSWORD");
			MainActivity.spu.deleteDataInSP("LOGINORNOT");
			MainActivity.DBUtil.deletePicFromDB();
			MainActivity.DBUtil.savePicInDB(PicLoadUtil.LoadBitmap(getActivity(), R.drawable.fakelogo));
			((MainActivity)getActivity()).updatePersonalInfoLeftDrawer();
			
			Intent intent = new Intent();
			intent.setClass(getActivity(),MainActivity.class);
			startActivity(intent);
			getActivity().finish();
			break;

		}
	}
    
	/*
	 *从手机中获取照片
	 * */
	
	public void getImageFromPhone(){
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);  
        intent.addCategory(Intent.CATEGORY_OPENABLE);  
        intent.setType("image/*");  
        intent.putExtra("crop", "true");  
        intent.putExtra("aspectX",1);  
        intent.putExtra("aspectY",1);  
        intent.putExtra("outputX", 160);  
        intent.putExtra("outputY", 160);  
        intent.putExtra("return-data",true);  
        startActivityForResult(intent, 11); 
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
     
        if (requestCode == 11 && resultCode == Activity.RESULT_OK) {
	        System.out.println(resultCode);  
	        Bitmap cameraBitmap = (Bitmap) data.getExtras().get("data");  
	        cameraBitmap = PicLoadUtil.toRoundBitmap(cameraBitmap);
	        iv_personal_pic.setBackgroundDrawable(new BitmapDrawable(cameraBitmap));
	        MainActivity.DBUtil.deletePicFromDB();
	        MainActivity.DBUtil.savePicInDB(cameraBitmap);
	        ((MainActivity)getActivity()).updatePersonalInfoLeftDrawer();
	        super.onActivityResult(requestCode, resultCode, data); 
        }
    }


    
}