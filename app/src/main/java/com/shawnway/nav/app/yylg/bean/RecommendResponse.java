package com.shawnway.nav.app.yylg.bean;

import com.shawnway.nav.app.yylg.view.MySlideShowView;

import java.util.ArrayList;

/**
 * Created by Eiffel on 2015/12/26.
 */
public class RecommendResponse extends ResponseGson<RecommendResponse.RecommendBody> {

    public class RecommendBody {
      public   ArrayList<RecommendItem> recommandList;
    }

    public static class RecommendItem {
        public long drawCycleID;
        public long latestDrawCycleID;
        public long latestProductID;
        public String thumbnail;
        public long cycle;
        public double price;
        public int buyUnit;
        public int puredCnt;
        public int totCnt;
        public int leftCnt;

        public MySlideShowView.SlideShowBean toSliderBean() {
            return new MySlideShowView.SlideShowBean(thumbnail);
        }
    }
}
