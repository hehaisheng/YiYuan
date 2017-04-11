package com.shawnway.nav.app.yylg.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.tool.StringUtils;

/**
 * Created by Eiffel on 2015/11/26.
 */
public class MoneyPicker extends LinearLayout {
    private static final String TAG = "MoneyPicker";
    private final Context context;
    private int selectedMoney;
    private EditText inputer;
    private RadioGroup group;

    public MoneyPicker(Context context) {
        this(context, null);

    }

    public MoneyPicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MoneyPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initLayout();
    }

    private void initLayout() {
        LayoutInflater.from(context).inflate(R.layout.layout_moneypicker, this, true);
        group = (RadioGroup) findViewById(R.id.radioGroup);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                inputer.setText("");
            }
        });
        inputer = (EditText) findViewById(R.id.money_inputer);
//        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//
//
//            }
//        });
    }

    public int getMoney() {
        String inputmoney = inputer.getText().toString();
        if (!StringUtils.isEmpty(inputmoney)) {
            try {
                return selectedMoney = Integer.parseInt(inputmoney);
            } catch (NumberFormatException e) {
                Log.d(TAG, "no num in inputer" + e.getStackTrace().toString());
            }
        }
        try {
            selectedMoney = Integer.parseInt((String) group.findViewById(group.getCheckedRadioButtonId()).getTag());

        } catch (ClassCastException e) {
            Log.d(TAG, "should set int tag to the radiobutton in moneypicker" + e.getStackTrace().toString());
        }

        return selectedMoney;

    }


}


