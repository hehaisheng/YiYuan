package com.shawnway.nav.app.yylg.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baoyz.widget.PullRefreshLayout;
import com.shawnway.nav.app.yylg.R;

/**
 * Created by 裘 on 2016/9/27 0027.
 */

public class HomeWrapperFragment extends Fragment implements PullRefreshLayout.OnRefreshListener {

    private static final String TAG = "LastAnnounceFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initFragment();
    }

    private void initFragment() {
        Fragment fragment = new HomeFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment, fragment).commit();
    }

    @Override
    public void onRefresh() {
        initFragment();
    }
}