package com.shawnway.nav.app.yylg.style;

import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.tool.ToastUtil;


public class WebViewStyleFactory {
    public static LaunchDisplayer createDefaultLuanchStyle(
            View webViewContainer, View loadingview, View failloadingview) {
        return new DefaultLoadStyle(webViewContainer, loadingview,
                failloadingview);
    }

    static class DefaultLoadStyle implements LaunchDisplayer {
        View webViewContainer;
        View loadingview;
        View failloadingview;

        public DefaultLoadStyle(View webViewContainer, View loadingview,
                                View failloadingview) {
            this.webViewContainer = webViewContainer;
            this.loadingview = loadingview;
            this.failloadingview = failloadingview;

        }

        @Override
        public void notNet() {
        }

        @Override
        public void connectSuccess() {
            if (webViewContainer != null
                    && View.GONE == failloadingview.getVisibility()) {
                webViewContainer.setVisibility(View.VISIBLE);
                loadingview.setVisibility(View.GONE);
            }
        }

        @Override
        public void connectFalse() {
            // webViewContainer.setVisibility(View.GONE);
            if (loadingview != null && failloadingview != null) {
                loadingview.setVisibility(View.GONE);
                failloadingview.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void loading() {
            if (loadingview != null && failloadingview != null) {
                failloadingview.setVisibility(View.GONE);
                loadingview.setVisibility(View.VISIBLE);
            }


        }


        @Override
        public void delayConnectFalse() {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    connectFalse();
                    ToastUtil.showShort(failloadingview.getContext(), failloadingview.getContext().getString(R.string.network_error));
                }
            }, 1500);
        }

        @Override
        public void pageNeedFix() {
            loadingview.setVisibility(View.GONE);
            ((ImageView) failloadingview.findViewById(R.id.fail_load_img))
                    .setImageResource(R.drawable.need_fix);
            failloadingview.findViewById(R.id.fail_load).setVisibility(View.VISIBLE);

        }

    }

    public interface LaunchDisplayer {
        /**
         * to display the ui when occur situation below
         */
        abstract public void notNet();

        abstract public void connectSuccess();

        abstract public void connectFalse();

        abstract public void loading();

        abstract public void delayConnectFalse();

        public abstract void pageNeedFix();
    }

}
