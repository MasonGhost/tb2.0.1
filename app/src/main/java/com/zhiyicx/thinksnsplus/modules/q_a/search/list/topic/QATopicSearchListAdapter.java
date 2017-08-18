package com.zhiyicx.thinksnsplus.modules.q_a.search.list.topic;

import android.content.Context;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/10
 * @Contact master.jungle68@gmail.com
 */

public class QATopicSearchListAdapter extends CommonAdapter<QAListInfoBean> {

    public QATopicSearchListAdapter(Context context, int layoutId, List<QAListInfoBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, QAListInfoBean data, int position) {
        setItemData(holder, data, position);
    }

    private void setItemData(final ViewHolder holder, final QAListInfoBean data, final int position) {
        holder.setText(R.id.tv_content, data.getSubject());
    }


}
