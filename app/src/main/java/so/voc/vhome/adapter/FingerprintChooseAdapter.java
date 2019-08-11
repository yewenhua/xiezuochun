package so.voc.vhome.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.ViewGroup;

import so.voc.vhome.R;
import so.voc.vhome.base.BaseAdapter;
import so.voc.vhome.base.BaseHolder;
import so.voc.vhome.databinding.ItemFingerprintChooseBinding;
import so.voc.vhome.model.FingerMemberModel;
import so.voc.vhome.model.MemberModel;

/**
 * Fun：指纹用户关联适配器
 *
 * @Author：Linus_Xie
 * @Date：2019/6/10 15:18
 */
public class FingerprintChooseAdapter extends BaseAdapter<FingerMemberModel, FingerprintChooseHolder> {

    public FingerprintChooseAdapter(Context context) {
        super(context);
    }

    @Override
    public int getCustomViewType(int position) {
        return position;
    }

    @Override
    public FingerprintChooseHolder createCustomViewHolder(ViewGroup parent, int viewType) {
        FingerprintChooseHolder holder = new FingerprintChooseHolder(parent, R.layout.item_fingerprint_choose);
        return holder;
    }

    @Override
    public void bindCustomViewHolder(FingerprintChooseHolder holder, int position) {
        FingerMemberModel memberModel = getItem(position);
        holder.binding.setMemberModel(memberModel);
    }
}

class FingerprintChooseHolder extends BaseHolder{

    protected ItemFingerprintChooseBinding binding;

    public FingerprintChooseHolder(ViewGroup parent, int resId) {
        super(parent, resId);
        binding = DataBindingUtil.bind(itemView);
    }
}
