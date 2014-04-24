package net.gameprix.android.project_d;

import android.os.Bundle;
import android.util.Log;

import com.unity3d.player.UnityPlayerActivity;


public class MainActivity extends UnityPlayerActivity {
	private final String TAG = "UC_SDK";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Log.v(TAG,"main activity create");
	}
	
	public void RequestLoginFromUnity(){
		Log.v(TAG, "login--------------start");
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				UCSDKManager.instance().initUCSDK(MainActivity.this);
			}
		});
		
	}
	
}
