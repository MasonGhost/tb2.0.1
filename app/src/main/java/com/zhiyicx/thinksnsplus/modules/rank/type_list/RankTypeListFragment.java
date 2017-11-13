package com.zhiyicx.thinksnsplus.modules.rank.type_list;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.data.beans.RankIndexBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.modules.rank.adapter.RankTypeEmptyItem;
import com.zhiyicx.thinksnsplus.modules.rank.adapter.RankTypeItem;
import com.zhiyicx.thinksnsplus.modules.rank.adapter.RankTypeMineItem;
import com.zhiyicx.thinksnsplus.modules.rank.main.container.RankTypeConfig;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.zhiyicx.thinksnsplus.modules.rank.type_list.RankTypeListActivity.BUNDLE_RANK_BEAN;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/23
 * @contact email:648129313@qq.com
 */

public class RankTypeListFragment extends TSListFragment<RankTypeListContract.Presenter, UserInfoBean>
        implements RankTypeListContract.View{

    private RankIndexBean mRankIndexBean;

    public RankTypeListFragment instance(Bundle bundle){
        RankTypeListFragment fragment = new RankTypeListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initData() {
        if (mRankIndexBean == null){
            mRankIndexBean = (RankIndexBean) getArguments().getSerializable(BUNDLE_RANK_BEAN);
        }
        super.initData();
        setCenterText(mRankIndexBean.getSubCategory());
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        MultiItemTypeAdapter multiItemTypeAdapter = new MultiItemTypeAdapter<>(getActivity(),
                mListDatas);
        multiItemTypeAdapter.addItemViewDelegate(new RankTypeItem(getContext(), getRankType(), mPresenter));
        multiItemTypeAdapter.addItemViewDelegate(new RankTypeEmptyItem());
        multiItemTypeAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (mPresenter.handleTouristControl()) { // 游客勿入
                    return;
                }
                if (mListDatas.get(position) != null && !mListDatas.get(position).getUser_id().equals(0L)){
                    PersonalCenterFragment.startToPersonalCenter(getContext(), mListDatas.get(position));
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        return multiItemTypeAdapter;
    }

    @Override
    public String getRankType() {
        if (mRankIndexBean == null){
            mRankIndexBean = (RankIndexBean) getArguments().getSerializable(BUNDLE_RANK_BEAN);
        }
        return mRankIndexBean == null ? "" : mRankIndexBean.getType();
    }

    @Override
    protected boolean isUseTouristLoadLimit() {
        return false;
    }

    @Override
    public void onNetResponseSuccess(@NotNull List<UserInfoBean> data, boolean isLoadMore) {
        if (!isLoadMore) {
            if (data.isEmpty()) { // 空白展位图
                UserInfoBean emptyData = new UserInfoBean();
                emptyData.setUser_id(0L);
                data.add(emptyData);
            }
        }
        super.onNetResponseSuccess(data, isLoadMore);
    }

    // 这个白石 又加上了limit 😔
//    @Override
//    protected int getPagesize() {
//        if (getRankType().equals(RankTypeConfig.RANK_USER_CHECK_ID)){
//            return DEFAULT_PAGE_SIZE_X;
//        }
//        return super.getPagesize();
//    }
}
