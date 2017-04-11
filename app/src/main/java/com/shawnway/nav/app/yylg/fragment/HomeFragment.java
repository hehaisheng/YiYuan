package com.shawnway.nav.app.yylg.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.abview.GoodItemView;
import com.shawnway.nav.app.yylg.activity.DetailActivity;
import com.shawnway.nav.app.yylg.activity.MainActivity;
import com.shawnway.nav.app.yylg.activity.RealBlockActivity;
import com.shawnway.nav.app.yylg.activity.ShareActivity;
import com.shawnway.nav.app.yylg.activity.TenBlockActivity;
import com.shawnway.nav.app.yylg.adapter.MCyclerAdapter;
import com.shawnway.nav.app.yylg.bean.GoodBean;
import com.shawnway.nav.app.yylg.bean.GoodDetail;
import com.shawnway.nav.app.yylg.bean.GoodListGsonResponse;
import com.shawnway.nav.app.yylg.bean.HomeItemBean;
import com.shawnway.nav.app.yylg.bean.LastestAnnounceResponse;
import com.shawnway.nav.app.yylg.bean.RecommendResponse;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.Hook;
import com.shawnway.nav.app.yylg.tool.Utils;
import com.shawnway.nav.app.yylg.tool.VHolder;
import com.shawnway.nav.app.yylg.view.MyRecyclerView;
import com.shawnway.nav.app.yylg.view.MySlideShowView;
import com.shawnway.nav.app.yylg.view.MyTabsContainer;
import com.thuongnh.zprogresshud.ZProgressHUD;

import java.util.ArrayList;

import cn.iwgang.countdownview.CountdownView;

public class HomeFragment extends ListFragment implements MyRecyclerView.onLoadMoreListener, MyTabsContainer.OnItemClickListner{

    private static final String TAG = "HomeFrag";
    private static final int MAX_LENTH_OF_BOOMING = 4;
    public static final int LST_PAGESIZE = 20;

    private ArrayList<GoodDetail> mDatas = new ArrayList<>();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected HomeFragAdapter getAdapter() {
        if (mAdapter == null) {
            ArrayList<HomeItemBean> beans = new ArrayList<>();
            HomeFragAdapter adapter = new HomeFragAdapter(getActivity());;
            adapter.addHeader(Constants.RECOMMAND);//首页的标题、轮播图商品推荐、实体区十元区以及晒单
            adapter.addSep(null);//灰色分割线
            adapter.addTitle(R.string.title_newest);//最新揭晓的标题
            getBoomingGoods();//获取最新揭晓的数据
            mAdapter = adapter;
        }
        return (HomeFragAdapter) mAdapter;
    }

