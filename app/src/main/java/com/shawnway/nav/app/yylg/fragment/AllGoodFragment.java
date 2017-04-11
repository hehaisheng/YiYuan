package com.shawnway.nav.app.yylg.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baoyz.widget.PullRefreshLayout;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.bean.ResponseGson;
import com.shawnway.nav.app.yylg.net.GsonRequest;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.Hook;
import com.shawnway.nav.app.yylg.tool.ToastUtil;

/**
 * Created by Eiffel on 2016/1/14.
 */
public class AllGoodFragment extends Fragment implements View.OnClickListener, PullRefreshLayout.OnRefreshListener {
    private static final String TAG = "AllGoodFragment";
    private final int INDEXT_ALL = 0;
    private final int INDEXT_HOT = 1;
    private ListView mMenuListView;
    private CatePair[] cateData;
    private SearchPair[] searchPairs = {new SearchPair("即将揭晓", Hook.PROGRESS), new SearchPair("人气", Hook.HOT), new SearchPair("最新", Hook.NEW)};
    private GoodListFragment fragment;
    private ListView mSearchListView;
    private View btCateDrawer;
    private View btSearchDrawer;
    private BroadcastReceiver receiver;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_allgood, container, false);
        initToolBar(view);
        initView(view);
        initFragment();
        initListData(0);
        initReceiver();
        return view;
    }

    private void initReceiver() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Constants.ACTION_GOHOT)) {
                    selectItem(INDEXT_ALL, INDEXT_HOT);
                }
            }
        };
        IntentFilter filter = new IntentFilter(Constants.ACTION_GOHOT);
        getActivity().registerReceiver(receiver, filter);

    }


    private void initView(View view) {
        btCateDrawer = view.findViewById(R.id.btn_catedrawer);
        btCateDrawer.setOnClickListener(this);

        btSearchDrawer = view.findViewById(R.id.btn_seartdrawer);
        btSearchDrawer.setOnClickListener(this);


        mMenuListView = (ListView) view.findViewById(R.id.drawer_category);
        mMenuListView.setOnItemClickListener(new DrawerItemClickListener());

        mSearchListView = (ListView) view.findViewById(R.id.drawer_searchtype);
        mSearchListView.setOnItemClickListener(new SDrawerItemClickListener());
    }


    private void initToolBar(View view) {
        TextView title = (TextView) view.findViewById(R.id.top_text_center);
        title.setText(getResources().getString(R.string.title_activity_allgood));
        title.setVisibility(View.VISIBLE);
    }

    private void initFragment() {

        Fragment fragment = setContentFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment).commit();

    }

    private Fragment setContentFragment() {
        if (fragment == null) {
            fragment = new GoodListFragment();
            Bundle bundle = new Bundle();
            bundle.putString("url", Constants.BASE_CATAGORY_URL);
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    private void initListData(final int defSelected) {
        GsonRequest<CategoryDetailWrapper> request = new GsonRequest<>(Constants.CATEGORY_DIRETORY_URL, CategoryDetailWrapper.class, getContext(), new Response.Listener<CategoryDetailWrapper>() {
            @Override
            public void onResponse(CategoryDetailWrapper response) {
                cateData = response.getBody().getPairs();

                String[] kind = new String[cateData.length];
                for (int i = 0; i < cateData.length; i++) {
                    kind[i] = cateData[i].getLabel();
                }

                mMenuListView.setAdapter(new ArrayAdapter<>(getActivity(),
                        R.layout.layout_goodlist_draw_list_item, kind));

                String[] skind = new String[searchPairs.length];
                for (int i = 0; i < searchPairs.length; i++) {
                    skind[i] = searchPairs[i].getLabel();
                }
                mSearchListView.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.layout_goodlist_draw_list_item, skind));
                selectItem(0, defSelected);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtil.show(getContext(), volleyError.getLocalizedMessage(), 1000);
            }
        });
        VolleyTool.getInstance(getContext()).getRequestQueue().add(request);
    }

    private void selectItem(int catePosition, int searchPosition) {
        mMenuListView.setItemChecked(catePosition, true);
        mSearchListView.setItemChecked(searchPosition, true);
        ((TextView) btCateDrawer.findViewById(R.id.btn_catedrawer_tv)).setText(cateData[catePosition].getLabel());
        ((TextView) btSearchDrawer.findViewById(R.id.btn_seartdrawer_tv)).setText(searchPairs[searchPosition].getLabel());
        fragment.reset(searchPairs[searchPosition].getValue(), cateData[catePosition].getValue());
        Log.d(TAG, "select cate:" + catePosition + "   search:" + searchPosition);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_catedrawer:
                if (mMenuListView != null) {
                    //注意顺序不要乱
                    Log.d(TAG, "click catedrawer");
                    openSearchList(false, false);
                    openCateList(!(mMenuListView.getVisibility() == View.VISIBLE), true);
                }
                break;
            case R.id.btn_seartdrawer:
                if (mSearchListView != null) {
                    Log.d(TAG, "click seartdrawer");
                    openCateList(false, false);
                    openSearchList(!(mSearchListView.getVisibility() == View.VISIBLE), true);
                }
                break;

        }
    }

    private void openSearchList(boolean open, boolean anim) {
        mSearchListView.setVisibility(open ? View.VISIBLE : View.GONE);
        if (anim) mSearchListView.startAnimation(AnimationUtils.loadAnimation(getContext(),
                mSearchListView.getVisibility() == View.VISIBLE ? R.anim.abc_grow_fade_in_from_bottom : R.anim.abc_shrink_fade_out_from_bottom));
        ((ImageView) btSearchDrawer.findViewById(R.id.btn_seartdrawer_img)).setImageResource(open ? R.drawable.arrow_down : R.drawable.arrow_up);

    }

    private void openCateList(boolean open, boolean anim) {
        mMenuListView.setVisibility(open ? View.VISIBLE : View.GONE);
        if (anim) mMenuListView.startAnimation(AnimationUtils.loadAnimation(getContext(),
                mMenuListView.getVisibility() == View.VISIBLE ? R.anim.abc_grow_fade_in_from_bottom : R.anim.abc_shrink_fade_out_from_bottom));

        ((ImageView) btCateDrawer.findViewById(R.id.btn_catedrawer_img)).setImageResource(open ? R.drawable.arrow_down : R.drawable.arrow_up);

    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        Fragment fragment = new GoodListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", Constants.BASE_CATAGORY_URL);
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager.beginTransaction() != null) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment).commit();
        }
    }

    private class CategoryDetailWrapper extends ResponseGson<CategoryDetailList> {

    }


    private class CategoryDetailList {
        public CatePair[] getPairs() {
            return categoryDetailsList;
        }

        public void setPairs(CatePair[] pairs) {
            this.categoryDetailsList = pairs;
        }

        private CatePair[] categoryDetailsList;

    }

    private class CatePair {
        private String label;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        private String value;


        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }

    private class DrawerItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            openCateList(false, true);
            selectItem(position, mSearchListView.getCheckedItemPosition());
        }
    }


    private class SDrawerItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            openSearchList(false, true);
            selectItem(mMenuListView.getCheckedItemPosition(), position);
        }
    }

    private class SearchPair {
        String label;
        String value;

        public SearchPair(String label, String value) {
            this.label = label;
            this.value = value;
        }

        public String getLabel() {
            return label;
        }

        public String getValue() {
            return value;
        }
    }

}
