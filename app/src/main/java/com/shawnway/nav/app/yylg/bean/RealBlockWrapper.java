package com.shawnway.nav.app.yylg.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/27 0027.
 */
public class RealBlockWrapper extends ResponseGson<RealBlockWrapper.RealBlockBody>{

    public class RealBlockBody{
        int page;
        int pageSize;
        int totalPage;
        ArrayList<RealBlockBean> drawCycleDetailsList;

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

        public ArrayList<RealBlockBean> getDrawCycleDetailsList() {
            return drawCycleDetailsList;
        }

        public void setDrawCycleDetailsList(ArrayList<RealBlockBean> drawCycleDetailsList) {
            this.drawCycleDetailsList = drawCycleDetailsList;
        }
    }

    public class RealBlockBean{
        public int drawCycleID;
        public int productID;
        public int latestDrawCycleID;
        public int latestDrawCycle;
        public int latestProductID;
        public String prodName;
        public int cycle;
        public int price;
        public int buyUnit;
        public int puredCnt;
        public int totCnt;
        public int leftCnt;
        public String thumbnail;
    }
}
