package com.ys.pulldownfiltrate.PullDownFiltrate;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import com.ys.pulldownfiltrate.R;

/**
 * 默认的筛选器popwindow
 */
public class BaseQuickPullDownFIlrateItem extends PullDownFiltrateView.BaseFiltrateItem<BaseQuickPullDownFIlrateItem.BaseFiltratePopupWindow> {

    BaseFiltrateAdapter filtrateAdapter;
    onItemSelectListener onItemSelectListener;

    /**
     * @param context
     * @param title   筛选器选择的title
     */
    public BaseQuickPullDownFIlrateItem(Context context, String title, BaseFiltrateAdapter adapter) {
        super(context, title);
        this.filtrateAdapter = adapter;
        init();
    }

    private void init() {
        popupWindow = new BaseFiltratePopupWindow(ctx);
        filtrateAdapter.setOnItemClickListener(new BaseFiltrateAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                filtrateAdapter.lastPosition = position;
                setTitle(filtrateAdapter.getTitle(position));
                if (onItemSelectListener != null) {
                    onItemSelectListener.onItemSelect(position);
                }
                filtrateAdapter.notifyDataSetChanged();
            }
        });
        popupWindow.setAdapter(filtrateAdapter);
    }

    /**
     * popupwindow中选项被选中的回调
     * @param onItemSelectListener
     */
    public void setOnItemSelectListener(BaseQuickPullDownFIlrateItem.onItemSelectListener onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
    }

    /**
     * 以MaxHeightRecyclerView为内容的popupwindow
     */
    public static class BaseFiltratePopupWindow extends PopupWindow {
        View view;
        MaxHeightRecyclerView recyclerView;
        private Context context;

        public BaseFiltratePopupWindow(Context context) {
            this.context = context;
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.view_base_filtrate_popwindow, null);
            this.setContentView(view);
            this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            recyclerView = view.findViewById(R.id.recyclerview);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            setTouchInterceptor(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        int height = recyclerView.getHeight();
                        int y = (int) event.getY();
                        if (y > height) {
                            if (isShowing()) {
                                dismiss();
                                return true;
                            }
                        }
                    }
                    return false;
                }
            });
        }

        /**
         * 为recyclerview设置adapter
         * @param adapter
         */
        public void setAdapter(BaseFiltrateAdapter adapter) {
            if (adapter != null) {
                recyclerView.setAdapter(adapter);
            }
        }

        @Override
        public void showAsDropDown(View anchor) {
            if (Build.VERSION.SDK_INT >= 24) {
                Rect rect = new Rect();
                anchor.getGlobalVisibleRect(rect);
                int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
                setHeight(h + (DimensUtils.navigationGestureEnabled(context) ? DimensUtils.getStatusHeight(context) : 0));
            }
            super.showAsDropDown(anchor);
        }


    }

    /**
     * 封装了点击事件的adapter
     * @param <K>
     */
    public static abstract class BaseFiltrateAdapter<K extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<K> {

        protected int lastPosition = -1;
        private OnItemClickListener onItemClickListener;

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public void onBindViewHolder(@NonNull K k, int i) {
            final int position = i;
            if (i == lastPosition) {
                setSelect(k, true);
            } else {
                setSelect(k, false);
            }
            k.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onClick(position);
                    }
                }
            });
        }

        public abstract String getTitle(int position);

        public abstract void setSelect(K helper, boolean isSelect);

        protected interface OnItemClickListener{
            void onClick(int position);
        }

    }



    public interface onItemSelectListener {
        void onItemSelect(int position);
    }

}
