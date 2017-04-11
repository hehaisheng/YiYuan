package com.shawnway.nav.app.yylg.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.shawnway.nav.app.yylg.R;

/**
 * Created by Administrator on 2016/6/22 0022.
 */
public class LastAnnounceFragment extends Fragment implements PullRefreshLayout.OnRefreshListener{

    private static final String TAG = "LastAnnounceFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lastannounce,container,false);
        initToolBar(view);
        initFragment();
        return view;
    }

    private void initFragment() {
        Fragment fragment = new LstAnnounFragment();
        FragmentManager fragmentManager = getFragmentManager();
        if(fragmentManager==null || fragment==null){
            return;
        }
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_lastannounce_fragment, fragment).commit();
    }

    private void initToolBar(View view) {
        TextView title=(TextView) view.findViewById(R.id.top_text_center);
        title.setText(getResources().getString(R.string.title_activity_lstanounce));
        title.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRefresh() {
        initFragment();
    }
}
