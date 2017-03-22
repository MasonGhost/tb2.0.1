package com.zhiyicx.thinksnsplus.modules.music_fm.music_comment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.InputLimitView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.MusicCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.i.OnUserInfoClickListener;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import butterknife.BindView;

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

    public static MusicCommentFragment newInstance(Bundle params) {
        MusicCommentFragment fragment = new MusicCommentFragment();
        fragment.setArguments(params);
        return fragment;
    }

    @Override
    protected MultiItemTypeAdapter getAdapter() {
        return null;
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

    }

    @Override
    public void setPresenter(MusicCommentContract.Presenter presenter) {

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

}
