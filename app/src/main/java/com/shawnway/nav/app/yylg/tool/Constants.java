package com.shawnway.nav.app.yylg.tool;

import android.os.Environment;

import com.shawnway.nav.app.yylg.activity.LoginActivity;
import com.shawnway.nav.app.yylg.activity.MainActivity;

public class Constants {
    //resource string
    public final static String respath = Environment.getExternalStorageDirectory().getPath() + "/RenYiGou/";
//    public final static String HOST_STRING = "http://www.renyigo.com:8080/";
    //public final static String HOST_STRING = "http://www.tescochn.com/";//原来一元乐购的host

    //    public final static String HOST_STRING = "http://120.24.169.190/yiyuanlegou/";//四月的服务器，哈哈被服务器的病毒折磨了一个月了
  //  public final static String HOST_STRING = "http://120.25.97.222/";//4.27之后的服务器

   public final static String HOST_STRING = "http://tescochn.com/";//生产环境


   //192.168.31.172:8080谢聪的开发
  //public final static String HOST_STRING = "http://122.112.210.44:8080/";//树煜的服务器

// public final static String HOST_STRING = "http://192.168.31.172:8080/";//谢聪的开发
   //http://192.168.31.172:8080/admin/logon?error=true

//    public final static String HOST_STRING = "http://192.168.31.122/";
//    public final static String HOST_STRING = "http://fy983951558.imwork.net/";//后台的机子
//    public final static String HOST_STRING = "http://192.168.31.122:8080/";

//        public static final String APP_ID ="wx0acdf543e3ba149c" ;
//    public static  final String APP_ID="wx9f35391e3d92eef4";//yylg，正常的

   // wxdedfac7c04f7ee9f
 public static final String APP_ID = "wxdedfac7c04f7ee9f";//wxd3a1cdf74d0c41b3 微信支付测试用的appid    wxdedfac7c04f7ee9f 一元乐购的appid

    public final static String VALUE = 0 + "";

    //控制本地调试和联网调试
    public final static boolean dummy = false;
    //Debug
    public final static boolean DEBUG = true;


    public static final int DEF_PAYTIMEOUT = 10;
    public static final int DEF_TIMEOUT = 16;
//    public static final int DEF_PAGE_SIZE = 20;
    public static final int DEF_PAGE_SIZE = 20;
    public static final int TEN_BLOCK_PAGE_SIZE = 10;


    public static final String COOKIES_STORE_NAME = "cookie-store";
    public static final String TOKEN = "remember-me";
    public static final String JSESSIONID = "JSESSIONID";


    public static final String VALIDATE_URL = HOST_STRING + "registraion/validate";
    public static final String CAPTCHA_URL = HOST_STRING + "nonsecure/jcaptcha";
    public static final String LOGIN_URL = HOST_STRING + "login";
    public static final String REGIST_URL = HOST_STRING + "registraion/register";
    public static final String ACTION_UPDATE_BALANCE="com.shawnway.nav.app.yylg.updatebalance";
    public static final String ACTION_LOGIN = "com.shawnway.nav.app.yylg.login";
    public static final String ACTION_LOGOUT = "com.shawnway.nav.app.yylg.logout";
    public static final String ACTION_CART_CHANGE = "com.shawnway.nav.app.yylg.cartchange";
    public static final String ACTION_CART_NUM_CHANGE = "com.shawnway.nav.app.yylg.cartnumchange";
    public static final String ACTION_CHANGE_HOME_INDEX = "com.shawnway.nav.app.yylg.homeindexchange";
    public static final String ACTION_PAY_SUCCESS = "com.shawnway.nav.app.yylg.paysuccess";
    public static final String ACTION_GOHOT = "com.shawnway.nav.app.yylg.gohot";
    public static final String ACTION_AUTOLOGIN = "com.shawnway.nav.app.yylg.autologin";

    public static final int ACTION_REQUEST_LOGIN = 500;

