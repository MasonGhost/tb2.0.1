package com.zhiyicx.thinksnsplus.modules.q_a.detail.topic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.klinker.android.link_builder.Link;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.base.TSViewPagerAdapter;
import com.zhiyicx.common.base.BaseApplication;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.QAPublishBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QATopicBean;
import com.zhiyicx.thinksnsplus.modules.information.adapter.ScaleTransitionPagerTitleView;
import com.zhiyicx.thinksnsplus.modules.q_a.detail.topic.list.TopicDetailListFragment;
import com.zhiyicx.thinksnsplus.modules.q_a.publish.question.PublishQuestionActivity;
import com.zhiyicx.thinksnsplus.modules.q_a.reward.expert_search.ExpertSearchActivity;
import com.zhiyicx.thinksnsplus.widget.ExpandableTextView;
import com.zhiyicx.thinksnsplus.widget.HorizontalStackIconView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
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
import static com.zhiyicx.thinksnsplus.modules.q_a.detail.topic.list.TopicDetailListFragment.BUNDLE_TOPIC_TYPE;
import static com.zhiyicx.thinksnsplus.modules.q_a.publish.question.PublishQuestionFragment.BUNDLE_PUBLISHQA_BEAN;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/14
 * @contact email:648129313@qq.com
 */

public class TopicDetailFragment extends TSFragment<TopicDetailContract.Presenter>
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
    ExpandableTextView mTvTopicDescription;
    @BindView(R.id.expert_list)
    HorizontalStackIconView mExpertList;
    @BindView(R.id.mg_indicator)
    MagicIndicator mMgIndicator;
    @BindView(R.id.view_diver)
    View mViewDiver;
    @BindView(R.id.vp_list)
    ViewPager mVpList;
    @BindView(R.id.btn_publish_question)
    ImageView mBtnPublishQuestion;

    protected TSViewPagerAdapter mTsViewPagerAdapter;
    private CommonNavigator mCommonNavigator;
    private QATopicBean mQaTopicBean;

    private String mCurrentType = TYPE_NEW;
    private List<String> mTypeList;

    public TopicDetailFragment instance(Bundle bundle) {
        TopicDetailFragment fragment = new TopicDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {
        initMagicIndicator();
        initListener();
    }

    @Override
    protected void initData() {
        mTypeList = new ArrayList<>();
        mTypeList.add(TYPE_HOT);
        mTypeList.add(TYPE_EXCELLENT);
        mTypeList.add(TYPE_REWARD);
        mTypeList.add(TYPE_NEW);
        mTypeList.add(TYPE_ALL);
        mQaTopicBean = (QATopicBean) getArguments().getSerializable(BUNDLE_TOPIC_BEAN);
        mPresenter.getTopicDetail(String.valueOf(mQaTopicBean.getId()));
        initViewPager();
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_topic_detail;
    }
    @Override
    protected boolean showToolBarDivider() {
        return true;
    }
    @Override
    protected int setToolBarBackgroud() {
        return R.color.white;
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
    protected void setRightClick() {
        // 点击弹起分享框
        Bitmap shareBitmap = ConvertUtils.drawable2BitmapWithWhiteBg(getContext(), mIvTopicCover
                .getDrawable(), R.mipmap.icon);
        mPresenter.shareTopic(shareBitmap);
    }

    @Override
    protected int setRightImg() {
        return R.mipmap.icon_share;
    }

    private void initMagicIndicator() {
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
                    mCurrentType = mTypeList.get(index);
                    mVpList.setCurrentItem(index);
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
        ViewPagerHelper.bind(mMgIndicator, mVpList);
    }

    private void initViewPager() {
        mVpList.setOffscreenPageLimit(5);
        mTsViewPagerAdapter = new TSViewPagerAdapter(getChildFragmentManager());
        mTsViewPagerAdapter.bindData(initFragments());
        mVpList.setAdapter(mTsViewPagerAdapter);
    }

    private List<Fragment> initFragments() {
        List<Fragment> list = new ArrayList<>();
        for (String type : mTypeList) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(BUNDLE_TOPIC_BEAN, mQaTopicBean);
            bundle.putString(BUNDLE_TOPIC_TYPE, type);
            list.add(new TopicDetailListFragment().instance(bundle));
        }
        return list;
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
        mTvTopicChangeFollow.setPadding(mQaTopicBean.getHas_follow() ?
                getResources().getDimensionPixelSize(R.dimen.spacing_small) : getResources().getDimensionPixelSize(R.dimen.spacing_normal), 0, 0, 0);
        mTvTopicFeedCount.setText(String.format(Locale.getDefault(),
                getString(R.string.qa_show_topic_detail_feed), String.valueOf(mQaTopicBean.getFollows_count()),
                String.valueOf(mQaTopicBean.getQuestions_count())));
        ConvertUtils.stringLinkConvert(mTvTopicFeedCount, dealTopicDetail(mQaTopicBean));
    }

    @Override
    public void showDeleteSuccess() {
        showSnackSuccessMessage(getString(R.string.qa_question_delete_success));
    }

    private void setTopicDetailData() {
        setCenterText(mQaTopicBean.getName());
        mTvTopicName.setText(mQaTopicBean.getName());
        updateFollowState();
        mTvTopicDescription.setText(String.format(getString(R.string.qa_topic_description), mQaTopicBean.getDescription()));
        mExpertList.setExpertCount(mQaTopicBean.getExperts_count());
        mExpertList.setDigUserHeadIcon(mQaTopicBean.getExperts());
        mViewDiver.setVisibility(mQaTopicBean.getExperts_count() == 0 ? View.GONE : View.VISIBLE);
        Glide.with(BaseApplication.getContext())
                .load(TextUtils.isEmpty(mQaTopicBean.getAvatar()) ? "" : mQaTopicBean.getAvatar())
                .placeholder(R.drawable.shape_default_image)
                .error(R.drawable.shape_default_image)
                .into(mIvTopicCover);
    }

    private List<Link> dealTopicDetail(QATopicBean topicBean) {
        List<Link> links = new ArrayList<>();
        Link numberCountLink = new Link(Pattern.compile("[0-9]+")).setTextColor(ContextCompat.getColor(getContext(), R.color
                .themeColor))
                .setTextColorOfHighlightedLink(ContextCompat.getColor(getContext(), R.color
                        .normal_for_assist_text))
                .setHighlightAlpha(.8f)
                .setUnderlined(false);
        links.add(numberCountLink);
        Link followCountLink = new Link(String.valueOf(topicBean.getFollows_count()))
                .setTextColor(ContextCompat.getColor(getContext(), R.color
                        .themeColor))
                .setTextColorOfHighlightedLink(ContextCompat.getColor(getContext(), R.color
                        .general_for_hint))
                .setHighlightAlpha(.8f)
                .setUnderlined(false);
        links.add(followCountLink);

        Link answerCountLink = new Link(String.valueOf(topicBean.getQuestions_count()))
                .setTextColor(ContextCompat.getColor(getContext(), R.color
                        .themeColor))
                .setTextColorOfHighlightedLink(ContextCompat.getColor(getContext(), R.color
                        .general_for_hint))
                .setHighlightAlpha(.8f)
                .setUnderlined(false);
        links.add(answerCountLink);
        return links;
    }

    private void initListener() {
        RxView.clicks(mTvTopicChangeFollow)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    // 修改关注状态
                    mPresenter.handleTopicFollowState(String.valueOf(mQaTopicBean.getId()), !mQaTopicBean.getHas_follow());
                });
        RxView.clicks(mExpertList)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    // 跳转专家列表
                    Intent intent = new Intent(getActivity(), ExpertSearchActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(BUNDLE_TOPIC_BEAN, mQaTopicBean);
                    intent.putExtras(bundle);
                    startActivity(intent);
                });
        RxView.clicks(mBtnPublishQuestion)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    if (mQaTopicBean != null) {
                        QAPublishBean qaPublishBean = new QAPublishBean();
                        List<QAPublishBean.Topic> typeIdsList = new ArrayList<>();
                        QAPublishBean.Topic typeIds = new QAPublishBean.Topic();
                        typeIds.setId(mQaTopicBean.getId().intValue());
                        typeIds.setName(mQaTopicBean.getName());
                        typeIdsList.add(typeIds);
                        qaPublishBean.setTopics(typeIdsList);

                        String mark = AppApplication.getmCurrentLoginAuth().getUser_id() + "" + System
                                .currentTimeMillis();
                        qaPublishBean.setMark(Long.parseLong(mark));
                        mPresenter.saveQuestion(qaPublishBean);

                        Intent publishQaIntent = new Intent(getActivity(), PublishQuestionActivity.class);
                        Bundle publishQaBundle = new Bundle();

                        publishQaBundle.putParcelable(BUNDLE_PUBLISHQA_BEAN, qaPublishBean);
                        publishQaIntent.putExtras(publishQaBundle);

                        startActivity(publishQaIntent);

//                        Intent intent = new Intent(getActivity(), EditeQuestionDetailActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putParcelable(BUNDLE_PUBLISHQA_TOPIC, mQaTopicBean);
//                        intent.putExtras(bundle);
//                        startActivity(intent);
                    }
                });
    }



}
