package com.zhiyicx.thinksnsplus.modules.draftbox.adapter;

import android.app.Activity;
import android.view.View;

import com.zhiyicx.baseproject.config.MarkdownConfig;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.AnswerDraftBean;
import com.zhiyicx.thinksnsplus.data.beans.BaseDraftBean;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @Author Jliuer
 * @Date 2017/08/23/14:22
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class AnswerDraftItem extends BaseDraftItem<AnswerDraftBean> {

    public AnswerDraftItem(Activity activity) {
        super(activity);
    }

    @Override
    public boolean isForViewType(BaseDraftBean item, int position) {
        return item instanceof AnswerDraftBean;
    }

    @Override
    protected void bindData(ViewHolder holder, AnswerDraftBean realData) {
        holder.setText(R.id.tv_draft_title, RegexUtils.replaceImageId(MarkdownConfig.IMAGE_FORMAT, realData.getBody()));
        holder.setText(R.id.tv_draft_time, TimeUtils.getTimeFriendlyForDetail(realData.getCreated_at()));
        holder.setVisible(R.id.tv_draft_content, View.GONE);
    }
}
