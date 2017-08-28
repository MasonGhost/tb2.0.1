package com.zhiyicx.thinksnsplus.modules.edit_userinfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.UserTagBean;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;

import java.util.List;


/**
 * @Describe list adapter for recommenc question topic
 * @Author Jungle68
 * @Date 2017/7/25
 * @Contact master.jungle68@gmail.com
 */

public class UserInfoTagsAdapter extends TagAdapter<UserTagBean> {

    private final LayoutInflater mInflater;
    private boolean mIsCircleRadus = false;

    public UserInfoTagsAdapter(List<UserTagBean> datas, Context context) {
        super(datas);
        mInflater = LayoutInflater.from(context);
    }

    public UserInfoTagsAdapter(List<UserTagBean> datas, Context context, boolean isCircleRadus) {
        super(datas);
        mInflater = LayoutInflater.from(context);
        mIsCircleRadus = isCircleRadus;
    }

    @Override
    public View getView(FlowLayout parent, int position, UserTagBean qaTopicBean) {
        TextView tv = (TextView) mInflater.inflate(R.layout.item_userinfo_tags,
                parent, false);
        if (mIsCircleRadus) {
            tv.setBackgroundResource(R.drawable.shape_default_radus_circle_gray);
        } else {
            tv.setBackgroundResource(R.drawable.item_react_bg_gray);
        }
        tv.setText(qaTopicBean.getTagName());
        return tv;
    }
}
