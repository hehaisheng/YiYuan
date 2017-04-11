package com.shawnway.nav.app.yylg.tool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.shawnway.nav.app.yylg.R;
import com.switfpass.pay.utils.Util;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by Eiffel on 2015/12/27.
 */
public class ShareFriendUtil {
    private IWXAPI api;
    private Context context;

    public ShareFriendUtil(Context context){
        this.context=context;
        api = WXAPIFactory.createWXAPI(context, Constants.APP_ID);
        Log.d("微信", "将APP注册到微信");
    }

    public void sendWebPage(boolean toWXCircle){
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "http://a.app.qq.com/o/simple.jsp?pkgname=com.shawnway.nav.app.yylg";
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "一元乐购  乐购中国";
        msg.description = "商品种类齐全，购物模式新颖，奖励多多，你也来看看吧！";
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.yy_appicon);
        Bitmap thumb = Bitmap.createScaledBitmap(bmp,100,100,true);
        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumb,true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");//唯一标识一个请求
        req.message = msg;//发送的内容
        //分享到朋友圈或者好友会话
        req.scene = toWXCircle ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
