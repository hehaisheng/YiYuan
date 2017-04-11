package com.shawnway.nav.app.yylg.tool;

import android.content.Context;
import android.text.InputType;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.bean.GoodListGsonResponse;
import com.shawnway.nav.app.yylg.bean.LastestAnnounceResponse;
import com.shawnway.nav.app.yylg.bean.RememberMeResponse;
import com.shawnway.nav.app.yylg.bean.ResponseGson;
import com.shawnway.nav.app.yylg.bean.WinRecBean;
import com.shawnway.nav.app.yylg.net.VolleyTool;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Eiffel on 2015/11/12.
 */
public class Hook {
    private static final String TAG = "Hook";

    public static final String NEW = "new";
    public static final String ALL = "all";
    public static final String PROGRESS = "progress";
    public static final String HOT = "hot";
    public static final String FREESTYLE = "FREESTYLE";
    public static final String NORMALSTYLE="NORMAL";

    private static Hook mInstance;
    private Context mContext;

    private Hook(Context context) {
        mContext = context;
    }

    public static Hook getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Hook(context);
        }
        return mInstance;
    }


    public void catagoryAll(int page, Listener listener, ErrorListener errorListener) {
        catagoryAll("1", page, Constants.DEF_PAGE_SIZE, listener, errorListener);
    }

    public void catagoryNew(int page, Listener listener, ErrorListener errorListener) {
        catagoryNew("1", page, Constants.DEF_PAGE_SIZE, listener, errorListener);
    }

    public void catagoryHot(int page, Listener listener, ErrorListener errorListener) {
        catagoryHot("1", page, Constants.DEF_PAGE_SIZE, listener, errorListener);
    }

    public void catagoryProgress(int page, Listener listener, ErrorListener errorListener) {
        catagoryProgress("1", page, Constants.DEF_PAGE_SIZE, listener, errorListener);
    }

    public void catagory(String searchBy, int page, Listener listener, ErrorListener errorListener) {
        Log.d(TAG,"查询方式："+searchBy+"，页数："+page);
        catagory(searchBy, "1", "NORMAL", page, 6, listener, errorListener);
    }

    public void catagoryNormal(String searchBy, int page, Listener listener, ErrorListener errorListener) {
        catagory(searchBy, "1", NORMALSTYLE, page, Constants.DEF_PAGE_SIZE, listener, errorListener);
    }


    public void catagoryFree(String searchBy,int page, Listener listener, ErrorListener errorListener) {
        catagory(searchBy,"1", FREESTYLE, page, Constants.DEF_PAGE_SIZE, listener, errorListener);
    }

    public void catagoryAll(String categoryId, int page, Listener listener, ErrorListener errorListener) {
        catagoryAll(categoryId, page, Constants.DEF_PAGE_SIZE, listener, errorListener);
    }

    public void catagoryNew(String categoryId, int page, Listener listener, ErrorListener errorListener) {
        catagoryNew(categoryId, page, Constants.DEF_PAGE_SIZE, listener, errorListener);
    }

    public void catagoryHot(String categoryId, int page, Listener listener, ErrorListener errorListener) {
        catagoryHot(categoryId, page, Constants.DEF_PAGE_SIZE, listener, errorListener);
    }

    public void catagoryProgress(String categoryId, int page, Listener listener, ErrorListener errorListener) {
        catagoryProgress(categoryId, page, Constants.DEF_PAGE_SIZE, listener, errorListener);
    }

    public void catagory(String searchBy, String categoryId, int page, Listener listener, ErrorListener errorListener) {
        catagory(searchBy, categoryId, "NORMAL", page, Constants.DEF_PAGE_SIZE, listener, errorListener);
    }

    public void catagory(String searchby, String categoryId,String style, int page, int pagesize, Listener listener, ErrorListener errorListener) {
        //人气商品中只显示OPEN进行中的数据 TODO:加入下架了并且没有出售完毕的商品
        String url = Constants.BASE_CATAGORY_URL + "?searchBy=" + searchby + "&categoryId=" + categoryId + "&page=" + page + "&pageSize=" + pagesize+"&drawStatus=OPEN";
        if (style!=null) url+="&viewMode="+style;
        VolleyTool.getInstance(mContext).sendGsonRequest(mContext,GoodListGsonResponse.class, url, listener, errorListener);
        Log.d(TAG, "send catagory:" + url);
    }


    public void sendGoodListRequest(String searchby, String categoryId, int page, int pagesize, Listener listener, ErrorListener errorListener) {
        String url = Constants.BASE_CATAGORY_URL + "?searchBy=" + searchby + "&categoryId=" + categoryId + "&page=" + page + "&pageSize=" + pagesize;
        VolleyTool.getInstance(mContext).sendGsonRequest(mContext,GoodListGsonResponse.class, url, listener, errorListener);
        Log.d(TAG, "send catagory:" + url);
    }


    public void catagoryAll( String categoryId, int page,int pagesize, Listener listener, ErrorListener errorListener) {
        catagory(ALL, categoryId, "NORMAL", page, pagesize, listener, errorListener);
    }

    public void catagoryNew(String categoryId, int page, int pagesize, Listener listener, ErrorListener errorListener) {
        catagory(NEW, categoryId, "NORMAL", page, pagesize, listener, errorListener);
    }

    public void catagoryHot(String categoryId, int page, int pagesize, Listener listener, ErrorListener errorListener) {
        catagory(HOT, categoryId,"NORMAL", page, pagesize, listener, errorListener);
    }

    public void catagoryProgress(String categoryId, int page, int pagesize, Listener listener, ErrorListener errorListener) {
        catagory(HOT, categoryId, "NORMAL",page, pagesize, listener, errorListener);
    }


    /**
     * Rememberme
     */
    public void rememberMe(Listener<RememberMeResponse> listener, ErrorListener errorListener) {
        VolleyTool.getInstance(mContext).sendGsonRequest(mContext, RememberMeResponse.class, Constants.REMEMBER_ME_URL, listener, errorListener);
    }

    /*
     * donate
     */
    public static void donate(final WinRecBean bean, final Context context, final Response.Listener<ResponseGson> listener, final Response.ErrorListener errorListener) {
        //改对话框的布局在alert_dialog.xml这个布局文件中
        SweetAlertDialog dlg = new SweetAlertDialog(context, SweetAlertDialog.INPUT_TYPE);
        dlg.setContentText(context.getString(R.string.donate_dlg_title)).setInputerType(InputType.TYPE_CLASS_PHONE).
                setConfirmText(context.getString(R.string.donate_dlg_cfm_btn)).setCancelText(context.getString(R.string.cancel))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        try {
                            JSONObject params = new JSONObject();
                            params.put("recieverCel", sweetAlertDialog.getInputedText());
                            params.put("winprizeId", bean.getWinprizeId());

                            VolleyTool.getInstance(context).sendGsonPostRequest(context, ResponseGson.class, Constants.DONATE_URL, params.toString(), listener, errorListener
//
                            );
                        } catch (JSONException e) {
                            Log.e(TAG, e.getStackTrace().toString());
                        }
                    }
                }).show();
    }

    public void login(String acc, String dwp, Listener<ResponseGson> listener, ErrorListener errorListener) {
        HashMap<String, String> map = new HashMap<>();
        map.put("username", acc);
        map.put("password", dwp);
        map.put("remember-me", "Yes");
        VolleyTool.getInstance(mContext).sendGsonPostRequest(mContext, ResponseGson.class, Constants.LOGIN_URL, map, listener, errorListener);

    }



    /**
     * 获取最新揭晓数据
     * @param calculatingSize  设置计算中列表的最大长度，
     * @param announceSize   设置已揭晓列表的最大长度
     * @param listener
     * @param errorListener
     */
