package com.bcgtgjyb.autolable;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bigwen on 2016/1/15.
 */
public class AutoLable extends ViewGroup {

    private String TAG = AutoLable.class.getName();
    private List<MyView> lists = new ArrayList<>();
    private boolean isAdd = false;

    public AutoLable(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(
            ViewGroup.LayoutParams p)
    {
        return new MarginLayoutParams(p);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs)
    {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams()
    {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int lineWidth = 0;
        int nowHeight = 0;
        int lastViewHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            final View view = getChildAt(i);
            measureChild(view,widthMeasureSpec,heightMeasureSpec);
            MyView myView = new MyView();

            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) view.getLayoutParams();
            int viewWidth = view.getMeasuredWidth() + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin;
            int viewHeight = view.getMeasuredHeight() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin;
            Log.i(TAG, "onMeasure view  "+viewWidth +"  "+viewHeight);
            Log.i(TAG, "onMeasure now  "+lineWidth +"  "+nowHeight);
            //没有超过一行宽度
            if (lineWidth + viewWidth <= width) {
                Log.i(TAG, "onMeasure 没有超过一行宽度");
                myView.l = lineWidth;
                myView.r = lineWidth + viewWidth;
                myView.t = nowHeight;
                myView.b = nowHeight + viewHeight;
                lineWidth = lineWidth + viewWidth;
                isAdd = false;
            } else {
                Log.i(TAG, "onMeasure 超过一行宽度换行");
                isAdd = true;
                lineWidth = 0;
                nowHeight = nowHeight + viewHeight;
                myView.l = lineWidth;
                myView.r = lineWidth + viewWidth;
                myView.t = nowHeight;
                myView.b = nowHeight + viewHeight;
                lineWidth = lineWidth+viewWidth;
            }
            lists.add(myView);
            lastViewHeight = Math.max(lastViewHeight,viewHeight);


        }
        Log.i(TAG, "onMeasure setMeasuredDimension"+width+"  "+nowHeight + lastViewHeight);
        setMeasuredDimension(width, nowHeight + lastViewHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.i(TAG, "onLayout "+lists.size());
        for(int i =0;i<getChildCount();i++){
            getChildAt(i).layout(lists.get(i).l,
                    lists.get(i).t,
                    lists.get(i).r,
                    lists.get(i).b);
        }
    }


    private class MyView {
        public int l;
        public int t;
        public int r;
        public int b;
    }

}
