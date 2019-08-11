package so.voc.vhome.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.ViewGroup;

import so.voc.vhome.R;
import so.voc.vhome.base.BaseAdapter;
import so.voc.vhome.base.BaseHolder;
import so.voc.vhome.databinding.ItemFingerprintManageBinding;
import so.voc.vhome.model.FingerModel;

/**
 * Fun：
 *
 * @Author：Linus_Xie
 * @Date：2019/7/6 13:16
 */
public class FingerprintManageAdapter extends BaseAdapter<FingerModel, FingerprintManageHolder> {


    public FingerprintManageAdapter(Context context) {
        super(context);
    }

    @Override
    public int getCustomViewType(int position) {
        return position;
    }

    @Override
    public FingerprintManageHolder createCustomViewHolder(ViewGroup parent, int viewType) {
        FingerprintManageHolder holder = new FingerprintManageHolder(parent, R.layout.item_fingerprint_manage);
        return holder;
    }

    @Override
    public void bindCustomViewHolder(FingerprintManageHolder holder, int position) {
        FingerModel fingerModel = getItem(position);
        holder.binding.setFingerModel(fingerModel);

        holder.binding.llFinger.setOnClickListener(v -> {
            if (mListener != null){
                mListener.onFingerClick(position);
            }
        });
    }

    public interface OnFingerClickListener{
        void onFingerClick(int position);
    }

    private OnFingerClickListener mListener;

    public void setOnFingerClickListener(OnFingerClickListener listener){
        this.mListener = listener;
    }
}


class FingerprintManageHolder extends BaseHolder{

    protected ItemFingerprintManageBinding binding;

    public FingerprintManageHolder(ViewGroup parent, int resId) {
        super(parent, resId);
        binding = DataBindingUtil.bind(itemView);
    }
}