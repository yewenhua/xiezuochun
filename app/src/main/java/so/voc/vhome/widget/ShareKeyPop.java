package so.voc.vhome.widget;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;

import so.voc.vhome.R;
import so.voc.vhome.base.BasePopupWindow;

/**
 * Fun：
 *
 * @Author：Linus_Xie
 * @Date：2019/6/17 11:04
 */
public class ShareKeyPop extends BasePopupWindow implements View.OnClickListener{

    private LinearLayout llShareKey;
    private LinearLayout llShareRecord;

    public ShareKeyPop(View parentView, Activity activity) {
        super(parentView, activity);
    }

    @Override
    public View setPopupView(Activity activity) {
        popupView = View.inflate(activity, R.layout.pop_share_key, null);
        return popupView;
    }

    @Override
    public void initChildView() {
        llShareKey = popupView.findViewById(R.id.ll_shareKey);
        llShareKey.setOnClickListener(this);
        llShareRecord = popupView.findViewById(R.id.ll_shareRecord);
        llShareRecord.setOnClickListener(this);
    }

    @Override
    public void OnChildClick(View v) {
        if (onShareKeyPopClickListener == null){
            return;
        }
        switch (v.getId()){
            case R.id.ll_shareKey:
                onShareKeyPopClickListener.onShareKeyPop(0);
                break;
            case R.id.ll_shareRecord:
                onShareKeyPopClickListener.onShareKeyPop(1);
                break;
        }
    }

    public interface OnShareKeyPopClickListener{
        void onShareKeyPop(int position);
    }

    private OnShareKeyPopClickListener onShareKeyPopClickListener;

    public void OnShareKeyPopClickListener(OnShareKeyPopClickListener onShareKeyPopClickListener){
        this.onShareKeyPopClickListener = onShareKeyPopClickListener;
    }
}
