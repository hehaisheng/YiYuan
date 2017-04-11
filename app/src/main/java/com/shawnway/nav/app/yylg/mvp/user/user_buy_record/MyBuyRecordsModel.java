package com.shawnway.nav.app.yylg.mvp.user.user_buy_record;

import com.shawnway.nav.app.yylg.mvp.user.user_buy_record.bean.MyBuyRecordsBean;
import com.shawnway.nav.app.yylg.net.RetrofitManager;
import com.shawnway.nav.app.yylg.tool.ThreadTransformer;

import rx.Observable;

/**
 * Created by Kevin on 2017/1/6
 */

public class MyBuyRecordsModel {

    /**
     * 获取用户购买记录
     *
     * @param condition 查询条件
     */
    public Observable<MyBuyRecordsBean> getMyBuyRecords(MyBuyRecordsBean condition) {
        return RetrofitManager.getInstance()
                .getApi()
                .getMyBuyRecords(condition.getPageNo(),
                        condition.getPageSize(),
                        condition.getDrawStatus())
                .compose(ThreadTransformer.<MyBuyRecordsBean>applySchedulers());

    }
}