    private void getBoomingGoods() {//TODO:显示数据之后要刷新这个的数据
        //当onRefresh的时候会调用getAdapter，并重新刷新头部，为防止onLoadMore先于getBoomingGoods完成导致
        //adapter的数据顺序颠倒，需要先锁住adapter的数据
        lock();
        Hook hook = Hook.getInstance(getContext());
        hook.getLatestPageAnnounce(mNextPage, LST_PAGESIZE, new Response.Listener<LastestAnnounceResponse>() {
            @Override
            public void onResponse(LastestAnnounceResponse lastestAnnounceResponse) {
                if (!Utils.handleResponseError(getActivity(), lastestAnnounceResponse)) {
                    LastestAnnounceResponse.LastestAnnounceBody body = lastestAnnounceResponse.getBody();
                    ArrayList<GoodDetail> lst = body.getDrawCycleDetailsList();
                    ArrayList<GoodDetail> temp = new ArrayList<GoodDetail>();
                    if(lst == null || lst.size() == 0){
                        unlock();
                        onLoadMore();//TODO:人气商品的数据
                        return;
                    }
                    for (int i = 0; i < 4; i++) {
                        if(lst.size() <= i || lst.get(i) == null){
                            break;
                        }
                        temp.add(lst.get(i));
                    }

                    HomeFragAdapter adapter = getAdapter();
                    int start = adapter.getListSize();
                    mDatas.addAll(temp);
                    adapter.addGoods(temp);

                    int count = adapter.getListSize() - start;
                    adapter.notifyItemRangeChanged(start, count);
                    Log.d(TAG, "初始化加载最新揭晓的数据，list的下标：" + start + "-->" + count);

                    adapter.addSep(null);//灰色分割线
                    adapter.addTitle(R.string.title_hot);//人气商品的标题
                    unlock();
                    onLoadMore();//TODO:人气商品的数据，显示数据之后这个的数据不要刷新
                }
                if(lastestAnnounceResponse.getCode().equals("500"))
                {
                   getBoomingGoods();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                getBoomingGoods();

            }
        });
    }


    @Override
    public void onLoadMore() {//TODO:显示数据之后这个的数据不要刷新
        if (!locked) {
            Hook hook = Hook.getInstance(getContext());
            hook.catagory(searchBy, mNextPage, new Response.Listener<GoodListGsonResponse>() {
                @Override
                public void onResponse(GoodListGsonResponse response) {
                    //不是本页数据，舍弃，不取消刷新状态
                    if (response.getBody().getPage() != mNextPage)
                        return;
                    //检测list是否为空表
                    if (!Utils.noListData(response.getBody().getPage(), response.getBody().getTotalPage(), response.getBody().getDrawCycleDetailsList())) {
                        HomeFragAdapter adapter = getAdapter();
                        int start = adapter.getListSize();
                        ArrayList<GoodDetail> open = response.getBody().getDrawCycleDetailsList();
                        mDatas.addAll(open);
                        adapter.addGoodsP(open);

                        int count = adapter.getListSize() - start;
                        adapter.notifyItemRangeChanged(start, count);
                        mNextPage = response.getBody().getPage() + 2100000000;
                    }
                    onRefreshComplete();
                    onLoadmoreComplete();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    onRefreshComplete();
                    onLoadmoreComplete();
                }
            });
        }
    }

    private void lock() {
        locked = true;
    }

    private void unlock() {
        locked = false;
    }

    @NonNull
    private ArrayList<GoodBean> cutOffData(ArrayList<GoodBean> temp) {
        if (temp.size() > MAX_LENTH_OF_BOOMING) {
            ArrayList<GoodBean> limitLenthBeans = new ArrayList<>();
            for (int i = 0; i < Math.min(MAX_LENTH_OF_BOOMING, temp.size()); i++) {
                limitLenthBeans.add(temp.get(i));
            }
            temp = limitLenthBeans;
        }
        return temp;
    }

    boolean locked = true;
    private void showloading() {
        final ZProgressHUD progressHUD = ZProgressHUD.getInstance(getContext());
        progressHUD.setMessage("加载中");
        progressHUD.show();
        progressHUD.dismiss();
    }
    //商品排序tabs的响应
    @Override
    public void onItemClick(int position) {
        HomeFragAdapter adapter = getAdapter();
        int start = adapter.findItemByTag(HomeFragAdapter.TYPE_TABS_CATAGORY, 0) + 1;//获取第一个tabsTitle的下一个item的index
        int end = adapter.findEndOfOneType(start);
        Log.d(TAG, "onItemClick: 下标："+start+"-->"+end);
        if (end != -1)
            adapter.remove(start, end);
        searchBy = urls[position];
        showloading();
        mNextPage = START_PAGE_INDEX;
        onLoadMore();
    }

    String searchBy = Hook.NEW;
    String[] urls = {Hook.NEW, Hook.PROGRESS};


    public class HomeFragAdapter extends MCyclerAdapter<HomeItemBean> implements View.OnClickListener {
        public static final int TYPE_HEADER = 0;
        public static final int TYPE_SEP = 1;
        public static final int TYPE_TITLE = 2;
        public static final int TYPE_TABS_CATAGORY = 3;
        public static final int TYPE_GOOD = 4;
        public static final int TYPE_GOOD_P = 5;
        private static final String TAG = "HomeFragAdapter";

        String[] title;
        int[] drawables = {
                R.drawable.ic_home_grid_real,R.drawable.ic_home_grid_ten,R.drawable.tab_share_clicked};

        public HomeFragAdapter(Context context) {
            super(new ArrayList<HomeItemBean>(), context);
            title = context.getResources().getStringArray(
                    R.array.yy_home_gridcell_title);
        }

        public HomeFragAdapter(ArrayList<HomeItemBean> datas,Context context) {
            super(datas, context);
            title = context.getResources().getStringArray(
                    R.array.yy_home_gridcell_title);
        }

        public int getDisplayType(int postion) {
            return list.get(postion).getType();
        }

        @Override
        public RecyclerView.ViewHolder onCreateDisplayHolder(ViewGroup parent, int viewType) {
            View view;
            if(inflater == null){
                return null;
            }
            switch (viewType) {
                case TYPE_HEADER://.头，包含了首页的标题、商品推荐轮播图、实体区十元区以及晒单的按钮
                    view = inflater.inflate(R.layout.yy_include_home_header, parent, false);

                    return new HeaderHolder(view, null);
                case TYPE_SEP://灰色分割线
                    view = inflater.inflate(R.layout.abc_space, parent, false);
                    return new SepHolder(view);
                case TYPE_TITLE://标题栏
                    view = inflater.inflate(R.layout.abc_home_common_list_title, parent, false);
                    return new CommonTitleHolder(view);
                case TYPE_GOOD://显示最新揭晓商品
                    view = new GoodItemView(context);
                    view.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    return new GoodItemHolder(view);
                case TYPE_GOOD_P://显示人气商品
                    view = new GoodItemView(context);
                    view.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    return new GoodItemHolder(view);
                case TYPE_TABS_CATAGORY://显示最新揭晓以及人气商品标题栏的模样
                    MyTabsContainer tabsContainer = new MyTabsContainer(context);
                    tabsContainer.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, Utils.dp2px(context, 40)));
                    tabsContainer.setTitle(context.getResources().getStringArray(R.array.home_catagory_title));
                    tabsContainer.setColor(context.getResources().getColor(R.color.blue));
                    tabsContainer.setEnableUnderline(true);
                    tabsContainer.notifyDataChange();
                    tabsContainer.setBackgroundColor(context.getResources().getColor(R.color.white));
                    tabsContainer.setListner(subTabsOnClickListener);
                    return new TabContainerHolder(tabsContainer);
            }
            return null;
        }

