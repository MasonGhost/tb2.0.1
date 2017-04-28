package com.zhiyicx.common.net;

import android.text.TextUtils;

import com.zhiyicx.common.utils.FileUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
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
    public static Map<String, RequestBody> uploadSingleFilePath(String params, String filePath, HashMap<String, String> data) {
        return uploadSingleFile(params, new File(filePath), data);
    }

    public static Map<String, RequestBody> uploadSingleFile(String params, File file, HashMap<String, String> data) {
        /*MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);//表单类型
        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        builder.addFormDataPart(params, file.getName(), imageBody);//imgfile 后台接收图片流的参数名
        return builder.build().part(0);*/
        String mimeType = FileUtils.getMimeTypeByFile(file);
        RequestBody requestBody = RequestBody.create(MediaType.parse(TextUtils.isEmpty(mimeType) ? "multipart/form-data" : mimeType), file);
        HashMap<String, RequestBody> map = new HashMap<>();
        map.put(params, requestBody);
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        map.put("options", builder.build());
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
                    String mimeType = FileUtils.getMimeTypeByFile(file);
                    RequestBody imageBody = RequestBody.create(
                            MediaType.parse(TextUtils.isEmpty(mimeType) ? "multipart/form-data" : mimeType), file);
                    builder.addFormDataPart(fileParam, file.getName(), imageBody);//imgfile 后台接收图片流的参数名
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
        List<MultipartBody.Part> parts = builder.build().parts();
        return parts;
    }

    /**
     * 上传文件以及参数携带，构造参数
     *
     * @param filePathList 携带的文件
     * @param params       携带的表单数据
     */
    public static List<MultipartBody.Part> upLoadFileAndParams(Map<String, String> filePathList, HashMap<String, Object> params) {

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);//表单类型
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                builder.addFormDataPart(entry.getKey(), entry.getValue().toString());//ParamKey.TOKEN 自定义参数key常量类，即参数名
            }
        }
        if (filePathList != null) {
            Set<String> filePathKey = filePathList.keySet();
            for (String fileParam : filePathKey) {
                try {
                    File file = new File(filePathList.get(fileParam));//filePath 图片地址
                    String mimeType = FileUtils.getMimeTypeByFile(file);
                    RequestBody imageBody = RequestBody.create(
                            MediaType.parse(TextUtils.isEmpty(mimeType) ? "multipart/form-data" : mimeType), file);
                    builder.addFormDataPart(fileParam,file.getName(), imageBody);//imgfile 后台接收图片流的参数名
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
        // 如果没有任何参数传入，又调用了该方法，需要传一个缺省参数， Multipart body must have at least one part.
        if ((params == null || params.isEmpty()) && (filePathList == null || filePathList.isEmpty())) {
            builder.addFormDataPart("hehe", "hehe");
        }
        List<MultipartBody.Part> parts = builder.build().parts();
        return parts;
    }
}
