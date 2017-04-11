package com.shawnway.nav.app.yylg.mvp.user.user_invite.explain;

import android.app.Activity;
import android.text.TextUtils;

import com.shawnway.nav.app.yylg.base.BasePresenter;
import com.shawnway.nav.app.yylg.base.BaseSubscriber;
import com.shawnway.nav.app.yylg.mvp.user.user_invite.explain.bean.CommissionExplain;
import com.shawnway.nav.app.yylg.net.Api;
import com.shawnway.nav.app.yylg.tool.Utils;

/**
 * Created by Kevin on 2016/11/30
 */

public class ExplainPresenter extends BasePresenter<ExplainConstract.IExplainView> implements ExplainConstract.IExplainPresenter {

    private final ExplainModel explainModel;

    protected ExplainPresenter(Activity mActivity, ExplainConstract.IExplainView IView) {
        super(mActivity, IView);
        explainModel = new ExplainModel();
    }

    /**
     * 获取佣金说明页面的数据
     *
     * @param phone
     */
    @Override
    public void getCommissionExplain(String phone) {
        addSubscriber(explainModel.getCommissionExplain(phone)
                .subscribe(new BaseSubscriber<CommissionExplain>() {
                    @Override
                    public void onSuccess(CommissionExplain commissionExplain) {
                        if (!TextUtils.equals(commissionExplain.getHeader().getCode(), Api.CODE_PERMISSON)) {
                            IView.onAddCommissionExplain(commissionExplain);
                        } else
                            IView.starLoginActivity();
                    }
                }));

    }
}
