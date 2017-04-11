package com.shawnway.nav.app.yylg.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.shawnway.nav.app.yylg.tool.Utils;

/**
 * Created by 裘 on 2016/10/31 0031.
 */

public class HeartService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new AutoLoginThread().start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
    }

    class AutoLoginThread extends Thread {
        @Override
        public void run() {
            Utils.autoLogin(getApplicationContext());
        }
    }
}
