package so.voc.vhome.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.ViewGroup;

import so.voc.vhome.R;
import so.voc.vhome.base.BaseAdapter;
import so.voc.vhome.base.BaseHolder;
import so.voc.vhome.databinding.ItemNotifyBinding;
import so.voc.vhome.model.NotifyModel;
import so.voc.vhome.util.CommonUtils;

/**
 * Fun：消息适配器
 *
 * @Author：Linus_Xie
 * @Date：2019/6/20 15:51
 */
public class NotifyAdapter extends BaseAdapter<NotifyModel, NotifyHolder> {

    public NotifyAdapter(Context context) {
        super(context);
    }

    @Override
    public int getCustomViewType(int position) {
        return position;
    }

    @Override
    public NotifyHolder createCustomViewHolder(ViewGroup parent, int viewType) {
        NotifyHolder holder = new NotifyHolder(parent, R.layout.item_notify);
        return holder;
    }

    @Override
    public void bindCustomViewHolder(NotifyHolder holder, int position) {
        NotifyModel notifyModel = getItem(position);
        holder.binding.setNotifyModel(notifyModel);
        if (notifyModel.getNotificationType().equals("SHARE")){
            holder.binding.ivNotifyType.setBackgroundResource(R.drawable.iv_share_notify);
        } else if (notifyModel.getNotificationType().equals("SYSTEM")){
            holder.binding.ivNotifyType.setBackgroundResource(R.drawable.iv_sys_notify);
        } else {
            holder.binding.ivNotifyType.setBackgroundResource(R.drawable.iv_vhome_notify);
        }
    }
}

class NotifyHolder extends BaseHolder {

    protected ItemNotifyBinding binding;

    public NotifyHolder(ViewGroup parent, int resId) {
        super(parent, resId);
        binding = DataBindingUtil.bind(itemView);
    }
}
