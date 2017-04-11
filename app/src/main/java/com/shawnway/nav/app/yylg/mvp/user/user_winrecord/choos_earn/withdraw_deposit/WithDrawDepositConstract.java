package com.shawnway.nav.app.yylg.mvp.user.user_winrecord.choos_earn.withdraw_deposit;

import com.shawnway.nav.app.yylg.implemen.ILoginView;
import com.shawnway.nav.app.yylg.mvp.user.user_winrecord.choos_earn.bean.RechargeAndTransferBean;
import com.shawnway.nav.app.yylg.mvp.user.user_winrecord.choos_earn.bean.ResponseResult;

/**
 * Created by Kevin on 2016/12/5
 */

public class WithDrawDepositConstract {
    public interface IWithDrawDepositView extends ILoginView{
        void onAlipayTranfer(ResponseResult responseResult);
    }

    public interface IWithDrawDepositPresenter {
        void alipayTranfer(RechargeAndTransferBean rechargeAndTransferBean);
    }
}
