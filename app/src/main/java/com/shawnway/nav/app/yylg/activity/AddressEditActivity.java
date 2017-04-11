package com.shawnway.nav.app.yylg.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.bean.AddressBean;
import com.shawnway.nav.app.yylg.bean.ResponseGson;
import com.shawnway.nav.app.yylg.bean.WinRecBean;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.tool.CloseActivityUtil;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.FileUtils;
import com.shawnway.nav.app.yylg.tool.ToastUtil;
import com.shawnway.nav.app.yylg.tool.Utils;
import com.thuongnh.zprogresshud.ZProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Eiffel on 2015/11/18.
 */
public class AddressEditActivity extends MyActivity implements View.OnClickListener {
    private static final String TAG = "AddressEditActivity";
    public static final int MODE_EDIT = -1;
    public static final int MODE_ADDRESS_SELECT = 1;
    public static final int MODE_ADD = 2;

    //服务器的快递方式领奖分为了两个api，分别用于可选领奖方式的快递领奖跟不可选领奖方式的快递领奖
    //so need requestcode to tag the difference;
    public static  final int REQUEST_COMMON_SELECT=1;
    public static  final int REQUEST_CANSELECTI_SELECT=2;

    private Spinner provinceSpinner = null;  //省级（省、直辖市）
    private Spinner citySpinner = null;     //地级市
    private Spinner countySpinner = null;    //县级（区、县、县级市）
    ArrayAdapter<String> provinceAdapter = null;  //省级适配器
    ArrayAdapter<String> cityAdapter = null;    //地级适配器
    ArrayAdapter<String> countyAdapter = null;    //县级适配器
//    static int provincePosition = 3;

    private JSONObject citydata;
    private ArrayList<String> province;
    private ArrayList<String> city;
    private ArrayList<String> area;

    private AddressBean bean;
    private int mode = MODE_ADD;

    private WinRecBean winData;
    private int reqcode;

    //mode add
    public static void getInstance(Activity activity) {
        getInstance(activity,null,null,MODE_ADD,0);
    }

    //mode edit
    public static void getInstance(Activity activity, AddressBean address) {
        getInstance(activity,address,null,MODE_EDIT,0);
    }



    //mode select
    public static void getInstance(Activity activity, WinRecBean bean, int reqcode) {
        getInstance(activity, null, bean, MODE_ADDRESS_SELECT, reqcode);
    }


