package com.shawnway.nav.app.yylg.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.bean.ResponseGson;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.StringUtils;
import com.shawnway.nav.app.yylg.tool.ToastUtil;
import com.shawnway.nav.app.yylg.tool.Utils;
import com.shawnway.nav.app.yylg.tool.VolleyErrorHelper;
import com.thuongnh.zprogresshud.ZProgressHUD;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eiffel on 2015/12/15.
 */
public class PerDataFragment extends Fragment implements View.OnClickListener {


    private static final String TAG = "PerDataFragment";
    private View layoutName;
    private View layoutPwd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        layoutName = view.findViewById(R.id.layout_modify_name);
        layoutPwd = view.findViewById(R.id.layout_modify_pwd);
        updateHeader();
        initRadioGroup(view);
        initListener();
        return view;
    }

    private void updateHeader() {
        VolleyTool.getInstance(getContext()).sendGsonRequest(this, NickResp.class, Constants.PERSONALDATA, new Response.Listener<NickResp>() {
            @Override
            public void onResponse(NickResp nickResp) {
                if (!Utils.handleResponseError(getActivity(), nickResp)) {
                    updateHeaderUi(nickResp);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
    }


    private void updateHeaderUi(NickResp nickResp) {
        ((TextView) getView().findViewById(R.id.tv_account)).setText(getString(R.string.account, nickResp.getBody().nickname));
    }

    private void initListener() {
        if (layoutName != null)
            layoutName.findViewById(R.id.bt_modify_nickname).setOnClickListener(this);
        if (layoutPwd != null) {
            layoutPwd.findViewById(R.id.bt_modify_pwd).setOnClickListener(this);
        }
    }

    private void initRadioGroup(View view) {
        ((RadioGroup) view.findViewById(R.id.radioGroup)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_modify_name:
                        layoutName.setVisibility(View.VISIBLE);
                        layoutPwd.setVisibility(View.GONE);
                        break;
                    case R.id.radio_modify_pwd:
                        layoutName.setVisibility(View.GONE);
                        layoutPwd.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_modify_nickname:
                modNick();
                break;
            case R.id.bt_modify_pwd:
                modPwd();
                break;
        }
    }

    private void modNick() {
        try {
            ZProgressHUD.getInstance(getContext());

            Map<String, String> params = new HashMap<>();

            String nick = Utils.getEditTextStr(layoutName.findViewById(R.id.inputer_nickname));
            if (StringUtils.isBlank(nick))
                ToastUtil.showShort(getContext(), getString(R.string.error_nick_null));
            else {
                params.put("nickName", URLEncoder.encode(nick, "UTF-8"));
                Log.d(TAG, "修改昵称时的参数：" + params);
                VolleyTool.getInstance(getContext()).sendGsonRequest(this, NickResp.class, Constants.MOD_NICK, params, new Response.Listener<NickResp>() {
                    @Override
                    public void onResponse(NickResp nickResp) {
                        if (!Utils.handleResponseError(getActivity(), nickResp)) {
                            ToastUtil.showShort(getContext(), getString(R.string.success_modify));
                            ZProgressHUD.dismis();
                            getActivity().sendBroadcast(new Intent(Constants.ACTION_LOGIN));
                            updateHeaderUi(nickResp);
                            getActivity().finish();

                        }
                    }
                }, new Response.ErrorListener() {


                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        ToastUtil.showShort(getContext(), "系统错误,请重试");
                        ZProgressHUD.dismis();
                    }
                });
            }
        } catch (UnsupportedEncodingException e) {
            Log.e(e.getClass().getSimpleName(), "unsupport encoding");
            ZProgressHUD.dismis();
        }
    }


    private void modPwd() {
        ZProgressHUD.getInstance(getContext());
        Map<String, String> params = new HashMap<>();
        params.put("originalPwd", Utils.getEditTextStr(layoutPwd.findViewById(R.id.inputer_old_pwd)));
        params.put("newPwd", Utils.getEditTextStr(layoutPwd.findViewById(R.id.inputer_new_pwd)));
        VolleyTool.getInstance(getContext()).sendGsonPostRequest(this, NickResp.class, Constants.MOD_PWD, params, new Response.Listener<NickResp>() {
            @Override
            public void onResponse(NickResp nickResp) {
                if (!Utils.handleResponseError(getActivity(), nickResp)) {
                    ToastUtil.showShort(getContext(), getString(R.string.success_modify));
                    ZProgressHUD.dismis();
                    getActivity().finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtil.showShort(getContext(), "修改失败：" + VolleyErrorHelper.getMessage(volleyError, getContext()));
                ZProgressHUD.dismis();
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.ACTION_REQUEST_LOGIN && resultCode == Activity.RESULT_OK) {
            updateHeader();
        } else if (requestCode == Constants.ACTION_REQUEST_LOGIN) {
            getActivity().finish();
        }
    }

    class NickResp extends ResponseGson<NickBody> {

    }

    class NickBody {
        String nickname;
    }
}
