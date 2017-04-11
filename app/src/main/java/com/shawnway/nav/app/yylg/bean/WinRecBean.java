package com.shawnway.nav.app.yylg.bean;

import android.content.Context;
import android.util.Log;

import com.shawnway.nav.app.yylg.R;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Eiffel on 2015/11/19.
 */
public class WinRecBean implements Serializable {


    public final static String BALANCE = "BALANCE";//转入余额
    public final static String SELF_PICKEDUP = "SELF_PICKEDUP";//实体店领取
    public final static String TRFR_APLIPAY = "TRFR_APLIPAY";//转入支付宝
    public final static String POST = "POST";
    public final static String DONATE = "DONATE";//可赠与好友
    public final static String PHONE_RECHARGE = "PHONE_RECHARGE";//充值
    public final static String WITHDRAW_DEPOSIT = "WITHDRAW_DEPOSIT";//提现

    private static final String STA_LOGISTIC_SENT = "LOGISTIC_SENT";//已发货
    private static final String STA_CLAIMED = "CLAIMED";//已领取
    private static final String STA_SHOWED = "SHOWED";//已晒单
    private static final String STA_UNCLAIMED = "UNCLAIMED";//未领取
    private static final String STA_PROCESSING = "PROCESSING";//处理中，等待发货
    private static final String TAG = "WinRecBean";


    public static final int TYPE_ENSURE = 1;//已发货，即“可确认收货”状态
    public static final int TYPE_SHARED = 2;//已晒单
    public static final int TYPE_CHOOSE_ADDRESS = 3;//编辑收货地址
    public static final int TYPE_PICKUP = 4;//实体店领取
    public static final int TYPE_CHOOSE = 5;//需要选择收货方式
    public static final int TYPE_SHARE = 6;//可晒单
    public static final int TYPE_DONATE = 7;//可赠与好友
    public static final int TYPE_PROCESSING = 8;//处理中，等待发货
    public static final int TYPE_BALANCE = 9;//转入余额
    public static final int TYPE_PHONE_RECHARGE = 10;//充值
    public static final int TYPE_WITHDRAW_DEPOSIT = 11;//提现
    public static final int TYPE_ERROR = -1;


    int drawcycleId;
    int winprizeId;
    int cycle;
    public String productName;
    String finalResult;
    String announceDate;
    String claimStatus;
    String thumbnail;
    ArrayList<String> claimTypeList;
    String finalClaimType;
    boolean donatable;

    public String getLogAddr() {
        return logAddr;
    }

    public void setLogAddr(String logAddr) {
        this.logAddr = logAddr;
    }

    public String getLogRecvr() {
        return logRecvr;
    }

