package com.shawnway.nav.app.yylg.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/4 0004.
 */
public class CashOutRecordResponse extends ResponseGson<CashOutRecordResponse.CashOutBody> {

public class CashOutBody {
    public int page;
    public int pageSize;
    public int totalPage;
    public int totalRecord;
    public ArrayList<CashOutRecordBean> list;
}

public static class CashOutRecordBean {
    public String createTime;//创建时间
    public String no;//提现的银行账号
    public double amount;//提现金额
    public String withdrawType;//哪个银行
    public String withdrawStatus;//提现进度
}
}
