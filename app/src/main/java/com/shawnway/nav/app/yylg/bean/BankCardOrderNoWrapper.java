package com.shawnway.nav.app.yylg.bean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/13 0013.
 */
public class BankCardOrderNoWrapper extends ResponseGson<BankCardOrderNoWrapper.CreatOrderNoBody> {

    public class CreatOrderNoBody{
        public int amount;
        public String createdDate;
        public ArrayList<cycleItemBean> cycleItems;
        public int id;
        public String location;
        public String orderNo;//订单号码
        public String SorderStatus;//订单状态
        public String updatedDate;
        public int ver;
    }

    public class cycleItemBean{
        public int count;
        public String createdDate;
        public long drawCycle_id;
        public int id;
        public String updatedDate;
        public int ver;
    }
}
