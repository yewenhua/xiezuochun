package so.voc.vhome.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.View;
import android.view.ViewGroup;

import so.voc.vhome.R;
import so.voc.vhome.base.BaseAdapter;
import so.voc.vhome.base.BaseHolder;
import so.voc.vhome.databinding.ItemMemberListBinding;
import so.voc.vhome.model.MemberModel;
import so.voc.vhome.net.VHomeApi;
import so.voc.vhome.util.BindingUtil;
import so.voc.vhome.util.Constants;
import so.voc.vhome.util.SharedPreferencesUtils;
import so.voc.vhome.util.Util;

/**
 * Fun：成员管理适配器
 *
 * @Author：Linus_Xie
 * @Date：2019/6/10 13:44
 */
public class MemberListAdapter extends BaseAdapter<MemberModel, MemberListHolder> {

    public MemberListAdapter(Context context) {
        super(context);
    }

    @Override
    public int getCustomViewType(int position) {
        return position;
    }

    @Override
    public MemberListHolder createCustomViewHolder(ViewGroup parent, int viewType) {
        MemberListHolder holder = new MemberListHolder(parent, R.layout.item_member_list);
        return holder;
    }

    @Override
    public void bindCustomViewHolder(MemberListHolder holder, int position) {
        MemberModel memberModel = getItem(position);
        holder.binding.setMemberModel(memberModel);
        if (!Util.initNullStr(memberModel.getMemberHeadIcon()).isEmpty()){
            BindingUtil.loadImage(holder.binding.civHeadIcon, VHomeApi.GET_HEAD_ICON
                    + memberModel.getMemberHeadIcon() + "?access_token="
                    + String.valueOf(SharedPreferencesUtils.get(Constants.ACCESS_TOKEN, "")));
        }
        if (!memberModel.isVisible()){
            holder.binding.ivArrow.setVisibility(View.GONE);
            if (memberModel.getId() == (Integer) SharedPreferencesUtils.get(Constants.ID, 0)){
                holder.binding.ivArrow.setVisibility(View.VISIBLE);
            }
        }
    }
}

class MemberListHolder extends BaseHolder{

    protected ItemMemberListBinding binding;

    public MemberListHolder(ViewGroup parent, int resId) {
        super(parent, resId);
        binding = DataBindingUtil.bind(itemView);
    }
}
