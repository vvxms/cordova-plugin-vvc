package xu.li.cordova.wechat;

import org.apache.cordova.CallbackContext; 
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONException;
import org.json.JSONObject;
 

import android.util.Base64;
import android.util.Log;
  
import com.tencent.mm.sdk.modelmsg.SendAuth; 

public class Wechat extends CordovaPlugin { 
 
     public static Wechat instance = null;
 
    @Override
    protected void pluginInitialize() {

        super.pluginInitialize(); 
    } 
   

    @Override
    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException { 
 
        if (action.equals("sendAuthRequest")) {
            return true;
        }  

        return false;
    }  
}
