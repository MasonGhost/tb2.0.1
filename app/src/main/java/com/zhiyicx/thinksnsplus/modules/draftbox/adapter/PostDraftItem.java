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
    protected String setCreateTime(PostDraftBean draftBean) {
        return draftBean.getCreate_at();
    }

    @Override
    protected String setTitle(PostDraftBean draftBean) {
        return draftBean.getTitle();
    }
}
