package com.zhiyicx.thinksnsplus.modules.dynamic.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.widget.pictureviewer.PictureViewer;
import com.zhiyicx.baseproject.widget.pictureviewer.core.ImageInfo;
import com.zhiyicx.baseproject.widget.pictureviewer.core.ParcelableSparseArray;
import com.zhiyicx.baseproject.widget.pictureviewer.core.PhotoView;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicToolBean;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailActivity;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListBaseItem;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForEightImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForFiveImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForFourImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForNineImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForOneImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForSevenImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForSixImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForThreeImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForTwoImage;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_DETAIL_DATA;

/**
 * @Describe 动态列表
 * @Author Jungle68
 * @Date 2017/1/17
 * @Contact master.jungle68@gmail.com
 */
public class DynamicFragment extends TSListFragment<DynamicContract.Presenter, DynamicBean> implements DynamicContract.View, DynamicListBaseItem.OnReSendClickListener, DynamicListBaseItem.OnMenuItemClickLisitener, DynamicListBaseItem.OnImageClickListener, DynamicListBaseItem.OnUserInfoClickListener, MultiItemTypeAdapter.OnItemClickListener {
    private static final String BUNDLE_DYNAMIC_TYPE = "dynamic_type";
    public static final long ITEM_SPACING = 5L; // 单位dp
    @Inject
    DynamicPresenter mDynamicPresenter;  // 仅用于构造

    private String mDynamicType = ApiConfig.DYNAMIC_TYPE_NEW;


    private List<DynamicBean> mDynamicBeens = new ArrayList<>();
    private PictureViewer pictureViewer;

