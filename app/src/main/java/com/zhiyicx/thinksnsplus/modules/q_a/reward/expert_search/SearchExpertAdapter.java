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
        tvName.setText(expertBean.getName());
        tvDigCount.setText(String.format(Locale.getDefault(), mContext.getString(R.string.qa_publish_show_expert),
                expertBean.getAnswer_count(), expertBean.getDig_count()));
        ConvertUtils.stringLinkConvert(tvDigCount, setLinks(expertBean));
        ftlTags.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_NONE);
        List<UserTagBean> tagBeenList = new ArrayList<>();
        UserTagBean tagBean = new UserTagBean();
        tagBean.setTagName("既宅又腐");
        UserTagBean tagBean1 = new UserTagBean();
        tagBean1.setTagName("没有前途");
        UserTagBean tagBean2 = new UserTagBean();
        tagBean2.setTagName("中二青年欢乐多");
        UserTagBean tagBean3 = new UserTagBean();
        tagBean3.setTagName("智障儿童欢乐多");
        UserTagBean tagBean4 = new UserTagBean();
        tagBean4.setTagName("眼镜控");
        UserTagBean tagBean5 = new UserTagBean();
        tagBean5.setTagName("白锅锅喜欢背锅");
        tagBeenList.add(tagBean);
        tagBeenList.add(tagBean1);
        tagBeenList.add(tagBean2);
        tagBeenList.add(tagBean3);
        tagBeenList.add(tagBean4);
        tagBeenList.add(tagBean5);
        UserTagAdapter adapter = new UserTagAdapter(mContext);
        ftlTags.setAdapter(adapter);
        adapter.clearAndAddAll(tagBeenList);
        ftlTags.setOnTagSelectListener((parent, selectedList) -> {

        });
        //        ImageUtils.loadCircleUserHeadPic(userInfoBean, headPic);
    }

    private List<Link> setLinks(ExpertBean expertBean) {
        List<Link> links = new ArrayList<>();
        Link numberCountLink = new Link(Pattern.compile("[0-9]+")).setTextColor(ContextCompat.getColor(getContext(), R.color
                .themeColor))
                .setTextColorOfHighlightedLink(ContextCompat.getColor(getContext(), R.color
                        .normal_for_assist_text))
                .setHighlightAlpha(.8f)
                .setUnderlined(false);
        links.add(numberCountLink);
        Link digCountLink = new Link(String.valueOf(expertBean.getDig_count())).setTextColor(ContextCompat.getColor(getContext(), R.color
                .themeColor))
                .setTextColorOfHighlightedLink(ContextCompat.getColor(getContext(), R.color
                        .normal_for_assist_text))
                .setHighlightAlpha(.8f)
                .setUnderlined(false);
        links.add(digCountLink);
        return links;
    }

}
