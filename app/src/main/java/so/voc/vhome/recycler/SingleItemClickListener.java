package so.voc.vhome.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Fun：
 *
 * @Author：Linus_Xie
 * @Date：2019/5/1 8:53
 */
public class SingleItemClickListener implements RecyclerView.OnItemTouchListener {

    private OnItemClickListener clickListener;
    private GestureDetector gestureDetector;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

//        void onItemLongClick(View view, int position);
    }

    public SingleItemClickListener(final RecyclerView recyclerView,
                                   OnItemClickListener listener) {
        this.clickListener = listener;
        gestureDetector = new GestureDetector(recyclerView.getContext(),
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                        if (childView != null && clickListener != null) {
                            clickListener.onItemClick(childView, recyclerView.getChildAdapterPosition(childView));
                        }
                        return true;
                    }

//                    @Override
//                    public void onLongPress(MotionEvent e) {
//                        View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
//                        if (childView != null && clickListener != null) {
//                            clickListener.onItemLongClick(childView, recyclerView.getChildAdapterPosition(childView));
//                        }
//                    }
                });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        gestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}

