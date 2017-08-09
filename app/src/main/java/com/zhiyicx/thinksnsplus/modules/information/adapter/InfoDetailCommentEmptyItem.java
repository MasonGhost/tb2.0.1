package com.zhiyicx.thinksnsplus.modules.information.adapter;

import android.text.TextUtils;

import com.zhiyicx.baseproject.widget.EmptyView;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.InfoCommentListBean;
import com.zhiyicx.thinksnsplus.widget.EmptyItem;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @Author Jliuer
 * @Date 2017/03/30
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InfoDetailCommentEmptyItem extends EmptyItem<InfoCommentListBean> {
    @Override
    public boolean isForViewType(InfoCommentListBean item, int position) {
        return TextUtils.isEmpty(item.getComment_content());
    }
}
