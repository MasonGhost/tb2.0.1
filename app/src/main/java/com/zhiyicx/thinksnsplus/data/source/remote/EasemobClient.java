package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/15
 * @contact email:648129313@qq.com
 */

public interface EasemobClient {

    /**
     * 创建群组会话
     * groupname 	// 群组名称（必填）
     * desc 		// 群组简介（必填）
     * public 		// 是否是公开群，此属性为必须的（必填）
     * maxusers	// 群组成员最大数
     * members_only	// 加入群是否需要群主或者群管理员审批，默认是false
     * allowinvites	// 是否允许群成员邀请别人加入此群
     * owner		// 群组的管理员（必填）
     * members		// 群组的成员
     */
    @POST(ApiConfig.APP_PATH_CREATE_CHAT_GROUP)
    Observable<ChatGroupBean> createGroup(@Query("groupname") String groupName, @Query("desc") String groupIntro, @Query("public") int isPublic,
                                          @Query("maxusers") int maxUser, @Query("members_only") boolean isMemberOnly, @Query("allowinvites") int isAllowInvites,
                                          @Query("owner") long owner, @Query("members") String members);

    /**
     * 更新群信息
     *
     * @param groupName      groupName
     * @param groupIntro     groupIntro
     * @param isPublic       isPublic
     * @param maxUser        maxUser
     * @param isMemberOnly   isMemberOnly
     * @param isAllowInvites isAllowInvites
     * @param groupFace      groupFace
     */
    @PATCH(ApiConfig.APP_PATH_CREATE_CHAT_GROUP)
    Observable<ChatGroupBean> updateGroup(@Query("im_group_id") String im_group_id, @Query("groupname") String groupName, @Query("desc") String groupIntro, @Query("public") int isPublic,
                                          @Query("maxusers") int maxUser, @Query("members_only") boolean isMemberOnly, @Query("allowinvites") int isAllowInvites,
                                          @Query("group_face") String groupFace);

    /**
     * 批量获取群信息
     *
     * @param ids im_group_id 以逗号隔开
     */
    @GET(ApiConfig.APP_PATH_GET_GROUP_INFO_S)
    Observable<List<ChatGroupBean>> getGroupInfo(@Query("im_group_id") String ids);
}
