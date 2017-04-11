package com.shawnway.nav.app.yylg.abview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.bean.SignResponseWrapper;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.Utils;

/**
 * Created by Eiffel on 2015/12/2.
 */
public class MissionItemView extends LinearLayout implements View.OnClickListener {

    public static final int UNCOMPELETE = 0;
    public static final int UNCLAIM = 1;
    public static final int COMPELETE = 2;
    public static final int UNSIGN = 3;
    public static final int SIGNED = 4;

    private static final String TAG = "MissionItemView";
    private int mStatue;

    private String mDesc;
    private String mReward;
    private String mTitle;
    private BitmapDrawable mImage;
    private AlertDialog dlg;

    public MissionItemView(Context context) {
        this(context, null);
    }

    public MissionItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MissionItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.MissionItemView);

        int n = a.getIndexCount();

        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.MissionItemView_mivImage:
                    mImage= (BitmapDrawable) a.getDrawable(attr);
                    break;
                case R.styleable.MissionItemView_mivTitle:
                    mTitle = a.getString(attr);
                    break;
                case R.styleable.MissionItemView_mivReward:
                    mReward= a.getString(attr);
                    break;
                case R.styleable.MissionItemView_mivDesc:
                    mDesc  = a.getString(attr);
                    break;
                case R.styleable.MissionItemView_mivSta:
                    mStatue = a.getInt(attr, UNCOMPELETE);
                    break;

            }

        }

        a.recycle();

        LayoutInflater.from(getContext()).inflate(R.layout.layout_item_present, this, true);
        initView();
    }

    private void initView() {
        if (mImage != null) {
            ImageView iamge = (ImageView) findViewById(R.id.image);
            iamge.setImageDrawable(mImage);
        }

        if (mTitle != null) {
            TextView title = (TextView) findViewById(R.id.title);
            title.setText(mTitle);
        }

        if (mReward != null) {
            TextView rew = (TextView) findViewById(R.id.reward);
            rew.setText(mReward);
        }

        if (mDesc != null) {
            TextView desc = (TextView) findViewById(R.id.desc);
            desc.setText(mDesc);
        }

        updateButton(mStatue);
    }

    public void updateButton(int mStatue) {
        clearButton();

        switch (mStatue) {
            case UNCOMPELETE:
                findViewById(R.id.tv_mission_uncomplite).setVisibility(VISIBLE);
                break;
            case UNCLAIM:
                findViewById(R.id.mission_button).setVisibility(VISIBLE);
                break;
            case COMPELETE:
                findViewById(R.id.tv_mission_complite).setVisibility(VISIBLE);
                break;
            case UNSIGN:
                Button sign = (Button) findViewById(R.id.mission_sign);
                sign.setVisibility(VISIBLE);
                sign.setEnabled(true);
                sign.setText(getContext().getString(R.string.mission_not_sign));
                sign.setOnClickListener(this);
                break;
            case SIGNED:
                Button sig = (Button) findViewById(R.id.mission_sign);
                sig.setBackgroundColor(getResources().getColor(R.color.appcolor_light));
                sig.setVisibility(VISIBLE);
                sig.setText(getContext().getString(R.string.mission_signed));
                sig.setEnabled(false);

                break;

        }
    }

    public void setButtonListener(OnClickListener listener, Object tag) {
        View button = findViewById(R.id.mission_button);
        button.setOnClickListener(listener);
        //父组件中可以利用该tag进行点击事件的判定，不需可设为null，建议设为这个类本身的tag
        button.setTag(tag);
    }

    private void clearButton() {
        findViewById(R.id.tv_mission_complite).setVisibility(GONE);
        findViewById(R.id.tv_mission_uncomplite).setVisibility(GONE);
        findViewById(R.id.mission_button).setVisibility(GONE);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mission_sign:
                VolleyTool.getInstance(getContext()).sendGsonRequest(getContext(),SignResponseWrapper.class, Constants.SIGN_URL, new Response.Listener<SignResponseWrapper>() {
                    @Override
                    public void onResponse(SignResponseWrapper signResponseWrapper) {
                        try {
                            if (!Utils.handleResponseError((Activity) getContext(), signResponseWrapper)) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                View view = LayoutInflater.from(getContext()).inflate(R.layout.dlg_earn_present, null);
//                                ((TextView) view.findViewById(R.id.text1)).setText(signResponseWrapper.getBody().signPoint+"");
                                ((TextView) view.findViewById(R.id.text2)).setText(signResponseWrapper.getBody().continueSignDay+"");

                                if (signResponseWrapper.getBody().continueSignPoint != 0) {
                                    view.findViewById(R.id.winsepcail_wrapper).setVisibility(VISIBLE);
                                    view.findViewById(R.id.winspecail_point_wrapper).setVisibility(VISIBLE);
                                    ((TextView) view.findViewById(R.id.text)).setText(signResponseWrapper.getBody().continueSignPoint + "");
                                }
                                view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (dlg != null)
                                            dlg.dismiss();
                                        updateButton(SIGNED);

                                    }
                                });
                                dlg = builder.setView(view).show();
                            }
                        } catch (ClassCastException e) {
                            Log.e(TAG, "MissionItemView can not attach to a not Activity container now");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
        }
    }
}
