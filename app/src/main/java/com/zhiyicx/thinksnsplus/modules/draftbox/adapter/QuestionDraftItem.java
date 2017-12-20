package com.zhiyicx.thinksnsplus.modules.draftbox.adapter;

import android.app.Activity;
import android.view.View;

import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.BaseDraftBean;
import com.zhiyicx.thinksnsplus.data.beans.QAPublishBean;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @Author Jliuer
 * @Date 2017/08/22/14:06
 * @Email Jliuer@aliyun.com
 * @Description 草稿箱
 */
public class QuestionDraftItem extends BaseDraftItem<QAPublishBean> {

    public QuestionDraftItem(Activity activity) {
        super(activity);
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_draft;
    }

    @Override
    public boolean isForViewType(BaseDraftBean item, int position) {
        return item instanceof QAPublishBean;
    }

    @Override
    protected void bindData(ViewHolder holder, QAPublishBean realData) {
        holder.setText(R.id.tv_draft_title, realData.getSubject());
        holder.setText(R.id.tv_draft_time, TimeUtils.getTimeFriendlyForDetail(realData.getCreated_at()));
        holder.setVisible(R.id.tv_draft_content, View.GONE);
    }
}
