package com.shawnway.nav.app.yylg.tool;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.shawnway.nav.app.yylg.R;

/**
 * Created by Kevin on 2016/12/15
 */

public class GlideUtils {
    public static void loadImage(Context context, String url, int errorImg, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .error(errorImg)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static void loadImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .error(R.drawable.portrait)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static void loadImage(Context context, int drawable, ImageView imageView) {
        Glide.with(context)
                .load(drawable)
                .error(R.drawable.portrait)
                .into(imageView);
    }

    public static void loadImage(Context context, String url, ImageView imageView, int errorimage) {
        Glide.with(context)
                .load(url)
                .error(errorimage == -1 ? R.drawable.portrait : errorimage)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static void clearPicCache(Context context) {
        Glide glide = Glide.get(context);
    }
}
