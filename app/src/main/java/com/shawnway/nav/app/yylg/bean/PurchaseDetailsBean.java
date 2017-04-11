package com.shawnway.nav.app.yylg.bean;

import com.shawnway.nav.app.yylg.mvp.user.user_buy_record.bean.MyBuyRecordsBean;

import java.util.List;

/**
 * Created by Kevin on 2016/12/14
 */

public class PurchaseDetailsBean {

    /**
     * header : {"version":"1.0","fieldErrors":[],"code":"000"}
     * body : {"drawCycleId":911,"prodName":"ddd","buyUnit":1,"cycle":6,"drawStatus":"OPEN","puredCnt":17,"totCnt":30,"leftCnt":13,"thumbnail":"http://yiyuangou.oss-cn-shenzhen.aliyuncs.com/thumbs/images%2F%E5%B0%8F%E7%B1%B3%E7%BA%A2%E7%B1%B33%2F2016090320223900024_600x600.PNG","drawDetails":[{"purchSummary":"2016-12-14 10:41:19 000购买了1份","drawNumbers":"10000005 "},{"purchSummary":"2016-12-14 10:35:02 000购买了1份","drawNumbers":"10000030 "}],"totalPurch":2}
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

    public static class BodyBean extends MyBuyRecordsBean.BodyBean.PurchaseDetailsListBean {

    }
}
