package so.voc.vhome.ui.activity;

import android.app.ListActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.ArrayList;
import java.util.List;

import so.voc.vhome.R;
import so.voc.vhome.adapter.NotifyDetailAdapter;
import so.voc.vhome.base.BaseActivity;
import so.voc.vhome.databinding.ActivityNotifyDetailBinding;
import so.voc.vhome.model.NotifyDetailModel;
import so.voc.vhome.model.ResponseModel;
import so.voc.vhome.net.VHomeApi;
import so.voc.vhome.okgo.HudCallback;
import so.voc.vhome.okgo.StringCallback;
import so.voc.vhome.recycler.DividerDecoration;
import so.voc.vhome.util.CommonUtils;
import so.voc.vhome.util.Constants;
import so.voc.vhome.util.SharedPreferencesUtils;

/**
 * Fun：
 *
 * @Author：Linus_Xie
 * @Date：2019/6/19 14:14V
 */
public class ShareNotifyActivity extends BaseActivity<ActivityNotifyDetailBinding> implements OnRefreshListener, OnLoadMoreListener, SwipeMenuCreator, OnItemMenuClickListener {

    private int pageNumber = 1;
    private List<NotifyDetailModel.ContentBean> contentBeans;
    private NotifyDetailAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_detail);
    }

    @Override
    protected void initView() {
        setTitle(CommonUtils.getString(R.string.share_list));
        contentBeans = new ArrayList<>();
        mAdapter = new NotifyDetailAdapter(this);
        DividerDecoration dividerDecoration = new DividerDecoration(this, DividerDecoration.VERTICAL_LIST);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        bindingView.rvNotify.setLayoutManager(manager);
        bindingView.rvNotify.addItemDecoration(dividerDecoration);
        bindingView.rvNotify.setAdapter(mAdapter);
        bindingView.srlNotify.setOnRefreshListener(this);
        bindingView.srlNotify.setOnLoadMoreListener(this);
        bindingView.srlNotify.autoRefresh();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        pageNumber++;
        getShareNotify();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        pageNumber = 1;
        getShareNotify();

    }

    private void getShareNotify() {
        OkGo.<ResponseModel<NotifyDetailModel>>get(VHomeApi.NOTIFICATION_PAGE)
                .params(Constants.ACCESS_TOKEN, String.valueOf(SharedPreferencesUtils.get(Constants.ACCESS_TOKEN, "")))
                .params("pageNumber", pageNumber)
                .params("notificationType", "SHARE")
                .execute(new StringCallback<ResponseModel<NotifyDetailModel>>() {
                    @Override
                    public void onSuccess(Response<ResponseModel<NotifyDetailModel>> response) {
                        super.onSuccess(response);
                        bindingView.srlNotify.finishLoadMore();
                        bindingView.srlNotify.finishRefresh();
                        if (response.body().code == 0){
                            NotifyDetailModel NotifyDetailModel = response.body().data;
                            List<NotifyDetailModel.ContentBean> contentBeanList = NotifyDetailModel.getContent();
                            if (pageNumber == 1){
                                contentBeans = contentBeanList;
                                mAdapter.fillList(contentBeanList);
                            } else {
                                for (NotifyDetailModel.ContentBean contentBean : contentBeanList){
                                    contentBeans.add(contentBean);
                                }
                                mAdapter.appendList(contentBeanList);
                            }
                        }
                    }
                });
    }

    @Override
    public void onItemClick(SwipeMenuBridge menuBridge, int adapterPosition) {
        int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
        int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。

        if (direction == SwipeRecyclerView.RIGHT_DIRECTION) {
            notificationDel(contentBeans.get(adapterPosition));
        }
    }

    /**
     * 删除信息
     * @param contentBean
     */
    private void notificationDel(NotifyDetailModel.ContentBean contentBean) {
        OkGo.<ResponseModel>post(VHomeApi.NOTIFICATION_DELETE + contentBean.getId())
                .params(Constants.ACCESS_TOKEN, String.valueOf(SharedPreferencesUtils.get(Constants.ACCESS_TOKEN, "")))
                .isSpliceUrl(true)
                .tag(this)
                .execute(new HudCallback<ResponseModel>(this) {
                    @Override
                    public void onSuccess(Response<ResponseModel> response) {
                        super.onSuccess(response);
                        if (response.body().code == 0){
                            contentBeans.remove(contentBean);
                            mAdapter.removeItem(contentBean);
                        }
                    }
                });
    }

    @Override
    public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int position) {
        int width = getResources().getDimensionPixelSize(R.dimen.dp_70);
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        // 添加右侧的，如果不添加，则右侧不会出现菜单。
        {
            SwipeMenuItem deleteItem = new SwipeMenuItem(ShareNotifyActivity.this).setBackground(R.color.colorRed)
                    .setText("删除")
                    .setTextColor(Color.WHITE)
                    .setWidth(width)
                    .setHeight(height);
            rightMenu.addMenuItem(deleteItem);// 添加菜单到右侧。
        }
    }
}
