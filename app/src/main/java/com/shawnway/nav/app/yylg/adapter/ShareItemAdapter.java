package com.shawnway.nav.app.yylg.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.activity.CommentsActivity;
import com.shawnway.nav.app.yylg.activity.CustomInfoActivity;
import com.shawnway.nav.app.yylg.activity.ShareDetialActivity;
import com.shawnway.nav.app.yylg.base.BaseSubscriber;
import com.shawnway.nav.app.yylg.bean.ResponseGson;
import com.shawnway.nav.app.yylg.bean.ShareListWrapper;
import com.shawnway.nav.app.yylg.fragment.UserFragment;
import com.shawnway.nav.app.yylg.net.RetrofitManager;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.GlideUtils;
import com.shawnway.nav.app.yylg.tool.ThreadTransformer;
import com.shawnway.nav.app.yylg.tool.ToastUtil;
import com.shawnway.nav.app.yylg.tool.Utils;
import com.shawnway.nav.app.yylg.view.CirclerImageView;
import com.shawnway.nav.app.yylg.bean.ShareListWrapper.ShareBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Eiffel on 2015/11/7.
 */
public class ShareItemAdapter extends MCyclerAdapter<ShareListWrapper.ShareBean> implements View.OnClickListener {
    private static final String TAG = "ShareItemAdapter";
    protected final int TYPE_ITEM = 1;


    private Context mContenxt;

    public ShareItemAdapter(Context context) {
        this(new ArrayList<ShareBean>(), context);
    }

    public ShareItemAdapter(List<ShareBean> shareBeans, Context context) {
        super(shareBeans, context);
        mContenxt = context;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public int getDisplayType(int postion) {
        return TYPE_ITEM;
    }


    @Override
    public RecyclerView.ViewHolder onCreateDisplayHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_ITEM:
                View view = inflater.inflate(R.layout.layout_share_item, parent, false);
                return new ItemHolder(view);
        }
        return null;
    }

    private ShareBean bean;

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, int position) {
        switch (getDisplayType(position)) {
            case TYPE_ITEM:
                bean = list.get(position);

                ItemHolder _holder = (ItemHolder) holder;
                _holder.wrapper.setTag(bean);
                _holder.wrapper.setOnClickListener(this);

                _holder.header.setOnClickListener(this);
                _holder.customername.setTag(bean);
                _holder.customername.setText(bean.custName);
                _holder.customername.setOnClickListener(this);

                _holder.title.setText(bean.subject);
                _holder.time.setText(bean.showDate);
                _holder.content.setText(bean.content);

                int imgcnt = bean.images.size();
                _holder.img0.setImage(imgcnt > 0 ? bean.images.get(0) : null);
                _holder.img1.setImage(imgcnt > 1 ? bean.images.get(1) : null);
                _holder.img2.setImage(imgcnt > 2 ? bean.images.get(2) : null);
                _holder.img3.setImage(imgcnt > 3 ? bean.images.get(3) : null);
                _holder.img4.setImage(imgcnt > 4 ? bean.images.get(4) : null);

                _holder.likes.setText(String.valueOf(bean.praiseCnt));
                _holder.likes.setTag(bean);
                _holder.likes.setOnClickListener(this);

                _holder.comments.setText(String.valueOf(bean.commentCnt));
                _holder.comments.setTag(bean);
                _holder.comments.setOnClickListener(this);

                setUserImage(bean.winnerId, _holder.header);
                break;
        }
    }

    /**
     * 设置晒单用户头像
     *
     * @param userImageView
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
    public RecyclerView.LayoutManager getLayoutManager(Context context) {
        LinearLayoutManager lm = new LinearLayoutManager(context);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        lm.setSmoothScrollbarEnabled(true);
        return lm;
    }


    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.share_list_customer_header://已完成TODO:除了问题了，一直都是
            case R.id.share_list_customer:
                try {
                    CustomInfoActivity.getInstance(mContenxt, ((ShareBean) v.getTag()).winnerId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.share_list_wrapper:
                try {
                    ShareDetialActivity.getInstance(mContenxt, (ShareBean) v.getTag());
                } catch (ClassCastException e) {
                    Log.e(TAG, "should set position tag to wrapper");
                }
                break;
            case R.id.bt_comments://全部评论
                CommentsActivity.getInstance(context, CommentsActivity.class, (ShareBean) v.getTag());
                break;
            case R.id.bt_likes:
                Map<String, String> params = new HashMap<>();
                params.put("prizeshowId", String.valueOf(((ShareBean) v.getTag()).prizeShowId));
                VolleyTool.getInstance(context).sendGsonRequest(context, LikeResponse.class, Constants.LIKE, params, new Response.Listener<LikeResponse>() {
                    @Override
                    public void onResponse(LikeResponse likeResponse) {
                        if (!Utils.handleResponseError((Activity) context, likeResponse)) {
                            try {
                                //TODO:如果要避免同一个用户对同一个晒单点赞多次，需要后台做过滤操作
                                ((TextView) v).setText(String.valueOf(likeResponse.getBody().praiseCnt));
                                ToastUtil.showShort(context, context.getString(R.string.success_like));
                            } catch (ClassCastException e) {
                                Log.d(TAG, "like view not exist");
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
                break;
        }
    }


    private class ItemHolder extends RecyclerView.ViewHolder {
        ViewGroup wrapper;

        CircleImageView header;
        TextView customername;
        TextView time;
        TextView title;
        TextView statue;
        TextView content;
        CirclerImageView img0;
        CirclerImageView img1;
        CirclerImageView img2;
        CirclerImageView img3;
        CirclerImageView img4;
        TextView comments;
        TextView likes;


        public ItemHolder(View itemView) {
            super(itemView);
            wrapper = (ViewGroup) itemView.findViewById(R.id.share_list_wrapper);
            header = (CircleImageView) itemView.findViewById(R.id.share_customer_header);
            customername = (TextView) itemView.findViewById(R.id.share_list_customer);
            time = (TextView) itemView.findViewById(R.id.share_list_time);
            title = (TextView) itemView.findViewById(R.id.share_list_title);
            statue = (TextView) itemView.findViewById(R.id.share_list_status);
            content = (TextView) itemView.findViewById(R.id.share_list_content);
            img0 = (CirclerImageView) itemView.findViewById(R.id.img0);
            img1 = (CirclerImageView) itemView.findViewById(R.id.img1);
            img2 = (CirclerImageView) itemView.findViewById(R.id.img2);
            img3 = (CirclerImageView) itemView.findViewById(R.id.img3);
            img4 = (CirclerImageView) itemView.findViewById(R.id.img4);
            likes = (TextView) itemView.findViewById(R.id.bt_likes);
            comments = (TextView) itemView.findViewById(R.id.bt_comments);
        }
    }


    private class LikeResponse extends ResponseGson<LikeBody> {
    }

    private class LikeBody {
        int praiseCnt;
        long prizeshowId;
    }
}
