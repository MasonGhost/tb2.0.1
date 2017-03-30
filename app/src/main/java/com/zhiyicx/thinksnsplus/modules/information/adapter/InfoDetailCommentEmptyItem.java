package com.zhiyicx.thinksnsplus.modules.information.adapter;

import com.zhiyicx.thinksnsplus.data.beans.InfoCommentListBean;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @Author Jliuer
 * @Date 2017/03/30
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InfoDetailCommentEmptyItem implements ItemViewDelegate<InfoCommentListBean> {
    @Override
    public int getItemViewLayoutId() {
        return 0;
    }

    @Override
    public boolean isForViewType(InfoCommentListBean item, int position) {
        return false;
    }

    @Override
    public void convert(ViewHolder holder, InfoCommentListBean infoCommentListBean,
                        InfoCommentListBean lastT, int position) {

    }
}
