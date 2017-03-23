package com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.adapter;

import android.text.TextUtils;

import com.zhiyicx.baseproject.widget.EmptyView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.MusicCommentListBean;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/3/10
 * @Contact master.jungle68@gmail.com
 */

public class MusicEmptyCommentItem implements ItemViewDelegate<MusicCommentListBean> {

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_dynamic_detail_comment_empty;
    }

    @Override
    public boolean isForViewType(MusicCommentListBean item, int position) {
        return TextUtils.isEmpty(item.getComment_content());
    }

    @Override
    public void convert(ViewHolder holder, MusicCommentListBean dynamicCommentBean,
                        MusicCommentListBean lastT, int position) {
        EmptyView emptyView = holder.getView(R.id.comment_emptyview);
        emptyView.setNeedTextTip(false);
        emptyView.setErrorType(EmptyView.STATE_NODATA_ENABLE_CLICK);
    }


}
