package com.zhiyicx.thinksnsplus.modules.home;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.impl.imageloader.GlideImageConfig;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.imageloader.core.ImageLoader;
import com.zhiyicx.common.utils.recycleviewdecoration.LinearDecoration;
import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.MessageItem;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @Describe 消息页面
 * @Author Jungle68
 * @Date 2017/1/5
 * @Contact master.jungle68@gmail.com
 */
public class MessageFragment extends TSFragment {
    private static final int ITEM_TYPE_COMMNETED = 0;
    private static final int ITEM_TYPE_LIKED = 1;

    @BindView(R.id.rv_message_list)
    RecyclerView mRvMessageList;

    private ImageLoader mImageLoader;
    private List<MessageItem> mMessageItems;

    public MessageFragment() {
    }

    public static MessageFragment newInstance() {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    protected int setToolBarBackgroud() {
        return R.color.white;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.message);
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected void initView(View rootView) {
        mToolbarCenter.setTextColor(ContextCompat.getColor(getContext(), R.color.important_for_content));
    }


    @Override
    protected void initData() {
        mImageLoader = AppApplication.AppComponentHolder.getAppComponent().imageLoader();
        mMessageItems = new ArrayList<>();
        initCommentAndLike(mMessageItems);
        mRvMessageList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvMessageList.addItemDecoration(new LinearDecoration(0, ConvertUtils.dp2px(getContext(), 10f), 0, 0));//设置Item的间隔
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRvMessageList.setHasFixedSize(true);
        mRvMessageList.setItemAnimator(new DefaultItemAnimator());//设置动画
        mRvMessageList.setAdapter(new CommonAdapter<MessageItem>(getActivity(), R.layout.item_message_list, mMessageItems) {
            @Override
            protected void convert(ViewHolder holder, MessageItem messageItem, int position) {
                setItemData(holder, messageItem, position);
            }

        });
    }

    /**
     * 评论的和点赞的数据
     */
    private void initCommentAndLike(List<MessageItem> messageItems) {
        MessageItem commentItem = new MessageItem();
        Message commentMessage = new Message();
        commentMessage.setTxt("默默的小红大家来到江苏高考加分临时价格来看大幅减少了国家法律的世界观浪费时间管理方式的建立各级地方楼市困局"
                + getString(R.string.comment_me));
        commentMessage.setCreate_time(System.currentTimeMillis());
        commentItem.setLastMessage(commentMessage);
        commentItem.setUnReadMessageNums(Math.round(15));
        messageItems.add(commentItem);

        MessageItem likedmessageItem = new MessageItem();
        Message likeMessage = new Message();
        likeMessage.setTxt("一叶之秋、晴天色"
                + getString(R.string.like_me));
        likeMessage.setCreate_time(System.currentTimeMillis());
        likedmessageItem.setLastMessage(likeMessage);
        likedmessageItem.setUnReadMessageNums(Math.round(15));
        messageItems.add(likedmessageItem);
        MessageItem test = new MessageItem();
        UserInfoBean testUserinfo = new UserInfoBean();
        testUserinfo.setUserIcon("http://image.xinmin.cn/2017/01/11/bedca80cdaa44849a813e7820fff8a26.jpg");
        testUserinfo.setUserName("颤三");
        test.setUserInfo(testUserinfo);
        Message testMessage = new Message();
        testMessage.setTxt("一叶之秋、晴天色"
                + getString(R.string.like_me));
        testMessage.setCreate_time(System.currentTimeMillis());
        test.setLastMessage(likeMessage);
        test.setUnReadMessageNums(Math.round(15));
        messageItems.add(test);
    }

    /**
     * 设置item 数据
     *
     * @param holder      控件管理器
     * @param messageItem 当前数据
     * @param position    当前数据位置
     */

    private void setItemData(ViewHolder holder, MessageItem messageItem, int position) {
        switch (position) {
            case ITEM_TYPE_COMMNETED:// 评论图标
                mImageLoader.loadImage(getContext(), GlideImageConfig.builder()
                        .resourceId(R.mipmap.login_ico_copeneye)
                        .imagerView((ImageView) holder.getView(R.id.iv_headpic)).build()
                );
                holder.setText(R.id.tv_name, getString(R.string.critical));
                break;
            case ITEM_TYPE_LIKED:// 点赞图标
                mImageLoader.loadImage(getContext(), GlideImageConfig.builder()
                        .resourceId(R.mipmap.login_ico_copeneye)
                        .imagerView((ImageView) holder.getView(R.id.iv_headpic)).build()
                );
                holder.setText(R.id.tv_name, getString(R.string.liked));
                break;

            default:// 网络头像
                mImageLoader.loadImage(getContext(), GlideImageConfig.builder()
                        .url(messageItem.getUserInfo().getUserIcon())
                        .imagerView((ImageView) holder.getView(R.id.iv_headpic)).build()
                );
                holder.setText(R.id.tv_name, messageItem.getUserInfo().getUserName());
        }

        holder.setText(R.id.tv_content, messageItem.getLastMessage().getTxt());
        holder.setText(R.id.tv_time, ConvertUtils.millis2FitTimeSpan(messageItem.getLastMessage().getCreate_time(), 3));
        holder.setText(R.id.tv_tip, String.valueOf(messageItem.getUnReadMessageNums()));
    }
}