    public void setLogRecvr(String logRecvr) {
        this.logRecvr = logRecvr;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getExpressCompany() {
        return expressCompany;
    }

    public void setExpressCompany(String expressCompany) {
        this.expressCompany = expressCompany;
    }

    public String getExpressCompanyPhone() {
        return expressCompanyPhone;
    }

    public void setExpressCompanyPhone(String expressCompanyPhone) {
        this.expressCompanyPhone = expressCompanyPhone;
    }

    public String getTrackingNo() {
        return trackingNo;
    }

    public void setTrackingNo(String trackingNo) {
        this.trackingNo = trackingNo;
    }

    String trackingNo;
    boolean isAllowPrizeShow;
    String expressCompany;
    String expressCompanyPhone;
    String updateDate;
    String logRecvr;
    String logAddr;


    String originalOwner;//赠与者originalOwner
    int buyUnit;

    private String buttontext;
    private String statuetext;
    private int type;

    private double purPrice;//已完成TODO:后台要增加的字段，商品的原价，用于转到余额的时候显示给用户看，用户可以根据价格选择是否转到余额

    private BigDecimal amountOfcard;

    public double getPurPrice() {
        return purPrice;
    }

    public void setPurPrice(double purPrice) {
        this.purPrice = purPrice;
    }

    public boolean isAllowPrizeShow() {
        return isAllowPrizeShow;
    }

    public void setAllowPrizeShow(boolean allowPrizeShow) {
        this.isAllowPrizeShow = allowPrizeShow;
    }

    public String getOriginalOwner() {
        return originalOwner;
    }

    public void setOriginalOwner(String originalOwner) {
        this.originalOwner = originalOwner;
    }

    public int getBuyUnit() {
        return buyUnit;
    }


    public void setBuyUnit(int buyUnit) {
        this.buyUnit = buyUnit;
    }

    public int getDrawcycleId() {
        return drawcycleId;
    }

    public void setDrawcycleId(int drawcycleId) {
        this.drawcycleId = drawcycleId;
    }

    public int getCycle() {
        return cycle;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getAnnounceDate() {
        return announceDate;
    }

    public void setAnnounceDate(String announceDate) {
        this.announceDate = announceDate;
    }

    public String getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(String claimStatus) {
        this.claimStatus = claimStatus;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public ArrayList<String> getClaimTypeList() {
        return claimTypeList;
    }

    public void setClaimTypeList(ArrayList<String> claimTypeList) {
        this.claimTypeList = claimTypeList;
    }

    public boolean isDonatable() {
        return donatable;
    }


    public void setDonatable(boolean donatable) {
        this.donatable = donatable;
    }

    public int getWinprizeId() {
        return winprizeId;
    }

    public void setWinprizeId(int winprizeId) {
        this.winprizeId = winprizeId;
    }

    public String getFinalClaimType() {
        return finalClaimType;
    }

    public void setFinalClaimType(String finalClaimType) {
        this.finalClaimType = finalClaimType;
    }

    public String getFinalResult() {
        return finalResult;
    }

    public void setFinalResult(String finalResult) {
        this.finalResult = finalResult;
    }

    public String getButtontext(Context context) {
        updateStatuses(context);
        return buttontext;
    }

    public void setButtontext(String buttontext) {
        this.buttontext = buttontext;
    }

    public int getType(Context context) {
        updateStatuses(context);
        return type;
    }


    public void setType(int type) {
        this.type = type;
    }

    public String getStatuetext(Context context) {
        if (type == 0 || buttontext == null)
            updateStatuses(context);
        return statuetext;
    }


    public void setStatuetext(String statuetext) {
        this.statuetext = statuetext;
    }

    //用来根据记录状态来设置按钮文字
    private void updateStatuses(Context context) {
        if (claimStatus == null) {
            Log.e(TAG, "a win prize contian no claimStatus");
            return;
        }
        statuetext = getStatusText(context, claimStatus);

        switch (claimStatus) {
            case STA_UNCLAIMED:
                if (finalClaimType == null && ((claimTypeList != null && claimTypeList.size() > 1)))
                    setStatus(buttontext = context.getString(R.string.win_record_action_choose_way), TYPE_CHOOSE);
                else {
                    if (claimTypeList == null || claimTypeList.size() == 0)
                        setStatus(null, TYPE_ERROR);
                    else if (finalClaimType != null) {
                        if (finalClaimType.equals(SELF_PICKEDUP))
                            setStatus(context.getString(R.string.win_record_action_show_pickupstatement), TYPE_PICKUP);
                        else if (finalClaimType.equals(DONATE))
                            setStatus(context.getString(R.string.win_record_action_donate), TYPE_DONATE);
                        else if (finalClaimType.equals(BALANCE))
                            setStatus(context.getString(R.string.choose_btn_to_balance), TYPE_BALANCE);
                        else if (finalClaimType.equals(POST))
                            setStatus(context.getString(R.string.win_record_action_choose_address), TYPE_CHOOSE_ADDRESS);
                        else if (finalClaimType.equals(PHONE_RECHARGE))
                            setStatus("充值", TYPE_PHONE_RECHARGE);
                        else if (finalClaimType.equals(WITHDRAW_DEPOSIT))
                            setStatus("虚拟充值", TYPE_WITHDRAW_DEPOSIT);
                    }
                }
                break;
            case STA_LOGISTIC_SENT:
                setStatus(context.getString(R.string.win_record_action_confirm_receive), TYPE_ENSURE);
                break;
            case STA_CLAIMED:
                if (!isAllowPrizeShow()) {//
                    setStatus(context.getString(R.string.win_record_action_can_not_share), TYPE_ERROR);
                } else {
                    setStatus(context.getString(R.string.win_record_action_claimed), TYPE_SHARE);
                }
                break;
            case STA_SHOWED:
                setStatus(context.getString(R.string.win_record_action_shared), TYPE_SHARED);
                break;
            case STA_PROCESSING:
                setStatus(null, TYPE_PROCESSING);
                break;
        }
    }

    private String getStatusText(Context context, String claimStatus) {
        switch (claimStatus) {
            case STA_UNCLAIMED:
                return context.getString(R.string.win_record_statue_unclaimed);
            case STA_CLAIMED:
                return context.getString(R.string.win_record_statue_claimed);
            case STA_SHOWED:
                return context.getString(R.string.win_record_statue_showed);
            case STA_LOGISTIC_SENT:
                return context.getString(R.string.win_record_statue_logistic_sent);
            case STA_PROCESSING:
                return context.getString(R.string.win_record_statue_processing);

            default:
                return "";
        }
    }

    private void setStatus(String btText, int tp) {
        buttontext = btText;
        type = tp;
    }

    public BigDecimal getAmountOfcard() {
        return amountOfcard;
    }

    public void setAmountOfcard(BigDecimal amountOfcard) {
        this.amountOfcard = amountOfcard;
    }
}
