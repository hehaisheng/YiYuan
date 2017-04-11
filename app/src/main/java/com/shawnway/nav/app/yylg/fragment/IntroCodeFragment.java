package com.shawnway.nav.app.yylg.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.bean.ResponseGson;
import com.shawnway.nav.app.yylg.mvp.user.user_invite.explain.ExplainActivity;
import com.shawnway.nav.app.yylg.net.Api;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.QRCode;
import com.shawnway.nav.app.yylg.tool.ToastUtil;
import com.shawnway.nav.app.yylg.tool.Utils;
import com.shawnway.nav.app.yylg.tool.VolleyErrorHelper;
import com.thuongnh.zprogresshud.ZProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

/**
 * Created by Eiffel on 2015/12/28.
 */
public class IntroCodeFragment extends MyFragment implements View.OnClickListener {
    private static final String TAG = "IntroCodeFragment";
    private boolean islock = true;
    private Button buttonExplain;
    //http://tescochn.com/wechat/views/share.html?cellphonenumber=
    private String invite_url = Api.BASE_URL + "wechat/views/share.html?cellphonenumber=";
    private String condition = "&inviteCode=";

    private ImageView imageQRCode;
    private Button saveQRCode;
    private Bitmap qrcodeBitmap;
    private View view;
    private MediaScannerConnection msc;

    public void lock() {
        islock = true;
    }

    public void unLock() {
        islock = false;
    }

