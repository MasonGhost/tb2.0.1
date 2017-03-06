package com.zhiyicx.thinksnsplus.modules.dynamic.detail;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicToolBean;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.dynamic.IDynamicReppsitory;
import com.zhiyicx.thinksnsplus.modules.dynamic.send.SendDynamicContract;

import java.util.List;

import rx.Observable;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/27
 * @contact email:450127106@qq.com
 */

public interface DynamicDetailContract {
    //对于经常使用的关于UI的方法可以定义到BaseView中,如显示隐藏进度条,和显示文字消息
    interface View extends ITSListView<DynamicBean, Presenter> {
        /**
         * 设置是否喜欢该动态
         *
         * @param isLike
         */
        void setLike(boolean isLike);

        /**
         * 设置是否收藏该动态
         *
         * @param isCollect
         */
        void setCollect(boolean isCollect);

        /**
         * 设置点赞头像
         */
        void setDigHeadIcon(List<UserInfoBean> userInfoBeanList);

        /**
         * 更新关注状态
         */
        void upDateFollowFansState(int followState);
    }

    //Model层定义接口,外部只需关心model返回的数据,无需关心内部细节,及是否使用缓存
    interface Repository extends IDynamicReppsitory {
        /**
         * 获取用户关注状态
         *
         * @param user_ids
         * @return
         */
        Observable<BaseJson<List<FollowFansBean>>> getUserFollowState(String user_ids);
    }

    interface Presenter extends ITSListPresenter<DynamicBean> {
        /**
         * 获取当前动态的点赞列表
         */
        void getDynamicDigList(Long feed_id, Integer max_id);

        /**
         * 处理喜欢逻辑
         *
         * @param dynamicToolBean 更新数据库
         */
        void handleLike(boolean isLiked, Long feed_id, DynamicToolBean dynamicToolBean);

        /**
         * 处理收藏逻辑
         *
         * @param isCollected
         * @param feed_id
         * @param dynamicToolBean
         */

        void handleCollect(boolean isCollected, Long feed_id, DynamicToolBean dynamicToolBean);

        /**
         * 动态分享
         */
        void shareDynamic();

        /**
         * 关注或者取消关注
         */
        void handleFollowUser(FollowFansBean followFansBean);

    }
}
