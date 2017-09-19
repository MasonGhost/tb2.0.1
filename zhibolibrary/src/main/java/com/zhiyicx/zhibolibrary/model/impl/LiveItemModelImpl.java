package com.zhiyicx.zhibolibrary.model.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.zhiyicx.zhibolibrary.app.ZhiboApplication;
import com.zhiyicx.zhibolibrary.model.LiveItemModel;
import com.zhiyicx.zhibolibrary.model.api.ZBLApi;
import com.zhiyicx.zhibolibrary.model.api.service.ServiceManager;
import com.zhiyicx.zhibolibrary.model.api.service.UserService;
import com.zhiyicx.zhibolibrary.model.entity.ApiList;
import com.zhiyicx.zhibolibrary.model.entity.BaseJson;
import com.zhiyicx.zhibolibrary.model.entity.SearchResult;
import com.zhiyicx.zhibolibrary.model.entity.StreamStatus;
import com.zhiyicx.zhibolibrary.model.entity.UserInfo;
import com.zhiyicx.zhibolibrary.ui.fragment.LiveItemFragment;
import com.zhiyicx.zhibosdk.manage.ZBCloudApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by zhiyicx on 2016/3/30.
 */
public class LiveItemModelImpl implements LiveItemModel {
    private UserService mUserService;
    private ApiList tmp;

    public LiveItemModelImpl(ServiceManager manager, OkHttpClient client) {
        this.mUserService = manager.getUserService();
    }

    @Override
    public Observable<ApiList> getNotList(String order, String videoOrder, int page) {
        Map<String, Object> condition = new HashMap<>();
        return getList(order, videoOrder, page, condition);
    }


    @Override
    public Observable<ApiList> getFlterList(String order, String videoOrder, int page, Map<String, Object> condition) {

        return getList(order, videoOrder, page, condition);

    }

    /**
     * 获取直播或者回放列表
     *
     * @param order
     * @param page
     * @return
     */
    private Observable<ApiList> getList(
            final String order,
            final String videoOrder,
            final int page, Map<String, Object> map
    ) {
        map.put("p", page);
        if (order.equals(LiveItemFragment.TYPE_VIDEO)) {//回放地址和order
            map.put("order", videoOrder);
            return ZBCloudApiClient.getInstance().sendCloudApiRequestForRx(ZBLApi.ZB_API_GET_VIDEO_LIST, map).subscribeOn(Schedulers.io())
                    .map(new Func1<JsonObject, ApiList>() {
                        @Override
                        public ApiList call(JsonObject jsonObject) {
                            return new Gson().fromJson(jsonObject, ApiList.class);
                        }
                    });
        }
        else {//直播orrder和地址
            map.put("order", order);
            //获取关注的直播列表,先获取关注信息，再获取播放状态信息
            if (order.equals(LiveItemFragment.TYPE_FOLLOW)) {
                return getFollowApiListObservable(page);


            }
            //获热门和最新的直播列表
            else {
                return ZBCloudApiClient.getInstance().sendCloudApiRequestForRx(ZBLApi.ZB_API_GET_LIVE_LIST, map).subscribeOn(Schedulers.io())
                        .map(new Func1<JsonObject, ApiList>() {
                            @Override
                            public ApiList call(JsonObject jsonObject) {
                                return new Gson().fromJson(jsonObject, ApiList.class);
                            }
                        });
            }
        }


    }

