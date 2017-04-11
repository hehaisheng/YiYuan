package com.shawnway.nav.app.yylg.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.activity.CustomInfoActivity;
import com.shawnway.nav.app.yylg.base.BaseSubscriber;
import com.shawnway.nav.app.yylg.bean.ShareListWrapper;
import com.shawnway.nav.app.yylg.fragment.UserFragment;
import com.shawnway.nav.app.yylg.net.RetrofitManager;
import com.shawnway.nav.app.yylg.tool.AppUtils;
import com.shawnway.nav.app.yylg.tool.GlideUtils;
import com.shawnway.nav.app.yylg.tool.ThreadTransformer;
import com.shawnway.nav.app.yylg.view.CirclerImageView;
import com.shawnway.nav.app.yylg.view.MyLinearLayoutManager;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Eiffel on 2015/11/7.
 */
public class ShareDetialItemAdapter extends MCyclerAdapter<String> implements MCyclerAdapter.CycleItemClilkListener {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    ShareListWrapper.ShareBean headerData;
    private Context mContext;

    public ShareDetialItemAdapter(ShareListWrapper.ShareBean headerData, Context context) {
        this(new ArrayList<String>(), headerData, context);
        mContext = context;
    }

    public ShareDetialItemAdapter(ArrayList<String> list, ShareListWrapper.ShareBean headerData, Context context) {
        super(list, context);
        this.headerData = headerData;
    }


    public ShareDetialItemAdapter(Activity activity) {
        this(new ArrayList<String>(), null, activity);
    }


    @Override
    public int getDisplayType(int postion) {
        if (postion == 0)
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateDisplayHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                final HeaderHolder holder = new HeaderHolder(inflater.inflate(R.layout.layout_share_detail_header, parent, false));
                holder.title.setText(headerData.subject);
                holder.author.setText(headerData.custName);
                holder.goodname.setText(context.getString(R.string.detail_good_title_text, headerData.cycle, headerData.prodName));
                holder.content.setText(headerData.content);
                holder.revealTime.setText(headerData.drawDate);
                holder.amount.setText(String.valueOf(headerData.puredCnt));//晒单详情中的参与人次是总的参与人次不是中奖者的参与人次
                setUserImage(headerData.winnerId, holder.header);
                return holder;
            default:
                View view = inflater.inflate(R.layout.layout_share_detail_image, parent, false);
                return new ImageItemHolder(view, this);
        }
    }

    /**
     * 设置用户头像
     */
    private void setUserImage(int winnerId, final ImageView userImageView) {
        RetrofitManager.getInstance()
                .getApi()
                .getCustomerImage(winnerId)
                .compose(ThreadTransformer.<UserFragment.ImageBean>applySchedulers())
                .subscribe(new BaseSubscriber<UserFragment.ImageBean>() {
                    @Override
                    public void onSuccess(UserFragment.ImageBean imageBean) {
                        GlideUtils.loadImage(context,
                                imageBean.getBody().getPathUrl(),
                                R.drawable.portrait,
                                userImageView);
                    }
                });
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, int position) {
        switch (getDisplayType(position)) {
            case TYPE_HEADER:
                break;
            case TYPE_ITEM:
                CirclerImageView img = ((ImageItemHolder) holder).img;
                img.setImage(list.get(position - 1));
                break;
        }

    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager(Context context) {
        LinearLayoutManager lm = new MyLinearLayoutManager(context);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        return lm;
    }

    @Override
    public void onCycleItemClick(View view, int position) {

    }

    public void setHeaderData(ShareListWrapper.ShareBean data) {
        headerData = data;
        notifyDataSetChanged();
    }

    class HeaderHolder extends RecyclerView.ViewHolder {
        CircleImageView header;
        TextView title;
        TextView author;
        TextView goodname;
        TextView amount;
        TextView revealTime;
        TextView content;


        public HeaderHolder(View itemView) {
            super(itemView);
            header = (CircleImageView) itemView.findViewById(R.id.share_list_customer_header);
            header.setOnClickListener(new View.OnClickListener() {
                //服务器数据少了winnerId字段（已完成TODO）
                @Override
                public void onClick(View v) {
                    CustomInfoActivity.getInstance(mContext, headerData.winnerId);
                }
            });
            title = (TextView) itemView.findViewById(R.id.share_detail_title);
            author = (TextView) itemView.findViewById(R.id.share_detail_author);
            author.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomInfoActivity.getInstance(mContext, headerData.winnerId);
                }
            });
            goodname = (TextView) itemView.findViewById(R.id.share_detail_goods);
            amount = (TextView) itemView.findViewById(R.id.share_detail_num);
            revealTime = (TextView) itemView.findViewById(R.id.share_detail_reveal_time);
            content = (TextView) itemView.findViewById(R.id.share_detail_content);
        }

    }

    private class ImageItemHolder extends BaseHolder {
        CirclerImageView img;

        public ImageItemHolder(View view, CycleItemClilkListener listener) {
            super(view, listener);
            img = (CirclerImageView) view.findViewById(R.id.share_detail_image);
        }
    }
}
