package xu.li.cordova.wechat;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaArgs;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.util.Base64;
import android.util.Log;
  
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX; 
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class Wechat extends CordovaPlugin {

    public static final String TAG = "Cordova.Plugin.Wechat";

    public static final String WXAPPID_PROPERTY_KEY = "wechatappid";

    public static final String ERROR_WECHAT_NOT_INSTALLED = "未安装微信";
    public static final String ERROR_INVALID_PARAMETERS = "参数格式错误";
    public static final String ERROR_SEND_REQUEST_FAILED = "发送请求失败";
    public static final String ERROR_WECHAT_RESPONSE_COMMON = "普通错误";
    public static final String ERROR_WECHAT_RESPONSE_USER_CANCEL = "用户点击取消并返回";
    public static final String ERROR_WECHAT_RESPONSE_SENT_FAILED = "发送失败";
    public static final String ERROR_WECHAT_RESPONSE_AUTH_DENIED = "授权失败";
    public static final String ERROR_WECHAT_RESPONSE_UNSUPPORT = "微信不支持";
    public static final String ERROR_WECHAT_RESPONSE_UNKNOWN = "未知错误";

    public static final String EXTERNAL_STORAGE_IMAGE_PREFIX = "external://";

    public static final String KEY_ARG_MESSAGE = "message";
    public static final String KEY_ARG_SCENE = "scene";
    public static final String KEY_ARG_TEXT = "text";
    public static final String KEY_ARG_MESSAGE_TITLE = "title";
    public static final String KEY_ARG_MESSAGE_DESCRIPTION = "description";
    public static final String KEY_ARG_MESSAGE_THUMB = "thumb";
    public static final String KEY_ARG_MESSAGE_MEDIA = "media";
    public static final String KEY_ARG_MESSAGE_MEDIA_TYPE = "type";
    public static final String KEY_ARG_MESSAGE_MEDIA_WEBPAGEURL = "webpageUrl";
    public static final String KEY_ARG_MESSAGE_MEDIA_IMAGE = "image";
    public static final String KEY_ARG_MESSAGE_MEDIA_TEXT = "text";
    public static final String KEY_ARG_MESSAGE_MEDIA_MUSICURL = "musicUrl";
    public static final String KEY_ARG_MESSAGE_MEDIA_MUSICDATAURL = "musicDataUrl";
    public static final String KEY_ARG_MESSAGE_MEDIA_VIDEOURL = "videoUrl";
    public static final String KEY_ARG_MESSAGE_MEDIA_FILE = "file";
    public static final String KEY_ARG_MESSAGE_MEDIA_EMOTION = "emotion";
    public static final String KEY_ARG_MESSAGE_MEDIA_EXTINFO = "extInfo";
    public static final String KEY_ARG_MESSAGE_MEDIA_URL = "url";

    public static final int TYPE_WECHAT_SHARING_APP = 1;
    public static final int TYPE_WECHAT_SHARING_EMOTION = 2;
    public static final int TYPE_WECHAT_SHARING_FILE = 3;
    public static final int TYPE_WECHAT_SHARING_IMAGE = 4;
    public static final int TYPE_WECHAT_SHARING_MUSIC = 5;
    public static final int TYPE_WECHAT_SHARING_VIDEO = 6;
    public static final int TYPE_WECHAT_SHARING_WEBPAGE = 7;
    public static final int TYPE_WECHAT_SHARING_TEXT = 8;

    public static final int SCENE_SESSION = 0;
    public static final int SCENE_TIMELINE = 1;
    public static final int SCENE_FAVORITE = 2;

    public static final int MAX_THUMBNAIL_SIZE = 320;

    protected CallbackContext currentCallbackContext;
    protected IWXAPI wxAPI;
    protected String appId;
    public static Wechat instance = null;
 

    @Override
    protected void pluginInitialize() {

        super.pluginInitialize();

        instance = this;

        initWXAPI();

        Log.d(TAG, "plugin initialized.");
    } 
  
   protected void initWXAPI() {
        if (wxAPI == null) {
            String appId = getAppId();

            wxAPI = WXAPIFactory.createWXAPI(webView.getContext(), appId, true);
            wxAPI.registerApp(appId);
        }
    }

    public IWXAPI getWxAPI() {
        return wxAPI;
    } 

    public CallbackContext getCurrentCallbackContext() {
        return currentCallbackContext;
    }

    @Override
    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {
        Log.d(TAG, String.format("%s is called. Callback ID: %s.", action, callbackContext.getCallbackId()));
 
        if (action.equals("sendAuthRequest")) {
            return sendAuthRequest(args, callbackContext);
        }  

        return false;
    } 

    protected boolean sendAuthRequest(CordovaArgs args, CallbackContext callbackContext) {
        final IWXAPI api = getWXAPI();

        final SendAuth.Req req = new SendAuth.Req();
        try {
            req.scope = args.getString(0);
            req.state = args.getString(1);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());

            req.scope = "snsapi_userinfo";
            req.state = "wechat";
        }

        if (api.sendReq(req)) {
            Log.i(TAG, "Auth request has been sent successfully.");

            // send no result
            sendNoResultPluginResult(callbackContext);
        } else {
            Log.i(TAG, "Auth request has been sent unsuccessfully.");

            // send error
            callbackContext.error(ERROR_SEND_REQUEST_FAILED);
        }

        return true;
    } 
}
