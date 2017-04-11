package com.shawnway.nav.app.yylg.customer.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.customer.bean.ImageSelectorImage;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * 图片Adapter
 */
public class ImageSelectorImageGridAdapter extends BaseAdapter {

    private static final int TYPE_CAMERA = 0;
    private static final int TYPE_NORMAL = 1;

    private Context mContext;

    private LayoutInflater mInflater;
    private boolean showCamera = true;
    private boolean showSelectIndicator = true;

    private List<ImageSelectorImage> mImageSelectorImages = new ArrayList<ImageSelectorImage>();
    private List<ImageSelectorImage> mSelectedImageSelectorImages = new ArrayList<ImageSelectorImage>();

    private int mItemSize;
    private GridView.LayoutParams mItemLayoutParams;
    

    public ImageSelectorImageGridAdapter(Activity context, boolean showCamera){
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.showCamera = showCamera;
        mItemLayoutParams = new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, GridView.LayoutParams.MATCH_PARENT);
    }
    /**
     * 显示选择指示器
     * @param b
     */
    public void showSelectIndicator(boolean b) {
        showSelectIndicator = b;
    }

    public void setShowCamera(boolean b){
        if(showCamera == b) return;

        showCamera = b;
        notifyDataSetChanged();
    }

    public boolean isShowCamera(){
        return showCamera;
    }

    /**
     * 选择某个图片，改变选择状态
     * @param imageSelectorImage
     */
    public void select(ImageSelectorImage imageSelectorImage) {
        if(mSelectedImageSelectorImages.contains(imageSelectorImage)){
            mSelectedImageSelectorImages.remove(imageSelectorImage);
        }else{
            mSelectedImageSelectorImages.add(imageSelectorImage);
        }
        notifyDataSetChanged();
    }

    /**
     * 通过图片路径设置默认选择
     * @param resultList
     */
    public void setDefaultSelected(ArrayList<String> resultList) {
        for(String path : resultList){
            ImageSelectorImage imageSelectorImage = getImageByPath(path);
            if(imageSelectorImage != null){
                mSelectedImageSelectorImages.add(imageSelectorImage);
            }
        }
        if(mSelectedImageSelectorImages.size() > 0){
            notifyDataSetChanged();
        }
    }

    private ImageSelectorImage getImageByPath(String path){
        if(mImageSelectorImages != null && mImageSelectorImages.size()>0){
            for(ImageSelectorImage imageSelectorImage : mImageSelectorImages){
                if(imageSelectorImage.path.equalsIgnoreCase(path)){
                    return imageSelectorImage;
                }
            }
        }
        return null;
    }

    /**
     * 设置数据集
     * @param imageSelectorImages
     */
    public void setData(List<ImageSelectorImage> imageSelectorImages) {
        mSelectedImageSelectorImages.clear();

        if(imageSelectorImages != null && imageSelectorImages.size()>0){
            mImageSelectorImages = imageSelectorImages;
        }else{
            mImageSelectorImages.clear();
        }
        notifyDataSetChanged();
    }

    /**
     * 重置每个Column的Size
     * @param columnWidth
     */
    public void setItemSize(int columnWidth) {

        if(mItemSize == columnWidth){
            return;
        }

        mItemSize = columnWidth;

        mItemLayoutParams = new GridView.LayoutParams(mItemSize, mItemSize);

        notifyDataSetChanged();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if(showCamera){
            return position==0 ? TYPE_CAMERA : TYPE_NORMAL;
        }
        return TYPE_NORMAL;
    }

    @Override
    public int getCount() {
        return showCamera ? mImageSelectorImages.size()+1 : mImageSelectorImages.size();
    }

    @Override
    public ImageSelectorImage getItem(int i) {
        if(showCamera){
            if(i == 0){
                return null;
            }
            return mImageSelectorImages.get(i-1);
        }else{
            return mImageSelectorImages.get(i);
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        int type = getItemViewType(i);
        if(type == TYPE_CAMERA){
            view = mInflater.inflate(R.layout.is_list_item_camera, viewGroup, false);
            view.setTag(null);
        }else if(type == TYPE_NORMAL){
            ViewHolde holde;
            if(view == null){
                view = mInflater.inflate(R.layout.is_list_item_image, viewGroup, false);
                holde = new ViewHolde(view);
            }else{
                holde = (ViewHolde) view.getTag();
                if(holde == null){
                    view = mInflater.inflate(R.layout.is_list_item_image, viewGroup, false);
                    holde = new ViewHolde(view);
                }
            }
            if(holde != null) {
                holde.bindData(getItem(i));
            }
        }

        /** Fixed View Size */
        GridView.LayoutParams lp = (GridView.LayoutParams) view.getLayoutParams();
        if(lp.height != mItemSize){
            view.setLayoutParams(mItemLayoutParams);
        }
        
        

        return view;
    }

    class ViewHolde {
        ImageView image;
        ImageView indicator;

        ViewHolde(View view){
            image = (ImageView) view.findViewById(R.id.imageSelectorImage);
            indicator = (ImageView) view.findViewById(R.id.checkmark);
            view.setTag(this);
        }

        void bindData(final ImageSelectorImage data){
            if(data == null) return;
            // 处理单选和多选状态
            if(showSelectIndicator){
                indicator.setVisibility(View.VISIBLE);
                if(mSelectedImageSelectorImages.contains(data)){
                    // 设置选中状态
                    indicator.setImageResource(R.drawable.is_btn_selected);
                }else{
                    // 未选择
                    indicator.setImageResource(R.drawable.is_btn_unselected);
                }
            }else{
                indicator.setVisibility(View.GONE);
            }
            File imageFile = new File(data.path);

            if(mItemSize > 0) {
                // 显示图片
                Picasso.with(mContext)
                        .load(imageFile)
                        .placeholder(R.drawable.is_default_error)
                        .resize(mItemSize, mItemSize)
                        .centerCrop()
                        .into(image);
            }
        }
    }

}
