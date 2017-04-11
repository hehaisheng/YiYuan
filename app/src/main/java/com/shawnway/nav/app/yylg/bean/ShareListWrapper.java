package com.shawnway.nav.app.yylg.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Eiffel on 2015/12/6.
 */
public class ShareListWrapper extends ResponseGson<ShareListWrapper.ShareListBody> {


    public static class ShareListBody {

        public int page;
        public int pageSize;
        public int totalPage;
        public int totalRecord;
        public ArrayList<ShareBean> prizeShowItems;
    }

    public static class ShareBean implements Serializable {
        public int winnerId;
        public int prizeShowId;
        public String custName;
        public int cycle;
        public int puredCnt;
        public String luckyNum;
        public String prodName;
        public String subject;
        public String content;
        public int commentCnt;
        public int praiseCnt;
        public ArrayList<String> images;//预设为3，否则shareItemAdapter会数组越界，有余力需优化此处
        public String showDate;
        public String drawDate;
    }
}
