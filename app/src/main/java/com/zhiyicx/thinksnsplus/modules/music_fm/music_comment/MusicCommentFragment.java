package com.zhiyicx.thinksnsplus.modules.music_fm.music_comment;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.utils.WindowUtils;
import com.zhiyicx.baseproject.widget.InputLimitView;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.AndroidBug5497Workaround;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.MusicCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.MusicCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.i.OnCommentTextClickListener;
import com.zhiyicx.thinksnsplus.i.OnUserInfoClickListener;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.adapter.MusicCommentItem;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.adapter.MusicEmptyCommentItem;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.EventBus;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow.POPUPWINDOW_ALPHA;
import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_MUSIC_COMMENT_COUNT;
import static com.zhiyicx.thinksnsplus.modules.home.message.messagecomment.MessageCommentAdapter.BUNDLE_SOURCE_ID;

/**
 * @Author Jliuer
 * @Date 2017/03/22
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MusicCommentFragment extends TSListFragment<MusicCommentContract.Presenter,
        MusicCommentListBean> implements MusicCommentContract.View, OnUserInfoClickListener, OnCommentTextClickListener,
        InputLimitView.OnSendClickListener, MultiItemTypeAdapter.OnItemClickListener,
        MusicCommentItem.OnReSendClickListener, MusicCommentHeader.HeadlerClickEvent {

    @BindView(R.id.ilv_comment)
    InputLimitView mIlvComment;
    @BindView(R.id.v_shadow)
    View mVShadow;
    @BindView(R.id.tv_toolbar_center)
    TextView mTvToolbarCenter;
    @BindView(R.id.tv_toolbar_left)
    TextView mTvToolbarLeft;

    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    public static final String CURRENT_COMMENT = "current_comment";
    public static final String CURRENT_COMMENT_TYPE_ABLUM = "special";
    public static final String CURRENT_COMMENT_TYPE_MUSIC = "music";
    public static final String CURRENT_COMMENT_TYPE = "type";
    private MusicCommentHeader.HeaderInfo mHeaderInfo;
    private MusicCommentHeader mMusicCommentHeader;
    private long mReplyUserId = 0;// 被评论者的 id ,评论动态 id = 0
    private ActionPopupWindow mDeletCommentPopWindow;
    private ActionPopupWindow mReSendCommentPopWindow;

    public static MusicCommentFragment newInstance(Bundle params) {
        MusicCommentFragment fragment = new MusicCommentFragment();
        fragment.setArguments(params);
        return fragment;
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    protected boolean isNeedRefreshAnimation() {
        return false;
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected void initView(View rootView) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            AndroidBug5497Workaround.assistActivity(getActivity());
        }
        super.initView(rootView);
        mIlvComment.setSendButtonVisiable(true);
        mIlvComment.setEtContentHint(getString(R.string.default_input_hint));
        mMusicCommentHeader = new MusicCommentHeader(getActivity());
        mMusicCommentHeader.setHeadlerClickEvent(this);
        mHeaderInfo = (MusicCommentHeader.HeaderInfo) getArguments()
                .getSerializable(CURRENT_COMMENT);
        if (mHeaderInfo != null) {
            mMusicCommentHeader.setHeadInfo(mHeaderInfo);
            mTvToolbarCenter.setText(String.format("评论(%s)", mHeaderInfo.getCommentCount()));
        } else {
            Long ids = getArguments().getLong(BUNDLE_SOURCE_ID);
            mHeaderInfo = new MusicCommentHeader.HeaderInfo();
            mHeaderInfo.setId(ids.intValue());
            if (getType().equals(CURRENT_COMMENT_TYPE_MUSIC)) {
                mPresenter.getMusicDetails(ids.intValue() + "");
            } else if (getType().equals(CURRENT_COMMENT_TYPE_ABLUM)) {
                mPresenter.getMusicAblum(ids.intValue() + "");
            }
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
        mTvToolbarCenter.setText(String.format("评论(%d)", mHeaderInfo.getCommentCount()));
        EventBus.getDefault().post(mHeaderInfo, EVENT_MUSIC_COMMENT_COUNT);
    }

    @Override
    protected MultiItemTypeAdapter<MusicCommentListBean> getAdapter() {
        MultiItemTypeAdapter adapter = new MultiItemTypeAdapter<>(getContext(), mListDatas);
        MusicCommentItem musicCommentItem = new MusicCommentItem();
        musicCommentItem.setOnReSendClickListener(this);
        musicCommentItem.setOnUserInfoClickListener(this);
        musicCommentItem.setOnCommentTextClickListener(this);
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
        setHeaderInfo(mHeaderInfo);
        mVShadow.setVisibility(View.GONE);
        mPresenter.sendComment(mReplyUserId, text);
        if (WindowUtils.getAblumHeadInfo() != null) {
            WindowUtils.getAblumHeadInfo().setCommentCount(WindowUtils.getAblumHeadInfo().getCommentCount() + 1);
        }
    }

    @Override
    public void onReSendClick(MusicCommentListBean musicCommentListBean) {
        initReSendCommentPopupWindow(musicCommentListBean);
        mReSendCommentPopWindow.show();
    }

    @Override
    public void onResponseError(Throwable throwable, boolean isLoadMore) {
        super.onResponseError(throwable, isLoadMore);
        mMusicCommentHeader.hide();
    }

    @Override
    public long getCommentId() {
        return mHeaderInfo.getId();
    }

    @Override
    public void setPresenter(MusicCommentContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onUserInfoClick(UserInfoBean userInfoBean) {
        PersonalCenterFragment.startToPersonalCenter(getContext(), userInfoBean);
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        handleItemClick(position);
    }

    private void handleItemClick(int position) {
        position = position - 1;
        if (mListDatas.get(position).getUser_id() == AppApplication.getmCurrentLoginAuth()
                .getUser_id()) {// 自己的评论
//            if (mListDatas.get(position).getId() != -1) {
            initDeleteCommentPopupWindow(mListDatas.get(position));
            mDeletCommentPopWindow.show();
//            } else {
//                return;
//            }
        } else {
            mReplyUserId = mListDatas.get(position).getUser_id();
            showCommentView();
            String contentHint = getString(R.string.default_input_hint);
            if (mListDatas.get(position).getReply_user() != mHeaderInfo.getId()) {
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
    public void onNetResponseSuccess(@NotNull List<MusicCommentListBean> data, boolean isLoadMore) {
        if (!isLoadMore && data.isEmpty()) { // 增加空数据，用于显示占位图
            MusicCommentListBean emptyData = new MusicCommentListBean();
            data.add(emptyData);
            mMusicCommentHeader.setCommentList(0);
        } else if (!isLoadMore && !data.isEmpty()) {
            mMusicCommentHeader.setCommentList(data.size());
//            mHeaderInfo.setCommentCount(data.size());
//            setHeaderInfo(mHeaderInfo);
        }
        super.onNetResponseSuccess(data, isLoadMore);
    }

    @Override
    public String getType() {
        return getArguments().getString(CURRENT_COMMENT_TYPE, "");
    }

    @Override
    public void headClick() {
        getActivity().finish();
    }

    private void initLisener() {
        RxView.clicks(mVShadow)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    mIlvComment.clearFocus();
                    DeviceUtils.hideSoftKeyboard(getActivity(), mIlvComment.getEtContent());
                    mVShadow.setVisibility(View.GONE);

                });
        mIlvComment.setOnSendClickListener(this);

        RxView.globalLayouts(mIlvComment)
                .flatMap(new Func1<Void, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(Void aVoid) {
                        if (mIlvComment == null) {
                            return Observable.just(false);
                        }
                        Rect rect = new Rect();
                        //获取root在窗体的可视区域
                        mIlvComment.getWindowVisibleDisplayFrame(rect);
                        //获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
                        int rootInvisibleHeight = mIlvComment.getRootView().getHeight() - rect.bottom;
                        int dispayHeight = UIUtils.getWindowHeight(getContext());
                        return Observable.just(rootInvisibleHeight > (dispayHeight * (1f / 3)));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {
                    if (mVShadow == null) {
                        return;
                    }
                    //若不可视区域高度大于1/3屏幕高度，则键盘显示
                    if (aBoolean) {
                        mVShadow.setVisibility(View.VISIBLE);
                    } else {
                        mVShadow.setVisibility(View.GONE);
                    }
                });

    }

    public void showCommentView() {
        // 评论
        mIlvComment.setSendButtonVisiable(true);
        mIlvComment.getFocus();
        mVShadow.setVisibility(View.VISIBLE);
        DeviceUtils.showSoftKeyboard(getActivity(), mIlvComment.getEtContent());
    }

    private void initDeleteCommentPopupWindow(final MusicCommentListBean data) {
        mDeletCommentPopWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.dynamic_list_delete_comment))
                .item1Color(ContextCompat.getColor(getContext(), R.color.themeColor))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(() -> {

                    mDeletCommentPopWindow.hide();
                    showDeleteTipPopupWindow(getString(R.string.delete_comment), () -> {
                        mHeaderInfo.setCommentCount(mHeaderInfo.getCommentCount() - 1);
                        setHeaderInfo(mHeaderInfo);
                        if (WindowUtils.getAblumHeadInfo() != null) {
                            if (WindowUtils.getAblumHeadInfo().getCommentCount() > 0) {
                                WindowUtils.getAblumHeadInfo().setCommentCount(WindowUtils.getAblumHeadInfo().getCommentCount() - 1);
                            }
                        }
                        mPresenter.deleteComment(data);
                    }, true);
                })
                .bottomClickListener(() -> mDeletCommentPopWindow.hide())
                .build();
    }

    private void initReSendCommentPopupWindow(final MusicCommentListBean commentBean) {
        mReSendCommentPopWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.dynamic_list_resend_comment))
                .item1Color(ContextCompat.getColor(getContext(), R.color.themeColor))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(() -> {
                    mPresenter.reSendComment(commentBean);
                    mReSendCommentPopWindow.hide();
                })
                .bottomClickListener(() -> mReSendCommentPopWindow.hide())
                .build();
    }

    @OnClick(R.id.tv_toolbar_left)
    public void onViewClicked() {
        getActivity().finish();
    }

    @Override
    public void onCommentTextClick(int position) {
        handleItemClick(position);
    }
}
