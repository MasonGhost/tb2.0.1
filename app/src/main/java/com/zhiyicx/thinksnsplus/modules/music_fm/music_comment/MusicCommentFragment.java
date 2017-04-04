package com.zhiyicx.thinksnsplus.modules.music_fm.music_comment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.InputLimitView;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumDetailsBean;
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
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

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
    public static final String CURRENT_MUSIC = "current_music";
    private MusicAlbumDetailsBean.MusicsBean mCurrentMusic;
    private MusicCommentHeader mMusicCommentHeader;

    public static MusicCommentFragment newInstance(Bundle params) {
        MusicCommentFragment fragment = new MusicCommentFragment();
        fragment.setArguments(params);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mCurrentMusic = (MusicAlbumDetailsBean.MusicsBean) getArguments()
                .getSerializable(CURRENT_MUSIC);
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);
        mMusicCommentHeader = new MusicCommentHeader(getActivity());
        mMusicCommentHeader.setHeadInfo(mCurrentMusic);
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
//        adapter.addItemViewDelegate(new MusicEmptyCommentItem());
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
        mPresenter.sendComment(mCurrentMusic.getMusic_info().getId()+"", text);
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

    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        return false;
    }

    @Override
    protected void requestNetData(Long maxId, boolean isLoadMore) {
        mPresenter.requestNetData(mCurrentMusic.getMusic_info().getId() + "", maxId, isLoadMore);
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
//        if (!isLoadMore && data.isEmpty()) { // 增加空数据，用于显示占位图
//            MusicCommentListBean emptyData = new MusicCommentListBean();
//            data.add(emptyData);
//        }
        mMusicCommentHeader.setCommentList(data.size());
        super.onNetResponseSuccess(data, isLoadMore);
    }

    private void initLisener(){
        showCommentView();
        mIlvComment.setOnSendClickListener(this);
    }

    public void showCommentView() {
        // 评论
        mIlvComment.setVisibility(View.VISIBLE);
        mIlvComment.setSendButtonVisiable(true);
//        mIlvComment.getFocus();
    }
}
