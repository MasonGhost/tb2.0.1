package com.zhiyicx.thinksnsplus.modules.circle.detailv2.dig;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.dig.adapter.DynamicDigListItem;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.dig.adapter.InfoDigListItem;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.dig.adapter.PostDigListItem;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

/**
 * @Author Jliuer
 * @Date 17/12/11 15:41
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class DigListFragment extends TSListFragment<DigListContract.Presenter, BaseListBean>
        implements DigListContract.View {

    public static final String SOURCEID = "sourceid";
    public static final String TYPE = "type";

    private long sourceId;
    private String mDigType;

    public DigListFragment instance(Bundle bundle) {
        DigListFragment fragment = new DigListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        if (getArguments() == null) {
            return;
        }
        mDigType = getArguments().getString(TYPE);
        sourceId = getArguments().getLong(SOURCEID);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        MultiItemTypeAdapter adapter = new MultiItemTypeAdapter(getContext(), mListDatas);
        DynamicDigListItem dynamicDigListItem = new DynamicDigListItem(getActivity(), mPresenter);
        InfoDigListItem infoDigListItem = new InfoDigListItem(getActivity(), mPresenter);
        PostDigListItem postDigListItem = new PostDigListItem(getActivity(), mPresenter);
        adapter.addItemViewDelegate(dynamicDigListItem);
        adapter.addItemViewDelegate(infoDigListItem);
        adapter.addItemViewDelegate(postDigListItem);
        return adapter;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.dig_list);
    }


    @Override
    public long getSourceId() {
        return sourceId;
    }

    @Override
    public String getType() {
        return mDigType;
    }

    @Override
    public void upDataFollowState(int position) {
        refreshData(position);
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }
}
