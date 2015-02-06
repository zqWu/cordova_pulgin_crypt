package org.wzq.android.crypt;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class PluginCrypt extends CordovaPlugin {
	public final static String TAG = PluginCrypt.class.getSimpleName();

	private AbstractCrypter crypter;
	// password and salgorithm(AES,DES...)
	private static final String ACTION_SET = "setting";
	private static final String ACTION_ENCRYPT = "encrypt";
	private static final String ACTION_DECRYPT = "decrypt";

	@Override
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		super.initialize(cordova, webView);
		Log.v(TAG, "PluginCrypt init...");
	}

	@Override
	public boolean execute(String action, final JSONArray args, final CallbackContext callbackContext)
	        throws JSONException {
		Log.d(TAG, "plugin Mdm, action=" + action + ",rawArgs=" + args.toString());
		if (ACTION_SET.equals(action)) {
			JSONObject setting = args.getJSONObject(0);
			String password = setting.getString("password");
			try {
				crypter = new CryptAES(password);
				callbackContext.success();
			} catch (Exception e) {
				JSONObject jb = new JSONObject();
				jb.put("error", "Crypter init:" + e.toString());
				callbackContext.error(jb);
			}
			return true;
		}

		if (ACTION_ENCRYPT.equals(action)) {
			if (crypter == null) {
				Log.e(TAG, "Crypter not init");
				JSONObject jb = new JSONObject();
				jb.put("error", "Crypter not init");
				callbackContext.error(jb);
			} else {
				this.cordova.getThreadPool().execute(new Runnable() {
					@Override
					public void run() {
						docrypt(args, callbackContext, true);
					}
				});

			}
			return true;
		}

		if (ACTION_DECRYPT.equals(action)) {
			if (crypter == null) {
				Log.e(TAG, "Crypter not init");
				JSONObject jb = new JSONObject();
				jb.put("error", "Crypter not init");
				callbackContext.error(jb);
			} else {
				this.cordova.getThreadPool().execute(new Runnable() {
					@Override
					public void run() {
						docrypt(args, callbackContext, false);
					}
				});
			}
			return true;
		}

		Log.e(TAG, "not support action = " + action);
		return false;
	}

	private void docrypt(final JSONArray args, final CallbackContext callbackContext, boolean isEncrypt) {
		try {
			JSONArray array = new JSONArray();
			for (int i = 0; i < args.length(); i++) {
				String item = args.getString(i);
				JSONObject itemResult = new JSONObject();
				String result;
				try {
					if (isEncrypt) {
						result = crypter.encryptString(item);
					} else {
						result = crypter.decryptString(item);
					}
					itemResult.put("succFlag", "true");
					itemResult.put("value", result);
					Log.v(TAG, item+" --> "+result);
				} catch (Exception e) {
					Log.e(TAG, "encrypt fail:" + e.toString());
					itemResult.put("succFlag", "false");
					itemResult.put("value", e.toString());
				}
				array.put(itemResult);
			}
			callbackContext.success(array);
		} catch (JSONException e) {
			// should not happen
		}
	}
}
