package com.zhiyicx.thinksnsplus.modules.channel.detail;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.InputLimitView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.ChannelSubscripBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.modules.channel.detail.adapter.ItemChannelDetailHeader;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListBaseItem;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForEightImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForFiveImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForFourImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForNineImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForOneImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForSevenImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForSixImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForThreeImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForTwoImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForZeroImage;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/11
 * @contact email:450127106@qq.com
 */

public class ChannelDetailFragment extends TSListFragment<ChannelDetailContract.Presenter, DynamicBean> implements ChannelDetailContract.View {

    @BindView(R.id.tv_top_tip_text)
    TextView mTvTopTipText;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_channel_name)
    TextView mTvChannelName;
    @BindView(R.id.iv_subscrib_btn)
    ImageView mIvSubscribBtn;
    @BindView(R.id.rl_toolbar_container)
    RelativeLayout mRlToolbarContainer;
    @BindView(R.id.v_horizontal_line)
    View mVHorizontalLine;
    @BindView(R.id.ll_toolbar_container_parent)
    LinearLayout mLlToolbarContainerParent;
    @BindView(R.id.v_shadow)
    View mVShadow;
    @BindView(R.id.ilv_comment)
    InputLimitView mIlvComment;
    @BindView(R.id.btn_send_dynamic)
    FloatingActionButton mBtnSendDynamic;
    public static final String CHANNEL_HEADER_INFO_DATA = "channel_header_info_data";
    private ItemChannelDetailHeader mItemChannelDetailHeader;
    private ChannelSubscripBean mChannelSubscripBean;// 从上一个页面传过来的频道信息

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mItemChannelDetailHeader = new ItemChannelDetailHeader(getActivity(), mRvList, mHeaderAndFooterWrapper, mLlToolbarContainerParent);
        mItemChannelDetailHeader.initHeaderView(true);
    }

    @Override
    protected void initData() {
        mRefreshlayout.setRefreshEnabled(isRefreshEnable());
        mChannelSubscripBean = getArguments().getParcelable(CHANNEL_HEADER_INFO_DATA);
        mItemChannelDetailHeader.initHeaderViewData(mChannelSubscripBean);
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean setUseSatusbar() {
        return true;
    }

    @Override
    protected boolean isRefreshEnable() {
        return false;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        MultiItemTypeAdapter adapter = new MultiItemTypeAdapter(getContext(), mListDatas);
        setAdapter(adapter, new DynamicListItemForZeroImage(getContext()));
        setAdapter(adapter, new DynamicListItemForOneImage(getContext()));
        setAdapter(adapter, new DynamicListItemForTwoImage(getContext()));
        setAdapter(adapter, new DynamicListItemForThreeImage(getContext()));
        setAdapter(adapter, new DynamicListItemForFourImage(getContext()));
        setAdapter(adapter, new DynamicListItemForFiveImage(getContext()));
        setAdapter(adapter, new DynamicListItemForSixImage(getContext()));
        setAdapter(adapter, new DynamicListItemForSevenImage(getContext()));
        setAdapter(adapter, new DynamicListItemForEightImage(getContext()));
        setAdapter(adapter, new DynamicListItemForNineImage(getContext()));
        return adapter;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_channel_detail;
    }

    public static ChannelDetailFragment newInstance(Bundle bundle) {
        ChannelDetailFragment channelDetailFragment = new ChannelDetailFragment();
        channelDetailFragment.setArguments(bundle);
        return channelDetailFragment;
    }

    private void setAdapter(MultiItemTypeAdapter adapter, DynamicListBaseItem dynamicListBaseItem) {
       /* dynamicListBaseItem.setOnImageClickListener(this);
        dynamicListBaseItem.setOnUserInfoClickListener(this);
        dynamicListBaseItem.setOnMenuItemClickLisitener(this);
        dynamicListBaseItem.setOnReSendClickListener(this);
        dynamicListBaseItem.setOnMoreCommentClickListener(this);
        dynamicListBaseItem.setOnCommentClickListener(this);
        dynamicListBaseItem.setOnCommentStateClickListener(this);*/
        adapter.addItemViewDelegate(dynamicListBaseItem);
    }

}
