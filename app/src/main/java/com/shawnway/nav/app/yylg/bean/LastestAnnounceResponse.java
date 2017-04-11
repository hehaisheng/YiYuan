package com.shawnway.nav.app.yylg.bean;

import java.util.ArrayList;

/**
 * Created by Eiffel on 2015/12/13.
 */
public class LastestAnnounceResponse  extends  ResponseGson<LastestAnnounceResponse.LastestAnnounceBody>{

    public static class  LastestAnnounceBody{
        public int page;
        public int pageSize;
        public int totalPage;
        public int totalRecord;

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public int getTotalRecord() {
            return totalRecord;
        }

        public void setTotalRecord(int totalRecord) {
            this.totalRecord = totalRecord;
        }

        public ArrayList<GoodDetail> getDrawCycleDetailsList() {
            return drawCycleDetailsList;
        }

        public void setDrawCycleDetailsList(ArrayList<GoodDetail> drawCycleDetailsList) {
            this.drawCycleDetailsList = drawCycleDetailsList;
        }

        public ArrayList<GoodDetail> drawCycleDetailsList;
    }
}
