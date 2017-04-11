package com.shawnway.nav.app.yylg.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baoyz.widget.PullRefreshLayout;
import com.google.gson.Gson;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.activity.MainActivity;
import com.shawnway.nav.app.yylg.activity.OrderActivity;
import com.shawnway.nav.app.yylg.adapter.CarItemAdapter;
import com.shawnway.nav.app.yylg.app.MyApplication;
import com.shawnway.nav.app.yylg.bean.CarItemBean;
import com.shawnway.nav.app.yylg.bean.GoodBean;
import com.shawnway.nav.app.yylg.bean.GoodDetail;
import com.shawnway.nav.app.yylg.bean.GoodListGsonResponse;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.tool.AppUtils;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.ToastUtil;
import com.shawnway.nav.app.yylg.tool.Utils;
import com.shawnway.nav.app.yylg.tool.VHolder;
import com.shawnway.nav.app.yylg.view.MyRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CarFragment extends MyFragment implements OnClickListener, PullRefreshLayout.OnRefreshListener {
    private static final String TAG = "CarFragment";
    public static final int REQUEST_PAY_INCART = 502;
    private List<CarItemBean> mData=new ArrayList<>();
    private MyRecyclerView recyclerView;
    private CarItemAdapter mAdapter;
    private boolean editing;
    private View notEmptyView;
    private Handler mHandler;
    private static final int NOTIFY_ITEM_CHECKEC_STATE_CHANGE = 0;
    private static final int NOTIFY_ITEM_NUMBER_CHANGE = 1;
    private BroadcastReceiver receiver;
    private PullRefreshLayout mPullToRefreshLayout;

    private boolean lock = true;
    public void lock(){lock = true;}
    public void unLock(){lock = false;}


    private  class MyHandler extends Handler {

//        private final WeakReference<CarFragment> mFragment;
        CarFragment carFragment;

        public MyHandler(CarFragment fragment) {
            //因为这里是弱引用，导致内存回收时，会清除保存的数据
//            mFragment = new WeakReference<CarFragment>(fragment);
            carFragment=fragment;
        }

        public void handleMessage(Message msg) {
            if (msg.what == NOTIFY_ITEM_CHECKEC_STATE_CHANGE) {
//                mFragment.get().editing = (Boolean) msg.obj;
                carFragment.editing=(Boolean) msg.obj;
//                if (!mFragment.get().editing)
//                    mFragment.get().change2Editing(mFragment.get().editing);
//                mFragment.get().notifySelectedChange();
                if(!carFragment.editing)
                {
                    carFragment.change2Editing(carFragment.editing);
                }
                carFragment.notifySelectedChange();

            } else if (msg.what == NOTIFY_ITEM_NUMBER_CHANGE) {
                //arg1:商品变化为数量arg1   arg2:商品在列表中的位置
                /** 修改列表数据**/
                try {
                    if (msg.arg2 != -1) {
//                        mFragment.get().mData.get(msg.arg2).setBuyed(msg.arg1);
                        String itemKey=msg.arg2+"";
                        Utils.setParam(getActivity(),itemKey,msg.arg1);
                        int itemNum=(int) Utils.getParam(getActivity(),itemKey,1);
                        carFragment.mData.get(msg.arg2).setBuyed(itemNum);
                    }



                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                /**遍历计算总价**/
//                TextView sum = (TextView) mFragment.get().notEmptyView.findViewById((R.id.money));
//                sum.setText(String.valueOf((int) mFragment.get().sum()) + ".00");
                TextView sum = (TextView) carFragment.notEmptyView.findViewById((R.id.money));
                sum.setText(String.valueOf((int) carFragment.sum()) + ".00");
            }
        }
    }

    private void notifySelectedChange() {
        TextView countDelNum = (TextView) notEmptyView.findViewById(R.id.cart_multi_summary);
        countDelNum.setText("共选中" + mAdapter.countChecked() + "件物品");
    }

    private void initHandler() {
        mHandler = new MyHandler(this);
    }

    private double sum() {
        long tolPrize = 0;
        for (int i = 0; i < mData.size(); i++) {
            tolPrize += mData.get(i).calWorth();
        }
        return tolPrize;
    }

    private void initReceiver() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Constants.ACTION_CART_CHANGE)) {
                    Log.d(TAG, "cart info:");
                    updateCart();
                } else if (intent.getAction().equals(Constants.ACTION_PAY_SUCCESS)) {
                    mData.clear();
                    Intent i = new Intent(Constants.ACTION_CART_CHANGE);
                    getActivity().sendBroadcast(i);
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.ACTION_PAY_SUCCESS);
        filter.addAction(Constants.ACTION_CART_CHANGE);
        getActivity().registerReceiver(receiver, filter);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initReceiver();


        mData = ((MyApplication) getActivity().getApplication()).getData();
        editing = false;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        initHandler();
        initToolbar(view);
        initView(view);
        maintainLayout(view);


        change2Editing(editing);
        return view;
    }


    private void initView(View view) {
        recyclerView = VHolder.get(view, R.id.cart);
        mAdapter = new CarItemAdapter(mData, getActivity());
        mAdapter.bindCustomerHandler(mHandler);
        recyclerView.setLayoutManager(mAdapter.getLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter);


        mPullToRefreshLayout = (PullRefreshLayout) view.findViewById(R.id.pullRefreshLayout);
        mPullToRefreshLayout.setOnRefreshListener(this);

        notEmptyView = view.findViewById(R.id.wraper_car_notempty);
        view.findViewById(R.id.go).setOnClickListener(this);
        view.findViewById(R.id.submit).setOnClickListener(this);
    }


    private void maintainLayout(View view) {
        if (mData.size() != 0)
            showItemList(view);
        else
            showEmpty(view);
    }


    private void initToolbar(View view) {
        TextView title = (TextView) view.findViewById(R.id.top_text_center);
        title.setText(getResources().getString(R.string.title_activity_cart));
        title.setVisibility(View.VISIBLE);
    }

    //切换编辑状态
    private void change2Editing(boolean editing) {
        Log.d(TAG, "changing 2 editing");
        mAdapter.updateDataStatus(editing);
        if (editing) {
            View totalView = notEmptyView.findViewById(R.id.footer);
            totalView.setVisibility(View.GONE);
            ViewStub deleteStub = (ViewStub) notEmptyView.findViewById(R.id.cart_multi_view_stub);
            if (deleteStub != null) {
                Log.d(TAG, "inflate ");
                deleteStub.inflate();
                notEmptyView.findViewById(R.id.cart_multi_del_btn).setOnClickListener(this);
                notEmptyView.findViewById(R.id.cart_multi_checkbox).setOnClickListener(this);
                notEmptyView.findViewById(R.id.tv_selectall).setOnClickListener(this);
                notEmptyView.findViewById(R.id.cart_multi_summary).setOnClickListener(this);
            } else {
                notEmptyView.findViewById(R.id.wraper_delete_cart).setVisibility(View.VISIBLE);
            }
            setCheckAllSelected(false);
        } else {
            View totalView = notEmptyView.findViewById(R.id.footer);
            totalView.setVisibility(View.VISIBLE);
            TextView amountOfcart = (TextView) totalView.findViewById(R.id.amount);
            amountOfcart.setText(getResources().getString(R.string.cart_number_desc).replace(Utils.getTextTemplecate(1), mData.size() + ""));
            ViewStub deleteStub = (ViewStub) notEmptyView.findViewById(R.id.cart_multi_view_stub);
            if (deleteStub == null) {
                notEmptyView.findViewById(R.id.wraper_delete_cart).setVisibility(View.INVISIBLE);
            }
        }
    }

    private void showEmpty(View view) {
        view.findViewById(R.id.wraper_car_notempty).setVisibility(View.GONE);
        view.findViewById(R.id.wraper_car_empty).setVisibility(View.VISIBLE);

    }


    private void showItemList(View view) {
        view.findViewById(R.id.wraper_car_notempty).setVisibility(View.VISIBLE);
        view.findViewById(R.id.wraper_car_empty).setVisibility(View.GONE);

    }


    @Override
    public void onStart() {
        Log.d(TAG, "carfragment onstart");
        updateCart();
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);

    }

    private void updateCart() {
        if (mAdapter == null)
            return;
        Log.d(TAG, "onResume:adapter size is" + mAdapter.getListSize());
        if (mData.size() == 0) {
            mPullToRefreshLayout.setRefreshing(false);
            maintainLayout(getView());

        }
        else
        {
            AppUtils.updateCartList(mData, getContext(), new Response.Listener<GoodListGsonResponse>() {
                @Override
                public void onResponse(GoodListGsonResponse goodListGsonResponse) {
                    if (!Utils.handleResponseError(getActivity(), goodListGsonResponse)) {

                        //为防止在服务器返回结果前购物车内容已遭到增减，需要优先遍历本地的购物车列表，更新其信息
                        boolean change = false;

                        synchronized (mData) {
                            ArrayList<GoodDetail> newlist = goodListGsonResponse.getBody().getDrawCycleDetailsList();
                            try {
                                for (int i = 0; i < mData.size(); i++) {
                                    for (int j = 0; j < newlist.size(); j++) {
                                        GoodBean good = mData.get(i).getGood();
                                        GoodBean newgood = newlist.get(j);
                                        if (good.getDrawCycleID() == newgood.getDrawCycleID()) {
                                            if (!good.equals(newgood)) {
                                                if (good.getFinalRslt() != null || good.getLeftCnt() == 0) {
                                                    //若返回数据中包含揭晓结果或剩余数量已为0，则清除该记录
                                                    mData.remove(i);
                                                } else {
                                                    good.clone(newgood);
                                                }
                                                change = true;
                                            }
                                        }
                                    }
                                }
                            } catch (IndexOutOfBoundsException e) {
                                Log.e(TAG, "car update outofbound");
                                e.printStackTrace();
                            }

                            if (change) {
                                mAdapter.notifyDataSetChanged();
                                maintainLayout(getView());
                                changeTabNum();
                            }
                        }
                    }
                    mPullToRefreshLayout.setRefreshing(false);


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    mPullToRefreshLayout.setRefreshing(false);
                }
            });
        }


        maintainLayout(getView());
        change2Editing(editing);
        mAdapter.notifyDataSetChanged();
    }

    private void changeTabNum() {
        Intent intent = new Intent(Constants.ACTION_CART_NUM_CHANGE);
        getActivity().sendBroadcast(intent);
        saveCartData(getContext());
    }


    private void setCheckAllSelected(boolean tag) {
        View v = getView().findViewById(R.id.cart_multi_checkbox);
        if (tag)
            ((ImageButton) v).setImageResource(R.drawable.ic_radius_checkbox_checked);
        else
            ((ImageButton) v).setImageResource(R.drawable.ic_radius_checkbox);

    }

    //检测购物车有没购买东西
    private boolean noBuyed() {
        int total = 0;
        for (CarItemBean bean : mData) {
            total += bean.getBuyed();
        }
        return total == 0;
    }


    @Override
    public void onRefresh() {
        updateCart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_edit:
                editing = !editing;
                change2Editing(editing);
                break;
            case R.id.go:
                MainActivity.getInstance(getContext(), 0);
                break;
            case R.id.cart_multi_del_btn:
                editing = !editing;
                change2Editing(editing);
                mAdapter.removeCheckedItem();
                maintainLayout(getView());
                changeTabNum();
                break;
            case R.id.cart_multi_checkbox:
            case R.id.cart_multi_summary:
            case R.id.tv_selectall:
                boolean tag = true;
                try {
                    tag = (boolean) v.getTag();
                    v.setTag(!tag);
                } catch (Exception e) {
                    v.setTag(tag);
                }
                mAdapter.checkAll((Boolean) v.getTag());
                setCheckAllSelected((boolean) v.getTag());
                notifySelectedChange();
                break;
            case R.id.submit://已完成TODO:需要进行一次网络判断，没有网络的时候,直接返回。 为此需要一个网络请求，那么就可以使用一个最简单的Get请求来进行网络的测试
                lock();
                VolleyTool.getInstance(getContext()).sendGsonRequest(this, IntroCodeFragment.InvitatorCodeResponse.class, Constants.MY_INVITATOR, new Response.Listener<IntroCodeFragment.InvitatorCodeResponse>() {
                    @Override
                    public void onResponse(IntroCodeFragment.InvitatorCodeResponse response) {
                        unLock();
                        if (!lock) {
                            submitSkip();
                        }
                        if (!Utils.handleResponseError(getActivity(), response)) {
                            String code = response.getBody().invitatorCode;
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        return;
                    }
                });
                break;
            default:
                break;
        }
    }

    private void submitSkip() {
        if (!noBuyed()) {
           OrderActivity.getInstance(getActivity(), null, REQUEST_PAY_INCART);
        } else {
            ToastUtil.showShort(getContext(), getString(R.string.cart_toast_buynothing));
        }
    }

    public void saveCartData(Context context){
        Gson gson = new Gson();
        String cartData = gson.toJson(mData);
        Log.d("购物车", "在购物车中商品数据发生变化时保存购物车数据:" + cartData);
        Utils.setParam(context, "cart", cartData);
    }

}
