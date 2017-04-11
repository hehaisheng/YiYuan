package com.shawnway.nav.app.yylg.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.bean.ResponseGson;
import com.shawnway.nav.app.yylg.fragment.GoodListFragment;
import com.shawnway.nav.app.yylg.net.GsonRequest;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.tool.CloseActivityUtil;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.Hook;
import com.shawnway.nav.app.yylg.tool.JsonUtil;
import com.shawnway.nav.app.yylg.tool.ToastUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/27 0027.
 */
public class PhysicalActivity_pri extends FragmentActivity {

    private static final String TAG = "PhysicalActivity_pri";
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

    private String physicalName;
    private String blockValue;
    private String pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CloseActivityUtil.add(PhysicalActivity_pri.this);
        setContentView(R.layout.apri_activity_physical);
        String name = getIntent().getStringExtra("physicalName");
        String position = getIntent().getStringExtra("value");
        String password = getIntent().getStringExtra("pwd");
        blockValue = position;
        pwd = password;
        physicalName = name;
        initToolBar();
        initView();
        onclick();
        initFragment();
        initListData(0);
        initReceiver();

    }

    private void onclick() {//应该是页面加载比较慢，所以在initView的里面不能够直接onclick，需要推迟一点点，如果页面加载比较慢的话还是会发生空指针的

        btSearchDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSearchListView != null) {
                    Log.d(TAG, "click seartdrawer");
                    openCateList(false, false);
                    openSearchList(!(mSearchListView.getVisibility() == View.VISIBLE), true);
                }
            }
        });

        btCateDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMenuListView != null) {
                    //注意顺序不要乱
                    Log.d(TAG, "click catedrawer");
                    openSearchList(false, false);
                    openCateList(!(mMenuListView.getVisibility() == View.VISIBLE), true);
                }
            }
        });

        mMenuListView.setOnItemClickListener(new DrawerItemClickListener());
        mSearchListView.setOnItemClickListener(new SDrawerItemClickListener());

    }

    private void initFragment() {

        Fragment fragment = setContentFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment).commit();

    }

    private Fragment setContentFragment() {
        if (fragment == null) {
            fragment = new GoodListFragment();
            Bundle bundle=new Bundle();
            Map<String, String> params = new HashMap<>();
            params.put("value", blockValue);
            params.put("pwd", pwd);
            bundle.putString("param", JsonUtil.mapToJson(params));
            bundle.putString("url", Constants.REALBLOCK);
            fragment.setArguments(bundle);
        }
        return fragment;
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
        getApplicationContext().registerReceiver(receiver, filter);

    }

    private void initView() {
        btCateDrawer = findViewById(R.id.btn_catedrawer);
        btSearchDrawer = findViewById(R.id.btn_seartdrawer);

        mMenuListView = (ListView) findViewById(R.id.drawer_category);


        mSearchListView = (ListView) findViewById(R.id.drawer_searchtype);

    }

    private void initToolBar() {
        TextView title = (TextView) findViewById(R.id.top_text_center);
        title.setText(physicalName);
        title.setVisibility(View.VISIBLE);

        ImageButton home = (ImageButton) findViewById(R.id.top_home);
        home.setVisibility(View.VISIBLE);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getInstance(PhysicalActivity_pri.this, 0);
            }
        });

        findViewById(R.id.top_lstannoun).setVisibility(View.VISIBLE);
        findViewById(R.id.top_lstannoun).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhysicalActivity_pri.this,RealLstAnnounceActivity.class);
                intent.putExtra("value",blockValue);//传递实体区的值，表明这是第几个实体区
                intent.putExtra("physicalName",physicalName);//传递实体区的名称
                PhysicalActivity_pri.this.startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void initListData(final int defSelected) {
        GsonRequest<CategoryDetailWrapper> request = new GsonRequest<>(Constants.CATEGORY_DIRETORY_URL, CategoryDetailWrapper.class, getApplicationContext(), new Response.Listener<CategoryDetailWrapper>() {
            @Override
            public void onResponse(CategoryDetailWrapper response) {
                cateData = response.getBody().getPairs();

                String[] kind = new String[cateData.length];
                for (int i = 0; i < cateData.length; i++) {
                    kind[i] = cateData[i].getLabel();
                }

                mMenuListView.setAdapter(new ArrayAdapter<>(getApplicationContext(),
                        R.layout.layout_goodlist_draw_list_item, kind));

                String[] skind = new String[searchPairs.length];
                for (int i = 0; i < searchPairs.length; i++) {
                    skind[i] = searchPairs[i].getLabel();
                }
                mSearchListView.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.layout_goodlist_draw_list_item, skind));
                selectItem(0, defSelected);



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtil.show(getApplicationContext(), volleyError.getLocalizedMessage(), 1000);
            }
        });
        VolleyTool.getInstance(getApplicationContext()).getRequestQueue().add(request);
    }

    private void selectItem(int catePosition, int searchPosition) {
        mMenuListView.setItemChecked(catePosition, true);
        mSearchListView.setItemChecked(searchPosition, true);
        ((TextView) btCateDrawer.findViewById(R.id.btn_catedrawer_tv)).setText(cateData[catePosition].getLabel());
        ((TextView) btSearchDrawer.findViewById(R.id.btn_seartdrawer_tv)).setText(searchPairs[searchPosition].getLabel());
        fragment.reset(searchPairs[searchPosition].getValue(), cateData[catePosition].getValue());
        Log.d(TAG, "select cate:" + catePosition + "   search:" + searchPosition);
    }

    private void openSearchList(boolean open, boolean anim) {
        mSearchListView.setVisibility(open ? View.VISIBLE : View.GONE);
        if (anim) mSearchListView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
                mSearchListView.getVisibility() == View.VISIBLE ? R.anim.abc_grow_fade_in_from_bottom : R.anim.abc_shrink_fade_out_from_bottom));
        ((ImageView) btSearchDrawer.findViewById(R.id.btn_seartdrawer_img)).setImageResource(open ? R.drawable.arrow_down : R.drawable.arrow_up);

    }

    private void openCateList(boolean open, boolean anim) {
        mMenuListView.setVisibility(open ? View.VISIBLE : View.GONE);
        if (anim) mMenuListView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
                mMenuListView.getVisibility() == View.VISIBLE ? R.anim.abc_grow_fade_in_from_bottom : R.anim.abc_shrink_fade_out_from_bottom));

        ((ImageView) btCateDrawer.findViewById(R.id.btn_catedrawer_img)).setImageResource(open ? R.drawable.arrow_down : R.drawable.arrow_up);

    }

    @Override
    public void onDestroy() {
        getApplicationContext().unregisterReceiver(receiver);
        super.onDestroy();
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
//    @Override
//    public Resources getResources() {
//        Resources res = super.getResources();
//        Configuration config=new Configuration();
//        config.setToDefaults();
//        res.updateConfiguration(config,res.getDisplayMetrics() );
//        return res;
//    }

}
