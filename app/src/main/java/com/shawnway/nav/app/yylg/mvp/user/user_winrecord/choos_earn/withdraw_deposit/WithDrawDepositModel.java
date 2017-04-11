package com.shawnway.nav.app.yylg.mvp.user.user_winrecord.choos_earn.withdraw_deposit;

import com.shawnway.nav.app.yylg.mvp.user.user_winrecord.choos_earn.bean.RechargeAndTransferBean;
import com.shawnway.nav.app.yylg.mvp.user.user_winrecord.choos_earn.bean.ResponseResult;
import com.shawnway.nav.app.yylg.net.RetrofitManager;
import com.shawnway.nav.app.yylg.tool.ThreadTransformer;

import rx.Observable;

/**
 * Created by Kevin on 2016/12/5
 */

public class WithDrawDepositModel {

    /**
     * 淘宝卡的提现
     *
     * @param rechargeAndTransferBean
     * @return
     */
    public Observable<ResponseResult> alipayTransfer(RechargeAndTransferBean rechargeAndTransferBean) {
        return RetrofitManager.getInstance()
                .getApi()
                .alipayTransfer(rechargeAndTransferBean.getCellphone(),
                        rechargeAndTransferBean.getUsername(),
                        rechargeAndTransferBean.getAmountOfcard(),
                        rechargeAndTransferBean.getWinprizeId(),
                        rechargeAndTransferBean.getAlipayAccount())
                .compose(ThreadTransformer.<ResponseResult>applySchedulers());


    }
}
