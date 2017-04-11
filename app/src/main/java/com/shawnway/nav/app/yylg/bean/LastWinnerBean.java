package com.shawnway.nav.app.yylg.bean;

import java.util.List;

/**
 * Created by Kevin on 2016/12/16
 */

public class LastWinnerBean {

    /**
     * header : {"version":"1.0","fieldErrors":[],"code":"000"}
     * body : {"announceDate":"2016-10-16 18:34:30","purDate":"2016-10-16 18:22:04 000","winnerName":"137****3456","winnerId":45,"finalRslt":"10000009","cellphone":"13710773456","drawcycleId":787,"imgUrl":"http://122.112.210.44:8080null","location":"广东省广州市"}
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
         * announceDate : 2016-10-16 18:34:30
         * purDate : 2016-10-16 18:22:04 000
         * winnerName : 137****3456
         * winnerId : 45
         * finalRslt : 10000009
         * cellphone : 13710773456
         * drawcycleId : 787
         * imgUrl : http://122.112.210.44:8080null
         * location : 广东省广州市
         */

        private String announceDate;
        private String purDate;
        private String winnerName;
        private int winnerId;
        private String finalRslt;
        private String cellphone;
        private int drawcycleId;
        private String imgUrl;
        private String location;

        public String getAnnounceDate() {
            return announceDate;
        }

        public void setAnnounceDate(String announceDate) {
            this.announceDate = announceDate;
        }

        public String getPurDate() {
            return purDate;
        }

        public void setPurDate(String purDate) {
            this.purDate = purDate;
        }

        public String getWinnerName() {
            return winnerName;
        }

        public void setWinnerName(String winnerName) {
            this.winnerName = winnerName;
        }

        public int getWinnerId() {
            return winnerId;
        }

        public void setWinnerId(int winnerId) {
            this.winnerId = winnerId;
        }

        public String getFinalRslt() {
            return finalRslt;
        }

        public void setFinalRslt(String finalRslt) {
            this.finalRslt = finalRslt;
        }

        public String getCellphone() {
            return cellphone;
        }

        public void setCellphone(String cellphone) {
            this.cellphone = cellphone;
        }

        public int getDrawcycleId() {
            return drawcycleId;
        }

        public void setDrawcycleId(int drawcycleId) {
            this.drawcycleId = drawcycleId;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }
    }
}
