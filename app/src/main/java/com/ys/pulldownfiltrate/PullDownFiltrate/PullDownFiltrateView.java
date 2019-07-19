package com.ys.pulldownfiltrate.PullDownFiltrate;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import com.ys.pulldownfiltrate.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装下拉刷新PopupWindow与radioGroup
 */
public class PullDownFiltrateView extends LinearLayout {

    Context ctx;
    List<BaseFiltrateItem> filtrateItems;
    RadioGroup radioGroup;

    public PullDownFiltrateView(Context context) {
        super(context);
        this.ctx = context;
        init();
    }

    public PullDownFiltrateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.ctx = context;
        init();
    }

    public PullDownFiltrateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.ctx = context;
        init();
    }

    @TargetApi(21)
    public PullDownFiltrateView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.ctx = context;
        init();
    }

    private void init() {
        filtrateItems = new ArrayList<>();
        radioGroup = new RadioGroup(ctx);
        radioGroup.setGravity(Gravity.CENTER_VERTICAL);
        radioGroup.setShowDividers(SHOW_DIVIDER_MIDDLE);
        radioGroup.setOrientation(HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(radioGroup, layoutParams);
        setBackgroundColor(ctx.getResources().getColor(R.color.white));

    }

    public void setDivider(Drawable divider){
        if (divider != null) {
            radioGroup.setDividerDrawable(divider);
        }
    }

    public void setDivider(Drawable divider,int dividerPadding){
        if(divider != null){
            radioGroup.setDividerDrawable(divider);
            radioGroup.setDividerPadding(dividerPadding);
        }
    }

    /**
     * 隐藏分割线
     */
    public void hideDivider() {
        radioGroup.setDividerDrawable(null);
        radioGroup.setShowDividers(SHOW_DIVIDER_NONE);
    }

    public void addFiltrateItem(final BaseFiltrateItem item) {
        final int site = filtrateItems.size();
        filtrateItems.add(item);
        item.radioButton.setId(site);
        item.radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                item.setCheck(isChecked, radioGroup);
            }
        });
        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.weight = 1;
        item.parentGroup = radioGroup;
        radioGroup.addView(item.radioButton, site, layoutParams);
        ColorDrawable dw = new ColorDrawable(0x80000000);
        item.popupWindow.setBackgroundDrawable(dw);
        item.popupWindow.setFocusable(false);
        item.popupWindow.setTouchable(true);
        item.popupWindow.setOutsideTouchable(false);
        item.popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        item.popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        item.popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                radioGroup.clearCheck();
            }
        });
    }

    public void clearPopWindow() {
        radioGroup.clearCheck();
    }

    public static abstract class BaseFiltrateItem<T extends PopupWindow> {

        String title;
        T popupWindow;
        MyRadioButton radioButton;
        Context ctx;
        RadioGroup parentGroup;

        public BaseFiltrateItem(Context context, String title) {
            this.ctx = context;
            this.title = title;
            radioButton = new MyRadioButton(ctx);
            radioButton.setText(title);
            radioButton.setTextColor(context.getResources().getColor(R.color.black));
            Drawable arrow = context.getResources().getDrawable(R.mipmap.icon_arrow_down);
            arrow.setBounds(0, 0, arrow.getIntrinsicWidth(), arrow.getIntrinsicHeight());
            radioButton.setCompoundDrawablePadding(DimensUtils.dip2px(context, 7));
            radioButton.setCompoundDrawables(null, null, arrow, null);
            radioButton.setGravity(Gravity.CENTER);
            radioButton.setMaxLines(1);
            radioButton.setTextColor(ctx.getResources().getColor(R.color.black));
            radioButton.setTextSize(13);
            radioButton.setEllipsize(TextUtils.TruncateAt.END);
            radioButton.setButtonDrawable(null);
            radioButton.setChecked(false);
        }

        public final String getTitle() {
            return title;
        }

        public final void setTitle(String title) {
            this.title = title;
            radioButton.setText(title);
            parentGroup.clearCheck();
            setCheck(false, parentGroup);
        }

        public final void setTextSize(float size) {
            radioButton.setTextSize(size);
        }

        public final T getPopupWindow() {
            return popupWindow;
        }

        public final void setPopupWindow(T popupWindow) {
            this.popupWindow = popupWindow;
        }

        void setCheck(boolean isCheck, RadioGroup radioGroup) {
            if (isCheck) {
                radioButton.setTextColor(ctx.getResources().getColor(R.color.colorAccent));
                Drawable arrow = ctx.getResources().getDrawable(R.mipmap.icon_up_arrow);
                arrow.setBounds(0, 0, arrow.getIntrinsicWidth(), arrow.getIntrinsicHeight());
                radioButton.setCompoundDrawables(null, null, arrow, null);
                if (!popupWindow.isShowing()) {
                    popupWindow.showAsDropDown(radioGroup);
                }
            } else {
                radioButton.setTextColor(ctx.getResources().getColor(R.color.black));
                Drawable arrow = ctx.getResources().getDrawable(R.mipmap.icon_arrow_down);
                arrow.setBounds(0, 0, arrow.getIntrinsicWidth(), arrow.getIntrinsicHeight());
                radioButton.setCompoundDrawables(null, null, arrow, null);
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        }
    }

}
