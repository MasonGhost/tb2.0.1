package com.zhiyicx.appupdate;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.allenliu.versionchecklib.core.AVersionService;
import com.allenliu.versionchecklib.utils.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhiyicx.common.utils.SharePreferenceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Describe 版本更新后台处理
 * @Author Jungle68
 * @Date 2017/10/17
 * @Contact master.jungle68@gmail.com
 */
public class UpdateService extends AVersionService {
    public UpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onResponses(AVersionService service, String response) {
        List<AppVersionBean> appVersionBean;
        try {
            appVersionBean = new Gson().fromJson(response, new TypeToken<List<AppVersionBean>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
            stopSelf();
            return;
        }
        //可以在判断版本之后在设置是否强制更新或者VersionParams
        if (appVersionBean != null
                && !appVersionBean.isEmpty()
                && !checkIsAbord(getApplicationContext(), appVersionBean.get(0).getVersion_code())
                && AppUtils.getVersionCode(getApplicationContext()) < appVersionBean.get(0).getVersion_code()) {
            CustomVersionDialogActivity.isForceUpdate = appVersionBean.get(0).getIs_forced() > 0;
            Bundle bundle = new Bundle();
            bundle.putParcelable(CustomVersionDialogActivity.BUNDLE_VERSIONDATA, appVersionBean.get(0));
            showVersionDialog(appVersionBean.get(0).getLink(), "检测到新版本", appVersionBean.get(0).getDescription(), bundle);

        } else {
            stopSelf();

        }
    }

    /**
     * 检查版本是否被忽略
     *
     * @return
     */
    public boolean checkIsAbord(Context context, int verstionCode) {
        ArrayList<Integer> abordVersions = SharePreferenceUtils.getObject(context, CustomVersionDialogActivity.SHAREPREFERENCE_TAG_ABORD_VERION);

        return abordVersions != null && abordVersions.contains(verstionCode);

    }


}
