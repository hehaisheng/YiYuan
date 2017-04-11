package com.shawnway.nav.app.yylg.bean;

/**
 * Created by Eiffel on 2016/3/22.
 */
public class CustomerInfoWrapper extends ResponseGson<CustomerInfoWrapper.CustomerInfoBody> {

    public class CustomerInfoBody {
        public String customName;//用户名customName
        public String buyRecAmount;//乐购记录数量buyRecAmount
        public String earnAmount;//获得中奖商品数量earnAmount
        public String userImg;//用户头像
    }
}
