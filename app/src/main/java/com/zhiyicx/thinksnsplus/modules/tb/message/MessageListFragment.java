package com.zhiyicx.thinksnsplus.modules.tb.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.imageview.SquareImageView;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.HintSideBarUserBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.data.beans.TbMessageBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.tb.contract.DaggerContractListComponent;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import javax.inject.Inject;

import static com.zhiyicx.thinksnsplus.data.beans.TbMessageBean.FEED;
import static com.zhiyicx.thinksnsplus.data.beans.TbMessageBean.NEWS;

/**
 * Created by lx on 2018/3/24.
 */

public class MessageListFragment extends TSListFragment<MessageListContract.Presenter, TbMessageBean>
        implements MessageListContract.View{

    @Inject
    MessageListPresenter mMessageListPresenter;

    public static MessageListFragment newInstance() {
        MessageListFragment messageListFragment = new MessageListFragment();
        return messageListFragment;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        setCenterTextColor(R.color.white);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerMessageListComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .messageListPresenterModule(new MessageListPresenterModule(this))
                .build().inject(this);
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.message);
    }

    @Override
    protected int setToolBarBackgroud() {
        return R.color.themeColor;
    }

    @Override
    protected void setCenterTextColor(int resId) {
        super.setCenterTextColor(resId);
    }

    @Override
    protected boolean showToolbar() {
        return true;
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    protected boolean setUseSatusbar() {
        return false;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        CommonAdapter adapter = new CommonAdapter<TbMessageBean>(mActivity, R.layout.item_tbmessage, mListDatas) {
            @Override
            protected void convert(ViewHolder holder, TbMessageBean tbMessageBean, int position) {
                switch (tbMessageBean.getChannel()){
                    case FEED: {
                        DynamicDetailBeanV2 feed = tbMessageBean.getFeed();
                        UserInfoBean userInfoBean = feed.getUserInfoBean();
                        if (userInfoBean.getAvatar() != null) {
                            SquareImageView squareImageView = holder.getView(R.id.iv_message_headpic);
                            Glide.with(mActivity)
                                    .load(userInfoBean.getAvatar())
                                    .placeholder(R.drawable.shape_default_image)
                                    .placeholder(R.drawable.shape_default_error_image)
                                    .centerCrop()
                                    .into(squareImageView);
                        }
                        holder.setText(R.id.name, userInfoBean.getName());
                        holder.setText(R.id.content, feed.getFeed_content());
                        holder.setText(R.id.tv_tbtime, TimeUtils.getTimeFriendlyNormal(feed.getCreated_at()));
                      break;
                    }
                    case NEWS: {
                        InfoListDataBean news = tbMessageBean.getNews();
                        UserInfoBean userInfoBean = news.getUser();
                        if (userInfoBean.getAvatar() != null) {
                            SquareImageView squareImageView = holder.getView(R.id.iv_message_headpic);
                            Glide.with(mActivity)
                                    .load(userInfoBean.getAvatar())
                                    .placeholder(R.drawable.shape_default_image)
                                    .placeholder(R.drawable.shape_default_error_image)
                                    .centerCrop()
                                    .into(squareImageView);
                        }
                        holder.setText(R.id.name, userInfoBean.getName());
                        if(TextUtils.isEmpty(news.getText_content())){
                            holder.setText(R.id.content, news.getContent());
                        } else {
                            holder.setText(R.id.content, news.getText_content());
                        }
                        holder.setText(R.id.tv_tbtime, TimeUtils.getTimeFriendlyNormal(news.getCreated_at()));
                        break;
                    }
                    default:
                        break;
                }
            }
        };
        return adapter;
    }
}
