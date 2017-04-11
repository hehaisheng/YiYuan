package com.shawnway.nav.app.yylg.mvp.user.user_invite.explain;

import com.shawnway.nav.app.yylg.mvp.user.user_invite.explain.bean.CommissionExplain;
import com.shawnway.nav.app.yylg.net.RetrofitManager;
import com.shawnway.nav.app.yylg.tool.ThreadTransformer;

import rx.Observable;

/**
 * Created by Kevin on 2016/11/30
 */

public class ExplainModel {

    /**
     * 获取佣金说明页面的数据
     *
     * @param phone
     */
    public Observable<CommissionExplain> getCommissionExplain(String phone) {
        return RetrofitManager.getInstance()
                .getApi()
                .getCommissionExplain(phone)
                .compose(ThreadTransformer.<CommissionExplain>applySchedulers());
    }
}
