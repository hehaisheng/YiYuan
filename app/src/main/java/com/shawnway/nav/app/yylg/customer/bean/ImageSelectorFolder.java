package com.shawnway.nav.app.yylg.customer.bean;

import java.util.List;

/**
 * 文件夹
 * Created by Nereo on 2015/4/7.
 */
public class ImageSelectorFolder {
    public String name;
    public String path;
    public ImageSelectorImage cover;
    public List<ImageSelectorImage> imageSelectorImages;

    @Override
    public boolean equals(Object o) {
        try {
            ImageSelectorFolder other = (ImageSelectorFolder) o;
            return this.path.equalsIgnoreCase(other.path);
        }catch (ClassCastException e){
            e.printStackTrace();
        }
        return super.equals(o);
    }
}
