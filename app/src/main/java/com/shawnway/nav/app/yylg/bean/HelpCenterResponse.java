package com.shawnway.nav.app.yylg.bean;

/**
 * Created by Eiffel on 2015/12/31.
 */
public class HelpCenterResponse extends ResponseGson<HelpCenterResponse.HelpCenterBody> {


    public class HelpCenterBody {
        public String registedNumber;
        public String shopName;
        public String copyright;
        public String commissionRate;
        public String hotline;
        public String guideInfo;
        public String serviceProtocol;
    }
}
