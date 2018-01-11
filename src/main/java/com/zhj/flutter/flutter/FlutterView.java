package com.zhj.flutter.flutter;

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

import com.zhj.flutter.R;
import com.zhj.flutter.util.ScreenUtils;

import at.wirecube.additiveanimations.additive_animator.AdditiveAnimator;


/**
 * 飘屏View
 * Created by zhahaijun on 2018/1/5.
 * Email 18655961751@163.com
 */

public class FlutterView extends RelativeLayout {

    private String TAG = "FlutterView";

    private boolean isShowing;//是否正在显示
    private int position;//当前下标

    //动画消失回调
    private IFlutterAnimCallBack iFlutterAnimCallBack;

    int[] startLocation = new int[]{ScreenUtils.getScreenWidth(getContext()), 0};

    float screenWidth = -ScreenUtils.getScreenWidth(getContext());//屏幕宽度
    private int mAnimTime = 5000;//飘屏时间
    private String mContent;//内容

    public void setiFlutterAnimCallBack(IFlutterAnimCallBack iFlutterAnimCallBack) {
        this.iFlutterAnimCallBack = iFlutterAnimCallBack;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public FlutterView(Context context) {
        this(context, null);
    }

    public FlutterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlutterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        View.inflate(getContext(), R.layout.widget_flutter_win_layout, this);
    }

    /**
     * 添加 消息
     *
     * @param string
     */
    public synchronized void addFlutterMsg(String string) {
        this.mContent = string;
        if (mContent != null) {
            isShowing = true;
            showFlutterView();
        }
    }

    /**
     * 显示飘屏动画  国外的一个大神动画库
     */
    public void showFlutterView() {
        AdditiveAnimator.animate(this)
                .x(startLocation[0])//先回到起点
                .then()
                .x(screenWidth)//开始从右向左的横向移动
                .setDuration(mAnimTime)
                .setInterpolator(new LinearInterpolator())//匀速移动
                .addListener(mAnimatorListener).start();
    }


    //动画监听
    Animator.AnimatorListener mAnimatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            isShowing = true;
            setVisibility(VISIBLE);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            isShowing = false;
            if (getVisibility() != INVISIBLE) {
                setVisibility(INVISIBLE);
            }
            if (null != iFlutterAnimCallBack) {//消失回调 通知外部调用
                iFlutterAnimCallBack.onDismiss(position);
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    };

    /**
     * 是否正在显示
     *
     * @return
     */
    public boolean isShowing() {
        return isShowing;
    }

    /**
     * 设置显示
     *
     * @param view
     */
    private void setLayoutVisibility(View view) {
        if (null != view && view.getVisibility() != VISIBLE) {
            view.setVisibility(VISIBLE);
        }
    }

    /**
     * 设置隐藏
     *
     * @param view
     */
    private void setLayoutGone(View view) {
        if (null != view && view.getVisibility() != INVISIBLE) {
            view.setVisibility(INVISIBLE);
        }
    }

    /**
     * 清除消息
     * 本意是清除动画 但是动画库清除动画的方法有bug存在  所以只需更换自己写的动画就可以
     */
    public void clear() {
        isShowing = false;
        mContent = null;
        if (getVisibility() != INVISIBLE) {
            setVisibility(INVISIBLE);
        }
    }
}
