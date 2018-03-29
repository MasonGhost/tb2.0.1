package com.zhiyicx.thinksnsplus.modules.tb.contract;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.imageview.SquareImageView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.HintSideBarUserBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.home.HomeFragment;
import com.zhiyicx.thinksnsplus.modules.tb.detail.MerchainMessagelistActivity;
import com.zhiyicx.thinksnsplus.modules.tb.search.SearchMechanismUserActivity;
import com.zhiyicx.thinksnsplus.widget.hintsidebar.HintSideBar;
import com.zhiyicx.thinksnsplus.widget.hintsidebar.SideBar;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.Collections;

import javax.inject.Inject;

import butterknife.BindView;

public class ContractListFragment extends TSListFragment<ContractListContract.Presenter, HintSideBarUserBean>
        implements ContractListContract.View, SideBar.OnChooseLetterChangedListener, MultiItemTypeAdapter.OnItemClickListener {

    @Inject
    ContractListPresenter mContractListPresenter;

    @BindView(R.id.hintSideBar)
    HintSideBar mHintSideBar;

    public static ContractListFragment newInstance() {
        ContractListFragment contractListFragment = new ContractListFragment();
        return contractListFragment;
    }

    @Override
    protected void initData() {
        super.initData();
        mHintSideBar.setOnChooseLetterChangedListener(this);
    }

    @Override
    protected int setEmptView() {
        return R.mipmap.def_follow_prompt;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        setCenterTextColor(R.color.white);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerContractListComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .contractListPresenterModule(new ContractListPresenterModule(this))
                .build().inject(this);
    }

    @Override
    protected int setLeftImg() {
        return R.mipmap.ic_return_a_click;
    }

    @Override
    protected void setLeftClick() {
        ((HomeFragment) getParentFragment()).setCurrenPageToMessage();
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_user_followings;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.tb_user_followings);
    }

    @Override
    protected int setRightImg() {
        return R.mipmap.ic_search_a_click;
    }

    @Override
    protected void setRightClick() {
        startActivity(new Intent(mActivity, SearchMechanismUserActivity.class));
    }

    @Override
    protected int setToolBarBackgroud() {
        return R.color.themeColor;
    }

    @Override
    protected void setCenterTextColor(int resId) {
        super.setCenterTextColor(resId);
    }

    @Override
    protected boolean showToolbar() {
        return true;
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    protected boolean setUseSatusbar() {
        return true;
    }

    @Override
    protected boolean setUseStatusView() {
        return true;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        Collections.sort(mListDatas);
        CommonAdapter adapter = new CommonAdapter<HintSideBarUserBean>(mActivity, R.layout.item_user, mListDatas) {
            @Override
            protected void convert(ViewHolder holder, HintSideBarUserBean hintSideBarUserBean, int position) {
                HintSideBarUserBean user = mListDatas.get(position);
                TextView mTvAtalog = holder.getView(R.id.catalog);
                if (user.getAvatar() != null) {
                    SquareImageView squareImageView = holder.getView(R.id.iv_contact_headpic);
                    Glide.with(mActivity)
                            .load(user.getAvatar())
                            .placeholder(R.drawable.shape_default_image)
                            .placeholder(R.drawable.shape_default_error_image)
                            .centerCrop()
                            .into(squareImageView);
                }
                holder.setText(R.id.name, user.getUserName());
                int sectionForPosition = getSectionForPosition(position);
                int positionForSection = getPositionForSection(sectionForPosition);
                if (position == positionForSection) {
                    mTvAtalog.setVisibility(View.VISIBLE);
                    mTvAtalog.setText(user.getHeadLetter() + "");
                } else {
                    mTvAtalog.setVisibility(View.GONE);
                }
            }
        };
        adapter.setOnItemClickListener(this);
        return adapter;

    }

    public int getPositionForSection(int sectionIndex) {
        for (int i = 0; i < mListDatas.size(); i++) {
            if (mListDatas.get(i).getHeadLetter() == sectionIndex) {
                return i;
            }
        }
        return -1;
    }

    public int getSectionForPosition(int position) {
        return mListDatas.get(position).getHeadLetter();
    }

    @Override
    protected void layzLoad() {
        if (mPresenter != null && getUserVisibleHint()) {
            getNewDataFromNet();
        }
    }

    @Override
    public void onChooseLetter(String s) {
        int i = getFirstPositionByChar(s.charAt(0));
        if (i == -1) {
            return;
        }
        ((LinearLayoutManager) layoutManager).scrollToPositionWithOffset(i, 0);
    }

    public int getFirstPositionByChar(char sign) {
        if (sign == '↑') {
            return 0;
        }
        for (int i = 0; i < mListDatas.size(); i++) {
            if (mListDatas.get(i).getHeadLetter() == sign) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onNoChooseLetter() {

    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        UserInfoBean userInfoBean = mPresenter.getLocalUsrinfo(mListDatas.get(position).getId());
        if (userInfoBean != null) {
            // 进入公众号
            Intent intent = new Intent(getActivity(), MerchainMessagelistActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(MerchainMessagelistActivity.BUNDLE_USER, userInfoBean);
            intent.putExtras(bundle);
            startActivity(intent);
        }

    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        return false;
    }
}
