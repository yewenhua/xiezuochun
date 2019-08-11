package so.voc.vhome.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.ViewGroup;

import java.util.HashMap;

import so.voc.vhome.R;
import so.voc.vhome.base.BaseAdapter;
import so.voc.vhome.base.BaseHolder;
import so.voc.vhome.databinding.ItemFriendsBinding;
import so.voc.vhome.model.FriendsModel;
import so.voc.vhome.net.VHomeApi;
import so.voc.vhome.util.BindingUtil;
import so.voc.vhome.util.Constants;
import so.voc.vhome.util.SharedPreferencesUtils;

/**
 * Fun：
 *
 * @Author：Linus_Xie
 * @Date：2019/6/26 15:58
 */
public class FriendsAdapter extends BaseAdapter<FriendsModel, FriendsHolder> {

    public FriendsAdapter(Context context) {
        super(context);
    }

    @Override
    public int getCustomViewType(int position) {
        return position;
    }

    @Override
    public FriendsHolder createCustomViewHolder(ViewGroup parent, int viewType) {
        FriendsHolder holder = new FriendsHolder(parent, R.layout.item_friends);
        return holder;
    }

    @Override
    public void bindCustomViewHolder(FriendsHolder holder, int position) {
        FriendsModel friendsModel = getItem(position);
        holder.binding.setFriendsModel(friendsModel);
        BindingUtil.loadImage(holder.binding.civHeadIcon, VHomeApi.GET_HEAD_ICON
                + friendsModel.getHeadIcon() + "?access_token="
                + String.valueOf(SharedPreferencesUtils.get(Constants.ACCESS_TOKEN, "")));
    }
}

class FriendsHolder extends BaseHolder{

    protected ItemFriendsBinding binding;

    public FriendsHolder(ViewGroup parent, int resId) {
        super(parent, resId);
        binding = DataBindingUtil.bind(itemView);
    }
}
