package com.shawnway.nav.app.yylg.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.bean.ResponseGson;
import com.shawnway.nav.app.yylg.bean.WinRecBean;
import com.shawnway.nav.app.yylg.customer.activity.ImageSelectorActivity;
import com.shawnway.nav.app.yylg.customer.adapter.ImageSelectorPubSelectedImgsAdapter;
import com.shawnway.nav.app.yylg.net.MultipartRequest;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.tool.CloseActivityUtil;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.ImageFactory;
import com.shawnway.nav.app.yylg.tool.StringUtils;
import com.shawnway.nav.app.yylg.tool.ToastUtil;
import com.shawnway.nav.app.yylg.tool.Utils;
import com.shawnway.nav.app.yylg.tool.VolleyErrorHelper;
import com.thuongnh.zprogresshud.ZProgressHUD;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

/**
 * Created by Eiffel on 2015/11/23.
 */
public class AddShareActivity extends MyActivity implements View.OnClickListener {

    private static final int REQUEST_IMAGE = 2;
    private static final String TAG = "AddShareActivity";
    private static final int MINPICTURE = 1;

    private ArrayList<String> mSelectPath;

    GridView gridView;
    ImageSelectorPubSelectedImgsAdapter imageSelectorPubSelectedImgsAdapter;
    private WinRecBean bean;
    private AsyncTask addShareTask;
    private MultipartRequest addShareReq;


    public static void getInstance(Activity context, WinRecBean bean, int reqcode) {
        Intent intent = new Intent(context, AddShareActivity.class);
        intent.putExtra("data", bean);
        context.startActivityForResult(intent, reqcode);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CloseActivityUtil.add(AddShareActivity.this);
        setContentView(R.layout.activity_add_share);
        bean = (WinRecBean) getIntent().getExtras().getSerializable("data");
        Log.d(TAG, new Gson().toJson(bean));

        gridView = (GridView) findViewById(R.id.gridView);
        ((TextView) findViewById(R.id.addshare_good_name)).setText(bean.productName);
        initToolbar();
    }


