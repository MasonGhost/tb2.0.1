package com.zhiyicx.thinksnsplus.modules.information.adapter;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.InfoCommentListBean;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @Author Jliuer
 * @Date 2017/03/23
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InfoDetailCommentItem implements ItemViewDelegate<InfoCommentListBean> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_dynamic_detail_comment;
    }

    @Override
    public boolean isForViewType(InfoCommentListBean item, int position) {
        return position != 0;
    }

    @Override
    public void convert(ViewHolder holder, InfoCommentListBean infoCommentListBean,
                        InfoCommentListBean lastT, int position) {

    }
}
