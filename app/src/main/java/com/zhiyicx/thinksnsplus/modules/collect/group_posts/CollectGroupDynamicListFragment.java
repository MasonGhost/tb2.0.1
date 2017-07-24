package com.zhiyicx.thinksnsplus.modules.collect.group_posts;

import com.zhiyicx.thinksnsplus.modules.channel.detail.ChannelDetailFragment;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListBaseItem;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

/**
 * @Author Jliuer
 * @Date 2017/07/24/10:56
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CollectGroupDynamicListFragment extends ChannelDetailFragment {
    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    protected boolean setUseSatusbar() {
        return false;
    }

    @Override
    protected boolean setUseStatusView() {
        return false;
    }

    @Override
    protected boolean setHeadShow() {
        return false;
    }

    protected void setAdapter(MultiItemTypeAdapter adapter, DynamicListBaseItem dynamicListBaseItem) {
        dynamicListBaseItem.setOnUserInfoClickListener(this);
        adapter.addItemViewDelegate(dynamicListBaseItem);
        dynamicListBaseItem.setShowCommentList(false)
                .setShowReSendBtn(false)
                .setShowToolMenu(false);
    }
}
