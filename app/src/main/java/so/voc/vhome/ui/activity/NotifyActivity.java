package so.voc.vhome.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import so.voc.vhome.R;
import so.voc.vhome.adapter.NotifyAdapter;
import so.voc.vhome.adapter.NotifyDetailAdapter;
import so.voc.vhome.base.BaseActivity;
import so.voc.vhome.databinding.ActivityNotifyBinding;
import so.voc.vhome.model.NotifyModel;
import so.voc.vhome.model.ResponseModel;
import so.voc.vhome.net.VHomeApi;
import so.voc.vhome.okgo.StringCallback;
import so.voc.vhome.recycler.DividerDecoration;
import so.voc.vhome.recycler.SingleItemClickListener;
import so.voc.vhome.util.ActivityUtil;
import so.voc.vhome.util.CommonUtils;
import so.voc.vhome.util.Constants;
import so.voc.vhome.util.SharedPreferencesUtils;

/**
 * Fun：通知消息
 *
 * @Author：Linus_Xie
 * @Date：2019/6/14 18:45
 */
public class NotifyActivity extends BaseActivity<ActivityNotifyBinding> implements OnRefreshListener {

    private NotifyAdapter mAdapter;
    private List<NotifyModel> notifyModelList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);
    }

    @Override
    protected void initView() {
        setTitle(CommonUtils.getString(R.string.notification_message));
        notifyModelList = new ArrayList<>();
        mAdapter = new NotifyAdapter(this);
        DividerDecoration dividerDecoration = new DividerDecoration(this, DividerDecoration.VERTICAL_LIST);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        bindingView.rvNotify.setLayoutManager(manager);
        bindingView.rvNotify.addItemDecoration(dividerDecoration);
        bindingView.rvNotify.addOnItemTouchListener(new SingleItemClickListener(bindingView.rvNotify, new SingleItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                NotifyModel notifyModel = notifyModelList.get(position);
                if (notifyModel.getNotificationType().equals("SHARE")){
                    ActivityUtil.goActivity(NotifyActivity.this, ShareNotifyActivity.class);
                } else if (notifyModel.getNotificationType().equals("SYSTEM")){

                } else {

                }
            }
        }));
        bindingView.srlNotify.setOnRefreshListener(this);
        bindingView.srlNotify.autoRefresh();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindingView.srlNotify.autoRefresh();
    }

    @Override
    protected void onLoading() {
        super.onLoading();
        bindingView.srlNotify.autoRefresh();
    }

    private void getNotify() {
        OkGo.<ResponseModel<List<NotifyModel>>>get(VHomeApi.NOTIFICATION_STATISTICS)
                .params(Constants.ACCESS_TOKEN, String.valueOf(SharedPreferencesUtils.get(Constants.ACCESS_TOKEN, "")))
                .execute(new StringCallback<ResponseModel<List<NotifyModel>>>() {
                    @Override
                    public void onSuccess(Response<ResponseModel<List<NotifyModel>>> response) {
                        super.onSuccess(response);
                        if (response.body().code == 0){
                            bindingView.srlNotify.finishRefresh();
                            List<NotifyModel> notifyModels = response.body().data;
                            notifyModelList = new ArrayList<>();
                            for (NotifyModel notifyModel : notifyModels){
                                if (!notifyModel.getMessage().isEmpty()){
                                    notifyModelList.add(notifyModel);
                                }
                            }
                            if (notifyModelList.size() > 0){
                                mAdapter.fillList(notifyModelList);
                                bindingView.rvNotify.setAdapter(mAdapter);
                            } else {
                                showNoContent(4);
                            }
                        }
                    }
                });
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getNotify();
    }
}
