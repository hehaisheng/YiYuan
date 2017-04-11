package com.shawnway.nav.app.yylg.mvp.user.user_buy_record;

import android.app.Activity;
import android.text.TextUtils;

import com.shawnway.nav.app.yylg.base.BasePresenter;
import com.shawnway.nav.app.yylg.base.BaseSubscriber;
import com.shawnway.nav.app.yylg.mvp.user.user_buy_record.bean.MyBuyRecordsBean;
import com.shawnway.nav.app.yylg.net.Api;
import com.shawnway.nav.app.yylg.tool.ToastUtil;

import java.util.ArrayList;

/**
 * Created by Kevin on 2017/1/6
 */

public class MyBuyRecordsPresenter extends BasePresenter<MyBuyRecordsContract.IMyBuyRecordsView> implements MyBuyRecordsContract.IMyBuyRecordPresenter {

    private final MyBuyRecordsModel myBuyRecordsModel;

    protected MyBuyRecordsPresenter(Activity mActivity, MyBuyRecordsContract.IMyBuyRecordsView IView) {
        super(mActivity, IView);
        myBuyRecordsModel = new MyBuyRecordsModel();
    }

    /**
     * 获取购买记录
     *
     * @param condition 分页条件
     */
    @Override
    public void getMyBuyRecords(MyBuyRecordsBean condition) {
        addSubscriber(myBuyRecordsModel.getMyBuyRecords(condition)
                .subscribe(new BaseSubscriber<MyBuyRecordsBean>() {
                    @Override
                    public void onSuccess(MyBuyRecordsBean myBuyRecordsBean) {
                        if (TextUtils.equals(Api.CODE_PERMISSON, myBuyRecordsBean.getHeader().getCode())) {
                            IView.starLoginActivity();
                            return;
                        }
                        if (myBuyRecordsBean.getBody().getPurchaseDetailsList() == null) {
                            myBuyRecordsBean.getBody().setPurchaseDetailsList(new ArrayList<MyBuyRecordsBean.BodyBean.PurchaseDetailsListBean>());
                        }
                        IView.addMyBuyRecords(myBuyRecordsBean);
                        IView.dismissLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        IView.errorLoading();
                    }
                }));
    }

}
