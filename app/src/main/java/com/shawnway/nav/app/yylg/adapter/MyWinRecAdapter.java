package com.shawnway.nav.app.yylg.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baoyz.widget.PullRefreshLayout;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.activity.AddShareActivity;
import com.shawnway.nav.app.yylg.activity.AddressEditActivity;
import com.shawnway.nav.app.yylg.activity.ChooseEarnActivity;
import com.shawnway.nav.app.yylg.activity.DetailActivity;
import com.shawnway.nav.app.yylg.activity.SelfPickActivity;
import com.shawnway.nav.app.yylg.bean.ResponseGson;
import com.shawnway.nav.app.yylg.bean.WinRecBean;
import com.shawnway.nav.app.yylg.mvp.user.user_winrecord.choos_earn.phone_recharge.PhoneRechargeActivity;
import com.shawnway.nav.app.yylg.mvp.user.user_winrecord.choos_earn.withdraw_deposit.WithDrawDepositActivity;
import com.shawnway.nav.app.yylg.mvp.user.user_winrecord.express.ExpressActivity;
import com.shawnway.nav.app.yylg.net.VolleyTool;
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
import java.util.List;

/**
 * Created by Eiffel on 2015/11/19.
 */
public class MyWinRecAdapter extends MCyclerAdapter<WinRecBean> implements View.OnClickListener, MCyclerAdapter.CycleItemClilkListener {
    private static final int TYPE_ITEM = 0;
    private static final String TAG = "MyWinRecAdapter";
    private final int REQUEST_ADD_SHARE = 12;
    public static final int REQUEST_CHOOSE_EARN = 11;
    private PullRefreshLayout.OnRefreshListener mOnRefreshListener;
    private AlertDialog dlg;
    private Context mContext;
    private DecimalFormat df1 = new DecimalFormat("0.00");//用于进行小数的格式化

    //必须是Activity
    public MyWinRecAdapter(Activity activity, PullRefreshLayout.OnRefreshListener listener) {
        this(new ArrayList<WinRecBean>(), activity, listener);
    }

    public MyWinRecAdapter(List<WinRecBean> list, Activity activity, PullRefreshLayout.OnRefreshListener listener) {
        super(list, activity);
        mOnRefreshListener = listener;
        mContext = activity;
    }

