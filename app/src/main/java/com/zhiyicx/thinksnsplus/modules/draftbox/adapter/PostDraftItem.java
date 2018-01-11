package com.zhiyicx.thinksnsplus.modules.draftbox.adapter;

import android.app.Activity;
import android.text.TextUtils;

import com.zhiyicx.thinksnsplus.data.beans.BaseDraftBean;
import com.zhiyicx.thinksnsplus.data.beans.PostDraftBean;

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
        String result = draftBean.getTitle();
        if (TextUtils.isEmpty(result)) {
            result = draftBean.getContent();
        }
        return result;
    }
}
