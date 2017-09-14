package com.zhiyicx.thinksnsplus.modules.home.message.notifacationlist;

import android.content.Context;

import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.TSPNotificationBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * @author Catherine
 * @describe 先随便展示一下吧
 * @date 2017/8/31
 * @contact email:648129313@qq.com
 */

public class NotificationAdapter extends CommonAdapter<TSPNotificationBean>{

    public NotificationAdapter(Context context, List<TSPNotificationBean> datas) {
        super(context, R.layout.item_notification, datas);
    }

    @Override
    protected void convert(ViewHolder holder, TSPNotificationBean tspNotificationBean, int position) {
        holder.setText(R.id.tv_notification_content, tspNotificationBean.getData().getContent());
        holder.setText(R.id.tv_time, TimeUtils.getTimeFriendlyNormal(tspNotificationBean.getCreated_at()));
    }
}
