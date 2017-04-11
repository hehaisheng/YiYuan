package com.shawnway.nav.app.yylg.bean;

/**
 * Created by Eiffel on 2015/12/7.
 */
public class AssertResponseWraper extends ResponseGson<AssertResponseWraper.BankBody> {

    public static class BankBody {
        public String balance;
        public String point;
        public String commission;
    }
}