    public static void getInstance(Activity activity, AddressBean addressBean,WinRecBean winRecBean,int mode, int requestCode) {
        Intent intent = new Intent(activity, AddressEditActivity.class);
        if (addressBean!=null)intent.putExtra("address",addressBean);
        if (winRecBean!=null )intent.putExtra("win", winRecBean);
        intent.putExtra("mode", mode);
        intent.putExtra("reqcode",requestCode);
        activity.startActivityForResult(intent,requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CloseActivityUtil.add(AddressEditActivity.this);
        setContentView(R.layout.activity_address_edit);
        initData();
        initJsonData();
        initToolbar(bean == null);
        initView();


    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        //已通过getInstance设置了mode，不再自动改变mode
        if (bundle != null) {
            mode = bundle.getInt("mode", MODE_ADD);
            bean = (AddressBean) bundle.get("address");
            winData= (WinRecBean) bundle.get("win");
            reqcode =bundle.getInt("reqcode",REQUEST_CANSELECTI_SELECT);
        }
    }

    private void initView() {
        switch (mode) {
            case MODE_EDIT:
                initInputer(bean);
                break;
            case MODE_ADDRESS_SELECT:
                getDefAddress();
                View del = findViewById(R.id.ship_address_edit_commit);
                del.setVisibility(View.VISIBLE);
                del.setOnClickListener(this);
                break;
            case MODE_ADD:
                findViewById(R.id.ship_address_edit_del).setVisibility(View.GONE);
                break;
        }
    }

    private void initInputer(AddressBean bean) {
        ((EditText) findViewById(R.id.ship_address_edit_name)).setText(bean.getReceiver());
        ((EditText) findViewById(R.id.ship_address_edit_mobile)).setText(bean.getCellphone());
        ((EditText) findViewById(R.id.ship_address_edit_detail)).setText(bean.getAddress());
    }

    private void getDefAddress() {
        final ZProgressHUD progressHUD = ZProgressHUD.getInstance(this);
        progressHUD.setMessage("加载中");
        progressHUD.show();


        VolleyTool.getInstance(this).sendGsonRequest(this,AddressResponseWrapper.class, Constants.GET_ADDRESS_URL, new Response.Listener<AddressResponseWrapper>() {
            @Override
            public void onResponse(AddressResponseWrapper response) {
                if (!Utils.handleResponseError(AddressEditActivity.this, response, getString(R.string.wrong_address_false_to_get))) {
                    //If SUCCESS
                    if (response.getBody().delAddressDetails != null) {
                        initInputer(response.getBody().delAddressDetails);
                    }
                }
                progressHUD.dismiss();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtil.showShort(AddressEditActivity.this, getString(R.string.wrong_address_false_to_get));
                progressHUD.dismiss();
            }
        });
    }

    private void initJsonData() {
        String cityjson = FileUtils.readRaw(this, R.raw.city);
        try {
            citydata = new JSONObject(cityjson);
        } catch (JSONException e) {
            Log.e(TAG, "city.json is error");
            citydata = new JSONObject();
        }
    }

    private Gson gson = new Gson();

    /*
     * 设置下拉框
     */
    private void setSpinner() {
        provinceSpinner = (Spinner) findViewById(R.id.ship_address_edit_aname);
        citySpinner = (Spinner) findViewById(R.id.ship_address_edit_bname);
        countySpinner = (Spinner) findViewById(R.id.ship_address_edit_cname);

        //绑定适配器和值
        try {
            province = gson.fromJson(citydata.getJSONArray("p").toString(), new TypeToken<ArrayList<String>>() {
            }.getType());
        } catch (JSONException e) {
            province = new ArrayList<>();
        }
        province.add(0, getString(R.string.address_spinner_def_select));
        provinceAdapter = new ArrayAdapter<String>(AddressEditActivity.this,
                android.R.layout.simple_spinner_item, province);
        provinceSpinner.setAdapter(provinceAdapter);
        provinceSpinner.setSelection(0, true);  //设置默认选中项，此处为默认选中第4个值
        setCitySpinner();

        setAreaSpinner();

        //省级下拉框监听
        provinceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // 表示选项被改变的时候触发此方法，主要实现办法：动态改变地级适配器的绑定值
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                //position为当前省级选中的值的序号
                setCitySpinner();
                setAreaSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });


        //地级下拉监听
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                setAreaSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    /*
     * 设置下拉框
     */
    private void setSpinner(String pro, String cty, String ar) {

        try {
            provinceSpinner.setSelection(province.indexOf(pro), true);  //设置默认选中项，此处为默认选中第4个值
            citySpinner.setSelection(city.indexOf(cty), true);
            countySpinner.setSelection(area.indexOf(ar), true);
        } catch (IndexOutOfBoundsException e) {
            Log.e(TAG, e.getStackTrace().toString());
        } catch (Exception e) {
            Log.e(TAG, e.getStackTrace().toString());
        }
    }

    private void setCitySpinner() {
        String selectedcity = (String) provinceSpinner.getSelectedItem();

        try {
            city = gson.fromJson(citydata.getJSONObject("c").getJSONArray(selectedcity).toString(), new TypeToken<ArrayList<String>>() {
            }.getType());
        } catch (JSONException e) {
            Log.d(TAG, "selected" + selectedcity + "  but not found in json");
            city = new ArrayList<>();
        }
        city.add(0, getString(R.string.address_spinner_def_select));
        cityAdapter = new ArrayAdapter<String>(AddressEditActivity.this,
                android.R.layout.simple_spinner_item, city);
        citySpinner.setAdapter(cityAdapter);
        citySpinner.setSelection(0, true);  //默认选中第0个
    }

