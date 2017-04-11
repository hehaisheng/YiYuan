package com.shawnway.nav.app.yylg.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.activity.DetailActivity;
import com.shawnway.nav.app.yylg.app.MyApplication;
import com.shawnway.nav.app.yylg.bean.CarItemBean;
import com.shawnway.nav.app.yylg.bean.GoodBean;
import com.shawnway.nav.app.yylg.bean.GoodDetail;
import com.shawnway.nav.app.yylg.tool.AppUtils;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.view.CirclerImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eiffel on 11/6/2015.
 */
public class GoodListAdapter extends MCyclerAdapter<GoodDetail> implements MCyclerAdapter.CycleItemClilkListener, View.OnClickListener {
    private static final String TAG = "GoodListAdapter";
    public final static int STYLE_BIG_SIZE = 0;
    public final static int STYLE_SMAL_SIZE = 1;

    private int style = STYLE_BIG_SIZE;

    public GoodListAdapter(Activity context) {
        this(context, STYLE_BIG_SIZE);
    }


    public GoodListAdapter(List<GoodDetail> list, Context context, int style) {
        super(list, context);
        this.style = style;
    }

    public GoodListAdapter(Context context, int style) {
        this(new ArrayList<GoodDetail>(), context, style);
    }


    @Override
    public int getDisplayType(int postion) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateDisplayHolder(ViewGroup parent, int viewType) {
        View view;
        switch (style) {
            case STYLE_SMAL_SIZE:
                view = inflater.inflate(R.layout.layout_rect_goods_item_small, parent, false);
                break;
            default:
                view = inflater.inflate(R.layout.layout_rect_goods_item, parent, false);
                break;
        }

        view.findViewById(R.id.goods_add_cart).setOnClickListener(this);
        return new GoodItemHolder(view, this);
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, int position) {

        GoodDetail bean = list.get(position);
        Log.d(TAG, bean.toString());


        float progress = (float)bean.getPuredCnt() / bean.getTotCnt();
        Log.d(TAG, "worth is" + progress);
        GoodItemHolder item = (GoodItemHolder) holder;
        item.setIsRecyclable(false);//设置item不复用
        if(bean.getBuyUnit() == 10)//显示十元商品图标
            item.tenIcon.setVisibility(View.VISIBLE);
        item.img.setImage(bean.getThumbnail());
        item.name.setText("（第"+bean.getCycle()+"期）"+bean.getProdName());
        item.worth.setText(context.getString(R.string.good_worth,bean.getPrice()));
        item.joined.setText(String.valueOf(bean.getPuredCnt()));
        item.total.setText(String.valueOf(bean.getTotCnt()));
        item.left.setText(String.valueOf(bean.getLeftCnt()));
        item.progressBar.setProgress((int) (progress * 1000));
        item.addToCart.setTag(position);
    }


    public RecyclerView.LayoutManager getLayoutManager(Context context) {
        LinearLayoutManager lm = new LinearLayoutManager(context);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        return lm;
    }

    @Override
    public void onCycleItemClick(View view, int position) {
        DetailActivity.getInstance(context,list.get(position));
    }

    @Override
    public void onClick(View v) {

        AppUtils.addTocart((Activity) context,list.get((Integer) v.getTag()));
    }

    private View.OnClickListener onClickListener;

    public void setOnClickListener(View.OnClickListener listener) {
        onClickListener = listener;
    }

    class GoodItemHolder extends BaseHolder {
        CirclerImageView img;
        TextView name;
        TextView worth;
        TextView joined;
        TextView total;
        TextView left;
        ProgressBar progressBar;
        ImageButton addToCart;
        ImageView tenIcon;

        public GoodItemHolder(View view, CycleItemClilkListener listener) {
            super(view, listener);
            img = (CirclerImageView) view.findViewById(R.id.goods_img);
            name = (TextView) view.findViewById(R.id.goods_name);
            worth = (TextView) view.findViewById(R.id.goods_worth);
            joined= (TextView) view.findViewById(R.id.tv_joined);
            total= (TextView) view.findViewById(R.id.tv_total);
            left= (TextView) view.findViewById(R.id.tv_left);
            progressBar= (ProgressBar) view.findViewById(R.id.progressbar);
            addToCart = (ImageButton) view.findViewById(R.id.goods_add_cart);
            tenIcon = (ImageView) view.findViewById(R.id.good_ten_label);

        }
    }
}
