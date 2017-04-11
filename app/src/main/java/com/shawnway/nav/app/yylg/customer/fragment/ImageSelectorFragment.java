package com.shawnway.nav.app.yylg.customer.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.customer.activity.ImageSelectorPreViewActivity;
import com.shawnway.nav.app.yylg.customer.adapter.ImageSelectorFolderAdapter;
import com.shawnway.nav.app.yylg.customer.adapter.ImageSelectorImageGridAdapter;
import com.shawnway.nav.app.yylg.customer.bean.ImageSelectorFolder;
import com.shawnway.nav.app.yylg.customer.bean.ImageSelectorImage;
import com.shawnway.nav.app.yylg.tool.AppUtils;
import com.shawnway.nav.app.yylg.tool.DateUtil;
import com.shawnway.nav.app.yylg.tool.FileUtils;
import com.shawnway.nav.app.yylg.tool.ISPreferencesUtils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * 图片选择Fragment
 */
public class ImageSelectorFragment extends Fragment {


    /** 最大图片选择次数，int类型 */
    public static final String EXTRA_SELECT_COUNT = "max_select_count";
    /** 图片选择模式，int类型 */
    public static final String EXTRA_SELECT_MODE = "select_count_mode";
    /** 是否显示相机，boolean类型 */
    public static final String EXTRA_SHOW_CAMERA = "show_camera";
    /** 默认选择的数据集 */
    public static final String EXTRA_DEFAULT_SELECTED_LIST = "default_result";
    /** 单选 */
    public static final int MODE_SINGLE = 0;
    /** 多选 */
    public static final int MODE_MULTI = 1;
    // 不同loader定义
    private static final int LOADER_ALL = 0;
    private static final int LOADER_CATEGORY = 1;
    // 请求加载系统照相机
    private static final int REQUEST_CAMERA = 100;


    // 结果数据
    private ArrayList<String> resultList = new ArrayList<String>();
    // 文件夹数据
    private ArrayList<ImageSelectorFolder> mResultImageSelectorFolder = new ArrayList<ImageSelectorFolder>();

    // 图片Grid
    private GridView mGridView;
    private Callback mCallback;

    private ImageSelectorImageGridAdapter mImageAdapter;
    private ImageSelectorFolderAdapter mImageSelectorFolderAdapter;

    private ListPopupWindow mFolderPopupWindow;

    // 时间线
    private TextView mTimeLineText;
    // 类别
    private TextView mCategoryText;
    // 预览按钮
    private Button mPreviewBtn;
    // 底部View
    private View mPopupAnchorView;

    private int mDesireImageCount;

    private boolean hasFolderGened = false;

    private File mTmpFile;
    
