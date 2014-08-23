package am.hour.unknown.Fragment;

import com.gitonway.niftydialogeffects.widget.niftydialogeffects.Effectstype;
import com.gitonway.niftydialogeffects.widget.niftydialogeffects.NiftyDialogBuilder;

import am.hour.unknown.Activity.MainApplication;
import am.hour.unknown.utils.Constant;
import am.hour.unknown.R;
import am.hour.unknown.utils.PicLoadUtil;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;



public class SettingFragment extends Fragment implements OnClickListener{
	public View rootView = null;
	public LinearLayout ll_setting_share = null;
	public LinearLayout ll_setting_feedback = null;
	public LinearLayout ll_setting_update = null;
	public LinearLayout ll_setting_about = null;
	public LinearLayout ll_setting_like = null;
	
	public ImageView iv_setting_share = null;
	public ImageView iv_setting_feedback = null;
	public ImageView iv_setting_update = null;
	public ImageView iv_setting_about = null;
	public ImageView iv_setting_like = null;
	
	LayoutParams lp = null;
	
    public SettingFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_setting, container, false);
        int i = getArguments().getInt(Constant.ARG_FRAGMENT_INFO);
        
        getandsetViews();
        setActionbarTitleColor(Color.BLACK);
        setLayoutParams();
        setImageViewBackground();
        
        return rootView;
    }
    
    public void getandsetViews(){
    	ll_setting_share = (LinearLayout)rootView.findViewById(R.id.ll_setting_share);
    	ll_setting_feedback = (LinearLayout)rootView.findViewById(R.id.ll_setting_feedback);
    	ll_setting_update = (LinearLayout)rootView.findViewById(R.id.ll_setting_update);
    	ll_setting_about = (LinearLayout)rootView.findViewById(R.id.ll_setting_about);
    	ll_setting_like = (LinearLayout)rootView.findViewById(R.id.ll_setting_like);
    	
    	iv_setting_share = (ImageView)rootView.findViewById(R.id.iv_setting_share);
    	iv_setting_feedback = (ImageView)rootView.findViewById(R.id.iv_setting_feedback);
    	iv_setting_update = (ImageView)rootView.findViewById(R.id.iv_setting_update);
    	iv_setting_about = (ImageView)rootView.findViewById(R.id.iv_setting_about);
    	iv_setting_like = (ImageView)rootView.findViewById(R.id.iv_setting_like);
    	
    	ll_setting_feedback.setOnClickListener(this);
    	ll_setting_update.setOnClickListener(this);
    	ll_setting_about.setOnClickListener(this);
    	ll_setting_share.setOnClickListener(this);
    }
    
    public void setActionbarTitleColor(int color){
    	int titleId = Resources.getSystem().getIdentifier(
				"action_bar_title", "id", "android");
        TextView actionbarTitle = (TextView) getActivity().findViewById(titleId);
        actionbarTitle.setTextColor(color);
    }
    
    public void setLayoutParams(){
    	lp = ll_setting_share.getLayoutParams();    	 
   	    ((MarginLayoutParams) lp).setMargins(0,MainApplication.ACTIONBAR_HEIGHT, 0, 0);
   	    ll_setting_share.setLayoutParams(lp);
    }
    public void setImageViewBackground(){
    	iv_setting_share.setBackgroundDrawable
		(PicLoadUtil.LoadBitmapDrawable(getActivity(),R.drawable.share));
    	iv_setting_feedback.setBackgroundDrawable
		(PicLoadUtil.LoadBitmapDrawable(getActivity(),R.drawable.feedback));
    	iv_setting_update.setBackgroundDrawable
		(PicLoadUtil.LoadBitmapDrawable(getActivity(),R.drawable.update));
    	iv_setting_about.setBackgroundDrawable
		(PicLoadUtil.LoadBitmapDrawable(getActivity(),R.drawable.about));
    	iv_setting_like.setBackgroundDrawable
		(PicLoadUtil.LoadBitmapDrawable(getActivity(),R.drawable.liked));
    }
    



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Bundle args = new Bundle();
		switch (v.getId()){
		case R.id.ll_setting_feedback:
			MyDialogFragment feedbackdialog = new MyDialogFragment();
			
	        args.putInt(Constant.ARG_FRAGMENT_INFO, Constant.FEEDBACK_DIALOG);
	        feedbackdialog.setArguments(args);
			feedbackdialog.show(getFragmentManager(), "FeedBackDialog");
			break;
		case R.id.ll_setting_about:
			MyDialogFragment aboutdialog = new MyDialogFragment();
			
	        args.putInt(Constant.ARG_FRAGMENT_INFO, Constant.ABOUT_DIALOG);
	        aboutdialog.setArguments(args);
			aboutdialog.show(getFragmentManager(), "AboutDialog");
			break;
		case R.id.ll_setting_update:
			
			MyDialogFragment updatedialog = new MyDialogFragment();
			args.putInt(Constant.ARG_FRAGMENT_INFO, Constant.UPDATE_DIALOG);
			updatedialog.setArguments(args);
			updatedialog.show(getFragmentManager(), "UpdateDialog");
			break;
		case R.id.ll_setting_share:
			//使用NiftyDialog生成特效的对话框
			final NiftyDialogBuilder dialogBuilder=NiftyDialogBuilder.getInstance(getActivity());
			dialogBuilder
            .withTitle("Modal Dialog")                                  //.withTitle(null)  no title
            .withTitleColor("#FFFFFF")                                  //def
            .withDividerColor("#11000000")                              //def
        //    .withMessage("This is a modal Dialog.")                     //.withMessage(null)  no Msg
        //    .withMessageColor("#FFFFFF")                                //def
            .withIcon(getResources().getDrawable(R.drawable.icon))
            .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
            .withDuration(700)                                          //def
            .withEffect(Effectstype.Shake)                                         //def Effectstype.Slidetop
            .withButton1Text("OK")                                      //def gone
            .withButton2Text("Cancel")                                  //def gone
            .setCustomView(R.layout.custom_view,MainApplication.getContext())
            .setButton1Click(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainApplication.getContext(), "i'm btn1", Toast.LENGTH_SHORT).show();
                    dialogBuilder.dismiss();
                }
            })
            .setButton2Click(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainApplication.getContext(), "i'm btn2", Toast.LENGTH_SHORT).show();
                    dialogBuilder.dismiss();
                }
            })
            .show();
			break;

		}
		
	}

}