    private void setAreaSpinner() {
        String selectedPro = (String) provinceSpinner.getSelectedItem();
        String selectedCty = (String) citySpinner.getSelectedItem();
        String key = selectedPro + "-" + selectedCty;

        try {
            area = gson.fromJson(citydata.getJSONObject("a").getJSONArray(key).toString(), new TypeToken<ArrayList<String>>() {
            }.getType());
        } catch (JSONException e) {
            Log.d(TAG, "selected" + selectedPro + "  but not found in json");
            area = new ArrayList<>();
        }
        area.add(0, getString(R.string.address_spinner_def_select));
        countyAdapter = new ArrayAdapter<String>(AddressEditActivity.this,
                android.R.layout.simple_spinner_item, area);
        countySpinner.setAdapter(countyAdapter);
        countySpinner.setSelection(0, true);
    }


    private void initToolbar(boolean isInAddMode) {

        TextView centerText = (TextView) findViewById(R.id.top_text_center);
        centerText.setText(getResources().getString(isInAddMode ? R.string.title_ship_address_add : R.string.title_ship_address_edit));
        centerText.setVisibility(View.VISIBLE);
        ImageButton backButton = (ImageButton) findViewById(R.id.top_back);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(this);
        if (mode != MODE_ADDRESS_SELECT) {
            TextView save = (TextView) findViewById(R.id.action_blue_common);
            save.setText(getString(R.string.save));
            save.setVisibility(View.VISIBLE);
            save.setOnClickListener(this);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_back:
                finish();
                break;
            case R.id.action_blue_common:
                save();
                break;
            case R.id.ship_address_edit_del:
                delete();
                break;
            case R.id.ship_address_edit_commit:
                commitWinAddress();
                break;
        }

    }

