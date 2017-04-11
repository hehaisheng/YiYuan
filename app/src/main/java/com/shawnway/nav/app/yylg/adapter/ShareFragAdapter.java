package com.shawnway.nav.app.yylg.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.bean.ShareListWrapper;
import com.shawnway.nav.app.yylg.bean.UrlBean;
import com.shawnway.nav.app.yylg.view.MySlideShowView;

/**
 * Created by Eiffel on 2015/12/16.
 */
public class ShareFragAdapter extends ShareItemAdapter {
    private static final String TAG = "ShareFragAdapter";
    private final int TYPE_HEADER = 0;

    public ShareFragAdapter(Context context) {
        super(context);
    }


    @Override
    public int getItemCount() {

        int count=super.getItemCount()+1;
        Log.d(TAG,"item count:"+count);
        return count;
    }


    @Override
    public int getDisplayType(int postion) {
        if (postion == 0) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    @Override
    public ShareListWrapper.ShareBean getItem(int pos) {
        return super.getItem(pos-1);
    }

    @Override
    public RecyclerView.ViewHolder onCreateDisplayHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        if ((holder = super.onCreateDisplayHolder(parent, viewType)) == null) {
            switch (viewType) {
                case TYPE_HEADER:
                    View header = inflater.inflate(R.layout.layout_header_allshare, parent, false);
                    ((MySlideShowView) header.findViewById(R.id.slider)).initPic(UrlBean.shareurls, "推荐");
                    return new HeaderHoler(header);

            }
        }

        return holder;

    }


    private class HeaderHoler extends RecyclerView.ViewHolder {
        public HeaderHoler(View header) {
            super(header);
        }
    }
}
