package com.zhiyicx.thinksnsplus.modules.q_a.detail.topic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.klinker.android.link_builder.Link;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.widget.textview.CenterImageSpan;
import com.zhiyicx.baseproject.widget.textview.CircleImageDrawable;
import com.zhiyicx.common.base.BaseApplication;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QATopicBean;
import com.zhiyicx.thinksnsplus.modules.information.adapter.ScaleTransitionPagerTitleView;
import com.zhiyicx.thinksnsplus.widget.HorizontalStackIconView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import net.lucode.hackware.magicindicator.FragmentContainerHelper;
import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import butterknife.BindView;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.modules.q_a.detail.topic.TopicDetailActivity.BUNDLE_TOPIC_BEAN;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/14
 * @contact email:648129313@qq.com
 */

public class TopicDetailFragment extends TSListFragment<TopicDetailContract.Presenter, QAListInfoBean>
        implements TopicDetailContract.View {

    private static final String TYPE_ALL = "all";
    private static final String TYPE_NEW = "new";
    private static final String TYPE_HOT = "hot";
    private static final String TYPE_REWARD = "reward";
    private static final String TYPE_EXCELLENT = "excellent";

    @BindView(R.id.iv_topic_cover)
    ImageView mIvTopicCover;
    @BindView(R.id.tv_topic_name)
    TextView mTvTopicName;
    @BindView(R.id.tv_topic_feed_count)
    TextView mTvTopicFeedCount;
    @BindView(R.id.tv_topic_change_follow)
    CheckBox mTvTopicChangeFollow;
    @BindView(R.id.tv_topic_description)
    TextView mTvTopicDescription;
    @BindView(R.id.expert_list)
    HorizontalStackIconView mExpertList;
    @BindView(R.id.mg_indicator)
    MagicIndicator mMgIndicator;

    private CommonNavigator mCommonNavigator;
    private QATopicBean mQaTopicBean;

    private String mCurrentType = TYPE_NEW;
    private List<String> mTypeList;
    private FragmentContainerHelper mFragmentContainerHelper = new FragmentContainerHelper();

    public TopicDetailFragment instance(Bundle bundle) {
        TopicDetailFragment fragment = new TopicDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        initMagicIndicator();
        initListener();
    }

    @Override
    protected void initData() {
        super.initData();
        mTypeList = new ArrayList<>();
        mTypeList.add(TYPE_NEW);
        mTypeList.add(TYPE_EXCELLENT);
        mTypeList.add(TYPE_REWARD);
        mTypeList.add(TYPE_HOT);
        mTypeList.add(TYPE_ALL);
        mFragmentContainerHelper.handlePageSelected(0, false);
//        mQaTopicBean = (QATopicBean) getArguments().getSerializable(BUNDLE_TOPIC_BEAN);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        CommonAdapter adapter = new CommonAdapter<QAListInfoBean>(getActivity(), R.layout.item_qa_content, mListDatas) {
            @Override
            protected void convert(ViewHolder holder, QAListInfoBean o, int position) {
                holder.setText(R.id.item_info_title, "火星很危险，快回地球去吧");
                holder.setText(R.id.item_info_time, "一周前");
                holder.setText(R.id.item_info_count, String.format(Locale.getDefault(), getString(R.string.qa_show_topic_followed_reward)
                        , 200, 40, 18f));
                ConvertUtils.stringLinkConvert(holder.getTextView(R.id.item_info_count), setLinks(null));
                TextView contentTextView = holder.getView(R.id.item_info_hotcomment);
                String content = "火星很危险，快回地球去吧火星很危险，快回地球去吧火星很危险，" +
                        "快回地球去吧火星很危险，" +
                        "快回地球去吧火星很危险，快回地球去吧火星很危险，快回地球去吧火星很危险，快回地球去吧火星很危险，" +
                        "快回地球去吧火星很危险，快回地球去吧火星很危险，快回地球去吧";
                Bitmap newBmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), com.zhiyicx.baseproject.R.mipmap.ico_potoablum),
                        contentTextView.getLineHeight(), contentTextView.getLineHeight(), true);
                Drawable headImage = new CircleImageDrawable(newBmp);
                headImage.setBounds(8, 0, 8 + contentTextView.getLineHeight(), contentTextView.getLineHeight());
                ImageSpan imgSpan = new CenterImageSpan(headImage, true);
                SpannableString spannableString = SpannableString.valueOf("T" + content);
                spannableString.setSpan(imgSpan, 0, 1, Spannable
                        .SPAN_EXCLUSIVE_EXCLUSIVE);
                contentTextView.setText(spannableString);

            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        return adapter;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_topic_detail;
    }

    @Override
    protected int setRightImg() {
        return R.mipmap.topbar_share_white;
    }

    private List<Link> setLinks(QAListInfoBean listInfoBean) {
        List<Link> links = new ArrayList<>();
        Link followCountLink = new Link("200").setTextColor(ContextCompat.getColor(getContext(), R.color
                .themeColor))
                .setTextColorOfHighlightedLink(ContextCompat.getColor(getContext(), R.color
                        .general_for_hint))
                .setHighlightAlpha(.8f)
                .setUnderlined(false);
        links.add(followCountLink);

        Link answerCountLink = new Link("40").setTextColor(ContextCompat.getColor(getContext(), R.color
                .themeColor))
                .setTextColorOfHighlightedLink(ContextCompat.getColor(getContext(), R.color
                        .general_for_hint))
                .setHighlightAlpha(.8f)
                .setUnderlined(false);
        links.add(answerCountLink);

        Link rewardMoneyLink = new Link("￥18.0").setTextColor(ContextCompat.getColor(getContext(), R.color
                .withdrawals_item_enable))
                .setTextColorOfHighlightedLink(ContextCompat.getColor(getContext(), R.color
                        .general_for_hint))
                .setHighlightAlpha(.8f)
                .setUnderlined(false);
        links.add(rewardMoneyLink);
        Link numberCountLink = new Link(Pattern.compile("[0-9]+")).setTextColor(ContextCompat.getColor(getContext(), R.color
                .themeColor))
                .setTextColorOfHighlightedLink(ContextCompat.getColor(getContext(), R.color
                        .normal_for_assist_text))
                .setHighlightAlpha(.8f)
                .setUnderlined(false);
        links.add(numberCountLink);
        return links;
    }

    private void initMagicIndicator(){
        String[] title = getResources().getStringArray(R.array.topic_detail_category);
        mMgIndicator.setBackgroundColor(Color.WHITE);
        mCommonNavigator = new CommonNavigator(getActivity());
        mCommonNavigator.setAdjustMode(true);
        mCommonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return title.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {

                SimplePagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView
                        (context);
                simplePagerTitleView.setNormalColor(ContextCompat.getColor(context,
                        R.color.normal_for_assist_text));

                simplePagerTitleView.setSelectedColor(ContextCompat.getColor(context,
                        R.color.important_for_content));

                simplePagerTitleView.setText(title[index]);

                simplePagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, context.getResources
                        ().getInteger(R.integer.tab_text_size_15));
                simplePagerTitleView.setOnClickListener(v -> {
                    mFragmentContainerHelper.handlePageSelected(index, false);
                    mCurrentType = mTypeList.get(index);
                    requestNetData(0L, false);
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setMode(LinePagerIndicator.MODE_MATCH_EDGE);// 占满
                linePagerIndicator.setLineHeight(UIUtil.dip2px(context, context.getResources()
                        .getInteger(R.integer.no_line_height)));
                return linePagerIndicator;
            }
        });
        mMgIndicator.setNavigator(mCommonNavigator);
        mFragmentContainerHelper.attachMagicIndicator(mMgIndicator);
    }

    @Override
    public void setTopicDetail(QATopicBean qaTopicBean) {
        this.mQaTopicBean = qaTopicBean;
        setTopicDetailData();
    }

    @Override
    public Long getTopicId() {
        return mQaTopicBean == null ? 0L : mQaTopicBean.getId();
    }

    @Override
    public String getCurrentType() {
        return mCurrentType;
    }

    @Override
    public QATopicBean getCurrentTopicBean() {
        return mQaTopicBean;
    }

    @Override
    public void updateFollowState() {
        mTvTopicChangeFollow.setChecked(mQaTopicBean.getHas_follow());
        mTvTopicChangeFollow.setText(mQaTopicBean.getHas_follow() ?
                getContext().getString(R.string.followed) : getContext().getString(R.string.follow));
    }

    private void setTopicDetailData(){
        setCenterText(mQaTopicBean.getName());
        mTvTopicName.setText(mQaTopicBean.getName());
        updateFollowState();
        mTvTopicDescription.setText(mQaTopicBean.getDescription());
        mExpertList.setExpertCount(mQaTopicBean.getExperts_count());
        mExpertList.setDigUserHeadIcon(mQaTopicBean.getExperts());
        mTvTopicFeedCount.setText(String.format(Locale.getDefault(),
                getString(R.string.qa_show_topic_detail_feed), mQaTopicBean.getFollows_count(), mQaTopicBean.getQuestions_count()));
        ConvertUtils.stringLinkConvert(mTvTopicFeedCount, setLinks(null));
        Glide.with(BaseApplication.getContext())
                .load(TextUtils.isEmpty(mQaTopicBean.getAvatar()) ? "" : mQaTopicBean.getAvatar())
                .placeholder(R.drawable.shape_default_image)
                .error(R.drawable.shape_default_image)
                .into(mIvTopicCover);
    }

    private void initListener(){
        RxView.clicks(mTvTopicChangeFollow)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    // 修改关注状态
                    mPresenter.handleTopicFollowState(String.valueOf(mQaTopicBean.getId()), !mQaTopicBean.getHas_follow());
                });
    }

}
