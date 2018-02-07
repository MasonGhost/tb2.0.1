package com.zhiyicx.thinksnsplus.modules.draftbox.adapter;

import android.app.Activity;
import android.view.View;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.config.MarkdownConfig;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.AnswerDraftBean;
import com.zhiyicx.thinksnsplus.data.beans.BaseDraftBean;
import com.zhiyicx.thinksnsplus.widget.ChooseBindPopupWindow;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

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
    protected String editeType() {
        return mActivity.getString(R.string.look_question);
    }

    @Override
    protected String setTitle(AnswerDraftBean draftBean) {
        return draftBean.getTitle();
    }

    @Override
    protected void bindData(ViewHolder holder, AnswerDraftBean draftBean) {
        holder.setVisible(R.id.tv_draft_content,View.VISIBLE);
        holder.setText(R.id.tv_draft_content, draftBean.getSubject());
    }

    @Override
    protected void initPopWindow(View v, BaseDraftBean draftBean) {
        mPopupWindow = ChooseBindPopupWindow.Builder()
                .with(mActivity)
                .alpha(0.8f)
                .itemlStr(editeType())
                .item2Str(mActivity.getString(R.string.info_delete))
                .isOutsideTouch(true)
                .itemListener(position -> {
                    if (position == 0) {
                        if (mQuestionDraftItemEvent != null) {
                            mQuestionDraftItemEvent.toQuestionDetail(draftBean);
                        }
                    } else if (position == 1) {
                        if (mQuestionDraftItemEvent != null) {
                            mQuestionDraftItemEvent.deleteDraft(draftBean);
                        }
                    }
                    mPopupWindow.hide();
                })
                .build();
        mPopupWindow.showAsDropDown(v);
    }
}
