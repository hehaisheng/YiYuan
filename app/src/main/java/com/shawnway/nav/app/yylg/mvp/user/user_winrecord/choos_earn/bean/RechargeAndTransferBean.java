package com.shawnway.nav.app.yylg.mvp.user.user_winrecord.choos_earn.bean;

import com.nostra13.universalimageloader.utils.L;

import java.math.BigDecimal;

/**
 * Created by Kevin on 2016/12/8
 */

public class RechargeAndTransferBean {
    private String cellphone;
    private String username;
    private BigDecimal amountOfcard;
    private Long winprizeId;
    private String alipayAccount;

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BigDecimal getAmountOfcard() {
        return amountOfcard;
    }

    public void setAmountOfcard(BigDecimal amountOfcard) {
        this.amountOfcard = amountOfcard;
    }

    public Long getWinprizeId() {
        return winprizeId;
    }

    public void setWinprizeId(Long winprizeId) {
        this.winprizeId = winprizeId;
    }

    public String getAlipayAccount() {
        return alipayAccount;
    }

    public void setAlipayAccount(String alipayAccount) {
        this.alipayAccount = alipayAccount;
    }
}
