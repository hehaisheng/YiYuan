package com.shawnway.nav.app.yylg.bean;

public class JoinBean {
    String customerName;
    int customerID;
    int purchCount;
    String purchDate;
    String location;
    private String imgUrl;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getPurchCount() {
        return purchCount;
    }

    public void setPurchCount(int purchCount) {
        this.purchCount = purchCount;
    }

    public String getPurchDate() {
        return purchDate;
    }

    public void setPurchDate(String purchDate) {
        this.purchDate = purchDate;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
