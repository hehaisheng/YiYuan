package com.shawnway.nav.app.yylg.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shawnway.nav.app.yylg.R;
import com.shawnway.nav.app.yylg.tool.ToastUtil;
import com.shawnway.nav.app.yylg.tool.Utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * ViewPager实现的轮播图广告自定义视图，如京东首页的广告轮播图效果； 既支持自动轮播页面也支持手势滑动切换页面
 */

public class MySlideShowView extends FrameLayout {
    // 自动轮播的时间间隔
    private final static int TIME_INTERVAL = 8;
    // 自动轮播启用开关
    private static boolean isAutoPlay = true;
    private int maskColor;
    private int focusDraw;
    private int blurDraw;

    // 自定义轮播图的资源
//    private String[] imageUrls;
    // 放轮播图片的ImageView 的list
//    private List<CirclerImageView> imageViewsList;
    // 放圆点的View的list
    private List<View> dotViewsList;

    private ViewPager viewPager;
    // 当前轮播页
    private int currentItem = 0;
    // 定时任务
    private ScheduledExecutorService scheduledExecutorService;

    private Context context;

    // Handler
    private Handler mHandler;
    private static class MyHandler extends Handler{
        private final WeakReference<MySlideShowView> mSlider;

        public MyHandler (MySlideShowView slider){
            mSlider=new WeakReference<>(slider);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mSlider.get().viewPager.setCurrentItem(mSlider.get().currentItem);
        }

    }
    public MySlideShowView(Context context) {
        this(context, null);
    }

