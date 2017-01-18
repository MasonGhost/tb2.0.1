package com.zhiyicx.thinksnsplus.modules.commontest;

import android.text.TextUtils;

import com.zhiyicx.common.base.BaseApplication;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.StorageTaskBean;
import com.zhiyicx.thinksnsplus.data.source.remote.CommonClient;
import com.zhiyicx.thinksnsplus.data.source.remote.LoginClient;
import com.zhiyicx.thinksnsplus.modules.AcitivityTest;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * @author LiuChao
 * @describe
 * @date 2017/1/14
 * @contact email:450127106@qq.com
 */

public class CommonTest extends AcitivityTest {
    private CommonClient mCommonClient;

    @Before
    public void init() {
        mCommonClient = AppApplication.AppComponentHolder.getAppComponent().serviceManager().getCommonClient();
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
