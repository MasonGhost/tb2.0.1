package com.zhiyicx.thinksnsplus.data.source.repository.i;

import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.base.BaseJsonV2;

import rx.Observable;

/**
 * @author LiuChao
 * @describe
 * @date 2017/1/22
 * @contact email:450127106@qq.com
 */

public interface IUploadRepository {
    /**
     * 上传单个文件
     *
     * @param {params}    文件流需要的参数字段名，好像这儿随便来一个就可以了，但是不能没有;2017/4/28 修改为服务器处理的 input 数据
     * @param filePath    文件本地路径
     * @param mimeType    文件类型
     * @param isPic       是否上传的图片，这样在获取mime时，可以方便一点
     * @param photoHeight 图片高度
     * @param photoWidth  图片宽度
     * @return
     */
    Observable<BaseJson<Integer>> upLoadSingleFileV2(String filePath, String mimeType, boolean isPic, int photoWidth, int photoHeight);

    /**
     * 校验文件hash
     *
     * @param hash 文件 MD5 hash
     * @return
     */
    Observable<BaseJsonV2> checkStorageHash(String hash);

    /**
     * 上传个人头像
     *
     * @param filePath
     * @return
     */
    Observable<Object> uploadAvatar(String filePath);

    /**
     * 上传个人背景封面
     *
     * @param filePath
     * @return
     */
    Observable<Object> uploadBg(String filePath);
}
