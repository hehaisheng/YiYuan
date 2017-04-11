package com.shawnway.nav.app.yylg.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.bean.ViewDetailResponse;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.style.WebViewStyleFactory;
import com.shawnway.nav.app.yylg.tool.AppUtils;
import com.shawnway.nav.app.yylg.tool.ToastUtil;
import com.shawnway.nav.app.yylg.webview.MyWebViewClient;


public class BaseWebViewFragment extends MyFragment {

    private final String TAG = "XiaoSimpleFragment";
    private WebView webView;
    private WebViewStyleFactory.LaunchDisplayer uilaunchStyle;

    protected boolean needcache = false;
    private String mDefaultURl;
    private String mHtmlSrc;
    private boolean isFixCenter = true;

    public BaseWebViewFragment() {
        super();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mDefaultURl = bundle.getString("url");
            mHtmlSrc = bundle.getString("src");
            isFixCenter = bundle.getBoolean("fitcenter", true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_fragment_webbase, container, false);
        webView = (WebView) view.findViewById(R.id.webview);
        setWebViewStyle(view);
        setWebView(view);
        return view;
    }

    private void setWebViewStyle(View view) {
        View webViewContainer = view.findViewById(R.id.web_view_container);
        View loadingView = view.findViewById(R.id.loading_bar);
        View failLoadView = view.findViewById(R.id.fail_load);
        failLoadView.setOnClickListener(new ErrorOnclickListener());
        setLuanchStyle(WebViewStyleFactory.createDefaultLuanchStyle(
                webViewContainer, loadingView, failLoadView));
    }

    protected void setWebView(View view) {
        WebView webView = getWebView();

        if (webView != null) {
            WebSettings settings = webView.getSettings();
            settings.setJavaScriptEnabled(getArguments().getBoolean("js", false));
            settings.setSupportZoom(getArguments().getBoolean("zoom", false));
            settings.setDefaultTextEncodingName("UTF-8");
            if (getArguments().getBoolean("zoom", false)) {
                settings.setBuiltInZoomControls(true); // 显示放大缩小 controler
                settings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);// 默认缩放模式
                settings.setUseWideViewPort(true);  //为图片添加放大缩小功能
                setInitialScale();
            }
            if (isFixCenter) {
                settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); //自动适应屏幕
            }
            webView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return true;
                }
            });

            if (Build.VERSION.SDK_INT >= 19) {
                webView.getSettings().setCacheMode(
                        WebSettings.LOAD_DEFAULT);
            }
        }
        if (webView != null) {
            webView.setWebChromeClient(new WebChromeClient());
            webView.setWebViewClient(new MyWebViewClient(uilaunchStyle));
        }

        if (mDefaultURl != null && !mDefaultURl.isEmpty()) {
            loadurl(mDefaultURl);
        }

        else {
            getRealContent();
        }

    }

    private void setInitialScale() {
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        webView.setInitialScale(100);
    }

    //通过mHtmlStr获取，可是并没有传递数据过来
    private void getRealContent() {
        VolleyTool.getInstance(getContext()).sendGsonRequest(this, ViewDetailResponse.class, mHtmlSrc, new Response.Listener<ViewDetailResponse>() {
            @Override
            public void onResponse(ViewDetailResponse response) {
                loadHtml(response.getBody().getFullDesc());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtil.showNetError(getContext());
            }
        });
    }

    public void loadurl(String url) {
        if (!AppUtils.hasInternet(getActivity())) {
            getLuanchStyle().delayConnectFalse();
        } else {
            if (url != null) {
                webView.loadUrl(url);
            } else {
                getLuanchStyle().pageNeedFix();
            }
        }

    }

    public void loadfunction(String function) {
        String method;
        if (isAnonymous(function)) {

            method = "javascript:(" + function + "(" + "))";

        } else {
            method = "javascript:" + function + "(" + ")";

        }
        webView.loadUrl(method);
        Log.d(TAG, method);
    }

    public void loadHtml(String html) {
        if (html != null)
            webView.loadDataWithBaseURL("about:blank", html, "text/html", "utf-8", null);
        else
            ToastUtil.showShort(getContext(), "服务器暂无改商品图文信息");
    }

    private boolean isAnonymous(String function) {
        return function.indexOf('(') != -1;
    }

    private boolean isLocalUrl(String url) {
        return url.contains(".html");
    }


    protected String getDefaultUrl() {
        return "";
    }

    public WebViewStyleFactory.LaunchDisplayer getLuanchStyle() {
        return uilaunchStyle;
    }

    private void setLuanchStyle(WebViewStyleFactory.LaunchDisplayer displayer) {
        uilaunchStyle = displayer;
    }

    public WebView getWebView() {
        return webView;
    }


    public boolean refreshWebView() {
        if (getWebView() != null) {
            uilaunchStyle.loading();
            getWebView().reload();
            return true;
        }
        return false;
    }


    @Override
    public Context getContext() {

        return getActivity();
    }


    public void loadUrlWithNetCheck() {
        getLuanchStyle().loading();
        if (getWebView().getUrl() != null) {
            Log.d(TAG, getWebView().getUrl());
            loadurl(getWebView().getUrl());
        } else {
            loadurl(mDefaultURl);
        }
    }

    class ErrorOnclickListener implements OnClickListener {

        @Override
        public void onClick(View v) {

            loadUrlWithNetCheck();
        }

    }
}