    List<ImageSelectorImage> imageSelectorImages;
    
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (Callback) activity;
        }catch (ClassCastException e){
            throw new ClassCastException("The Activity must implement ImageSelectorFragment.Callback interface...");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        return inflater.inflate(R.layout.is_fragment_multi_image, container, false);
    }

    @Override
    public void onViewCreated(View view,  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 选择图片数量
        mDesireImageCount = getArguments().getInt(EXTRA_SELECT_COUNT);

        // 图片选择模式
        final int mode = getArguments().getInt(EXTRA_SELECT_MODE);

        // 默认选择
        if(mode == MODE_MULTI) {
            ArrayList<String> tmp = getArguments().getStringArrayList(EXTRA_DEFAULT_SELECTED_LIST);
            if(tmp != null && tmp.size()>0) {
                resultList = tmp;
            }
        }

        // 是否显示照相机
        final boolean showCamera = getArguments().getBoolean(EXTRA_SHOW_CAMERA, true);
        mImageAdapter = new ImageSelectorImageGridAdapter(getActivity(), showCamera);
        // 是否显示选择指示器
        mImageAdapter.showSelectIndicator(mode == MODE_MULTI);

        // 如果显示了照相机，则创建临时文件
        if(showCamera){
            mTmpFile = FileUtils.createTmpFile(getActivity());
        }

        mPopupAnchorView = view.findViewById(R.id.footer);

        mTimeLineText = (TextView) view.findViewById(R.id.timeline_area);
        // 初始化，先隐藏当前timeline
        mTimeLineText.setVisibility(View.GONE);

        mCategoryText = (TextView) view.findViewById(R.id.category_btn);
        // 初始化，加载所有图片
        mCategoryText.setText(R.string.folder_all);
        mCategoryText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mFolderPopupWindow.isShowing()){
                    mFolderPopupWindow.dismiss();
                }else {
                    mFolderPopupWindow.show();
                    int index = mImageSelectorFolderAdapter.getSelectIndex();
                    index = index == 0 ? index : index - 1;
                    mFolderPopupWindow.getListView().setSelection(index);
                }
            }
        });

        mPreviewBtn = (Button) view.findViewById(R.id.preview);
        // 初始化，按钮状态初始化
        if(resultList == null || resultList.size()<=0){
            mPreviewBtn.setText(R.string.preview);
            mPreviewBtn.setEnabled(false);
        }
        mPreviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO 预览
            	Intent intent=new Intent(getActivity(), ImageSelectorPreViewActivity.class);
            	Bundle b=new Bundle();
            	b.putStringArrayList("imglist", resultList);
            	intent.putExtra("b", b);
            	startActivity(intent);
            }
        });

        mGridView = (GridView) view.findViewById(R.id.grid);
        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int state) {

                final Picasso picasso = Picasso.with(getActivity());
                if(state == SCROLL_STATE_IDLE || state == SCROLL_STATE_TOUCH_SCROLL){
                    picasso.resumeTag(getActivity());
                }else{
                    picasso.pauseTag(getActivity());
                }

                if(state == SCROLL_STATE_IDLE){
                    // 停止滑动，日期指示器消失
                    mTimeLineText.setVisibility(View.GONE);
                }else if(state == SCROLL_STATE_FLING){
                    mTimeLineText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(mTimeLineText.getVisibility() == View.VISIBLE) {
                    int index = firstVisibleItem + 1 == view.getAdapter().getCount() ? view.getAdapter().getCount() - 1 : firstVisibleItem + 1;
                    ImageSelectorImage imageSelectorImage = (ImageSelectorImage) view.getAdapter().getItem(index);
                    if (imageSelectorImage != null) {
                        mTimeLineText.setText(DateUtil.formatPhotoDate(imageSelectorImage.path));
                    }
                }
            }
        });
        mGridView.setAdapter(mImageAdapter);
        mGridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
			@Override @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            public void onGlobalLayout() {

                final int width = mGridView.getWidth();
                final int height = mGridView.getHeight();

                final int desireSize = getResources().getDimensionPixelOffset(R.dimen.image_size);
                final int numCount = width / desireSize;
                final int columnSpace = getResources().getDimensionPixelOffset(R.dimen.space_size);
                int columnWidth = (width - columnSpace*(numCount-1)) / numCount;
                mImageAdapter.setItemSize(columnWidth);

                if(mFolderPopupWindow == null){
                    createPopupFolderList(width, height);
                }

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                    mGridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }else{
                    mGridView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(mImageAdapter.isShowCamera()){
                    // 如果显示照相机，则第一个Grid显示为照相机，处理特殊逻辑
                    if(i == 0){
                        showCameraAction();
                    }else{
                        // 正常操作
                        ImageSelectorImage imageSelectorImage = (ImageSelectorImage) adapterView.getAdapter().getItem(i);
                        selectImageFromGrid(imageSelectorImage, mode);
                    }
                }else{
                    // 正常操作
                    ImageSelectorImage imageSelectorImage = (ImageSelectorImage) adapterView.getAdapter().getItem(i);
                    selectImageFromGrid(imageSelectorImage, mode);
                }
            }
        });

        mImageSelectorFolderAdapter = new ImageSelectorFolderAdapter(getActivity());
    }

    /**
     * 创建弹出的ListView
     */
    private void createPopupFolderList(int width, int height) {
        mFolderPopupWindow = new ListPopupWindow(getActivity());
        mFolderPopupWindow.setAdapter(mImageSelectorFolderAdapter);
        mFolderPopupWindow.setContentWidth(AppUtils.getScreenWidth(getActivity()));
//        mFolderPopupWindow.setHeight(height * 5/8);
        mFolderPopupWindow.setAnchorView(mPopupAnchorView);
        mFolderPopupWindow.setModal(true);
        mFolderPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    getActivity().getSupportLoaderManager().restartLoader(LOADER_ALL, null, mLoaderCallback);
                    mCategoryText.setText(R.string.folder_all);
                    mImageAdapter.setShowCamera(true);
                } else {
                    ImageSelectorFolder imageSelectorFolder = (ImageSelectorFolder) adapterView.getAdapter().getItem(i);
                    if (null != imageSelectorFolder) {
                        Bundle args = new Bundle();
                        args.putString("path", imageSelectorFolder.path);
                        getActivity().getSupportLoaderManager().restartLoader(LOADER_CATEGORY, args, mLoaderCallback);
                        mCategoryText.setText(imageSelectorFolder.name);
                    }
                    mImageAdapter.setShowCamera(false);
                }
                mImageSelectorFolderAdapter.setSelectIndex(i);
                mFolderPopupWindow.dismiss();

                // 滑动到最初始位置
                mGridView.smoothScrollToPosition(0);
            }
        });
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 首次加载所有图片
        //new LoadImageTask().execute();
        getActivity().getSupportLoaderManager().initLoader(LOADER_ALL, null, mLoaderCallback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 相机拍照完成后，返回图片路径
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == REQUEST_CAMERA){
                if (mTmpFile != null) {
                    if (mCallback != null) {
                        mCallback.onCameraShot(mTmpFile);
                    }
                }
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        if(mFolderPopupWindow != null){
            if(mFolderPopupWindow.isShowing()){
                mFolderPopupWindow.dismiss();
            }
        }

        mGridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
			@Override @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            public void onGlobalLayout() {
                final int height = mGridView.getHeight();
                final int desireSize = getResources().getDimensionPixelOffset(R.dimen.image_size);
                final int numCount = mGridView.getWidth() / desireSize;
                final int columnSpace = getResources().getDimensionPixelOffset(R.dimen.space_size);
                int columnWidth = (mGridView.getWidth() - columnSpace*(numCount-1)) / numCount;
                mImageAdapter.setItemSize(columnWidth);

                if(mFolderPopupWindow != null){
                    mFolderPopupWindow.setHeight(height * 5/8);
                }

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                    mGridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }else{
                    mGridView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });

        super.onConfigurationChanged(newConfig);

    }

    /**
     * 选择相机
     */
    private void showCameraAction() {
        // 跳转到系统照相机
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(cameraIntent.resolveActivity(getActivity().getPackageManager()) != null){
            // 设置系统相机拍照后的输出路径
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTmpFile));
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        }else{
            Toast.makeText(getActivity(), R.string.msg_no_camera, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 选择图片操作
     * @param imageSelectorImage
     */
    private void selectImageFromGrid(ImageSelectorImage imageSelectorImage, int mode) {
        if(imageSelectorImage != null) {
            // 多选模式
            if(mode == MODE_MULTI) {
                if (resultList.contains(imageSelectorImage.path)) {
                    resultList.remove(imageSelectorImage.path);
                    if(resultList.size() != 0) {
                        mPreviewBtn.setEnabled(true);
                        mPreviewBtn.setText(getResources().getString(R.string.preview) + "(" + resultList.size() + ")");
                    }else{
                        mPreviewBtn.setEnabled(false);
                        mPreviewBtn.setText(R.string.preview);
                    }
                    if (mCallback != null) {
                        mCallback.onImageUnselected(imageSelectorImage.path);
                    }
                } else {
                    // 判断选择数量问题
                    if(mDesireImageCount == resultList.size()){
                        Toast.makeText(getActivity(), R.string.msg_amount_limit, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    resultList.add(imageSelectorImage.path);
                    mPreviewBtn.setEnabled(true);
                    mPreviewBtn.setText(getResources().getString(R.string.preview) + "(" + resultList.size() + ")");
                    if (mCallback != null) {
                        mCallback.onImageSelected(imageSelectorImage.path);
                    }
                }
                mImageAdapter.select(imageSelectorImage);
            }else if(mode == MODE_SINGLE){
                // 单选模式
                if(mCallback != null){
                    mCallback.onSingleImageSelected(imageSelectorImage.path);
                }
            }
        }
    }

    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media._ID };

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if(id == LOADER_ALL) {
                CursorLoader cursorLoader = new CursorLoader(getActivity(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        null, null, IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            }else if(id == LOADER_CATEGORY){
                CursorLoader cursorLoader = new CursorLoader(getActivity(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        IMAGE_PROJECTION[0]+" like '%"+args.getString("path")+"%'", null, IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null) {
                imageSelectorImages = new ArrayList<ImageSelectorImage>();
                int count = data.getCount();
                if (count > 0) {
                    data.moveToFirst();
                    do{
                        String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                        String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                        long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                        ImageSelectorImage imageSelectorImage = new ImageSelectorImage(path, name, dateTime);
                        imageSelectorImages.add(imageSelectorImage);
                       try{ if( !hasFolderGened ) {
                            // 获取文件夹名称
                            File imageFile = new File(path);
                            File folderFile = imageFile.getParentFile();
                            ImageSelectorFolder imageSelectorFolder = new ImageSelectorFolder();
                            imageSelectorFolder.name = folderFile.getName();
                            imageSelectorFolder.path = folderFile.getAbsolutePath();
                            imageSelectorFolder.cover = imageSelectorImage;
                            if (!mResultImageSelectorFolder.contains(imageSelectorFolder)) {
                                List<ImageSelectorImage> imageSelectorImageList = new ArrayList<ImageSelectorImage>();
                                imageSelectorImageList.add(imageSelectorImage);
                                imageSelectorFolder.imageSelectorImages = imageSelectorImageList;
                                mResultImageSelectorFolder.add(imageSelectorFolder);
                            } else {
                                // 更新
                                ImageSelectorFolder f = mResultImageSelectorFolder.get(mResultImageSelectorFolder.indexOf(imageSelectorFolder));
                                f.imageSelectorImages.add(imageSelectorImage);
                            }
                        }}catch (NullPointerException e){
                           e.printStackTrace();
                       }

                    }while(data.moveToNext());

                    mImageAdapter.setData(imageSelectorImages);

                    // 设定默认选择
                    if(resultList != null && resultList.size()>0){
                        mImageAdapter.setDefaultSelected(resultList);
                    }

                    mImageSelectorFolderAdapter.setData(mResultImageSelectorFolder);
                    hasFolderGened = true;

                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    /**
     * 回调接口
     */
    public interface Callback{
        public void onSingleImageSelected(String path);
        public void onImageSelected(String path);
        public void onImageUnselected(String path);
        public void onCameraShot(File imageFile);
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	String imgpaths= ISPreferencesUtils.getSharePreStr(getActivity(), "imgsdel");
    	if(!TextUtils.isEmpty(imgpaths)){
    		String[] img_paths=imgpaths.split(",");
    		for(int i=0;i<img_paths.length;i++){
    			 if (resultList.contains(img_paths[i])) {
                     resultList.remove(img_paths[i]);
                     if(resultList.size() != 0) {
                         mPreviewBtn.setEnabled(true);
                         mPreviewBtn.setText(getResources().getString(R.string.preview) + "(" + resultList.size() + ")");
                     }else{
                         mPreviewBtn.setEnabled(false);
                         mPreviewBtn.setText(R.string.preview);
                     }
                     if (mCallback != null) {
                         mCallback.onImageUnselected(img_paths[i]);
                     }
                     ImageSelectorImage imageSelectorImage =getImageByPath(img_paths[i]);
                     if(imageSelectorImage !=null){
                    	 mImageAdapter.select(getImageByPath(img_paths[i]));
                     }
                 } 
    		}
    		ISPreferencesUtils.putSharePre(getActivity(), "imgsdel", "");
    	}
    }
    
/**
 * 根据
 * @param path
 * @return
 */
    private ImageSelectorImage getImageByPath(String path){
        if(imageSelectorImages != null && imageSelectorImages.size()>0){
            for(ImageSelectorImage imageSelectorImage : imageSelectorImages){
                if(imageSelectorImage.path.equalsIgnoreCase(path)){
                    return imageSelectorImage;
                }
            }
        }
        return null;
    }
    
}
