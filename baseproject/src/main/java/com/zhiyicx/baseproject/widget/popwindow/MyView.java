package com.zhiyicx.baseproject.widget.popwindow;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.zhiyicx.baseproject.R;
import com.zhiyicx.baseproject.widget.DynamicDetailMenuView;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/2/9
 * @Contact master.jungle68@gmail.com
 */

public class MyView extends DynamicDetailMenuView {

    protected
    @DrawableRes
    int[] mImageNormalResourceIds = new int[]{
            R.mipmap.home_ico_good_normal,
            R.mipmap.home_ico_comment_normal,
            R.mipmap.detail_ico_share_normal,
            R.mipmap.home_ico_more
    };// 图片 ids 正常状态
    protected
    @DrawableRes
    int[] mImageCheckedResourceIds = new int[]{
            R.mipmap.home_ico_good_high,
            R.mipmap.home_ico_comment_normal,
            R.mipmap.detail_ico_share_normal,
            R.mipmap.home_ico_more
    };// 图片 ids 选中状态
    protected
    @StringRes
    int[] mTexts = new int[]{
            R.string.like,
            R.string.comment,
            R.string.share,
            R.string.more
    };// 文字 ids

    protected
    @ColorRes
    int mTextNormalColor = R.color.normal_for_disable_button_text;// 正常文本颜色
    protected
    @ColorRes
    int mTextCheckedColor = R.color.normal_for_disable_button_text;// 选中文本颜色

    public MyView(Context context) {
        super(context);
    }
}
