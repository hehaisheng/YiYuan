package com.shawnway.nav.app.yylg.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Eiffel on 2015/11/19.
 */

public class MyBuyRecordWrapper extends ResponseGson<MyBuyRecordWrapper.MyBuyRecordBody> {

    public static class MyBuyRecordBody {
        public int page;
        public int pageSize;
        public int totalPage;
        public int totalRecord;
        public ArrayList<MyBuyRecordBean> purchaseDetailsList;
    }

    public static class MyBuyRecordBean implements Serializable {
        public static final String STA_OPNE = "OPEN";
        public static final String STA_ANNOUNCED = "ANNOUNCED";
        public static final String STA_CALCULATING = "CALCULATING";


        public int drawCycleId;
        public String prodName;
        public int cycle;
        public String drawStatus;
        public int puredCnt;
        public int totCnt;
        public int leftCnt;
        public String thumbnail;

        public String winnerName;
        public String announceDate;
        public String finalRslt;

        public int buyUnit;

        public ArrayList<DrawDetail> drawDetails;
        public int totalPurch;


        public static class DrawDetail implements Serializable {

            public String purchSummary;
            public String drawNumbers;

        }
    }
}