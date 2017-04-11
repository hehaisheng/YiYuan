package com.shawnway.nav.app.yylg.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.bean.CommentsResponseWrapper.CommentBean;
import com.shawnway.nav.app.yylg.view.CirclerImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eiffel on 2015/12/9.
 */
public class CommentAdapter  extends  MCyclerAdapter<CommentBean>{
    public CommentAdapter( Context context) {
        super(new ArrayList<CommentBean>(), context);
    }

    public CommentAdapter(List<CommentBean> list, Context context) {
        super(list, context);
    }

    @Override
    public int getDisplayType(int postion) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateDisplayHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(inflater.inflate(R.layout.layout_item_comment,parent,false));
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, int position) {
        ItemHolder _holder= (ItemHolder) holder;
        CommentBean bean=list.get(position);

        _holder.name.setText(bean.custName);
        _holder.time.setText(bean.commentDate);
        _holder.comment.setText(bean.content);
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager(Context context) {
        return new LinearLayoutManager(context);
    }

    private class ItemHolder extends RecyclerView.ViewHolder {
        CirclerImageView header;
        TextView name;
        TextView time;
        TextView comment;

        public ItemHolder(View itemView) {
            super(itemView);
            header= (CirclerImageView) itemView.findViewById(R.id.image);
            name= (TextView) itemView.findViewById(R.id.name);
            time= (TextView) itemView.findViewById(R.id.time);
            comment= (TextView) itemView.findViewById(R.id.content);
        }
    }
}
