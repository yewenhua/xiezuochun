package so.voc.vhome.widget;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;

import so.voc.vhome.R;
import so.voc.vhome.base.BasePopupWindow;

/**
 * Fun：共享设备弹出框
 *
 * @Author：Linus_Xie
 * @Date：2019/6/12 17:33
 */
public class ShareDevicePop extends BasePopupWindow implements View.OnClickListener {

    private LinearLayout llShareDevice;
    private LinearLayout llShareRecord;

    public ShareDevicePop(View parentView, Activity activity) {
        super(parentView, activity);
    }

    @Override
    public View setPopupView(Activity activity) {
        popupView = View.inflate(activity, R.layout.pop_share_device, null);
        return popupView;
    }

    @Override
    public void initChildView() {
        llShareDevice = popupView.findViewById(R.id.ll_shareDevice);
        llShareDevice.setOnClickListener(this);
        llShareRecord = popupView.findViewById(R.id.ll_shareRecord);
        llShareRecord.setOnClickListener(this);
    }

    @Override
    public void OnChildClick(View v) {
        if (onShareDevicePopClickListener == null){
            return;
        }
        switch (v.getId()){
            case R.id.ll_shareDevice:
                onShareDevicePopClickListener.onShareDevicePop(0);
                break;
            case R.id.ll_shareRecord:
                onShareDevicePopClickListener.onShareDevicePop(1);
                break;
        }
    }

    public interface OnShareDevicePopClickListener{
        void onShareDevicePop(int position);
    }

    private OnShareDevicePopClickListener onShareDevicePopClickListener;

    public void OnShareDevicePopClickListener(OnShareDevicePopClickListener onShareDevicePopClickListener){
        this.onShareDevicePopClickListener = onShareDevicePopClickListener;
    }
}
