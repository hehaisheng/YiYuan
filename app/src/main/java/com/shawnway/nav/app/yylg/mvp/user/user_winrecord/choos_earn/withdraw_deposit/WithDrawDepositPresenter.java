package com.shawnway.nav.app.yylg.mvp.user.user_winrecord.choos_earn.withdraw_deposit;

import android.app.Activity;
import android.text.TextUtils;

import com.shawnway.nav.app.yylg.base.BasePresenter;
import com.shawnway.nav.app.yylg.base.BaseSubscriber;
import com.shawnway.nav.app.yylg.mvp.user.user_winrecord.choos_earn.bean.RechargeAndTransferBean;
import com.shawnway.nav.app.yylg.mvp.user.user_winrecord.choos_earn.bean.ResponseResult;
import com.shawnway.nav.app.yylg.net.Api;

/**
 * Created by Kevin on 2016/12/5
 */

public class WithDrawDepositPresenter extends BasePresenter<WithDrawDepositConstract.IWithDrawDepositView> implements WithDrawDepositConstract.IWithDrawDepositPresenter {

    private final WithDrawDepositModel withDrawDepositModel;

    protected WithDrawDepositPresenter(Activity mActivity, WithDrawDepositConstract.IWithDrawDepositView IView) {
        super(mActivity, IView);
        withDrawDepositModel = new WithDrawDepositModel();
    }

    /**
     * 淘宝卡提现
     *
     * @param rechargeAndTransferBean
     */
    @Override
    public void alipayTranfer(RechargeAndTransferBean rechargeAndTransferBean) {
        IView.showLoading();
        addSubscriber(withDrawDepositModel.alipayTransfer(rechargeAndTransferBean)
                .subscribe(new BaseSubscriber<ResponseResult>() {
                               @Override
                               public void onSuccess(ResponseResult responseResult) {
                                   if (TextUtils.equals(responseResult.getHeader().getCode(), Api.CODE_PERMISSON)) {
                                       IView.starLoginActivity();
                                   } else IView.onAlipayTranfer(responseResult);
                                   IView.dismissLoading();
                               }

                               @Override
                               public void onError(Throwable e) {
                                   super.onError(e);
                                   IView.errorLoading();
                               }
                           }
                ));
    }
}
