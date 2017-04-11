package com.shawnway.nav.app.yylg.bean;

import java.util.ArrayList;

/**
 * Created by Eiffel on 2015/12/15.
 */
public class ChargeRecordResponse extends ResponseGson<ChargeRecordResponse.ChargeBody> {

    public class ChargeBody {
        public int page;
        public int pageSize;
        public int totalPage;
        public int totalRecord;
        public String amount;
        public ArrayList<ChargeRecordBean> balanceHistoryList;
    }

    public static class ChargeRecordBean {
        public String createdDate;
        public String amount;
        public String description;
    }
}
