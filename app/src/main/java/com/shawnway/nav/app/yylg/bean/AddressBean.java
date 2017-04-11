package com.shawnway.nav.app.yylg.bean;

import java.io.Serializable;

/**
 * Created by Eiffel on 2015/11/17.
 */
public class AddressBean implements Serializable{
    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    String receiver;
    String cellphone;
    String address;
}