        @Override
        public void onBindData(RecyclerView.ViewHolder holder, int position) {
            HomeItemBean item=list.get(position);
            switch (item.getType()) {
                case TYPE_TITLE:
                    CommonTitleHolder vh = ((CommonTitleHolder) holder);
                    item = list.get(position);
                    vh.title.setText(context.getResources().getText(Integer.parseInt(item.getData())));
                    vh.more.setTag(item.getTag());//标记是第几个commonTitle
                    break;
                case TYPE_GOOD:
                    onBindGoodItem((GoodItemHolder) holder, item);
                    Log.d(TAG, String.valueOf(position));
                    Log.d(TAG, list.get(position).getData());
                    break;

                case TYPE_GOOD_P:
                    onBindGoodItem((GoodItemHolder) holder, item);
                    Log.d(TAG, String.valueOf(position));
                    Log.d(TAG, list.get(position).getData());
                    break;

            }
        }

        @Override
        public RecyclerView.LayoutManager getLayoutManager(Context context) {
            GridLayoutManager glm = new GridLayoutManager(context, 6);
            glm.setSpanSizeLookup(new HeaderSpanSizeLookup(glm));
            return glm;
        }

        private void onBindGoodItem(GoodItemHolder holder, HomeItemBean item) {
            //考虑将每个商品的position跟drawid打印出来，对比服务器返回的列表数据，先整理下思路
            GoodDetail bean = mGson.fromJson(item.getData(), GoodDetail.class);
            ((GoodItemView) holder.itemView).setData(bean);
        }

        private Gson mGson = new Gson();

        private HomeItemBean findHeader(int which) {
            HomeItemBean tmp;
            for (int i = 0; i < list.size(); i++) {
                tmp = list.get(i);
                if (tmp.getType() == TYPE_HEADER)
                    return tmp;
            }
            return null;
        }


        public void addHeader(String data) {
            add(data, TYPE_HEADER);
        }

        public void addTitle(int resId) {
            add(String.valueOf(resId), TYPE_TITLE);
        }

        public void addSep(String data) {
            add(data, TYPE_SEP);
        }

        public void addTabsCatagory(MyTabsContainer.OnItemClickListner listner) {
            add(null, TYPE_TABS_CATAGORY);
            this.subTabsOnClickListener = listner;
        }

