package com.shawnway.nav.app.yylg.mvp.user.user_winrecord.express;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.base.BaseActivity;
import com.shawnway.nav.app.yylg.bean.ResponseGson;
import com.shawnway.nav.app.yylg.bean.WinRecBean;
import com.shawnway.nav.app.yylg.mvp.user.user_invite.explain.ExplainConstract;
import com.shawnway.nav.app.yylg.mvp.user.user_invite.explain.ExplainPresenter;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.ToastUtil;
import com.shawnway.nav.app.yylg.tool.Utils;
import com.shawnway.nav.app.yylg.tool.VolleyErrorHelper;
import com.shawnway.nav.app.yylg.view.TabButton;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Kevin on 2016/11/30
 */

public class ExpressActivity extends BaseActivity<ExpressPresenter> implements ExpressConstract.IExpressView {
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
    @BindView(R.id.express_date)
    TextView expressDate;
    @BindView(R.id.express_orderId)
    TextView expressOrderId;
    @BindView(R.id.express_company)
    TextView expressCompany;
    @BindView(R.id.express_customer_service)
    TextView expressCustomerService;
    @BindView(R.id.express_recevier)
    TextView expressRecevier;
    @BindView(R.id.express_recevier_address)
    TextView expressRecevierAddress;
    @BindView(R.id.express_ensure_receipt)
    Button expressEnsureReceipt;
    @BindView(R.id.layout_mwin_express)
    RelativeLayout layoutMwinExpress;
    @BindView(R.id.layout_not_express_info)
    RelativeLayout layoutNoExpressInfo;
    private WinRecBean winRecBean;

    @Override
    public int getLayout() {
        return R.layout.activity_mwin_express;
    }

    public static void start(Activity context, WinRecBean winRecBean, int reqCode) {
        Intent starter = new Intent(context, ExpressActivity.class);
        starter.putExtra("WinRecBean", winRecBean);
        context.startActivityForResult(starter, reqCode);
    }

    @Override
    protected void initDataAndEvents() {
        initToolbar();
        initView();
        initData();
    }

    private void initData() {
        expressDate.setText(winRecBean.getUpdateDate());
        expressOrderId.setText(winRecBean.getTrackingNo());
        expressCompany.setText(winRecBean.getExpressCompany());
        expressCustomerService.setText(winRecBean.getExpressCompanyPhone());
        expressRecevier.setText(winRecBean.getLogRecvr());
        expressRecevierAddress.setText(winRecBean.getLogAddr());
    }

    private void initView() {
        winRecBean = (WinRecBean) getIntent().getExtras().get("WinRecBean");
        if (!TextUtils.isEmpty(winRecBean.getTrackingNo())
                && !TextUtils.equals(winRecBean.getTrackingNo(), "")) {
            setVisiableView(layoutMwinExpress);
            layoutNoExpressInfo.setVisibility(View.GONE);
        } else {
            setVisiableView(layoutNoExpressInfo);
            layoutMwinExpress.setVisibility(View.GONE);
        }
    }

    private void initToolbar() {
        topTextCenter.setText("物流信息");
        setVisiableView(toolbar, topBack, topTextCenter);
    }

    @OnClick({R.id.top_back, R.id.express_ensure_receipt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_back:
                finish();
                break;
            case R.id.express_ensure_receipt:
                WinRecBean winRecBean = (WinRecBean) getIntent().getExtras().get("WinRecBean");
                ensureReceived(winRecBean);
                break;
        }
    }


    /**
     * 确认收货
     *
     * @param bean
     */
    private void ensureReceived(final WinRecBean bean) {
        SweetAlertDialog dialog = new SweetAlertDialog(mContent, SweetAlertDialog.WARNING_TYPE);
        dialog.setConfirmText(mContent.getString(R.string.confirm_receive)).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(final SweetAlertDialog sweetAlertDialog) {

                try {
                    JSONObject jo = new JSONObject();
                    jo.put("winprizeId", bean.getWinprizeId());

                    VolleyTool.getInstance(mContent).sendGsonPostRequest(mContent, ResponseGson.class, Constants.ENSURE_REC_URL, jo.toString(), new Response.Listener<ResponseGson>() {
                        @Override
                        public void onResponse(ResponseGson responseGson) {
                            if (!Utils.handleResponseError(mContent, responseGson)) {
                                ToastUtil.showShort(mContent, mContent.getString(R.string.confirm_receive_success));
                                sweetAlertDialog.dismiss();
                                setResult(RESULT_OK);
                                finish();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            ToastUtil.showShort(mContent, VolleyErrorHelper.getMessage(volleyError, mContent));

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).setCancelText(mContent.getString(R.string.cancel)).
                setContentText(mContent.getString(R.string.confirm_receive_content)).
                setTitleText(mContent.getString(R.string.confirm_receive_title)).show();
    }
}
