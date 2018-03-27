package com.zhiyicx.thinksnsplus.modules.tb.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideRoundTransform;
import com.zhiyicx.baseproject.widget.BadgeView;
import com.zhiyicx.baseproject.widget.imageview.SquareImageView;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.data.beans.TbMessageBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.home.HomeFragment;
import com.zhiyicx.thinksnsplus.modules.tb.detail.MerchainMessagelistActivity;
import com.zhiyicx.thinksnsplus.modules.tb.search.SearchMechanismUserActivity;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import javax.inject.Inject;

import static com.zhiyicx.thinksnsplus.data.beans.TbMessageBean.FEED;
import static com.zhiyicx.thinksnsplus.data.beans.TbMessageBean.NEWS;
import static com.zhiyicx.thinksnsplus.modules.home.HomeFragment.PAGE_CONTACT;

/**
 * Created by lx on 2018/3/24.
 */

public class MessageListFragment extends TSListFragment<MessageListContract.Presenter, TbMessageBean>
        implements MessageListContract.View, MultiItemTypeAdapter.OnItemClickListener {

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
    protected int setLeftImg() {
        return R.mipmap.ic_mail_list_click;
    }

    @Override
    protected void setLeftClick() {
        ((HomeFragment) getParentFragment()).setCurrenPageToContact(PAGE_CONTACT);
    }

    @Override
    protected boolean isRefreshEnable() {
        return false;
    }

    @Override
    protected boolean isLoadingMoreEnable() {
        return false;
    }

    @Override
    protected int setRightImg() {
        return R.mipmap.ic_search_a_click;
    }

    @Override
    protected void setRightClick() {
        startActivity(new Intent(mActivity, SearchMechanismUserActivity.class));
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
    protected boolean setUseSatusbar() {
        return true;
    }

    @Override
    protected boolean setUseStatusView() {
        return true;
    }

    @Override
    protected void initData() {
        super.initData();
        refreshData();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateList();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        updateList();
    }

    private void updateList() {
        if (getUserVisibleHint()&&getActivity() != null && mPresenter != null) {
            mPresenter.requestCacheData(DEFAULT_PAGE_MAX_ID, false);
        }

    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        CommonAdapter adapter = new CommonAdapter<TbMessageBean>(mActivity, R.layout.item_tbmessage, mListDatas) {
            @Override
            protected void convert(ViewHolder holder, TbMessageBean tbMessageBean, int position) {
                holder.setBackgroundRes(R.id.ll_container
                        , tbMessageBean.getMIsPinned() ? R.drawable.selector_bg_item_message_pinned : R.drawable.selector_bg_item_message
                );
                switch (tbMessageBean.getChannel()) {
                    case FEED: {
                        DynamicDetailBeanV2 feed = tbMessageBean.getFeed();
                        UserInfoBean userInfoBean = feed.getUserInfoBean();
                        if (userInfoBean.getAvatar() != null) {
                            ImageView squareImageView = holder.getView(R.id.iv_message_headpic);
                            ImageUtils.loadRounRectImage(squareImageView,userInfoBean.getAvatar(),5);

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
                            ImageView squareImageView = holder.getView(R.id.iv_message_headpic);
                            ImageUtils.loadRounRectImage(squareImageView,userInfoBean.getAvatar(),5);
                        }
                        holder.setText(R.id.name, userInfoBean.getName());
                        if (TextUtils.isEmpty(news.getTitle())) {
                            holder.setText(R.id.content, news.getTitle());
                        } else {
                            holder.setText(R.id.content, news.getSubject());
                        }
                        holder.setText(R.id.tv_tbtime, TimeUtils.getTimeFriendlyNormal(news.getCreated_at()));
                        break;
                    }
                    default:
                        break;
                }
                ((BadgeView) holder.getView(R.id.tv_read_tip)).setText("");
                ((BadgeView) holder.getView(R.id.tv_read_tip)).setVisibility(tbMessageBean.getMIsRead() ? View.GONE : View.VISIBLE);

            }
        };
        adapter.setOnItemClickListener(this);
        return adapter;
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        // 进入公众号
        Intent intent = new Intent(getActivity(), MerchainMessagelistActivity.class);
        Bundle bundle = new Bundle();
        UserInfoBean userInfoBean;
        switch (mListDatas.get(position).getChannel()) {
            case FEED:
                userInfoBean = mListDatas.get(position).getFeed().getUserInfoBean();
                break;
            case NEWS:
                userInfoBean = mListDatas.get(position).getNews().getUser();
                break;
            default:
                userInfoBean = mListDatas.get(position).getFeed().getUserInfoBean();
        }
        bundle.putSerializable(MerchainMessagelistActivity.BUNDLE_USER, userInfoBean);
        intent.putExtras(bundle);
        startActivity(intent);
        mListDatas.get(position).setMIsRead(true);
        mPresenter.updateMessageReadStaus(mListDatas.get(position));
    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        return false;
    }
}
