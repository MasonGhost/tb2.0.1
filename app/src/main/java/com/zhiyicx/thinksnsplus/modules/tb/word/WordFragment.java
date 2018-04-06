package com.zhiyicx.thinksnsplus.modules.tb.word;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.InfoCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.WordResourceBean;
import com.zhiyicx.thinksnsplus.i.OnUserInfoClickListener;
import com.zhiyicx.thinksnsplus.modules.information.adapter.InfoDetailCommentCopyItem;
import com.zhiyicx.thinksnsplus.modules.information.adapter.InfoDetailCommentEmptyItem;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.widget.CenterDialog;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import static com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow.POPUPWINDOW_ALPHA;

public class WordFragment extends TSListFragment<WordContract.Presenter, InfoCommentListBean> implements WordContract.View,
        OnUserInfoClickListener,
        InfoDetailCommentCopyItem.OnDeleteClickListener{
    public static final String BUNDLE_WORD_RESOURCE_DATA = "word_resource_data";

    private WordResourceBean mWordResourceBean;
    private ActionPopupWindow mDeletCommentPopWindow;
    private WordHeaderView mWordHeaderView;
    private CenterDialog mWordDialog;

    public static WordFragment newInstance(Bundle bundle) {
        WordFragment wordFragment = new WordFragment();
        wordFragment.setArguments(bundle);
        return wordFragment;
    }

    @Override
    public Long getNewsId() {
        return Long.parseLong(mWordResourceBean.getId());
    }

    @Override
    public void wordSuccess() {
        //showSnackSuccessMessage(getString(R.string.word_success_tip));
        mWordDialog.show();
    }

    @Override
    protected MultiItemTypeAdapter<InfoCommentListBean> getAdapter() {
        MultiItemTypeAdapter multiItemTypeAdapter = new MultiItemTypeAdapter<>(getActivity(),
                mListDatas);
        InfoDetailCommentCopyItem infoDetailCommentItem = new InfoDetailCommentCopyItem(new
                OnDeleteClickListener());
        infoDetailCommentItem.setOnUserInfoClickListener(this);
        multiItemTypeAdapter.addItemViewDelegate(infoDetailCommentItem);
        multiItemTypeAdapter.addItemViewDelegate(new InfoDetailCommentEmptyItem(R.mipmap.ico_bg_color));
        return multiItemTypeAdapter;
    }

    @Override
    public void onUserInfoClick(UserInfoBean userInfoBean) {
        PersonalCenterFragment.startToPersonalCenter(getContext(), userInfoBean);
    }

    class OnDeleteClickListener implements InfoDetailCommentCopyItem.OnDeleteClickListener {
        @Override
        public void onItemDelete(int position) {
            comment(position);
        }
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mWordDialog = new CenterDialog(mActivity);
        if (getArguments() != null) {
            mWordResourceBean = (WordResourceBean) getArguments().getSerializable(BUNDLE_WORD_RESOURCE_DATA);
            setCenterText(mWordResourceBean.getUser().getName());
            initHeaderView();
        } else {
            throw new IllegalArgumentException("params is error ！please check it.");
        }
    }

    @Override
    public void onItemDelete(int position) {
        comment(position);
    }

    @Override
    protected boolean isNeedRefreshAnimation() {
        return false;
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    protected void layzLoad() {
        if (mPresenter != null && getUserVisibleHint()) {
            getNewDataFromNet();
        }
    }

    @Override
    protected void initData() {
        if (mWordResourceBean.getUser() == null || mWordResourceBean.getUser().getUser_id() == null) {
            throw new IllegalArgumentException("user info not be null!");
        }
        /*if (TextUtils.isEmpty(mWordResourceBean.getUser().getName())) {
            mPresenter.getUserInfoById(mWordResourceBean.getUser().getUser_id());
        } else {
            mWordHeaderView.setWordDetial(mWordResourceBean);
        }*/
        mWordHeaderView.setWordDetial(mWordResourceBean);
    }

    @Override
    public void refreshData() {
        mWordHeaderView.updateLlMyWord((mListDatas.size() == 1 && mListDatas.get(0).getId() == null) || mListDatas.isEmpty());
        super.refreshData();
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_word;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mWordHeaderView.onDestroyView();
    }

    @Override
    protected boolean isRefreshEnable() {
        return false;
    }

    private void initHeaderView() {
        mWordHeaderView = new WordHeaderView(getContext());
        //留言点击
        mWordHeaderView.mBtWord.setOnClickListener(v -> {
                DeviceUtils.hideSoftKeyboard(getContext(), mWordHeaderView.mEtWordContent.getEtContent());
                mPresenter.word(0, mWordHeaderView.mEtWordContent.getInputContent());
        });
        mHeaderAndFooterWrapper.addHeaderView(mWordHeaderView.getWordHeader());
        View mFooterView = new View(getContext());
        mFooterView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
        mHeaderAndFooterWrapper.addFootView(mFooterView);
        mRvList.setAdapter(mHeaderAndFooterWrapper);
        mHeaderAndFooterWrapper.notifyDataSetChanged();
    }

    @Override
    public void showLoading() {
        super.showLoading();
        mWordHeaderView.showLoading();
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        mWordHeaderView.hideLoading();
    }

    /**
     * 评论
     *
     * @param position
     */
    private void comment(int position) {
        position = position - mHeaderAndFooterWrapper.getHeadersCount();// 减去 header
        InfoCommentListBean infoCommentListBean = mListDatas.get(position);
        if (infoCommentListBean != null && !TextUtils.isEmpty(infoCommentListBean.getComment_content())) {
            if (infoCommentListBean.getUser_id() == AppApplication.getMyUserIdWithdefault()) {// 自己的评论
                initDeleteCommentPopupWindow(infoCommentListBean);
                mDeletCommentPopWindow.show();
            }
        }
    }

    /**
     * 初始化评论删除选择弹框
     */
    private void initDeleteCommentPopupWindow(final InfoCommentListBean data) {
        mDeletCommentPopWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.dynamic_list_delete_word))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(() -> {
                    mPresenter.deleteComment(data);
                    mDeletCommentPopWindow.hide();
                })
                .bottomClickListener(() -> mDeletCommentPopWindow.hide())
                .build();
    }
}
