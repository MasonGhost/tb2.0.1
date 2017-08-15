package com.zhiyicx.thinksnsplus.modules.q_a.publish.add_topic;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.qa.QATopicBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;


/**
 * @Describe list adapter for recommenc question topic
 * @Author Jungle68
 * @Date 2017/7/25
 * @Contact master.jungle68@gmail.com
 */

public class AddTopicAdapter extends CommonAdapter<QATopicBean> {

    public AddTopicAdapter(Context context, int layoutId, List<QATopicBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, QATopicBean qaTopicBean, int position) {
        setItemData(holder, qaTopicBean, position);
    }

    private void setItemData(final ViewHolder holder, final QATopicBean topicBean, final int position) {


        // 设置用户名，用户简介
        holder.setText(R.id.tv_title, topicBean.getName());
        holder.setText(R.id.tv_content, topicBean.getDescription());
        ImageView headImage = holder.getImageViwe(R.id.iv_cover);
        Glide.with(mContext)
                .load(topicBean.getAvatar())
                .placeholder(R.drawable.shape_default_image)
                .error(R.drawable.shape_default_image)
                .into(headImage);

    }


}
