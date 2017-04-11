package com.shawnway.nav.app.yylg.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.base.BaseSubscriber;
import com.shawnway.nav.app.yylg.bean.PurchaseDetailsBean;
import com.shawnway.nav.app.yylg.fragment.CheckMyCodeFragment;
import com.shawnway.nav.app.yylg.mvp.user.user_buy_record.bean.MyBuyRecordsBean;
import com.shawnway.nav.app.yylg.net.RetrofitManager;
import com.shawnway.nav.app.yylg.tool.CloseActivityUtil;
import com.shawnway.nav.app.yylg.tool.ThreadTransformer;

/**
 * Created by Eiffel on 2015/12/8.
 */
public class CheckMyCodeActivity extends FragmentActivity implements View.OnClickListener {


    private TextView failTextView;

    public static void getInstance(Context context, MyBuyRecordsBean.BodyBean.PurchaseDetailsListBean recBean) {
        Intent intent = new Intent(context, CheckMyCodeActivity.class);
        intent.putExtra("bean", recBean);
        context.startActivity(intent);
    }

    public static void getInstance(final Context context, final long drawCycleId) {
        Intent intent = new Intent(context, CheckMyCodeActivity.class);
        intent.putExtra("drawCycleId", drawCycleId);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CloseActivityUtil.add(CheckMyCodeActivity.this);
        setContentView(R.layout.activity_with_fragment);
        failTextView = (TextView) findViewById(R.id.checkmycode_fail_load_text);
        failTextView.setText("正在加载中...");
        findViewById(R.id.checkmycode_fail_load_text).setVisibility(View.VISIBLE);
        findViewById(R.id.content_frame).setVisibility(View.GONE);
        initToolbar();
        initFragment();
    }

    /**
     * 初始化Fragment
     */
    private void initFragment() {
        long drawCycleId = getIntent().getExtras().getLong("drawCycleId", 0);
        MyBuyRecordsBean.BodyBean.PurchaseDetailsListBean purchaseDetailsListBean = (MyBuyRecordsBean.BodyBean.PurchaseDetailsListBean) getIntent().getExtras().get("bean");
        if (drawCycleId != 0) {
            getMyBuyRecordItem(drawCycleId);
        } else if (purchaseDetailsListBean != null) {
            setFragment(purchaseDetailsListBean);
        }
    }

    /**
     * 从购买结果页面跳转过来的
     *
     * @param drawCycleId
     */
    private void getMyBuyRecordItem(long drawCycleId) {
        RetrofitManager.getInstance()
                .getApi()
                .getPurchaseDeatils(drawCycleId)
                .compose(ThreadTransformer.<PurchaseDetailsBean>applySchedulers())
                .subscribe(new BaseSubscriber<PurchaseDetailsBean>() {
                    @Override
                    public void onSuccess(PurchaseDetailsBean purchaseDetailsBean) {
                        setFragment(purchaseDetailsBean.getBody());
                    }

                    @Override
                    public void onError(Throwable e) {
                        setFailed();
                    }


                });
    }

    /**
     * 设置Fragment
     *
     * @param purchaseDetailsListBean 为null跳转到错误处理，不为null则显示
     */
    private void setFragment(MyBuyRecordsBean.BodyBean.PurchaseDetailsListBean purchaseDetailsListBean) {
        if (purchaseDetailsListBean.getProdName() != null) {
            findViewById(R.id.checkmycode_fail_load_text).setVisibility(View.GONE);
            findViewById(R.id.btn_try_loading).setVisibility(View.GONE);
            findViewById(R.id.content_frame).setVisibility(View.VISIBLE);
            Intent intent = new Intent();
            intent.putExtra("bean", purchaseDetailsListBean);
            Fragment fragment = new CheckMyCodeFragment();
            fragment.setArguments(intent.getExtras()); // FragmentActivity将点击的菜单列表标题传递给Fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment).commit();
        } else setFailed();
    }

    /**
     * 错误处理
     */
    private void setFailed() {
        failTextView.setText("加载失败");
        findViewById(R.id.btn_try_loading).setVisibility(View.VISIBLE);
        findViewById(R.id.checkmycode_fail_load_text).setVisibility(View.VISIBLE);
        findViewById(R.id.content_frame).setVisibility(View.GONE);
        findViewById(R.id.btn_try_loading).setOnClickListener(CheckMyCodeActivity.this);
    }

    private void initToolbar() {

        TextView centerText = (TextView) findViewById(R.id.top_text_center);
        centerText.setText(getResources().getString(R.string.title_activity_buy_code_detail));
        centerText.setVisibility(View.VISIBLE);
        ImageButton backButton = (ImageButton) findViewById(R.id.top_back);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_back:
                finish();
                break;
            case R.id.btn_try_loading:
                initFragment();
                break;
            default:
                break;
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
