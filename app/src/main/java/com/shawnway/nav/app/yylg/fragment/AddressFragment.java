package com.shawnway.nav.app.yylg.fragment;

import android.app.Activity;
import android.content.Intent;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shawnway.nav.app.yylg.adapter.AddressAdapter;
import com.shawnway.nav.app.yylg.bean.AddressBean;
import com.shawnway.nav.app.yylg.bean.ResponseGson;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.Utils;

/**
 * Created by Eiffel on 2015/11/17.
 */
public class AddressFragment extends ListFragment {


    @Override
    protected AddressAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new AddressAdapter(getContext());
        }
        return (AddressAdapter) mAdapter;
    }

    @Override
    public void onLoadMore() {
        VolleyTool.getInstance(getContext()).sendGsonRequest(this,AddressResponseWrapper.class, Constants.GET_ADDRESS_URL, new Response.Listener<AddressResponseWrapper>() {
            @Override
            public void onResponse(AddressResponseWrapper response) {
                if (!Utils.handleResponseError(getActivity(), response)) {
                    if (response.getBody().delAddressDetails != null) {
                        getAdapter().add(response.getBody().delAddressDetails);
                        getAdapter().notifyDataSetChanged();
                    }
                }
                onRefreshComplete();
                onLoadmoreComplete();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                onRefreshComplete();
                onLoadmoreComplete();

            }
        });
    }

    @Override
    protected int getDataSize() {
        return getAdapter().getListSize();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==Constants.ACTION_REQUEST_LOGIN&&resultCode != Activity.RESULT_OK) {
            getActivity().finish();
        } else {
            onRefresh();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class AddressResponseWrapper extends ResponseGson<AddressResponseBody> {
    }

    private class AddressResponseBody {
        AddressBean delAddressDetails;
    }
}