    public MySlideShowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MySlideShowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        mHandler =new MyHandler(this);


        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.MySlideShowView);

        int n = a.getIndexCount();

        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.MySlideShowView_mssvFocus:
                    focusDraw = a.getResourceId(attr, 0);
                    break;
                case R.styleable.MySlideShowView_mssvBlur:
                    blurDraw = a.getResourceId(attr, 0);
                    break;
                case R.styleable.MySlideShowView_mssvMask:
                    maskColor = a.getColor(attr, getResources().getColor(R.color.transparent));
                    break;

            }
        }

        a.recycle();

        if (focusDraw == 0) focusDraw = R.drawable.dot_focused;
        if (blurDraw == 0) blurDraw = R.drawable.dot_blured;

        initData();
        if (isAutoPlay) {
            startPlay();
        }

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
//        getParent().requestDisallowInterceptTouchEvent(true); //只需这句话，让父类不拦截触摸事件就可以了。
        //TODO:判断一个下滑的事件，然后做一个监听的接口给外部调用
        setRefreshListener(event);
        return super.dispatchTouchEvent(event);
    }

    int startX = 0,startY = 0;
    int disX = 0,disY = 0;
    private void setRefreshListener(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:// 手指按下时
                startX = (int) event.getRawX();
                startY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:// 手指移动时
                int currentX = (int) event.getRawX();// 获取当前x坐标
                int currentY = (int) event.getRawY();// 获取当前y坐标
                disX = currentX - startX;// x方向移动的距离
                disY = currentY - startY;// y方向移动的距离
                break;
            case MotionEvent.ACTION_UP:// 手指离开时
                // 当x方向移动的距离小于y方向移动的距离时
                if (Math.abs(disX) < Math.abs(disY) && mRefresh!=null) {//做一个监听的接口给外部调用
                    mRefresh.onPullRefresh();
                }else{
//                    ToastUtil.showShort(context,"水平方向滑动");
                }
                startX = (int) event.getRawX();
                startY = (int) event.getRawY();
                break;

        }
    }

    public void setOnRefreshListener(OnRefreshListner listener){
        mRefresh=listener;
    }

    OnRefreshListner mRefresh;

    public interface OnRefreshListner{
        void onPullRefresh();
    }

    /**
     * 开始轮播图切换
     */
    private void startPlay() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), TIME_INTERVAL, TIME_INTERVAL,
                TimeUnit.SECONDS);
    }

    /**
     * 停止轮播图切换
     */
    private void stopPlay() {
        scheduledExecutorService.shutdown();
    }

    /**
     * 初始化相关Data
     */
    private void initData() {
//        imageViewsList = new ArrayList<CirclerImageView>();
        dotViewsList = new ArrayList<View>();

//		// 一步任务获取图片
//		new GetListTask().execute("");

    }

    /**
     * 初始化Views等UI
     */
    private void initUI(Context context) {
        if (mData== null)
            return;

        LayoutInflater.from(context).inflate(R.layout.layout_slideshow, this,
                true);

        LinearLayout dotLayout = (LinearLayout) findViewById(R.id.dotLayout);
        dotLayout.removeAllViews();

        // 热点个数与图片特殊相等
        for (int i = 0; i < mData.size(); i++) {

//            imageViewsList.add(view);

            ImageView dotView = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.leftMargin = 4;
            params.rightMargin = 4;
            dotLayout.addView(dotView, params);
            dotViewsList.add(dotView);
        }

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setFocusable(true);
//        viewPager.setOffscreenPageLimit(6);
        viewPager.setAdapter(new MyPagerAdapter());
        viewPager.setOnPageChangeListener(new MyPageChangeListener());
        viewPager.setCurrentItem(0);

        try {
            selectPage(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnItemClickListener(OnItemClickListner listener){
        mOnItemClick=listener;
    }

    OnItemClickListner mOnItemClick;

    public interface OnItemClickListner{
        void onItemClick(int pos);
    }

    /**
     * 填充ViewPager的页面适配器
     */
    private class MyPagerAdapter extends PagerAdapter implements OnClickListener {

        @Override
        public void destroyItem(View container, int position, Object object) {
            // ((ViewPag.er)container).removeView((View)object);
            ((ViewPager) container).removeView((View) object);
        }

        @Override
        public Object instantiateItem(View container, int position) {
            View item = LayoutInflater.from(context).inflate(R.layout.abc_item_slide_show, null, false);
//            ((CirclerImageView) item.findViewById(R.id.imageView)).setBigImage(bean.url, 300, 300);
            if(mData.size() == 0){
                Log.d("轮播图", "数据为空");
                CirclerImageView imageView = (CirclerImageView) item.findViewById(R.id.yylg_imageView);
                imageView.setImage(getContext());
                imageView.setBackgroundResource(R.drawable.img_blank);
                //设置遮罩
                item.findViewById(R.id.mask).setVisibility(View.GONE);
                //设置标题tag
                TextView tag= (TextView) item.findViewById(R.id.tag);
                tag.setVisibility(GONE);
                //设置标题
                TextView title= (TextView) item.findViewById(R.id.title);
                title.setVisibility(GONE);
            }else{
                Log.d("轮播图", "数据不为空"+mData.size());
                final int maskPos = position % mData.size();
                SlideShowBean bean = mData.get(maskPos);
                item.setTag(maskPos);
                item.setOnClickListener(this);
                ((CirclerImageView) item.findViewById(R.id.yylg_imageView)).setImage(bean.url);
                //设置遮罩
                item.findViewById(R.id.mask).setBackgroundColor(maskColor == 0 ? getResources().getColor(R.color.transparent) : maskColor);
                //设置标题tag
                TextView tag= (TextView) item.findViewById(R.id.tag);
                tag.setVisibility(bean.tag == null ? GONE : VISIBLE);
                tag.setText(getResources().getString(R.string.slide_show_tag_wrapper,bean.tag));
                //设置标题
                TextView title= (TextView) item.findViewById(R.id.title);
                title.setVisibility(bean.title == null ? GONE : VISIBLE);
                title.setText(bean.title);
            }
            try {
                ((ViewPager) container).addView(item);
            } catch (Exception e) {
            }
            return item;
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClick!=null)
                mOnItemClick.onItemClick((Integer) v.getTag());
        }
//
//        @Override
//        public void restoreState(Parcelable arg0, ClassLoader arg1) {
//
//        }
//
//        @Override
//        public Parcelable saveState() {
//            return null;
//        }

//        @Override
//        public void startUpdate(View arg0) {
//
//        }
//
//        @Override
//        public void finishUpdate(View arg0) {
//
//        }

    }

    /**
     * ViewPager的监听器 当ViewPager中页面的状态发生改变时调用
     */
    private class MyPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            switch (arg0) {
                case 1:// 手势滑动，空闲中
                    isAutoPlay = false;
//				Log.d(TAG, "page freeing");
                    break;
                case 2:// 界面切换中
//				Log.d(TAG, "page tabing");
                    isAutoPlay = true;
                    break;
                default:
//				Log.d(TAG, "page default code:"+arg0);

            }
        }

        /**
         *
         * @param arg0 第一页的位置指数目前显示。 页面位置+ 1可见如果positionOffset是零。
         * @param arg1 值(0,1)表示页面位置的偏移。
         * @param arg2 在像素值指示位置的偏移量
         */
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int pos) {

            selectPage(pos);
        }


    }

    private void selectPage(int pos) {
        currentItem = pos;
        if(mData.size() == 0){
            int maskpos = 0;
            for (int i = 0; i < dotViewsList.size(); i++) {
                if (i == maskpos) {
                    ((View) dotViewsList.get(i))
                            .setBackgroundResource(focusDraw);
                } else {
                    ((View) dotViewsList.get(i))
                            .setBackgroundResource(blurDraw);
                }
            }
        }else{
            int maskpos = pos % mData.size();
            for (int i = 0; i < dotViewsList.size(); i++) {
                if (i == maskpos) {
                    ((View) dotViewsList.get(i))
                            .setBackgroundResource(focusDraw);
                } else {
                    ((View) dotViewsList.get(i))
                            .setBackgroundResource(blurDraw);
                }
            }
        }
    }


    /**
     * 执行轮播图切换任务
     */
    private class SlideShowTask implements Runnable {

        @Override
        public void run() {
            synchronized (viewPager) {
//				currentItem = (currentItem + 1) % imageViewsList.size();
                if (isAutoPlay) {
                    currentItem++;
                    mHandler.obtainMessage().sendToTarget();
                }
            }
        }

    }

//	/**
//	 * 销毁ImageView资源，回收内存
//	 *
//	 */
//	private void destoryBitmaps() {
//
//		for (int i = 0; i < IMAGE_COUNT; i++) {
//			ImageView imageView = imageViewsList.get(i);
//			Drawable drawable = imageView.getDrawable();
//			if (drawable != null) {
//				// 解除drawable对view的引用
//				drawable.setCallback(null);
//			}
//		}
//	}

    public void initPic(String[]imageUrls){
        initPic(imageUrls,null);
    }

    public void initPic(String[] imageUrls,String defTag) {
        mData=new ArrayList<>();
        for (int i = 0; i < imageUrls.length; i++) {
            mData.add(new SlideShowBean(imageUrls[i],defTag));
        }
        initUI(context);
    }


    public void initPic(ArrayList<SlideShowBean> beans) {
        mData=beans;
        initUI(context);
    }

    ArrayList<SlideShowBean> mData;

    public static class SlideShowBean {
        String url; //图片url
        String tag; //标题前面的标签
        String title;//标题

        public SlideShowBean(String imageUrl) {
            this(imageUrl,null);
        }

        public SlideShowBean(String imageUrl,String tag) {
            url=imageUrl;
            this.tag=tag;
        }
    }


}