//    public void getLatestAnnounce(int calculatingSize, int announceSize,
//                                  Listener<LastestAnnounceResponse> listener, ErrorListener errorListener) {
//
//        HashMap<String,String> param=new HashMap<>();
//        param.put("calculatingSize", String.valueOf(calculatingSize));
//        param.put("announceSize", String.valueOf(announceSize));
//        VolleyTool.getInstance(mContext).sendGsonRequest(mContext, LastestAnnounceResponse.class, Constants.LATESTANNOUNCE_URL, param, listener, errorListener);
//
//    }

    /**
     * 获取实体区的最新揭晓数据
     * @param calculatingSize  设置计算中列表的最大长度，
     * @param announceSize   设置已揭晓列表的最大长度
     * @param physicalId 实体区的id
     * @param listener
     * @param errorListener
     */
//    public void getRealLatestAnnounce(int calculatingSize, int announceSize,String physicalId,
//                                  Listener<LastestAnnounceResponse> listener, ErrorListener errorListener) {
//
//        HashMap<String,String> param=new HashMap<>();
//        param.put("calculatingSize", String.valueOf(calculatingSize));
//        param.put("announceSize", String.valueOf(announceSize));
//        param.put("physicalId",String.valueOf(physicalId));
//        VolleyTool.getInstance(mContext).sendGsonRequest(mContext, LastestAnnounceResponse.class, Constants.LATESTANNOUNCE_URL, param, listener, errorListener);
//
//    }

    /**
     * 获取最新揭晓数据
     * @param page  页数
     * @param PageSize   一页中的数据量
     * @param listener
     * @param errorListener
     */
    public void getLatestPageAnnounce(int page, int PageSize,
                                  Listener<LastestAnnounceResponse> listener, ErrorListener errorListener) {

        HashMap<String,String> param=new HashMap<>();
        param.put("page", String.valueOf(page));
        param.put("PageSize", String.valueOf(PageSize));
        VolleyTool.getInstance(mContext).sendGsonRequest(mContext, LastestAnnounceResponse.class, Constants.LATESTANNOUNCE_URL, param, listener, errorListener);

    }

    /**
     * 获取实体区的最新揭晓数据
     * @param page 页数
     * @param pageSize   一页中的数据量
     * @param physicalId 实体区的id
     * @param listener
     * @param errorListener
     */
    public void getRealLatestPageAnnounce(int page, int pageSize,String physicalId,
                                      Listener<LastestAnnounceResponse> listener, ErrorListener errorListener) {

        HashMap<String,String> param=new HashMap<>();
        param.put("page", String.valueOf(page));
        param.put("pageSize", String.valueOf(pageSize));
        param.put("physicalId",String.valueOf(physicalId));
        VolleyTool.getInstance(mContext).sendGsonRequest(mContext, LastestAnnounceResponse.class, Constants.LATESTANNOUNCE_URL, param, listener, errorListener);

    }

}
