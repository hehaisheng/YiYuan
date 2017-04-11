package com.shawnway.nav.app.yylg.fragment;

import android.support.v4.app.Fragment;

import com.shawnway.nav.app.yylg.net.VolleyTool;


/**
 * Created by Eiffel on 2016/1/28.
 */
public class MyFragment extends Fragment {
    @Override
    public void onDestroy() {
        super.onDestroy();
        VolleyTool.getInstance(getContext()).getRequestQueue().cancelAll(this);
    }

    public String getLogTag(){
        return getClass().getSimpleName();
    }
}
