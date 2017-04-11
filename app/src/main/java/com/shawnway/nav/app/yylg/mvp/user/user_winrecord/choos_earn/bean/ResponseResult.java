package com.shawnway.nav.app.yylg.mvp.user.user_winrecord.choos_earn.bean;

import java.util.List;

/**
 * Created by Kevin on 2016/12/10
 */

public class ResponseResult {

    /**
     * header : {"version":"1.0","fieldErrors":[],"code":"000"}
     * body : {"transferResult":"true"}
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
         * transferResult : true
         */

        private String transferResult;
        private String rechargeResult;

        public String getTransferResult() {
            return transferResult;
        }

        public void setTransferResult(String transferResult) {
            this.transferResult = transferResult;
        }

        public String getRechargeResult() {
            return rechargeResult;
        }

        public void setRechargeResult(String rechargeResult) {
            this.rechargeResult = rechargeResult;
        }
    }
}
