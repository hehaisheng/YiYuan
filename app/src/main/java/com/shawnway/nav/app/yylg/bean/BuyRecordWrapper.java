package com.shawnway.nav.app.yylg.bean;

import com.shawnway.nav.app.yylg.implemen.PageListIterator;

import java.util.ArrayList;

/**
 * Created by Eiffel on 2016/3/23.
 */
public class BuyRecordWrapper extends ResponseGson<BuyRecordWrapper.BuyRecordBody>  {
    public class BuyRecordBody implements PageListIterator{
        int page;
        int pageSize;
        int totalPage;
        int totalRecord;

        ArrayList<BuyRecordBean> purchaseDetailsList;//purchaseDetailsList

        @Override
        public int getPage() {
            return page;
        }

        @Override
        public int getTotalPage() {
            return totalPage;
        }

        @Override
        public ArrayList getList() {
            return purchaseDetailsList;
        }
    }

    public static class BuyRecordBean{
        String drawStatus;//状态，总共有四种
        long drawCycleId;//某期商品id
        int customerID;//用户id
        String prodName;//商品名
        int cycle;//第几期
        int puredCnt;//已参与人数
        int totCnt;//总共需要参与的人数
        int leftCnt;//剩余人数
        String thumbnail;//图片的URL
        String finalRslt;//幸运号码
        String winnerName;//中奖者姓名
        String remainTime;//倒计时

        int buyUnit;//最小购买份数

        public int getBuyUnit() {
            return buyUnit;
        }

        public void setBuyUnit(int buyUnit) {
            this.buyUnit = buyUnit;
        }

        public String getDrawStatus(){
            return drawStatus;
        }

        public void setDrawStatus(String drawStatus){
            this.drawStatus = drawStatus;
        }

        public long getDrawCycleId() {
            return drawCycleId;
        }

        public void setDrawCycleId(long drawCycleId) {
            this.drawCycleId = drawCycleId;
        }

        public int getCustomerID() {
            return customerID;
        }

        public void setCustomerID(int customerID) {
            this.customerID = customerID;
        }

        public String getProdName() {
            return prodName;
        }

        public void setProdName(String prodName) {
            this.prodName = prodName;
        }

        public int getCycle() {
            return cycle;
        }

        public void setCycle(int cycle) {
            this.cycle = cycle;
        }

        public int getPuredCnt() {
            return puredCnt;
        }

        public void setPuredCnt(int puredCnt) {
            this.puredCnt = puredCnt;
        }

        public int getTotCnt() {
            return totCnt;
        }

        public void setTotCnt(int totCnt) {
            this.totCnt = totCnt;
        }

        public int getLeftCnt() {
            return leftCnt;
        }

        public void setLeftCnt(int leftCnt) {
            this.leftCnt = leftCnt;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public String getFinalRslt() {
            return finalRslt;
        }

        public void setFinalRslt(String finalRslt) {
            this.finalRslt = finalRslt;
        }

        public String getWinnerName() {
            return winnerName;
        }

        public void setWinnerName(String winnerName) {
            this.winnerName = winnerName;
        }

        public String getRemainTime() {
            return remainTime;
        }

        public void setRemainTime(String remainTime) {
            this.remainTime = remainTime;
        }
    }
}
