package com.shawnway.nav.app.yylg.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shawnway.nav.app.yylg.R;

public class ShareFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_shareorder, container, false);
        initHeader(view);
        return view;

    }

    private void initHeader(View view) {
        TextView title=(TextView) view.findViewById(R.id.top_text_center);
        title.setText(getResources().getString(R.string.title_activity_share));
        title.setVisibility(View.VISIBLE);
    }

}