    public static DynamicFragment newInstance(String dynamicType) {
        DynamicFragment fragment = new DynamicFragment();
        Bundle args = new Bundle();
        args.putString(BUNDLE_DYNAMIC_TYPE, dynamicType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected void initView(View rootView) {
        pictureViewer = (PictureViewer) rootView.findViewById(R.id.picture_view);
        super.initView(rootView);
    }

    @Override
    protected float getItemDecorationSpacing() {
        return ITEM_SPACING;
    }

    @Override
    protected boolean isLoadingMoreEnable() {
        return true;
    }

    @Override
    protected boolean getPullDownRefreshEnable() {
        return true;
    }

    @Override
    protected MultiItemTypeAdapter getAdapter() {
        MultiItemTypeAdapter adapter = new MultiItemTypeAdapter(getContext(), mDynamicBeens);
        setAdapter(adapter, new DynamicListBaseItem(getContext()));
        setAdapter(adapter, new DynamicListItemForOneImage(getContext()));
        setAdapter(adapter, new DynamicListItemForTwoImage(getContext()));
        setAdapter(adapter, new DynamicListItemForThreeImage(getContext()));
        setAdapter(adapter, new DynamicListItemForFourImage(getContext()));
        setAdapter(adapter, new DynamicListItemForFiveImage(getContext()));
        setAdapter(adapter, new DynamicListItemForSixImage(getContext()));
        setAdapter(adapter, new DynamicListItemForSevenImage(getContext()));
        setAdapter(adapter, new DynamicListItemForEightImage(getContext()));
        setAdapter(adapter, new DynamicListItemForNineImage(getContext()));

        adapter.setOnItemClickListener(this);
        return adapter;
    }

    private void setAdapter(MultiItemTypeAdapter adapter, DynamicListBaseItem dynamicListBaseItem) {
        dynamicListBaseItem.setOnImageClickListener(this);
        dynamicListBaseItem.setOnUserInfoClickListener(this);
        dynamicListBaseItem.setOnMenuItemClickLisitener(this);
        dynamicListBaseItem.setOnReSendClickListener(this);
        adapter.addItemViewDelegate(dynamicListBaseItem);
    }


    @Override
    protected void initData() {
        DaggerDynamicComponent // 在 super.initData();之前，因为initdata 会使用到 presenter
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .dynamicPresenterModule(new DynamicPresenterModule(this))
                .build().inject(this);
        mDynamicType = getArguments().getString(BUNDLE_DYNAMIC_TYPE);

        super.initData();
    }

    @Override
    public void setPresenter(DynamicContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {
        mRefreshlayout.setRefreshing(false);
        mRefreshlayout.setLoadingMore(false);
    }

    @Override
    public void showMessage(String message) {
        ToastUtils.showToast(message);
    }

    /**
     * scan imags
     *
     * @param dynamicBean
     * @param position
     */
    @Override
    public void onImageClick(ViewHolder holder, DynamicBean dynamicBean, int position) {
        List<ImageBean> imageBeanList = dynamicBean.getFeed().getStorages();
        ParcelableSparseArray<ImageInfo> imageInfoParcelableSparseArray = new ParcelableSparseArray<>();
        switch (dynamicBean.getFeed().getStorages().size()) {
            case 9:
                imageInfoParcelableSparseArray.put(position, ((PhotoView) holder.getView(R.id.siv_8)).getInfo());
            case 8:
                imageInfoParcelableSparseArray.put(position, ((PhotoView) holder.getView(R.id.siv_7)).getInfo());
            case 7:
                imageInfoParcelableSparseArray.put(position, ((PhotoView) holder.getView(R.id.siv_6)).getInfo());
            case 6:
                imageInfoParcelableSparseArray.put(position, ((PhotoView) holder.getView(R.id.siv_5)).getInfo());
            case 5:
                imageInfoParcelableSparseArray.put(position, ((PhotoView) holder.getView(R.id.siv_4)).getInfo());
            case 4:
                imageInfoParcelableSparseArray.put(position, ((PhotoView) holder.getView(R.id.siv_3)).getInfo());
            case 3:
                imageInfoParcelableSparseArray.put(position, ((PhotoView) holder.getView(R.id.siv_2)).getInfo());
            case 2:
                imageInfoParcelableSparseArray.put(position, ((PhotoView) holder.getView(R.id.siv_1)).getInfo());
            case 1:
                imageInfoParcelableSparseArray.put(position, ((PhotoView) holder.getView(R.id.siv_0)).getInfo());
                break;
            default:

        }
        pictureViewer.setData(imageBeanList, imageInfoParcelableSparseArray);
        pictureViewer.show(position);
    }

    /**
     * scan user Info
     *
     * @param dynamicBean
     */
    @Override
    public void onUserInfoClick(DynamicBean dynamicBean) {
        showMessage(dynamicBean.getUserInfoBean().getName());
    }

    @Override
    public String getDynamicType() {
        return mDynamicType;
    }

    @Override
    public List<DynamicBean> getDatas() {
        return mDynamicBeens;
    }

    @Override
    public void refresh() {
        refreshData();
    }

    @Override
    public void refresh(int position) {
        LogUtils.d(TAG, "mDynamicBeens    position  = " + mDynamicBeens.toString());
        refreshData(position);
    }

    /**
     * resend click
     *
     * @param position
     */
    @Override
    public void onReSendClick(int position) {
        mDynamicBeens.get(position).setState(DynamicBean.SEND_ING);
        refresh();
        mPresenter.reSendDynamic(position);
    }


    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        Intent intent = new Intent(getActivity(), DynamicDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(DYNAMIC_DETAIL_DATA, mAdapter.getItem(position));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        return false;
    }

    @Override
    public void onMenuItemClick(View view, int dataPosition, int viewPosition) {
        switch (viewPosition) { // 0 1 2 3 代表 view item 位置
            case 0: // 喜欢
                handleLike(dataPosition);
                break;

            case 1:
                onItemClick(null, null, dataPosition);
                break;

            case 2:
                onItemClick(null, null, dataPosition);
                break;

            case 3: // 更多
                showMessage("点击了跟多");

                break;
            default:
                onItemClick(null, null, dataPosition);
        }

    }

    private void handleLike(int dataPosition) {
        // 先更新界面，再后台处理
        mDynamicBeens.get(dataPosition).getTool().setIs_digg_feed(mDynamicBeens.get(dataPosition).getTool().getIs_digg_feed() == DynamicToolBean.STATUS_DIGG_FEED_UNCHECKED ? DynamicToolBean.STATUS_DIGG_FEED_CHECKED : DynamicToolBean.STATUS_DIGG_FEED_UNCHECKED);
        mDynamicBeens.get(dataPosition).getTool().setFeed_digg_count(mDynamicBeens.get(dataPosition).getTool().getIs_digg_feed() == DynamicToolBean.STATUS_DIGG_FEED_UNCHECKED ?
                mDynamicBeens.get(dataPosition).getTool().getFeed_digg_count() - 1 : mDynamicBeens.get(dataPosition).getTool().getFeed_digg_count() + 1);
        refresh();
        mPresenter.handleLike(mDynamicBeens.get(dataPosition).getTool().getIs_digg_feed() == DynamicToolBean.STATUS_DIGG_FEED_CHECKED,
                mDynamicBeens.get(dataPosition).getFeed().getFeed_id(), dataPosition);
    }
}
