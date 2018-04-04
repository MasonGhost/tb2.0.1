package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.beans.InfoCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoPublishBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeBean;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_INFO_LIST_TB;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/7
 * @contact email:648129313@qq.com
 */

public interface IBaseInfoRepository {

    // 搜索也用这个接口
    Observable<List<InfoListDataBean>> getInfoListV2(String cate_id, String key, long max_id,
                                                     long page, int isRecommend);

    Observable<List<InfoListDataBean>> getCollectionListV2(long max_id);

    Observable<List<InfoListDataBean>> getMyInfoList(String type, long max_id);

    Observable<InfoTypeBean> getInfoType();

    Observable<BaseJsonV2<Object>> publishInfo(InfoPublishBean infoPublishBean);

    Observable<BaseJsonV2<Object>> updateInfo(InfoPublishBean infoPublishBean);

    Observable<InfoCommentBean> getInfoCommentListV2(String news_id,
                                                     Long max_id,
                                                     Long limit);

    Observable<List<InfoDigListBean>> getInfoDigListV2(String news_id,
                                                       Long max_id);

    Observable<List<InfoListDataBean>> getRelateInfoList(String news_id);

    Observable<InfoListDataBean> getInfoDetail(String news_id);


    void handleCollect(boolean isCollected, String news_id);

    void handleLike(boolean isLiked, final String news_id);

    void sendComment(String comment_content, Long news_id,
                     int reply_to_user_id, Long comment_mark);

    void deleteComment(int news_id, int comment_id);

    Observable<BaseJsonV2<Object>> deleteInfo(String category, String news_id);

    /*******************************************  TB  *********************************************/


    /**
     * 获取资讯列表
     *
     * @param key  搜索用的关键字
     * @param type top-头条资讯 follow-关注机构资讯 默认top
     */
    Observable<List<InfoListDataBean>> getInfoListTB(String cate_id,
                                                     Long max_id,
                                                     Long limit,
                                                     Long page,
                                                     String key,
                                                     String type,
                                                     Long user_id
    );

    /**
     * 获取用户对该资讯的所有评论
     *
     * @param news_id  资讯ID
     */
    Observable<List<InfoCommentListBean>> getMyInfoCommentListV2(String news_id, Long max_id, Long limit);


}
