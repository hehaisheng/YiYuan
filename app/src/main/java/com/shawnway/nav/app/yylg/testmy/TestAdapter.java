package com.shawnway.nav.app.yylg.testmy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.shawnway.nav.app.yylg.R;

/**
 * Created by Administrator on 2017-02-28.
 */

public class TestAdapter extends BaseAdapter {
    public Context context;
    public TestAdapter(Context context)
    {
        this.context=context;
    }
    @Override
    public int getCount() {
        return 20;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.layout_item_systeminfo,parent,false);
    }
}