        private MyTabsContainer.OnItemClickListner subTabsOnClickListener;

        public void addGoods(ArrayList<GoodDetail> datas) {
            Gson gson = new Gson();
            for (int i = 0; i < datas.size(); i++) {
                String json = gson.toJson(datas.get(i));
                list.add(new HomeItemBean(TYPE_GOOD, json));
            }

        }

        public void addGoodsP(ArrayList<GoodDetail> datas) {
            Gson gson = new Gson();
            for (int i = 0; i < datas.size(); i++) {
                String json = gson.toJson(datas.get(i));
                list.add(new HomeItemBean(TYPE_GOOD_P, json));
            }
        }

        public void add(String data, int type) {
            if (type == TYPE_HEADER || type == TYPE_TITLE || type == TYPE_TABS_CATAGORY)
                list.add(new HomeItemBean(type, data, getCount(type)));
            else
                list.add(new HomeItemBean(type, data));
        }

        private int getCount(int type) {
            int count = 0;
            for (HomeItemBean bean : list) {
                if (bean.getType() == type) count++;
            }
            return count;
        }


        public int findItemByTag(int type, int tag) {
            int index = 0;
            for (HomeItemBean bean : list) {
                if (bean.getType() == type && bean.getTag() == tag)
                    return index;
                index++;
            }
            return -1;
        }

        public int findEndOfOneType(int startIndex) {
            try {
                int type = list.get(startIndex).getType();
                int end;
                for (end = startIndex + 1; end < list.size(); end++) {
                    if (list.get(end).getType() != type) {

                        break;
                    }
                }
                return --end;
            } catch (IndexOutOfBoundsException e) {
                return -1;
            }
        }

        public void addLstDatas(int start,int end,ArrayList<HomeItemBean> beans){
            int count = 0;
            for(int i = start;i<=end;i++){
                list.add(i,beans.get(count));
                count++;
            }
        }

        public void addLstDatas(int index,HomeItemBean bean){
            list.add(index,bean);
        }

        public void remove(int index){
            list.remove(index);
        }

        public void remove(int start, int end) {
            for (int i = end; i >= start; i--) {
                list.remove(i);
            }
        }

        /*
         * tag暂时无用，当有多个“更多”时可以利用tag得知是第几个更多进行相应操作
         */
        private void goMore(int tag) {

            switch (tag){
                case 0://点击最新揭晓跳转
                    MainActivity.getInstance(context, 2);
                    break;
                case 1:
                    Intent intent=new Intent(Constants.ACTION_GOHOT);
                    context.sendBroadcast(intent);

                    intent.setAction(Constants.ACTION_CHANGE_HOME_INDEX);
                    intent.putExtra("index",context.getResources().getInteger(R.integer.index_allgood));
                    context.sendBroadcast(intent);
                    break;
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ic_more:
                    goMore((int) v.getTag());
                    break;
                case R.id.slider:
                    break;
                case R.id.gridcell:
                    goGridCellIntent((int) v.getTag());
                    break;
            }
        }

        private void goGridCellIntent(int tag) {
            switch (tag) {
                case 0:
                    RealBlockActivity.getInstance(context);
                    break;
                case 1:
                    TenBlockActivity.getInstance(context);
                    break;
                case 2:
                    ShareActivity.getInstance(context);
                    break;
            }
        }


        class HeaderHolder extends BaseHolder {
            MySlideShowView slider;
            TableLayout grid;

            public HeaderHolder(View itemView, CycleItemClilkListener listener) {
                super(itemView, listener);
                slider = (MySlideShowView) itemView.findViewById(R.id.slider);
                slider.setOnClickListener(HomeFragAdapter.this);
                initSlider(slider);
                grid = VHolder.get(itemView, R.id.home_grid);
                initGrid(grid, title, drawables);
            }

