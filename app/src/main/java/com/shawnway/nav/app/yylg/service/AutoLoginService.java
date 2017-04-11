package com.shawnway.nav.app.yylg.service;

import android.app.IntentService;
import android.content.Intent;

import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.Utils;

/**
 * Created by Kevin on 2016/12/29
 */

public class AutoLoginService extends IntentService {
    private static final String TAG = "AutoLoginService";

    public AutoLoginService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Utils.autoLogin(Constants.mainActivity);
    }
}
