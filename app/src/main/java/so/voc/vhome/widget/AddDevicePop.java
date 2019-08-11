package so.voc.vhome.widget;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;

import so.voc.vhome.R;
import so.voc.vhome.base.BasePopupWindow;

/**
 * Fun：设备添加弹出框
 *
 * @Author：Linus_Xie
 * @Date：2019/6/10 10:21
 */
public class AddDevicePop extends BasePopupWindow implements View.OnClickListener {

    private LinearLayout llNb;
    private LinearLayout llBle;
    private LinearLayout llWifi;
    private LinearLayout llBleWifi;

    public AddDevicePop(View parentView, Activity activity) {
        super(parentView, activity);
    }

    @Override
    public View setPopupView(Activity activity) {
        popupView = View.inflate(activity, R.layout.pop_add_device, null);
        return popupView;
    }

    @Override
    public void initChildView() {
        llNb = popupView.findViewById(R.id.ll_nb);
        llNb.setOnClickListener(this);
        llBle = popupView.findViewById(R.id.ll_ble);
        llBle.setOnClickListener(this);
        llWifi = popupView.findViewById(R.id.ll_wifi);
        llWifi.setOnClickListener(this);
        llBleWifi = popupView.findViewById(R.id.ll_ble_wifi);
        llBleWifi.setOnClickListener(this);
    }

    @Override
    public void OnChildClick(View v) {
        if (onAddDevicePopClickListener == null){
            return;
        }
        switch (v.getId()){
            case R.id.ll_nb:
                onAddDevicePopClickListener.onAddDevicePop(0);
                break;
            case R.id.ll_ble:
                onAddDevicePopClickListener.onAddDevicePop(1);
                break;
            case R.id.ll_wifi:
                onAddDevicePopClickListener.onAddDevicePop(2);
                break;
            case R.id.ll_ble_wifi:
                onAddDevicePopClickListener.onAddDevicePop(3);
                break;
        }
    }

    public interface OnAddDevicePopClickListener {
        void onAddDevicePop(int position);
    }

    private OnAddDevicePopClickListener onAddDevicePopClickListener;

    public void setOnAddDevicePopClickListener(OnAddDevicePopClickListener onAddDevicePopClickListener) {
        this.onAddDevicePopClickListener = onAddDevicePopClickListener;
    }
}
