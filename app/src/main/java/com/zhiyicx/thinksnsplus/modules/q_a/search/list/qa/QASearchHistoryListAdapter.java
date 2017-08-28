package com.zhiyicx.thinksnsplus.modules.q_a.search.list.qa;

import android.content.Context;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.qa.QASearchHistoryBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/10
 * @Contact master.jungle68@gmail.com
 */

public class QASearchHistoryListAdapter extends CommonAdapter<QASearchHistoryBean> {

    public QASearchHistoryListAdapter(Context context, int layoutId, List<QASearchHistoryBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, QASearchHistoryBean data, int position) {
        setItemData(holder, data, position);
    }

    private void setItemData(final ViewHolder holder, final QASearchHistoryBean data, final int position) {
        holder.setText(R.id.tv_content, data.getContent());
        RxView.clicks(holder.getView(R.id.iv_delete))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(aVoid -> {
                    // TODO: 2017/8/18 删除历史
                });
    }


}
