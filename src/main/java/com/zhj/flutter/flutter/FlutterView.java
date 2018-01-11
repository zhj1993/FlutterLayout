package com.zhj.flutter.flutter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import com.zhj.flutter.R;


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
     * 显示飘屏
     */
    private Animation mTranslateAnim;

    public void showFlutterView() {
        if (null == mTranslateAnim) {
            mTranslateAnim = getTranslateAnim();
            mTranslateAnim.setAnimationListener(mAnimationListener);
            mTranslateAnim.setInterpolator(new LinearInterpolator());
        }
        mTranslateAnim.setDuration(mAnimTime);
        startAnimation(mTranslateAnim);
    }

    private Animation getTranslateAnim() {
        Animation translate = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 1,
                Animation.RELATIVE_TO_PARENT, -1f, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0);
        translate.setInterpolator(new LinearInterpolator());
        return translate;
    }

    Animation.AnimationListener mAnimationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            isShowing = true;
            setVisibility(VISIBLE);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            isShowing = false;
            setVisibility(INVISIBLE);
            if (iFlutterAnimCallBack != null) {//消失回调
                iFlutterAnimCallBack.onDismiss(position);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

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
     * 清除动画
     */
    public void clear() {
        if (getVisibility() != INVISIBLE) {
            setVisibility(INVISIBLE);
        }
        if (null != mTranslateAnim && isShowing()) {
            mTranslateAnim.cancel();
            if (getAnimation() != null) {
                getAnimation().cancel();
            }
        }
        isShowing = false;
        mContent = null;
    }
}
