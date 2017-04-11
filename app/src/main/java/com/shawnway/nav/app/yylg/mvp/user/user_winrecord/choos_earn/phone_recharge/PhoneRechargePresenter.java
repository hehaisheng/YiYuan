package com.shawnway.nav.app.yylg.mvp.user.user_winrecord.choos_earn.phone_recharge;

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

public class PhoneRechargePresenter extends BasePresenter<PhoneRechargeConstract.IPhoneRechargeView> implements PhoneRechargeConstract.IPhoneRechargePresenter {

    private final PhoneRechargeModel phoneRechargeModel;

    protected PhoneRechargePresenter(Activity mActivity, PhoneRechargeConstract.IPhoneRechargeView IView) {
        super(mActivity, IView);
        phoneRechargeModel = new PhoneRechargeModel();
    }


    @Override
    public void phoneRecharge(RechargeAndTransferBean rechargeAndTransferBean) {
        IView.showLoading();
        addSubscriber(phoneRechargeModel.phoneRecharge(rechargeAndTransferBean)
                .subscribe(new BaseSubscriber<ResponseResult>() {
                    @Override
                    public void onSuccess(ResponseResult responseResult) {
                        if (TextUtils.equals(responseResult.getHeader().getCode(), Api.CODE_PERMISSON)) {
                            IView.starLoginActivity();
                        } else {
                            IView.onPhoneRechargeResult(responseResult);
                        }
                        IView.dismissLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        IView.errorLoading();
                    }
                })
        );
    }
}
