package com.shawnway.nav.app.yylg.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.activity.MainActivity;
import com.shawnway.nav.app.yylg.bean.GrantHisBean;
import com.shawnway.nav.app.yylg.bean.ResponseGson;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.StringUtils;
import com.shawnway.nav.app.yylg.tool.ToastUtil;
import com.shawnway.nav.app.yylg.tool.Utils;
import com.shawnway.nav.app.yylg.tool.VolleyErrorHelper;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eiffel on 2016/2/5.
 */
public class GrantCfmFragment extends MyFragment implements View.OnClickListener {
    public static GrantCfmFragment getInstance(String payee) {
        Bundle bundle = new Bundle();
        bundle.putString("payee", payee);
        GrantCfmFragment fragment = new GrantCfmFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static GrantFragment getInstance(String amount,String remark,View view){
        Bundle bundle = new Bundle();
        bundle.putString("amount",Utils.getEditTextStr(view.findViewById(R.id.inputer_amount)));
        bundle.putString("remark", Utils.getEditTextStr(view.findViewById(R.id.inputer_remark)));
        GrantFragment fragment = new GrantFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grant_cfm, container, false);
        setListener(view);
        return view;
    }

    private void setListener(View view) {
        view.findViewById(R.id.submit).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.submit:
                String amount=Utils.getEditTextStr(getView().findViewById(R.id.inputer_amount));
                if (StringUtils.isBlank(amount))
                {
                    ToastUtil.showShort(getContext(),getString(R.string.error_amountnull));
                }else {

                    final Map<String, String> params = new HashMap<>();
                    params.put("payee", getArguments().getString("payee"));
                    params.put("amount", amount);
                    params.put("remark", Utils.getEditTextStr(getView().findViewById(R.id.inputer_remark)));

                    VolleyTool.getInstance(getContext()).sendGsonPostRequest(this, ResponseGson.class, Constants.GRANT_URL, params, new Response.Listener<ResponseGson>() {
                        @Override
                        public void onResponse(ResponseGson responseGson) {
                            if (!Utils.handleResponseError(getActivity(), responseGson)) {
//                                saveHistory(params);//有没有保存了两遍的嫌疑？这是第一遍
                                getActivity().setResult(Activity.RESULT_OK);
                                getActivity().finish();
                            }
                            saveHistory(params);//有没有保存了两遍的嫌疑？这是第二遍
                            ToastUtil.showShort(getContext(), "赠与成功", true);
                            MainActivity.getInstance(getContext(),4);

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            ToastUtil.showShort(getContext(), VolleyErrorHelper.getMessage(volleyError,getContext()),true);
                            saveHistory(params);

                        }
                    });
                    break;
                }
        }
    }

    private void saveHistory(Map<String, String> params) {
        GrantHisBean bean=new GrantHisBean();
        try {
            //保存赠与记录
            JSONArray hst=bean.getHis(getContext());
            hst.put(params.get("payee"));
            bean.saveHist(getContext(),hst);
        } catch (JSONException e) {
            Log.e(getLogTag(), "failed to convert grant history to jsonarray");
        }
    }
}
