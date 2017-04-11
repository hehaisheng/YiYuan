package com.shawnway.nav.app.yylg.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.baoyz.widget.PullRefreshLayout;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.activity.AccountDetailsActivity;
import com.shawnway.nav.app.yylg.activity.AddressActivity;
import com.shawnway.nav.app.yylg.activity.ChargeActivity;
import com.shawnway.nav.app.yylg.activity.GrantActivity;
import com.shawnway.nav.app.yylg.activity.GrantRecordActivity;
import com.shawnway.nav.app.yylg.activity.HelpCenterActivity;
import com.shawnway.nav.app.yylg.activity.IntroCodeActivity;
import com.shawnway.nav.app.yylg.activity.LoginActivity;
import com.shawnway.nav.app.yylg.activity.MissionActivity;
import com.shawnway.nav.app.yylg.activity.MyCommissionActivity;
import com.shawnway.nav.app.yylg.activity.MyPointActivity;
import com.shawnway.nav.app.yylg.activity.MyShareActivity;
import com.shawnway.nav.app.yylg.activity.PersonalDataActivity;
import com.shawnway.nav.app.yylg.activity.WinRecordActivity;
import com.shawnway.nav.app.yylg.base.BaseSubscriber;
import com.shawnway.nav.app.yylg.bean.ResponseGson;
import com.shawnway.nav.app.yylg.mvp.user.user_buy_record.MyBuyRecordsActivity;
import com.shawnway.nav.app.yylg.net.RetrofitManager;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.GlideUtils;
import com.shawnway.nav.app.yylg.tool.ImageUtils;
import com.shawnway.nav.app.yylg.tool.ShareFriendUtil;
import com.shawnway.nav.app.yylg.tool.ThreadTransformer;
import com.shawnway.nav.app.yylg.tool.Utils;
import com.thuongnh.zprogresshud.ZProgressHUD;

