package com.shawnway.nav.app.yylg.bean;

/**
 * Created by Eiffel on 2015/12/3.
 */
public class SignResponseWrapper extends  ResponseGson<SignResponseWrapper.SignBean>{

    public static class SignBean {
        public String signStatus;
        public int continueSignDay;
        public int signPoint;
        public int continueSignPoint;
    }
}
