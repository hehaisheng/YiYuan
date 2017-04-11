package com.shawnway.nav.app.yylg.implemen;

import com.android.volley.VolleyError;
import com.shawnway.nav.app.yylg.bean.ResponseGson;

/**
 * Created by Eiffel on 2016/3/24.
 */
public interface ResponseListener {
    void onDeliverResponse(ResponseGson response);
    void onError(VolleyError error);
}
