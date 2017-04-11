package com.shawnway.nav.app.yylg.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.net.ImageRequestPlus;
import com.shawnway.nav.app.yylg.net.VolleyTool;
import com.shawnway.nav.app.yylg.tool.NetUtil;
import com.shawnway.nav.app.yylg.tool.StringUtils;
import com.squareup.picasso.Picasso;


/**
 * Created by Eiffel on 2015/11/3.
 * 处理网络图片的视图，通过设置xml:civShouldCache来改变是否使用缓存
 * 服务器通过设置url后缀来改变图片的品质，如"http://abc_200x200.jpg"代表200x200的品质，该类提供设置的功能
 * <p/>
 * 1、使用缓存：使用{@link ImageLoader}类
 * 2、不使用缓存：使用{@link ImageRequestPlus}
 */
public class CirclerImageView extends ImageView {

    private boolean shouldCache = true;
    String url;
    protected String TAG;

    // 控件默认长、宽


    private int defaultWidth = 0;


    private int defaultHeight = 0;


    // 比例


    private float scale = 0;


    public CirclerImageView(Context context) {
        this(context, null);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setImageDrawable(getResources().getDrawable(R.drawable.img_blank));
    }

    public CirclerImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public CirclerImageView(Context context, AttributeSet attrs,
                            int defStyleAttr) {
        this(context, attrs, defStyleAttr, null);

    }

    public CirclerImageView(Context context, AttributeSet attrs,
                            int defStyleAttr, String url) {
        super(context, attrs, defStyleAttr);


        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CirclerImageView);

        int n = a.getIndexCount();

        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.CirclerImageView_civShouldCache:
                    shouldCache = a.getBoolean(attr, true);
                    break;
            }

        }

        a.recycle();

        this.url = url;
        if (url != null) {
            setImage(url, true);
        }
    }

    @Override

    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
    }

    public void setImage(final String url) {
        setImage(url, true);
    }

    public void setImage(Context context){
        new CirclerImageView(context);
    }

    // 默认缓存位置data/data/包名/cache/volley,默认大小5MB
    public void setImage(final String url, final boolean retry) {
        if (StringUtils.isEmpty(url)) {
            this.setVisibility(INVISIBLE);
            return;
        }else{
            this.setVisibility(VISIBLE);
        }
        final String newurl;
        if(url.contains("_")){
            newurl = retryWithHandleSuffix(url);
        }else{
            newurl = url;
        }

        //根据给出的size处理url，现在服务器的设定是在返回的文件名后面加上  "_200x200"或之类的
        if (!shouldCache) {
            RequestQueue requestQueue = VolleyTool.getInstance(getContext())
                    .getRequestQueue();
            Log.d(TAG, "begin get picture：" + newurl);
            ImageRequestPlus request = new ImageRequestPlus(getContext(), newurl, new Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    setImageBitmap(response);
                }
            }, 0, 0, Config.RGB_565, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    setImageDrawable(getResources().getDrawable(R.drawable.img_blank));
                }
            });
            //set cookie
            request.setSendCookie();
            request.setTag(getContext().getClass().getSimpleName());
            request.setShouldCache(true);
            requestQueue.add(request);
        } else {
            ImageLoader imageLoader = VolleyTool.getInstance(getContext()).getmImageLoader();

            Log.d(TAG, "imagelaoder sended url:" + newurl);
            imageLoader.get(newurl, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                    setImageBitmap(imageContainer.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    setImageDrawable(getResources().getDrawable(R.drawable.img_blank));
                }
            }, 0, 0);
        }
    }

    private String retryWithHandleSuffix(String url) {
        int start = url.indexOf("_");
        int end = url.lastIndexOf(".");
        String newurl = null;
        if (start != -1) {
            StringBuilder builder = new StringBuilder(url);
            try {
                newurl = builder.delete(start, end).toString();//重新加载图片的时候为什么要将两个符号之间的字符删掉？
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return newurl;
    }

    private String makeUrl(String url, int width, int height) {
        if (width > 0 && height > 0) {
            StringBuilder builder = new StringBuilder(url);
            builder.insert(builder.lastIndexOf("."), "_" + width + "x" + height);
            url = builder.toString();
        }
        return url;
    }

    /**
     * 设置图片要加载的url及品质，注意，必须要服务器支持该品质才行，暂时只支持300x300
     *
     * @param url    原始资源url
     * @param width  宽度，单位px
     * @param height 高度，单位px
     */
    public void setBigImage(String url, int width, int height) {
        url = makeUrl(url, width, height);
        setImage(url, true);
    }

}
