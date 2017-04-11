package com.shawnway.nav.app.yylg.customer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.customer.bean.ImageSelectorFolder;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * 文件夹Adapter
 */
public class ImageSelectorFolderAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;

    private List<ImageSelectorFolder> mImageSelectorFolders = new ArrayList<ImageSelectorFolder>();

    int mImageSize;

    int lastSelected = 0;

    public ImageSelectorFolderAdapter(Context context){
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mImageSize = mContext.getResources().getDimensionPixelOffset(R.dimen.folder_cover_size);
    }

    /**
     * 设置数据集
     * @param imageSelectorFolders
     */
    public void setData(List<ImageSelectorFolder> imageSelectorFolders) {
        if(imageSelectorFolders != null && imageSelectorFolders.size()>0){
            mImageSelectorFolders = imageSelectorFolders;
        }else{
            mImageSelectorFolders.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mImageSelectorFolders.size()+1;
    }

    @Override
    public ImageSelectorFolder getItem(int i) {
        if(i == 0) return null;
        return mImageSelectorFolders.get(i-1);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null){
            view = mInflater.inflate(R.layout.is_list_item_folder, viewGroup, false);
            holder = new ViewHolder(view);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        if (holder != null) {
            if(i == 0){
                holder.name.setText("所有图片");
                holder.size.setText(getTotalImageSize()+"张");
                if(mImageSelectorFolders.size()>0){
                    ImageSelectorFolder f = mImageSelectorFolders.get(0);
                    Picasso.with(mContext)
                            .load(new File(f.cover.path))
                            .error(R.drawable.is_default_error)
                            .resize(mImageSize, mImageSize)
                            .centerCrop()
                            .into(holder.cover);
                }
            }else {
                holder.bindData(getItem(i));
            }
            if(lastSelected == i){
                holder.indicator.setVisibility(View.VISIBLE);
            }else{
                holder.indicator.setVisibility(View.GONE);
            }
        }
        return view;
    }

    private int getTotalImageSize(){
        int result = 0;
        if(mImageSelectorFolders != null && mImageSelectorFolders.size()>0){
            for (ImageSelectorFolder f: mImageSelectorFolders){
                result += f.imageSelectorImages.size();
            }
        }
        return result;
    }

    public void setSelectIndex(int i) {
        if(lastSelected == i) return;

        lastSelected = i;
        notifyDataSetChanged();
    }

    public int getSelectIndex(){
        return lastSelected;
    }

    class ViewHolder{
        ImageView cover;
        TextView name;
        TextView size;
        ImageView indicator;
        ViewHolder(View view){
            cover = (ImageView)view.findViewById(R.id.cover);
            name = (TextView) view.findViewById(R.id.name);
            size = (TextView) view.findViewById(R.id.size);
            indicator = (ImageView) view.findViewById(R.id.indicator);
            view.setTag(this);
        }

        void bindData(ImageSelectorFolder data) {
            name.setText(data.name);
            size.setText(data.imageSelectorImages.size()+"张");
            // 显示图片
            Picasso.with(mContext)
                    .load(new File(data.cover.path))
                    .placeholder(R.drawable.is_default_error)
                    .resize(mImageSize, mImageSize)
                    .centerCrop()
                    .into(cover);
            // TODO 选择标识
        }
    }

}
