package com.shawnway.nav.app.yylg.mvp.user.user_invite.explain.bean;

import java.util.List;

/**
 * Created by Kevin on 2016/12/1
 */

public class InviteCode {

    /**
     * header : {"version":"1.0","fieldErrors":[],"code":"000"}
     * body : {"customerDetails":{"invitationCode":"12769691","invitatorCode":""}}
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
         * customerDetails : {"invitationCode":"12769691","invitatorCode":""}
         */

        private CustomerDetailsBean customerDetails;

        public CustomerDetailsBean getCustomerDetails() {
            return customerDetails;
        }

        public void setCustomerDetails(CustomerDetailsBean customerDetails) {
            this.customerDetails = customerDetails;
        }

        public static class CustomerDetailsBean {
            /**
             * invitationCode : 12769691
             * invitatorCode :
             */

            private String invitationCode;
            private String invitatorCode;

            public String getInvitationCode() {
                return invitationCode;
            }

            public void setInvitationCode(String invitationCode) {
                this.invitationCode = invitationCode;
            }

            public String getInvitatorCode() {
                return invitatorCode;
            }

            public void setInvitatorCode(String invitatorCode) {
                this.invitatorCode = invitatorCode;
            }
        }
    }
}
