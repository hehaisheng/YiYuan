package com.shawnway.nav.app.yylg.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.activity.HelpCenterActivity;
import com.shawnway.nav.app.yylg.activity.ServiceProtocolActivity;
import com.shawnway.nav.app.yylg.bean.HelpCenterResponse;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.Utils;

/**
 * Created by Eiffel on 2015/12/30.
 */
public class ServiceProFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_serpro, container, false);
        initData();
        return view;
    }

    private void initData() {
        VolleyTool.getInstance(getContext()).sendGsonRequest(this,HelpCenterResponse.class, Constants.HELP_CENTER, new Response.Listener<HelpCenterResponse>() {
            @Override
            public void onResponse(HelpCenterResponse helpCenterResponse) {
                if (!Utils.handleResponseError(getActivity(), helpCenterResponse)) {
                    HelpCenterResponse.HelpCenterBody body = helpCenterResponse.getBody();
                    ((TextView) getView().findViewById(R.id.text)).setText(Html.fromHtml(body.serviceProtocol));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
    }
}
