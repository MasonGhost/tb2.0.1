package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.common.base.BaseJson;

import rx.Observable;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/14
 * @Contact master.jungle68@gmail.com
 */

public interface ICommentRepository {

    Observable<BaseJson<Object>> sendComment(String comment_content, long reply_to_user_id, long comment_mark, String path);

}
