package com.zhiyicx.thinksnsplus.modules.music_fm.music_comment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.InputLimitView;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.MusicCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.i.OnUserInfoClickListener;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.adapter.MusicCommentItem;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.adapter.MusicEmptyCommentItem;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow.POPUPWINDOW_ALPHA;

/**
 * @Author Jliuer
 * @Date 2017/03/22
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MusicCommentFragment extends TSListFragment<MusicCommentContract.Presenter,
        MusicCommentListBean> implements MusicCommentContract.View, OnUserInfoClickListener,
        InputLimitView.OnSendClickListener, MultiItemTypeAdapter.OnItemClickListener {

    @BindView(R.id.ilv_comment)
    InputLimitView mIlvComment;

    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    public static final String CURRENT_COMMENT = "current_comment";
    public static final String CURRENT_COMMENT_TYPE_ABLUM = "special";
    public static final String CURRENT_COMMENT_TYPE_MUSIC = "music";
    public static final String CURRENT_COMMENT_TYPE = "type";
    private MusicCommentHeader.HeaderInfo mHeaderInfo;
    private MusicCommentHeader mMusicCommentHeader;
    private int mReplyUserId = 0;// 被评论者的 id ,评论动态 id = 0
    private ActionPopupWindow mDeletCommentPopWindow;

    public static MusicCommentFragment newInstance(Bundle params) {
        MusicCommentFragment fragment = new MusicCommentFragment();
        fragment.setArguments(params);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mIlvComment.post(new Runnable() { // 处理评论框位置协调
            @Override
            public void run() {
                mRvList.setPadding(0, 0, 0, mIlvComment.getHeight());
            }
        });
        mMusicCommentHeader = new MusicCommentHeader(getActivity());

        mHeaderInfo = (MusicCommentHeader.HeaderInfo) getArguments()
                .getSerializable(CURRENT_COMMENT);
        if (mHeaderInfo != null) {
            mMusicCommentHeader.setHeadInfo(mHeaderInfo);
        }
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);

        mHeaderAndFooterWrapper.addHeaderView(mMusicCommentHeader.getMusicCommentHeader());
        mRvList.setAdapter(mHeaderAndFooterWrapper);
        mHeaderAndFooterWrapper.notifyDataSetChanged();
        initLisener();
    }

    @Override
    protected MultiItemTypeAdapter<MusicCommentListBean> getAdapter() {
        MultiItemTypeAdapter adapter = new MultiItemTypeAdapter<>(getContext(), mListDatas);
        MusicCommentItem musicCommentItem = new MusicCommentItem();
        musicCommentItem.setOnUserInfoClickListener(this);
        adapter.addItemViewDelegate(musicCommentItem);
        adapter.addItemViewDelegate(new MusicEmptyCommentItem());
        adapter.setOnItemClickListener(this);
        return adapter;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_music_comment;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.comment);
    }

    @Override
    public void onSendClick(View v, String text) {
        DeviceUtils.hideSoftKeyboard(getContext(), v);
        mHeaderInfo.setCommentCount(mHeaderInfo.getCommentCount() + 1);
        mPresenter.sendComment(mReplyUserId, text);
    }

    @Override
    public int getCommentId() {
        return mHeaderInfo.getId();
    }

    @Override
    public void setPresenter(MusicCommentContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onUserInfoClick(UserInfoBean userInfoBean) {

    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        position = position - 1;
        LogUtils.d(mListDatas.get(position).getUser_id());
        LogUtils.d(AppApplication.getmCurrentLoginAuth().getUser_id());
        if (mListDatas.get(position).getUser_id() == AppApplication.getmCurrentLoginAuth()
                .getUser_id()) {// 自己的评论
            if (mListDatas.get(position).getId() != -1) {
                initLoginOutPopupWindow(mListDatas.get(position));
                mDeletCommentPopWindow.show();
            } else {
                return;
            }
        } else {
            mReplyUserId = mListDatas.get(position).getUser_id();
            showCommentView();
            String contentHint = getString(R.string.default_input_hint);
            if (mListDatas.get(position).getReply_to_user_id() != mHeaderInfo.getId()) {
                contentHint = getString(R.string.reply, mListDatas.get(position).getFromUserInfoBean().getName());
            }
            mIlvComment.setEtContentHint(contentHint);
        }
    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        return false;
    }

    @Override
    protected void requestNetData(Long maxId, boolean isLoadMore) {
        mPresenter.requestNetData(mHeaderInfo.getId() + "", maxId, isLoadMore);
    }

    @Override
    public void refreshData() {
        mHeaderAndFooterWrapper.notifyDataSetChanged();
        mMusicCommentHeader.setCommentList(mHeaderInfo.getCommentCount());
    }

    @Override
    protected Long getMaxId(@NotNull List<MusicCommentListBean> data) {
        return (long) data.get(data.size() - 1).getId();
    }

    @Override
    protected List<MusicCommentListBean> requestCacheData(Long maxId, boolean isLoadMore) {
        return new ArrayList<>();
    }

    @Override
    public void onNetResponseSuccess(@NotNull List<MusicCommentListBean> data, boolean isLoadMore) {
        if (!isLoadMore && data.isEmpty()) { // 增加空数据，用于显示占位图
            MusicCommentListBean emptyData = new MusicCommentListBean();
            data.add(emptyData);
        }
        mMusicCommentHeader.setCommentList(data.size());
        super.onNetResponseSuccess(data, isLoadMore);
    }

    @Override
    public String getType() {
        return getArguments().getString(CURRENT_COMMENT_TYPE, "");
    }

    private void initLisener() {
        showCommentView();
        mIlvComment.setOnSendClickListener(this);
    }

    public void showCommentView() {
        // 评论
        mIlvComment.setVisibility(View.VISIBLE);
        mIlvComment.setSendButtonVisiable(true);
    }

    private void initLoginOutPopupWindow(final MusicCommentListBean data) {
        mDeletCommentPopWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.dynamic_list_delete_comment))
                .item1StrColor(ContextCompat.getColor(getContext(), R.color.themeColor))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(new ActionPopupWindow.ActionPopupWindowItem1ClickListener() {
                    @Override
                    public void onItem1Clicked() {
                        ToastUtils.showToast("暂无接口");
                        mPresenter.deleteComment(data);
                        mDeletCommentPopWindow.hide();
                    }
                })
                .bottomClickListener(new ActionPopupWindow.ActionPopupWindowBottomClickListener() {
                    @Override
                    public void onBottomClicked() {
                        mDeletCommentPopWindow.hide();
                    }
                })
                .build();
    }
}
