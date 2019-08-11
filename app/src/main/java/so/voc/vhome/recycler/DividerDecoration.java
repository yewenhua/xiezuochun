package so.voc.vhome.recycler;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import so.voc.vhome.R;


/**
 * Fun：普通的分割线,垂直列表绘制每一行的底部分割线,水平列表绘制每一列的右侧分割线
 *
 * @Author：Linus_Xie
 * @Date：2019/5/1 8:46
 */
public class DividerDecoration extends RecyclerView.ItemDecoration {
    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    Drawable mDivider;
    int mOrientation;

    public DividerDecoration(Context context, int orientation) {
        mDivider = context.getResources().getDrawable(R.drawable.base_divider);
        mOrientation = orientation;
    }

    public DividerDecoration(Context context, int orientation, @DrawableRes int resId) {
        mDivider = context.getResources().getDrawable(resId);
        if (mDivider == null) {
            mDivider = context.getResources().getDrawable(R.drawable.base_divider);
        }
        mOrientation = orientation;
    }

    public DividerDecoration(Context context, int orientation, Drawable drawable) {
        mDivider = drawable == null ? context.getResources().getDrawable(R.drawable.base_divider) : drawable;
        mOrientation = orientation;
    }

    public void setDivider(Drawable drawable){
        if (drawable != null){
            mDivider = drawable;
        }
    }

    public void setOrientation(int orientation){
        mOrientation = orientation;
    }

    public int getOrientation() {
        return mOrientation;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent) {
        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {//最后一行不显示分割线
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin + Math.round(ViewCompat.getTranslationY(child));
            final int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + params.rightMargin + Math.round(ViewCompat.getTranslationX(child));
            final int right = left + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        if (mOrientation == VERTICAL_LIST) {
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        } else {
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
        }
    }
}
