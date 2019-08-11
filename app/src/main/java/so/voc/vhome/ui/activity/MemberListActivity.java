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

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import so.voc.vhome.R;
import so.voc.vhome.adapter.MemberListAdapter;
import so.voc.vhome.base.BaseActivity;
import so.voc.vhome.databinding.ActivityMemberListBinding;
import so.voc.vhome.model.MemberModel;
import so.voc.vhome.model.ResponseModel;
import so.voc.vhome.net.VHomeApi;
import so.voc.vhome.okgo.StringCallback;
import so.voc.vhome.recycler.DividerDecoration;
import so.voc.vhome.recycler.SingleItemClickListener;
import so.voc.vhome.util.ActivityUtil;
import so.voc.vhome.util.CommonUtils;
import so.voc.vhome.util.Constants;
import so.voc.vhome.util.RxBusUtil;
import so.voc.vhome.util.SharedPreferencesUtils;

/**
 * Fun：设备成员管理列表
 *
 * @Author：Linus_Xie
 * @Date：2019/6/10 13:34
 */
public class MemberListActivity extends BaseActivity<ActivityMemberListBinding> implements OnRefreshListener {

    public static final String TAG = MemberListActivity.class.getSimpleName();

    private MemberListAdapter mAdapter;
    private List<MemberModel> memberModels;
    private Observable observable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_list);
    }

    @Override
    protected void initView() {
        setTitle(CommonUtils.getString(R.string.member_manager));
        memberModels = new ArrayList<>();
        mAdapter = new MemberListAdapter(this);
        DividerDecoration dividerDecoration = new DividerDecoration(this, DividerDecoration.VERTICAL_LIST);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        bindingView.rvMemberList.setLayoutManager(manager);
        bindingView.rvMemberList.addItemDecoration(dividerDecoration);
        bindingView.rvMemberList.addOnItemTouchListener(new SingleItemClickListener(bindingView.rvMemberList, new SingleItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (String.valueOf(SharedPreferencesUtils.get(Constants.AUTHORITY_NAME, "")).equals(Constants.OWNER)) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("memberModel", memberModels.get(position));
                    ActivityUtil.goActivityWithBundle(MemberListActivity.this, MemberDetailActivity.class, bundle);
                } else {
                    if (memberModels.get(position).getId() == (Integer) SharedPreferencesUtils.get(Constants.ID, 0)) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("memberModel", memberModels.get(position));
                        ActivityUtil.goActivityWithBundle(MemberListActivity.this, MemberDetailActivity.class, bundle);
                    } else {
                        return;
                    }
                }

            }
        }));
        bindingView.srlMember.setOnRefreshListener(this);
        bindingView.srlMember.autoRefresh();

        observable = RxBusUtil.getInstance().register(TAG, Integer.class);
        observable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                switch (integer) {
                    case 0:
                        bindingView.srlMember.autoRefresh();
                        break;
                }
            }
        });
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getMemberList();
    }

    private void getMemberList() {
        OkGo.<ResponseModel<List<MemberModel>>>get(VHomeApi.DEVICE_MEMBERS + String.valueOf(SharedPreferencesUtils.get(Constants.DEVICE_ID, "")))
                .params(Constants.ACCESS_TOKEN, String.valueOf(SharedPreferencesUtils.get(Constants.ACCESS_TOKEN, "")))
                .tag(this)
                .execute(new StringCallback<ResponseModel<List<MemberModel>>>() {
                    @Override
                    public void onSuccess(Response<ResponseModel<List<MemberModel>>> response) {
                        bindingView.srlMember.finishRefresh();
                        super.onSuccess(response);
                        if (response.body().code == 0) {
                            memberModels = response.body().data;
                            mAdapter.fillList(memberModels);
                            bindingView.rvMemberList.setAdapter(mAdapter);
                        } else {
                            ToastUtils.show(response.body().msg);
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBusUtil.getInstance().unregister(this, observable);
    }
}
