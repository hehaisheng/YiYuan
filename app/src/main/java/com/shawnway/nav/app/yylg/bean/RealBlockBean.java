package com.shawnway.nav.app.yylg.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/6 0006.
 */
public class RealBlockBean implements Serializable {
    private static final long serialVersionUID = -4985529336101234567L;

    int puredCnt;
    int totCnt;
    String thumbnail;//图片地址的url
    String prodName;

    public int getPuredCnt() {
        return puredCnt;
    }

    public void setPuredCnt(int puredCnt) {
        this.puredCnt = puredCnt;
    }

    public int getTotCnt() {
        return totCnt;
    }

    public void setTotCnt(int totCnt) {
        this.totCnt = totCnt;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

}
