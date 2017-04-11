package com.shawnway.nav.app.yylg.bean;

/**
 * Created by Eiffel on 2015/11/4.
 */
public class HomeItemBean {
    int type=-1;
    String data;
    int tag;


    public HomeItemBean(int type){
        this.type=type;
    }

    public HomeItemBean(int type, String data){
        this(type);
        this.data=data;
    }
    public HomeItemBean(int type, String data,int tag){
        this(type);
        this.data=data;
        this.tag=tag;
    }



    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }


}
