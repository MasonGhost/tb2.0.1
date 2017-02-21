package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.common.base.BaseJson;

import java.util.Map;

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
     * @return
     */
    Observable<BaseJson<Integer>> upLoadSingleFile(String hash, String fileName, String params, String filePath);
}
