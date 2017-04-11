package com.shawnway.nav.app.yylg.bean;

import com.shawnway.nav.app.yylg.R;

import java.util.ArrayList;

/**
 * Created by Eiffel on 2016/3/28.
 */
public class SupportedBank {
    private String name;
    private int src;
    private String tag;

    public SupportedBank(String name,int src,String tag){
        this.name=name;
        this.src=src;
        this.tag=tag;
    }

    /**
     * 添加支持的银行
     农业银行
     中国银行
     建设银行
     邮储银行  -->TODO:没有，后台要添加
     招商银行
     平安银行  -->TODO:没有，后台要添加
     华夏银行
     光大银行
     浦发银行
     民生银行
     兴业银行
     工商银行
     * @return 支持的银行列表
     */
    public static ArrayList<SupportedBank> getSupportedCashOutBank(){
        ArrayList<SupportedBank> list=new ArrayList<>();
        list.add(new SupportedBank("请选择",R.drawable.sel,"CHOICE"));
        list.add(new SupportedBank("中国工商银行",R.drawable.bank_icbc,"ICBC"));
        list.add(new SupportedBank("中国建设银行",R.drawable.bank_ccb,"CCB"));
        list.add(new SupportedBank("中国农业银行",R.drawable.bank_abc,"ABC"));
        list.add(new SupportedBank("中国光大银行",R.drawable.bank_ceb,"CEB"));
        list.add(new SupportedBank("华夏银行",R.drawable.bank_hxb,"HXB"));
        list.add(new SupportedBank("中国邮政储蓄银行",R.drawable.bank_psbc,"PSBC"));// -->TODO:没有，后台要添加
        list.add(new SupportedBank("中国民生银行",R.drawable.bank_cmbc,"CMBC"));
        list.add(new SupportedBank("浦发银行",R.drawable.bank_spdb,"SPDB"));
        list.add(new SupportedBank("兴业银行",R.drawable.bank_cib,"CIB"));
        list.add(new SupportedBank("招商银行",R.drawable.bank_cmb,"CMB"));
        list.add(new SupportedBank("中国银行",R.drawable.bank_bc,"BC"));
        list.add(new SupportedBank("平安银行",R.drawable.bank_pab,"PAB"));// -->TODO:没有，后台要添加
        return list;
    }

    public String getName() {
        return name;
    }

    public int getSrc() {
        return src;
    }

    public String getTag() {
        return tag;
    }
}
