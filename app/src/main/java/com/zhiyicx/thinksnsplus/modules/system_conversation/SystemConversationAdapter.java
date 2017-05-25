package com.zhiyicx.thinksnsplus.modules.system_conversation;

import android.content.Context;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.SystemConversationBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * @author LiuChao
 * @describe
 * @date 2017/5/20
 * @contact email:450127106@qq.com
 */

public class SystemConversationAdapter extends CommonAdapter<SystemConversationBean> {

    public SystemConversationAdapter(Context context, int layoutId, List<SystemConversationBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, SystemConversationBean systemConversationBean, int position) {
        setItemData(holder, systemConversationBean, position);
    }

    private void setItemData(final ViewHolder holder, final SystemConversationBean systemConversationBean, final int position) {
        holder.setText(R.id.tv_content, systemConversationBean.getContent());
    }

}
