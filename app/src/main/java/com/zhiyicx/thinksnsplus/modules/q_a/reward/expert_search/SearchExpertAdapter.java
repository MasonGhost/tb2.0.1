package com.zhiyicx.thinksnsplus.modules.q_a.reward.expert_search;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.SubscriptSpan;
import android.widget.ImageView;
import android.widget.TextView;

import com.klinker.android.link_builder.Link;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.ExpertBean;
import com.zhiyicx.thinksnsplus.data.beans.UserTagBean;
import com.zhiyicx.thinksnsplus.widget.flowtag.FlowTagLayout;
import com.zhiyicx.thinksnsplus.widget.flowtag.OnTagSelectListener;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/26
 * @contact email:648129313@qq.com
 */

public class SearchExpertAdapter extends CommonAdapter<ExpertBean>{

    public SearchExpertAdapter(Context context, List<ExpertBean> datas) {
        super(context, R.layout.item_search_expert, datas);
    }

    @Override
    protected void convert(ViewHolder holder, ExpertBean expertBean, int position) {
        ImageView ivHeadpic = holder.getView(R.id.iv_headpic);
        TextView tvName = holder.getView(R.id.tv_name);
        TextView tvDigCount = holder.getView(R.id.tv_dig_count);
        FlowTagLayout ftlTags = holder.getView(R.id.ftl_tags);
        tvName.setText("小仙女");
        tvDigCount.setText(String.format(Locale.getDefault(), mContext.getString(R.string.qa_publish_show_expert),
                expertBean.getAnswer_count(), expertBean.getDig_count()));
        ConvertUtils.stringLinkConvert(tvDigCount, setLinks(expertBean));
        ftlTags.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_NONE);
        List<UserTagBean> tagBeen = new ArrayList<>();
        tagBeen.add(new UserTagBean());
        tagBeen.add(new UserTagBean());
        tagBeen.add(new UserTagBean());
        tagBeen.add(new UserTagBean());
        tagBeen.add(new UserTagBean());
        tagBeen.add(new UserTagBean());
        UserTagAdapter adapter = new UserTagAdapter(mContext);
        ftlTags.setAdapter(adapter);
        adapter.clearAndAddAll(tagBeen);
        ftlTags.setOnTagSelectListener((parent, selectedList) -> {

        });
        //        ImageUtils.loadCircleUserHeadPic(userInfoBean, headPic);
    }

    private List<Link> setLinks(ExpertBean expertBean) {
        List<Link> links = new ArrayList<>();
        Link digCountLink = new Link(Pattern.compile("[0-9]+")).setTextColor(ContextCompat.getColor(getContext(), R.color
                .themeColor))
                .setTextColorOfHighlightedLink(ContextCompat.getColor(getContext(), R.color
                        .normal_for_assist_text))
                .setHighlightAlpha(.8f)
                .setUnderlined(false);
        links.add(digCountLink);
        return links;
    }

}
