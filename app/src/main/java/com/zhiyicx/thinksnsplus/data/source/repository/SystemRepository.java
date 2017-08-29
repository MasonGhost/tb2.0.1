package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;
import android.content.Context;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.zhiyicx.baseproject.config.SystemConfig;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.SharePreferenceUtils;
import com.zhiyicx.imsdk.core.ChatType;
import com.zhiyicx.imsdk.db.dao.MessageDao;
import com.zhiyicx.imsdk.entity.Conversation;
import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.imsdk.entity.MessageStatus;
import com.zhiyicx.imsdk.entity.MessageType;
import com.zhiyicx.rxerrorhandler.functions.RetryWithInterceptDelay;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribe;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.SharePreferenceTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.ImageAdvert;
import com.zhiyicx.thinksnsplus.data.beans.LocationBean;
import com.zhiyicx.thinksnsplus.data.beans.LocationContainerBean;
import com.zhiyicx.thinksnsplus.data.beans.PayStrBean;
import com.zhiyicx.thinksnsplus.data.beans.SystemConfigBean;
import com.zhiyicx.thinksnsplus.data.beans.SystemConversationBean;
import com.zhiyicx.thinksnsplus.data.beans.TagCategoryBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.SystemConversationBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.TagCategoryBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserTagBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.remote.CommonClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.i.ISystemRepository;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.zhiyicx.rxerrorhandler.functions.RetryWithInterceptDelay.RETRY_INTERVAL_TIME;
import static com.zhiyicx.rxerrorhandler.functions.RetryWithInterceptDelay.RETRY_MAX_COUNT;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/4/25
 * @Contact master.jungle68@gmail.com
 */

public class SystemRepository implements ISystemRepository {

    @Inject
    protected UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;
    @Inject
    protected SystemConversationBeanGreenDaoImpl mSystemConversationBeanGreenDao;

    @Inject
    protected TagCategoryBeanGreenDaoImpl mTagCategoryBeanGreenDao;

    @Inject
    protected UserTagBeanGreenDaoImpl mUserTagBeanGreenDao;

    @Inject
    protected ChatRepository mChatRepository;

    private CommonClient mCommonClient;
    private Context mContext;

    @Inject
    public SystemRepository(ServiceManager serviceManager, Application context) {
        mCommonClient = serviceManager.getCommonClient();
        mContext = context;
    }

