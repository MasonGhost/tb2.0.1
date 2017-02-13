package com.zhiyicx.thinksnsplus.modules.home.message;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleTransform;
import com.zhiyicx.baseproject.widget.BadgeView;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.imsdk.entity.ChatRoomContainer;
import com.zhiyicx.imsdk.entity.Conversation;
import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.imsdk.manage.ChatClient;
import com.zhiyicx.imsdk.manage.listener.ImMsgReceveListener;
import com.zhiyicx.imsdk.manage.listener.ImStatusListener;
import com.zhiyicx.imsdk.manage.listener.ImTimeoutListener;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.chat.ChatActivity;
import com.zhiyicx.thinksnsplus.modules.chat.ChatFragment;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.UserInfoActivity;
import com.zhiyicx.thinksnsplus.modules.home.message.messagecomment.MessageCommentActivity;
import com.zhiyicx.thinksnsplus.modules.home.message.messagelike.MessageLikeActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.functions.Action1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe 消息页面
 * @Author Jungle68
 * @Date 2017/1/5
 * @Contact master.jungle68@gmail.com
 */
public class MessageFragment extends TSListFragment<MessageContract.Presenter, MessageItemBean> implements MessageContract.View, ImMsgReceveListener, ImStatusListener, ImTimeoutListener {
    private static final int ITEM_TYPE_COMMNETED = 0;
    private static final int ITEM_TYPE_LIKED = 1;

    @Inject
    protected MessagePresenter mPresenter;
    private ImageLoader mImageLoader;
    private List<MessageItemBean> mMessageItemBeen = new ArrayList<>();


    /**
     * IM 聊天
     */
    private ChatClient mChatClient;

