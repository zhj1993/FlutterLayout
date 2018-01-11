package com.zhj.flutter.flutter;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.zhj.flutter.R;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 飘屏布局
 * Created by zhahaijun on 2018/1/5.
 * Email 18655961751@163.com
 */

public class FlutterLayout extends LinearLayout implements IFlutterAnimCallBack {

    private int mFlutterSize = 1;//飘屏个数

    /**
     * 飘屏队列(在多个线程中使用此List)
     */
    private CopyOnWriteArrayList<String> mListQueue;

    public FlutterLayout(Context context) {
        this(context, null);
    }

    public FlutterLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlutterLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FlutterView);
        if (null != typedArray) {
            mFlutterSize = typedArray.getInteger(R.styleable.FlutterView_flutter_size, mFlutterSize);
            typedArray.recycle();
        }
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        mListQueue = new CopyOnWriteArrayList<>();
        for (int i = 0; i < mFlutterSize; i++) {
            FlutterView flutterView = new FlutterView(getContext());
            flutterView.setPosition(i);
            flutterView.setiFlutterAnimCallBack(this);
            flutterView.setVisibility(INVISIBLE);
            addView(flutterView);
        }
    }

    /**
     * 添加飘屏
     *
     * @param string
     */
    public synchronized void addFlutterMsg(String string) {
        if (null != string) {
            boolean addflag = false;//是否添加到队列
            for (int i = 0; i < getChildCount(); i++) {
                FlutterView flutterView = (FlutterView) getChildAt(i);
                if (!flutterView.isShowing()) {//不在显示
                    addflag = true;
                    flutterView.addFlutterMsg(string);
                    break;
                }
            }
            if (!addflag) {//都在显示  就添加到集合里面
                mListQueue.add(string);
            }
        }
    }

    /**
     * 飘屏动画消失回调
     *
     * @param position
     */
    @Override
    public void onDismiss(int position) {
        //防止数组下标越界
        if (position >= 0 && position <= getChildCount() - 1
                && null != mListQueue && mListQueue.size() > 0) {
            FlutterView flutterView = (FlutterView) getChildAt(position);
            String string = mListQueue.get(0);
            flutterView.addFlutterMsg(string);//添加之后 移除队列
            mListQueue.remove(0);
        }
    }

    /**
     * 移除全部消息
     */
    public void clear() {
        for (int i = 0; i < getChildCount(); i++) {
            FlutterView flutterView = (FlutterView) getChildAt(i);
            flutterView.clear();
        }
        if (null != mListQueue) {
            mListQueue.clear();
        }
    }
}
