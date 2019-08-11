package so.voc.vhome.adapter;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ObjectUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import so.voc.vhome.R;
import so.voc.vhome.base.BaseAdapter;
import so.voc.vhome.base.BaseHolder;
import so.voc.vhome.databinding.ItemNotifyDetailBinding;
import so.voc.vhome.model.NotifyDetailModel;
import so.voc.vhome.model.NotifyModel;
import so.voc.vhome.model.ResponseModel;
import so.voc.vhome.net.VHomeApi;
import so.voc.vhome.okgo.HudCallback;
import so.voc.vhome.util.Constants;
import so.voc.vhome.util.SharedPreferencesUtils;
import so.voc.vhome.util.Util;

/**
 * Fun：分享通知列表适配器
 *
 * @Author：Linus_Xie
 * @Date：2019/6/19 14:31
 */
public class NotifyDetailAdapter extends BaseAdapter<NotifyDetailModel.ContentBean, NotifyDetailHolder> {

    private Context mContext;

    public NotifyDetailAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public int getCustomViewType(int position) {
        return position;
    }

    @Override
    public NotifyDetailHolder createCustomViewHolder(ViewGroup parent, int viewType) {
        NotifyDetailHolder holder = new NotifyDetailHolder(parent, R.layout.item_notify_detail);
        return holder;
    }

    @Override
    public void bindCustomViewHolder(NotifyDetailHolder holder, int position) {
        NotifyDetailModel.ContentBean contentBean = getItem(position);
        holder.binding.setContentBean(contentBean);
        if (ObjectUtils.isNotEmpty(contentBean.getDeviceShareView())) {
            if (!contentBean.getDeviceShareView().isConfirmed() && !contentBean.getDeviceShareView().isRefused()) {//两个都是false，则出现选择框
                holder.binding.llChoice.setVisibility(View.VISIBLE);
                holder.binding.llAgreed.setVisibility(View.GONE);
                holder.binding.llRejected.setVisibility(View.GONE);
            } else if (contentBean.getDeviceShareView().isConfirmed() && !contentBean.getDeviceShareView().isRefused()) {
                holder.binding.llAgreed.setVisibility(View.VISIBLE);
                holder.binding.llChoice.setVisibility(View.GONE);
                holder.binding.llRejected.setVisibility(View.GONE);
            } else if (!contentBean.getDeviceShareView().isConfirmed() && contentBean.getDeviceShareView().isRefused()) {
                holder.binding.llRejected.setVisibility(View.VISIBLE);
                holder.binding.llChoice.setVisibility(View.GONE);
                holder.binding.llAgreed.setVisibility(View.GONE);
            }
            holder.binding.setIsLimit(contentBean.getDeviceShareView().getDeviceMemberDateConstraintType().equals("CONSTRAINT"));
        }

        holder.binding.llAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptShare(getItem(position));
            }
        });
        holder.binding.llRefused.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refusedShare(getItem(position));
            }
        });
    }

    private void acceptShare(NotifyDetailModel.ContentBean contentBean) {
        OkGo.<ResponseModel>post(VHomeApi.DEVICE_SHARE_CONFIRM + contentBean.getDeviceShareView().getId())
                .params(Constants.ACCESS_TOKEN, String.valueOf(SharedPreferencesUtils.get(Constants.ACCESS_TOKEN, "")))
                .tag(this)
                .execute(new HudCallback<ResponseModel>((Activity) mContext) {
                    @Override
                    public void onSuccess(Response<ResponseModel> response) {
                        super.onSuccess(response);
                        if (response.body().code == 0){
                            contentBean.getDeviceShareView().setConfirmed(true);
                            notifyDataSetChanged();
                        }
                    }
                });
    }

    private void refusedShare(NotifyDetailModel.ContentBean contentBean) {
        OkGo.<ResponseModel>post(VHomeApi.DEVICE_SHARE_REFUSE + contentBean.getDeviceShareView().getId())
                .params(Constants.ACCESS_TOKEN, String.valueOf(SharedPreferencesUtils.get(Constants.ACCESS_TOKEN, "")))
                .tag(this)
                .execute(new HudCallback<ResponseModel>((Activity) mContext) {
                    @Override
                    public void onSuccess(Response<ResponseModel> response) {
                        super.onSuccess(response);
                        if (response.body().code == 0){
                            contentBean.getDeviceShareView().setRefused(true);
                            notifyDataSetChanged();
                        }
                    }
                });
    }
}

class NotifyDetailHolder extends BaseHolder{

    protected ItemNotifyDetailBinding binding;

    public NotifyDetailHolder(ViewGroup parent, int resId) {
        super(parent, resId);
        binding = DataBindingUtil.bind(itemView);
    }
}
