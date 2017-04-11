package com.shawnway.nav.app.yylg.customer.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.customer.adapter.ImageSelectorPubSelectedImgsAdapter;

import java.util.ArrayList;


public class ImageSelectorMainActivity extends Activity {

    private static final int REQUEST_IMAGE = 2;

    private ArrayList<String> mSelectPath;
    
    GridView gridView;
	ImageSelectorPubSelectedImgsAdapter imageSelectorPubSelectedImgsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView=(GridView) findViewById(R.id.gridView);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ImageSelectorMainActivity.this, ImageSelectorActivity.class);
                // 是否显示拍摄图片
                intent.putExtra(ImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
                // 最大可选择图片数量
                intent.putExtra(ImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
                // 选择模式
                intent.putExtra(ImageSelectorActivity.EXTRA_SELECT_MODE, ImageSelectorActivity.MODE_MULTI);
                // 默认选择
                if(mSelectPath != null && mSelectPath.size()>0){
                    intent.putExtra(ImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mSelectPath);
                }
                startActivityForResult(intent, REQUEST_IMAGE);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE){
            if(resultCode == RESULT_OK){
            	mSelectPath = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
                imageSelectorPubSelectedImgsAdapter =new ImageSelectorPubSelectedImgsAdapter(getApplicationContext(), mSelectPath, new ImageSelectorPubSelectedImgsAdapter.OnItemClickClass() {
					@Override
					public void OnItemClick(View v, String filepath) {
						mSelectPath.remove(filepath);
						imageSelectorPubSelectedImgsAdapter.notifyDataSetChanged();
					}
				});
            }
            gridView.setAdapter(imageSelectorPubSelectedImgsAdapter);
        }
    }
}
