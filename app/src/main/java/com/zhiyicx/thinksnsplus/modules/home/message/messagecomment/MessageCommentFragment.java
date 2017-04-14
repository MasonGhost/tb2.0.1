package com.zhiyicx.thinksnsplus.modules.home.message.messagecomment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.InputLimitView;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.CommentedBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe 消息评论
 * @Author Jungle68
 * @Date 2017/1/17
 * @Contact master.jungle68@gmail.com
 */
public class MessageCommentFragment extends TSListFragment<MessageCommentContract.Presenter, CommentedBean> implements MessageCommentContract.View, InputLimitView.OnSendClickListener, MultiItemTypeAdapter.OnItemClickListener {

    @BindView(R.id.ilv_comment)
    InputLimitView mIlvComment;
    @BindView(R.id.v_shadow)
    View mVShadow;
    private long mReplyUserId;// 被评论者的 id ,评论动态 id = 0
    private int mCurrentPostion;// 当前点击的 item 位置

    public MessageCommentFragment() {
    }

    public static MessageCommentFragment newInstance() {
        MessageCommentFragment fragment = new MessageCommentFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected boolean setUseSatusbar() {
        return true;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_list_with_input;
    }


    @Override
    protected String setCenterTitle() {
        return getString(R.string.comment);
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    protected float getItemDecorationSpacing() {
        return 0;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        initInputView();
    }

    @Override
    protected CommonAdapter<CommentedBean> getAdapter() {
        CommonAdapter commonAdapter = new MessageCommentAdapter(getActivity(), R.layout.item_message_comment_list, mListDatas);
        commonAdapter.setOnItemClickListener(this);
        return commonAdapter;
    }

    @Override
    protected void initData() {
        super.initData();

    }

    private void initInputView() {
        mVShadow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeInputView();
            }
        });
        RxView.clicks(mVShadow)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        closeInputView();

                    }
                });
        mIlvComment.setOnSendClickListener(this);
    }

    @Override
    public void onSendClick(View v, String text) {
        DeviceUtils.hideSoftKeyboard(getContext(), v);
        mIlvComment.setVisibility(View.GONE);
        mVShadow.setVisibility(View.GONE);
        mPresenter.sendComment(mCurrentPostion, mReplyUserId, text);
    }

    @Override
    public void closeInputView() {
        if (mIlvComment.getVisibility() == View.VISIBLE) {
            mIlvComment.setVisibility(View.GONE);
            DeviceUtils.hideSoftKeyboard(getActivity(), mIlvComment.getEtContent());
        }
        mVShadow.setVisibility(View.GONE);
    }

    public void showCommentView() {
        // 评论
        mIlvComment.setVisibility(View.VISIBLE);
        mIlvComment.setSendButtonVisiable(true);
        mIlvComment.getFocus();
        mVShadow.setVisibility(View.VISIBLE);
        DeviceUtils.showSoftKeyboard(getActivity(), mIlvComment.getEtContent());
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        if (mListDatas.get(position).getUser_id() == AppApplication.getmCurrentLoginAuth().getUser_id()) {// 过滤自己的

        } else {
            mReplyUserId = mListDatas.get(position).getUser_id();
            mCurrentPostion = position;
            showCommentView();
            String contentHint = getString(R.string.reply, mListDatas.get(position).getCommentUserInfo().getName());
            mIlvComment.setEtContentHint(contentHint);
        }
    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        return false;
    }


}
