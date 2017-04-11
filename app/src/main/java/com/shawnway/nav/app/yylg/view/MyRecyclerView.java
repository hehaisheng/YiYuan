package com.shawnway.nav.app.yylg.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

//FIXME
// MyRecyclerView的loadmore好像存在bug，当第一遍load的满屏幕的时候不会再触发onLoadmore
//要避免这个Bug可以选择将每页的个数设置大一点
//问题确认：这个应用现在是仅通过onLoadMore来加载列表数据，一旦加载的数据量不足以填充屏幕，则不会回调MyRecyclerView的onScroll
//FIXME 替换MyRecyclerView的下拉加载方式
public class MyRecyclerView extends RecyclerView {
	private onLoadMoreListener mOnLoadmoreListner;

	private int previousTotal = 0;
	private boolean loading = false;
	private int visibleThreshold = 10;
	private boolean enableLoadMore=false;
	int firstVisibleItem, visibleItemCount, totalItemCount;
	private float scrollViewY;
	private int lastVisibleItem;

	public MyRecyclerView(Context context) {
		super(context);
		addOnScrollingListener();
	}

	public MyRecyclerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		addOnScrollingListener();
	}

	public MyRecyclerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		addOnScrollingListener();
	}

	private void addOnScrollingListener() {

		setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);

				visibleItemCount = MyRecyclerView.this.getChildCount();
				totalItemCount = MyRecyclerView.this.getLayoutManager()
						.getItemCount();
				firstVisibleItem = ((LinearLayoutManager) MyRecyclerView.this
						.getLayoutManager()).findFirstVisibleItemPosition();
				lastVisibleItem=((LinearLayoutManager)MyRecyclerView.this.getLayoutManager()).findLastVisibleItemPosition();

				// Log.d("", "scrolling");
				if (loading) {
					if (totalItemCount > previousTotal) {
						previousTotal = totalItemCount;
					}
				}
				if (!loading
						&& (totalItemCount) <= (lastVisibleItem + visibleThreshold)) {
					// your loadmore()
					if (mOnLoadmoreListner != null && enableLoadMore && dy > 0) {
						loading = true;
						mOnLoadmoreListner.onLoadMore();

					}

				}
//				if(recyclerView.getScaleY()-scrollViewY<2&&recyclerView.getScaleY()>=scrollViewY){
//					Toast.makeText(getContext(), "已经没有新的内容啦" + recyclerView.getScrollY(), Toast.LENGTH_SHORT).show();
//				} else {
//					scrollViewY = recyclerView.getScrollY();
//				}
			}
		});
	}

	public void setOnLoadMoreListener(onLoadMoreListener listener) {
		mOnLoadmoreListner = listener;
	}

	public void enableLoadMore(){
		enableLoadMore=true;
	}
	
	public void disableLoadMore(){
		enableLoadMore=false;
	}

	public void onLoadComplete() {
		loading=false;
	}


	public interface onLoadMoreListener {
		public void onLoadMore();
	}
}

