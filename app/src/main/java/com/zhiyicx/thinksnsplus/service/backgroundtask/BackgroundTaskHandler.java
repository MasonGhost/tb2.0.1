package com.zhiyicx.thinksnsplus.service.backgroundtask;

import android.app.Application;

import com.google.gson.Gson;
import com.zhiyicx.baseproject.cache.CacheBean;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.net.UpLoadFile;
import com.zhiyicx.common.utils.NetUtils;
import com.zhiyicx.imsdk.entity.IMConfig;
import com.zhiyicx.imsdk.receiver.NetChangeReceiver;
import com.zhiyicx.rxerrorhandler.functions.RetryWithInterceptDelay;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBean;
import com.zhiyicx.thinksnsplus.data.beans.IMBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.BackgroundRequestTaskBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicCommentBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.InfoCommentListBeanDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.SendDynamicRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UpLoadRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.jpush.JpushAlias;

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
import rx.functions.FuncN;
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
    private static final int RETRY_INTERVAL_TIME = 2; // 循环间隔时间 单位 s
    private static final long MESSAGE_SEND_INTERVAL_FOR_CPU = 500;// 消息发送间隔时间，防止 cpu 占用过高
    private static final long MESSAGE_SEND_INTERVAL_FOR_CPU_TIME_OUT = 2000;// 消息发送间隔时间，防止 cpu 占用过高

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

    private Queue<BackgroundRequestTaskBean> mTaskBeanConcurrentLinkedQueue = new ConcurrentLinkedQueue<>();// 线程安全的队列

    private List<BackgroundRequestTaskBean> mBackgroundRequestTaskBeanCaches = new ArrayList<>();

    private boolean mIsExit = false; // 是否关闭

    private boolean mIsNetConnected = false;

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
        if (mTaskBeanConcurrentLinkedQueue.add(backgroundRequestTaskBean)) {
            mBackgroundRequestTaskBeanCaches.add(backgroundRequestTaskBean);
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
        new Thread(handleTaskRunnable).start();
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
        List<BackgroundRequestTaskBean> cacheDatas = mBackgroundRequestTaskBeanGreenDao.getMultiDataFromCache();
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
            while (!mIsExit) {
                if (mIsNetConnected && !mTaskBeanConcurrentLinkedQueue.isEmpty()) {
                    BackgroundRequestTaskBean backgroundRequestTaskBean = mTaskBeanConcurrentLinkedQueue.poll();
                    handleTask(backgroundRequestTaskBean);
                }
                threadSleep();
            }
            // 存储未执行的数据到数据库，下次执行
            if (mBackgroundRequestTaskBeanCaches.size() > 0) {
                mBackgroundRequestTaskBeanGreenDao.saveMultiData(mBackgroundRequestTaskBeanCaches);
            }
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
             * 发送动态
             */
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
    private void postMethod(final BackgroundRequestTaskBean backgroundRequestTaskBean) {

        mServiceManager.getCommonClient().handleBackGroundTaskPost(backgroundRequestTaskBean.getPath(), UpLoadFile.upLoadFileAndParams(null, backgroundRequestTaskBean.getParams()))
                .subscribe(new BaseSubscribe<Object>() {
                    @Override
                    protected void onSuccess(Object data) {
                        mBackgroundRequestTaskBeanCaches.remove(backgroundRequestTaskBean);
                    }

                    @Override
                    protected void onFailure(String message) {
                        addBackgroundRequestTask(backgroundRequestTaskBean);
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
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(datas));
        mServiceManager.getCommonClient().handleBackGroudTaskDelete(backgroundRequestTaskBean.getPath()
                , body)
                .subscribe(new BaseSubscribe<CacheBean>() {
                    @Override
                    protected void onSuccess(CacheBean data) {
                        mBackgroundRequestTaskBeanCaches.remove(backgroundRequestTaskBean);
                    }

                    @Override
                    protected void onFailure(String message) {
                        addBackgroundRequestTask(backgroundRequestTaskBean);
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
                        mBackgroundRequestTaskBeanCaches.remove(backgroundRequestTaskBean);
                        IMConfig imConfig = new IMConfig();
                        imConfig.setImUid(data.getUser_id());
                        imConfig.setToken(data.getIm_password());
                        imConfig.setWeb_socket_authority("ws://"+mAuthRepository.getComponentConfigLocal().get(0).getValue());
                        mAuthRepository.saveIMConfig(imConfig);
                        mAuthRepository.loginIM();
                    }

                    @Override
                    protected void onFailure(String message) {
                        addBackgroundRequestTask(backgroundRequestTaskBean);
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
        List<Long> integers = new ArrayList<>();
        if (backgroundRequestTaskBean.getParams().get("user_id") instanceof List) {
            integers.addAll((Collection<? extends Long>) backgroundRequestTaskBean.getParams().get("user_id"));
        } else {
            integers.add(Long.valueOf(backgroundRequestTaskBean.getParams().get("user_id") + ""));
        }

        mUserInfoRepository.getUserInfo(integers)
                .subscribe(new BaseSubscribe<List<UserInfoBean>>() {
                    @Override
                    protected void onSuccess(List<UserInfoBean> data) {
                        mBackgroundRequestTaskBeanCaches.remove(backgroundRequestTaskBean);
                        mUserInfoBeanGreenDao.insertOrReplace(data);
                        // 用户信息获取成功后就可以通知界面刷新了
                        EventBus.getDefault().post(data, EventBusTagConfig.EVENT_USERINFO_UPDATE);
                        new JpushAlias(mContext, data.get(0).getUser_id() + "");// 设置极光推送别名
                    }

                    @Override
                    protected void onFailure(String message) {
                        addBackgroundRequestTask(backgroundRequestTaskBean);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        throwable.printStackTrace();
                        addBackgroundRequestTask(backgroundRequestTaskBean);
                    }
                });
    }

    /**
     * 处理动态发送的后台任务
     */
    private void sendDynamic(final BackgroundRequestTaskBean backgroundRequestTaskBean) {
        final HashMap<String, Object> params = backgroundRequestTaskBean.getParams();
        final Long feedMark = (Long) params.get("params");
        final DynamicBean dynamicBean = mDynamicBeanGreenDao.getDynamicByFeedMark(feedMark);
        // 发送动态到动态列表：状态为发送中
        dynamicBean.setState(DynamicBean.SEND_ING);

        // 存入数据库
        // ....
        final DynamicDetailBean dynamicDetailBean = dynamicBean.getFeed();
        List<String> photos = dynamicDetailBean.getLocalPhotos();
        Observable<BaseJson<Object>> observable = null;
        // 有图片需要上传时：先处理图片上传任务，成功后，获取任务id，发布动态
        if (photos != null && !photos.isEmpty()) {
            // 先处理图片上传，图片上传成功后，在进行动态发布
            List<Observable<BaseJson<Integer>>> upLoadPics = new ArrayList<>();
            for (int i = 0; i < photos.size(); i++) {
                String filePath = photos.get(i);
                upLoadPics.add(mUpLoadRepository.upLoadSingleFile("file" + i, filePath, true));
            }
            observable = // 组合多个图片上传任务
                    Observable.combineLatest(upLoadPics, new FuncN<List<Integer>>() {
                        @Override
                        public List<Integer> call(Object... args) {
                            // 得到图片上传的结果
                            List<Integer> integers = new ArrayList<Integer>();
                            for (Object obj : args) {
                                BaseJson<Integer> baseJson = (BaseJson<Integer>) obj;
                                if (baseJson.isStatus()) {
                                    integers.add(baseJson.getData());// 将返回的图片上传任务id封装好
                                } else {
                                    throw new NullPointerException();// 某一次失败就抛出异常，重传，因为有秒传功能所以不会浪费多少流量
                                }
                            }
                            return integers;
                        }
                    }).flatMap(new Func1<List<Integer>, Observable<BaseJson<Object>>>() {
                        @Override
                        public Observable<BaseJson<Object>> call(List<Integer> integers) {
                            List<ImageBean> imageBeens = new ArrayList<ImageBean>();
                            // 动态相关图片：图片任务id的数组，将它作为发布动态的参数
                            for (int i = 0; i < integers.size(); i++) {
                                imageBeens.add(new ImageBean(integers.get(i)));
                            }
                            dynamicDetailBean.setStorage_task_ids(integers);
                            return mSendDynamicRepository.sendDynamic(dynamicDetailBean);// 进行动态发布的请求
                        }
                    });
        } else {
            // 没有图片上传任务，直接发布动态
            observable = mSendDynamicRepository.sendDynamic(dynamicDetailBean);// 进行动态发布的请求

        }
        observable.subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithInterceptDelay(RETRY_MAX_COUNT, RETRY_INTERVAL_TIME))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribe<Object>() {
                    @Override
                    protected void onSuccess(Object data) {
                        // 动态发送成功
                        mBackgroundRequestTaskBeanCaches.remove(backgroundRequestTaskBean);
                        // 发送动态到动态列表：状态为发送成功
                        dynamicBean.setState(DynamicBean.SEND_SUCCESS);
                        dynamicBean.setFeed_id(((Double) data).longValue());
                        mDynamicBeanGreenDao.insertOrReplace(dynamicBean);
                        EventBus.getDefault().post(dynamicBean, EVENT_SEND_DYNAMIC_TO_LIST);
                    }

                    @Override
                    protected void onFailure(String message) {
                        // 发送动态到动态列表：状态为发送失败
                        dynamicBean.setState(DynamicBean.SEND_ERROR);
                        mDynamicBeanGreenDao.insertOrReplace(dynamicBean);
                        EventBus.getDefault().post(dynamicBean, EVENT_SEND_DYNAMIC_TO_LIST);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        throwable.printStackTrace();
                        // 发送动态到动态列表：状态为发送失败
                        dynamicBean.setState(DynamicBean.SEND_ERROR);
                        mDynamicBeanGreenDao.insertOrReplace(dynamicBean);
                        EventBus.getDefault().post(dynamicBean, EVENT_SEND_DYNAMIC_TO_LIST);
                    }
                });

    }

    /**
     * 处理评论发送的后台任务
     */
    private void sendComment(final BackgroundRequestTaskBean backgroundRequestTaskBean) {

        final HashMap<String, Object> params = backgroundRequestTaskBean.getParams();
        final Long commentMark = (Long) params.get("comment_mark");
        final DynamicCommentBean dynamicCommentBean = mDynamicCommentBeanGreenDao.getCommentByCommentMark(commentMark);
        // 发送动态到动态列表：状态为发送中
        mServiceManager.getCommonClient()
                .handleBackGroundTaskPost(backgroundRequestTaskBean.getPath(), UpLoadFile.upLoadFileAndParams(null, backgroundRequestTaskBean.getParams()))
                .retryWhen(new RetryWithInterceptDelay(RETRY_MAX_COUNT, RETRY_INTERVAL_TIME))
                .subscribe(new BaseSubscribe<Object>() {
                    @Override
                    protected void onSuccess(Object data) {
                        mBackgroundRequestTaskBeanCaches.remove(backgroundRequestTaskBean);
                        dynamicCommentBean.setComment_id(((Double) data).longValue());
                        dynamicCommentBean.setState(DynamicBean.SEND_SUCCESS);
                        mDynamicCommentBeanGreenDao.insertOrReplace(dynamicCommentBean);
                        EventBus.getDefault().post(dynamicCommentBean, EVENT_SEND_COMMENT_TO_DYNAMIC_LIST);
                    }

                    @Override
                    protected void onFailure(String message) {
                        dynamicCommentBean.setState(DynamicBean.SEND_ERROR);
                        mDynamicCommentBeanGreenDao.insertOrReplace(dynamicCommentBean);
                        EventBus.getDefault().post(dynamicCommentBean, EVENT_SEND_COMMENT_TO_DYNAMIC_LIST);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
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

        mServiceManager.getCommonClient()
                .handleBackGroundTaskPost(backgroundRequestTaskBean.getPath(), UpLoadFile.upLoadFileAndParams(null, backgroundRequestTaskBean.getParams()))
                .retryWhen(new RetryWithInterceptDelay(RETRY_MAX_COUNT, RETRY_INTERVAL_TIME))
                .subscribe(new BaseSubscribe<Object>() {
                    @Override
                    protected void onSuccess(Object data) {
                        mBackgroundRequestTaskBeanCaches.remove(backgroundRequestTaskBean);
                        infoCommentListBean.setId(((Double) data).intValue());
                        infoCommentListBean.setState(DynamicBean.SEND_SUCCESS);
                        mInfoCommentListBeanDao.insertOrReplace(infoCommentListBean);
                        EventBus.getDefault().post(infoCommentListBean, EVENT_SEND_COMMENT_TO_INFO_LIST);
                    }

                    @Override
                    protected void onFailure(String message) {
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
}