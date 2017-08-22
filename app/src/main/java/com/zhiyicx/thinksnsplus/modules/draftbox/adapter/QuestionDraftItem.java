package com.zhiyicx.thinksnsplus.modules.draftbox.adapter;

import android.app.Activity;
import android.view.View;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.BaseDraftBean;
import com.zhiyicx.thinksnsplus.data.beans.QAPublishBean;
import com.zhiyicx.thinksnsplus.widget.ChooseBindPopupWindow;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Author Jliuer
 * @Date 2017/08/22/14:06
 * @Email Jliuer@aliyun.com
 * @Description 草稿箱
 */
public class QuestionDraftItem implements ItemViewDelegate<BaseDraftBean> {

    QuestionDraftItemEvent mQuestionDraftItemEvent;
    private ChooseBindPopupWindow mPopupWindow;
    private Activity mActivity;

    public QuestionDraftItem(Activity activity) {
        mActivity = activity;
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
                        initPopWindow(holder.getImageViwe(R.id.iv_draft_more), realData);
                    }
                });

        RxView.clicks(holder.getConvertView())
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    if (mQuestionDraftItemEvent != null) {
                        mQuestionDraftItemEvent.toEditDraft(realData);
                    }
                });

    }

    private void initPopWindow(View v, QAPublishBean realData) {
        if (mPopupWindow == null) {
            mPopupWindow = ChooseBindPopupWindow.Builder()
                    .with(getActivity())
                    .alpha(0.8f)
                    .itemlStr("编辑")
                    .item2Str("删除")
                    .isOutsideTouch(true)
                    .itemListener(position -> {
                        if (position == 0) {
                            if (mQuestionDraftItemEvent != null) {
                                mQuestionDraftItemEvent.toEditDraft(realData);
                            }
                        } else if (position == 1) {
                            if (mQuestionDraftItemEvent != null) {
                                mQuestionDraftItemEvent.deleteDraft(realData);
                            }
                        }
                        mPopupWindow.hide();
                    })
                    .build();
        }
        mPopupWindow.showAsDropDown(v, 0, 10);
    }

    public Activity getActivity() {
        return mActivity;
    }

    public void setQuestionDraftItemEvent(QuestionDraftItemEvent questionDraftItemEvent) {
        mQuestionDraftItemEvent = questionDraftItemEvent;
    }

    public interface QuestionDraftItemEvent {
        void toEditDraft(BaseDraftBean draftBean);

        void deleteDraft(BaseDraftBean draftBean);
    }
}
