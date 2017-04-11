package com.shawnway.nav.app.yylg.tool;

import java.util.Random;

/**
 * Created by Administrator on 2016/4/8 0008.
 *
 * 用来随机生成一个或者一串的数字
 */
public class RanUtils {

    public static String ran_str_1(){
        Random ran = new Random();
        return ran.nextInt(10)+"";
    }
    public static String ran_str_2(){
        return ran_str_1()+ran_str_1();
    }
    public static String ranN(int n){
        String str = "";
        for(int i = 0;i<n;i++){
            str = str+ran_str_1();
        }
        return str;
    }

    public static int ran(int n){
        Random random = new Random();
        return random.nextInt(n);
    }

}
