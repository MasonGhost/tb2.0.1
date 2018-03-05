package com.zhiyicx.thinksnsplus.modules.home.message.notifacationlist;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkMetadata;
import com.zhiyicx.baseproject.config.MarkdownConfig;
import com.zhiyicx.baseproject.widget.textview.SpanTextViewWithEllipsize;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.TSPNotificationBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Catherine
 * @describe 先随便展示一下吧
 * @date 2017/8/31
 * @contact email:648129313@qq.com
 */

public class NotificationAdapter extends CommonAdapter<TSPNotificationBean> {

    public NotificationAdapter(Context context, List<TSPNotificationBean> datas) {
        super(context, R.layout.item_notification, datas);
    }

    @Override
    protected void convert(ViewHolder holder, TSPNotificationBean tspNotificationBean, int position) {
        SpanTextViewWithEllipsize content = holder.getView(R.id.tv_notification_content);
        content.setText(tspNotificationBean.getData().getFridendlyContent());
        ConvertUtils.stringLinkConvert(content, setLiknks(tspNotificationBean, content.getText().toString()), false);
        if (tspNotificationBean.isOpen()) {
            content.setMaxLines(Integer.MAX_VALUE);
        } else {
            content.setMovementMethod(LinkMovementMethod.getInstance());
            content.setMaxLines(5);
            content.setEllipsize(TextUtils.TruncateAt.END);
        }
        content.setShowDot(!tspNotificationBean.isOpen(),5);
        content.setOnClickListener(v -> {
            if (tspNotificationBean.isOpen()) {
                tspNotificationBean.setOpen(false);
                content.setMovementMethod(LinkMovementMethod.getInstance());
                ((TextView) v).setMaxLines(5);
                content.setEllipsize(TextUtils.TruncateAt.END);

            } else {
                tspNotificationBean.setOpen(true);
                content.setMaxLines(Integer.MAX_VALUE);
                content.setText(tspNotificationBean.getData().getFridendlyContent());
                ConvertUtils.stringLinkConvert(content, setLiknks(tspNotificationBean, content.getText().toString()), false);
            }
            content.setShowDot(!tspNotificationBean.isOpen(),5);
        });
        holder.setText(R.id.tv_time, TimeUtils.getTimeFriendlyNormal(tspNotificationBean.getCreated_at()));
    }

    protected List<Link> setLiknks(final TSPNotificationBean tspNotificationBean, String content) {
        List<Link> links = new ArrayList<>();
        if (content.contains(Link.DEFAULT_NET_SITE)) {
            Link commentNameLink = new Link(Link.DEFAULT_NET_SITE)
                    .setTextColor(ContextCompat.getColor(mContext, R.color
                            .net_link_color))
                    .setLinkMetadata(LinkMetadata.builder()
                            .putString(LinkMetadata.METADATA_KEY_COTENT, tspNotificationBean.getData().getContent())
                            .putSerializableObj(LinkMetadata.METADATA_KEY_TYPE, LinkMetadata.SpanType.NET_SITE)
                            .build())
                    .setTextColorOfHighlightedLink(ContextCompat.getColor(mContext, R.color
                            .general_for_hint))
                    .setHighlightAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                    .setOnClickListener((clickedText, linkMetadata) -> {
                        LogUtils.d(clickedText);
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse(clickedText);
                        intent.setData(content_url);
                        mContext.startActivity(intent);
                    })
                    .setOnLongClickListener((clickedText, linkMetadata) -> {

                    })
                    .setUnderlined(false);
            links.add(commentNameLink);
        }
        return links;
    }
}
