package com.shawnway.nav.app.yylg.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baoyz.widget.PullRefreshLayout;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.activity.WebViewActivity;
import com.shawnway.nav.app.yylg.adapter.CalDelCountDownAdapter;
import com.shawnway.nav.app.yylg.adapter.CalDelCountDownMoreAdapter;
import com.shawnway.nav.app.yylg.bean.GoodBean;
import com.shawnway.nav.app.yylg.bean.ResponseGson;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.StringUtils;
import com.shawnway.nav.app.yylg.tool.Utils;
import com.shawnway.nav.app.yylg.view.NoScrollListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/25 0025.
 */
public class CalDetCountDownFragment extends MyFragment implements View.OnClickListener, PullRefreshLayout.OnRefreshListener{
    //FIXME  确认三种开奖方式所返回的drawType字段是否跟下面相符
    public static final String TYPE_1 = "LOTTERY";      //彩票？
    public static final String TYPE_2 = "NORMAL";
    public static final String TYPE_3 = "PARVALUE";     //票面价值？

    private final int HEADER_LENTH = 5;     //购买的记录条数超过这个需要用到“展开记录”来显示五条之后的记录
    NoScrollListView headerList;
    NoScrollListView moreList;
    private GoodBean bean;
    private ArrayList<CalDelResponse.CalDelBody.CalInfoItem> mData;
    private ArrayList<CalDelResponse.CalDelBody.CalInfoItem> mMoreData;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bean = (GoodBean) getArguments().getSerializable("goodbean");
        if (bean == null) {
            getActivity().finish();
            throw new IllegalArgumentException("should give 'bean' before create an Instance of CalDelFragment");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_caldetail, container, false);
        headerList = (NoScrollListView) view.findViewById(R.id.lv_caldet);      //显示乐购时间、数据、参与者的
        moreList = (NoScrollListView) view.findViewById(R.id.lv_caldet_more);   //显示展开数据
        setListener(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        onRefresh();
        super.onViewCreated(view, savedInstanceState);
    }

    private void setListener(View view) {
        view.findViewById(R.id.bt_caldet_openrecord).setOnClickListener(this);
        view.findViewById(R.id.bt_gothirdpart).setOnClickListener(this);
        view.findViewById(R.id.bt_gothirdpart2).setOnClickListener(this);
    }


