package so.voc.vhome.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.ViewGroup;

import so.voc.vhome.R;
import so.voc.vhome.base.BaseAdapter;
import so.voc.vhome.base.BaseHolder;
import so.voc.vhome.databinding.ItemDeviceBinding;
import so.voc.vhome.model.DeviceModel;
import so.voc.vhome.util.Constants;
import so.voc.vhome.util.SharedPreferencesUtils;

/**
 * Fun：设备列表适配器
 *
 * @Author：Linus_Xie
 * @Date：2019/6/4 13:25
 */
public class DeviceAdapter extends BaseAdapter<DeviceModel, DeviceHolder> {

    public DeviceAdapter(Context context) {
        super(context);
    }

    @Override
    public int getCustomViewType(int position) {
        return position;
    }

    @Override
    public DeviceHolder createCustomViewHolder(ViewGroup parent, int viewType) {
        DeviceHolder holder = new DeviceHolder(parent, R.layout.item_device);
        return holder;
    }

    @Override
    public void bindCustomViewHolder(DeviceHolder holder, int position) {
        DeviceModel deviceModel = getItem(position);
        if (deviceModel.getDeviceId().equals(String.valueOf(SharedPreferencesUtils.get(Constants.DEVICE_ID, "")))){
            holder.binding.setIsUsed(true);
        } else {
            holder.binding.setIsUsed(false);
        }
        holder.binding.setDeviceModel(deviceModel);
    }
}

class DeviceHolder extends BaseHolder{

    protected ItemDeviceBinding binding;

    public DeviceHolder(ViewGroup parent, int resId) {
        super(parent, resId);
        binding = DataBindingUtil.bind(itemView);
    }
}
