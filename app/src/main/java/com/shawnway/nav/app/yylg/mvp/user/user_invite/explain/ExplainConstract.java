package com.shawnway.nav.app.yylg.mvp.user.user_invite.explain;

import com.shawnway.nav.app.yylg.implemen.ILoginView;
import com.shawnway.nav.app.yylg.mvp.user.user_invite.explain.bean.CommissionExplain;

/**
 * Created by Kevin on 2016/11/30
 */

public class ExplainConstract {
    public interface IExplainView extends ILoginView{
        void onAddCommissionExplain(CommissionExplain commissionExplain);
    }

    public interface IExplainPresenter {

        void getCommissionExplain(String phone);
    }
}
