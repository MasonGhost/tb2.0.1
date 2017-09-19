package com.zhiyicx.votesdk.policy;

import com.google.gson.Gson;
import com.zhiyicx.votesdk.entity.BaseModel;
import com.zhiyicx.votesdk.entity.VoteInfo;
import com.zhiyicx.votesdk.utils.NetUtils;
import com.zhiyicx.zhibosdk.manage.listener.ZBCloudApiCallback;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 类说明
 * 网络请求回调
 * Created by lei on 2016/8/26.
 */
public abstract class ZBCallBack implements ZBCloudApiCallback {
    private BaseModel model;
    private Gson gson;

    public abstract void onSuccess(VoteInfo info);

    public abstract void onFailure(String code, String msg);

    public ZBCallBack(BaseModel model) {
        this.model = model;
        gson = new Gson();
    }

    @Override
    public void onResponse(String s) {
        model = (BaseModel) parseData2Model(gson, s, model);
        if (model == null)
            throw new IllegalArgumentException("json can't be parsed model");
        if (NetUtils.CODE_SUCCESS.equals(model.getCode())) {
            onSuccess((VoteInfo) model);
        } else {
            onFailure(model.getCode(), model.getMessage());
        }
    }


    @Override
    public void onError(Throwable throwable) {
        onFailure("-1", "onError");
    }


    private Object parseData2Model(Gson gson, String jsonStr, BaseModel model) {
        BaseModel result = null;
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            if (NetUtils.CODE_SUCCESS.equals(jsonObject.get("code"))) {
                if (jsonObject.has("data") && !jsonObject.isNull("data")) {
                    result = gson.fromJson(String.valueOf(jsonObject.getJSONObject("data")), model.getClass());

                }
            }
            if (result == null) result = new BaseModel();
            if (jsonObject.has("code")) {
                result.setCode(jsonObject.getString("code"));
            }
            if (jsonObject.has("message")) {
                result.setMessage(jsonObject.getString("message"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}