    public static MessageFragment newInstance() {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int setLeftImg() {
        return 0;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.message);
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);

//        HeaderAndFooterWrapper mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(messageListAdapter);
//        TextView t2 = new TextView(getContext());
//        t2.setText("Header 2");
//        t2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ToastUtils.showToast("nihao header");
//            }
//        });
//        mHeaderAndFooterWrapper.addHeaderView(t2);
//        mRvMessageList.setAdapter(mHeaderAndFooterWrapper);
//        mHeaderAndFooterWrapper.notifyDataSetChanged();
    }

    @Override
    protected void initData() {
//        DaggerMessageComponent
//                .builder()
//                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
//                .messagePresenterModule(new MessagePresenterModule(this))
//                .build()
//                .inject(this);
        mImageLoader = AppApplication.AppComponentHolder.getAppComponent().imageLoader();
        initCommentAndLike(mMessageItemBeen);
        refreshData();
        initIM();

    }

    @Override
    protected CommonAdapter getAdapter() {
        return new CommonAdapter<MessageItemBean>(getActivity(), R.layout.item_message_list, mMessageItemBeen) {
            @Override
            protected void convert(ViewHolder holder, MessageItemBean messageItemBean, int position) {
                setItemData(holder, messageItemBean, position);
            }

        };
    }

    @Override
    protected boolean insertOrUpdateData(@NotNull List data) {
        return false;
    }

    @Override
    protected List getCacheData(int maxId) {
        return null;
    }

    private void initIM() {
        mChatClient = new ChatClient(getActivity());
        mChatClient.setImMsgReceveListener(this);
        mChatClient.setImStatusListener(this);
        mChatClient.setImTimeoutListener(this);
    }

    /**
     * 评论的和点赞的数据
     */
    private void initCommentAndLike(List<MessageItemBean> messageItemBeen) {
        MessageItemBean commentItem = new MessageItemBean();
        Message commentMessage = new Message();
        commentMessage.setTxt("默默的小红大家来到江苏高考加分临时价格来看大幅减少了国家法律的世界观浪费时间管理方式的建立各级地方楼市困局"
                + getString(R.string.comment_me));
        commentMessage.setCreate_time(System.currentTimeMillis());
        commentItem.setLastMessage(commentMessage);
        commentItem.setUnReadMessageNums(Math.round(15));
        messageItemBeen.add(commentItem);

        MessageItemBean likedmessageItemBean = new MessageItemBean();
        Message likeMessage = new Message();
        likeMessage.setTxt("一叶之秋、晴天色"
                + getString(R.string.like_me));
        UserInfoBean userinfo = new UserInfoBean();
        userinfo.setUserIcon("http://192.168.10.222/i.php");
        likedmessageItemBean.setUserInfo(userinfo);
        likeMessage.setCreate_time(System.currentTimeMillis());
        likedmessageItemBean.setLastMessage(likeMessage);
        likedmessageItemBean.setUnReadMessageNums(Math.round(15));
        messageItemBeen.add(likedmessageItemBean);
        for (int i = 0; i < 10; i++) {
            MessageItemBean test = new MessageItemBean();
            UserInfoBean testUserinfo = new UserInfoBean();
            testUserinfo.setUserIcon("http://192.168.10.222/i.php");
            testUserinfo.setName("颤三");
            testUserinfo.setUser_id((long) (10 + i));
            test.setUserInfo(testUserinfo);
            Message testMessage = new Message();
            testMessage.setTxt("一叶之秋、晴天色" + i
                    + getString(R.string.like_me));
            testMessage.setCreate_time(System.currentTimeMillis());
            test.setLastMessage(likeMessage);
            test.setUnReadMessageNums((int) (Math.random() * 10));
            messageItemBeen.add(test);
        }

    }

    /**
     * 设置item 数据
     *
     * @param holder          控件管理器
     * @param messageItemBean 当前数据
     * @param position        当前数据位置
     */

    private void setItemData(ViewHolder holder, final MessageItemBean messageItemBean, int position) {
        switch (position) {
            case ITEM_TYPE_COMMNETED:// 评论图标
                mImageLoader.loadImage(getContext(), GlideImageConfig.builder()
                        .resourceId(R.mipmap.ico_message_comment)
                        .imagerView((ImageView) holder.getView(R.id.iv_headpic)).build()
                );
                holder.setText(R.id.tv_name, getString(R.string.critical));
                setViewEnable(holder, false);
                RxView.clicks(holder.getConvertView())
                        .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                        .subscribe(new Action1<Void>() {
                            @Override
                            public void call(Void aVoid) {
                                toCommentList();
                            }
                        });
                break;
            case ITEM_TYPE_LIKED:// 点赞图标
                mImageLoader.loadImage(getContext(), GlideImageConfig.builder()
                        .resourceId(R.mipmap.ico_message_good)
                        .imagerView((ImageView) holder.getView(R.id.iv_headpic)).build()
                );

                holder.setText(R.id.tv_name, getString(R.string.liked));
                setViewEnable(holder, false);
                RxView.clicks(holder.getConvertView())
                        .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                        .subscribe(new Action1<Void>() {
                            @Override
                            public void call(Void aVoid) {
                                toLikeList();
                            }
                        });
                break;

            default:// 网络头像
                mImageLoader.loadImage(getContext(), GlideImageConfig.builder()
                        .url(messageItemBean.getUserInfo().getUserIcon())
                        .transformation(new GlideCircleTransform(getContext()))
                        .imagerView((ImageView) holder.getView(R.id.iv_headpic))
                        .build()
                );
                setViewEnable(holder, true);
                holder.setText(R.id.tv_name, messageItemBean.getUserInfo().getName());
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
        }

        holder.setText(R.id.tv_content, messageItemBean.getLastMessage().getTxt());
        holder.setText(R.id.tv_time, ConvertUtils.millis2FitTimeSpan(messageItemBean.getLastMessage().getCreate_time(), 3));
        ((BadgeView) holder.getView(R.id.tv_tip)).setBadgeCount(messageItemBean.getUnReadMessageNums());


    }

    private void setViewEnable(ViewHolder holder, boolean isEnable) {
        holder.getView(R.id.tv_name).setEnabled(isEnable);
        holder.getView(R.id.iv_headpic).setEnabled(isEnable);
    }

    /**
     * 进入聊天页
     *
     * @param messageItemBean
     */
    private void toChat(MessageItemBean messageItemBean) {
        Intent to = new Intent(getActivity(), ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ChatFragment.BUNDLE_USERID, String.valueOf(messageItemBean.getUserInfo().getUser_id()));
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

    /**
     * 前往评论列表
     */
    private void toCommentList() {
        Intent to = new Intent(getActivity(), MessageCommentActivity.class);
        startActivity(to);
    }

    /**
     * 前往点赞列表
     */
    private void toLikeList() {
        Intent to = new Intent(getActivity(), MessageLikeActivity.class);
        startActivity(to);
    }

    // Fragment 注入 ,不需要该方法
    @Override
    public void setPresenter(MessageContract.Presenter presenter) {
        LogUtils.d("-------------------------------------->"+presenter);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {

    }

    /*******************************************  聊天相关回调  *********************************************/


    /**
     * @param message
     */

    @Override
    public void onMessageReceived(Message message) {

    }

    @Override
    public void onMessageACKReceived(Message message) {

    }

    @Override
    public void onConversationJoinACKReceived(ChatRoomContainer chatRoomContainer) {

    }

    @Override
    public void onConversationLeaveACKReceived(ChatRoomContainer chatRoomContainer) {

    }

    @Override
    public void onConversationMCACKReceived(List<Conversation> conversations) {

    }

    @Override
    public void synchronousInitiaMessage(int limit) {

    }

    @Override
    public void onConnected() {

    }

    @Override
    public void onDisconnect(int code, String reason) {

    }

    @Override
    public void onError(Exception error) {

    }

    @Override
    public void onMessageTimeout(Message message) {

    }

    @Override
    public void onConversationJoinTimeout(int roomId) {

    }

    @Override
    public void onConversationLeaveTimeout(int roomId) {

    }

    @Override
    public void onConversationMcTimeout(List<Integer> roomIds) {

    }

}
