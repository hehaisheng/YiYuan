package com.shawnway.nav.app.yylg.tool;

import android.app.Activity;

import java.util.LinkedList;

/**
 * Created by Administrator on 2016/6/1 0001.
 */
public class CloseActivityUtil {
    private static LinkedList<Activity> acys = new LinkedList<Activity>();

    public static Activity curActivity;

    public static void add(Activity acy)
    {
        acys.add(acy);
    }

    public static void remove(Activity acy) {
        acys.remove(acy);
    }

    public static void closePage(){
        Activity acy;
        while (acys.size() != 0)
        {
            acy = acys.poll();
            if (!acy.isFinishing())
            {
                acy.finish();
            }
        }
    }

    public static void close()
    {
        Activity acy;
        while (acys.size() != 0)
        {
            acy = acys.poll();
            if (!acy.isFinishing())
            {
                acy.finish();
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}
