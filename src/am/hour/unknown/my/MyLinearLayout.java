package am.hour.unknown.my;

import java.util.Hashtable;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

//这个类是放在首页面下方的那个，可以动态添加元素，而且可以自动换行显示
public class MyLinearLayout extends LinearLayout {
    int mLeft, mRight, mTop, mBottom;
    Hashtable<View, Position> map = new Hashtable<View, Position>();
    private final int dividerLine = 5;  
    private final int dividerCol = 8;  


    public MyLinearLayout(Context context) {
        super(context);
    }


    public MyLinearLayout(Context context, int horizontalSpacing, int verticalSpacing) {
        super(context);
    }


    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {


        int mWidth = MeasureSpec.getSize(widthMeasureSpec);
        int mCount = getChildCount();
//        int mX = 0;
//        int mY = 0;
        mLeft = 0;
        mRight = 0;
        mTop = 5;
        mBottom = 0;


        int j = 0;


        for (int i = 0; i < mCount; i++) {
            final View child = getChildAt(i);
           
            child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            int childw = child.getMeasuredWidth();
            int childh = child.getMeasuredHeight();
            mRight += childw;
            Position position = new Position();
            mLeft = getPosition(i - j, i);
            mRight = mLeft + child.getMeasuredWidth();
            if (mRight >= mWidth) {
//                mX = childw;
//                mY += childh;
                j = i;
                mLeft = getPaddingLeft();
                mRight = mLeft + child.getMeasuredWidth();
                mTop += childh + dividerLine;
            }
            mBottom = mTop + child.getMeasuredHeight();
//            mY = mTop;  
//            mX = mRight;
            position.left = mLeft;
            position.top = mTop;
            position.right = mRight;
            position.bottom = mBottom;
            map.put(child, position);
        }
        setMeasuredDimension(mWidth, mBottom+getPaddingBottom());
    }


    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(1, 1); // default of 1px spacing
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // TODO Auto-generated method stub


        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            Position pos = map.get(child);
            if (pos != null) {
                child.layout(pos.left, pos.top, pos.right, pos.bottom);
            } else {
                Log.i("MyLayout", "error");
            }
        }
    }


    private class Position {
        int left, top, right, bottom;
    }


    public int getPosition(int IndexInRow, int childIndex) {
        if (IndexInRow > 0) {
            return getPosition(IndexInRow - 1, childIndex - 1)
                    + getChildAt(childIndex - 1).getMeasuredWidth() + dividerCol;
        }
        return getPaddingLeft();
    }
}
