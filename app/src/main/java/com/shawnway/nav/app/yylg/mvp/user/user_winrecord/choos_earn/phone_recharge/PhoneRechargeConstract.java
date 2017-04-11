package com.shawnway.nav.app.yylg.mvp.user.user_winrecord.choos_earn.phone_recharge;

import com.shawnway.nav.app.yylg.implemen.ILoginView;
import com.shawnway.nav.app.yylg.mvp.user.user_winrecord.choos_earn.bean.RechargeAndTransferBean;
import com.shawnway.nav.app.yylg.mvp.user.user_winrecord.choos_earn.bean.ResponseResult;

/**
 * Created by Kevin on 2016/12/5
 */

public class PhoneRechargeConstract {
    public interface IPhoneRechargeView extends ILoginView {
        void onPhoneRechargeResult(ResponseResult responseResult);
    }

    public interface IPhoneRechargePresenter {
        void phoneRecharge(RechargeAndTransferBean rechargeAndTransferBean);
    }
}
