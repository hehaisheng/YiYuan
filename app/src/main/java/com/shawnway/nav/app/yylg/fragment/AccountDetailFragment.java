package com.shawnway.nav.app.yylg.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.bean.RememberMeResponse;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.Hook;
import com.shawnway.nav.app.yylg.tool.ToastUtil;
import com.shawnway.nav.app.yylg.tool.Utils;
import com.shawnway.nav.app.yylg.tool.VolleyErrorHelper;

/**
 * Created by Eiffel on 2015/12/15.
 */
public class AccountDetailFragment extends Fragment {
    ChargeDetailFragment chargeFrag;
    WasteDetailFragment wasteFrag;
    CashOutRecordFragment cashoutFrag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_detail, container, false);
        RadioGroup tabs =((RadioGroup) view.findViewById(R.id.radioGroup));
        tabs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_charge_detail:
                        if (chargeFrag == null)
                            chargeFrag = new ChargeDetailFragment();
                        getFragmentManager().beginTransaction().replace(R.id.content, chargeFrag).commit();
                        break;
                    case R.id.radio_waste_detail:
                        if (wasteFrag == null)
                            wasteFrag = new WasteDetailFragment();
                        getFragmentManager().beginTransaction().replace(R.id.content, wasteFrag).commit();
                        break;
                    case R.id.radio_cashout_detail:
                        if (cashoutFrag == null)
                            cashoutFrag = new CashOutRecordFragment();
                        getFragmentManager().beginTransaction().replace(R.id.content, cashoutFrag).commit();
                        break;
                }
            }
        });
        tabs.check(R.id.radio_charge_detail);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode== Constants.ACTION_REQUEST_LOGIN&&resultCode == Activity.RESULT_OK) {

        } else if (requestCode== Constants.ACTION_REQUEST_LOGIN){
            getActivity().finish();
        }
    }
}