    @Override
    public int getDisplayType(int postion) {
        return TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateDisplayHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_item_mwin_record, parent, false);
        setListener(view);
        return new ItemHolder(view, this);
    }

    private void setListener(View view) {
        view.findViewById(R.id.button).setOnClickListener(this);
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, int position) {
        switch (getDisplayType(position)) {
            case TYPE_ITEM:
                final WinRecBean bean = list.get(position);
                Log.d(TAG, "中奖奖品：" + bean.getProductName());
                Log.d(TAG, "中奖id：" + bean.getWinprizeId());
                ItemHolder itemHolder = (ItemHolder) holder;
                itemHolder.setIsRecyclable(false);//取消item复用

                Log.d(TAG, "on bind:" + bean.getClaimStatus());
                if (bean.getBuyUnit() == 10)//个人中心的我的中奖纪录少了buyUnit字段（已完成TODO）
                    itemHolder.tenIcon.setVisibility(View.VISIBLE);

                if (bean.getButtontext(context) == null)
                    itemHolder.button.setVisibility(View.GONE);
                else {
                    if (bean.getButtontext(context).equals("不能晒单")) {
                        itemHolder.button.setBackgroundColor(context.getResources().getColor(R.color.lightskyblue));
                    }
                    itemHolder.button.setText(bean.getButtontext(context));
                    itemHolder.button.setVisibility(View.VISIBLE);
                }

                if (bean.getOriginalOwner() != null) {
                    itemHolder.isGrantGoods.setVisibility(View.VISIBLE);
                    itemHolder.towho.setVisibility(View.VISIBLE);
                    itemHolder.towho.setText("赠与者：" + bean.getOriginalOwner());
                }

                itemHolder.image.setImage(bean.getThumbnail());
                itemHolder.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DetailActivity.getInstance(mContext, bean.getDrawcycleId());
                    }
                });

                itemHolder.status.setText(bean.getStatuetext(context));
                itemHolder.annountime.setText(bean.getAnnounceDate());
                itemHolder.luckynum.setText(bean.getFinalResult());

                String tamp = context.getString(R.string.win_record_title);
                tamp = Utils.insertParams(1, tamp, bean.getCycle() + "");
                itemHolder.name.setText(Utils.insertParams(2, tamp, bean.getProductName()));

                holder.itemView.setTag(bean);
                itemHolder.button.setTag(bean);
                break;
        }
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager(Context context) {
        return new LinearLayoutManager(context);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                try {
                    final WinRecBean bean = (WinRecBean) v.getTag();
                    switch (bean.getType(context)) {
                        case WinRecBean.TYPE_CHOOSE:
                            ChooseEarnActivity.getInstance((Activity) context, bean, REQUEST_CHOOSE_EARN);
                            mOnRefreshListener.onRefresh();
                            break;
                        case WinRecBean.TYPE_SHARE:// 还需要根据后台的字段判断是否可以晒单（已完成TODO）
                            AddShareActivity.getInstance((Activity) context, (WinRecBean) v.getTag(), REQUEST_ADD_SHARE);
                            break;
                        case WinRecBean.TYPE_ENSURE://查看物流
                            ExpressActivity.start((Activity) context, bean, REQUEST_CHOOSE_EARN);
                            //ensureReceived(bean);
                            break;
                        case WinRecBean.TYPE_SHARED:
                            break;
                        case WinRecBean.TYPE_CHOOSE_ADDRESS:
                            AddressEditActivity.getInstance((Activity) context, bean, AddressEditActivity.REQUEST_COMMON_SELECT);
                            break;
                        case WinRecBean.TYPE_PICKUP:
                            SelfPickActivity.getInstance(context);
                            break;
                        case WinRecBean.TYPE_DONATE:
                            Hook.donate(bean, context, new Response.Listener<ResponseGson>() {
                                @Override
                                public void onResponse(ResponseGson responseGson) {
                                    if (!Utils.handleResponseError(context, responseGson, R.string.donate_failed + ":")) {
                                        ToastUtil.showShort(context, context.getString(R.string.donate_success));
                                        mOnRefreshListener.onRefresh();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
//                                    ToastUtil.showShort(context, VolleyErrorHelper.getMessage(volleyError, context));
                                    ToastUtil.showShort(context, context.getString(R.string.donate_failed));
                                }
                            });
                            break;
                        case WinRecBean.TYPE_BALANCE:
                            choosoBalance(bean);
                            break;
                        case WinRecBean.TYPE_PHONE_RECHARGE:
                            PhoneRechargeActivity.start(context, bean, PhoneRechargeActivity.REQUEST_RECHARGE_CODE);
                            break;
                        case WinRecBean.TYPE_WITHDRAW_DEPOSIT:
                            WithDrawDepositActivity.start(context, bean, WithDrawDepositActivity.REQUEST_WITHDRAW_CODE);
                            break;
                    }
                } catch (ClassCastException e) {
                    Log.d(TAG, "the button has set a wrong tag:" + e.getStackTrace().toString());
                }
        }
    }

    private void choosoBalance(final WinRecBean bean) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        //加载dialog的View
        View view = View.inflate(mContext, R.layout.dialog_choose_balance, null);
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
                    VolleyTool.getInstance(context).sendGsonPostRequest(context, ResponseGson.class, Constants.TRANSFER_BALANCE_URL, jo.toString(), new Response.Listener<ResponseGson>() {
                        @Override
                        public void onResponse(ResponseGson response) {
                            if (!Utils.handleResponseError(false, (Activity) context, response)) {
                                ToastUtil.showShort(context, context.getString(R.string.chooseprize_result_tran_balance_success));
                                dialog.dismiss();
                                Utils.setParam(mContext, "winRecordDel", bean.getWinprizeId());
                                ((Activity) mContext).finish();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            ToastUtil.showShort(context, VolleyErrorHelper.getMessage(volleyError, context));

                        }
                    });
                } catch (JSONException e) {
                    Log.e(TAG, "need winprizeId");
                }
            }
        });

    }


    @Override
    public void onCycleItemClick(View view, int position) {

    }

    class ItemHolder extends BaseHolder {

        private CirclerImageView image;
        private final Button button;
        private TextView name;
        private TextView luckynum;
        private TextView status;
        private TextView annountime;
        private TextView isGrantGoods;
        private TextView towho;
        private ImageView tenIcon;

        public ItemHolder(View itemView, CycleItemClilkListener listener) {
            super(itemView, listener);
            image = (CirclerImageView) itemView.findViewById(R.id.image);
            button = (Button) itemView.findViewById(R.id.button);
            name = (TextView) itemView.findViewById(R.id.name);
            luckynum = (TextView) itemView.findViewById(R.id.luckycode);
            status = (TextView) itemView.findViewById(R.id.statue);
            annountime = (TextView) itemView.findViewById(R.id.announcetime);
            towho = (TextView) itemView.findViewById(R.id.towho);
            isGrantGoods = (TextView) itemView.findViewById(R.id.isgrantgoods);
            tenIcon = (ImageView) itemView.findViewById(R.id.good_ten_label);
        }
    }
}
