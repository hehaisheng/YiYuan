package com.shawnway.nav.app.yylg.tool;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Eiffel on 2015/11/16.
 */
public class NetUtil {
    public static String putGetBody(String url, Map body) {
        StringBuffer excuteUrl = new StringBuffer(url);
        if (!url.contains("?"))
            excuteUrl.append('?');
        Collection<String> c = body.keySet();
        Iterator it = c.iterator();
        for (; it.hasNext();) {
            String key= it.next().toString();
            Object value=  body.get(key);
            excuteUrl.append(key+"="+value+"&");
        }
        excuteUrl.deleteCharAt(excuteUrl.length()-1);//删除最后的'&'
        return excuteUrl.toString();
    }

    public static String encodeUrl(String url) {
        try {
            url = URLEncoder.encode(url, "utf-8");
            url = url.replace("%2F", "/");
            url = url.replace("%3A", ":");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return  url;
    }


//    public static String insertUrl
}
