package com.shawnway.nav.app.yylg.bean;

import com.google.gson.annotations.SerializedName;

public class WeChatPayBody {
    public String appid;
    public String partnerid;
    public String prepayid;
    public String noncestr;
    public String timestamp;
    public String sign;
    public String outTradeNo;

    @SerializedName("package")
    public String pack;
}