package com.zhiyicx.thinksnsplus.modules.home.message.messagelike;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleTransform;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.imsdk.entity.Conversation;
import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.chat.ChatActivity;
import com.zhiyicx.thinksnsplus.modules.chat.ChatFragment;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.UserInfoActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe 消息赞
 * @Author Jungle68
 * @Date 2017/1/17
 * @Contact master.jungle68@gmail.com
 */
public class MessageLikeFragment extends TSListFragment<MessageLikeContract.Presenter, MessageItemBean> implements MessageLikeContract.View {

    private ImageLoader mImageLoader;

    public MessageLikeFragment() {
    }

    public static MessageLikeFragment newInstance() {
        MessageLikeFragment fragment = new MessageLikeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.like);
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
    }

    @Override
    protected float getItemDecorationSpacing() {
        return 0;
    }

    @Override
    protected void initData() {
        mImageLoader = AppApplication.AppComponentHolder.getAppComponent().imageLoader();
        initCommentAndLike(mListDatas);
        refreshData();
    }

    @Override
    protected CommonAdapter<MessageItemBean> getAdapter() {
        return new CommonAdapter<MessageItemBean>(getActivity(), R.layout.item_message_like_list, mListDatas) {
            @Override
            protected void convert(ViewHolder holder, MessageItemBean messageItemBean, int position) {
                setItemData(holder, messageItemBean, position);
            }

        };
    }

    /**
     * 评论的和点赞的数据
     */
    private void initCommentAndLike(List<MessageItemBean> messageItemBeen) {
        UserInfoBean testUserinfo = new UserInfoBean();
        testUserinfo.setAvatar("http://image.xinmin.cn/2017/01/11/bedca80cdaa44849a813e7820fff8a26.jpg");
        testUserinfo.setName("颤三");
        testUserinfo.setUser_id(12l);
        MessageItemBean commentItem = new MessageItemBean();
        commentItem.setUserInfo(testUserinfo);
        Conversation commentMessage = new Conversation();
        Message message=new Message();
        message.setTxt("默默的小红大家来到江苏高考加分临时价格来看大幅减少了国家法律的世界观浪费时间管理方式的建立各级地方楼市困局"
                + getString(R.string.comment_me));
        commentMessage.setLast_message(message);
        commentMessage.setLast_message_time(System.currentTimeMillis());
        commentItem.setConversation(commentMessage);
        commentItem.setUnReadMessageNums(Math.round(15));
        messageItemBeen.add(commentItem);

        MessageItemBean likedmessageItemBean = new MessageItemBean();
        likedmessageItemBean.setUserInfo(testUserinfo);
        Conversation likeMessage = new Conversation();
        Message message1=new Message();
        message1.setTxt("一叶之秋、晴天色"
                + getString(R.string.like_me));
        likeMessage.setLast_message(message1);
        likeMessage.setLast_message_time(System.currentTimeMillis());
        likedmessageItemBean.setConversation(likeMessage);
        likedmessageItemBean.setUnReadMessageNums(Math.round(15));
        messageItemBeen.add(likedmessageItemBean);
        MessageItemBean test = new MessageItemBean();

        test.setUserInfo(testUserinfo);
        Message testMessage = new Message();
        testMessage.setTxt("一叶之秋、晴天色"
                + getString(R.string.like_me));
        testMessage.setCreate_time(System.currentTimeMillis());
        test.setConversation(likeMessage);
        test.setUnReadMessageNums((int) (Math.random() * 10));
        messageItemBeen.add(test);
    }

    /**
     * 设置item 数据
     *
     * @param holder          控件管理器
     * @param messageItemBean 当前数据
     * @param position        当前数据位置
     */

    private void setItemData(ViewHolder holder, final MessageItemBean messageItemBean, int position) {
        if (position == mListDatas.size() - 1) {
            holder.setVisible(R.id.v_bottom_line, View.GONE);
        } else {
            holder.setVisible(R.id.v_bottom_line, View.VISIBLE);
        }
        mImageLoader.loadImage(getContext(), GlideImageConfig.builder()
                .url(messageItemBean.getUserInfo().getAvatar())
                .transformation(new GlideCircleTransform(getContext()))
                .imagerView((ImageView) holder.getView(R.id.iv_headpic))
                .build());
        if (position % 2 == 0) {
            holder.setVisible(R.id.tv_deatil, View.GONE);
            holder.setVisible(R.id.iv_detail_image, View.VISIBLE);
            mImageLoader.loadImage(getContext(), GlideImageConfig.builder()
                    .url(messageItemBean.getUserInfo().getAvatar())
                    .imagerView((ImageView) holder.getView(R.id.iv_detail_image))
                    .build());
        } else {
            holder.setVisible(R.id.iv_detail_image, View.GONE);
            holder.setVisible(R.id.tv_deatil, View.VISIBLE);
            holder.setText(R.id.tv_deatil, messageItemBean.getConversation().getLast_message().getTxt());
        }

        holder.setText(R.id.tv_name, messageItemBean.getUserInfo().getName());
        holder.setText(R.id.tv_time, TimeUtils.getTimeFriendlyNormal(messageItemBean.getConversation().getLast_message_time()));
        // 响应事件
        RxView.clicks(holder.getView(R.id.tv_name))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        toUserCenter();
                    }
                });
        RxView.clicks(holder.getView(R.id.iv_headpic))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        toUserCenter();
                    }
                });
        RxView.clicks(holder.getConvertView())
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        toChat(messageItemBean);
                    }
                });

        RxView.clicks(holder.getView(R.id.iv_detail_image))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        toChat(messageItemBean);
                    }
                });
        RxView.clicks(holder.getView(R.id.tv_deatil))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        toChat(messageItemBean);
                    }
                });
    }

    /**
     * 进入聊天页
     *
     * @param messageItemBean
     */
    private void toChat(MessageItemBean messageItemBean) {
        Intent to = new Intent(getActivity(), ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ChatFragment.BUNDLE_MESSAGEITEMBEAN, messageItemBean);
        to.putExtras(bundle);
        startActivity(to);
    }

    /**
     * 前往用户个人中心
     */
    private void toUserCenter() {
        Intent to = new Intent(getActivity(), UserInfoActivity.class);
        startActivity(to);
    }

    @Override
    public void setPresenter(MessageLikeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {
        mRefreshlayout.setRefreshing(false);
    }

    @Override
    public void showMessage(String message) {

    }
}
