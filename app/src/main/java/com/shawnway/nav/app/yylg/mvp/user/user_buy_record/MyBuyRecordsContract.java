package com.shawnway.nav.app.yylg.mvp.user.user_buy_record;

import com.shawnway.nav.app.yylg.implemen.ILoginView;
import com.shawnway.nav.app.yylg.mvp.user.user_buy_record.bean.MyBuyRecordsBean;

/**
 * Created by Kevin on 2017/1/6
 */

public class MyBuyRecordsContract {
    public interface IMyBuyRecordsView extends ILoginView {

        void addMyBuyRecords(MyBuyRecordsBean myBuyRecordsBean);
    }

    public interface IMyBuyRecordPresenter {

        void getMyBuyRecords(MyBuyRecordsBean condition);
    }
}
