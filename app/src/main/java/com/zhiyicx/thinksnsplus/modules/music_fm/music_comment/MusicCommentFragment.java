package com.zhiyicx.thinksnsplus.modules.music_fm.music_comment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.utils.ImageUtils;
import com.zhiyicx.baseproject.widget.InputLimitView;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumDetailsBean;
import com.zhiyicx.thinksnsplus.data.beans.MusicCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.MusicDetaisBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.i.OnUserInfoClickListener;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.adapter.MusicCommentItem;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.adapter.MusicEmptyCommentItem;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

import static com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow.POPUPWINDOW_ALPHA;
import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.modules.home.message.messagecomment.MessageCommentAdapter.BUNDLE_SOURCE_ID;

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
    @BindView(R.id.v_shadow)
    View mVShadow;

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
        mIlvComment.setSendButtonVisiable(true);
        mIlvComment.post(new Runnable() { // 处理评论框位置协调
            @Override
            public void run() {
//                mRvList.setPadding(0, 0, 0, mIlvComment.getHeight());
            }
        });
        mMusicCommentHeader = new MusicCommentHeader(getActivity());

        mHeaderInfo = (MusicCommentHeader.HeaderInfo) getArguments()
                .getSerializable(CURRENT_COMMENT);
        if (mHeaderInfo != null) {
            mMusicCommentHeader.setHeadInfo(mHeaderInfo);
            mToolbarCenter.setText(String.format("评论(%d)",mHeaderInfo.getCommentCount()));
        } else {
            Long ids = getArguments().getLong(BUNDLE_SOURCE_ID);
            mHeaderInfo = new MusicCommentHeader.HeaderInfo();
            mHeaderInfo.setId(ids.intValue());
        }
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);

        mHeaderAndFooterWrapper.addHeaderView(mMusicCommentHeader.getMusicCommentHeader());
        mRvList.setAdapter(mHeaderAndFooterWrapper);
        mHeaderAndFooterWrapper.notifyDataSetChanged();
        initLisener();
    }

    @Override
    public void setHeaderInfo(MusicCommentHeader.HeaderInfo headerInfo) {
        mHeaderInfo = headerInfo;
        mMusicCommentHeader.setHeadInfo(mHeaderInfo);
        mToolbarCenter.setText(String.format("评论(%d)",mHeaderInfo.getCommentCount()));
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
        if (mListDatas.get(mListDatas.size() - 1).getComment_content() == null) {
            mMusicCommentHeader.setCommentList(0);
            return;
        }
        mMusicCommentHeader.setCommentList(mHeaderInfo.getCommentCount());
    }

    @Override
    protected Long getMaxId(@NotNull List<MusicCommentListBean> data) {
        return data.get(data.size() - 1).getId();
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
            mMusicCommentHeader.setCommentList(0);
        } else if (!isLoadMore && !data.isEmpty()) {
            mMusicCommentHeader.setCommentList(data.size());
        }
        super.onNetResponseSuccess(data, isLoadMore);
    }

    @Override
    public String getType() {
        return getArguments().getString(CURRENT_COMMENT_TYPE, "");
    }

    private void initLisener() {
        RxView.clicks(mVShadow)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        mIlvComment.setVisibility(View.GONE);
                        mIlvComment.clearFocus();
                        DeviceUtils.hideSoftKeyboard(getActivity(), mIlvComment.getEtContent());
                        mVShadow.setVisibility(View.GONE);

                    }
                });
        mIlvComment.setOnSendClickListener(this);
    }

    public void showCommentView() {
        // 评论
        mIlvComment.setSendButtonVisiable(true);
        mIlvComment.getFocus();
        mVShadow.setVisibility(View.VISIBLE);
        DeviceUtils.showSoftKeyboard(getActivity(), mIlvComment.getEtContent());
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
                        mHeaderInfo.setCommentCount(mHeaderInfo.getCommentCount() - 1);
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