    //http://122.112.210.44:8080/preOrder
    public static final String PAY_PARAM = HOST_STRING + "preOrder";
    public static final String RESEND_URL = HOST_STRING + "registraion/resend";
    public static final String DETAIL_URL = HOST_STRING + "drawcycledetails";
    public static final String CATEGORY_URL = HOST_STRING + "drawcycle?searchBy=all";
    public static final String CATEGORY_DIRETORY_URL = HOST_STRING + "category/list";//获取分类列表目录
    public static final String RETRIEVEBALANCE_URL = HOST_STRING + "customer/balance";
    public static final String PAY_URL = HOST_STRING + "customer/checkout";
    public static final String BASE_CATAGORY_URL = HOST_STRING + "drawcycle";
    public static final String REMEMBER_ME_URL = HOST_STRING + "customer/remember-me";
    public static final String VIEWDETAIL_URL = HOST_STRING + "productdetails";
    public static final String PATICIPATION_URL = HOST_STRING + "participation";
    public static final String USERINFO_URL = HOST_STRING + "customer/home";//个人中心信息
    public static final String LOGOUT_URL = HOST_STRING + "customer/logout";
    public static final String MY_BALANCE_URL = HOST_STRING + "customer/point";
    public static final String MY_WIN_URL = HOST_STRING + "customer/winprize";
    public static final String UPDATE_ADDRESS_URL = HOST_STRING + "customer/updateaddress";
    public static final String ADD_SHARE_URL = HOST_STRING + "customer/add-prizeshow";
    public static final String RECORD_URL = HOST_STRING + "customer/purchase-history?";
    public static final String RECORD_OPEN_URL = RECORD_URL + "drawStatus=OPEN";
    public static final String RECORD_ANNOUNCED_URL = RECORD_URL + "drawStatus=ANNOUNCED";
    public static final String GET_ADDRESS_URL = HOST_STRING + "customer/deliveryaddress";
    public static final String TRANSFER_BALANCE_URL = HOST_STRING + "customer/transfer-balance";
    public static final String GET_GOOD_INFO_URL = HOST_STRING + "drawcycleitems";
    public static final String PREPAY_URL = HOST_STRING + "customer/topup/prepay";
    public static final String CHOOSE_EARN_URL = HOST_STRING + "customer/claim-type";
    public static final String CHOOSE_POST_URL = HOST_STRING + "customer/claim-post";
    public static final String WECHAT_TRADEOUT = HOST_STRING + "customer/topup/queryOrder";
    public static final String RETRIEVEPWD_UTL = HOST_STRING + "resetpwd/confirm";
    public static final String RETRIEVEPWDVALIDATE_URL = HOST_STRING + "resetpwd/validate-phone";
    public static final String SIGN_URL = HOST_STRING + "customer/sign";
    public static final String CHECK_SIGN_URL = HOST_STRING + "customer/check-signed";

