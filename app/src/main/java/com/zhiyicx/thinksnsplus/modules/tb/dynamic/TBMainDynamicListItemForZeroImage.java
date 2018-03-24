package com.zhiyicx.thinksnsplus.modules.tb.dynamic;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.widget.DynamicListMenuView;
import com.zhiyicx.baseproject.widget.textview.SpanTextViewWithEllipsize;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListBaseItem;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.thinksnsplus.widget.comment.DynamicListCommentView;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/10
 * @contact email:450127106@qq.com
 */

public class TBMainDynamicListItemForZeroImage extends DynamicListBaseItem {


    public TBMainDynamicListItemForZeroImage(Context context) {
        super(context);
    }


    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_dynamic_list_zero_image_for_tb_main;
    }

    @Override
    public boolean isForViewType(DynamicDetailBeanV2 item, int position) {
        return item.getFeed_mark() != null && (item.getImages() == null || item.getImages().isEmpty());
    }

    @Override
    public void convert(ViewHolder holder, DynamicDetailBeanV2 dynamicBean, DynamicDetailBeanV2 lastT, int position, int itemCounts) {
        holder.setText(R.id.tv_time, TimeUtils.getYeayMonthDay(TimeUtils.utc2LocalLong(dynamicBean.getCreated_at())));
        /*
        文本内容处理
         */
        SpanTextViewWithEllipsize contentView = holder.getView(R.id.tv_content);
        contentView.setOnClickListener(v -> holder.getConvertView().performClick());
        contentView.setText(dynamicBean.getFriendlyContent());
        ConvertUtils.stringLinkConvert(contentView, setLiknks(dynamicBean, contentView.getText().toString()), false);
        if (dynamicBean.isOpen()) {
            contentView.setMaxLines(Integer.MAX_VALUE);
        } else {
            contentView.setMovementMethod(LinkMovementMethod.getInstance());
            contentView.setMaxLines(mMaxlinesShow);
            contentView.setEllipsize(TextUtils.TruncateAt.END);
        }
        contentView.setShowDot(!dynamicBean.isOpen(), mMaxlinesShow);
        contentView.setOnClickListener(v -> {
            if (dynamicBean.isOpen()) {
                dynamicBean.setOpen(false);
                contentView.setMovementMethod(LinkMovementMethod.getInstance());
                ((TextView) v).setMaxLines(mMaxlinesShow);
                contentView.setEllipsize(TextUtils.TruncateAt.END);

            } else {
                dynamicBean.setOpen(true);
                contentView.setMaxLines(Integer.MAX_VALUE);
                contentView.setText(dynamicBean.getFriendlyContent());
                ConvertUtils.stringLinkConvert(contentView, setLiknks(dynamicBean, contentView.getText().toString()), false);
            }
            contentView.setShowDot(!dynamicBean.isOpen(), mMaxlinesShow);
        });

    }
}
