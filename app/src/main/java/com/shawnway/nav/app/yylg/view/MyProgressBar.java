package com.shawnway.nav.app.yylg.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shawnway.nav.app.yylg.R;

/**
 * Created by Eiffel on 2016/3/29.
 */
public class MyProgressBar extends RelativeLayout {
    int joint=0;
    int left=0;
    int total=0;

    public MyProgressBar(Context context) {
        this(context, null, 0);
    }
    public MyProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        infalteLayout();

//        initLayout();
    }
    private void infalteLayout() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_good_progressbar, this, true);
        updateView();
    }


    private void updateView() {
        float progress = ((float) joint) / total;

        TextView tvJoined= (TextView) findViewById(R.id.tv_joined);
        tvJoined.setText(String.valueOf(joint));

        TextView tvTotal= (TextView) findViewById(R.id.tv_total);
        tvTotal.setText(String.valueOf(total));

        TextView tvLeft= (TextView) findViewById(R.id.tv_left);
        tvLeft.setText(String.valueOf(left));


//        ImageView probar= (ImageView) findViewById(R.id.goods_progress);
//        ViewGroup.LayoutParams layoutParams = probar.getLayoutParams();
//        layoutParams.width= (int) (findViewById(R.id.goods_progress_bg).getWidth()*0.5);
//        probar.setLayoutParams(layoutParams);
//        probar.invalidate();
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.mpbProgress);
        progressBar.setProgress((int) (progress * 1000));
    }

    public void setDate(int total,int joint,int left){
        this.total=total;
        this.joint=joint;
        this.left=left;
        updateView();
    }

    public void setJoint(int joint) {
        this.joint = joint;
        updateView();
    }

    public void setLeftGood(int left) {
        this.left = left;
        updateView();
    }

    public void setTotal(int total) {
        this.total = total;
        updateView();
    }
}
