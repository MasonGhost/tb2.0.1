package com.zhiyicx.thinksnsplus.service.backgroundtask;

import android.app.Application;

import com.google.gson.Gson;
import com.zhiyicx.baseproject.cache.CacheBean;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.net.UpLoadFile;
import com.zhiyicx.common.utils.ActivityHandler;
import com.zhiyicx.common.utils.NetUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.imsdk.entity.IMConfig;
import com.zhiyicx.imsdk.receiver.NetChangeReceiver;
import com.zhiyicx.rxerrorhandler.functions.RetryWithInterceptDelay;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.ErrorCodeConfig;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentToll;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.GroupSendDynamicDataBean;
import com.zhiyicx.thinksnsplus.data.beans.IMBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.SendDynamicDataBean;
import com.zhiyicx.thinksnsplus.data.beans.SendDynamicDataBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.BackgroundRequestTaskBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicCommentBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicDetailBeanV2GreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.InfoCommentListBeanDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.SendDynamicDataBeanV2GreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.SendDynamicRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.SystemRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UpLoadRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.inject.Inject;

import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_SEND_COMMENT_TO_DYNAMIC_LIST;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_SEND_COMMENT_TO_INFO_LIST;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_SEND_DYNAMIC_TO_LIST;

/**
 * @Describe 后台任务处理逻辑
 * @Author Jungle68
 * @Date 2017/1/22
 * @Contact master.jungle68@gmail.com
 */

public class BackgroundTaskHandler {
    private static final int RETRY_MAX_COUNT = 3; // 最大重试次
    public static final String NET_CALLBACK = "net_callback";
    private static final int RETRY_INTERVAL_TIME = 5; // 循环间隔时间 单位 s
    private static final long MESSAGE_SEND_INTERVAL_FOR_CPU = 1000;// 消息发送间隔时间，防止 cpu 占用过高
    private static final long MESSAGE_SEND_INTERVAL_FOR_CPU_TIME_OUT = 3000;// 消息发送间隔时间，防止 cpu 占用过高

    @Inject
    Application mContext;
    @Inject
    ServiceManager mServiceManager;
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;
    @Inject
    BackgroundRequestTaskBeanGreenDaoImpl mBackgroundRequestTaskBeanGreenDao;
    @Inject
    AuthRepository mAuthRepository;
    @Inject
    SystemRepository mSystemRepository;
    @Inject
    UserInfoRepository mUserInfoRepository;
    @Inject
    SendDynamicRepository mSendDynamicRepository;
    @Inject
    UpLoadRepository mUpLoadRepository;
    @Inject
    DynamicBeanGreenDaoImpl mDynamicBeanGreenDao;
    @Inject
    DynamicCommentBeanGreenDaoImpl mDynamicCommentBeanGreenDao;
    @Inject
    InfoCommentListBeanDaoImpl mInfoCommentListBeanDao;

    @Inject
    DynamicDetailBeanV2GreenDaoImpl mDynamicDetailBeanV2GreenDao;

    @Inject
    SendDynamicDataBeanV2GreenDaoImpl mSendDynamicDataBeanV2Dao;

    private Queue<BackgroundRequestTaskBean> mTaskBeanConcurrentLinkedQueue = new ConcurrentLinkedQueue<>();// 线程安全的队列

//    private List<BackgroundRequestTaskBean> mBackgroundRequestTaskBeanCaches = new ArrayList<>();

    private boolean mIsExit = false; // 是否关闭

    private boolean mIsNetConnected = false;

    private Thread mBackTaskDealThread;

    public BackgroundTaskHandler() {
        init();
    }

    /**
     * 加入任务
     *
     * @param backgroundRequestTaskBean 任务
     * @return 如果任务为 null，返回 false,否者返回 true
     */
    public boolean addBackgroundRequestTask(BackgroundRequestTaskBean backgroundRequestTaskBean) {
        if (backgroundRequestTaskBean == null) {
            return false;
        }
        mIsExit = false;
        if (mBackTaskDealThread == null) {
            mBackTaskDealThread = new Thread(handleTaskRunnable);
        }
        if (!mBackTaskDealThread.isAlive()) {
            mBackTaskDealThread.getState();
        }
        if (mTaskBeanConcurrentLinkedQueue.add(backgroundRequestTaskBean)) {
            mBackgroundRequestTaskBeanGreenDao.insertOrReplace(backgroundRequestTaskBean);
            return true;
        }
        return false;
    }

    /**
     * 停止处理任务
     */
    public void stopTask() {
        mIsExit = true;
    }

    private void init() {
        AppApplication.AppComponentHolder.getAppComponent().inject(this);
        getCacheData();
        mBackTaskDealThread = new Thread(handleTaskRunnable);
        mBackTaskDealThread.start();
        EventBus.getDefault().register(this);
        mIsNetConnected = NetUtils.netIsConnected(mContext);
    }

    /**
     * 网络变化监听，暂时不需要 ，配合 Evnetbus 使用
     */
    @Subscriber(tag = NetChangeReceiver.EVENT_NETWORK_CONNECTED)
    public void onNetConnected() {
        mIsNetConnected = true;
    }


