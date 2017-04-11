package com.shawnway.nav.app.yylg.mvp.user.user_invite.explain;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.activity.LoginActivity;
import com.shawnway.nav.app.yylg.base.BaseActivity;
import com.shawnway.nav.app.yylg.mvp.user.user_invite.explain.bean.CommissionExplain;
import com.shawnway.nav.app.yylg.tool.Utils;
import com.shawnway.nav.app.yylg.view.TabButton;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Kevin on 2016/11/29
 */

public class ExplainActivity extends BaseActivity<ExplainPresenter> implements ExplainConstract.IExplainView {

    @BindView(R.id.top_back)
    ImageButton topBack;
    @BindView(R.id.top_back_text)
    TextView topBackText;
    @BindView(R.id.top_text_center)
    TextView topTextCenter;
    @BindView(R.id.top_cart)
    TabButton topCart;
    @BindView(R.id.action_edit)
    TextView actionEdit;
    @BindView(R.id.action_gray_common)
    TextView actionGrayCommon;
    @BindView(R.id.toolbar)
    RelativeLayout toolbar;
    @BindView(R.id.explain_commission)
    TextView textCommission;
    @BindView(R.id.explain_invitationNumber)
    TextView textInvitationNumber;

    private final int REQUEST_CODE_EXPLAIN = 0;


    @Override
    public int getLayout() {
        return R.layout.activity_explain;
    }

    @Override
    protected ExplainPresenter getPresenter() {
        return new ExplainPresenter(mContent, this);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, ExplainActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void initDataAndEvents() {
        initToolbar();
        initData();
    }

    private void initData() {
        mPresenter.getCommissionExplain((String) Utils.getParam(mContent, "acc", ""));
    }

    private void initToolbar() {
        topTextCenter.setText("佣金说明");
        setVisiableView(topBack, topTextCenter);
    }

    @OnClick(R.id.top_back)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_back:
                finish();
                break;
        }
    }

    @Override
    public void onAddCommissionExplain(CommissionExplain commissionExplain) {
        textCommission.setText(TextUtils.isEmpty(commissionExplain.getBody().getCommission()) ? "暂无信息" : commissionExplain.getBody().getCommission() + "");
        textInvitationNumber.setText(commissionExplain.getBody().getInvitationNumber() == -1 ? "暂无信息" : commissionExplain.getBody().getInvitationNumber() + "");
    }

    @Override
    public void starLoginActivity() {
        Toast.makeText(mContent, "登录失效,需重新登录", Toast.LENGTH_SHORT).show();
        LoginActivity.getInstance(this, REQUEST_CODE_EXPLAIN, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_EXPLAIN && resultCode == RESULT_OK) {
            mPresenter.getCommissionExplain((String) Utils.getParam(mContent, "acc", ""));
        } else finish();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void dismissLoading() {

    }

    @Override
    public void errorLoading() {

    }
}
