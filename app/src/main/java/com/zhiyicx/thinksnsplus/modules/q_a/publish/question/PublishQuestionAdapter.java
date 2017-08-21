package com.zhiyicx.thinksnsplus.modules.q_a.publish.question;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.config.MarkdownConfig;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.QAPublishBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.modules.q_a.publish.add_topic.AddTopicActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.modules.q_a.publish.question.PublishQuestionFragment.BUNDLE_PUBLISHQA_BEAN;

/**
 * @Describe list adapter for recommenc question
 * @Author Jungle68
 * @Date 2017/7/25
 * @Contact master.jungle68@gmail.com
 */

public class PublishQuestionAdapter extends CommonAdapter<QAListInfoBean> {

    public PublishQuestionAdapter(Context context, int layoutId, List<QAListInfoBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, QAListInfoBean qa_listInfoBean, int position) {
        setItemData(holder, qa_listInfoBean, position);
    }

    private void setItemData(final ViewHolder holder, final QAListInfoBean qa_listInfoBean, final int position) {

        // 设置用户名，用户简介
        holder.setText(R.id.tv_content, RegexUtils.replaceImageId(MarkdownConfig.IMAGE_FORMAT,qa_listInfoBean.getSubject()));

        // 添加点击事件
        RxView.clicks(holder.getConvertView())
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(aVoid -> {
                    Intent intent = new Intent(mContext, AddTopicActivity.class);
                    Bundle bundle = new Bundle();
                    QAPublishBean qaPublishBean = new QAPublishBean();
                    qaPublishBean.setSubject(qa_listInfoBean.getBody());
                    bundle.putParcelable(BUNDLE_PUBLISHQA_BEAN, qaPublishBean);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                });

    }


}
