package com.zhiyicx.thinksnsplus.comment;

import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/04/26/14:14
 * @Email Jliuer@aliyun.com
 * @Description 评论内容转换
 */
public interface ICommonMetadataProvider {
    List<CommonMetadata> convertAndSave();
}
