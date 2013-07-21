package com.xianglanqi.angrygirl.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;

public class WXAdapter {

    private static final WXAdapter instance = new WXAdapter();

    private WXAdapter() {

    }

    public static final WXAdapter getInstance() {
        return instance;
    }

    private static final String APP_ID = "wx62b2217684d2e7be";

    // private static final String APP_KEY = "84ba6858160d5a7e251c4825b8755cfb";

    private IWXAPI api;

    public void regToWX(Context context) {
        if (null == api) {
            api = WXAPIFactory.createWXAPI(context, APP_ID, true);
            boolean b = api.registerApp(APP_ID);
            Log.d("hy", "registerApp: " + b);
        }
    }

    public void sendImageToTimeline(final String description, final Bitmap bitmap) {
        WXImageObject imageObj = new WXImageObject(bitmap);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imageObj;
        msg.description = description;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;

        Log.d("hy", "sendReq:" + api.sendReq(req));
    }

    public boolean isSupportTimeline() {
        return api.getWXAppSupportAPI() >= 0x21020001;
    }

}