    public static Fragment getInstance() {
        return new IntroCodeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_myintrocode, container, false);
        buttonExplain = (Button) view.findViewById(R.id.fragment_myintrocode_btn_explain);
        imageQRCode = (ImageView) view.findViewById(R.id.iv_invite_qrcode);
        saveQRCode = (Button) view.findViewById(R.id.btn_save_qrcode);
        saveQRCode.setOnClickListener(this);
        buttonExplain.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUi();
    }

    private void initCode() {
        VolleyTool.getInstance(getContext()).sendGsonRequest(this, InvitatorCodeResponse.class, Constants.MY_INVITATOR, new Response.Listener<InvitatorCodeResponse>() {
            @Override
            public void onResponse(InvitatorCodeResponse response) {
                if (!Utils.handleResponseError(getActivity(), response)) {
                    String code = response.getBody().invitatorCode;
                    if (code.equals("未被邀请")) {
                        changeToInvited(false, code);
                    } else {
                        changeToInvited(true, code);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
    }

    private void initUi() {
        lock();
        ZProgressHUD.getInstance(getContext()).setMessage("加载中").show();
        VolleyTool.getInstance(getContext()).sendGsonRequest(this, MyIntrocodeResponse.class, Constants.MYINTROCODE, new Response.Listener<MyIntrocodeResponse>() {
            @Override
            public void onResponse(MyIntrocodeResponse response) {
                if (!Utils.handleResponseError(getActivity(), response)) {
                    try {
                        Log.d(TAG, "my introcode" + response.getBody().customerDetails.invitationCode);
                        Log.d(TAG, "invite me:" + response.getBody().customerDetails.invitatorCode);
                        updateStatu(response);
                        unLock();
                        initCode();
                        initQRCode(response.getBody().customerDetails.invitationCode);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
                ZProgressHUD.dismis();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (isAdded())
                    ToastUtil.showShort(getContext(), VolleyErrorHelper.getMessage(volleyError, getContext()));

            }
        });
    }

    /**
     * 生成二维码
     *
     * @param inviteCode 邀请码
     */
    private void initQRCode(String inviteCode) {
        String acc = (String) Utils.getParam(getActivity(), "acc", "");
        invite_url = invite_url + acc + condition + inviteCode;
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.yy_appicon);
        qrcodeBitmap = QRCode.createQRCodeWithLogo(invite_url, 500, bmp);
        imageQRCode.setImageBitmap(qrcodeBitmap);
        saveQRCode.setVisibility(View.VISIBLE);
    }

    private void updateStatu(MyIntrocodeResponse response) {
        TextView myCode = (TextView) getView().findViewById(R.id.tv_myintrocode);
        final String code = response.getBody().customerDetails.invitationCode;
        myCode.setText(code);
        Button btnSend = (Button) getView().findViewById(R.id.fragment_myintrocode_btn_send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.putExtra("sms_body", "我的一元乐购的邀请码是：" + code + "，我们一起来玩吧！");
                sendIntent.setType("vnd.android-dir/mms-sms");
                startActivity(sendIntent);
            }
        });
    }

    private void changeToInvited(boolean invited, String code) {
//        View view = getView();
//        View invitationWrapper = view.findViewById(R.id.layout_invitator_wrapper);
//        invitationWrapper.setVisibility(invited ? View.VISIBLE : View.GONE);
//        if (invited) {//真的
//            TextView tvCode = (TextView) view.findViewById(R.id.tv_invitator);
//            tvCode.setText(code);
//        } else {//code未被邀请
//
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
//                summit();
                break;
            case R.id.fragment_myintrocode_btn_explain:
                ExplainActivity.start(getActivity());
                break;
            case R.id.btn_save_qrcode:

                saveQRCodes();

                break;
        }
    }

    /**
     * 保存二维码弹窗
     */
    private void saveQRCodes() {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setConfirmText("保存").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(final SweetAlertDialog sweetAlertDialog) {
                if (Build.VERSION.SDK_INT >= 23) {
                    PermissionGen.with(IntroCodeFragment.this)
                            .addRequestCode(100)
                            .permissions(
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .request();
                } else {
                    onSaveQRCode();
                }
                sweetAlertDialog.dismiss();
            }
        }).setCancelText(getContext().getString(R.string.cancel)).
                setContentText("是否保存二维码到本地相册？").
                setTitleText("保存二维码").show();
    }

    /**
     * 保存二维码
     */
    @PermissionSuccess(requestCode = 100)
    private void onSaveQRCode() {
        MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), qrcodeBitmap, "qrCode", "一元乐购二维码");
        Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_SHORT).show();
        msc = new MediaScannerConnection(getActivity(), new MediaScannerConnection.MediaScannerConnectionClient() {
            @Override
            public void onMediaScannerConnected() {
                msc.scanFile("/sdcard/qrCode.jpg", "qrCode/jpeg");
            }

            @Override
            public void onScanCompleted(String path, Uri uri) {
                msc.disconnect();
            }
        });
    }

    /**
     * 获取权限失败提示
     */
    @PermissionFail(requestCode = 100)
    private void onSaveQRCodeFail() {
        Toast.makeText(getActivity(), "需要获取权限才能继续", Toast.LENGTH_SHORT).show();
    }

    /**
     * 获取权限返回结果
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    private void summit() {
        JSONObject params = new JSONObject();
        JSONObject customerDetails = new JSONObject();
        try {
            customerDetails.put("invitatorCode", Utils.getEditTextStr(getView().findViewById(R.id.inputer_introcode)));
            params.put("customerDetails", customerDetails);
            VolleyTool.getInstance(getContext()).sendGsonPostRequest(this, IntrocodeResponse.class, Constants.INTROCODE, params.toString(), new Response.Listener<IntrocodeResponse>() {
                @Override
                public void onResponse(IntrocodeResponse response) {
                    if (!Utils.handleResponseError(getActivity(), response)) {
                        ToastUtil.showShort(getContext(), getString(R.string.success_modify));
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    String erromsg = VolleyErrorHelper.getMessage(volleyError, getContext());
                    if (erromsg != null) ToastUtil.showShort(getContext(), erromsg);
                    else ToastUtil.showSysError(getContext());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "resultcode is " + resultCode + "  ;requestcode is " + requestCode);
        if (resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "onLoginResult");
            initUi();
        } else if (requestCode == Constants.ACTION_REQUEST_LOGIN) {
            getActivity().finish();
        }

    }

    class MyIntrocodeResponse extends ResponseGson<MyIntrocodeBody> {
    }

    class MyIntrocodeBody {
        MyIntroCodeDetail customerDetails;

        private class MyIntroCodeDetail {
            String invitationCode;
            String invitatorCode;
        }
    }

    private class IntrocodeResponse extends ResponseGson<IntrocodeBody> {
    }

    private class IntrocodeBody {

    }

    public class InvitatorCodeResponse extends ResponseGson<InvitatorCodeBody> {

    }

    public class InvitatorCodeBody {
        public String invitatorCode;
    }
}