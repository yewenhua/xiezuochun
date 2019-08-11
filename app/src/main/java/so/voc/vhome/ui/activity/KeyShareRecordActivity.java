package so.voc.vhome.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import so.voc.vhome.R;
import so.voc.vhome.adapter.KeyShareRecordAdapter;
import so.voc.vhome.base.BaseActivity;
import so.voc.vhome.databinding.ActivityShareRecordBinding;
import so.voc.vhome.model.DeviceShareRecordModel;
import so.voc.vhome.model.KeyShareRecordModel;
import so.voc.vhome.model.ResponseModel;
import so.voc.vhome.net.VHomeApi;
import so.voc.vhome.okgo.HudCallback;
import so.voc.vhome.okgo.StringCallback;
import so.voc.vhome.recycler.DividerDecoration;
import so.voc.vhome.recycler.SingleItemClickListener;
import so.voc.vhome.util.CommonUtils;
import so.voc.vhome.util.Constants;
import so.voc.vhome.util.SharedPreferencesUtils;

/**
 * Fun：钥匙分享记录
 *
 * @Author：Linus_Xie
 * @Date：2019/6/17 16:10
 */
public class KeyShareRecordActivity extends BaseActivity<ActivityShareRecordBinding> implements OnRefreshListener, OnLoadMoreListener {

    private KeyShareRecordAdapter mAdapter;
    private List<KeyShareRecordModel.ContentBean> contentBeans;
    private int pageNumber = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_record);
    }

    @Override
    protected void initView() {
        setTitle(CommonUtils.getString(R.string.share_key_record));
        contentBeans = new ArrayList<>();
        mAdapter = new KeyShareRecordAdapter(this);
        DividerDecoration dividerDecoration = new DividerDecoration(this, DividerDecoration.VERTICAL_LIST);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        bindingView.rvRecord.setLayoutManager(manager);
        bindingView.rvRecord.addItemDecoration(dividerDecoration);
        bindingView.rvRecord.addOnItemTouchListener(new SingleItemClickListener(bindingView.rvRecord, new SingleItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                delShareRecord(contentBeans.get(position));
            }
        }));
        bindingView.rvRecord.setAdapter(mAdapter);
        bindingView.srlShareRecord.setOnRefreshListener(this);
        bindingView.srlShareRecord.setOnLoadMoreListener(this);
        bindingView.srlShareRecord.autoRefresh();
    }

    private void delShareRecord(KeyShareRecordModel.ContentBean contentBean) {
        OkGo.<ResponseModel>post(VHomeApi.KEY_SHARE_DELETE + contentBean.getId())
                .params(Constants.ACCESS_TOKEN, String.valueOf(SharedPreferencesUtils.get(Constants.ACCESS_TOKEN, "")))
                .tag(this)
                .execute(new HudCallback<ResponseModel>((this)) {
                    @Override
                    public void onSuccess(Response<ResponseModel> response) {
                        super.onSuccess(response);
                        if (response.body().code == 0) {
                            ToastUtils.show(response.body().msg);
                            mAdapter.removeItem(contentBean);
                            contentBeans.remove(contentBean);
                            LogUtils.e(contentBeans.size() + "...." + contentBeans.toString());
                        }
                    }
                });
    }

    private void getShareKey() {
        OkGo.<ResponseModel<KeyShareRecordModel>>get(VHomeApi.KEY_SHARE_SEARCH)
                .params(Constants.ACCESS_TOKEN, String.valueOf(SharedPreferencesUtils.get(Constants.ACCESS_TOKEN, "")))
                .params("pageNumber", pageNumber)
                .params("pageSize", "20")
                .tag(this)
                .execute(new StringCallback<ResponseModel<KeyShareRecordModel>>() {
                    @Override
                    public void onSuccess(Response<ResponseModel<KeyShareRecordModel>> response) {
                        super.onSuccess(response);
                        bindingView.srlShareRecord.finishRefresh();
                        bindingView.srlShareRecord.finishLoadMore();
                        if (response.body().code == 0) {
                            KeyShareRecordModel keyShareRecordModel = response.body().data;
                            List<KeyShareRecordModel.ContentBean> contentBeanList = keyShareRecordModel.getContent();
                            if (pageNumber == 1){
                                contentBeans = contentBeanList;
                                if (contentBeans.size() == 0){
                                    showNoContent(1);
                                } else {
                                    mAdapter.fillList(contentBeanList);
                                }
                            } else {
                                for (KeyShareRecordModel.ContentBean contentBean: contentBeanList) {
                                    contentBeans.add(contentBean);
                                }
                                mAdapter.appendList(contentBeanList);
                            }
                        }
                    }
                });
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        pageNumber = 1;
        getShareKey();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        pageNumber++;
        getShareKey();
    }

    @Override
    protected void onLoading() {
        super.onLoading();
        pageNumber = 1;
        getShareKey();
    }
}
