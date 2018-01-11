package com.zhiyicx.thinksnsplus.modules.draftbox.adapter;

import android.app.Activity;

import com.zhiyicx.baseproject.config.MarkdownConfig;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.thinksnsplus.data.beans.AnswerDraftBean;
import com.zhiyicx.thinksnsplus.data.beans.BaseDraftBean;

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
    protected String setCreateTime(AnswerDraftBean draftBean) {
        return draftBean.getCreated_at();
    }

    @Override
    protected String setTitle(AnswerDraftBean draftBean) {
        return RegexUtils.replaceImageId(MarkdownConfig.IMAGE_FORMAT, draftBean.getBody());
    }
}
