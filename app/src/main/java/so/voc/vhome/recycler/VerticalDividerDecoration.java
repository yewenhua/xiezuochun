package so.voc.vhome.recycler;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

/**
 * Fun：适用于单行水平列表的水平分割线,分割线高度取Drawable的width
 *
 * @Author：Linus_Xie
 * @Date：2019/5/1 8:53
 */
public class VerticalDividerDecoration extends DividerDecoration {

    public VerticalDividerDecoration(Context context) {
        super(context, HORIZONTAL_LIST);
    }

    public VerticalDividerDecoration(Context context, @DrawableRes int resId) {
        super(context, HORIZONTAL_LIST, resId);
    }

    public VerticalDividerDecoration(Context context, @NonNull Drawable drawable) {
        super(context, HORIZONTAL_LIST, drawable);
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        int childCount = parent.getAdapter().getItemCount();
        if (itemPosition == childCount - 1) {
            outRect.set(0, 0, 0, 0);
        } else {
            outRect.set(0, 0, mDivider.getIntrinsicHeight(), 0);
        }
    }
}
