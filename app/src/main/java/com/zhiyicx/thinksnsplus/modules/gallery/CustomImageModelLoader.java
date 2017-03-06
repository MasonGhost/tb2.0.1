package com.zhiyicx.thinksnsplus.modules.gallery;

import android.content.Context;

import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.imsdk.utils.common.DeviceUtils;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/3/4
 * @Contact master.jungle68@gmail.com
 */
public class CustomImageModelLoader extends BaseGlideUrlLoader<CustomImageSizeModel> {
    private Context mContext;

    public CustomImageModelLoader(Context context) {
        super(context);
        mContext = context.getApplicationContext();
    }

    @Override
    protected String getUrl(CustomImageSizeModel model, int width, int height) {
        if (model.getImageBean().getImgUrl() != null) {
            return model.getImageBean().getImgUrl();
        }
        double screenwith = DeviceUtils.getScreenWidth(mContext);
        int part = (int) (model.getImageBean().getWidth() / screenwith * 100);
        if (part > 100) {
            part = 100;
        }
        String url = String.format(ApiConfig.IMAGE_PATH, model.getImageBean().getStorage_id(), part);

        return url;
    }
}