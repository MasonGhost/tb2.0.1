package com.zhiyicx.thinksnsplus.modules.draftbox.adapter;

import android.app.Activity;
import android.view.View;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.BaseDraftBean;
import com.zhiyicx.thinksnsplus.widget.ChooseBindPopupWindow;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Author Jliuer
 * @Date 2017/08/23/14:25
 * @Email Jliuer@aliyun.com
 * @Description
 */
public abstract class BaseDraftItem<D extends BaseDraftBean> implements ItemViewDelegate<D> {

    protected QuestionDraftItem.QuestionDraftItemEvent mQuestionDraftItemEvent;
    protected ChooseBindPopupWindow mPopupWindow;
    protected Activity mActivity;

    public BaseDraftItem(Activity activity) {
        mActivity = activity;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_draft;
    }

    @Override
    public boolean isForViewType(BaseDraftBean item, int position) {
        return false;
    }

    @Override
    public void convert(ViewHolder holder, D draftBean, D lastT, int position, int itemCounts) {
        setDraftClickListener(holder.getImageViwe(R.id.iv_draft_more), holder.getConvertView(), draftBean);
        bindData(holder, draftBean);
    }

    protected void initPopWindow(View v, BaseDraftBean draftBean) {
        mPopupWindow = ChooseBindPopupWindow.Builder()
                .with(mActivity)
                .alpha(0.8f)
                .itemlStr(mActivity.getString(R.string.edit))
                .item2Str(mActivity.getString(R.string.info_delete))
                .isOutsideTouch(true)
                .itemListener(position -> {
                    if (position == 0) {
                        if (mQuestionDraftItemEvent != null) {
                            mQuestionDraftItemEvent.toEditDraft(draftBean);
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

    protected void setDraftClickListener(View more, View itemView, BaseDraftBean draftBean) {
        RxView.clicks(more)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    if (mQuestionDraftItemEvent != null) {
                        initPopWindow(more, draftBean);
                    }
                });

        RxView.clicks(itemView)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    if (mQuestionDraftItemEvent != null) {
                        mQuestionDraftItemEvent.toEditDraft(draftBean);
                    }
                });
    }

    protected abstract void bindData(ViewHolder holder, D realData);

    public void setQuestionDraftItemEvent(QuestionDraftItemEvent questionDraftItemEvent) {
        mQuestionDraftItemEvent = questionDraftItemEvent;
    }

    public interface QuestionDraftItemEvent {
        void toEditDraft(BaseDraftBean draftBean);

        void deleteDraft(BaseDraftBean draftBean);
    }
}
