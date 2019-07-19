package com.ys.pulldownfiltrate.PullDownFiltrate;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.RadioGroup;

/**
 * 自定义RadioButton 在宽是match_parent时drawableRight图片还在文字旁边并且可以再次点击取消
 * Created by 贾明辉 on 2017/10/12.
 */

public class MyRadioButton extends android.support.v7.widget.AppCompatRadioButton {
    public MyRadioButton(Context context) {
        super(context);
    }

    public MyRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void toggle() {
        setChecked(!isChecked());
        if (!isChecked()) {
            ((RadioGroup) getParent()).clearCheck();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable[] drawables = getCompoundDrawables();
        if (drawables != null) {
            try {
                Drawable leftDrawable = drawables[0]; //drawableLeft
                Drawable rightDrawable = drawables[2];//drawableRight
                if (leftDrawable != null || rightDrawable != null) {
                    //1,获取text的width
                    float textWidth = getPaint().measureText(getText().toString());
                    //2,获取padding
                    int drawablePadding = getCompoundDrawablePadding();
                    int drawableWidth;
                    float bodyWidth;
                    if (leftDrawable != null) {
                        //3,获取drawable的宽度
                        drawableWidth = leftDrawable.getIntrinsicWidth();
                        if (textWidth > getWidth() - drawablePadding - drawableWidth) {
                            textWidth = getWidth() - drawablePadding - drawableWidth;
                        }
                        //4,获取绘制区域的总宽度
                        bodyWidth = textWidth + drawablePadding + drawableWidth;
                    } else {
                        drawableWidth = rightDrawable.getIntrinsicWidth();
                        if (textWidth > getWidth() - drawablePadding - drawableWidth) {
                            textWidth = getWidth() - drawablePadding - drawableWidth;
                        }
                        bodyWidth = textWidth + drawablePadding + drawableWidth;
                        //图片居右设置padding
                        setPadding(0, 0, (int) (getWidth() - bodyWidth), 0);
                    }
                    canvas.translate((getWidth() - bodyWidth) / 2, 0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onDraw(canvas);
    }
}
