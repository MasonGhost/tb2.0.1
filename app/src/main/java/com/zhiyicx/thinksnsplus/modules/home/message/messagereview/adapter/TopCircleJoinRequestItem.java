package com.zhiyicx.thinksnsplus.modules.home.message.messagereview.adapter;

import android.content.Context;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.thinksnsplus.data.beans.TopCircleJoinReQuestBean;
import com.zhiyicx.thinksnsplus.modules.home.message.messagereview.MessageReviewContract;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @Author Jliuer
 * @Date 2017/12/13/9:59
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class TopCircleJoinRequestItem extends BaseTopItem implements BaseTopItem.TopReviewEvetnInterface {

    public TopCircleJoinRequestItem(Context context, MessageReviewContract.Presenter presenter) {
        super(context, presenter);
    }

    @Override
    public boolean isForViewType(BaseListBean item, int position) {
        return item instanceof TopCircleJoinReQuestBean;
    }

    @Override
    public void convert(ViewHolder holder, BaseListBean baseListBean, BaseListBean lastT, int position, int itemCounts) {
        TopCircleJoinReQuestBean circleJoinReQuestBean = (TopCircleJoinReQuestBean) baseListBean;
    }

    @Override
    public void onReviewApprovedClick(BaseListBean data, int position) {
        TopCircleJoinReQuestBean circleJoinReQuestBean = (TopCircleJoinReQuestBean) data;
    }

    @Override
    public void onReviewRefuseClick(BaseListBean data, int position) {
        TopCircleJoinReQuestBean circleJoinReQuestBean = (TopCircleJoinReQuestBean) data;
    }
}
