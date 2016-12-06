package com.gary.chemmaster.ui;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by gary on 16/12/5.
 */
public class CYLMainViewPager extends ViewPager {

    int totalPager;

    /** 触摸时按下的点 **/
    PointF downP = new PointF();
    /** 触摸时当前的点 **/
    PointF curP = new PointF();
    OnSingleTouchListener onSingleTouchListener;

    public CYLMainViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public CYLMainViewPager(Context context, int totalPager) {
        super(context);
        this.totalPager = totalPager;
    }

    public CYLMainViewPager(Context context) {
        super(context);
    }


    private float xDistance, yDistance, xLast, yLast;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        //当拦截触摸事件到达此位置的时候，返回true，
        //说明将onTouch拦截在此控件，进而执行此控件的onTouchEvent

//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                xDistance = yDistance = 0f;
//                xLast = ev.getX();
//                yLast = ev.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
////                final float curX = ev.getX();
////                final float curY = ev.getY();
////
////                xDistance += Math.abs(curX - xLast);
////                yDistance += Math.abs(curY - yLast);
////                xLast = curX;
////                yLast = curY;
////
////                if (xDistance > yDistance) {
////                    return false;   //水平滑动时拦截事件
////                }
////                else return false;
//                return false;
//        }

//        return super.onInterceptTouchEvent(ev);
        return false;
    }


    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        // TODO Auto-generated method stub
        //每次进行onTouch事件都记录当前的按下的坐标
//        curP.x = arg0.getX();
//        curP.y = arg0.getY();
//
//        if(arg0.getAction() == MotionEvent.ACTION_DOWN){
//            //记录按下时候的坐标
//            //切记不可用 downP = curP ，这样在改变curP的时候，downP也会改变
//            downP.x = arg0.getX();
//            downP.y = arg0.getY();
//            //此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
//            getParent().requestDisallowInterceptTouchEvent(true);
//        }
//
//        if(arg0.getAction() == MotionEvent.ACTION_MOVE){
//            //此句代码是为了通知他的父ViewPager现在进行的是本控件的操作，不要对我的操作进行干扰
//            getParent().requestDisallowInterceptTouchEvent(true);
//        }
//
//        if(arg0.getAction() == MotionEvent.ACTION_UP){
//            //在up时判断是否按下和松手的坐标为一个点
//            //如果是一个点，将执行点击事件，这是我自己写的点击事件，而不是onclick
//            if(downP.x==curP.x && downP.y==curP.y){
//                onSingleTouch();
//                return true;
//            }
//        }
//
//        return super.onTouchEvent(arg0);

        return false;
    }

    /**
     * 单击
     */
    public void onSingleTouch() {
        if (onSingleTouchListener!= null) {

            onSingleTouchListener.onSingleTouch();
        }
    }

    /**
     * 创建点击事件接口
     * @author wanpg
     *
     */
    public interface OnSingleTouchListener {
        public void onSingleTouch();
    }

    public void setOnSingleTouchListener(OnSingleTouchListener onSingleTouchListener) {
        this.onSingleTouchListener = onSingleTouchListener;
    }


}
