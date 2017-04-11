package com.shawnway.nav.app.yylg.bean;

import java.util.ArrayList;

/**
 * Created by Eiffel on 2015/12/9.
 */
public class CommentsResponseWrapper extends ResponseGson<CommentsResponseWrapper.CommentsBody> {

    public static class CommentsBody {
        public int page;
        public int pageSize;
        public int totalPage;
        public int totalRecord;
        public ArrayList<CommentBean> commentList;
    }


    public static class CommentBean {

        public String custName;
        public  String commentDate;
        public  String content;

        public CommentBean(String name,String date,String conten){
            custName=name;
            commentDate=date;
            content=conten;
        };
    }
}
