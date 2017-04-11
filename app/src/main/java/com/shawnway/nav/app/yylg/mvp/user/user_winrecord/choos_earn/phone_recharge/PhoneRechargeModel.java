package com.shawnway.nav.app.yylg.mvp.user.user_winrecord.choos_earn.phone_recharge;

import com.shawnway.nav.app.yylg.mvp.user.user_winrecord.choos_earn.bean.RechargeAndTransferBean;
import com.shawnway.nav.app.yylg.mvp.user.user_winrecord.choos_earn.bean.ResponseResult;
import com.shawnway.nav.app.yylg.net.RetrofitManager;
import com.shawnway.nav.app.yylg.tool.ThreadTransformer;

import rx.Observable;

/**
 * Created by Kevin on 2016/12/5
 */

public class PhoneRechargeModel {

    /**
     * 充值话费卡
     *
     * @param rechargeAndTransferBean
     */
    public Observable<ResponseResult> phoneRecharge(RechargeAndTransferBean rechargeAndTransferBean) {
        return RetrofitManager.getInstance()
                .getApi()
                .telePhoneRecharge(rechargeAndTransferBean.getCellphone(),
                        rechargeAndTransferBean.getUsername(),
                        rechargeAndTransferBean.getAmountOfcard(),
                        rechargeAndTransferBean.getWinprizeId())
                .compose(ThreadTransformer.<ResponseResult>applySchedulers())
                ;
    }

}
