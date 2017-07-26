package com.zhiyicx.thinksnsplus.modules.q_a.qa_main.qa_listinfo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.klinker.android.link_builder.Link;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.textview.CenterImageSpan;
import com.zhiyicx.baseproject.widget.textview.CircleImageDrawable;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import static com.zhiyicx.thinksnsplus.modules.dynamic.list.DynamicFragment.ITEM_SPACING;

/**
 * @Author Jliuer
 * @Date 2017/07/25/10:47
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class QA_ListInfoFragment extends TSListFragment<QA_ListInfoConstact.Presenter, QAListInfoBean>
        implements QA_ListInfoConstact.View {

    public static final String BUNDLE_QA_TYPE = "qa_type";
    public static final String BUNDLE_QA = "qa";
    private String mQAInfoType;

    @Inject
    QA_ListInfoFragmentPresenter mQA_listInfoFragmentPresenter;

    public static QA_ListInfoFragment newInstance(String params) {
        QA_ListInfoFragment fragment = new QA_ListInfoFragment();
        Bundle args = new Bundle();
        args.putString(BUNDLE_QA_TYPE, params);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void onEmptyViewClick() {
        mRefreshlayout.setRefreshing(true);
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    protected boolean isNeedRefreshAnimation() {
        return false;
    }

    @Override
    protected boolean setUseSatusbar() {
        return false;
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
    protected void initData() {
        DaggerQA_ListInfoComponent
                .builder().appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .qA_listInfoFragmentPresenterModule(new QA_listInfoFragmentPresenterModule(this))
                .build().inject(this);
        mListDatas.add(null);
        mListDatas.add(null);
        mListDatas.add(null);
        mListDatas.add(null);
        mListDatas.add(null);
        mListDatas.add(null);
        mListDatas.add(null);
//        super.initData();

    }

    @Override
    protected List requestCacheData(Long maxId, boolean isLoadMore) {
        return mListDatas;
    }

    @Override
    protected void requestNetData(Long maxId, boolean isLoadMore) {
//        super.requestNetData(maxId, isLoadMore);
    }

    @Override
    protected float getItemDecorationSpacing() {
        return ITEM_SPACING;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new CommonAdapter<QAListInfoBean>(getActivity(), R.layout.item_qa_content, mListDatas) {
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
                Bitmap newBmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), com.zhiyicx.baseproject.R.mipmap.icon_256),
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
        return links;
    }
}
