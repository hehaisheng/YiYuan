package com.shawnway.nav.app.yylg.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.bean.ResponseGson;
import com.shawnway.nav.app.yylg.bean.WinRecBean;
import com.shawnway.nav.app.yylg.fragment.UserFragment;
import com.shawnway.nav.app.yylg.mvp.user.user_winrecord.choos_earn.phone_recharge.PhoneRechargeActivity;
import com.shawnway.nav.app.yylg.mvp.user.user_winrecord.choos_earn.withdraw_deposit.WithDrawDepositActivity;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.tool.CloseActivityUtil;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.Hook;
import com.shawnway.nav.app.yylg.tool.ToastUtil;
import com.shawnway.nav.app.yylg.tool.Utils;
import com.shawnway.nav.app.yylg.tool.VolleyErrorHelper;
import com.shawnway.nav.app.yylg.view.CirclerImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Eiffel on 2015/11/18.
 */
public class ChooseEarnActivity extends MyActivity implements View.OnClickListener {

    private static final String TAG = "ChooseEarnActivity";
    private WinRecBean bean;
    private DecimalFormat df1 = new DecimalFormat("0.00");//用于进行小数的格式化


    public static void getInstance(Activity activity, WinRecBean bean, int reqcode) {
        Intent intent = new Intent(activity, ChooseEarnActivity.class);
        intent.putExtra("bean", bean);
        activity.startActivityForResult(intent, reqcode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CloseActivityUtil.add(ChooseEarnActivity.this);
        setContentView(R.layout.activity_choose_reward_go);
        initData();
        initToolBar();
        initInfo();
        initChooseWay();

    }

    private void initInfo() {
        CirclerImageView img = (CirclerImageView) findViewById(R.id.image);
        img.setImage(bean.getThumbnail());
        ((TextView) findViewById(R.id.luckycode)).setText(getString(R.string.win_record_luckynumber, bean.getFinalResult()));

        TextView name = (TextView) findViewById(R.id.name);
        String tamp = getString(R.string.win_record_title);
        tamp = Utils.insertParams(1, tamp, bean.getCycle() + "");
        name.setText(Utils.insertParams(2, tamp, bean.getProductName()));


        TextView winTime = (TextView) findViewById(R.id.announcetime);
        winTime.setText(bean.getAnnounceDate());
    }

    private void initData() {
        bean = (WinRecBean) getIntent().getExtras().getSerializable("bean");
    }

    private void initChooseWay() {
        if (bean == null) return;
        ArrayList<String> ways = bean.getClaimTypeList();
        for (int i = 0; i < ways.size(); i++) {
            View wrapper = null;
            View btn = null;
            switch (ways.get(i)) {
                case WinRecBean.BALANCE:
                    wrapper = findViewById(R.id.win_chooser_to_balance_wrapper);
                    btn = findViewById(R.id.bt_to_balance);
                    btn.setTag(WinRecBean.BALANCE);
                    break;
                case WinRecBean.DONATE:
                    wrapper = findViewById(R.id.win_chooser_to_friend_wrapper);
                    btn = findViewById(R.id.bt_to_friend);
                    btn.setTag(WinRecBean.DONATE);
                    break;
                case WinRecBean.POST:
                    wrapper = findViewById(R.id.win_chooser_to_getreal_wrapper);
                    btn = findViewById(R.id.bt_to_getreal);
                    btn.setTag(WinRecBean.POST);
                    break;
                case WinRecBean.SELF_PICKEDUP:
                    wrapper = findViewById(R.id.win_chooser_to_realshop_wrapper);
                    btn = findViewById(R.id.bt_to_realshop);
                    btn.setTag(WinRecBean.SELF_PICKEDUP);
                    break;
                case WinRecBean.PHONE_RECHARGE:
                    wrapper = findViewById(R.id.win_chooser_to_phone_recharge_wrapper);
                    btn = findViewById(R.id.bt_phone_recharnge);
                    btn.setTag(WinRecBean.PHONE_RECHARGE);
                    break;
                case WinRecBean.WITHDRAW_DEPOSIT:
                    wrapper = findViewById(R.id.win_chooser_to_withdraw_deposit_wrapper);
                    btn = findViewById(R.id.bt_to_withdraw_deposit);
                    btn.setTag(WinRecBean.WITHDRAW_DEPOSIT);
                    break;
                default:
            }
            if (btn != null) {
                wrapper.setVisibility(View.VISIBLE);
                btn.setOnClickListener(this);
            }

        }
    }


    protected void initToolBar() {

        TextView centerText = (TextView) findViewById(R.id.top_text_center);
        centerText.setText(getResources().getString(R.string.title_activity_choose_earn));
        centerText.setVisibility(View.VISIBLE);
        ImageButton backButton = (ImageButton) findViewById(R.id.top_back);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(this);

    }


    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.top_back:
                finish();
                break;
            case R.id.bt_to_balance:
                choosoBalance();
                break;
            case R.id.bt_to_getreal:
                AddressEditActivity.getInstance(ChooseEarnActivity.this, bean, AddressEditActivity.REQUEST_CANSELECTI_SELECT);
                break;
            case R.id.bt_to_realshop:
                SelfPickActivity.getInstance(this);
                break;
            case R.id.bt_to_friend:
                Hook.donate(bean, this, new Response.Listener<ResponseGson>() {
                    @Override
                    public void onResponse(ResponseGson responseGson) {
                        if (!Utils.handleResponseError(ChooseEarnActivity.this, responseGson, getString(R.string.donate_failed))) {
                            ToastUtil.showShort(ChooseEarnActivity.this, getString(R.string.donate_success));
                            setResult(RESULT_OK);
                            finish();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        ToastUtil.showShort(ChooseEarnActivity.this, getString(R.string.donate_failed));

                    }
                });
                break;
            case R.id.bt_phone_recharnge:
                PhoneRechargeActivity.start(this, (WinRecBean) getIntent().getExtras().get("bean"), PhoneRechargeActivity.REQUEST_RECHARGE_CODE);
                setResult(RESULT_OK);

                break;
            case R.id.bt_to_withdraw_deposit:
                WithDrawDepositActivity.start(this, (WinRecBean) getIntent().getExtras().get("bean"), WithDrawDepositActivity.REQUEST_WITHDRAW_CODE);
                setResult(RESULT_OK);

                break;

        }
    }

