package com.shawnway.nav.app.yylg.testbaidu;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.app.MyApplication;

/**
 * Created by Administrator on 2017-03-27.
 */

public class TestBD extends Activity {

    public MyLocation myLocation;
    public TextView textView;
    private ReceiveBroadCast receiveBroadCast;  //广播实例



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_layout);
        initBD();
        initReceiver();

        //initDecoder();
    }
    public void initReceiver()
    {

        receiveBroadCast = new ReceiveBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.my.address");
        registerReceiver(receiveBroadCast, filter);
    }

    public void initBD() {
        textView = (TextView) findViewById(R.id.testtext);
        myLocation = new MyLocation(this);
        myLocation.start();



        //myLocation.initDecoder();

    }
    public class ReceiveBroadCast extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {

            boolean hasAddr=intent.getBooleanExtra("hasAddr",false);
            if(hasAddr)
            {
                textView.setText(MyApplication.getInstance().getAddress());
                myLocation.stop();
            }
            else
            {
                textView.setText("shib");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        myLocation.start();
                    }
                }).start();
            }


        }

    }


 }

