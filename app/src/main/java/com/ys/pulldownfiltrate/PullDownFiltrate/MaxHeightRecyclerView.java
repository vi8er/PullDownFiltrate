package com.ys.pulldownfiltrate.PullDownFiltrate;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import com.ys.pulldownfiltrate.R;

/**
 * 可设置最大高度的recyclerview
 */
public class MaxHeightRecyclerView extends RecyclerView {

    private int maxHeight;

    public MaxHeightRecyclerView(Context context) {
        super(context);
    }

    public MaxHeightRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public MaxHeightRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context, attrs);
    }

    private void initialize(Context context, AttributeSet attributeSet) {
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.MaxHeightRecyclerView);
        maxHeight = typedArray.getLayoutDimension(R.styleable.MaxHeightRecyclerView_maxHeight, maxHeight);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        if (maxHeight > 0) {
            heightSpec = View.MeasureSpec.makeMeasureSpec(maxHeight, View.MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthSpec, heightSpec);
    }
}
