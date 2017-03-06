package com.zhiyicx.thinksnsplus.widget.comment;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.zhiyicx.baseproject.widget.SimpleTextNoPullRecycleView;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;

/**
 * @Describe dynamic list comment view
 * @Author Jungle68
 * @Date 2017/3/6
 * @Contact master.jungle68@gmail.com
 */

public class DynamicListComment extends SimpleTextNoPullRecycleView<DynamicCommentBean> {

    public DynamicListComment(Context context) {
        super(context);
    }

    public DynamicListComment(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicListComment(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected String setShowText(DynamicCommentBean dynamicCommentBean, int position) {
        String content = "";
        content = dynamicCommentBean.getComment_content();
        return content;
    }

}
