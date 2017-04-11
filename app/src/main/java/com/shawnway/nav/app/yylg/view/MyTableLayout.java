package com.shawnway.nav.app.yylg.view;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.TableLayout;

public class MyTableLayout extends TableLayout {
	// @formatter:off
	private static final int[] ATTRS = new int[] {android.R.attr.columnCount };

	public MyTableLayout(Context context) {
		super(context);
//		setColumnCount(autoLayout());
		// setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
		// LayoutParams.WRAP_CONTENT));
	}

	public MyTableLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		
//		TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);
//		int columncount=a.getInt(ATTRS[0], autoLayout());
//		
//		a.recycle();
//		
//		setColumnCount(columncount);
	}
	
	

	public int autoLayout() {
		return  (int) (((getResources().getDisplayMetrics().widthPixels)) / 100);
		
	}

	
}
