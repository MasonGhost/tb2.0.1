package com.zhiyicx.thinksnsplus.modules.commontest;

import android.support.test.rule.ActivityTestRule;
import android.text.TextUtils;

import com.zhiyicx.common.base.BaseApplication;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.StorageTaskBean;
import com.zhiyicx.thinksnsplus.data.source.remote.CommonClient;
import com.zhiyicx.thinksnsplus.modules.AcitivityTest;
import com.zhiyicx.thinksnsplus.modules.RxUnitTestTools;
import com.zhiyicx.thinksnsplus.modules.register.RegisterActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static junit.framework.Assert.assertTrue;

/**
 * @author LiuChao
 * @describe
 * @date 2017/1/14
 * @contact email:450127106@qq.com
 */

public class CommonTest extends AcitivityTest {
    private CommonClient mCommonClient;
    @Rule
    public ActivityTestRule<RegisterActivity> mLoginActivityActivityTestRule = new ActivityTestRule<RegisterActivity>(RegisterActivity.class);

    @Before
    public void init() {
        RxUnitTestTools.openRxTools();
        mCommonClient = AppApplication.AppComponentHolder.getAppComponent().serviceManager().getCommonClient();
    }

    /**
     * 备注：此方法单独测试可行，一起运行会
     * 出现错误 Waited for the root of the view hierarchy to have window focus and not be requesting layout for over 10 seconds
     * summary token过期，跳转到登陆页面
     * steps  1.请求rap测试接口，返回token过期  2.判断是登陆界面上的界面是否可见
     * expected  登陆界面的控件可见
     */
    @Test
    public void tokenExpierd() throws Exception {
//        RxUnitTestTools.closeRxTools();
//        mCommonClient.testTokenExpierd("needRefresh")
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<BaseJson>() {
//                    @Override
//                    public void call(BaseJson baseJson) {
//
//                    }
//                });
//        Thread.sleep(3000);
//        // System.out.println("tokenTest==>"+baseJson);
//        int titleId = AppApplication.getContext().getResources()
//                .getIdentifier("alertTitle", "id", "android");
//        findViewById(titleId)
//                .inRoot(isDialog())
//                .check(matches(withText(R.string.token_expiers)))
//                .check(matches(isDisplayed()));
                        /*ViewInteraction etPhone = findViewById(R.id.et_login_phone);
                        etPhone.check(matches(isDisplayed()));*/
    }

    /**
     * summary 测试通过标识符获取资源，没找到返回0
     * steps
     * expected
     */
    @Test
    public void getResourceByName() throws Exception {
        String codeName = "code_" + 999;
        int id = UIUtils.getResourceByName(codeName, "string", BaseApplication.getContext());
        assertTrue(id == 0);
    }

    /**
     * summary 文件上传创建存储任务，文件已经存在，秒传
     * steps
     * expected 得到的Storage_id>0
     */
    @Test
    public void createStorageTaskExist() throws Exception {
        mCommonClient.createStorageTask("hash", "origin_filename", "exist")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseJson<StorageTaskBean>>() {
                    @Override
                    public void call(BaseJson<StorageTaskBean> storageTaskBeanBaseJson) {
                        StorageTaskBean storage = storageTaskBeanBaseJson.getData();
                        // 妙传结果
                        assertTrue(storageTaskBeanBaseJson.isStatus() && storage.getStorage_id() > 0);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        assertTrue(throwable == null);
                    }
                });
    }

    /**
     * summary 创建存储任务成功
     * steps
     * expected 返回url不为空，并且storageid=0
     */
    @Test
    public void creatStorageTaskSuccess() throws Exception {
        mCommonClient.createStorageTask("hash", "origin_filename", "next")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseJson<StorageTaskBean>>() {
                    @Override
                    public void call(BaseJson<StorageTaskBean> storageTaskBeanBaseJson) {
                        StorageTaskBean storage = storageTaskBeanBaseJson.getData();
                        // 创建任务成功
                        assertTrue(storageTaskBeanBaseJson.isStatus() && storage.getStorage_id() == 0 && !TextUtils.isEmpty(storage.getUri()));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        assertTrue(throwable == null);
                    }
                });
    }

    /**
     * summary 创建储存任务失败
     * steps
     * expected 返回code 9999
     */
    @Test
    public void creatStorageTaskFailure() throws Exception {

        mCommonClient.createStorageTask("hash", "origin_filename", "error")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseJson<StorageTaskBean>>() {
                    @Override
                    public void call(BaseJson<StorageTaskBean> storageTaskBeanBaseJson) {
                        StorageTaskBean storage = storageTaskBeanBaseJson.getData();
                        // 创建任务失败
                        assertTrue(!storageTaskBeanBaseJson.isStatus() && storageTaskBeanBaseJson.getCode() == 9999);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        assertTrue(throwable == null);
                    }
                });
    }

    /**
     * summary 获取上传任务成功的通知
     * steps
     * expected
     */
    @Test
    public void getNotifyStorageTaskSuccess() throws Exception {
        mCommonClient.notifyStorageTask("storage_task_id", "test", "success")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseJson>() {
                    @Override
                    public void call(BaseJson baseJson) {
                        assertTrue(baseJson.isStatus());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        assertTrue(throwable == null);
                    }
                });
    }

    /**
     * summary 删除存储任务成功
     * steps
     * expected
     */
    @Test
    public void deleteStorageTaskSuccess() throws Exception {
        mCommonClient.deleteStorageTask("storage_task_id", "success")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseJson>() {
                    @Override
                    public void call(BaseJson baseJson) {
                        assertTrue(baseJson.isStatus());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        assertTrue(throwable == null);
                    }
                });

    }

}
