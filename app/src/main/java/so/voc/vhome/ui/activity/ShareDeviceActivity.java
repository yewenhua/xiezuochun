package so.voc.vhome.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;

import com.blankj.utilcode.util.RegexUtils;
import com.hjq.toast.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import so.voc.vhome.R;
import so.voc.vhome.adapter.FriendsAdapter;
import so.voc.vhome.base.BaseActivity;
import so.voc.vhome.databinding.ActivityShareDeviceBinding;
import so.voc.vhome.model.FriendsModel;
import so.voc.vhome.model.ResponseModel;
import so.voc.vhome.net.VHomeApi;
import so.voc.vhome.okgo.HudCallback;
import so.voc.vhome.recycler.DividerDecoration;
import so.voc.vhome.recycler.SingleItemClickListener;
import so.voc.vhome.util.ActivityUtil;
import so.voc.vhome.util.CommonUtils;
import so.voc.vhome.util.Constants;
import so.voc.vhome.util.SharedPreferencesUtils;

/**
 * Fun：设备共享
 *
 * @Author：Linus_Xie
 * @Date：2019/6/26 15:04
 */
public class ShareDeviceActivity extends BaseActivity<ActivityShareDeviceBinding> {

    private FriendsModel friendsModel;
    private List<FriendsModel> friendsModels;
    private FriendsAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_device);
    }

    @Override
    protected void initView() {
        setTitle(CommonUtils.getString(R.string.share_device));
        mAdapter = new FriendsAdapter(this);
        DividerDecoration dividerDecoration = new DividerDecoration(this, DividerDecoration.VERTICAL_LIST);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        bindingView.rvFriends.setLayoutManager(manager);
        bindingView.rvFriends.addItemDecoration(dividerDecoration);
        bindingView.rvFriends.addOnItemTouchListener(new SingleItemClickListener(bindingView.rvFriends, new SingleItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("friendsModel", friendsModels.get(0));
                ActivityUtil.goActivityWithBundle(ShareDeviceActivity.this, ShareDeviceSettingActivity.class, bundle);
            }
        }));
        bindingView.rvFriends.setAdapter(mAdapter);
    }

    public void shareClick(View view){
        switch (view.getId()){
            case R.id.btn_confirmShare:
                String phoneStr = bindingView.etPhone.getText().toString().trim();
                if (!RegexUtils.isMobileSimple(phoneStr)){
                    ToastUtils.show(CommonUtils.getString(R.string.input_search_phone));
                    break;
                }
                searchFriend(phoneStr);
                break;
            case R.id.btn_shareRecord:
                ActivityUtil.goActivity(this, DeviceShareRecordActivity.class);
                break;
        }
    }

    private void searchFriend(String phoneStr) {
        OkGo.<ResponseModel<FriendsModel>>get(VHomeApi.MEMBER_SIMPLE + phoneStr)
                .params(Constants.ACCESS_TOKEN, String.valueOf(SharedPreferencesUtils.get(Constants.ACCESS_TOKEN, "")))
                .tag(this)
                .execute(new HudCallback<ResponseModel<FriendsModel>>(this) {
                    @Override
                    public void onSuccess(Response<ResponseModel<FriendsModel>> response) {
                        super.onSuccess(response);
                        if (response.body().code == 0){
                            friendsModel = response.body().data;
                            friendsModels = new ArrayList<>();
                            friendsModels.add(friendsModel);
                            mAdapter.fillList(friendsModels);
                            bindingView.rvFriends.setAdapter(mAdapter);
                        }
                    }
                });
    }
}
