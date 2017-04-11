package com.shawnway.nav.app.yylg.testmy;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.shawnway.nav.app.yylg.R;

/**
 * Created by Administrator on 2017-02-28.
 */

public class TestActivity extends Activity {

    public ListView listView;
    public TestAdapter testAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_list_systeminfo);
        listView=(ListView) findViewById(R.id.systeminfo_list1);
        testAdapter=new TestAdapter(this);
        listView.setAdapter(testAdapter);
    }
}
