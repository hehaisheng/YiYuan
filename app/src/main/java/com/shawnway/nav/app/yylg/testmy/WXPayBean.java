package com.shawnway.nav.app.yylg.testmy;

/**
 * Created by Administrator on 2017-03-20.
 */

public class WXPayBean {

    private String  charset;
    private String mch_id;
    private  String nonce_str;
    private String services;
    private String sign;
    private String sign_type;
    public String status;
    private String token_id;
    public String version;
    public void  setCharset(String charset1)
    {
        this.charset=charset1;
    }
    public String  getCharset()
    {
        return  this.charset;
    }
    public void setMch_id(String mch_id1)
    {
        this.mch_id=mch_id1;
    }
    public String getMch_id()
    {
        return this.mch_id;
    }
    public void setNonce_str(String nonce_str1)
    {
        this.nonce_str=nonce_str1;
    }
    public String getNonce_str()
    {
        return this.nonce_str;
    }
    public void setServices(String services1)
    {
        this.services=services1;
    }
    public String getServices()
    {
        return this.services;
    }
    public void setSign(String sign1)
    {
        this.sign=sign1;
    }
    public String getSign()
    {
        return this.sign;
    }
    public void setSign_type(String sign_type1)
    {
        this.sign_type=sign_type1;
    }
    public String getSign_type()
    {
        return this.sign_type;
    }
    public void setWxstatus(String wxstatus1)
    {
        this.status=wxstatus1;
    }
    public String getWxstatus()
    {
        return this.status;
    }
    public void setToken_id(String token_id1)
    {
        this.token_id=token_id1;
    }
    public String getToken_id()
    {
        return this.token_id;
    }
    public void setWxversion(String wxversion1)
    {
        this.version=wxversion1;
    }
    public String getWxversion()
    {
        return this.version;
    }



}