    private void initToolbar() {

        TextView centerText = (TextView) findViewById(R.id.top_back_text);
        centerText.setText(getResources().getString(R.string.title_activity_addshare));
        centerText.setVisibility(View.VISIBLE);

        ImageButton backButton = (ImageButton) findViewById(R.id.top_back);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(this);
//

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                mSelectPath = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
                imageSelectorPubSelectedImgsAdapter = new ImageSelectorPubSelectedImgsAdapter(getApplicationContext(), mSelectPath, new ImageSelectorPubSelectedImgsAdapter.OnItemClickClass() {
                    @Override
                    public void OnItemClick(View v, String filepath) {
                        mSelectPath.remove(filepath);
                        imageSelectorPubSelectedImgsAdapter.notifyDataSetChanged();
                    }
                });
            }
            gridView.setAdapter(imageSelectorPubSelectedImgsAdapter);
        }
    }


    private void share() {

        final Map<String, File> files = new HashMap<>();
        if (mSelectPath != null) {
            for (int i = 0; i < mSelectPath.size(); i++) {
                File file = new File(mSelectPath.get(i));
                files.put("file" + i, file);
            }
        }
        final String subject = Utils.getEditTextStr(findViewById(R.id.addshare_inputer_title));
        final String content = Utils.getEditTextStr(findViewById(R.id.addshare_inputer_comment));

        if (StringUtils.isBlank(subject)) {
            ToastUtil.showShort(this, getString(R.string.error_share_nosubject));
            return;
        }

        if (files.size() < MINPICTURE) {
            ToastUtil.showShort(this, getString(R.string.error_share_noten));
            return;
        }

        final ZProgressHUD loading = new ZProgressHUD(this);
        loading.setMessage(getString(R.string.sharing));
        loading.setOnDialogDismiss(new ZProgressHUD.OnDialogDismiss() {
            @Override
            public void onDismiss() {
                if (addShareTask != null)
                    addShareTask.cancel(true);
                if (addShareReq != null)
                    addShareReq.cancel();
            }
        });
        loading.show();
        if (addShareTask != null)
            addShareTask.cancel(true);

        addShareTask = new AsyncTask<Object, Integer, Integer>() {
            Map<String, File> temp = new HashMap<>();

            @Override
            protected Integer doInBackground(Object[] params) {
                String cacheDir = getCacheDir().getPath();

                try {
                    for (Map.Entry<String, File> entry : files.entrySet()) {
                        ImageFactory factory = new ImageFactory();
                        File file = entry.getValue();
                        String src = file.getPath();
                        String outp = cacheDir + file.getName();
                        try {
                            factory.compressAndGenImage(src, outp, ImageFactory.DEFAULT, false);
                            temp.put(entry.getKey(), new File(outp));
                        } catch (IOException e) {
                            Log.e(TAG, "failed to compress:" + src);
                            return RESULT_CANCELED;

                        }
                    }
                    return RESULT_OK;
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "failed to compress");
                    return RESULT_CANCELED;
                }
            }

            @Override
            protected void onPostExecute(Integer integer) {
                switch (integer) {
                    case RESULT_CANCELED:
                        loading.dismiss();
                        ToastUtil.showLong(AddShareActivity.this, "分享失败");
                        break;
                    case RESULT_OK:
                        Map<String, String> params = new HashMap<>();
                        params.put("winPrizeId", bean.getWinprizeId() + "");
                        params.put("subject", subject);
                        params.put("content", content);
                        for (Map.Entry<String, File> entry :
                                temp.entrySet()) {
                            Log.d(TAG, entry.getKey() + ":" + entry.getValue().length() + " bytes");
                        }
                        addShareReq = VolleyTool.getInstance(AddShareActivity.this).sendMultipartRequest(AddShareActivity.this, ResponseGson.class, Constants.ADD_SHARE_URL, temp, params, new Response.Listener<ResponseGson>() {
                            @Override
                            public void onResponse(ResponseGson response) {
                                if (!Utils.handleResponseError(AddShareActivity.this, response)) {
                                    ToastUtil.showShort(AddShareActivity.this, getString(R.string.share_success));
                                    loading.dismiss();
                                    setResult(RESULT_OK);
                                    finish();
                                } else loading.dismiss();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                loading.dismiss();
                                ToastUtil.showLong(AddShareActivity.this, "分享失败：" + VolleyErrorHelper.getMessage(volleyError, AddShareActivity.this));
                            }
                        });
                        break;

                }
            }
        };
        addShareTask.execute("");


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_back:
                finish();
                break;
            case R.id.goselect:
                if (Build.VERSION.SDK_INT >= 23) {
                    PermissionGen.with(this)
                            .addRequestCode(200)
                            .permissions(
                                    Manifest.permission.READ_EXTERNAL_STORAGE)
                            .request();
                } else {
                    selectImage();
                }
                break;
            case R.id.commit:
                share();
                break;
        }
    }

    /**
     * 显示选择图片
     */
    @PermissionSuccess(requestCode = 200)
    private void selectImage() {
        Intent intent = new Intent(AddShareActivity.this, ImageSelectorActivity.class);
        // 是否显示拍摄图片
        intent.putExtra(ImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // 最大可选择图片数量
        intent.putExtra(ImageSelectorActivity.EXTRA_SELECT_COUNT, 5);
        // 选择模式
        intent.putExtra(ImageSelectorActivity.EXTRA_SELECT_MODE, ImageSelectorActivity.MODE_MULTI);
        // 默认选择
        if (mSelectPath != null && mSelectPath.size() > 0) {
            intent.putExtra(ImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mSelectPath);
        }
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    @Override
    protected void onDestroy() {
        if (addShareTask != null)
            addShareTask.cancel(true);
        super.onDestroy();
    }


    /**
     * 获取权限失败提示
     */
    @PermissionFail(requestCode = 200)
    private void onPermissionFail() {
        Toast.makeText(this, "需要获取权限才能继续", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

//    @Override
//    public Resources getResources() {
//        Resources res = super.getResources();
//        Configuration config = new Configuration();
//        config.setToDefaults();
//        res.updateConfiguration(config, res.getDisplayMetrics());
//        return res;
//    }

}
