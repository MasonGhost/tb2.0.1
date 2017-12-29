package com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.adapter;

import android.text.TextUtils;

import com.zhiyicx.thinksnsplus.data.beans.MusicCommentListBean;
import com.zhiyicx.thinksnsplus.widget.EmptyItem;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/3/10
 * @Contact master.jungle68@gmail.com
 */
public class MusicEmptyCommentItem extends EmptyItem<MusicCommentListBean> {
    @Override
    public boolean isForViewType(MusicCommentListBean item, int position) {
        return TextUtils.isEmpty(item.getComment_content());
    }
}
