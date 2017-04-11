package com.shawnway.nav.app.yylg.tool;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Administrator on 2016/6/6 0006.
 */
public class PaaCreator {
    private static final String TAG = "通联支付设置参数";

    public static JSONObject randomPaa(int money,String orderNo,Long userId) {
        String amount = ""+money;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String timeStr = dateFormat.format(new Date());
        String goodName ="充值"+money+"元";
        amount = Integer.valueOf(amount)*100+"";
        String receiveUrl = Constants.PAY_RESULT;//通联支付结果通知的回调接口
        String merchantNum = "109020201605002";//正式商户号：109020201605002   测试商户号码：100020091218888   100020140728001
        String key = "1111111111";//1111111111   测试：1234567890
        Log.d(TAG, "通联支付设置参数时候的userId："+userId);
        String UserId = "<USER>"+userId+"</USER>";
        String payType = "27";
        String signType = "1";
//        String orderStr = timeStr + "0000";

        JSONObject paaParams = new JSONObject();
        try {
            paaParams.put("inputCharset", "1");  //必填，协议字符集，1代表UTF-8、2代表GBK、3代表GB2312
            paaParams.put("receiveUrl", receiveUrl);//必填，后台通知商户网站支付结果的url地址
            paaParams.put("version", "v1.0"); //必填 协议版本，固定填v1.0
            paaParams.put("signType", signType);//必填  默认填1 0 表示订单上送和交易结果通知都使用MD5进行签名 1 表示商户用使用MD5算法验签上送订单，通联交易结果通知使用证书签名
            paaParams.put("merchantId", merchantNum);//TODO:必填 调用通联支付服务的商户号
            paaParams.put("orderNo", orderNo);//必填 生成的订单号
            paaParams.put("orderAmount", amount);//必填 支付金额，整型数字，单位是分，即10元提交时金额应为1000
            paaParams.put("orderCurrency", "0");//必填 币种，0或156 – 人民币
            paaParams.put("orderDatetime", timeStr);//必填 订单生成的时间
            paaParams.put("productName", goodName);//TODO:必填 商品名称
//			paaParams.put("ext1", ext1FromInput());
//			paaParams.put("ext1", "<USER>201406231006545</USER>");
            paaParams.put("ext1", UserId);//TODO:会员模式的时候上传用户ID  后台给
            paaParams.put("payType", payType);//必填  支付类型，27则表示直接跳到输卡号界面，可支持借记卡和信用卡的认证或快捷支付
//			paaParams.put("issuerId", "visa");
//			paaParams.put("tradeNature", "GOODS");
//			paaParams.put("language", "3");
//            paaParams.put("cardNo", "6217003020000841342");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String[] paaParamsArray = {
                "1","inputCharset",
                receiveUrl,"receiveUrl",
                "v1.0","version",
//				"3","language",
                signType,"signType",
                merchantNum,"merchantId",
                orderNo,"orderNo",
                amount,"orderAmount",
                "0","orderCurrency",
                timeStr,"orderDatetime",
                goodName, "productName",
//				ext1FromInput(),"ext1",
//			    "<USER>201406231006545</USER>","ext1",
                UserId,"ext1",
                payType,"payType",
//				"visa","issuerId",
//				"GOODS","tradeNature",
                key,"key",//签名密钥
        };

        String paaStr = "";
        for (int i = 0; i < paaParamsArray.length; i++) {
            paaStr += paaParamsArray[i+1] + "=" + paaParamsArray[i] + "&";
            i++;
        }
        Log.d("MyPaaCreator", "PaaCreator " + paaStr.substring(0, paaStr.length() - 1));
        String md5Str = md5(paaStr.substring(0, paaStr.length() -1));
        Log.d("MyPaaCreator", "PaaCreator md5Str " + md5Str);
        try {
            paaParams.put("signMsg", md5Str);//必填 对请求内容进行签名
//				paaParams.put("ship_to_country", "US");
//				paaParams.put("ship_to_state", "AL");
//				paaParams.put("ship_to_city", "city");
//				paaParams.put("ship_to_street1", "street_1");
//				paaParams.put("ship_to_street2", "street_2");
//				paaParams.put("ship_to_phonenumber", "13812345678");
//				paaParams.put("ship_to_postalcode", "20004");
//				paaParams.put("ship_to_firstname", "Smith");
//				paaParams.put("ship_to_lastname", "Black");
//				paaParams.put("registration_name", "abc");
//				paaParams.put("registration_email", "abc@gmail.com");
//				paaParams.put("registration_phone", "999-13800000000");
//				paaParams.put("buyerid_period", "200");
//				paaParams.put("fnpay_mode", "1");
//				paaParams.put("bill_firstname", "handon");
//				paaParams.put("bill_lastname", "hao");
//				paaParams.put("expireddate", "1919");
//				paaParams.put("cvv2", "888");
//				paaParams.put("bill_email", "abc@gmail.com");
//				paaParams.put("bill_country", "US");
//				paaParams.put("bill_address", "billaddress");
//				paaParams.put("bill_city", "billcity");
//				paaParams.put("bill_state", "IL");
//				paaParams.put("bill_zip", "12345");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return paaParams;
    }

    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(
                    string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        return hexString(hash);
    }

    public static final String hexString(byte[] bytes)
    {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < bytes.length; i++)
        {
            buffer.append(hexString(bytes[i]));
        }
        return buffer.toString();
    }

    public static final String hexString(byte byte0)
    {
        char ac[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        char ac1[] = new char[2];
        ac1[0] = ac[byte0 >>> 4 & 0xf];
        ac1[1] = ac[byte0 & 0xf];
        String s = new String(ac1);
        return s;
    }

//	public static String ext1FromInput() {
//	    String[] paaParamsArray = {
//				"US","ship_to_country",
//				"AL","ship_to_state",
//				"city","ship_to_city",
//				"street_1","ship_to_street1",
//				"street_2","ship_to_street2",
//				"13812345678","ship_to_phonenumber",
//				"20004","ship_to_postalcode",
//				"Smith","ship_to_firstname",
//				"Black","ship_to_lastname",
//				"abc", "registration_name",
//				"abc@gmail.com","registration_email",
//				"999-13800000000","registration_phone",
//				"200","buyerid_period",
//				"1","fnpay_mode",
//				"handon","bill_firstname",
//				"hao","bill_lastname",
//				"1919","expireddate",
//				"888","cvv2",
//				"abc@gmail.com","bill_email",
//				"US","bill_country",
//				"billaddress","bill_address",
//				"billcity", "bill_city",
//				"IL","bill_state",
//				"12345","bill_zip",
//		    };
//
//		    String paaStr = "";
//		    for (int i = 0; i < paaParamsArray.length; i++) {
//		    	paaStr += paaParamsArray[i];
//		        i++;
//		    }
//		    Log.d("ext1FromInput", "ext1FromInput " + paaStr);
//		    String md5Str = md5(paaStr);
//		    Log.d("ext1FromInput", "ext1FromInput md5Str " + md5Str);
//
//		return md5Str;
//	}
}
