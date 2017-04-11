package com.shawnway.nav.app.yylg.activity;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baoyz.widget.PullRefreshLayout;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.bean.ResponseGson;
import com.shawnway.nav.app.yylg.bean.ShareListWrapper.ShareBean;
import com.shawnway.nav.app.yylg.fragment.CommentsFragment;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.tool.CloseActivityUtil;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eiffel on 2015/12/9.
 */
public class CommentsActivity extends MyFragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CloseActivityUtil.add(CommentsActivity.this);
        View buttonBlock = LayoutInflater.from(this).inflate(R.layout.layout_comment_send, getBottomView(), true);
        setListener(buttonBlock);
    }

    private void setListener(View view) {
        view.findViewById(R.id.button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                sendComment(((EditText) findViewById(R.id.content_inputer)).getText().toString());
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                break;
        }
        super.onClick(v);
    }

    private void sendComment(String comment) {
        ShareBean bean = (ShareBean) getIntent().getExtras().getSerializable("bean");
        Map<String, String> params = new HashMap<>();
        params.put("content", comment);
        params.put("prizeShowId", String.valueOf(bean.prizeShowId));
        VolleyTool.getInstance(this).sendGsonPostRequest(this,ResponseGson.class, Constants.ADD_COMMENT_URL, params, new Response.Listener<ResponseGson>() {
            @Override
            public void onResponse(ResponseGson responseGson) {
                if (!Utils.handleResponseError(CommentsActivity.this, responseGson)) {
                    EditText inputer=(EditText) findViewById(R.id.content_inputer);
                    inputer.setText(null);
                    inputer.clearFocus();
                    ((PullRefreshLayout.OnRefreshListener) getContentFragment()).onRefresh();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
    }

    @Override
    protected Fragment setContentFragment() {
        return new CommentsFragment();
    }

    @Override
    protected String getToolbarTitle() {
        return getString(R.string.title_activity_comments);
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
