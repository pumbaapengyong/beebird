/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package am.hour.beebird.Activity;

import java.util.HashMap;


import am.hour.beebird.Fragment.JobFragment;
import am.hour.beebird.Fragment.LikedFragment;
import am.hour.beebird.Fragment.LoginFragment;
import am.hour.beebird.Fragment.PersonalFragment;
import am.hour.beebird.Fragment.SettingFragment;
import am.hour.beebird.niftydialogeffects.Effectstype;
import am.hour.beebird.niftydialogeffects.NiftyDialogBuilder;
import am.hour.beebird.utils.Constant;
import am.hour.beebird.utils.DBUtil4Image;
import am.hour.beebird.utils.PicLoadUtil;
import am.hour.beebird.utils.SharedPreferenceUtil;
import am.hour.unknown.R;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
	
	static int init_people_pic=0;
	
	HashMap<Integer,Fragment> fragments = new HashMap<Integer,Fragment>();
	static Fragment fragment = null;

	static int recent_fragment_number=-1;
	
	private LinearLayout ll_person;
	private LinearLayout ll_random;
	private LinearLayout ll_liked;
	private LinearLayout ll_setting;
	
	private ImageView iv_ll_person_pic;
	private TextView tv_ll_person_username;
	
    private DrawerLayout mDrawerLayout;
    private LinearLayout mDrawerLeftLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    
    private ActionBar actionbar;
    public static SharedPreferenceUtil spu= null;
    public static DBUtil4Image DBUtil = null;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_main); 
        //对数据库和sharedpreference操作的类
        spu = new SharedPreferenceUtil();
        DBUtil = new DBUtil4Image();
        actionbar = getActionBar();
        actionbar.setBackgroundDrawable(PicLoadUtil.getActionBarDrawable());
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLeftLayout = (LinearLayout)findViewById(R.id.ll_left_drawer);
        ll_person = (LinearLayout)findViewById(R.id.ll_person);
        ll_random = (LinearLayout)findViewById(R.id.ll_random);
        ll_liked = (LinearLayout)findViewById(R.id.ll_liked);
        ll_setting = (LinearLayout)findViewById(R.id.ll_setting);

        iv_ll_person_pic = (ImageView)findViewById(R.id.people_logo);
        tv_ll_person_username = (TextView)findViewById(R.id.people_name);
        //将头像和用户名的信息读取出来放到左抽屉中
        updatePersonalInfoLeftDrawer();
        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        LeftLayoutClickListener llcl = new LeftLayoutClickListener();
        
        mDrawerLeftLayout.setOnClickListener(llcl);
        ll_person.setOnClickListener(llcl);
        ll_random.setOnClickListener(llcl);
        ll_liked.setOnClickListener(llcl);
        ll_setting.setOnClickListener(llcl);

        // enable ActionBar app icon to behave as action to toggle nav drawer
        actionbar.setDisplayHomeAsUpEnabled(true);
//        getActionBar().setHomeButtonEnabled(true);
        

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
            	actionbar.show();
            	actionbar.setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                
            }

            public void onDrawerOpened(View drawerView) {
            	if(!(recent_fragment_number==-1)&&!fragment.equals(null))
            	    fragments.put(recent_fragment_number, fragment);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                
            }
         // Called when a drawer's position changes.
            public void onDrawerSlide(View drawerView, float slideOffset){
            	if(slideOffset==0){
            		actionbar.show();
            	}else{
            		actionbar.hide();
            	}
            	
            }
           
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
        	
        	selectItem(1,Constant.NORMAL_ENTRY);
        	recent_fragment_number=1;
        	setTitle(R.string.list_left_one);
        	
            
        }

    }
	
	public void updatePersonalInfoLeftDrawer(){
		Bitmap people_logo_DB = DBUtil.getPicFromDB();
		if(people_logo_DB!=null)
		    iv_ll_person_pic.setBackgroundDrawable(new BitmapDrawable(people_logo_DB));
		else{
			iv_ll_person_pic.setBackgroundDrawable(PicLoadUtil.LoadBitmapDrawable(this, R.drawable.fakelogo));
			DBUtil.savePicInDB(PicLoadUtil.LoadBitmap(this, R.drawable.fakelogo));
		} 
			
        tv_ll_person_username.setText(spu.getStringFromSP("USERNAME", "�����¼"));
	}
	
	
	public void toast(String toast){
		Toast.makeText(this, toast, 500).show();
	}


	public void onDestroy(){
		super.onDestroy();
		//关闭数据库
		DBUtil.closeDB();
		
	}
	
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		 Rect frame = new Rect();
		 getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		 MainApplication.setActionbarHeight(getActionBar().getHeight()); 	
		 spu.saveIntInSP("ACTIONBAR_HEIGHT", getActionBar().getHeight());
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
//        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerLeftLayout);
//        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        menu.findItem(R.id.action_websearch).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // The action bar home/up action should open or close the drawer.
         // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {
        case R.id.action_websearch:
//            // create intent to perform web search for this planet
//            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
//            intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
//            // catch event that there's no activity to handle intent
//            if (intent.resolveActivity(getPackageManager()) != null) {
//                startActivity(intent);
//            } else {
//                Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
//            }
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    /* The click listner for ListView in the navigation drawer */

    
    private class LeftLayoutClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.ll_person:			
				selectItem(0,Constant.NORMAL_ENTRY);
				break;
			case R.id.ll_random:
				selectItem(1,Constant.NORMAL_ENTRY);
				break;
			case R.id.ll_liked:
				selectItem(2,Constant.NORMAL_ENTRY);
				break;
			case R.id.ll_setting:
				selectItem(3,Constant.NORMAL_ENTRY);
				break;  
			}
	        mDrawerLayout.closeDrawer(mDrawerLeftLayout);	
		}
    }
    /*
     * position=0,个人页面
     * position=1,工作的随机推荐页面
     * position=2,我的喜欢的列表页面
     * position=3,设置页面
     */

    public void selectItem(int position,int info) {
        // update the main content by replacing fragments
    	//如果点击的和之前的页面相同，不做处理
    	if(position==recent_fragment_number){
    		mDrawerLayout.closeDrawer(mDrawerLeftLayout);
    		return;
    	}
    	if(position==0){
    		setTitle(R.string.list_left_zero);
    		if(fragments.get(position)==null){
    			SharedPreferences sharedpreference  = getSharedPreferences("unknown", MODE_PRIVATE);
    			int loginornot = sharedpreference.getInt("LOGINORNOT", 0);
    			if(loginornot==0)
    			    fragment = new LoginFragment();
    			else if(loginornot==1)
    				fragment = new PersonalFragment();
    		}else{
    			
    			fragment = fragments.get(position);
    		}
    		recent_fragment_number=0;
    	}
    	if(position==1){
    		

    		
    		ll_random.setBackgroundResource(R.color.ll_leftdrawer_checked);
			ll_liked.setBackgroundResource(R.color.ll_leftdrawer_nonchecked);
			ll_setting.setBackgroundResource(R.color.ll_leftdrawer_nonchecked);
			setTitle(R.string.list_left_one);
    		if(fragments.get(position)==null){	
    			
    			fragment = new JobFragment();
    		}else{
    			
    			fragment = fragments.get(position);
    			
    		}	
    		recent_fragment_number=1;
    	} 
    	if(position==2){
    		ll_random.setBackgroundResource(R.color.ll_leftdrawer_nonchecked);
			ll_liked.setBackgroundResource(R.color.ll_leftdrawer_checked);
			ll_setting.setBackgroundResource(R.color.ll_leftdrawer_nonchecked);
            setTitle(R.string.list_left_two);
    		if(fragments.get(position)==null){	
    			
    			fragment = new LikedFragment();
    		}else{
    			
    			fragment = fragments.get(position);
    		}
    		recent_fragment_number=2;
    	}
    	if(position==3){
    		ll_random.setBackgroundResource(R.color.ll_leftdrawer_nonchecked);
			ll_liked.setBackgroundResource(R.color.ll_leftdrawer_nonchecked);
			ll_setting.setBackgroundResource(R.color.ll_leftdrawer_checked);
			setTitle(R.string.list_left_three);
    		if(fragments.get(position)==null){
    			
    			fragment = new SettingFragment();
    		}else{
    			
    			fragment = fragments.get(position);
    		}
    		recent_fragment_number=3;
    	}
    	
        Bundle args = new Bundle();
        args.putInt(Constant.ARG_FRAGMENT_INFO, info);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        mDrawerLayout.closeDrawer(mDrawerLeftLayout);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        actionbar.setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	recent_fragment_number=-1;
            
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            
        } else if (keyCode == KeyEvent.KEYCODE_HOME) {
            // 
            
            
        }
        return super.onKeyDown(keyCode, event);
    }


}