    @Override
    public void onRefresh() {
        final Map<String, String> params = new HashMap<>();
        params.put("drawcycleId", String.valueOf(bean.getDrawCycleID()));

        VolleyTool.getInstance(getContext()).sendGsonRequest(this, CalDelResponse.class, Constants.CALDETAILCOUNTDOWN_URL, params, new Response.Listener<CalDelResponse>() {
            @Override
            public void onResponse(CalDelResponse calDelResponse) {
                if (!Utils.handleResponseError(getActivity(), calDelResponse)) {
                    if (calDelResponse==null||calDelResponse.getBody()==null)
                        return;
                    View view = getView();
                    if (view != null) {
                        switch (calDelResponse.getBody().calculateResult.drawType) {
                            case TYPE_1:
                                view.findViewById(R.id.wraper_callist).setVisibility(View.VISIBLE);
                                //重新加载列表数据，可优化
                                clearData();
                                setListData(calDelResponse.getBody());
                                //填充type1数据
                                setupType1(calDelResponse.getBody());
                                break;
                            case TYPE_2:
                                view.findViewById(R.id.wraper_callist).setVisibility(View.VISIBLE);
                                clearData();
                                setListData(calDelResponse.getBody());
                                setType2Data(calDelResponse.getBody());
                                break;
                            case TYPE_3:
                                view.findViewById(R.id.wraper_callist).setVisibility(View.GONE);
                                clearData();
                                setupType3Data(calDelResponse.getBody());
                                break;


                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
    }

    private void setupType3Data(CalDelResponse.CalDelBody response) {
        View view=getView();
        if (view!=null){
            CalDelResponse.CalDelBody.CalResult result = response.calculateResult;
            View wrapper = view.findViewById(R.id.layout_calculate_type3);
            wrapper.setVisibility(View.VISIBLE);
            ((TextView)wrapper.findViewById(R.id.lb13)).setText("?");
            ((TextView) wrapper.findViewById(R.id.calculate_A)).setText("?");
            String lotteryRst= StringUtils.isBlank(result.lotteryOpenCode) ? getString(R.string.waiting_open) : result.lotteryOpenCode;
            ((TextView) wrapper.findViewById(R.id.calculate_lottery)).setText(getString(R.string.calculating_lottery,lotteryRst));
            ((TextView)wrapper.findViewById(R.id.calculate_lottery_cycle)).setText(getString(R.string.calculating_cycle, response.nextLotteryCircle));
            ((TextView) wrapper.findViewById(R.id.calculate_finalres)).setText(StringUtils.isBlank(result.finalResult) ? getString(R.string.waiting_released) : result.finalResult);
        }
    }

    private void setupType1(CalDelResponse.CalDelBody response) {
        View view = getView();
        if (view != null) {
            CalDelResponse.CalDelBody.CalResult result = response.calculateResult;
            View wrapper = view.findViewById(R.id.layout_calculate_type1);
            wrapper.setVisibility(View.VISIBLE);
            ((TextView) wrapper.findViewById(R.id.calculate_A)).setText(result.summation);
            String lotteryRst= StringUtils.isBlank(result.lotteryOpenCode) ? getString(R.string.waiting_open) : result.lotteryOpenCode;
            ((TextView) wrapper.findViewById(R.id.calculate_B)).setText(getString(R.string.calculating_lottery, lotteryRst));
            ((TextView)wrapper.findViewById(R.id.calculate_B_cycle)).setText(getString(R.string.calculating_cycle,response.nextLotteryCircle));
            ((TextView) wrapper.findViewById(R.id.calculate_joint)).setText(result.participaintCnt);//已完成TODO:参与人次
            ((TextView)wrapper.findViewById(R.id.calculate_finalres)).setText(StringUtils.isBlank(result.finalResult) ? getString(R.string.waiting_released) : result.finalResult);
            String PurchDate = result.lastPurchDate;
            String[] strs = PurchDate.split("T");
            String[] strings = strs[1].split("\\+");
            PurchDate = strs[0].concat(" ").concat(strings[0]);
            ((TextView) view.findViewById(R.id.caldetail_tv_endtime)).setText(getString(R.string.caldetail_label_endtime, PurchDate));
        }
    }

    private void setType2Data(CalDelResponse.CalDelBody body) {
        String statement = null;
        View view = getView();
        if (view != null) {
            if (body.calculateResult != null) {
                String summation = body.calculateResult.summation;
                String participaintCnt = body.calculateResult.participaintCnt;
                String result = body.calculateResult.finalResult;
                String remainder = body.calculateResult.remainder;
                String drawType = body.calculateResult.drawType;

                switch (drawType) {
                    case TYPE_2:
                        statement = getString(R.string.caldet_normal_2, summation, summation, participaintCnt, remainder, remainder, result);
                        view.findViewById(R.id.bt_gothirdpart).setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }

                String date = body.calculateResult.lastPurchDate;
                String[] strs = date.split("T");
                String[] strings = strs[1].split("'+'");
                date = strs[0].concat(" ").concat(strings[0]);

                ((TextView) view.findViewById(R.id.caldetail_tv_endtime)).setText(getString(R.string.caldetail_label_endtime, date));

            }
            if (statement == null)
                view.findViewById(R.id.layout_calculate_type2).setVisibility(View.GONE);
            else {
                view.findViewById(R.id.layout_calculate_type2).setVisibility(View.VISIBLE);
                TextView tv = (TextView) getView().findViewById(R.id.statement);
                tv.setText(Html.fromHtml(statement));
            }
        }
    }

    private void setListData(CalDelResponse.CalDelBody body) {
        if (body.participationList == null)
            return;
        ArrayList<CalDelResponse.CalDelBody.CalInfoItem> newlist = body.participationList;
        for (int i = 0; i < newlist.size(); i++) {
            if (i < HEADER_LENTH)
                mData.add(newlist.get(i));
            else
                mMoreData.add(newlist.get(i));
        }

        ((CalDelCountDownAdapter) headerList.getAdapter()).notifyDataSetChanged();
        ((CalDelCountDownMoreAdapter) moreList.getAdapter()).notifyDataSetChanged();

    }

    private void clearData() {
        if (headerList.getAdapter() == null) {
            mData = new ArrayList<>();
            headerList.setAdapter(new CalDelCountDownAdapter(mData, getContext()));
        }
        if (moreList.getAdapter() == null) {
            mMoreData = new ArrayList<>();
            moreList.setAdapter(new CalDelCountDownMoreAdapter(mMoreData, getContext()));
        }

        mData.clear();
        mMoreData.clear();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_caldet_openrecord://展开记录
                moreList.setVisibility(moreList.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                ((TextView) v).setText(moreList.getVisibility() == View.VISIBLE ? getString(R.string.caldet_close_record) : getString(R.string.caldet_open_record));
                break;
            case R.id.bt_gothirdpart://开奖查询
                WebViewActivity.getInstance(getContext(), R.string.title_activity_thirdpart,
                        getString(R.string.url_thirdpart), null, true, true, true);
                break;

            case R.id.bt_gothirdpart2://开奖查询
                WebViewActivity.getInstance(getContext(), R.string.title_activity_thirdpart,
                        getString(R.string.url_thirdpart), null, true, true, true);
                break;

        }
    }


    public class CalDelResponse extends ResponseGson<CalDelResponse.CalDelBody> {

        public class CalDelBody {
            public String nextLotteryCircle;
            public CalResult calculateResult;
            public ArrayList<CalInfoItem> participationList;//calcInforList;

            public class CalResult {
                public String finalResult;
                public String participaintCnt;//已完成TODO:少了
                public int count;
                public String summation;
                public String remainder;
                public String drawType;
                public String lotteryCycle;//已完成TODO:少了
                public String lotteryOpenCode;
                public String lotteryOpenTime;
                public String lotteryOpenCodeTail;
                public String lastPurchDate;
            }

            public class CalInfoItem {
                public String purDate;//purchDate;
                public String purchTime;
                public String result;
                public String customerName;//customerNme;
            }
        }
    }
}
