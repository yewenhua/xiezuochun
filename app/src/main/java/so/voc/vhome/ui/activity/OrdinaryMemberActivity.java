package so.voc.vhome.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.umeng.commonsdk.statistics.common.MLog;

import java.util.ArrayList;
import java.util.List;

import so.voc.vhome.R;
import so.voc.vhome.adapter.MemberListAdapter;
import so.voc.vhome.base.BaseActivity;
import so.voc.vhome.databinding.ActivityMemberListBinding;
import so.voc.vhome.model.MemberModel;
import so.voc.vhome.model.ResponseModel;
import so.voc.vhome.net.VHomeApi;
import so.voc.vhome.okgo.HudCallback;
import so.voc.vhome.okgo.StringCallback;
import so.voc.vhome.recycler.DividerDecoration;
import so.voc.vhome.recycler.SingleItemClickListener;
import so.voc.vhome.util.ActivityUtil;
import so.voc.vhome.util.CommonUtils;
import so.voc.vhome.util.Constants;
import so.voc.vhome.util.RxBusUtil;
import so.voc.vhome.util.SharedPreferencesUtils;

/**
 * Fun：普通成员
 *
 * @Author：Linus_Xie
 * @Date：2019/6/26 16:35
 */
public class OrdinaryMemberActivity extends BaseActivity<ActivityMemberListBinding> implements OnRefreshListener {

    private MemberListAdapter mAdapter;
    private List<MemberModel> memberModels;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_list);
    }

    @Override
    protected void initView() {
        setTitle(CommonUtils.getString(R.string.member_list));
        memberModels = new ArrayList<>();
        mAdapter = new MemberListAdapter(this);
        DividerDecoration dividerDecoration = new DividerDecoration(this, DividerDecoration.VERTICAL_LIST);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        bindingView.rvMemberList.setLayoutManager(manager);
        bindingView.rvMemberList.addItemDecoration(dividerDecoration);
        bindingView.rvMemberList.addOnItemTouchListener(new SingleItemClickListener(bindingView.rvMemberList, new SingleItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("memberModel", memberModels.get(position));
//                ActivityUtil.goActivityWithBundle(OrdinaryMemberActivity.this, MemberDetailActivity.class, bundle);
                //TODO 转让对话框
                transferDialog(memberModels.get(position).getId());
            }
        }));
        bindingView.srlMember.setOnRefreshListener(this);
        bindingView.srlMember.autoRefresh();
    }

    private void transferDialog(int id) {
        new MaterialDialog.Builder(this)
                .title(CommonUtils.getString(R.string.tips))
                .content(CommonUtils.getString(R.string.sure_transfer))
                .positiveText(CommonUtils.getString(R.string.confirm))
                .positiveColor(CommonUtils.getColor(R.color.colorRed))
                .negativeText(CommonUtils.getString(R.string.cancel))
                .callback(new MaterialDialog.Callback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        transferOwner(id);
                        dialog.dismiss();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .build()
                .show();
    }

    private void transferOwner(int id) {
        OkGo.<ResponseModel>post(VHomeApi.DEVICE_MEMBER_TRANSFER_OWNER + id)
                .tag(this)
                .params(Constants.ACCESS_TOKEN, String.valueOf(SharedPreferencesUtils.get(Constants.ACCESS_TOKEN, "")))
                .execute(new HudCallback<ResponseModel>(this) {
                    @Override
                    public void onSuccess(Response<ResponseModel> response) {
                        super.onSuccess(response);
                        if (response.body().code == 0){
                            RxBusUtil.getInstance().post(MemberListActivity.TAG, 0);
                            ActivityUtil.goActivityAndFinish(OrdinaryMemberActivity.this, MemberListActivity.class);
                        }
                    }
                });
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        ordinaryMember();
    }

    private void ordinaryMember() {
        OkGo.<ResponseModel<List<MemberModel>>>get(VHomeApi.DEVICE_MEMBER_ORDINARY + String.valueOf(SharedPreferencesUtils.get(Constants.DEVICE_ID, "")))
                .params(Constants.ACCESS_TOKEN, String.valueOf(SharedPreferencesUtils.get(Constants.ACCESS_TOKEN, "")))
                .tag(this)
                .execute(new StringCallback<ResponseModel<List<MemberModel>>>() {
                    @Override
                    public void onSuccess(Response<ResponseModel<List<MemberModel>>> response) {
                        bindingView.srlMember.finishRefresh();
                        super.onSuccess(response);
                        if (response.body().code == 0){
                            memberModels = response.body().data;
                            mAdapter.fillList(memberModels);
                            bindingView.rvMemberList.setAdapter(mAdapter);
                        } else {
                            ToastUtils.show(response.body().msg);
                        }
                    }
                });
    }
}
