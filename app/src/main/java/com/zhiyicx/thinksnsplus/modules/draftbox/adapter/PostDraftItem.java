package com.zhiyicx.thinksnsplus.modules.draftbox.adapter;

import android.app.Activity;
import android.text.TextUtils;

import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
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
    protected String editeType() {
        return mActivity.getString(R.string.edit_post);
    }

    @Override
    protected String setTitle(PostDraftBean draftBean) {
        String result = draftBean.getTitle();
        if (TextUtils.isEmpty(result)) {
            result = "暂无标题";
        }
        return result;
    }
}
