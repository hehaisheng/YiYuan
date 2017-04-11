package com.shawnway.nav.app.yylg.mvp.user.user_winrecord.choos_earn.phone_recharge;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.activity.ChooseEarnActivity;
import com.shawnway.nav.app.yylg.activity.LoginActivity;
import com.shawnway.nav.app.yylg.base.BaseActivity;
import com.shawnway.nav.app.yylg.base.BasePresenter;
import com.shawnway.nav.app.yylg.bean.WinRecBean;
import com.shawnway.nav.app.yylg.mvp.user.user_winrecord.choos_earn.bean.RechargeAndTransferBean;
import com.shawnway.nav.app.yylg.mvp.user.user_winrecord.choos_earn.bean.ResponseResult;
import com.shawnway.nav.app.yylg.tool.Utils;
import com.shawnway.nav.app.yylg.view.TabButton;
import com.thuongnh.zprogresshud.ZProgressHUD;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Kevin on 2016/12/1
 */

public class PhoneRechargeActivity extends BaseActivity<PhoneRechargePresenter> implements PhoneRechargeConstract.IPhoneRechargeView {
    @BindView(R.id.top_back)
    ImageButton topBack;
    @BindView(R.id.top_back_text)
    TextView topBackText;
    @BindView(R.id.top_text_center)
    TextView topTextCenter;
    @BindView(R.id.top_cart)
    TabButton topCart;
    @BindView(R.id.action_edit)
    TextView actionEdit;
    @BindView(R.id.action_gray_common)
    TextView actionGrayCommon;
    @BindView(R.id.toolbar)
    RelativeLayout toolbar;
    @BindView(R.id.phone_recharge_edit_phone)
    EditText phoneRechargeEditPhone;
    @BindView(R.id.phone_recharge_edit_user_name)
    EditText phoneRechargeEditUserName;
    @BindView(R.id.phone_recharge_amountOfcard)
    TextView phoneRechargeAmountOfcard;
    @BindView(R.id.btn_phone_recharge)
    Button btnPhoneRecharge;
    private WinRecBean winRecBean;
    private SweetAlertDialog sweetAlertDialog;
    private ZProgressHUD loading;
    public static final int REQUEST_RECHARGE_CODE=1;

    @Override
    public int getLayout() {
        return R.layout.activity_phone_recharge;
    }

    public static void start(Context context, WinRecBean winRecBean, int request_code) {
        Intent starter = new Intent(context, PhoneRechargeActivity.class);
        starter.putExtra("WinRecBean", winRecBean);
        ((Activity) context).startActivityForResult(starter, request_code);
    }

    @Override
    protected PhoneRechargePresenter getPresenter() {
        return new PhoneRechargePresenter(mContent, this);
    }

    @Override
    protected void initDataAndEvents() {
        initToolBar();
        initView();
    }

    private void initView() {
        winRecBean = (WinRecBean) getIntent().getExtras().get("WinRecBean");


        phoneRechargeEditPhone.setText((String) Utils.getParam(mContent, "phone", ""));
        if (winRecBean.getAmountOfcard() == null) {
            phoneRechargeAmountOfcard.setText("0元");
        } else phoneRechargeAmountOfcard.setText(winRecBean.getAmountOfcard() + "元");
    }

    private void initToolBar() {
        topTextCenter.setText("填写充值信息");
        setVisiableView(toolbar, topBack, topTextCenter);
    }

    @OnClick({R.id.top_back, R.id.btn_phone_recharge})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_back:
                finish();
                break;
            case R.id.btn_phone_recharge:
                //充值
                ensureRecharge();
                break;
        }
    }

    /**
     * 充值
     */
    private void ensureRecharge() {
        sweetAlertDialog = new SweetAlertDialog(mContent, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setConfirmText("确定提交").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(final SweetAlertDialog sweetAlertDialog) {
                if (!TextUtils.equals(phoneRechargeEditPhone.getText().toString(), "") && !TextUtils.equals(phoneRechargeEditUserName.getText().toString(), "")) {
                    RechargeAndTransferBean rechargeAndTransferBean = new RechargeAndTransferBean();
                    rechargeAndTransferBean.setCellphone(phoneRechargeEditPhone.getText().toString());
                    rechargeAndTransferBean.setUsername(phoneRechargeEditUserName.getText().toString());
                    rechargeAndTransferBean.setAmountOfcard(winRecBean.getAmountOfcard());
                    rechargeAndTransferBean.setWinprizeId(new Long(winRecBean.getWinprizeId()));
                    mPresenter.phoneRecharge(rechargeAndTransferBean);
                } else {
                    Toast.makeText(PhoneRechargeActivity.this, "手机号码或姓名不能为空,请重新填写", Toast.LENGTH_SHORT).show();
                }
            }
        }).setCancelText(mContent.getString(R.string.cancel)).
                setContentText("是否提交信息?该操作不能撤销").
                setTitleText("确认").show();
    }


    @Override
    public void showLoading() {
        loading = ZProgressHUD.getInstance(this).setMessage("正在提交..");
        loading.show();
    }

    @Override
    public void dismissLoading() {
        loading.dismiss();
    }

    @Override
    public void errorLoading() {
        loading.dismiss();
        sweetAlertDialog.dismiss();
        Toast.makeText(mContent, "服务器异常,请稍后再试", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPhoneRechargeResult(ResponseResult responseResult) {
        if (TextUtils.equals(responseResult.getHeader().getCode(), "000")) {
            if ((TextUtils.equals(responseResult.getBody().getRechargeResult(), "true"))) {
                Toast.makeText(mContent, "提交成功", Toast.LENGTH_SHORT).show();
                sweetAlertDialog.dismiss();

                setResult(RESULT_OK);
                finish();
            } else {
                sweetAlertDialog.dismiss();
                Toast.makeText(mContent, "已提交信息，请勿重复提交", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(mContent, "服务器异常，提交失败", Toast.LENGTH_SHORT).show();
            sweetAlertDialog.dismiss();
        }
    }

    @Override
    public void starLoginActivity() {
        Toast.makeText(mContent, "登录失效,请重新登录", Toast.LENGTH_SHORT).show();
        sweetAlertDialog.dismiss();
        LoginActivity.getInstance(this, 0, true);
    }

}
