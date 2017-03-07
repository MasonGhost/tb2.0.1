package com.zhiyicx.thinksnsplus.widget.comment;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.zhiyicx.baseproject.widget.SimpleTextNoPullRecycleView;
import com.zhiyicx.common.utils.ColorPhrase;
import com.zhiyicx.common.widget.NoLineClickSpan;
import com.zhiyicx.thinksnsplus.R;
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
    protected CharSequence setShowText(DynamicCommentBean dynamicCommentBean, int position) {
        String content = "";
        if (dynamicCommentBean.getReplyUser().getUser_id().longValue() == dynamicCommentBean.getFeed_user_id()) {
            content += "<" + dynamicCommentBean.getCommentUser().getName() + ">:  " + dynamicCommentBean.getComment_content();
        } else {
            content += "<" + dynamicCommentBean.getCommentUser().getName() + "> 回复 <" + dynamicCommentBean.getReplyUser().getName() + ">:  " + dynamicCommentBean.getComment_content();
        }

        CharSequence chars = ColorPhrase.from(addClickablePart(content)).withSeparator("<>")
                .innerColor(ContextCompat.getColor(getContext(), R.color.important_for_content))
                .outerColor(ContextCompat.getColor(getContext(), R.color.normal_for_assist_text))
                .format();
        System.out.println("chars = " + chars);
        return chars;
    }

    private SpannableStringBuilder addClickablePart(String str) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(str);

        int idx1 = str.indexOf("<");
        int idx2 = 0;
        while (idx1 != -1) {
            idx2 = str.indexOf(">", idx1) + 1;

            final String clickString = str.substring(idx1, idx2);
            ssb.setSpan(new NoLineClickSpan() {

                @Override
                public void onClick(View widget) {
                    Toast.makeText(getContext(), clickString,
                            Toast.LENGTH_SHORT).show();
                }
            }, idx1, idx2, 0);
            idx1 = str.indexOf("<", idx2);
        }

        return ssb;
    }

}
