package com.shawnway.nav.app.yylg.mvp.user.user_invite.explain.bean;

import java.util.List;

/**
 * Created by Kevin on 2016/12/2
 */

public class CommissionExplain {

    /**
     * header : {"version":"1.0","fieldErrors":[],"code":"000"}
     * body : {"commission":"2000.00","invitationNumber":0}
     */

    private HeaderBean header;
    private BodyBean body;

    public HeaderBean getHeader() {
        return header;
    }

    public void setHeader(HeaderBean header) {
        this.header = header;
    }

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public static class HeaderBean {
        /**
         * version : 1.0
         * fieldErrors : []
         * code : 000
         */

        private String version;
        private String code;
        private List<?> fieldErrors;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public List<?> getFieldErrors() {
            return fieldErrors;
        }

        public void setFieldErrors(List<?> fieldErrors) {
            this.fieldErrors = fieldErrors;
        }
    }

    public static class BodyBean {
        /**
         * commission : 2000.00
         * invitationNumber : 0
         */

        private String commission;
        private int invitationNumber = -1;

        public String getCommission() {
            return commission;
        }

        public void setCommission(String commission) {
            this.commission = commission;
        }

        public int getInvitationNumber() {
            return invitationNumber;
        }

        public void setInvitationNumber(int invitationNumber) {
            this.invitationNumber = invitationNumber;
        }
    }
}