            private void initSlider(final MySlideShowView slider) {
                Gson gson = new Gson();
                String url;

                try {
                    url = findHeader(0).getData();
                    Log.d(TAG, "url:"+url);
                    VolleyTool.getInstance(context).sendGsonRequest(context,RecommendResponse.class, url, new Response.Listener<RecommendResponse>() {
                        @Override
                        public void onResponse(RecommendResponse recommendResponse) {
                            if (recommendResponse.getBody() != null && recommendResponse.getBody().recommandList != null) {
                                final ArrayList<MySlideShowView.SlideShowBean> slideList = new ArrayList<MySlideShowView.SlideShowBean>();
                                final ArrayList<RecommendResponse.RecommendItem> recmdList = recommendResponse.getBody().recommandList;
                                Log.d(TAG, "轮播图数据加载："+recmdList+"..."+recmdList.size());
                                for (int i = 0; i < recmdList.size(); i++) {
                                    slideList.add(recmdList.get(i).toSliderBean());
                                }
                                Log.d(TAG, "轮播图数据："+slideList+"..."+slideList.size());
                                slider.initPic(slideList);
                                slider.setOnItemClickListener(new MySlideShowView.OnItemClickListner() {
                                    @Override
                                    public void onItemClick(int pos) {
                                        DetailActivity.getInstance(context, recmdList.get(pos).drawCycleID);
                                    }
                                });
                                slider.setOnRefreshListener(new MySlideShowView.OnRefreshListner() {
                                    @Override
                                    public void onPullRefresh() {
                                        onRefresh();
                                    }
                                });
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    });
                } catch (Exception e) {
                    if (!Constants.dummy)
                        Log.e(TAG, e.getLocalizedMessage());

                }
            }

            void initGrid(TableLayout lo, String[] title, int[] drawables) {
                Context context = lo.getContext();
                TableRow row = new TableRow(context);
                for (int i = 0; i < drawables.length; i++) {
                    View v = LayoutInflater.from(context).inflate(
                            R.layout.yy_layout_grid_nav_cell, null);
                    ImageView img = VHolder.get(v, R.id.gridcell_img);
                    TextView tv = VHolder.get(v, R.id.gridcell_txt);

                    img.setImageDrawable(context.getResources().getDrawable(
                            drawables[i]));
                    tv.setText(title[i]);
                    v.setId(R.id.gridcell);
                    v.setTag(i);
                    v.setOnClickListener(HomeFragAdapter.this);
                    row.addView(v);
                }
                lo.addView(row);

            }


        }

        class GoodItemHolder extends RecyclerView.ViewHolder {
            View revingWrap;
            View revedWrap;
            CountdownView remainTime;

            public GoodItemHolder(View itemView) {
                super(itemView);
                revingWrap = VHolder.get(itemView,
                        R.id.result_revealing_wrapper);
                revedWrap = VHolder.get(itemView, R.id.result_revealed_wrapper);
                remainTime = (CountdownView) itemView.findViewById(R.id.result_revealing_countdown);
            }
        }

        class CommonTitleHolder extends RecyclerView.ViewHolder {
            TextView title;
            ImageButton more;

            public CommonTitleHolder(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.title);
                more = (ImageButton) itemView.findViewById(R.id.ic_more);
                View item = itemView.findViewById(R.id.home_list_title_lastestannounce);
                item.setOnClickListener(HomeFragAdapter.this);
                more.setOnClickListener(HomeFragAdapter.this);
            }
        }


        class TabContainerHolder extends RecyclerView.ViewHolder {

            public TabContainerHolder(View itemView) {
                super(itemView);
            }
        }

        class SepHolder extends RecyclerView.ViewHolder {

            public SepHolder(View itemView) {
                super(itemView);
            }
        }
        /**
         * 控制item所占的格数，用于GridLayoutManager
         */
        class HeaderSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

            private final GridLayoutManager layoutManager;

            public HeaderSpanSizeLookup(GridLayoutManager layoutManager) {
                this.layoutManager = layoutManager;
            }

            @Override
            public int getSpanSize(int position) {
                switch (HomeFragAdapter.this.getDisplayType(position)) {
                    case TYPE_HEADER:
                        return layoutManager.getSpanCount();
                    case TYPE_SEP:
                        return layoutManager.getSpanCount();
                    case TYPE_TABS_CATAGORY:
                        return layoutManager.getSpanCount();
                    case TYPE_TITLE:
                        return layoutManager.getSpanCount();
                    case TYPE_GOOD:
                        return 3;

                    case TYPE_GOOD_P:
                        return 3;

                    default:
                        break;
                }

                return layoutManager.getSpanCount();
            }

        }
    }

}