    /**
     * 获取缓存中没有被执行的数据
     */
    private void getCacheData() {
        if (AppApplication.getmCurrentLoginAuth() == null) {
            return;
        }
        List<BackgroundRequestTaskBean> cacheDatas = mBackgroundRequestTaskBeanGreenDao.getMultiDataFromCacheByUserId(Long.valueOf(AppApplication.getmCurrentLoginAuth().getUser_id()));
        if (cacheDatas != null) {
            for (BackgroundRequestTaskBean tmp : cacheDatas) {
                mTaskBeanConcurrentLinkedQueue.add(tmp);
            }
        }
    }

    /**
     * 处理任务线程
     */
    private Runnable handleTaskRunnable = new Runnable() {
        @Override
        public void run() {

            while (!mIsExit && ActivityHandler.getInstance().getActivityStack() != null) {
//                LogUtils.d("---------backTask------- ");
                if (mIsNetConnected && !mTaskBeanConcurrentLinkedQueue.isEmpty()) {
                    BackgroundRequestTaskBean backgroundRequestTaskBean = mTaskBeanConcurrentLinkedQueue.poll();
                    handleTask(backgroundRequestTaskBean);
                }
                threadSleep();
            }
            mIsExit = true;
//            // 存储未执行的数据到数据库，下次执行
//            if (mBackgroundRequestTaskBeanCaches.size() > 0) {
//                mBackgroundRequestTaskBeanGreenDao.saveMultiData(mBackgroundRequestTaskBeanCaches);
//            }
            // 取消 event 监听
            EventBus.getDefault().unregister(BackgroundTaskHandler.this);
        }
    };

