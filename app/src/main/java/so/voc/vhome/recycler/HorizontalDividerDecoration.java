package so.voc.vhome.recycler;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

/**
 * Fun：适用于单列垂直列表的水平分割线,分割线高度取Drawable的height,且不绘制最后一行
 *
 * @Author：Linus_Xie
 * @Date：2019/5/1 8:48
 */
public class HorizontalDividerDecoration extends DividerDecoration {
    public HorizontalDividerDecoration(Context context) {
        super(context, VERTICAL_LIST);
    }

    public HorizontalDividerDecoration(Context context, @DrawableRes int resId) {
        super(context, VERTICAL_LIST, resId);
    }

    public HorizontalDividerDecoration(Context context, @NonNull Drawable divider) {
        super(context, VERTICAL_LIST, divider);
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        int childCount = parent.getAdapter().getItemCount();
        if (itemPosition == (childCount - 1)) {
            outRect.set(0, 0, 0, 0);
        } else {
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        }
    }
}



