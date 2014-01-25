package com.example.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.pushtester.R;

public class FragmentName extends Fragment{

	private INameListener	mListener;
	public static String TAG_LOGGING	= FragmentName.class.getSimpleName();
	
	public interface INameListener{
		abstract void onSubmitNameRequested(String name);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_enter_name, container, false);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			mListener = (INameListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement INameListener");
		}
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		
		
		Button btnSubmit	= (Button) getView().findViewById(R.id.enter_name_btn_submit);
		btnSubmit.setOnClickListener(new OnClickListener() {
			public void onClick(View v){
				EditText edtName	= (EditText) getView().findViewById(R.id.enter_name_edit);
				String name		= edtName.getText().toString();
				
				if(name.isEmpty()){
					Log.i(TAG_LOGGING, "No input name.");
					showAlert(
						getString(R.string.alert_noname_title),
						getString(R.string.alert_noname_message)
					);
					return;
				}
				
				if(!isNetworkConnected()){
					Log.w(TAG_LOGGING, "No internet connection found.");
					showAlert(
						getString(R.string.alert_nointernet_title),
						getString(R.string.alert_nointernet_message)
					);
					return;
				}
				
				mListener.onSubmitNameRequested(name);
			}
		});
	}
	
	private void showAlert(String title, String message){
		new AlertDialog.Builder(getActivity())
		.setTitle(title)
		.setMessage(message)
		.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) { 
				//Fire Ze Missiles
			}
		 })
		.show();
	}
	
	private boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		return (cm.getActiveNetworkInfo() != null);
	}
	
}
