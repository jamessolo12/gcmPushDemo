package com.example.pushtester;

import com.example.fragments.FragmentName;
import com.example.fragments.FragmentName.INameListener;
import com.example.fragments.FragmentWelcome;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

public class MainActivity extends FragmentActivity implements INameListener{

	public static final String PREFS_NAME_KEY	= "PREFS_NAME";
	
	private static final String TAG				= MainActivity.class.getSimpleName();
	private static final String PERSON_NAME_KEY = "PERSON_NAME";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		String strName	= getNameFromPrefs();
		
		if(strName.length() > 0){
			Bundle bundle	= new Bundle();
			bundle.putString(FragmentWelcome.TAG_NAME, strName);
			FragmentWelcome frag	= new FragmentWelcome();
			frag.setArguments(bundle);
			ft.replace(R.id.activity_default_fragment_container, frag, FragmentWelcome.class.getSimpleName());
		}else{
			ft.replace(R.id.activity_default_fragment_container, new FragmentName(), FragmentName.class.getSimpleName());
		}
		
		ft.commit();
	}

	@Override
	public void onSubmitNameRequested(String name) {
		storeNameInPrefs(getBaseContext(), name);
		switchFrags(name);
	}
	
	private void storeNameInPrefs(Context context, String name) {
		final SharedPreferences prefs = getSharedPreferences(PREFS_NAME_KEY, MainActivity.MODE_PRIVATE);
		int appVersion = getAppVersion(context);
		Log.i(TAG, "Saving name on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PERSON_NAME_KEY, name);
		editor.commit();
	}
	
	private String getNameFromPrefs(){
		SharedPreferences sp = getSharedPreferences(PREFS_NAME_KEY, MainActivity.MODE_PRIVATE);
		return sp.getString(PERSON_NAME_KEY, "");
	}
	
	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	private void switchFrags(String strName){
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Bundle bundle	= new Bundle();
		bundle.putString(FragmentWelcome.TAG_NAME, strName);
		FragmentWelcome frag	= new FragmentWelcome();
		frag.setArguments(bundle);
		ft.replace(R.id.activity_default_fragment_container, frag, FragmentWelcome.class.getSimpleName());
		ft.commit();
	}
	
	
	
}
