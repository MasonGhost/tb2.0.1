package com.zhiyicx.thinksnsplus.modules.draftbox.adapter;

import android.app.Activity;
import android.view.View;

import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.BaseDraftBean;
import com.zhiyicx.thinksnsplus.data.beans.PostDraftBean;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @Author Jliuer
 * @Date 2017/12/20/10:01
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class PostDraftItem extends BaseDraftItem<PostDraftBean> {

    public PostDraftItem(Activity activity) {
        super(activity);
    }

    @Override
    public boolean isForViewType(BaseDraftBean item, int position) {
        return item instanceof PostDraftBean;
    }

    @Override
    protected void bindData(ViewHolder holder, PostDraftBean realData) {
        holder.setText(R.id.tv_draft_title, realData.getTitle());
        holder.setText(R.id.tv_draft_time, TimeUtils.getTimeFriendlyForDetail(realData.getCreate_at()));
        holder.setVisible(R.id.tv_draft_content, View.GONE);
    }
}