import java.io.File;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class UserFragment extends Fragment implements View.OnClickListener, PullRefreshLayout.OnRefreshListener {
    private static final String TAG = "UserFragment";
    private BroadcastReceiver receiver;
    private Animation mFadeout;
    private Animation mFadein;
    private int autoLoginNum = 0;
    private ImageView userImage;
    private final int CHOOSE_PICTURE = 0;

    private final int TAKE_PICTURE = 1;

    private final int CROP_SMALL_PICTURE = 2;

    public static final String IMAGE_URL = "IMAGE_URL";

    private final String IS_BG_ACCOUNT = "isBackGroundAccount";

    public static final int REQUEST_LOGIN = 3;
    public static final int UPDATE_BALANCE=4;

    private Uri tempUri;
    private Button btnActionGrant;
    private TextView nicname;
    private TextView balance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_user, container, false);
        setListener(view);
        initReceiver();
        return view;
    }


    /**
     * 每次进入我的乐购都要刷新一次余额
     * 客户需要~
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            String dwp = (String) Utils.getParam(getActivity(), "dwp", "");
            //粗暴方式刷新（上手代码太6没办法改）
            if (!TextUtils.isEmpty(dwp)) {
                refreshBlance();
            }
        }
    }

    private void initReceiver() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Constants.ACTION_LOGIN)) {
                    try {
                        if (isAdded()) {
                            initUserInfo();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.getStackTrace().toString());
                    }
                } else if (intent.getAction().equals(Constants.ACTION_LOGOUT)) {
                    if (isAdded()) {
                        setToUnlog(getView());
                    }
                }
                else  if(intent.getAction().equals(Constants.ACTION_UPDATE_BALANCE))
                {
                    refreshBlance();
                }
            }
        };
        IntentFilter filter = new IntentFilter(Constants.ACTION_LOGIN);
        getActivity().registerReceiver(receiver, filter);
        filter = new IntentFilter(Constants.ACTION_LOGOUT);
        getActivity().registerReceiver(receiver, filter);
        filter = new IntentFilter(Constants.ACTION_UPDATE_BALANCE);
        getActivity().registerReceiver(receiver, filter);
    }

    private void initUserInfo() {
        RetrofitManager.getInstance()
                .getApi()
                .getCustomerInfo()
                .compose(ThreadTransformer.<UserInfoResponse>applySchedulers())
                .subscribe(new BaseSubscriber<UserInfoResponse>() {
                    @Override
                    public void onSuccess(UserInfoResponse userInfoResponse) {
                        setToHasLoged(userInfoResponse, getView());
                    }
                });
    }

    /**
     * 设置登录状态模块
     *
     * @param response
     * @param view
     */
    private void setToHasLoged(final UserInfoResponse response, final View view) {
        if (view != null && response.getBody().getCustomerDetails() != null) {
            view.findViewById(R.id.user_info_has_login).setVisibility(View.VISIBLE);
            view.findViewById(R.id.logout).setVisibility(View.VISIBLE);
            view.findViewById(R.id.user_login).setVisibility(View.GONE);
            view.findViewById(R.id.user_login_welcome).setVisibility(View.GONE);
            balance = (TextView) view.findViewById(R.id.user_info_balance);
            nicname = (TextView) view.findViewById(R.id.user_name);
            nicname.setText(response.getBody().getCustomerDetails().nickname);
            balance.setText(response.getBody().getCustomerDetails().balance);
        }
    }

    private void setToUnlog(final View view) {
        mFadeout = AnimationUtils.loadAnimation(getContext(), R.anim.abc_fade_out);
        mFadein = AnimationUtils.loadAnimation(getContext(), R.anim.abc_fade_in);
        final View loggedwrapper = view.findViewById(R.id.user_info_has_login);
        mFadeout.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                loggedwrapper.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.findViewById(R.id.user_login).setVisibility(View.VISIBLE);
        view.findViewById(R.id.user_login_welcome).setVisibility(View.VISIBLE);
        loggedwrapper.startAnimation(mFadeout);

        view.findViewById(R.id.logout).setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(receiver);
        super.onDestroy();
    }

    private void setListener(View view) {
        view.findViewById(R.id.user_login).setOnClickListener(this);
        view.findViewById(R.id.logout).setOnClickListener(this);
        view.findViewById(R.id.user_help).setOnClickListener(this);//乐购说明
        view.findViewById(R.id.action_grant).setOnClickListener(this);
        view.findViewById(R.id.action_charge).setOnClickListener(this);
        btnActionGrant = (Button) view.findViewById(R.id.action_grant);
        showBGAccountBtn();
        btnActionGrant.setOnClickListener(this);
        userImage = (ImageView) view.findViewById(R.id.user_avatar);
        initUserImage();
        userImage.setOnClickListener(this);
        //遍历表格布局，为单元格设置点击监听
        TableLayout tb = (TableLayout) view.findViewById(R.id.user_table);
        for (int i = 0; i < tb.getChildCount(); i++) {
            try {
                ViewGroup group = (ViewGroup) tb.getChildAt(i);
                for (int j = 0; j < group.getChildCount(); j++) {
                    group.getChildAt(j).setOnClickListener(this);
                }
            } catch (ClassCastException e) {
                tb.getChildAt(i).setOnClickListener(this);
            }
        }
    }


    private void logout() {
        VolleyTool.getInstance(getContext()).sendGsonPostRequest(this, ResponseGson.class, Constants.LOGOUT_URL, new Listener<ResponseGson>() {
            @Override
            public void onResponse(ResponseGson responseGson) {
                if (responseGson.getCode().equals(VolleyTool.SUCCESS)) {
                    Utils.erasePWDInfo(getContext());
                    Intent intent = new Intent(Constants.ACTION_LOGOUT);
                    getActivity().sendBroadcast(intent);

                }
            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
    }


    private void login() {
        Intent i = new Intent(getActivity(), LoginActivity.class);
        startActivityForResult(i, REQUEST_LOGIN);
}

    private void shareFriend() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dlg_share_to_friend, null);
        view.findViewById(R.id.btn_close).setOnClickListener(new ShareDlgListener());
        TableLayout tb = (TableLayout) view.findViewById(R.id.share_table);
        for (int i = 0; i < tb.getChildCount(); i++) {
            try {
                ViewGroup group = (ViewGroup) tb.getChildAt(i);
                for (int j = 0; j < group.getChildCount(); j++) {
                    group.getChildAt(j).setOnClickListener(new ShareDlgListener());
                }
            } catch (ClassCastException e) {
                tb.getChildAt(i).setOnClickListener(new ShareDlgListener());
            }
        }
        dlg = builder.setView(view).show();
    }

    private AlertDialog dlg;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "on userFragment activityresult");
        if (requestCode == Constants.ACTION_REQUEST_LOGIN && resultCode == RESULT_OK) {
            //登录成功初始化用户信息
            initUserInfo();
        }
        if (resultCode == RESULT_OK && requestCode == REQUEST_LOGIN) {
            //初始化用户头像
            initUserImage();
            showBGAccountBtn();
        }
        //图片返回处理
        if (resultCode == RESULT_OK && requestCode != REQUEST_LOGIN) { // 如果返回码是可以用的
            switch (requestCode) {
                case TAKE_PICTURE:
                    if (tempUri != null) {
                        startPhotoZoom(tempUri);
                    }
                    break;
                case CHOOSE_PICTURE:
                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
                default:
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (tempUri != null) {
                ImageUtils.deleteImageFile(tempUri.getPath());
            }
           // Toast.makeText(getActivity(), "取消", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 刷新余额
     */
    private void refreshBlance() {
        RetrofitManager.getInstance()
                .getApi()
                .getCustomerInfo()
                .compose(ThreadTransformer.<UserInfoResponse>applySchedulers())
                .subscribe(new BaseSubscriber<UserInfoResponse>() {
                    @Override
                    public void onSuccess(UserInfoResponse userInfoResponse) {
                        balance.setText(userInfoResponse.getBody().getCustomerDetails().balance);
                    }
                });
    }

    /**
     * 初始化用户头像
     */
    private void initUserImage() {
        String image_url = (String) Utils.getParam(getActivity(), IMAGE_URL, "");
        if (TextUtils.equals(image_url, "")) {
            GlideUtils.loadImage(getActivity(), R.drawable.portrait, userImage);
        } else
            GlideUtils.loadImage(getActivity(), image_url, userImage);
    }

    /**
     * 后台用户隐藏给予按钮
     */
    private void showBGAccountBtn() {
        String isBackstageAccount = (String) Utils.getParam(getActivity(), IS_BG_ACCOUNT, "");
        btnActionGrant.setVisibility(TextUtils.equals(isBackstageAccount, "true") ? View.GONE : View.VISIBLE);
    }


    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.user_login:
                login();
                break;
            case R.id.logout:
                logout();
                break;
            case R.id.user_help://乐购说明
                HelpCenterActivity.getInstance(getContext());
                break;
            case R.id.action_grant://赠与
                GrantActivity.getInstance(getContext(), GrantActivity.class);
                break;
            case R.id.action_charge://充值
                ChargeActivity.getInstance(getContext(), "UserFragment");
//                Intent intent=new Intent(getContext(), ChargeActivity.class);
//                startActivityForResult(intent,UPDATE_BALANCE);
                break;
            case R.id.user_grid_friend://分享
              shareFriend();
                break;
            case R.id.user_grid_address://收货地址
                Intent adddIt = new Intent(getContext(), AddressActivity.class);
                startActivity(adddIt);
                break;
            case R.id.user_grid_dollar://我的佣金
                Intent goMyCommission = new Intent(getContext(), MyCommissionActivity.class);
                startActivity(goMyCommission);
                break;
            case R.id.user_grid_balance://我的福分
                Intent goMyBalance = new Intent(getContext(), MyPointActivity.class);
                startActivity(goMyBalance);
                break;
            case R.id.user_grid_shoppinglist://我的购买记录，三种记录的数据一起加载，现在做的分页是五条数据一页，也就是说，
                // 最坑爹的情况是同时加载15条数据，而每条数据的乐购码的长度是个坑，如果乐购码过长，容易给后台造成负担
                Intent goShopHistory = new Intent(getContext(), MyBuyRecordsActivity.class);
                startActivity(goShopHistory);
                break;
            case R.id.user_grid_medal://我的中奖记录
                WinRecordActivity.getInstance(getContext());
                break;
            case R.id.user_grid_personaldata://个人资料
                PersonalDataActivity.getInstance(getContext(), PersonalDataActivity.class);
                break;
            case R.id.user_grid_present://任务奖励
                MissionActivity.getInstance(getContext());
                break;
            case R.id.user_grid_accountdetail://账户明细
                AccountDetailsActivity.getInstance(getContext(), AccountDetailsActivity.class);
                break;
            case R.id.user_grid_share://我的晒单
                MyShareActivity.getInstance(getContext());
                break;
            case R.id.user_grid_myintro://我的邀请码
                IntroCodeActivity.getInstance(getContext());
                break;
            case R.id.user_grid_grant://赠与记录
                GrantRecordActivity.getInstance(getContext());
                break;
            case R.id.user_avatar:
                if (Build.VERSION.SDK_INT >= 23) {
                    PermissionGen.with(this)
                            .addRequestCode(200)
                            .permissions(
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .request();
                } else {
                    setUserImage();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
    }

    private class ShareDlgListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_close:
                    if (dlg != null)
                        dlg.dismiss();
                    break;
                case R.id.bt_share_wx:
                    //已完成TODO
                    new ShareFriendUtil(getContext()).sendWebPage(false);
                    break;
                case R.id.bt_share_wxc:
                    //已完成TODO
                    new ShareFriendUtil(getContext()).sendWebPage(true);
                    break;
                case R.id.bt_share_scan:
                    //
                    break;
            }
        }
    }

    /**
     * 用户信息bean
     */
    public class UserInfoResponse extends ResponseGson<UserInfoBody> {
    }

    private class UserInfoBody {
        public UserInfoBean getCustomerDetails() {
            return customerDetails;
        }

        UserInfoBean customerDetails;

        private class UserInfoBean {
            String nickname;
            String cellphone;
            String balance;
            String point;
            String commission;

            public UserInfoBean() {
                nickname = "";
                cellphone = "";
                balance = "0.00";
                point = "0.00";
                commission = "0.00";
            }
        }
    }


    /**
     * 上传头像
     */
    @PermissionSuccess(requestCode = 200)
    private void setUserImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("上传头像");
        String[] items = new String[]{"本地选择", "拍照"};
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE:
                        Intent choosePictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        choosePictureIntent.setType("image/*");
                        getActivity().startActivityForResult(choosePictureIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE:
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        tempUri = Uri.fromFile(ImageUtils.createImageFile());
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                        getActivity().startActivityForResult(takePictureIntent, TAKE_PICTURE);
                        break;
                    default:
                        break;
                }
            }
        });
        builder.create().show();
    }

    /**
     * 获取权限失败提示
     */
    @PermissionFail(requestCode = 200)
    private void onPermissionFail() {
        Toast.makeText(getActivity(), "需要获取权限才能继续", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    /**
     * 裁剪图片
     *
     * @param uri
     */
    private void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.d(TAG, "The uri is not exist.");
        } else {
            tempUri = uri;
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri, "image/*");
            // 设置裁剪
            intent.putExtra("crop", "true");
            // aspectX aspectY 是宽高的比例
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            // outputX outputY 是裁剪图片宽高
            intent.putExtra("outputX", 150);
            intent.putExtra("outputY", 150);
            intent.putExtra("return-data", true);
            (getActivity()).startActivityForResult(intent, CROP_SMALL_PICTURE);
        }

    }

    /**
     * 设置头像图片
     *
     * @param data
     */
    private void setImageToView(Intent data) {
        // 上传至服务器
        // ... 可以在这里把Bitmap转换成file，然后得到file的url，做文件上传操作
        // 注意这里得到的图片已经是圆形图片了
        // bitmap是没有做个圆形处理的，但已经被裁剪了
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            String image_url = ImageUtils.savePhoto(photo, Environment
                    .getExternalStorageDirectory().getAbsolutePath(), String
                    .valueOf(System.currentTimeMillis()));
            upLoadUserImage(image_url);
            ImageUtils.deleteImageFile(tempUri.getPath());
        }
    }

    /**
     * 上传图片
     */
    private void upLoadUserImage(String iamge_url) {
        File file = new File(iamge_url);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("picture", file.getName(), requestFile);
        final ZProgressHUD zProgressHUD = ZProgressHUD.getInstance(getActivity()).setMessage("正在上传");
        zProgressHUD.show();
        RetrofitManager.getInstance()
                .getApi()
                .upload(body)
                .compose(ThreadTransformer.<ImageBean>applySchedulers())
                .subscribe(new BaseSubscriber<ImageBean>() {
                    @Override
                    public void onSuccess(ImageBean imageBean) {
                        Utils.setParam(getActivity(), IMAGE_URL, imageBean.getBody().getPathUrl());
                        GlideUtils.loadImage(getActivity(), imageBean.getBody().getPathUrl(), userImage);
                        zProgressHUD.dismiss();
                    }
                });
    }

    /**
     * 头像bean
     */
    public class ImageBean extends ResponseGson<ImageBean> {

        /**
         * pathUrl : C:/opt/yiyuanlegou/common/uploaded_images/cd1a3e33ic_user.jpg
         */

        private String pathUrl;
        private String isBackstageAccount;

        public String getPathUrl() {
            return pathUrl;
        }

        public void setPathUrl(String pathUrl) {
            this.pathUrl = pathUrl;
        }

        public String getIsBackstageAccount() {
            return isBackstageAccount;
        }

        public void setIsBackstageAccount(String isBackstageAccount) {
            this.isBackstageAccount = isBackstageAccount;
        }
    }
}