    private void choose(int winprizeid, String claimType, Response.Listener<ResponseGson> listener) {
        try {
            JSONObject jo = new JSONObject();
            jo.put("winprizeId", winprizeid);
            jo.put("claimType", claimType);

            VolleyTool.getInstance(this).sendGsonPostRequest(this, ResponseGson.class, Constants.CHOOSE_EARN_URL,
                    jo.toString(), listener, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            ToastUtil.showNetError(ChooseEarnActivity.this);
                        }
                    });
        } catch (JSONException e) {
            Log.e(TAG, e.getStackTrace().toString());
        }
    }

    private void choosoBalance() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ChooseEarnActivity.this);
        //加载dialog的View
        View view = View.inflate(ChooseEarnActivity.this, R.layout.dialog_choose_balance, null);
        //fbc一下dialog中的title、余额以及确定取消按钮
        TextView tvTitle = (TextView) view.findViewById(R.id.dialog_choose_balance_title);
        TextView tvBalance = (TextView) view.findViewById(R.id.dialog_choose_balance_money);
        Button cancleBtn = (Button) view.findViewById(R.id.choose_balance_negativeBtn);
        Button okBtn = (Button) view.findViewById(R.id.choose_balance_positiveBtn);
        //将View塞到dialog中去
        builder.setView(view);
        //将dialog显示出来,顺带着获取dialog对象
        final AlertDialog dialog = builder.show();
        //对dialog中fbc出来的东东进行你想要的相关操作
        tvBalance.setText(df1.format(bean.getPurPrice()));//已完成TODO:后台还缺少了一个成本价，要用到 dlf
        cancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                return;
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject jo = new JSONObject();
                    jo.put("winprizeId", bean.getWinprizeId());
                    VolleyTool.getInstance(ChooseEarnActivity.this).sendGsonPostRequest(ChooseEarnActivity.this, ResponseGson.class, Constants.TRANSFER_BALANCE_URL, jo.toString(), new Response.Listener<ResponseGson>() {
                        @Override
                        public void onResponse(ResponseGson response) {
                            if (!Utils.handleResponseError(false, (Activity) ChooseEarnActivity.this, response)) {
                                ToastUtil.showShort(ChooseEarnActivity.this, ChooseEarnActivity.this.getString(R.string.chooseprize_result_tran_balance_success));
                                dialog.dismiss();
                                Utils.setParam(ChooseEarnActivity.this, "winRecordDel", bean.getWinprizeId());
                                setResult(Activity.RESULT_OK);
                                finish();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            ToastUtil.showShort(ChooseEarnActivity.this, VolleyErrorHelper.getMessage(volleyError, ChooseEarnActivity.this));

                        }
                    });
                } catch (JSONException e) {
                    Log.e(TAG, "need winprizeId");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != Constants.ACTION_REQUEST_LOGIN && resultCode == RESULT_OK) {
            finish();
        } else if (requestCode == Constants.ACTION_REQUEST_LOGIN && resultCode == RESULT_OK) {

        } else if (requestCode == PhoneRechargeActivity.REQUEST_RECHARGE_CODE && resultCode == RESULT_OK) {
            //充值完
            setResult(RESULT_OK);
            finish();
        } else if (requestCode == WithDrawDepositActivity.REQUEST_WITHDRAW_CODE && resultCode == RESULT_OK) {
            //虚拟充值完
            setResult(RESULT_OK);
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

//    @Override
//    public Resources getResources() {
//        Resources res = super.getResources();
//        Configuration config = new Configuration();
//        config.setToDefaults();
//        res.updateConfiguration(config, res.getDisplayMetrics());
//        return res;
//    }
}
