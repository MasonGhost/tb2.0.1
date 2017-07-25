package com.zhiyicx.thinksnsplus.modules.q_a.publish.question;

import android.content.Context;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.thinksnsplus.data.beans.qa.QA_LIstInfoBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe list adapter for recommenc question
 * @Author Jungle68
 * @Date 2017/7/25
 * @Contact master.jungle68@gmail.com
 */

public class PublishQuestionAdapter extends CommonAdapter<QA_LIstInfoBean> {

    public PublishQuestionAdapter(Context context, int layoutId, List<QA_LIstInfoBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, QA_LIstInfoBean qa_lIstInfoBean, int position) {
        setItemData(holder, qa_lIstInfoBean, position);
    }

    private void setItemData(final ViewHolder holder, final QA_LIstInfoBean qa_lIstInfoBean, final int position) {


        // 设置用户名，用户简介
//        holder.setText(R.id.tv_name, userInfoBean.getName());

        // 添加点击事件
        RxView.clicks(holder.getConvertView())
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(aVoid -> ToastUtils.showLongToast(getContext(), "点击"));

    }


}
