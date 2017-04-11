package com.shawnway.nav.app.yylg.bean;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class GoodBean implements Serializable {

    private static final long serialVersionUID = -4985529336109329835L;

    public static final String TYPE_PROGRESS = "OPEN";//进行中
    public static final String TYPE_COUNTDOWN = "COUNTDOWN";//倒计时
    public static final String TYPE_CALCULATING = "CALCULATING";//计算中
    public static final String TYPE_RELEAVED = "ANNOUNCED";//已揭晓
    public static final String TYPE_CLOSE = "CLOSED";//已下架的数据
    public static final String TYPE_ERROR = "ERROR";//机器故障

    public String winnerPartCnt;
    long drawCycleID;
    long productID;
    String callOffDate;
    String prodName;
    String thumbnail;//图片地址的url
    String finalRslt;
    String winnerName;
    String announceDate;
    int buyUnit;
    int cycle;
    int puredCnt;
    int totCnt;
    int leftCnt;
    String drawStatus = null;
    int price;
    long remainTime = -1;//剩余时间，倒计时用

    String drawDate = null;//开奖时间（已完成TODO）

    String serverTime;//服务器的时间
    String realName;//实体区的名字
    String realPwd;//实体区的密码
    String realValue;//第几个实体区
    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public String getRealPwd() {
        return realPwd;
    }

    public void setRealPwd(String realPwd) {
        this.realPwd = realPwd;
    }

    public String getRealValue() {
        return realValue;
    }

    public void setRealValue(String realValue) {
        this.realValue = realValue;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getDrawDate() {
        return drawDate;
    }

    public void setDrawDate(String drawDate) {
        this.drawDate = drawDate;
    }

    public GoodBean(GoodDetail goodDetail) {
        drawCycleID = goodDetail.drawCycleID;
        productID = goodDetail.productID;
        callOffDate = goodDetail.callOffDate;
        prodName = goodDetail.prodName;
        thumbnail = goodDetail.thumbnail;
        finalRslt = goodDetail.finalRslt;
        buyUnit = goodDetail.buyUnit;
        cycle = goodDetail.cycle;
        puredCnt = goodDetail.puredCnt;
        totCnt = goodDetail.totCnt;
        drawStatus = goodDetail.drawStatus;
        price = goodDetail.price;
        remainTime = goodDetail.remainTime;
        drawDate = goodDetail.drawDate;//开奖时间（已完成TODO）
    }

    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public GoodBean() {

    }

    public GoodBean(JSONObject jo) {
        try {
            setProdName(jo.getString("prodName"));
            setPuredCnt(jo.getInt("puredCnt"));
            setTotCnt(jo.getInt("totCnt"));
            setLeftCnt(jo.getInt("leftCnt"));
            setThumbnail(jo.getString("thumbnail"));
            setDrawCycleID(jo.getInt("drawCycleID"));

            setCycle(jo.getInt("cycle"));
            setDrawStatus(jo.getString("drawStatus"));
            setRemainTime(jo.getLong("remainTime"));
            setDrawDate(jo.getString("drawDate"));//开奖时间（已完成TODO）

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public GoodBean(String thumbnail, String title, long remainTime, long id) {
        this.drawStatus = TYPE_CALCULATING;
        this.drawCycleID = id;
        this.thumbnail = thumbnail;
        this.prodName = title;
        this.remainTime = remainTime;
    }


    public int getBuyUnit() {
        return buyUnit;
    }

    public void setBuyUnit(int buyUnit) {
        this.buyUnit = buyUnit;
    }

    public long getProductID() {
        return productID;
    }

    public void setProductID(long productID) {
        this.productID = productID;
    }

    public long getDrawCycleID() {
        return drawCycleID;
    }

    public void setDrawCycleID(long drawCycleID) {
        this.drawCycleID = drawCycleID;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public int getCycle() {
        return cycle;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
    }


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

    public int getLeftCnt() {
        return leftCnt;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }

    public String getAnnounceDate() {
        return announceDate;
    }

    public void setAnnounceDate(String announceDate) {
        this.announceDate = announceDate;
    }

    public void setLeftCnt(int leftCnt) {
        this.leftCnt = leftCnt;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }


    public void setRemainTime(long time) {
        remainTime = time;
    }

    public void setDrawStatus(String drawStatus){
        this.drawStatus = drawStatus;
    }

    public String getDrawStatus(){
        return drawStatus;
    }

    public String getCallOffDate() {
        return callOffDate;
    }

    public void setCallOffDate(String callOffDate) {
        this.callOffDate = callOffDate;
    }

    public String getFinalRslt() {
        return finalRslt;
    }

    public void setFinalRslt(String finalRslt) {
        this.finalRslt = finalRslt;
    }

    public String getProdName() {
        return prodName;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public long getRemainTime() {
        return remainTime;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public CarItemBean toCarItemBean() {
        return new CarItemBean(this);
    }

    public void clone(GoodBean bean) {
        setBuyUnit(bean.getBuyUnit());
        setCycle(bean.getCycle());
        setDrawCycleID(bean.getDrawCycleID());
        setLeftCnt(bean.getLeftCnt());
        setProdName(bean.getProdName());
        setPuredCnt(bean.getPuredCnt());
        setTotCnt(bean.getTotCnt());
        setThumbnail(bean.getThumbnail());
        setFinalRslt(bean.getFinalRslt());
    }

    public boolean equalsTo(GoodBean c) {
        return (buyUnit == c.getBuyUnit() && cycle == c.getCycle() && drawCycleID == c.getDrawCycleID() && leftCnt == c.getLeftCnt() &&
                prodName.equals(c.prodName) && puredCnt == c.getPuredCnt() && totCnt == c.getTotCnt());

    }

    public /*int*/String getDisplayType() {
        //等服务器在商品详情接口补上商品状态后需要替换掉getLeftCnt!=0那个条件，这个条件太弱，无法准确判断是否计算中
        if (getDrawStatus() != null){
            switch (getDrawStatus()){
                case TYPE_CALCULATING:
                    return TYPE_CALCULATING;
                case TYPE_COUNTDOWN:
                    return TYPE_COUNTDOWN;
                case TYPE_PROGRESS:
                    return TYPE_PROGRESS;
                case TYPE_RELEAVED:
                    return TYPE_RELEAVED;
                case TYPE_CLOSE:
                    return TYPE_CLOSE;
                case TYPE_ERROR:
                    return TYPE_ERROR;
                default:
                    return null;
            }
        }
        else
        {
            if (getRemainTime() == -1 && getCallOffDate() == null && getFinalRslt() == null && getLeftCnt() != 0)
                return TYPE_PROGRESS;
            else if (getFinalRslt() != null)
                return TYPE_RELEAVED;
            return TYPE_CALCULATING;
        }

    }
}