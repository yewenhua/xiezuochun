package so.voc.vhome.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import so.voc.vhome.R;

/**
 * Fun：
 *
 * @Author：Linus_Xie
 * @Date：2019/6/10 9:43
 */
public abstract class BasePopupWindow<T> extends PopupWindow implements PopupWindow.OnDismissListener, View.OnClickListener {

    public static final String TAG = BasePopupWindow.class.getSimpleName();

    protected Activity activity;
    protected View popupView;
    protected BasePopupWindow basePopupWindow;
    protected View parentView;
    protected T data;

    public BasePopupWindow(View parentView, Activity activity, T data){
        this.activity = activity;
        this.parentView = parentView;
        this.data = data;
        popupView = setPopupView(activity);
        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        initChildView();
        this.setContentView(popupView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setOnDismissListener(this);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setAnimationStyle(R.style.bottomSheetAnim);
    }

    @SuppressLint("WrongConstant")
    public BasePopupWindow(View parentView, Activity activity) {
        this.activity = activity;
        this.parentView = parentView;
        popupView = setPopupView(activity);
        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        initChildView();
        this.setContentView(popupView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setOnDismissListener(this);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
                    return true;
                }
                return false;
            }
        });
        this.setAnimationStyle(R.style.bottomSheetAnim);
    }

    public abstract View setPopupView(Activity activity);

    public void showPopupWindow() {
        if (!this.isShowing()) {
            setAlpha(activity, 0.6f);
            this.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
        } else {
            this.dismiss();
        }
    }

    public void showPopupWindowDownOffset() {
        if (!this.isShowing()) {
            setAlpha(activity, 0.6f);
            this.showAsDropDown(parentView, (int) (-parentView.getWidth() * 1.3f), 0);
        }
    }

    public void showPopupWindowDown() {
        if (!this.isShowing()) {
            setAlpha(activity, 0.6f);
            this.showAsDropDown(parentView);
        }
    }

    public void showPopupWindowCenter() {
        int[] location = new int[2];
        parentView.getLocationOnScreen(location);
        Log.i(TAG, "location[0]: " + location[0] + "location[1]: " + location[1]);
        if (!this.isShowing()) {
            int popHeight = this.getContentView().getMeasuredHeight();
            int popWeight = this.getContentView().getMeasuredHeight();
            Log.i(TAG, "popHeight" + popHeight);
            Log.i(TAG, "popWeight" + popWeight);
            this.showAtLocation(parentView, Gravity.NO_GRAVITY, location[0] - popWeight + (int) (1f * parentView.getWidth()), location[1] - popHeight + (int) (1.5f * parentView.getHeight()));
        }
    }

    public void closePopupWindow(Activity activity) {
        if (this.isShowing()) {
            this.dismiss();
            setAlpha(activity, 1f);
        }

    }

    private void setAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        activity.getWindow().setAttributes(lp);
    }

    @Override
    public void onDismiss() {
        dismiss();
        setAlpha(activity, 1f);
    }

    @Override
    public void onClick(View v) {
        OnChildClick(v);
    }

    public abstract void initChildView();

    public abstract void OnChildClick(View v);
}
