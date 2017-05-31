package com.zhiyicx.thinksnsplus.modules.infomation.infomain;

import android.support.test.rule.ActivityTestRule;

import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeBean;
import com.zhiyicx.thinksnsplus.data.source.remote.InfoMainClient;
import com.zhiyicx.thinksnsplus.modules.AcitivityTest;
import com.zhiyicx.thinksnsplus.modules.RxUnitTestTools;
import com.zhiyicx.thinksnsplus.modules.information.infomain.InfoActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Author Jliuer
 * @Date 2017/03/14
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class InfoActivityTest extends AcitivityTest {

    InfoMainClient mInfoMainClient;

    @Rule
    public ActivityTestRule<InfoActivity> mActivityRule = new ActivityTestRule(InfoActivity.class);

    @Before
    public void initActivity() {
        RxUnitTestTools.openRxTools();
        mInfoMainClient = AppApplication.AppComponentHolder.getAppComponent().serviceManager()
                .getInfoMainClient();
    }

    @Test
    public void testGetInfoType() {
        mInfoMainClient.getInfoType()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<InfoTypeBean>() {
                    @Override
                    protected void onSuccess(InfoTypeBean data) {

                    }

                    @Override
                    protected void onFailure(String message, int code) {

                    }

                    @Override
                    protected void onException(Throwable throwable) {

                    }
                });
    }
}
