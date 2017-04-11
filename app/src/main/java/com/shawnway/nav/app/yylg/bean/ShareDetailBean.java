package com.shawnway.nav.app.yylg.bean;

/**
 * Created by Eiffel on 2015/11/7.
 */
public class ShareDetailBean {
    private String mData;

    public ShareDetailBean(String url) {
        mData=url;
    }

    public String getData() {
        return mData;
    }
}
