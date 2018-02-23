package com.zhiyicx.thinksnsplus.modules.draftbox.adapter;

import android.app.Activity;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.BaseDraftBean;
import com.zhiyicx.thinksnsplus.data.beans.QAPublishBean;

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
    public boolean isForViewType(BaseDraftBean item, int position) {
        return item instanceof QAPublishBean;
    }

    @Override
    protected String setCreateTime(QAPublishBean draftBean) {
        return draftBean.getCreated_at();
    }

    @Override
    protected String editeType() {
        return mActivity.getString(R.string.edit_question);
    }

    @Override
    protected String setTitle(QAPublishBean draftBean) {
        return draftBean.getSubject();
    }
}
