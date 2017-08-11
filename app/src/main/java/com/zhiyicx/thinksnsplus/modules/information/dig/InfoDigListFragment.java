package com.zhiyicx.thinksnsplus.modules.information.dig;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.InfoDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.modules.information.adapter.InfoDigListAdapter;

import static com.zhiyicx.thinksnsplus.modules.information.dig.InfoDigListActivity.BUNDLE_INFO_DIG;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/11
 * @contact email:648129313@qq.com
 */

public class InfoDigListFragment extends TSListFragment<InfoDigListContract.Presenter, InfoDigListBean>
        implements InfoDigListContract.View{

    private InfoListDataBean mInfoDataBean;

    public InfoDigListFragment instance(Bundle bundle){
        InfoDigListFragment fragment = new InfoDigListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mInfoDataBean = (InfoListDataBean) getArguments().getSerializable(BUNDLE_INFO_DIG);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new InfoDigListAdapter(getContext(), mListDatas, mPresenter);
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.dig_list);
    }

    @Override
    public long getNesId() {
        return mInfoDataBean == null ? 0 : mInfoDataBean.getId();
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
