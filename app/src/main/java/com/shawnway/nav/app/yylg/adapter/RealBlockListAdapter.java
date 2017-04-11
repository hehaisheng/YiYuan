package com.shawnway.nav.app.yylg.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.activity.DetailActivity;
import com.shawnway.nav.app.yylg.bean.GoodBean;
import com.shawnway.nav.app.yylg.bean.RealBlockBean;
import com.shawnway.nav.app.yylg.tool.AppUtils;
import com.shawnway.nav.app.yylg.view.CirclerImageView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/6 0006.
 */
public class RealBlockListAdapter extends MCyclerAdapter<RealBlockBean> /*implements View.OnClickListener*/ {

    public RealBlockListAdapter(Context context){
        this(new ArrayList<RealBlockBean>(), context);
    }

    public RealBlockListAdapter(List<RealBlockBean> list, Context context) {
        super(list, context);
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager(Context context) {
        LinearLayoutManager lm = new LinearLayoutManager(context);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        return lm;
    }

    @Override
    public int getDisplayType(int postion) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateDisplayHolder(ViewGroup parent, int viewType) {
        View view;
        view = inflater.inflate(R.layout.realblock_item,parent,false);
        return new RealBlockItemHolder(view);
    }

    @Override
    public void onBindData(RecyclerView.ViewHolder holder, int position) {
        RealBlockBean bean = list.get(position);
        float progress = (float)bean.getPuredCnt()/(float)bean.getTotCnt()*100;
        BigDecimal bd = new BigDecimal(progress);
        bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);
        RealBlockItemHolder item = (RealBlockItemHolder) holder;
        Uri uri = Uri.parse(bean.getThumbnail());
        item.name.setText(bean.getProdName());
        item.progress.setText(bd+"%");
        item.progressBar.setProgress((int) progress);
        item.addToCart.setTag(position);
    }

    class RealBlockItemHolder extends RecyclerView.ViewHolder{

        ImageView img;
        TextView name;
        TextView progress;
        ProgressBar progressBar;
        ImageButton addToCart;

        public RealBlockItemHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.goods_img);
            name = (TextView) itemView.findViewById(R.id.realblock_item_tvName);
            progress = (TextView) itemView.findViewById(R.id.realblock_tvprogress);
            progressBar = (ProgressBar) itemView.findViewById(R.id.realblock_item_progressbar);
            addToCart = (ImageButton) itemView.findViewById(R.id.goods_add_cart);

        }
    }
}
