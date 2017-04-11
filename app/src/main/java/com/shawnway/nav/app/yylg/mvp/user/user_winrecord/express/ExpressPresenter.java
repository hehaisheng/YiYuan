package com.shawnway.nav.app.yylg.mvp.user.user_winrecord.express;

import android.app.Activity;

import com.shawnway.nav.app.yylg.base.BasePresenter;

/**
 * Created by Kevin on 2016/11/30
 */

public class ExpressPresenter extends BasePresenter<ExpressConstract.IExpressView> {
    protected ExpressPresenter(Activity mActivity, ExpressConstract.IExpressView IView) {
        super(mActivity, IView);
    }
}
