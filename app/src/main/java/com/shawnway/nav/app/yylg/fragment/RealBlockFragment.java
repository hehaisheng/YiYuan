package com.shawnway.nav.app.yylg.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.activity.PhysicalActivity_pri;
import com.shawnway.nav.app.yylg.bean.RealBlockWrapper;
import com.shawnway.nav.app.yylg.bean.ResponseGson;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.tool.Constants;
import com.shawnway.nav.app.yylg.tool.StringUtils;
import com.shawnway.nav.app.yylg.tool.ToastUtil;
import com.shawnway.nav.app.yylg.tool.Utils;
import com.shawnway.nav.app.yylg.tool.VolleyErrorHelper;
import com.shawnway.nav.app.yylg.view.ClearEditView;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Eiffel on 2015/11/9.
 */
public class RealBlockFragment extends Fragment {

    private static final String TAG = "RealBlockFragment";
    private GridView mMenuListView;
    /**
     * 菜单列表
     */
    private String[] mMenuTitles = {"全部商品", "电脑/办公","手机/数码", "家用/电器", "化妆/个护","钟表/首饰",   "汽车/配件", "母婴/玩具","其它商品"};
    private CatePair[] cateData;
    private int mCurPosition;
    private SweetAlertDialog mDlg;

    private TextView mTv;
    private String[] kind;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goodlist_vertical_layout, container, false);
        initView(view);

        initListData(0);
        return view;
    }

    private void initListData(final int defSelected) {
        if(!Constants.DEBUG){
            cateData = new CatePair[mMenuTitles.length];
            String[] kind = new String[mMenuTitles.length];
            for(int i = 0;i<mMenuTitles.length;i++){
                CatePair bean = new CatePair();
                bean.setLabel(mMenuTitles[i]);
                bean.setValue(i+"");
                cateData[i] = bean;
                kind[i] = cateData[i].getLabel();
            }

            mMenuListView.setAdapter(new ArrayAdapter<>(getActivity(),
                    R.layout.layout_goodlist_draw_list_item, kind));
        }else {
            VolleyTool.getInstance(getContext()).sendGsonRequest(this, CategoryDetailWrapper.class, Constants.REALBLOCK_LIST, new Response.Listener<CategoryDetailWrapper>() {
                @Override
                public void onResponse(CategoryDetailWrapper response) {
                    cateData = response.getBody().getPairs();
                    if (cateData == null) {//防止没有数据的时候crash掉,但是这栏目一般都是有的，不会为空的
                        ToastUtil.showShort(getContext(), "服务器出了点小问题，请稍后再试...");
                        return;
                    }
                    kind = new String[cateData.length];
                    for (int i = 0; i < cateData.length; i++) {
                        kind[i] = cateData[i].getLabel();
                        Utils.setParam(getContext(),"realLength",cateData.length);
                        Utils.setParam(getContext(),"real"+i,cateData[i].getValue());
                    }

                    mMenuListView.setAdapter(new MyGridAdapter());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    ToastUtil.showShort(getContext(), VolleyErrorHelper.getMessage(volleyError, getContext()));

                }
            });

        }
    }
    private class MyGridAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return kind.length;
        }

        @Override
        public Object getItem(int position) {
            return kind[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            MyViewHolder holder;
            if(convertView == null){
                holder = new MyViewHolder();
                convertView = View.inflate(getContext(),R.layout.realblock_list_item,null);
                holder.tv = (TextView) convertView.findViewById(R.id.realblock_item_item_tv);
                convertView.setTag(holder);
            }else{
                holder = (MyViewHolder) convertView.getTag();
            }
            holder.tv.setText(kind[position]);
            return convertView;
        }
    }

    private class MyViewHolder{
        private TextView tv;
    }

    private void initView(View view) {
//        mMenuListView = (ListView) view.findViewById(R.id.left_drawer);
        mMenuListView = (GridView) view.findViewById(R.id.realblock_list);
        mTv = (TextView) view.findViewById(R.id.realblock_item_item_tv);
        mMenuListView.setOnItemClickListener(new DrawerItemClickListener());
    }


    /**
     * 切换主视图区域的Fragment
     *
     * @param position
     */
    private void selectItem(int position) {
        mCurPosition=position;
        String pwd=(String) Utils.getParam(getContext(),cateData[position].getValue(),"");//保存的密码
        if (StringUtils.isEmpty(pwd)){
            showValidateDlg(false,cateData[position].getValue());//显示dialog
        }else {
            String blockValue=cateData[position].getValue();
            setupRealContent(blockValue,(String) Utils.getParam(getContext(), blockValue, ""));
        }
        // 更新选择后的item和title，然后关闭菜单
        mMenuListView.setItemChecked(position, true);
    }

    /**
     * 要进行密码的判断，写过网络请求
     * 判断密码正确要进行页面的跳转，跳转到PhysicalActivity页面
     * 判断的结果，如果是密码不正确，则要弹吐司提醒用户密码错误
     * @param blockValue  第blockValue个实体区
     * @param pwd  实体区的密码
     */
    private void setupRealContent(final String blockValue, final String pwd) {

        Map<String, String> params = new HashMap<>();
        params.put("value", blockValue);
        params.put("pwd", pwd);
        params.put("page", "1");
        params.put("pageSize", "10");
        VolleyTool.getInstance(getContext()).sendGsonRequest(this, RealBlockWrapper.class, Constants.REALBLOCK, params, new Response.Listener<RealBlockWrapper>() {
            @Override
            public void onResponse(RealBlockWrapper response) {
                if (response.getCode().equals(VolleyTool.VALIDATE_ERROR)) {
                    showValidateDlg(true, cateData[mCurPosition].getValue());
                }
                else {
                    //需确保回调该方法前不再修改密码重发请求，否则容易保存失败
                    //修改密码重发时需要cancel旧请求
                    saveRealBlockPWD(blockValue, pwd);
                    String physicalName = cateData[mCurPosition].getLabel();//获取点击的实体区的名字

                    Intent intent = new Intent(getActivity(), PhysicalActivity_pri.class);
                    intent.putExtra("physicalName",physicalName);
                    intent.putExtra("value", blockValue);
                    intent.putExtra("pwd", pwd);
                    intent.putExtra("page", "1");
                    intent.putExtra("pageSize", "10");
                    getActivity().startActivity(intent);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                NetworkResponse response = volleyError.networkResponse;
                if (response.data != null) {
                    ResponseGson errorResp = new Gson().fromJson(
                            new String(response.data),
                            ResponseGson.class);
                    if (errorResp != null && errorResp.getCode().equals(VolleyTool.VALIDATE_ERROR)) {
                        ToastUtil.show(getContext(),"网络错误："+VolleyTool.VALIDATE_ERROR,Toast.LENGTH_SHORT);
                    }

                }
            }
        });
    }

    private RealBlockListFragment fragment;

    private Fragment setContentFragment() {
        if (fragment == null) {
            fragment = new RealBlockListFragment();
            Bundle bundle=new Bundle();
            bundle.putString("value",Constants.VALUE);
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    private void saveRealBlockPWD(String value, String pwd) {
        Utils.setParam(getContext(),value,pwd);
    }

    private void showValidateDlg(final boolean error, final String blockValue) {
        if (error){
            //已完成TODO DO SOMETHING
            ToastUtil.show(getContext(),"密码错误，请重新输入", Toast.LENGTH_SHORT);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        View view = View.inflate(getContext(),R.layout.dialog_realblock,null);

        //得到输入的密码的fbc
        final ClearEditView CETpwd = (ClearEditView) view.findViewById(R.id.dialog_realblock_pwd);
        //得到客服电话的fbc
        TextView phone = (TextView) view.findViewById(R.id.dialog_realblock_phone);
        //进入按钮的fbc
        Button btnOk = (Button) view.findViewById(R.id.positiveButton);
        //取消按钮的fbc
        Button btnCancle = (Button) view.findViewById(R.id.negativeButton);

        builder.setView(view);
        final AlertDialog dialog = builder.show();

        //点击客服电话，拨打电话
        phone.setText("4000403312");
        final String phoneNum = phone.getText().toString().trim();
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phoneNum));
                try {
                    getContext().startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnOk.setText("进入");
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取密码
                String pwd = CETpwd.getText().toString().trim();
                if(TextUtils.isEmpty(pwd)){//非空判断
                    Toast.makeText(getContext(),"密码不能为空",Toast.LENGTH_SHORT).show();
                    CETpwd.requestFocus();//密码输入框获取焦点，重新进入输入密码状态
                    return;
                }
                setupRealContent(blockValue,pwd);
                dialog.dismiss();
            }
        });

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }


    /**
     * ListView上的Item点击事件
     */
    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            selectItem(position);
        }
    }

    private class CategoryDetailWrapper extends ResponseGson<PhysicalDetailList> {
        @Override
        public PhysicalDetailList getBody() {
            return super.getBody();
        }
    }


    private class PhysicalDetailList {
        public CatePair[] getPairs() {
            return physicalDetailList;
        }

        public void setPairs(CatePair[] pairs) {
            this.physicalDetailList = pairs;
        }

        private CatePair[] physicalDetailList;

    }

    private class CatePair {
        private String label;
        private String value;
//        private String pwd;//为处理线程问题设置，除非服务器返回pwd否则一般存在

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }
}
