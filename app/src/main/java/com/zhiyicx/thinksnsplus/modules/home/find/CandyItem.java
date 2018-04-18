package com.zhiyicx.thinksnsplus.modules.home.find;

import android.content.Context;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.InfoCommentListBean;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @Author MasonGhost
 * @Date 2018/04/18
 * @Email lx1993m@gmail.com
 * @Description
 */

public class CandyItem implements ItemViewDelegate<InfoCommentListBean> {


    public CandyItem(Context context) {
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_candy;
    }

    @Override
    public boolean isForViewType(InfoCommentListBean item, int position) {
        return false;
    }

    @Override
    public void convert(ViewHolder holder, InfoCommentListBean infoCommentListBean, InfoCommentListBean lastT, int position, int itemCounts) {

    }
}
