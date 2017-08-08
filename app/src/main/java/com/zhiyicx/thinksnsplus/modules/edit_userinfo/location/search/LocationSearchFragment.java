package com.zhiyicx.thinksnsplus.modules.edit_userinfo.location.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.TouristConfig;
import com.zhiyicx.baseproject.widget.edittext.DeleteEditText;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.FileUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.data.beans.LocationBean;
import com.zhiyicx.thinksnsplus.modules.information.adapter.InfoListItem;
import com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsActivity;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import butterknife.BindView;
import butterknife.OnClick;

import static com.zhiyicx.thinksnsplus.modules.information.infomain.list.InfoListFragment.BUNDLE_INFO;

/**
 * @Describe 地区搜索界面
 * @Author Jungle68
 * @Date 2017/1/9
 * @Contact master.jungle68@gmail.com
 */
public class LocationSearchFragment extends TSListFragment<LocationSearchContract.Presenter, LocationBean> implements LocationSearchContract.View, MultiItemTypeAdapter.OnItemClickListener {


    @BindView(R.id.fragment_search_back)
    ImageView mFragmentInfoSearchBack;
    @BindView(R.id.fragment_info_search_edittext)
    DeleteEditText mFragmentInfoSearchEdittext;
    @BindView(R.id.fragment_search_cancle)
    TextView mFragmentInfoSearchCancle;
    @BindView(R.id.fragment_info_search_container)
    RelativeLayout mFragmentInfoSearchContainer;

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_location_search;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected boolean isRefreshEnable() {
        return false;
    }

    @Override
    protected boolean isLoadingMoreEnable() {
        return false;
    }

    @Override
    protected void musicWindowsStatus(boolean isShow) {
        super.musicWindowsStatus(isShow);
        if (isShow) {
            int rightX = ConvertUtils.dp2px(getContext(), 44) * 3 / 4 + ConvertUtils.dp2px(getContext(), 15);
            mFragmentInfoSearchContainer.setPadding(0, 0, rightX, 0);
        }
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mEmptyView.setVisibility(View.GONE);
        mFragmentInfoSearchEdittext.setOnEditorActionListener(
                (v, actionId, event) -> {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        mPresenter.searchLocation(mFragmentInfoSearchEdittext.getText().toString());
                        return true;
                    }
                    return false;
                });
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @OnClick({R.id.fragment_search_back, R.id.fragment_search_cancle})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_search_back:
                getActivity().finish();
                break;
            case R.id.fragment_search_cancle:
                getActivity().finish();
                break;
        }
    }

    @Override
    protected MultiItemTypeAdapter getAdapter() {
        MultiItemTypeAdapter adapter = new LocationSearchListAdapter(getActivity(),R.layout.item_location_search, mListDatas);
        adapter.setOnItemClickListener(this);
        return adapter;

    }

    @Override
    public void onDismiss() {
        super.onDismiss();
        mFragmentInfoSearchContainer.setPadding(0, 0, 0, 0);
    }


    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        return false;
    }
}