    private void commitWinAddress() {
        //非空判断，只要收货地址中有一栏是空的就不能够确认收货地址
//        String winname=((EditText) findViewById(R.id.ship_address_edit_aname)).getText().toString().trim();
//        String winnum=((EditText) findViewById(R.id.ship_address_edit_mobile)).getText().toString().trim();
//        String winaddress=((EditText) findViewById(R.id.ship_address_edit_detail)).getText().toString().trim();

        if(((EditText) findViewById(R.id.ship_address_edit_name)).getText().length()<1 || ((EditText) findViewById(R.id.ship_address_edit_mobile)).getText().length()<1 || ((EditText) findViewById(R.id.ship_address_edit_detail)).getText().length()<1){
            ToastUtil.showShort(this,"请填写正确的收货信息");
            return;
        }
//        judgeWinnerInfo(winname,winnum,winaddress);





        Dialog dlg= new SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE).
                setTitleText(getString(R.string.select_address_dlg_title)).
                setContentText(getString(R.string.select_address_dlg_content)).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(final SweetAlertDialog sweetAlertDialog) {
                WinBody params = new WinBody();
                params.winprizeId = winData.getWinprizeId();
                params.receiverName = Utils.getEditTextStr(findViewById(R.id.ship_address_edit_name));
                params.receiverAddress = Utils.getEditTextStr(findViewById(R.id.ship_address_edit_detail));
                params.receiverCellPhone = Utils.getEditTextStr(findViewById(R.id.ship_address_edit_mobile));


                String url= reqcode==REQUEST_COMMON_SELECT?Constants.CHOOSE_COM_POST_URL:Constants.CHOOSE_POST_URL;

                VolleyTool.getInstance(AddressEditActivity.this).sendGsonPostRequest(this,ResponseGson.class, url, new Gson().toJson(params), new Response.Listener<ResponseGson>() {
                    @Override
                    public void onResponse(ResponseGson responseGson) {
                        if (!Utils.handleResponseError(AddressEditActivity.this, responseGson)) {
                            sweetAlertDialog.dismiss();
                            setResult(RESULT_OK);
                            ToastUtil.showShort(AddressEditActivity.this, getString(R.string.success_choose));
                            setResult(RESULT_OK);
                            MainActivity.getInstance(AddressEditActivity.this,4);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        ToastUtil.showShort(AddressEditActivity.this, getString(R.string.success_failed));
                        sweetAlertDialog.dismiss();
                    }
                });
            }
        }).setCancelText("取消");
        dlg.setCancelable(true);
        dlg.show();


    }

    private void delete() {

    }

    private void save() {
        String winname=((EditText) findViewById(R.id.ship_address_edit_name)).getText().toString().trim();
        String winnum=((EditText) findViewById(R.id.ship_address_edit_mobile)).getText().toString().trim();
        String winaddress=((EditText) findViewById(R.id.ship_address_edit_detail)).getText().toString().trim();

        Pattern p = Pattern.compile("[a-zA-Z]*");
        Matcher m = p.matcher(winaddress);
        if(winname.equals("")||winname.isEmpty())
        {
            ToastUtil.showShort(this,"收货人信息包含空格，请填写正确的收货人名");

        }
        else if(winnum.equals("")||winnum.isEmpty())
        {
            ToastUtil.showShort(this,"收货手机号码信息包含空格，请填写正确的手机号码");

        }
        else  if(winaddress.equals("")||winaddress.isEmpty())
        {
            ToastUtil.showShort(this,"收货地址信息包含空格，请填写正确的地址");
        }
        else if(m.matches())
        {
            ToastUtil.showShort(this,"收货地址信息包含英文，请填写正确的地址");
        }
        else {

            SaveBody params = new SaveBody();
            params.delAddressDetails.setReceiver(Utils.getEditTextStr(findViewById(R.id.ship_address_edit_name)));
            params.delAddressDetails.setAddress(Utils.getEditTextStr(findViewById(R.id.ship_address_edit_detail)));
            params.delAddressDetails.setCellphone(Utils.getEditTextStr(findViewById(R.id.ship_address_edit_mobile)));
            VolleyTool.getInstance(this).sendGsonPostRequest(this, ResponseGson.class, Constants.UPDATE_ADDRESS_URL, new Gson().toJson(params), new Response.Listener<ResponseGson>() {
                @Override
                public void onResponse(ResponseGson responseGson) {
                    if (!Utils.handleResponseError(AddressEditActivity.this, responseGson)) {
                        ToastUtil.showShort(AddressEditActivity.this, getString(R.string.save_success));
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        ToastUtil.showShort(AddressEditActivity.this, getString(R.string.save_failed));
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    ToastUtil.showNetError(AddressEditActivity.this);
                }
            });
        }


    }


    class WinBody {
        int winprizeId;
        String receiverName;
        String receiverAddress;
        String receiverCellPhone;
    }


    class SaveBody {
        AddressBean delAddressDetails = new AddressBean();
    }

    private class AddressResponseWrapper extends ResponseGson<AddressResponseBody> {
    }

    private class AddressResponseBody {
        AddressBean delAddressDetails;
    }

//    @Override
//    public Resources getResources() {
//        Resources res = super.getResources();
//        Configuration config=new Configuration();
//        config.setToDefaults();
//        res.updateConfiguration(config,res.getDisplayMetrics() );
//        return res;
//    }

    public void judgeWinnerInfo(String name,String mobile,String address)
    {
        if(name.equals("")||name.isEmpty())
        {
            ToastUtil.showShort(this,"收货人信息包含空格，请填写正确的收货人名");
            return;
        }
        if(mobile.equals("")||mobile.isEmpty())
        {
            ToastUtil.showShort(this,"收货手机号码信息包含空格，请填写正确的手机号码");
            return;
        }
        if(address.equals("")||address.isEmpty())
        {
            ToastUtil.showShort(this,"收货地址信息包含空格，请填写正确的地址");
            return;
        }
        Pattern pattern=Pattern.compile("[\\u4e00-\\u9fa5]");
        Matcher matcher=pattern.matcher(address);
        if(!matcher.matches())
        {
            ToastUtil.showShort(this,"收货地址信息包含英文，请填写正确的地址");
            return;
        }

    }
}