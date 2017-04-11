package com.shawnway.nav.app.yylg.tool;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.widget.Toast;

import com.shawnway.nav.app.yylg.R;

public class ToastUtil {
    // Toast
    private static Toast toast;

    public static void showShort(Context context, CharSequence message) {
        showShort(context, message, false);
    }

    public static void showShort(Context context, CharSequence message, boolean center) {
        show(context, message, Toast.LENGTH_SHORT, center);
    }

    public static void showLong(Context context, CharSequence message) {
        show(context, message, Toast.LENGTH_LONG, false);
    }

    public static void show(Context context, CharSequence message, int duration) {
        show(context, message, duration, false);
    }

    public static void show(Context context, CharSequence message, int duration, boolean middel) {
        if (message==null||StringUtils.isBlank(message.toString()))
            return;
        try {
            if (null == toast) {
                toast = Toast.makeText(context, message, duration);
                if (middel)
                    toast.setGravity(Gravity.CENTER, 0, 0);
            } else {
                toast.setText(message);
            }
            toast.show();
        } catch (Exception e) {

        }
    }

    /**
     * Hide the toast, if any.
     */
    public static void hideToast() {
        if (null != toast) {
            toast.cancel();
        }
    }


    private Context mContext = null;
    private Toast mToast = null;
    private Handler mHandler = null;
    private String mText = null;
    private Runnable mToastThread = new Runnable() {
        int counter = 0;

        @Override
        public void run() {
            String temp = "";
            for (int i = 0; i < counter; i++) {
                temp += ".";
            }
            for (int j = 0; j < 3 - counter; j++) {
                temp += " ";
            }
            if (counter == 3) {
                counter = 0;
            } else
                counter++;

            mToast.setText(mText + temp);
            mToast.show();
            if (!stopFlag) {
                mHandler.postDelayed(mToastThread, 700);// 每隔3秒显示一次，经测试，这个时间间隔效果是最好
            }
        }
    };

    public ToastUtil(Context context) {
        mContext = context;
        mHandler = new Handler(mContext.getMainLooper());
        mToast = Toast.makeText(mContext, "", Toast.LENGTH_LONG);
    }

    public void setText(String text) {
        mText = text;
    }

    public void show() {
        stopFlag = false;
        mHandler.post(mToastThread);
    }

    public static Boolean stopFlag = false;

    public void stop() {

        mToast.cancel();
    }

    public void cancel() {
        stopFlag = true;
        mHandler.removeCallbacks(mToastThread);// 先把显示线程删除
        mToast.cancel();// 把最后一个线程的显示效果cancel掉，就一了百了了
    }

    public Boolean activityStopped() {
        return stopFlag;
    }

    public static void showNetError(Context context) {
        try {
//            showShort(context, "网络连接错误，请检查网络设置");
            showShort(context,context.getString(R.string.error_system));
        } catch (Exception e) {

        }
    }

    public static void showSysError(Context context) {
        try {
            showShort(context, context.getString(R.string.error_system));
        } catch (Exception e) {

        }
    }
}
