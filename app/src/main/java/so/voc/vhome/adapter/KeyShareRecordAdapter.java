package so.voc.vhome.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.ViewGroup;

import so.voc.vhome.R;
import so.voc.vhome.base.BaseAdapter;
import so.voc.vhome.base.BaseHolder;
import so.voc.vhome.databinding.ItemKeyShareRecordBinding;
import so.voc.vhome.model.KeyShareRecordModel;

/**
 * Fun：
 *
 * @Author：Linus_Xie
 * @Date：2019/6/17 16:23
 */
public class KeyShareRecordAdapter extends BaseAdapter<KeyShareRecordModel.ContentBean, KeyShareRecordHolder> {

    public KeyShareRecordAdapter(Context context) {
        super(context);
    }

    @Override
    public int getCustomViewType(int position) {
        return position;
    }

    @Override
    public KeyShareRecordHolder createCustomViewHolder(ViewGroup parent, int viewType) {
        KeyShareRecordHolder holder = new KeyShareRecordHolder(parent, R.layout.item_key_share_record);
        return holder;
    }

    @Override
    public void bindCustomViewHolder(KeyShareRecordHolder holder, int position) {
        KeyShareRecordModel.ContentBean contentBean = getItem(position);
        holder.binding.setContentBean(contentBean);
    }
}
class KeyShareRecordHolder extends BaseHolder{

    protected ItemKeyShareRecordBinding binding;

    public KeyShareRecordHolder(ViewGroup parent, int resId) {
        super(parent, resId);
        binding = DataBindingUtil.bind(itemView);
    }
}
