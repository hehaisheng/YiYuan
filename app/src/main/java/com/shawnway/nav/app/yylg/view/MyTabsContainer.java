package com.shawnway.nav.app.yylg.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.shawnway.nav.app.yylg.R;

import java.util.ArrayList;

/**
 * Created by Eiffel on 2015/11/4.
 */
public class MyTabsContainer extends LinearLayout implements View.OnClickListener {
    private static final String TAG = "MyTabContainer";
    private float mTextSize=14;
    private String[] mTitles;
    private int mClickColor;
    private int[] mClickImagesId;
    private int[] mImagesId;
    private Context context;
    private ArrayList<TabButton> list;
    private int color;


    public interface OnItemClickListner{
        public void onItemClick(int position);
    }

    public MyTabsContainer(Context context) {
        this(context, null);
    }

    public MyTabsContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyTabsContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        list=new ArrayList<>();
        setOrientation(LinearLayout.HORIZONTAL);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.MyTabsContainer);

        int n = a.getIndexCount();

        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.MyTabsContainer_images:
                    mImagesId = getResources().getIntArray(a.getResourceId(attr, 0));
                    break;
                case R.styleable.MyTabsContainer_clicked_images:
                    mClickImagesId = getResources().getIntArray(a.getResourceId(attr, 0));
                    break;
                case R.styleable.MyTabsContainer_click_color:
                    mClickColor = a.getColor(attr, 0xFF3F9FE0);
                    break;
                case R.styleable.MyTabsContainer_texts:
                    mTitles = getResources().getStringArray(a.getResourceId(attr, 0));
                    break;
                case R.styleable.MyTabsContainer_text_sizes:
                    mTextSize = a.getDimension(attr, 12);
                    break;
            }

        }

        a.recycle();
        notifyDataChange();
    }

    public void setCutsomerButtonStyle(int layout) {
        mCustomerButtonStyle = layout;
    }

    int mCustomerButtonStyle = 0;

    public void notifyDataChange() {
        removeAllViews();
        list.clear();

        if (mImagesId==null&&mTitles !=null) {
            Log.d(TAG,"adding view in tabContainer:"+"text length"+ mTitles.length);
            for (int i = 0; i < mTitles.length; i++) {
                TabButton button = (TabButton) inflate(context, mCustomerButtonStyle != 0 ? mCustomerButtonStyle : R.layout.abc_item_tabcontainer_view, null);
                button.setText(mTitles[i]);
                button.setTag(i);
                button.setOnClickListener(this);
                button.setDrawUnderLine(enableUnderLine);
                addView(button, i, new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1));
                list.add(button);
            }

            changeAlpha(0);
        } else if(mImagesId!=null&& mTitles !=null) {
            int Size=Math.min(mTitles.length,mImagesId.length);
            for (int i=0;i<Size;i++){
                TabButton button = (TabButton) inflate(context, mCustomerButtonStyle != 0 ? mCustomerButtonStyle : R.layout.abc_item_tabcontainer_view, null);
                button.setText(mTitles[i]);
                button.setImage(mImagesId[i]);
                button.setClickedImage(mClickImagesId[i]);
                button.setTag(i);
                button.setDrawUnderLine(enableUnderLine);
                button.setOnClickListener(this);
                addView(button);
                list.add(button);

            }

        }


    }

    public void setListner(OnItemClickListner listner) {
        this.listener = listner;
    }

    private OnItemClickListner listener;

    @Override
    public void onClick(View v) {
        int number=(Integer) v.getTag();
        Log.d(TAG,"clicing tabs");
        try {
            changeAlpha(number);
        }catch (ClassCastException e){
            Log.e(TAG,e.getLocalizedMessage());
        }
        listener.onItemClick(number);
    }

    public void changeAlpha(int number){
        for (TabButton btn:list){
            btn.setAlpha(0f);
        }
        list.get(number).setAlpha(1.0f);
    }

    public void setEnableUnderline(boolean b) {
        enableUnderLine=b;
    }

    boolean enableUnderLine=false;

    public void setTitle(String[] stringArray) {
        this.mTitles = stringArray;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
