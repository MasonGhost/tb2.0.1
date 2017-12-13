package com.zhiyicx.thinksnsplus.modules.draftbox.adapter;

import android.app.Activity;
import android.view.View;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.BaseDraftBean;
import com.zhiyicx.thinksnsplus.data.beans.QAPublishBean;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Author Jliuer
 * @Date 2017/08/22/14:06
 * @Email Jliuer@aliyun.com
 * @Description 草稿箱
 */
public class QuestionDraftItem extends BaseDraftItem {

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
    public void convert(ViewHolder holder, BaseDraftBean draftBean, BaseDraftBean lastT, int position, int itemCounts) {
        QAPublishBean realData = (QAPublishBean) draftBean;
        holder.setText(R.id.tv_draft_title, realData.getSubject());
        holder.setText(R.id.tv_draft_time, TimeUtils.getTimeFriendlyForDetail(realData.getCreated_at()));
        holder.setVisible(R.id.tv_draft_content, View.GONE);

        RxView.clicks(holder.getImageViwe(R.id.iv_draft_more))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    if (mQuestionDraftItemEvent != null) {
                        initPopWindow(holder.getImageViwe(R.id.iv_draft_more), draftBean);
                    }
                });

        RxView.clicks(holder.getConvertView())
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    if (mQuestionDraftItemEvent != null) {
                        mQuestionDraftItemEvent.toEditDraft(draftBean);
                    }
                });

    }

}
