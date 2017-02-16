package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBean;

import rx.Observable;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/16
 * @contact email:450127106@qq.com
 */

public interface FollowFansClient {
    /**
     * 获取粉丝关注列表
     *
     * @param userId 该用户的userId
     * @return
     */
    Observable<BaseJson<FollowFansBean>> getFollowFans(int userId);
}
