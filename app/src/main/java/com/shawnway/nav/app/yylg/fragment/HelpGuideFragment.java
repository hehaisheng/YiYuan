package com.shawnway.nav.app.yylg.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shawnway.nav.app.yylg.R;

/**
 * Created by Eiffel on 2015/12/30.
 */
public class HelpGuideFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_helpguide,container,false);
        ((TextView)view.findViewById(R.id.text)).setText(Html.fromHtml(getArguments().getString("bean")));
        return view;
    }
}
