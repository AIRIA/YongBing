package net.gameprix.android.project_d;

import com.unity3d.player.UnityPlayer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import cn.uc.gamesdk.GameUserLoginResult;
import cn.uc.gamesdk.IGameUserLogin;
import cn.uc.gamesdk.UCCallbackListener;
import cn.uc.gamesdk.UCCallbackListenerNullException;
import cn.uc.gamesdk.UCFloatButtonCreateException;
import cn.uc.gamesdk.UCGameSDK;
import cn.uc.gamesdk.UCGameSDKStatusCode;
import cn.uc.gamesdk.UCLogLevel;
import cn.uc.gamesdk.UCLoginFaceType;
import cn.uc.gamesdk.UCOrientation;
import cn.uc.gamesdk.info.FeatureSwitch;
import cn.uc.gamesdk.info.GameParamInfo;

public class UCSDKManager {

	private Activity mainActivity;
	/*------------------UC Platform -------------------------------------------------------*/
	private static UCSDKManager _instance;
	
	public static UCSDKManager instance(){
		if(_instance==null){
			_instance = new UCSDKManager();
		}
		return _instance;
	}
	
	
	public void initUCSDK(Activity activity) {
		mainActivity = activity;
		final ProgressDialog dialog = ProgressDialog.show(mainActivity, "", "正在初始化",
				true);
		dialog.setCancelable(false);
		GameParamInfo gpi = new GameParamInfo();
		gpi.setCpId(31477);
		gpi.setGameId(538939);
		gpi.setServerId(0);
		gpi.setFeatureSwitch(new FeatureSwitch(true, false));
		try {
			UCGameSDK.defaultSDK().setOrientation(UCOrientation.LANDSCAPE);
			UCGameSDK.defaultSDK().setLogoutNotifyListener(logoutListener);
			UCGameSDK.defaultSDK().setLoginUISwitch(UCLoginFaceType.USE_WIDGET);
			
			UCGameSDK.defaultSDK().initSDK(mainActivity, UCLogLevel.ERROR, true, gpi, new UCCallbackListener<String>() {
				@Override
				public void callback(int code, String msg) {
					System.out.println("msg:" + msg);// 返回的消息
					switch (code) {
					case UCGameSDKStatusCode.SUCCESS: // 初始化成功,可以执行后续的登录充值操作
						dialog.dismiss();
						ucLogin();
						break;
					case UCGameSDKStatusCode.INIT_FAIL: // 初始化失败,不能进行后续操作
						initUCSDK(mainActivity);
						break;
					default:
						break;
					}
				}
			});
		} catch (UCCallbackListenerNullException e) {
			e.printStackTrace();
		}
	}

	private void ucLogin() {

		mainActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					UCGameSDK.defaultSDK().login(mainActivity, new UCCallbackListener<String>() {
						@Override
						public void callback(int code, String msg) {
							switch (code) {
							case UCGameSDKStatusCode.SUCCESS:
								String sid = UCGameSDK.defaultSDK().getSid();
								String resultExp = "{\"user_id\": \"" + 
							              sid + "\"," + 
							              "\"open_id\"" + ": " + 123 + "," + 
							              "\"nickname\"" + ": " + "\"" + "Hello" + "\"" + "," + 
							              "\"getchannel\"" + ": " + "\"" + "119" + "\"" + 
							              "}";
								Log.d("UC_SDK", resultExp);
								UnityPlayer.UnitySendMessage("UnityAndroidManager", "LoginSuccessFromChina", resultExp);;
								break;
							case UCGameSDKStatusCode.FAIL:
								break;
							default:
								break;
							}

						}
					}, new IGameUserLogin() {

						@Override
						public GameUserLoginResult process(String arg0, String arg1) {
							return null;
						}
					}, "游戏官方帐号");
				} catch (UCCallbackListenerNullException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	/* 用户登出操作 在程序退出的时候调用 */
	public void ucLogout() {
		UCGameSDK.defaultSDK().exitSDK(mainActivity, new UCCallbackListener<String>() {
			@Override
			public void callback(int code, String data) {
				if (UCGameSDKStatusCode.SDK_EXIT == code) {
					// 在此加入退出游戏的代码
					Log.e("UCGameSDK", "退出SDK");
					destoryFloatButton();
				}
			}
		});
	}

	/* 创建UC悬浮按钮 */
	public void createUCFloatButton() {
		mainActivity.runOnUiThread(new Runnable() {
			public void run() {
				try {
					// 创建悬浮按钮。该悬浮按钮将悬浮显示在GameActivity页面上，点击时将会展开悬浮菜单，菜单中含有
					// SDK 一些功能的操作入口。
					// 创建完成后，并不自动显示，需要调用showFloatButton(Activity,
					// double, double, boolean)方法进行显示或隐藏。
					UCGameSDK.defaultSDK().createFloatButton(mainActivity, new UCCallbackListener<String>() {

						@Override
						public void callback(int statuscode, String data) {
							Log.d("SelectServerActivity`floatButton Callback", "statusCode == " + statuscode + "  data == " + data);
						}
					});

				} catch (UCCallbackListenerNullException e) {
					e.printStackTrace();
				} catch (UCFloatButtonCreateException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 必接功能<br>
	 * 悬浮按钮显示<br>
	 * 悬浮按钮显示需要在UI线程中调用<br>
	 */
	public void showUCFloatButton() {
		mainActivity.runOnUiThread(new Runnable() {
			public void run() {
				// 显示悬浮图标，游戏可在某些场景选择隐藏此图标，避免影响游戏体验
				try {
					UCGameSDK.defaultSDK().showFloatButton(mainActivity, 100, 50, true);
				} catch (UCCallbackListenerNullException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 必接功能<br>
	 * 悬浮按钮销毁<br>
	 * 悬浮按钮销毁需要在UI线程中调用<br>
	 */
	public void destoryFloatButton() {
		mainActivity.runOnUiThread(new Runnable() {
			public void run() {
				// 悬浮按钮销毁功能
				UCGameSDK.defaultSDK().destoryFloatButton(mainActivity);
			}
		});
	}
	
	UCCallbackListener<String> logoutListener = new UCCallbackListener<String>() {

		@Override
		public void callback(int statuscode, String data) {
			switch (statuscode) {
			case UCGameSDKStatusCode.NO_INIT:
				initUCSDK(mainActivity);
				break;
			case UCGameSDKStatusCode.NO_LOGIN:
				ucLogin();
				break;
			case UCGameSDKStatusCode.SUCCESS:
				
				break;
			case UCGameSDKStatusCode.FAIL:
				ucLogout();
				break;
			default:
				break;
			}
		}
	};
}
