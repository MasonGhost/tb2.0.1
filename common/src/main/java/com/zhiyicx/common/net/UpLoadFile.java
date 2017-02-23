package com.zhiyicx.common.net;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @author LiuChao
 * @describe 处理上传文件时的参数，将它封装成okHttp的Part类型
 * @date 2017/1/18
 * @contact email:450127106@qq.com
 */

public class UpLoadFile {
    /**
     * 上传单个文件
     *
     * @param params   上传文件的参数
     * @param filePath 文件的本地路径
     */
    public static Map<String, RequestBody> uploadSingleFile(String params, String filePath) {
        return uploadSingleFile(params, new File(filePath));
    }

    public static Map<String, RequestBody> uploadSingleFile(String params, File file) {
        /*MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);//表单类型
        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        builder.addFormDataPart(params, file.getName(), imageBody);//imgfile 后台接收图片流的参数名
        return builder.build().part(0);*/
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("pic", requestBody);
        return map;
    }

    /**
     * 上传文件多个文件
     */
    public static List<MultipartBody.Part> upLoadMultiFile(Map<String, String> filePathList) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);//表单类型
        if (filePathList != null) {
            Set<String> filePathKey = filePathList.keySet();
            for (String fileParam : filePathKey) {
                try {
                    File file = new File(filePathList.get(fileParam));//filePath 图片地址
                    RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    builder.addFormDataPart(fileParam, file.getName(), imageBody);//imgfile 后台接收图片流的参数名
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
        List<MultipartBody.Part> parts = builder.build().parts();
        return parts;
    }
}
