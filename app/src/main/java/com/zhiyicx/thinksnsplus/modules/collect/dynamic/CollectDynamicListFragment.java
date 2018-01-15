package com.zhiyicx.thinksnsplus.modules.collect.dynamic;

import android.os.Bundle;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.DynamicToolBean;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumListBean;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.DynamicFragment;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListBaseItem;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForEightImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForFiveImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForFourImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForNineImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForOneImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForSevenImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForSixImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForThreeImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForTwoImage;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListItemForZeroImage;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.Subscriber;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @author LiuChao
 * @describe
 * @date 2017/4/13
 * @contact email:450127106@qq.com
 */

public class CollectDynamicListFragment extends DynamicFragment {
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
    protected void setAdapter(MultiItemTypeAdapter adapter, DynamicListBaseItem dynamicListBaseItem) {
        dynamicListBaseItem.setOnImageClickListener(this);
        dynamicListBaseItem.setOnUserInfoClickListener(this);
        dynamicListBaseItem.setOnMenuItemClickLisitener(this);
        dynamicListBaseItem.setOnReSendClickListener(this);
        dynamicListBaseItem.setOnMoreCommentClickListener(this);
        dynamicListBaseItem.setOnCommentClickListener(this);
        dynamicListBaseItem.setOnCommentStateClickListener(this);
        adapter.addItemViewDelegate(dynamicListBaseItem);
        dynamicListBaseItem.setShowCommentList(false)
                .setShowReSendBtn(false)
                .setShowToolMenu(false);
    }

    @Override
    protected Long getMaxId(@NotNull List<DynamicDetailBeanV2> data) {
        return (long) mListDatas.size();
    }

    public static CollectDynamicListFragment newInstance() {
        CollectDynamicListFragment fragment = new CollectDynamicListFragment();
        fragment.setOnCommentClickListener(null);
        Bundle args = new Bundle();
        args.putString(BUNDLE_DYNAMIC_TYPE, ApiConfig.DYNAMIC_TYPE_MY_COLLECTION);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 每次进行动态收藏操作后，都会进行收藏列表的更新
     */
    @Subscriber(tag = EventBusTagConfig.EVENT_COLLECT_DYNAMIC)
    public void updateDynamicListAfterHandleCollect(DynamicDetailBeanV2 dynamicBean) {
        final boolean isCollect = dynamicBean.isHas_collect();
        LogUtils.i("DynamicBean" + dynamicBean.toString());
        // 存在这样的动态
        if (!isCollect) {// 取消收藏
            LogUtils.i("mListDatas" + mListDatas.contains(dynamicBean));
            mListDatas.remove(dynamicBean);
        } else {
            mListDatas.add(dynamicBean);
            // 按动态feedid大小进行逆序排列，防止上啦加载重复
            Collections.sort(mListDatas, (o1, o2) -> o2.getId() > o1.getId() ? 1 : -1);
        }
        refreshData();
    }
}
