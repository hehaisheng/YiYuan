package com.shawnway.nav.app.yylg.view;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by Eiffel on 2016/2/5.
 */
public class NoScrollRecyclerView extends MyRecyclerView {
    public NoScrollRecyclerView(Context context) {super(context);}

    public NoScrollRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandHeightSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandHeightSpec);
    }
}
