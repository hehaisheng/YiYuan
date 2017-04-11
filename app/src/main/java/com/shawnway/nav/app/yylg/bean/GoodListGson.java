package com.shawnway.nav.app.yylg.bean;

import com.shawnway.nav.app.yylg.implemen.PageListIterator;

import java.util.ArrayList;

/**
 * Created by Eiffel on 2015/11/12.
 */
public class GoodListGson implements PageListIterator{
    private int page;
    private  int totalPage;
    private  int totalRecord;
    private  int pageSize;
    private  boolean hasNext;
    private  ArrayList<GoodDetail> drawCycleDetailsList;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalPage() {
        return totalPage;
    }

    @Override
    public ArrayList getList() {
        return drawCycleDetailsList;
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

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public ArrayList<GoodDetail> getDrawCycleDetailsList() {
        if (drawCycleDetailsList==null){
            drawCycleDetailsList=new ArrayList<>();
        }
        return drawCycleDetailsList;
    }

    public void setDrawCycleDetailsList(ArrayList<GoodDetail> drawCycleDetailsList) {
        this.drawCycleDetailsList = drawCycleDetailsList;
    }
}