    /**
     * 线程休息
     */
    private void threadSleep() {
        //防止cpu占用过高
        try {
            if (mIsNetConnected) {
                Thread.sleep(MESSAGE_SEND_INTERVAL_FOR_CPU);
            } else {
                Thread.sleep(MESSAGE_SEND_INTERVAL_FOR_CPU_TIME_OUT);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理后台任务，用户信息和 IM 需要对返回数据处理，其他请求只是通知服务器，不需要做后续操作
     *
     * @param backgroundRequestTaskBean 后台任务
     */
    private void handleTask(final BackgroundRequestTaskBean backgroundRequestTaskBean) {

        switch (backgroundRequestTaskBean.getMethodType()) {
            /**
             * 通用 POST 接口处理
             */
            case POST:
                if (backgroundRequestTaskBean.getMax_retry_count() - 1 <= 0) {
                    EventBus.getDefault().post(backgroundRequestTaskBean, EventBusTagConfig.EVENT_BACKGROUND_TASK_CANT_NOT_DEAL);
                    return;
                }
                backgroundRequestTaskBean.setMax_retry_count(backgroundRequestTaskBean.getMax_retry_count() - 1);
                postMethod(backgroundRequestTaskBean);
                break;

            case POST_V2:
                if (backgroundRequestTaskBean.getMax_retry_count() - 1 <= 0) {
                    EventBus.getDefault().post(backgroundRequestTaskBean, EventBusTagConfig.EVENT_BACKGROUND_TASK_CANT_NOT_DEAL);
                    return;
                }
                backgroundRequestTaskBean.setMax_retry_count(backgroundRequestTaskBean.getMax_retry_count() - 1);
                postMethodV2(backgroundRequestTaskBean);
                break;
            /**
             * 通用 GET 接口处理
             */
            case GET:

                if (backgroundRequestTaskBean.getMax_retry_count() - 1 <= 0) {
                    EventBus.getDefault().post(backgroundRequestTaskBean, EventBusTagConfig.EVENT_BACKGROUND_TASK_CANT_NOT_DEAL);
                    return;
                }
                backgroundRequestTaskBean.setMax_retry_count(backgroundRequestTaskBean.getMax_retry_count() - 1);
                break;

            case PATCH:

                if (backgroundRequestTaskBean.getMax_retry_count() - 1 <= 0) {
                    EventBus.getDefault().post(backgroundRequestTaskBean, EventBusTagConfig.EVENT_BACKGROUND_TASK_CANT_NOT_DEAL);
                    return;
                }
                backgroundRequestTaskBean.setMax_retry_count(backgroundRequestTaskBean.getMax_retry_count() - 1);
                PatchMethod(backgroundRequestTaskBean);
                break;
            /**
             * 通用 DELETE 接口处理
             */
            case DELETE:
                if (backgroundRequestTaskBean.getMax_retry_count() - 1 <= 0) {
                    EventBus.getDefault().post(backgroundRequestTaskBean, EventBusTagConfig.EVENT_BACKGROUND_TASK_CANT_NOT_DEAL);
                    return;
                }
                backgroundRequestTaskBean.setMax_retry_count(backgroundRequestTaskBean.getMax_retry_count() - 1);
                deleteMethod(backgroundRequestTaskBean);
                break;
            case DELETE_V2:
                if (backgroundRequestTaskBean.getMax_retry_count() - 1 <= 0) {
                    EventBus.getDefault().post(backgroundRequestTaskBean, EventBusTagConfig.EVENT_BACKGROUND_TASK_CANT_NOT_DEAL);
                    return;
                }
                backgroundRequestTaskBean.setMax_retry_count(backgroundRequestTaskBean.getMax_retry_count() - 1);
                deleteMethodV2(backgroundRequestTaskBean);
                break;
            /**
             * 获取 IM 信息，必须保证 header 中已经加入了权限 token
             */
            case GET_IM_INFO:

                if (backgroundRequestTaskBean.getMax_retry_count() - 1 <= 0) {
                    EventBus.getDefault().post(backgroundRequestTaskBean, EventBusTagConfig.EVENT_BACKGROUND_TASK_CANT_NOT_DEAL);
                    return;
                }
                backgroundRequestTaskBean.setMax_retry_count(backgroundRequestTaskBean.getMax_retry_count() - 1);

                getIMInfo(backgroundRequestTaskBean);
                break;
            /**
             * 获取用户信息
             */
            case GET_USER_INFO:

                if (backgroundRequestTaskBean.getMax_retry_count() - 1 <= 0) {
                    EventBus.getDefault().post(backgroundRequestTaskBean, EventBusTagConfig.EVENT_BACKGROUND_TASK_CANT_NOT_DEAL);
                    return;
                }
                backgroundRequestTaskBean.setMax_retry_count(backgroundRequestTaskBean.getMax_retry_count() - 1);
                getUserInfo(backgroundRequestTaskBean);
                break;
            /**
             * 发送动态
             */
            case SEND_DYNAMIC:
                sendDynamic(backgroundRequestTaskBean);
                break;

            /**
             * 发送动态 V2 api
             */
            case SEND_DYNAMIC_V2:
                sendDynamicV2(backgroundRequestTaskBean);
                break;

            case SEND_GROUP_DYNAMIC:
                sendGroupDynamic(backgroundRequestTaskBean);
                break;

            /**
             * 设置动态评论收费 V2 api
             */
            case TOLL_DYNAMIC_COMMENT_V2:
                setTollDynamicComment(backgroundRequestTaskBean);
                break;

            case SEND_COMMENT:
                sendComment(backgroundRequestTaskBean);
                break;
            case SEND_INFO_COMMENT:
                sendInfoComment(backgroundRequestTaskBean);
                break;
            default:
        }

    }

    /**
     * 处理Post请求类型的后台任务
     */
    private void postMethodV2(final BackgroundRequestTaskBean backgroundRequestTaskBean) {
        HashMap params = backgroundRequestTaskBean.getParams();
        if (params == null) {
            params = new HashMap();
        }
        final OnNetResponseCallBack callBack = (OnNetResponseCallBack) params.get(NET_CALLBACK);
        params.remove(NET_CALLBACK);
        mServiceManager.getCommonClient().handleBackGroundTaskPostV2(backgroundRequestTaskBean.getPath(), UpLoadFile.upLoadFileAndParams(null, params))
                .subscribe(new BaseSubscribeForV2() {
                    @Override
                    protected void onSuccess(Object data) {
                        if (callBack != null) {
                            callBack.onSuccess(data);
                        }
                        mBackgroundRequestTaskBeanGreenDao.deleteSingleCache(backgroundRequestTaskBean);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        if (checkIsNeedReRequest(code)) {
                            addBackgroundRequestTask(backgroundRequestTaskBean);
                        } else {
                            mBackgroundRequestTaskBeanGreenDao.deleteSingleCache(backgroundRequestTaskBean);
                        }
                        if (callBack != null) {
                            callBack.onFailure(message, code);
                        }
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        addBackgroundRequestTask(backgroundRequestTaskBean);
                        if (callBack != null) {
                            callBack.onException(throwable);
                        }
                    }
                });
    }

    /**
     * 处理Post请求类型的后台任务
     */
    private void postMethod(final BackgroundRequestTaskBean backgroundRequestTaskBean) {
        HashMap params = backgroundRequestTaskBean.getParams();
        if (params == null) {
            params = new HashMap();
        }
        final OnNetResponseCallBack callBack = (OnNetResponseCallBack) params.get(NET_CALLBACK);
        params.remove(NET_CALLBACK);
        mServiceManager.getCommonClient().handleBackGroundTaskPost(backgroundRequestTaskBean.getPath(), UpLoadFile.upLoadFileAndParams(null, params))
                .subscribe(new BaseSubscribe<Object>() {
                    @Override
                    protected void onSuccess(Object data) {
                        if (callBack != null) {
                            callBack.onSuccess(data);
                        }
                        mBackgroundRequestTaskBeanGreenDao.deleteSingleCache(backgroundRequestTaskBean);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        if (checkIsNeedReRequest(code)) {
                            addBackgroundRequestTask(backgroundRequestTaskBean);
                        } else {
                            mBackgroundRequestTaskBeanGreenDao.deleteSingleCache(backgroundRequestTaskBean);
                        }
                        if (callBack != null) {
                            callBack.onFailure(message, code);
                        }
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        addBackgroundRequestTask(backgroundRequestTaskBean);
                        if (callBack != null) {
                            callBack.onException(throwable);
                        }
                    }
                });
    }

    /**
     * 处理Patch请求类型的后台任务
     */
    private void PatchMethod(final BackgroundRequestTaskBean backgroundRequestTaskBean) {

        mServiceManager.getCommonClient().handleBackGroundTaskPatch(backgroundRequestTaskBean.getPath(), UpLoadFile.upLoadFileAndParams(null, backgroundRequestTaskBean.getParams()))
                .subscribe(new BaseSubscribe<Object>() {
                    @Override
                    protected void onSuccess(Object data) {
                        mBackgroundRequestTaskBeanGreenDao.deleteSingleCache(backgroundRequestTaskBean);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        if (checkIsNeedReRequest(code)) {
                            addBackgroundRequestTask(backgroundRequestTaskBean);
                        } else {
                            mBackgroundRequestTaskBeanGreenDao.deleteSingleCache(backgroundRequestTaskBean);
                        }
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        addBackgroundRequestTask(backgroundRequestTaskBean);
                    }
                });
    }

    /**
     * 处理Delete请求类型的后台任务
     */
    private void deleteMethod(final BackgroundRequestTaskBean backgroundRequestTaskBean) {
        HashMap<String, Object> datas = backgroundRequestTaskBean.getParams();
        if (datas == null) {
            datas = new HashMap();
        }
        final OnNetResponseCallBack callBack = (OnNetResponseCallBack) datas.get(NET_CALLBACK);
        datas.remove(NET_CALLBACK);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(datas));
        mServiceManager.getCommonClient().handleBackGroudTaskDelete(backgroundRequestTaskBean.getPath()
                , body)
                .subscribe(new BaseSubscribe<CacheBean>() {
                    @Override
                    protected void onSuccess(CacheBean data) {
                        mBackgroundRequestTaskBeanGreenDao.deleteSingleCache(backgroundRequestTaskBean);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        if (checkIsNeedReRequest(code)) {
                            addBackgroundRequestTask(backgroundRequestTaskBean);
                        } else {
                            mBackgroundRequestTaskBeanGreenDao.deleteSingleCache(backgroundRequestTaskBean);
                        }
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        addBackgroundRequestTask(backgroundRequestTaskBean);
                    }
                });
    }

    /**
     * 处理Delete请求类型的后台任务 V2
     */
    private void deleteMethodV2(final BackgroundRequestTaskBean backgroundRequestTaskBean) {
        HashMap<String, Object> datas = backgroundRequestTaskBean.getParams();
        if (datas == null) {
            datas = new HashMap<>();
        }
        final OnNetResponseCallBack callBack = (OnNetResponseCallBack) datas.get(NET_CALLBACK);
        datas.remove(NET_CALLBACK);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(datas));
        mServiceManager.getCommonClient().handleBackGroudTaskDeleteV2(backgroundRequestTaskBean.getPath()
                , body)
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<CacheBean>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<CacheBean> data) {
                        mBackgroundRequestTaskBeanGreenDao.deleteSingleCache(backgroundRequestTaskBean);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        if (checkIsNeedReRequest(code)) {
                            addBackgroundRequestTask(backgroundRequestTaskBean);
                        } else {
                            mBackgroundRequestTaskBeanGreenDao.deleteSingleCache(backgroundRequestTaskBean);
                        }
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        addBackgroundRequestTask(backgroundRequestTaskBean);
                    }
                });
    }

    /**
     * 获取im信息
     */
    private void getIMInfo(final BackgroundRequestTaskBean backgroundRequestTaskBean) {
        mAuthRepository.getImInfo()
                .subscribe(new BaseSubscribe<IMBean>() {
                    @Override
                    protected void onSuccess(IMBean data) {
                        mBackgroundRequestTaskBeanGreenDao.deleteSingleCache(backgroundRequestTaskBean);
                        LogUtils.d("-----login-----imConfig--------" + data.toString());
                        IMConfig imConfig = new IMConfig();
                        imConfig.setImUid(data.getUser_id());
                        imConfig.setToken(data.getIm_password());
                        imConfig.setWeb_socket_authority("ws://" + mSystemRepository.getBootstrappersInfoFromLocal().getIm_serve());
                        mAuthRepository.saveIMConfig(imConfig);
                        mAuthRepository.loginIM();
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        if (checkIsNeedReRequest(code)) {
                            addBackgroundRequestTask(backgroundRequestTaskBean);
                        } else {
                            mBackgroundRequestTaskBeanGreenDao.deleteSingleCache(backgroundRequestTaskBean);
                        }
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        addBackgroundRequestTask(backgroundRequestTaskBean);
                    }
                });
    }

    /**
     * 处理用户信息的后台任务
     */

    private void getUserInfo(final BackgroundRequestTaskBean backgroundRequestTaskBean) {
        if (backgroundRequestTaskBean.getParams() == null || backgroundRequestTaskBean.getParams().get("user_id") == null) {
            return;
        }
        List<Object> integers = new ArrayList<>();
        if (backgroundRequestTaskBean.getParams().get("user_id") instanceof List) {
            integers.addAll((Collection<? extends Long>) backgroundRequestTaskBean.getParams().get("user_id"));
        } else {
            integers.add(Long.valueOf(backgroundRequestTaskBean.getParams().get("user_id") + ""));
        }
        mUserInfoRepository.getUserInfo(integers)
                .subscribe(new BaseSubscribe<List<UserInfoBean>>() {
                    @Override
                    protected void onSuccess(List<UserInfoBean> data) {
                        mBackgroundRequestTaskBeanGreenDao.deleteSingleCache(backgroundRequestTaskBean);
                        mUserInfoBeanGreenDao.insertOrReplace(data);
                        // 用户信息获取成功后就可以通知界面刷新了
                        EventBus.getDefault().post(data, EventBusTagConfig.EVENT_USERINFO_UPDATE);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        if (checkIsNeedReRequest(code)) {
                            addBackgroundRequestTask(backgroundRequestTaskBean);
                        } else {
                            mBackgroundRequestTaskBeanGreenDao.deleteSingleCache(backgroundRequestTaskBean);
                        }
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        throwable.printStackTrace();
                        addBackgroundRequestTask(backgroundRequestTaskBean);
                    }
                });
    }

    private void setTollDynamicComment(final BackgroundRequestTaskBean backgroundRequestTaskBean) {
        final HashMap<String, Object> params = backgroundRequestTaskBean.getParams();
        final Long feed_Id = (Long) params.get("feed_id");
        final int amount = (int) params.get("amount");

        final DynamicDetailBeanV2 dynamicDetailBeanV2 = mDynamicDetailBeanV2GreenDao.getDynamicByFeedId(feed_Id);
        if (dynamicDetailBeanV2 == null) {
            mBackgroundRequestTaskBeanGreenDao.deleteSingleCache(backgroundRequestTaskBean);
            return;
        }
        mSendDynamicRepository.setDynamicCommentToll(feed_Id, amount).subscribe(new BaseSubscribeForV2<DynamicCommentToll>() {
            @Override
            protected void onSuccess(DynamicCommentToll data) {
            }

            @Override
            protected void onFailure(String message, int code) {
                super.onFailure(message, code);
            }

            @Override
            protected void onException(Throwable throwable) {
                super.onException(throwable);
            }
        });

    }

    /**
     * 处理动态发送的后台任务
     */
    private void sendDynamic(final BackgroundRequestTaskBean backgroundRequestTaskBean) {
        final HashMap<String, Object> params = backgroundRequestTaskBean.getParams();
        final Long feedMark = (Long) params.get("params");
        SendDynamicDataBean sendDynamicDataBean = (SendDynamicDataBean) params.get("sendDynamicDataBean");
        final int dynamicBelong;
        final long channel_id;
        if (sendDynamicDataBean != null) {
            dynamicBelong = sendDynamicDataBean.getDynamicBelong();
            channel_id = sendDynamicDataBean.getDynamicChannlId();

        } else {
            dynamicBelong = SendDynamicDataBean.MORMAL_DYNAMIC;
            channel_id = 0;
        }
        final DynamicBean dynamicBean;
        if (dynamicBelong == SendDynamicDataBean.CHANNEL_DYNAMIC) {
            dynamicBean = (DynamicBean) params.get("dynamicbean");
        } else {
            dynamicBean = mDynamicBeanGreenDao.getDynamicByFeedMark(feedMark);
        }
        if (dynamicBean == null) {
            mBackgroundRequestTaskBeanGreenDao.deleteSingleCache(backgroundRequestTaskBean);
            return;
        }
        // 发送动态到动态列表：状态为发送中
        dynamicBean.setState(DynamicBean.SEND_ING);

        // 存入数据库
        // ....
        final DynamicDetailBean dynamicDetailBean = dynamicBean.getFeed();
        List<ImageBean> photos = dynamicDetailBean.getStorages();
        Observable<BaseJson<Object>> observable = null;
        // 有图片需要上传时：先处理图片上传任务，成功后，获取任务id，发布动态
        if (photos != null && !photos.isEmpty()) {
            // 先处理图片上传，图片上传成功后，在进行动态发布
            List<Observable<BaseJson<Integer>>> upLoadPics = new ArrayList<>();
            for (int i = 0; i < photos.size(); i++) {
                ImageBean imageBean = photos.get(i);
                String filePath = imageBean.getImgUrl();
                int photoWidth = (int) imageBean.getWidth();
                int photoHeight = (int) imageBean.getHeight();
                String photoMimeType = imageBean.getImgMimeType();
                upLoadPics.add(mUpLoadRepository.upLoadSingleFile(filePath, photoMimeType, true, photoWidth, photoHeight));
            }
            observable = // 组合多个图片上传任务
                    Observable.combineLatest(upLoadPics, args -> {
                        // 得到图片上传的结果
                        List<Integer> integers = new ArrayList<>();
                        for (Object obj : args) {
                            BaseJson<Integer> baseJson = (BaseJson<Integer>) obj;
                            if (baseJson.isStatus()) {
                                integers.add(baseJson.getData());// 将返回的图片上传任务id封装好
                            } else {
                                throw new NullPointerException();// 某一次失败就抛出异常，重传，因为有秒传功能所以不会浪费多少流量
                            }
                        }
                        return integers;
                    }).flatMap(new Func1<List<Integer>, Observable<BaseJson<Object>>>() {
                        @Override
                        public Observable<BaseJson<Object>> call(List<Integer> integers) {
                            dynamicDetailBean.setStorage_task_ids(integers);
                            return mSendDynamicRepository.sendDynamic(dynamicDetailBean, dynamicBelong, channel_id);// 进行动态发布的请求
                        }
                    });
        } else {
            // 没有图片上传任务，直接发布动态
            observable = mSendDynamicRepository.sendDynamic(dynamicDetailBean, dynamicBelong, channel_id);// 进行动态发布的请求
        }
        observable.subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithInterceptDelay(RETRY_MAX_COUNT, RETRY_INTERVAL_TIME))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribe<Object>() {
                    @Override
                    protected void onSuccess(Object data) {
                        // 发送动态到动态列表：状态为发送成功
//                        sendDynamicByEventBus(dynamicBelong, dynamicBean, true, backgroundRequestTaskBean, data);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        // 发送动态到动态列表：状态为发送失败

//                        sendDynamicByEventBus(dynamicBelong, dynamicBean, false, backgroundRequestTaskBean, null);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        throwable.printStackTrace();
                        // 发送动态到动态列表：状态为发送失败
//                        sendDynamicByEventBus(dynamicBelong, dynamicBean, false, backgroundRequestTaskBean, null);
                    }
                });

    }

    /**
     * 处理动态发送的后台任务 V2
     */
    private void sendDynamicV2(final BackgroundRequestTaskBean backgroundRequestTaskBean) {
        final HashMap<String, Object> params = backgroundRequestTaskBean.getParams();
        final Long feedMark = (Long) params.get("params");
        final SendDynamicDataBeanV2 sendDynamicDataBean = (SendDynamicDataBeanV2) params.get("sendDynamicDataBean");
        final DynamicDetailBeanV2 detailBeanV2;
        detailBeanV2 = mDynamicDetailBeanV2GreenDao.getDynamicByFeedMark(feedMark);

        if (sendDynamicDataBean == null || detailBeanV2 == null) {
            mBackgroundRequestTaskBeanGreenDao.deleteSingleCache(backgroundRequestTaskBean);
            return;
        }
        mSendDynamicDataBeanV2Dao.insertOrReplace(sendDynamicDataBean);
        detailBeanV2.setState(DynamicDetailBeanV2.SEND_ING);


        // 存入数据库
        // ....
        List<ImageBean> photos = sendDynamicDataBean.getPhotos();
        Observable<BaseJson<Object>> observable;
        // 有图片需要上传时：先处理图片上传任务，成功后，获取任务id，发布动态
        if (photos != null && !photos.isEmpty()) {
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
            observable = // 组合多个图片上传任务
                    Observable.combineLatest(upLoadPics, args -> {
                        // 得到图片上传的结果
                        List<Integer> integers = new ArrayList<>();
                        for (int i = 0; i < args.length; i++) {
                            BaseJson<Integer> baseJson = (BaseJson<Integer>) args[i];
                            if (baseJson.isStatus()) {
                                sendDynamicDataBean.getStorage_task().get(i).setId(baseJson.getData());
                                integers.add(baseJson.getData());// 将返回的图片上传任务id封装好
                            } else {
                                throw new NullPointerException();// 某一次失败就抛出异常，重传，因为有秒传功能所以不会浪费多少流量
                            }
                        }
                        return integers;
                    }).map(integers -> {
                        sendDynamicDataBean.setPhotos(null);
                        return sendDynamicDataBean;
                    }).flatMap(new Func1<SendDynamicDataBeanV2, Observable<BaseJson<Object>>>() {
                        @Override
                        public Observable<BaseJson<Object>> call(SendDynamicDataBeanV2 sendDynamicDataBeanV2) {
                            return mSendDynamicRepository.sendDynamicV2(sendDynamicDataBeanV2)
                                    .flatMap(new Func1<BaseJsonV2<Object>, Observable<BaseJson<Object>>>() {
                                        @Override
                                        public Observable<BaseJson<Object>> call(BaseJsonV2<Object> objectBaseJsonV2) {
                                            BaseJson<Object> baseJson = new BaseJson<>();
                                            baseJson.setData((double) objectBaseJsonV2.getId());
                                            String msg = objectBaseJsonV2.getMessage().get(0);
                                            baseJson.setStatus(msg.equals("发布成功"));
                                            baseJson.setMessage(msg);
                                            return Observable.just(baseJson);
                                        }
                                    });
                        }
                    });
        } else {
            // 没有图片上传任务，直接发布动态
            observable = mSendDynamicRepository.sendDynamicV2(sendDynamicDataBean)
                    .flatMap(new Func1<BaseJsonV2<Object>, Observable<BaseJson<Object>>>() {
                        @Override
                        public Observable<BaseJson<Object>> call(BaseJsonV2<Object> objectBaseJsonV2) {
                            BaseJson<Object> baseJson = new BaseJson<>();
                            baseJson.setData((double) objectBaseJsonV2.getId());
                            String msg = objectBaseJsonV2.getMessage().get(0);
                            baseJson.setStatus(msg.equals("发布成功"));
                            baseJson.setMessage(msg);
                            return Observable.just(baseJson);
                        }
                    });
        }
        observable.subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithInterceptDelay(RETRY_MAX_COUNT, RETRY_INTERVAL_TIME))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribe<Object>() {
                    @Override
                    protected void onSuccess(Object data) {
                        // 发送动态到动态列表：状态为发送成功
                        sendDynamicByEventBus(SendDynamicDataBean.MORMAL_DYNAMIC, detailBeanV2, true, backgroundRequestTaskBean, data);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        // 发送动态到动态列表：状态为发送失败
                        sendDynamicByEventBus(SendDynamicDataBean.MORMAL_DYNAMIC, detailBeanV2, false, backgroundRequestTaskBean, null);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        throwable.printStackTrace();
                        // 发送动态到动态列表：状态为发送失败
                        sendDynamicByEventBus(SendDynamicDataBean.MORMAL_DYNAMIC, detailBeanV2, false, backgroundRequestTaskBean, null);
                    }
                });

    }

    private void sendGroupDynamic(final BackgroundRequestTaskBean backgroundRequestTaskBean) {
        final HashMap<String, Object> params = backgroundRequestTaskBean.getParams();
        final GroupSendDynamicDataBean sendDynamicDataBean = (GroupSendDynamicDataBean) params.get("sendDynamicDataBean");

        if (sendDynamicDataBean == null) {
            mBackgroundRequestTaskBeanGreenDao.deleteSingleCache(backgroundRequestTaskBean);
            return;
        }

        // 存入数据库
        // ....
        List<ImageBean> photos = sendDynamicDataBean.getPhotos();
        Observable<BaseJson<Object>> observable;
        // 有图片需要上传时：先处理图片上传任务，成功后，获取任务id，发布动态
        if (photos != null && !photos.isEmpty()) {
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
            observable = // 组合多个图片上传任务
                    Observable.combineLatest(upLoadPics, args -> {
                        // 得到图片上传的结果
                        List<Integer> integers = new ArrayList<>();
                        for (int i = 0; i < args.length; i++) {
                            BaseJson<Integer> baseJson = (BaseJson<Integer>) args[i];
                            if (baseJson.isStatus()) {
                                sendDynamicDataBean.getImages().get(i).setId(baseJson.getData());
                                integers.add(baseJson.getData());// 将返回的图片上传任务id封装好
                            } else {
                                throw new NullPointerException();// 某一次失败就抛出异常，重传，因为有秒传功能所以不会浪费多少流量
                            }
                        }
                        return integers;
                    }).map(integers -> {
                        sendDynamicDataBean.setPhotos(null);
                        return sendDynamicDataBean;
                    }).flatMap(new Func1<GroupSendDynamicDataBean, Observable<BaseJson<Object>>>() {
                        @Override
                        public Observable<BaseJson<Object>> call(GroupSendDynamicDataBean sendDynamicDataBeanV2) {
                            return mSendDynamicRepository.sendGroupDynamic(sendDynamicDataBeanV2)
                                    .flatMap(new Func1<BaseJsonV2<Object>, Observable<BaseJson<Object>>>() {
                                        @Override
                                        public Observable<BaseJson<Object>> call(BaseJsonV2<Object> objectBaseJsonV2) {
                                            BaseJson<Object> baseJson = new BaseJson<>();
                                            baseJson.setData((double) objectBaseJsonV2.getId());
                                            String msg = objectBaseJsonV2.getMessage().get(0);
                                            baseJson.setStatus(msg.equals("发布成功"));
                                            baseJson.setMessage(msg);
                                            return Observable.just(baseJson);
                                        }
                                    });
                        }
                    });
        } else {
            // 没有图片上传任务，直接发布动态
            observable = mSendDynamicRepository.sendGroupDynamic(sendDynamicDataBean)
                    .flatMap(new Func1<BaseJsonV2<Object>, Observable<BaseJson<Object>>>() {
                        @Override
                        public Observable<BaseJson<Object>> call(BaseJsonV2<Object> objectBaseJsonV2) {
                            BaseJson<Object> baseJson = new BaseJson<>();
                            baseJson.setData((double) objectBaseJsonV2.getId());
                            String msg = objectBaseJsonV2.getMessage().get(0);
                            baseJson.setStatus(msg.equals("发布成功"));
                            baseJson.setMessage(msg);
                            return Observable.just(baseJson);
                        }
                    });
        }
        observable.subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithInterceptDelay(RETRY_MAX_COUNT, RETRY_INTERVAL_TIME))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribe<Object>() {
                    @Override
                    protected void onSuccess(Object data) {
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    private void sendDynamicByEventBus(int dynamicBelong, DynamicDetailBeanV2 dynamicBean, boolean sendSuccess
            , BackgroundRequestTaskBean backgroundRequestTaskBean, Object data) {
        switch (dynamicBelong) {
            case SendDynamicDataBean.MORMAL_DYNAMIC:
                EventBus.getDefault().post(dynamicBean, EVENT_SEND_DYNAMIC_TO_LIST);
                if (sendSuccess) {
                    // 动态发送成功
                    mBackgroundRequestTaskBeanGreenDao.deleteSingleCache(backgroundRequestTaskBean);
                    dynamicBean.setState(DynamicBean.SEND_SUCCESS);
                    dynamicBean.setId(((Double) data).longValue());
                    mSendDynamicDataBeanV2Dao.delteSendDynamicDataBeanV2ByFeedMark(String.valueOf(dynamicBean.getFeed_mark()));
                    mDynamicDetailBeanV2GreenDao.insertOrReplace(dynamicBean);
                } else {
                    dynamicBean.setState(DynamicBean.SEND_ERROR);
                    mDynamicDetailBeanV2GreenDao.insertOrReplace(dynamicBean);
                }
                break;
            case SendDynamicDataBean.CHANNEL_DYNAMIC:
                // 频道发送动态，不会显示在界面上,不用存在数据库中，不用做任何处理
                //EventBus.getDefault().post(dynamicBean, EVENT_SEND_DYNAMIC_TO_CHANNEL);
                break;
            default:
        }
    }

    /**
     * 处理评论发送的后台任务
     */
    private void sendComment(final BackgroundRequestTaskBean backgroundRequestTaskBean) {

        final HashMap<String, Object> params = backgroundRequestTaskBean.getParams();
        final Long commentMark = (Long) params.get("comment_mark");
        final DynamicCommentBean dynamicCommentBean = mDynamicCommentBeanGreenDao.getCommentByCommentMark(commentMark);
        if (dynamicCommentBean == null) {
            mBackgroundRequestTaskBeanGreenDao.deleteSingleCache(backgroundRequestTaskBean);
            return;
        }
        // 发送动态到动态列表：状态为发送中
        mServiceManager.getCommonClient()
                .handleBackGroundTaskPostV2(backgroundRequestTaskBean.getPath(), UpLoadFile.upLoadFileAndParams(null, backgroundRequestTaskBean.getParams()))
                .retryWhen(new RetryWithInterceptDelay(RETRY_MAX_COUNT, RETRY_INTERVAL_TIME))
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<Object>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<Object> data) {
                        mBackgroundRequestTaskBeanGreenDao.deleteSingleCache(backgroundRequestTaskBean);
                        dynamicCommentBean.setComment_id((long) data.getId());
                        dynamicCommentBean.setState(DynamicBean.SEND_SUCCESS);
                        mDynamicCommentBeanGreenDao.insertOrReplace(dynamicCommentBean);
                        EventBus.getDefault().post(dynamicCommentBean, EVENT_SEND_COMMENT_TO_DYNAMIC_LIST);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        dynamicCommentBean.setState(DynamicBean.SEND_ERROR);
                        mDynamicCommentBeanGreenDao.insertOrReplace(dynamicCommentBean);
                        EventBus.getDefault().post(dynamicCommentBean, EVENT_SEND_COMMENT_TO_DYNAMIC_LIST);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        dynamicCommentBean.setState(DynamicBean.SEND_ERROR);
                        mDynamicCommentBeanGreenDao.insertOrReplace(dynamicCommentBean);
                        EventBus.getDefault().post(dynamicCommentBean, EVENT_SEND_COMMENT_TO_DYNAMIC_LIST);
                    }
                });

    }

    /**
     * 处理资讯评论发送的后台任务
     */
    private void sendInfoComment(final BackgroundRequestTaskBean backgroundRequestTaskBean) {

        final HashMap<String, Object> params = backgroundRequestTaskBean.getParams();
        final Long commentMark = (Long) params.get("comment_mark");
        final InfoCommentListBean infoCommentListBean = mInfoCommentListBeanDao.getCommentByCommentMark
                (commentMark);
        if (infoCommentListBean == null) {
            mBackgroundRequestTaskBeanGreenDao.deleteSingleCache(backgroundRequestTaskBean);
            return;
        }
        mServiceManager.getCommonClient()
                .handleBackGroundTaskPost(backgroundRequestTaskBean.getPath(), UpLoadFile.upLoadFileAndParams(null, backgroundRequestTaskBean.getParams()))
                .retryWhen(new RetryWithInterceptDelay(RETRY_MAX_COUNT, RETRY_INTERVAL_TIME))
                .subscribe(new BaseSubscribe<Object>() {
                    @Override
                    protected void onSuccess(Object data) {
                        mBackgroundRequestTaskBeanGreenDao.deleteSingleCache(backgroundRequestTaskBean);
                        infoCommentListBean.setId(((Double) data).intValue());
                        infoCommentListBean.setState(DynamicBean.SEND_SUCCESS);
                        mInfoCommentListBeanDao.insertOrReplace(infoCommentListBean);
                        EventBus.getDefault().post(infoCommentListBean, EVENT_SEND_COMMENT_TO_INFO_LIST);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        infoCommentListBean.setState(DynamicBean.SEND_ERROR);
                        mInfoCommentListBeanDao.insertOrReplace(infoCommentListBean);
                        EventBus.getDefault().post(infoCommentListBean, EVENT_SEND_COMMENT_TO_INFO_LIST);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        infoCommentListBean.setState(DynamicBean.SEND_ERROR);
                        mInfoCommentListBeanDao.insertOrReplace(infoCommentListBean);
                        EventBus.getDefault().post(infoCommentListBean, EVENT_SEND_COMMENT_TO_INFO_LIST);
                    }
                });

    }

    /**
     * 检测是否需要重新请求
     *
     * @param code
     * @return true 需要
     */
    private boolean checkIsNeedReRequest(int code) {
        boolean result;
        switch (code) {
            case ErrorCodeConfig.STOREAGE_UPLOAD_FAIL:
                result = true;
                break;
            case ErrorCodeConfig.IM_CREATE_CHAT_AUTH_FAIL:
                result = true;
                break;
            case ErrorCodeConfig.IM_CREATE_CONVERSATION_FAIL:
                result = true;
                break;
            case ErrorCodeConfig.IM_UPDATE_AUTH_FAIL:
                result = true;
                break;
            case ErrorCodeConfig.IM_DELETE_CONVERSATION_FAIL:
                result = true;
                break;
            case ErrorCodeConfig.IM_HANDLE_CONVERSATION_MEMBER_FAIL:
                result = true;
                break;
            case ErrorCodeConfig.IM_QUIT_CONVERSATION_FAIL:
                result = true;
                break;
            case ErrorCodeConfig.IM_DELDETE_CONVERSATION_FAIL:
                result = true;
                break;
            default:
                result = false;
        }
        return result;
    }

    public interface OnNetResponseCallBack {
        void onSuccess(Object data);

        void onFailure(String message, int code);

        void onException(Throwable throwable);
    }
}