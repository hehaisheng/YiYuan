package com.shawnway.nav.app.yylg.bean;

/**
 * Created by Eiffel on 2015/12/26.
 */
public class GoodDetail extends GoodBean {

   public long latestDrawCycleID;
   public long latestProductID;
    public   int winnerId;
    public String parValue;
    public String saleValue;
    public String drawType;
    public String winnerPurchedDate;
    public String[] productImages;//产品图片
    public long latestDrawCycle;
    public String winnerLocation;

    public CarItemBean toCarItemBean() {
        GoodBean bean=new GoodBean(this);
        return new CarItemBean(bean);
    }


}
