package com.shawnway.nav.app.yylg.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.activity.LoginActivity;
import com.shawnway.nav.app.yylg.activity.RetrievePwdActvitity;
import com.shawnway.nav.app.yylg.base.BaseSubscriber;
import com.shawnway.nav.app.yylg.bean.HeaderGson;
import com.shawnway.nav.app.yylg.bean.ResponseGson;
import com.shawnway.nav.app.yylg.net.RetrofitManager;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.Hook;
import com.shawnway.nav.app.yylg.tool.ThreadTransformer;
import com.shawnway.nav.app.yylg.tool.ToastUtil;
import com.shawnway.nav.app.yylg.tool.Utils;
import com.shawnway.nav.app.yylg.tool.VolleyErrorHelper;

import java.util.HashMap;

/**
 * 登录的fragment页面，在登录成功之后每25分钟自动登录一次，因为后台那里的登录状态只能够保持大概30分钟就会挂掉
 */
public class LoginWtihPwdFrag extends MyFragment implements OnClickListener {
    private static final String TAG = "LoginWithPwdFrag";
    private TextView pwd;
    private TextView phone;
    private CheckBox remember;
    private boolean remember_me;

    public static LoginWtihPwdFrag getInstance(boolean clear) {
        LoginWtihPwdFrag frag = new LoginWtihPwdFrag();
        Bundle bundle = new Bundle();
        bundle.putBoolean("clear", clear);
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_with_pwd,
                container, false);
        initView(view);
        setListener(view);
        Bundle bundle = getArguments();
//        if (bundle != null && !bundle.getBoolean("clear", false))
        initRemember();
        return view;
    }

    private void initView(View view) {
        phone = (TextView) view.findViewById(R.id.login_input_pwd_phone);
        pwd = (TextView) view.findViewById(R.id.login_input_pwd_password);
        remember = (CheckBox) view.findViewById(R.id.checkbox);
        String acc = (String) Utils.getParam(getContext(), "phone", "");
        if (!acc.equals("")) {
            phone.setText(acc);
        }
    }

    private void initRemember() {
        String acc = (String) Utils.getParam(getContext(), "acc", "");
        String dwp = (String) Utils.getParam(getContext(), "dwp", "");
        boolean rember = (boolean) Utils.getParam(getContext(), "rem", false);
        if (acc != "" && dwp != "" && rember) {
            remember.setChecked(true);
            phone.setText(acc);
            pwd.setText(dwp);
        }
    }

    private void setListener(final View view) {
        view.findViewById(R.id.login_pwd_commit).setOnClickListener(this);
        view.findViewById(R.id.login_bt_pwd_find).setOnClickListener(this);
        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ((TextView) view.findViewById(R.id.tv_checkbox)).setTextColor(
                        getResources().getColor(isChecked ? R.color.app_textColor : R.color.divider_color));
            }
        });
        view.findViewById(R.id.tv_checkbox).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!remember.isChecked()) {
                    remember_me = true;
                } else {
                    remember_me = false;
                }
                remember.setChecked(!remember.isChecked());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_pwd_commit:
                login();
                break;
            case R.id.login_bt_pwd_find:
                RetrievePwdActvitity.getInstance(getContext());
            default:
                break;
        }
    }

    private void login() {
        final String acc = phone.getText().toString();
        final String dwp = pwd.getText().toString();

        if (isFormCorrect(phone.length(), dwp.length())) {
            if (!Constants.dummy) {

                HashMap<String, String> map = new HashMap<>();
                map.put("username", acc);
                map.put("password", dwp);
                map.put("remember-me", (Boolean) Utils.getParam(getContext(), "rem", false) ? "Yes" : "No");
                Hook.getInstance(getContext()).login(acc, dwp,
                        new Listener<ResponseGson>() {

                            @Override
                            public void onResponse(ResponseGson response) {
                                if (!Utils.handleResponseError(getActivity(), response)) {
                                    success(acc);
                                }

                            }
                        }, new ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                String erromsg = VolleyErrorHelper.getMessage(error, getContext());
                                if (erromsg != null)
                                    ToastUtil.showShort(getContext(), erromsg);
                                else neterror();
                            }
                        });


            } else {
                success(acc);
            }

        }

    }

    private void savePwd(String acc, String pwd, boolean remenber_me, UserFragment.ImageBean imageBean) {
        Utils.savePWDInfo(getContext(), acc, pwd, remenber_me, imageBean.getPathUrl(), imageBean.getIsBackstageAccount());
    }

    private boolean isFormCorrect(int phone, int pwd) {
        if (phone < 6) {
            ToastUtil
                    .show(getActivity(),
                            getResources().getString(
                                    R.string.wrong_phone_format_error), 1000);
            return false;
        } else if (pwd < 6) {
            ToastUtil.show(getActivity(),
                    getResources().getString(R.string.wrong_pwd_format_error),
                    1000);
            return false;
        }

        return true;
    }

    protected void neterror() {
        ToastUtil.show(getActivity(),
                "账号密码错误请重新输入", 1000);
    }


    protected void success(final String acc) {
        RetrofitManager.getInstance()
                .getApi()
                .retrieveUserImge()
                .compose(ThreadTransformer.<UserFragment.ImageBean>applySchedulers())
                .subscribe(new BaseSubscriber<UserFragment.ImageBean>() {
                    @Override
                    public void onSuccess(UserFragment.ImageBean imageBean) {
                        savePwd(acc, pwd.getText().toString(), remember_me, imageBean.getBody());
                        Utils.savephone(getContext(), acc);
                        ToastUtil.show(getActivity(),
                                getResources().getString(R.string.login_success), 1000);
                        Intent intent = new Intent(Constants.ACTION_LOGIN);
                        getActivity().sendBroadcast(intent);
                        getActivity().setResult(Activity.RESULT_OK);
                        getActivity().finish();
                    }
                });
    }

}
