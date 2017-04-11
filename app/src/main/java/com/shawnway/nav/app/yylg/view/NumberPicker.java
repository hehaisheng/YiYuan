package com.shawnway.nav.app.yylg.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.shawnway.nav.app.yylg.R;

import java.lang.ref.WeakReference;

public class NumberPicker extends LinearLayout implements OnClickListener {

    private static final String TAG = "NumberPicker";
    private Context context;
    private int gap = 1;//加减的间隔
    private EditText inputer;
    private int maxValue = 100;
    private int curNum;
    private int perWorth = 1;//单位价值

    public interface OnAmountChangeListener {
        public void onAmountChange(int amount, int tag);
    }


    public NumberPicker(Context context) {
        this(context, null);
    }

    public NumberPicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initListener();
    }

    private void initListener() {
        LayoutInflater.from(context).inflate(R.layout.layout_numberpicker, this, true);
        ImageButton minusBtn = (ImageButton) findViewById(R.id.minus);
        minusBtn.setOnClickListener(this);
        ImageButton addBtn = (ImageButton) findViewById(R.id.plus);
        addBtn.setOnClickListener(this);
        inputer = (EditText) findViewById(R.id.input);
        inputer.setText(getResources().getInteger(R.integer.inputer_numpicker_default) + "");//设置购物车默认的购买数量为1
//        if(){//如果是十元区的，设置默认的初始值为10
//            inputer.setText(getResources().getInteger(R.integer.inputer_numpicker_ten_default)+"");
//        }
        inputer.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    fixCurNum((EditText) v);
                }
            }


        });
    }

    private void fixCurNum(EditText v) {
        String content = v.getText().toString();
        if (content == null || content.isEmpty())
            content = String.valueOf(gap);
//        int sorted = fix2gap > maxValue ? maxValue : fix2gap;//判断是否超过最大值
        notifyAmountChange(Integer.parseInt(content),false);
    }

    private int castToGap(int parseInt) {
        if (maxValue<gap)
            return parseInt;
        else {
            int nubCasted = parseInt % gap == 0 ? parseInt : (parseInt / gap + 1) * gap;//判断是否gap的整数倍，进一法
            return nubCasted > maxValue ? maxValue : nubCasted; //判断转换后的数值是否超出最大值
        }
    }



    public void setCurNum(int number) {
        inputer.setText(number + "");
        fixCurNum(inputer);
    }

    private void notifyAmountChange(int number,boolean castToGap) {
        if (castToGap)
            number=castToGap(number);
        else
            number=number>maxValue?maxValue:number;
        curNum = number;
        inputer.setText(number + "");
        if (mAmountChangeListener != null) {
            if (getTag() == null)
                setTag(-1);
            mAmountChangeListener.onAmountChange(curNum, (Integer) getTag());
        }
    }

    public void setMaxValue(int maximun) {
        maxValue = maximun;
    }

    public void setGapValue(int gap) {
        this.gap = gap;
        nofityGapChange();
    }

    public int getPerWorth() {
        return perWorth;
    }

    public int getWorth() {
        return curNum * gap;
    }

//    public void setPerWorth(int perWorth) {
//        if (this.perWorth != perWorth) {
//            mAmountChangeListener.onAmountChange(getWorth(), tag);//总价值变了，告诉拥有者
//        }
//        this.perWorth = perWorth;
//    }


    private void nofityGapChange() {
        inputer.setText(castToGap(Integer.parseInt(inputer.getText().toString())) + "");
        Log.d(TAG, "numberPicker value after gap change:" + inputer.getText().toString());
    }


    public void setAmountChangeListener(OnAmountChangeListener listener) {
        mAmountChangeListener = listener;
    }

    private OnAmountChangeListener mAmountChangeListener;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        final int proposedheight = MeasureSpec.getSize(heightMeasureSpec);
//        final int actualHeight = getHeight();
//
//        if (actualHeight > proposedheight) {
//            // Keyboard is shown
//        } else {
//            // Keyboard is hidden

        //监听键盘事件
        if (inputer.isFocused()) {
            Handler handler = new MyHandler(this);
            handler.sendEmptyMessage(0);
//                Log.d(TAG, "keyboard hidden:actuallheight->"+actualHeight+"  proposeheight->"+proposedheight);
        }
//
//        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    public void onClick(View v) {
        curNum = Integer.parseInt(inputer.getText().toString());
        switch (v.getId()) {
            case R.id.minus:
                notifyAmountChange(curNum - gap < 0 ? 0 : curNum - gap,true);
                break;
            case R.id.plus:
               notifyAmountChange(curNum + gap > maxValue ? maxValue : curNum + gap,true);
                break;
            default:
                break;
        }

        inputer.setText(curNum + "");

    }

    private static class MyHandler extends Handler{
        private final WeakReference<NumberPicker> mPicker;

        public MyHandler(NumberPicker picker){
            mPicker=new WeakReference<>(picker);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mPicker.get().fixCurNum(mPicker.get().inputer);
            mPicker.get().inputer.selectAll();
        }

    }
    //    @Override
//    protected void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        if (newConfig.hardKeyboardHidden==newConfig.HARDKEYBOARDHIDDEN_YES||newConfig.keyboardHidden==newConfig.KEYBOARDHIDDEN_YES){
//            Log.d(TAG,"keyboard hidden");
//            notifyAmountChange(castToGap(Integer.parseInt(((EditText) inputer).getText().toString())));
//            inputer.selectAll();
//        }
//    }

}
