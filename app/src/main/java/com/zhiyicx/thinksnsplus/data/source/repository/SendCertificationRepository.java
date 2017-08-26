package com.zhiyicx.thinksnsplus.data.source.repository;

import com.google.gson.Gson;

import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.data.beans.SendCertificationBean;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.remote.UserInfoClient;
import com.zhiyicx.thinksnsplus.modules.certification.send.SendCertificationContract;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okhttp3.RequestBody;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.FuncN;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/3
 * @contact email:648129313@qq.com
 */

public class SendCertificationRepository implements SendCertificationContract.Repository{

    @Inject
    UpLoadRepository mUpLoadRepository;

    private UserInfoClient mUserInfoClient;

    @Inject
    public SendCertificationRepository(ServiceManager manager) {
        mUserInfoClient = manager.getUserInfoClient();
    }

    @Override
    public Observable<BaseJsonV2<Object>> sendCertification(SendCertificationBean bean) {
        List<ImageBean> photos = bean.getPicList();
        // 有图片需要上传时：先处理图片上传任务，成功后，获取任务id，发布动态
        // 先处理图片上传，图片上传成功后，在进行动态发布
        List<Observable<BaseJson<Integer>>> upLoadPics = new ArrayList<>();
        for (int i = 0; i < photos.size(); i++) {
            ImageBean imageBean = photos.get(i);
            String filePath = imageBean.getImgUrl();
            int photoWidth = (int) imageBean.getWidth();
            int photoHeight = (int) imageBean.getHeight();
            String photoMimeType = imageBean.getImgMimeType();
            upLoadPics.add(mUpLoadRepository.upLoadSingleFileV2(filePath, photoMimeType, true, photoWidth, photoHeight));
        }
        return Observable.zip(upLoadPics, (FuncN<Object>) args -> {
            List<Integer> integers = new ArrayList<>();
            for (int i = 0; i < args.length; i++) {
                BaseJson<Integer> baseJson = (BaseJson<Integer>) args[i];
                if (baseJson.isStatus()) {
                    if (i == 0 && bean.getFiles() != null){
                        bean.getFiles().clear();
                    }
                    bean.getFiles().add(baseJson.getData());
                } else {
                    throw new NullPointerException();// 某一次失败就抛出异常，重传，因为有秒传功能所以不会浪费多少流量
                }
            }
            return integers;
        }).map(integers -> {
            bean.setPicList(null);
            return bean;
        }).flatMap(new Func1<SendCertificationBean, Observable<BaseJsonV2<Object>>>() {
            @Override
            public Observable<BaseJsonV2<Object>> call(SendCertificationBean bean) {
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(bean));
                LogUtils.d("Cathy", new Gson().toJson(bean));
                if (bean.isUpdate()){
                    return mUserInfoClient.updateUserCertificationInfo(body);
                } else {
                    return mUserInfoClient.sendUserCertificationInfo(body);
                }
            }
        });
    }
}