    /**
     * 获取关注视频列表
     *
     * @param page
     * @return
     */
    private Observable<ApiList> getFollowApiListObservable(int page) {
        return getUserFollowList(ZhiboApplication.userInfo.auth_accesskey
                , ZhiboApplication.userInfo.auth_secretkey, ZhiboApplication.userInfo.usid, LiveItemFragment.TYPE_FOLLOW, page).subscribeOn(Schedulers.io())
                .flatMap(new Func1<BaseJson<SearchResult[]>, Observable<ApiList>>() {
                    @Override
                    public Observable<ApiList> call(BaseJson<SearchResult[]> json) {
                        tmp = new ApiList();
                        tmp.code = json.code;
                        tmp.message = json.message;
                        if (json.code.equals(ZBLApi.REQUEST_SUCESS) && json.data.length > 0) {
                            tmp.data = json.data;
                            String usids = "";
                            for (SearchResult value : tmp.data) {
                                if (value.user != null)
                                    usids += value.user.usid + ",";
                                else
                                    usids += "zb_user_0" + ",";
                            }
                            if (usids.length() > 0)
                                usids = usids.substring(0, usids.length() - 1);

                            Map<String, Object> params = new HashMap<String, Object>();
                            params.put("usid", usids);
                            return ZBCloudApiClient.getInstance().sendCloudApiRequestForRx(ZBLApi.API_GET_PRESENTER_STATUS, params).map(new Func1<JsonObject, ApiList>() {
                                @Override
                                public ApiList call(JsonObject jsonObject) {
                                    try {
                                        JSONObject js = new JSONObject(jsonObject.toString());

                                        if (js.getString("code").equals(ZBLApi.REQUEST_SUCESS)) {
                                            List<StreamStatus> streamStatuslist = new Gson().fromJson(js.getJSONArray("data").toString(), new TypeToken<List<StreamStatus>>() {
                                            }.getType());
                                            if (streamStatuslist != null) {
                                                for (int i = 0; i < streamStatuslist.size(); i++) {
                                                    if (streamStatuslist.get(i).info != null) {
                                                        tmp.data[i].im = streamStatuslist.get(i).info.im;
                                                        tmp.data[i].stream = streamStatuslist.get(i).info.stream;
                                                    }
                                                }
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                    return tmp;
                                }
                            });
                        }
                        else
                            return Observable.just(tmp);

                    }
                });
    }

    /**
     * 获取指定用户的回放信息
     *
     * @param page
     * @return
     */
    @Override
    public Observable<ApiList> getUserList(
            final int page,
            final String usid) {
        Map<String, Object> map = new HashMap<>();
        map.put("p", page);
        map.put("usid", usid);
        return ZBCloudApiClient.getInstance().sendCloudApiRequestForRx(ZBLApi.ZB_API_GET_VIDEO_LIST, map).subscribeOn(Schedulers.io())
                .map(new Func1<JsonObject, ApiList>() {
                    @Override
                    public ApiList call(JsonObject jsonObject) {
                        return new Gson().fromJson(jsonObject, ApiList.class);
                    }
                });


    }

    @Override
    public Observable<BaseJson<UserInfo[]>> getUsidInfo(final String userId, String filed) {

        return mUserService.getUsIdInfo(ZBLApi.API_GET_USID_INFO, userId, "", ZhiboApplication.userInfo.auth_accesskey, ZhiboApplication.userInfo.auth_secretkey).subscribeOn(Schedulers.io());

    }

    @Override
    public Observable<BaseJson<SearchResult[]>> getUserFollowList(String accessKey, String
            secretKey, String userId, String type, int page) {
        FormBody.Builder builder = getFormBody(accessKey, secretKey);
        builder.add("type", type);
        builder.add("user_id", userId);
        builder.add(ZBLApi.VAR_PAGE, page + "");
        FormBody formBody = builder.build();
        return mUserService.getFollowList(formBody);
    }

    /**
     * 获取formbody,其他地方可以继续增加param
     *
     * @param accessKey
     * @param secretKey
     * @return
     */

    private FormBody.Builder getFormBody(String accessKey, String secretKey) {
        FormBody.Builder builder = new FormBody.Builder();//form表单提交
        builder.add("api", ZBLApi.API_GET_USER_LIST);
        builder.add("auth_accesskey", accessKey);
        builder.add("auth_secretkey", secretKey);
        return builder;
    }

}
