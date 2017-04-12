package com.zhiyicx.thinksnsplus.modules.music_fm.CommonComment;

/**
 * @Author Jliuer
 * @Date 2017/04/12/9:25
 * @Email Jliuer@aliyun.com
 * @Description 评论抽取
 */
public interface ICommentEvent<C> {
    void handleComment(C comment);
}
