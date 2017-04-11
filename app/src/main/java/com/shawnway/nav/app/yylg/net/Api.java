package com.shawnway.nav.app.yylg.net;

import com.shawnway.nav.app.yylg.bean.LastWinnerBean;
import com.shawnway.nav.app.yylg.bean.PurchaseDetailsBean;
import com.shawnway.nav.app.yylg.fragment.UserFragment;
import com.shawnway.nav.app.yylg.mvp.user.user_buy_record.bean.MyBuyRecordsBean;
import com.shawnway.nav.app.yylg.mvp.user.user_invite.explain.bean.CommissionExplain;
import com.shawnway.nav.app.yylg.mvp.user.user_invite.explain.bean.InviteCode;
import com.shawnway.nav.app.yylg.mvp.user.user_winrecord.choos_earn.bean.ResponseResult;
import com.shawnway.nav.app.yylg.testmy.WXPayBean;

import java.math.BigDecimal;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Kevin on 2016/12/1
 */

public interface Api {
    //生产环境
  String BASE_URL = "http://tescochn.com/";
    //测试环境
    //String BASE_URL = "http://122.112.210.44:8080/";
    String CODE_PERMISSON = "A000";
    //String BASE_URL = "http://192.168.31.172:8080/";//谢聪的


    @GET("customer/invitation")
    Observable<InviteCode> getInviteCode();


  @FormUrlEncoded
  @POST("preOrder")
  Observable<WXPayBean> requestPayPar(@FieldMap Map<String,String> params);

  @FormUrlEncoded
  @POST("orderResultQuery")
  Observable<String>  jugdeWXResult(@FieldMap Map<String,String> stringMap);



   // @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("customer/checkout")
    Observable<String> postPay(@Body String string);//传入的参数为RequestBody

    //获取佣金说明的邀请人数
    @POST("customer/commissionDeclare/{phone}")
    Observable<CommissionExplain> getCommissionExplain(@Path("phone") String phone);

    //话费充值
    @FormUrlEncoded
    @POST("customer/telephoneRecharge")
    Observable<ResponseResult> telePhoneRecharge(@Field("cellphone") String cellphone,
                                                 @Field("username") String username,
                                                 @Field("amountOfcard") BigDecimal amountOfcard,
                                                 @Field("winprizeId") Long winprizeId);

    //淘宝卡提现
    @FormUrlEncoded
    @POST("customer/alipayTransfer")
    Observable<ResponseResult> alipayTransfer(@Field("cellphone") String cellphone,
                                              @Field("username") String username,
                                              @Field("amountOfcard") BigDecimal amountOfcard,
                                              @Field("winprizeId") Long winprizeId,
                                              @Field("alipayAccount") String alipayAccount);

    //某条购买记录
    @GET("customer/purchase-details")
    Observable<PurchaseDetailsBean> getPurchaseDeatils(@Query("drawCycleId") long drawCycleId);

    //上传头像
    @Multipart
    @POST("customer/uploadUserImg")
    Observable<UserFragment.ImageBean> upload(@Part MultipartBody.Part file);

    //获取用户头像
    @GET("customer/retrieveUserImge")
    Observable<UserFragment.ImageBean> retrieveUserImge();

    //获取商品详情上期获得者
    @GET("lastWinprize")
    Observable<LastWinnerBean> getLastWinPrize(@Query("drawcycleId") long drawcycleId);

    //获取指定用户的头像
    @GET("other/customer-retrieveUserImge")
    Observable<UserFragment.ImageBean> getCustomerImage(@Query("customerId") long customerId);

    //获取用户基本信息
    @GET("customer/home")
    Observable<UserFragment.UserInfoResponse> getCustomerInfo();

    //获取用户购买记录
    @GET("customer/purchase-history")
    Observable<MyBuyRecordsBean> getMyBuyRecords(@Query("page") int page,
                                                 @Query("pageSize") int pageSize,
                                                 @Query("drawStatus") String drawStatus);

}
