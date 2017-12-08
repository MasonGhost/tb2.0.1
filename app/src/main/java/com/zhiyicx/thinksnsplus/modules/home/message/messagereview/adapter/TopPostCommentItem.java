package com.zhiyicx.thinksnsplus.modules.home.message.messagereview.adapter;

import android.content.Context;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.thinksnsplus.data.beans.TopPostCommentListBean;
import com.zhiyicx.thinksnsplus.modules.home.message.messagereview.MessageReviewContract;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @author Jliuer
 * @Date 2017/12/08/17:02
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class TopPostCommentItem extends BaseTopItem implements BaseTopItem.TopReviewEvetnInterface{

    public TopPostCommentItem(Context context, MessageReviewContract.Presenter presenter) {
        super(context, presenter);
        setTopReviewEvetnInterface(this);
    }

    @Override
    public boolean isForViewType(BaseListBean item, int position) {
        return item instanceof TopPostCommentListBean;
    }

    @Override
    public void convert(ViewHolder holder, BaseListBean baseListBean, BaseListBean lastT, int position, int itemCounts) {

    }

    @Override
    public void onReviewApprovedClick(BaseListBean data, int position) {

    }

    @Override
    public void onReviewRefuseClick(BaseListBean data, int position) {

    }
}