    public static final String ENSURE_REC_URL = HOST_STRING + "customer/claim-confirm";
    public static final String DONATE_URL = HOST_STRING + "customer/donate-winprize";
    public static final String ALL_SHARE_URL = HOST_STRING + "commons/prizeshow-list";
    public static final String CHOOSE_COM_POST_URL = HOST_STRING + "customer/edit-address";
    public static final String My_SHARE_URL = HOST_STRING + "customer/my-prizeshow";
    public static final String ADD_COMMENT_URL = HOST_STRING + "customer/prizeshow/add-comment";
    public static final String COMMENTS_URL = HOST_STRING + "commons/prizeshow/list-comment";
    public static final String LATESTANNOUNCE_URL = HOST_STRING + "latestannounce";
    public static final String CALDETAIL_URL = HOST_STRING + "drawresult";
    public static final String CALDETAILCOUNTDOWN_URL = HOST_STRING + "other/participation";
    public static final String GOOD_SHARELIST_URL = HOST_STRING + "commons/prod-prizeshow-list";
    public static final String CHARGE_DETAIL = HOST_STRING + "customer/balance-history";
    public static final int DEF_BIG_PAGE_SIZE = 30;
    public static final String RECOMMAND = HOST_STRING + "commons/recommended?recommendType=FRONT_PAGE";
    public static final String MOD_NICK = HOST_STRING + "customer/update-nickname";
    public static final String PERSONALDATA = HOST_STRING + "customer/nickname";
    public static final String MOD_PWD = HOST_STRING + "resetpwd/update";
    public static final String INTROCODE = HOST_STRING + "customer/update-invitation";
    public static final String MYINTROCODE = HOST_STRING + "customer/invitation";
    public static final String COMMISSION = HOST_STRING + "c" +
            "ustomer/commission";
    public static final String HELP_CENTER = HOST_STRING + "commons/systempara";
    public static final String AllCYCLE = HOST_STRING + "allcycles";
    public static final String COMMISION_TO_BALANCE = HOST_STRING + "customer/commission-tobalance";
    public static final String PRIZESHOW = HOST_STRING + "commons/prizeshow-details";
    public static final String LIKE = HOST_STRING + "commons/praise-prizeshow";
    public static final String GRANT_URL = HOST_STRING + "customer/donate-balance";
    //赠与金额的历史
    public static final String GRANT_HIS_URL = HOST_STRING + "customer/donate-history";
   public static final String CUSTOMER_INFO = HOST_STRING + "other/info";
   public static final String CUSTOMER_HISTORY = HOST_STRING + "customer/history";
   public static final String CUSTOMER_SHARE = HOST_STRING + "customer/share";
   //    public static final String REALBLOCK_LIST = HOST_STRING + "category/reallist";//这行被东杰改成了下一行。。。
   public static final String REALBLOCK_LIST = HOST_STRING + "physical/list";
   //    public static final String REALBLOCK = HOST_STRING + "category/realblock";//这行被东杰改成了下一行。。。
   public static final String REALBLOCK = HOST_STRING + "physical/physicalblock";
   public static final String CASHOUT = HOST_STRING + "customer/cashout";
   public static final String WINNERCODE = HOST_STRING + "other/winnercode";
   //    public static final String OTHERWINNERCODE = HOST_STRING+"other/winnercode";//在已揭晓的商品中查看该商品的乐购码
   public static final String GRANTRECORD_URL = HOST_STRING + "customer/grantrecord";
   public static final String CUSTOMER_INFO_PRIZESHOW = HOST_STRING + "other/customer-prizeshow";//CustomerInfo的晒单
   public static final String CUSTOMER_INFO_WINPRIZE = HOST_STRING + "other/customer-winprize";//CUstomerInfo的中奖记录
   public static final String CUSTOMER_INFO_HISTORY = HOST_STRING + "other/customer-history";//CustomerInfo的购买记录
   public static final String BANKCART_PAYRESULT = HOST_STRING + "annotation-result";//通联支付的支付结果
   public static final String BANKCARD_REQUESTORDERNO = HOST_STRING + "customer/createOrder";//通联支付创建订单
   public static final String WEBCHAT_REQUESTORDERNO = HOST_STRING + "customer/rechargeOrder";//微信支付之前生成订单号
   public static final String WEBCHAT_NOTIFYURL = HOST_STRING + "ask-notify";//微信支付结果后台通知的URL
   public static final String CASHOUT_RECORD = HOST_STRING + "customer/withdraw-history?";//账户明细中的提现明细
   //    public static final String WEBCHAT_NOTIFYURL = "http://fy983951558.imwork.net/" + "ask-notify";
   public static final String PAY_GET_USERID = HOST_STRING + "customer/paycode";//支付前先来服务器拿通联的会员id
   public static final String PAY_RESULT = HOST_STRING + "ask-result";//通联支付的回调接口

    public static final String MY_INVITATOR = HOST_STRING + "customer/my-invitator";
    public static final String GETCURRENTTIME = HOST_STRING + "other/getCurrentTime";

    public static final String PURCHASE_DETAILS = HOST_STRING + "customer/purchase-details";

    public static LoginActivity loginActivity;
    public static MainActivity mainActivity;

}
