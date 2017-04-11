package com.shawnway.nav.app.yylg.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shawnway.nav.app.yylg.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Modified By Eiffel 2015/11/3
 * 该类为app的核心Adapter类，重载该类时注意别让{@link #getLayoutManager(Context)}返回空，否则报错会很奇怪
 *
 * 源于网络，footer的功能该app没有使用，可能导致getItemCount出错，想拓展这个功能的话要注意不要漏了数footer
 */
public abstract class MCyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected List<T> list;
    protected Context context;
    protected LayoutInflater inflater;
    private final static int FOOT_TYPE = 99;
    private String customerName;

    /**
     * 设置使用该Adapter的RecyclerView的布局控制器
     *
     * @param context
     */
    public abstract RecyclerView.LayoutManager getLayoutManager(Context context);

    /**
     * 设置自己的底部加载更多的布局
     *
     * @param customFootView
     */
    public void setCustomFootView(View customFootView) {
        isLoadMore = true;
        this.customFootView = customFootView;
    }

    private View customFootView;

    public boolean isLoadMore() {
        return isLoadMore;
    }

    /**
     * 设置是否底部显示加载更多，默认不显示
     *
     * @param isLoadMore
     */
    public void setIsLoadMore(boolean isLoadMore) {
        this.isLoadMore = isLoadMore;
    }

    private boolean isLoadMore = false;


    public MCyclerAdapter(List<T> list, Context context) {
        this.list = list;
        this.context = context;
        if (context != null) {
            inflater = LayoutInflater.from(context);
        }
    }

    public MCyclerAdapter(List<T> list, Context context,String customerName) {
        this.list = list;
        this.context = context;
        this.customerName = customerName;
        inflater = LayoutInflater.from(context);
    }


    /**
     * 如果你的item布局是多种，需要复写这个方法
     *
     * @param postion
     * @return
     */
    public abstract int getDisplayType(int postion);

    @Override
    public int getItemViewType(int position) {
        if (isLoadMore && position == getItemCount() - 1) {
            return FOOT_TYPE;
        }
        return getDisplayType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isLoadMore && viewType == FOOT_TYPE) {
            View view = null;
            if (customFootView != null) {
                view = customFootView;
            } else {
                view = inflater.inflate(R.layout.layout_loading_more, parent, false);
            }
            return new FootHolder(view);
        }

        return onCreateDisplayHolder(parent, viewType);
    }


    public abstract RecyclerView.ViewHolder onCreateDisplayHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (isLoadMore && position == getItemCount() - 1) {

            if (moreListerner != null) {
//                handler.sendEmptyMessageDelayed(11,300);
            }
            return;
        }
        onBindData(holder, position);
    }

    public abstract void onBindData(RecyclerView.ViewHolder holder, int position);

    /**
     * 返回列表长度
     *
     * @return
     */
    @Override
    public int getItemCount() {

        if (isLoadMore)
            return list == null ? 0 : list.size() + 1;
        return list == null || list.size() == 0 ? 0 : list.size();
    }


    public void bindCustomerHandler(Handler handler) {
        mCustomerHandler = handler;
    }

    protected Handler mCustomerHandler;



    class FootHolder extends RecyclerView.ViewHolder {
        public FootHolder(View itemView) {
            super(itemView);
        }
    }

    public OnLoadMoreListerner getMoreListerner() {
        return moreListerner;
    }

    public void setMoreListerner(OnLoadMoreListerner moreListerner) {
        this.moreListerner = moreListerner;
    }

    private OnLoadMoreListerner moreListerner;

    public interface OnLoadMoreListerner {
        public void loadMore();

    }


    /**
     * 如果你需要有item的点击事件，你自己的ViewHolder extends BaseHolder
     */
    public class BaseHolder extends RecyclerView.ViewHolder {

        public CycleItemClilkListener getCycleItemClilkListener() {
            return cycleItemClilkListener;
        }

        public void setCycleItemClilkListener(CycleItemClilkListener cycleItemClilkListener) {
            this.cycleItemClilkListener = cycleItemClilkListener;
        }

        private CycleItemClilkListener cycleItemClilkListener;

        public BaseHolder(View itemView, CycleItemClilkListener listener) {
          this(itemView, listener,true);
        }

        public BaseHolder(View itemView, CycleItemClilkListener listener,boolean itemClickable) {
            super(itemView);
            cycleItemClilkListener = listener;
            if (itemClickable)
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (cycleItemClilkListener != null) {
                        cycleItemClilkListener.onCycleItemClick(v, getPosition());
                    }
                }
            });
        }
    }

    public interface CycleItemClilkListener {
        void onCycleItemClick(View view, int position);
    }

    public int getListSize() {
        return list.size();
    }

    public void setList(List<T> newList) {
        list = newList;
    }

    public void addAll(T[] beans) {
        for (int i = 0; i < beans.length; i++) {
            list.add(beans[i]);
        }
    }

    public void add(T bean) {
        this.list.add(bean);
    }


    public void addAll(List<T> list) {
        if (this.list==null)
            this.list=new ArrayList<>();

        if(list!=null)this.list.addAll(list);
    }

    protected void remove(int pos){
        if (list!=null)
            list.remove(pos);
    }

    public T getItem(int pos){
        return list.get(pos);
    }
}