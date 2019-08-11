package so.voc.vhome.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clj.fastble.BleManager;
import com.clj.fastble.data.BleDevice;

import java.util.ArrayList;
import java.util.List;

import so.voc.vhome.R;

/**
 * Fun：蓝牙列表适配器
 *
 * @Author：Linus_Xie
 * @Date：2019/6/11 11:36
 */
public class BleAdapter extends BaseAdapter {

    private Context mContext;
    private List<BleDevice> bleDevices;

    public BleAdapter(Context mContext) {
        this.mContext = mContext;
        bleDevices = new ArrayList<>();
    }

    public void addDevice(BleDevice bleDevice){
        removeDevice(bleDevice);
        bleDevices.add(bleDevice);
    }

    public void removeDevice(BleDevice bleDevice) {
        for (int i = 0; i < bleDevices.size(); i++) {
            BleDevice device = bleDevices.get(i);
            if (bleDevice.getKey().equals(device.getKey())) {
                bleDevices.remove(i);
            }
        }
    }

    public void clearConnectedDevice() {
        for (int i = 0; i < bleDevices.size(); i++) {
            BleDevice device = bleDevices.get(i);
            if (BleManager.getInstance().isConnected(device)) {
                bleDevices.remove(i);
            }
        }
    }

    public void clearScanDevice() {
        for (int i = 0; i < bleDevices.size(); i++) {
            BleDevice device = bleDevices.get(i);
            if (!BleManager.getInstance().isConnected(device)) {
                bleDevices.remove(i);
            }
        }
    }

    public void clear() {
        clearConnectedDevice();
        clearScanDevice();
    }

    @Override
    public int getCount() {
        return bleDevices.size();
    }

    @Override
    public BleDevice getItem(int position) {
        if (position > bleDevices.size()) {
            return null;
        }
        return bleDevices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       ViewHolder viewHolder;
       if (convertView != null){
           viewHolder = (ViewHolder) convertView.getTag();
       } else {
           convertView = View.inflate(mContext, R.layout.item_ble, null);
           viewHolder = new ViewHolder();
           convertView.setTag(viewHolder);
           viewHolder.tvBleMac = convertView.findViewById(R.id.tv_bleMac);
           viewHolder.tvBleName = convertView.findViewById(R.id.tv_bleName);
           viewHolder.llConnect = convertView.findViewById(R.id.ll_connect);
       }
       final BleDevice bleDevice = getItem(position);
       if (bleDevice != null){
           viewHolder.tvBleName.setText(bleDevice.getName());
           viewHolder.tvBleMac.setText(bleDevice.getMac());
       }
       viewHolder.llConnect.setOnClickListener(v -> {
           if (mListener != null){
               mListener.onConnect(bleDevice);
           }
       });
        return convertView;
    }

    class ViewHolder {
        TextView tvBleName;
        TextView tvBleMac;
        LinearLayout llConnect;
    }

    public interface OnDeviceClickListener {
        void onConnect(BleDevice bleDevice);

//        void onDisConnect(BleDevice bleDevice);
//
//        void onDetail(BleDevice bleDevice);
    }

    private OnDeviceClickListener mListener;

    public void setOnDeviceClickListener(OnDeviceClickListener listener) {
        this.mListener = listener;
    }
}