    /**
     * 去获取服务器启动信息
     */
    @Override
    public void getBootstrappersInfoFromServer() {
        mCommonClient.getBootstrappersInfo()
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithInterceptDelay(RETRY_MAX_COUNT, RETRY_INTERVAL_TIME))
                .subscribe(new BaseSubscribeForV2<SystemConfigBean>() {
                    @Override
                    protected void onSuccess(SystemConfigBean data) {
                        // 广告类型
                        saveComponentStatus(data, mContext);
                    }
                });
    }


    /**
     * 去获取本地启动信息
     *
     * @return
     */
    @Override
    public SystemConfigBean getBootstrappersInfoFromLocal() {
        SystemConfigBean systemConfigBean = SharePreferenceUtils.getObject(mContext, SharePreferenceTagConfig.SHAREPREFERENCE_TAG_SYSTEM_BOOTSTRAPPERS);
        if (systemConfigBean == null) { // 读取本地默认配置
            systemConfigBean = new Gson().fromJson(SystemConfig.DEFAULT_SYSTEM_CONFIG, SystemConfigBean.class);
        }
        if (systemConfigBean.getAdverts() == null)
            return systemConfigBean;
        for (SystemConfigBean.Advert advert : systemConfigBean.getAdverts()) {
            if (advert.getData() instanceof LinkedHashMap) {
                LinkedHashMap advertMap = (LinkedHashMap) advert.getData();
                ImageAdvert imageAdvert = new ImageAdvert();
                for (Iterator it = advertMap.keySet().iterator(); it.hasNext(); ) {
                    String key = (String) it.next();
                    String value = (String) advertMap.get(key);
                    if (key.equals("image")) {
                        Glide.with(mContext).load(value).downloadOnly(DeviceUtils.getScreenWidth(mContext),
                                DeviceUtils.getScreenHeight(mContext));
                        imageAdvert.setImage(value);
                    }
                    if (key.equals("link")) {
                        imageAdvert.setLink(value);
                    }
                }
                advert.setImageAdvert(imageAdvert);
            }
        }
        return systemConfigBean;
    }

    @Override
    public String checkTShelper(long user_id) {
        if (getBootstrappersInfoFromLocal() == null || getBootstrappersInfoFromLocal().getIm_helper() == null) {
            return null;
        }
        List<SystemConfigBean.ImHelperBean> tshleprs = getBootstrappersInfoFromLocal().getIm_helper();
        for (SystemConfigBean.ImHelperBean tshlepr : tshleprs) {
            if (Integer.parseInt(tshlepr.getUid()) == user_id) {
                return tshlepr.getUrl();
            }
        }
        return null;
    }

    /**
     * 静态检测用户是否是 ts 助手
     *
     * @param context context
     * @param user_id user  id
     * @return
     */
    public static String checkHelperUrl(Context context, long user_id) {
        String tsHelperUrl = null;
        SystemConfigBean systemConfigBean = SharePreferenceUtils.getObject(context, SharePreferenceTagConfig.SHAREPREFERENCE_TAG_SYSTEM_BOOTSTRAPPERS);
        if (systemConfigBean == null) { // 读取本地默认配置
            systemConfigBean = new Gson().fromJson(SystemConfig.DEFAULT_SYSTEM_CONFIG, SystemConfigBean.class);
        }
        if (systemConfigBean != null && systemConfigBean.getIm_helper() != null) {
            List<SystemConfigBean.ImHelperBean> tshleprs = systemConfigBean.getIm_helper();
            for (SystemConfigBean.ImHelperBean tshlepr : tshleprs) {
                if (Integer.parseInt(tshlepr.getUid()) == user_id) {
                    tsHelperUrl = tshlepr.getUrl();
                    break;
                }
            }
        }
        return tsHelperUrl;
    }

    /**
     * 重置 ts 助手
     *
     * @param context context
     * @return
     */
    public static void resetTSHelper(Context context) {
        String tsHelperUrl = null;
        SystemConfigBean systemConfigBean = SharePreferenceUtils.getObject(context, SharePreferenceTagConfig.SHAREPREFERENCE_TAG_SYSTEM_BOOTSTRAPPERS);
        if (systemConfigBean == null) { // 读取本地默认配置
            systemConfigBean = new Gson().fromJson(SystemConfig.DEFAULT_SYSTEM_CONFIG, SystemConfigBean.class);
        } else {
            if (systemConfigBean != null && systemConfigBean.getIm_helper() != null) {
                List<SystemConfigBean.ImHelperBean> tshleprs = systemConfigBean.getIm_helper();
                for (SystemConfigBean.ImHelperBean tshlepr : tshleprs) {
                    tshlepr.setDelete(false);
                }
            }
        }
        SharePreferenceUtils.saveObject(context, SharePreferenceTagConfig.SHAREPREFERENCE_TAG_SYSTEM_BOOTSTRAPPERS, systemConfigBean);
    }

    /**
     * check {user_id} is tsHelper
     *
     * @param context context
     * @param user_id checked user_id
     * @return
     */
    public static boolean checkHelperId(Context context, long user_id) {
        boolean isTsHelper = false;
        SystemConfigBean systemConfigBean = SharePreferenceUtils.getObject(context, SharePreferenceTagConfig.SHAREPREFERENCE_TAG_SYSTEM_BOOTSTRAPPERS);
        if (systemConfigBean == null) { // 读取本地默认配置
            systemConfigBean = new Gson().fromJson(SystemConfig.DEFAULT_SYSTEM_CONFIG, SystemConfigBean.class);
        }
        if (systemConfigBean != null && systemConfigBean.getIm_helper() != null) {
            List<SystemConfigBean.ImHelperBean> tshleprs = systemConfigBean.getIm_helper();
            for (SystemConfigBean.ImHelperBean tshlepr : tshleprs) {
                if (Integer.parseInt(tshlepr.getUid()) == user_id) {
                    isTsHelper = true;
                    break;
                }
            }
        }
        return isTsHelper;
    }

    /**
     * update tsHelper  delete status
     *
     * @param context context
     * @param user_id user_id
     */
    public static void updateTsHelperDeletStatus(Context context, long user_id, boolean isDelete) {
        SystemConfigBean systemConfigBean = SharePreferenceUtils.getObject(context, SharePreferenceTagConfig.SHAREPREFERENCE_TAG_SYSTEM_BOOTSTRAPPERS);
        if (systemConfigBean == null) { // 读取本地默认配置
            systemConfigBean = new Gson().fromJson(SystemConfig.DEFAULT_SYSTEM_CONFIG, SystemConfigBean.class);
        }
        if (systemConfigBean != null && systemConfigBean.getIm_helper() != null) {
            List<SystemConfigBean.ImHelperBean> tshleprs = systemConfigBean.getIm_helper();
            for (SystemConfigBean.ImHelperBean tshlepr : tshleprs) {
                if (Integer.parseInt(tshlepr.getUid()) == user_id) {
                    tshlepr.setDelete(isDelete);
                    break;
                }
            }
        }
        SharePreferenceUtils.saveObject(context, SharePreferenceTagConfig.SHAREPREFERENCE_TAG_SYSTEM_BOOTSTRAPPERS, systemConfigBean);
    }

    /**
     * 保存启动信息
     *
     * @param systemConfigBean
     * @return
     */
    private boolean saveComponentStatus(SystemConfigBean systemConfigBean, Context context) {
        if (systemConfigBean == null || systemConfigBean.getIm_helper() == null) {
            return false;
        }
        SystemConfigBean localSystemConfigBean = SharePreferenceUtils.getObject(context, SharePreferenceTagConfig.SHAREPREFERENCE_TAG_SYSTEM_BOOTSTRAPPERS);
        if (localSystemConfigBean == null) { // 读取本地默认配置
            localSystemConfigBean = new Gson().fromJson(SystemConfig.DEFAULT_SYSTEM_CONFIG, SystemConfigBean.class);
        }
        if (localSystemConfigBean == null || localSystemConfigBean.getIm_helper() == null || localSystemConfigBean.getIm_helper().isEmpty()) {
            return SharePreferenceUtils.saveObject(mContext, SharePreferenceTagConfig.SHAREPREFERENCE_TAG_SYSTEM_BOOTSTRAPPERS, systemConfigBean);
        }
        for (SystemConfigBean.ImHelperBean imHelperBean : localSystemConfigBean.getIm_helper()) {

            for (SystemConfigBean.ImHelperBean tshlepr : systemConfigBean.getIm_helper()) {
                if (imHelperBean.getUid().equals(tshlepr.getUid())) {
                    tshlepr.setDelete(imHelperBean.isDelete());
                }
            }
        }

        return SharePreferenceUtils.saveObject(mContext, SharePreferenceTagConfig.SHAREPREFERENCE_TAG_SYSTEM_BOOTSTRAPPERS, systemConfigBean);
    }

    @Override
    public Observable<PayStrBean> getPayStr(String channel, double amount) {
        return mCommonClient.getPayStr(channel, amount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 系统反馈
     *
     * @param content 反馈内容
     * @return
     */
    @Override
    public Observable<Object> systemFeedback(String content, long system_mark) {
        return mCommonClient.systemFeedback(content, system_mark)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取系统对话信息
     *
     * @param max_id 分页标识
     * @param limit  每页数量
     * @return
     */
    @Override
    public Observable<BaseJson<List<SystemConversationBean>>> getSystemConversations(long max_id, int limit) {
        return mCommonClient.getSystemConversations(max_id, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(listBaseJson -> {
                    if (listBaseJson.isStatus()) {
//                            descNetSystemConversation(listBaseJson.getData());
                        mSystemConversationBeanGreenDao.saveMultiData(listBaseJson.getData());
                        handleTsHelperUserInfo(listBaseJson.getData());
                    }
                    return listBaseJson;
                });
    }

    /**
     * 系统对话信息更具 id 排序
     *
     * @param datas 对话信息
     */
    private void descNetSystemConversation(List<SystemConversationBean> datas) {
        Collections.sort(datas, (o1, o2) -> o1.getId().intValue() - o2.getId().intValue());
    }

    /**
     * 获取本地系统公告
     *
     * @param max_Id 唯一标识
     * @return
     */
    @Override
    public List<SystemConversationBean> requestCacheData(long max_Id) {
        List<SystemConversationBean> list = mSystemConversationBeanGreenDao.getMultiDataFromCacheByMaxId(max_Id);
        handleTsHelperUserInfo(list);
        return list;
    }

    /**
     * 创建 ts 助手对话
     */
    @Override
    public void creatTsHelperConversation() {

        // 新版 ts 助手
        for (final SystemConfigBean.ImHelperBean imHelperBean : getBootstrappersInfoFromLocal().getIm_helper()) {
            final String uids = AppApplication.getMyUserIdWithdefault() + "," + imHelperBean.getUid();
            final String pair = AppApplication.getmCurrentLoginAuth().getUser_id() + "&" + imHelperBean.getUid();// "pair":null,   // type=0时此项为两个uid：min_uid&max_uid
            mChatRepository.createConveration(ChatType.CHAT_TYPE_PRIVATE, "", "", uids)
                    .subscribe(new BaseSubscribe<Conversation>() {
                        @Override
                        protected void onSuccess(Conversation data) {
                            data.setIm_uid((int) AppApplication.getmCurrentLoginAuth().getUser_id());
                            data.setUsids(uids);
                            data.setPair(pair);
                            mChatRepository.insertOrUpdateConversation(data);
                            // 写入 ts helper 默认提示语句
                            Message message = new Message();
                            message.setId((int) System.currentTimeMillis());
                            message.setType(MessageType.MESSAGE_TYPE_TEXT);
                            message.setTxt(mContext.getString(R.string.ts_helper_default_tip));
                            message.setSend_status(MessageStatus.SEND_SUCCESS);
                            message.setIs_read(false);
                            message.setUid(Integer.parseInt(imHelperBean.getUid()));
                            message.setCid(data.getCid());
                            MessageDao.getInstance(mContext).insertOrUpdateMessage(message);
                        }
                    });
        }
    }

    /**
     * 获取全部标签
     *
     * @return
     */
    @Override
    public Observable<List<TagCategoryBean>> getAllTags() {
        return mCommonClient.getAllTags()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    /**
     * @param name search content
     * @return
     */
    @Override
    public Observable<List<LocationContainerBean>> searchLocation(String name) {
        return mCommonClient.searchLocation(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 热门城市
     * @return
     */
    @Override
    public Observable<List<String>> getHoCity() {
        return mCommonClient.getHoCity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 处理 TS 助手和用户信息
     *
     * @param list 对话信息
     */
    private void handleTsHelperUserInfo(List<SystemConversationBean> list) {
        UserInfoBean myUserInfo = mUserInfoBeanGreenDao.getSingleDataFromCache(AppApplication.getmCurrentLoginAuth().getUser_id());
        UserInfoBean tsHleper = new UserInfoBean();
        tsHleper.setName(mContext.getString(R.string.ts_helper));
        for (SystemConversationBean systemConversationBean : list) {
            systemConversationBean.setUserInfo(systemConversationBean.getUser_id() == null || systemConversationBean.getUser_id() == 0 ? tsHleper : myUserInfo);
            systemConversationBean.setToUserInfo(systemConversationBean.getTo_user_id() == null || systemConversationBean.getTo_user_id() == 0 ? tsHleper : myUserInfo);
        }
    }


}
