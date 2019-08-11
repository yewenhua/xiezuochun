package so.voc.vhome.adapter;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.View;
import android.view.ViewGroup;

import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import so.voc.vhome.R;
import so.voc.vhome.base.BaseAdapter;
import so.voc.vhome.base.BaseHolder;
import so.voc.vhome.databinding.ItemDeviceShareRecordBinding;
import so.voc.vhome.model.DeviceShareRecordModel;

/**
 * Fun：设备分享记录适配器
 *
 * @Author：Linus_Xie
 * @Date：2019/6/5 14:08
 */
public class DeviceShareRecordAdapter extends BaseAdapter<DeviceShareRecordModel.ContentBean, DeviceShareRecordHolder> {

    private Context mContext;

    public DeviceShareRecordAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public int getCustomViewType(int position) {
        return position;
    }

    @Override
    public DeviceShareRecordHolder createCustomViewHolder(ViewGroup parent, int viewType) {
        DeviceShareRecordHolder holder = new DeviceShareRecordHolder(parent, R.layout.item_device_share_record);
        return holder;
    }

    @Override
    public void bindCustomViewHolder(DeviceShareRecordHolder holder, int position) {
        DeviceShareRecordModel.ContentBean contentBean = getItem(position);
        holder.binding.setContentBean(contentBean);
    }
}

class DeviceShareRecordHolder extends BaseHolder{

    protected ItemDeviceShareRecordBinding binding;

    public DeviceShareRecordHolder(ViewGroup parent, int resId) {
        super(parent, resId);
        binding = DataBindingUtil.bind(itemView);
    }
}
