package com.zhiyicx.thinksnsplus.widget.comment;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.view.View;

import com.zhiyicx.baseproject.widget.SimpleTextNoPullRecycleView;
import com.zhiyicx.common.utils.ColorPhrase;
import com.zhiyicx.common.widget.NoLineClickSpan;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

/**
 * @Describe dynamic list comment view
 * @Author Jungle68
 * @Date 2017/3/6
 * @Contact master.jungle68@gmail.com
 */

public class DynamicNoPullRecycleView extends SimpleTextNoPullRecycleView<DynamicCommentBean> {

    private OnUserNameClickListener mOnUserNameClickListener;

    public DynamicNoPullRecycleView(Context context) {
        super(context);
    }

    public DynamicNoPullRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicNoPullRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected CharSequence setShowText(DynamicCommentBean dynamicCommentBean, int position) {
        return handleName(dynamicCommentBean);
    }

    /**
     * 处理名字的颜色与点击
     *
     * @param dynamicCommentBean
     * @return
     */
    private CharSequence handleName(DynamicCommentBean dynamicCommentBean) {
        String content = "";
        if (dynamicCommentBean.getReplyUser().getUser_id().longValue() == dynamicCommentBean.getFeed_user_id()) {
            content += "<" + dynamicCommentBean.getCommentUser().getName() + ">:  " + dynamicCommentBean.getComment_content();
        } else {
            content += "<" + dynamicCommentBean.getCommentUser().getName() + "> 回复 <" + dynamicCommentBean.getReplyUser().getName() + ">:  " + dynamicCommentBean.getComment_content();
        }

        CharSequence chars = ColorPhrase.from(addClickablePart(content, dynamicCommentBean)).withSeparator("<>")
                .innerColor(ContextCompat.getColor(getContext(), R.color.important_for_content))
                .outerColor(ContextCompat.getColor(getContext(), R.color.normal_for_assist_text))
                .format();
        return chars;
    }

    /**
     * 处理 名字点击
     *
     * @param str
     * @return
     */
    private SpannableStringBuilder addClickablePart(String str, final DynamicCommentBean userInfoBean) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(str);

        int idx1 = str.indexOf("<");
        int idx2 = 0;
        while (idx1 != -1) {
            idx2 = str.indexOf(">", idx1) + 1;
            final String clickString = str.substring(idx1 + 1, idx2 - 1);
            ssb.setSpan(new NoLineClickSpan() {

                @Override
                public void onClick(View widget) {
                    if (mOnUserNameClickListener != null) {
                        if (clickString.equals(userInfoBean.getCommentUser().getName())) {
                            mOnUserNameClickListener.onUserNameClick(userInfoBean.getCommentUser());
                        } else {
                            mOnUserNameClickListener.onUserNameClick(userInfoBean.getReplyUser());
                        }
                    }
                }
            }, idx1, idx2, 0);
            idx1 = str.indexOf("<", idx2);
        }

        return ssb;
    }

    public void setOnUserNameClickListener(OnUserNameClickListener onUserNameClickListener) {
        mOnUserNameClickListener = onUserNameClickListener;
    }

    public interface OnUserNameClickListener {
        void onUserNameClick(UserInfoBean userInfoBean);

    }

}
