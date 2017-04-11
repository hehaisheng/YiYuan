package com.shawnway.nav.app.yylg.bean;

import java.util.ArrayList;

/**
 * Created by Eiffel on 2015/12/15.
 */
public class WasteRecordResponse extends ResponseGson<WasteRecordResponse.WasteBody> {

    public class WasteBody {
        public int page;
        public int pageSize;
        public int totalPage;
        public int totalRecord;
        public String amount;
        public ArrayList<WasteRecordBean> balanceHistoryList;
    }

    public static class WasteRecordBean {
        public String createdDate;
        public  String amount;
    }
}
