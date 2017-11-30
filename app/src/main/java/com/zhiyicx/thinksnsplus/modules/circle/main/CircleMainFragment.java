package com.zhiyicx.thinksnsplus.modules.circle.main;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.beans.GroupInfoBean;
import com.zhiyicx.thinksnsplus.modules.circle.create.CreateCircleActivity;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.CircleDetailActivity;
import com.zhiyicx.thinksnsplus.modules.circle.main.adapter.BaseCircleItem;
import com.zhiyicx.thinksnsplus.modules.circle.main.adapter.CircleListItem;
import com.zhiyicx.thinksnsplus.modules.circle.main.adapter.CircleTypeItem;
import com.zhiyicx.thinksnsplus.modules.markdown_editor.MarkdownActivity;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

/**
 * @author Jliuer
 * @Date 2017/11/14/11:28
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleMainFragment extends TSListFragment<CircleMainContract.Presenter, CircleInfo>
        implements CircleMainContract.View, BaseCircleItem.CircleItemItemEvent {

    private CircleMainHeader mCircleMainHeader;

    @Override
    protected boolean setUseCenterLoading() {
        return false;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.group);
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
    protected boolean isNeedRefreshAnimation() {
        return false;
    }

    public static CircleMainFragment newInstance() {
        CircleMainFragment circleMainFragment = new CircleMainFragment();
        return circleMainFragment;
    }

    @Override
    protected int setRightImg() {
        return R.mipmap.ico_createcircle;
    }

    @Override
    protected int setRightLeftImg() {
        return R.mipmap.ico_search;
    }

    @Override
    protected void setRightClick() {
        super.setRightClick();
        startActivity(new Intent(getActivity(), CreateCircleActivity.class));
    }

    @Override
    protected void setRightLeftClick() {
        super.setRightLeftClick();
        startActivity(new Intent(getActivity(), MarkdownActivity.class));
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        for (int i = 0; i < 12; i++) {
            CircleInfo groupInfoBean = new CircleInfo();
            groupInfoBean.setId((i == 0 || i == 6) ? -1L : i);
            groupInfoBean.setName("我加入");
            groupInfoBean.setSummary("查看更多");
            mListDatas.add(groupInfoBean);
        }
        MultiItemTypeAdapter adapter = new MultiItemTypeAdapter<>(getContext(), mListDatas);
        adapter.addItemViewDelegate(new CircleListItem(this));
        adapter.addItemViewDelegate(new CircleTypeItem(this));
        return adapter;
    }

    @Override
    protected void initData() {
        mCircleMainHeader = new CircleMainHeader(getActivity(), null, 2341);
        mHeaderAndFooterWrapper.addHeaderView(mCircleMainHeader.getCircleMainHeader());
        super.initData();
    }

    @Override
    public void toAllCircle(CircleInfo groupInfoBean) {

    }

    @Override
    public void toCircleDetail(CircleInfo groupInfoBean) {
        startActivity(new Intent(getActivity(), CircleDetailActivity.class));
    }
}
