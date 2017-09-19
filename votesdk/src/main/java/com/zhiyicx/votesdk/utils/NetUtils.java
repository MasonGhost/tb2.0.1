package com.zhiyicx.votesdk.utils;

import android.text.TextUtils;

import com.zhiyicx.zhibosdk.manage.ZBCloudApiClient;
import com.zhiyicx.zhibosdk.manage.listener.ZBCloudApiCallback;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lei on 2016/8/15.
 */
public class NetUtils {
    public static final String CODE_SUCCESS = "00000";
    public static final String API_VOTE_CREATE = "ZBCloud_Apps_Vote_Create";//创建投票
    public static final String API_LAST_VOTE_INFO = "ZBCloud_Apps_Vote_GetUserLastVote";//获取最近一次投票信息
    public static final String API_NEWST_VOTE_INFO = "ZBCloud_Apps_Vote_GetInfo";//获取最近一次投票信息
    public static final String API_VOTE_SEND = "ZBCloud_Apps_Vote_Poll";//投票
    public static final String API_VOTE_PAUSE = "ZBCloud_Apps_Vote_EndPollTemp";//暂时停止投票
    public static final String API_VOTE_RESTART = "ZBCloud_Apps_Vote_StartPollTemp";//恢复投票
    public static final String API_VOTE_STOP = "ZBCloud_Apps_Vote_EndPoll";//结束投票
    public static final String API_VOTE_RESETTIME = "ZBCloud_Apps_Vote_SetTime";//重新设置投票时间

    /**
     * 发起投票
     *
     * @param options
     * @param time
     * @param title
     * @param callback
     */
    public static void createPoll(Map<String, String> options, long time, String title, ZBCloudApiCallback callback) {

        String optionStr = "";
        for (String option : options.keySet()) {
            optionStr += option + ":" + String.valueOf(options.get(option)) + ",";
        }
        optionStr = optionStr.substring(0, optionStr.lastIndexOf(","));
        Map<String, Object> params = new HashMap<>();
        params.put("time", time);
        if (!TextUtils.isEmpty(title)) {
            params.put("title", title);
        }
        params.put("options", optionStr);

        ZBCloudApiClient.getInstance().sendZBCloudApiRequest(API_VOTE_CREATE, params, callback);
    }
    public static void createPoll(List<String> options, long time, String title, ZBCloudApiCallback callback) {

        String optionStr = "";
        for (String option : options) {
            optionStr += option+",";
        }
        optionStr = optionStr.substring(0, optionStr.lastIndexOf(","));
        Map<String, Object> params = new HashMap<>();
        params.put("time", time);
        if (!TextUtils.isEmpty(title)) {
            params.put("title", title);
        }
        params.put("options", optionStr);

        ZBCloudApiClient.getInstance().sendZBCloudApiRequest(API_VOTE_CREATE, params, callback);
    }

    /**
     * 最近一次的投票信息
     *
     * @param usid
     * @param callback
     */
    public static void queryLastVoteInfo(String usid, ZBCloudApiCallback callback) {
        Map<String, Object> params = new HashMap<>();
        if (!TextUtils.isEmpty(usid)) {
            params.put("usid", usid);
        }
        ZBCloudApiClient.getInstance().sendZBCloudApiRequest(API_LAST_VOTE_INFO, params, callback);
    }

    /**
     * 最新一次的投票信息
     *
     * @param voteid
     * @param callback
     */
    public static void queryNewestVoteInfo(String voteid, ZBCloudApiCallback callback) {
        Map<String, Object> params = new HashMap<>();
        params.put("vote_id", voteid);
        ZBCloudApiClient.getInstance().sendZBCloudApiRequest(API_NEWST_VOTE_INFO, params, callback);
    }


    /**
     * 投票
     *
     * @param voteId
     * @param optionKey
     * @param count
     * @param callback
     */
    public static void sendVote(String voteId, String optionKey, int count, ZBCloudApiCallback callback) {
        Map<String, Object> params = new HashMap<>();
        params.put("vote_id", voteId);
        params.put("option_key", optionKey);
        // params.put("count", count);
        params.put("oper_token", MD5encode(voteId + optionKey) + "n" + count);//MD5(voteId+optionKey)+'n'+count

        ZBCloudApiClient.getInstance().sendZBCloudApiRequest(API_VOTE_SEND, params, callback);
    }


    /**
     * 临时关闭投票
     *
     * @param vote_id
     * @param callback
     */
    public static void votePause(String vote_id, ZBCloudApiCallback callback) {
        Map<String, Object> params = new HashMap<>();
        params.put("vote_id", vote_id);

        ZBCloudApiClient.getInstance().sendZBCloudApiRequest(API_VOTE_PAUSE, params, callback);
    }

    /**
     * 结束投票
     *
     * @param vote_id
     * @param callback
     */
    public static void voteStop(String vote_id, ZBCloudApiCallback callback) {
        Map<String, Object> params = new HashMap<>();
        params.put("vote_id", vote_id);

        ZBCloudApiClient.getInstance().sendZBCloudApiRequest(API_VOTE_STOP, params, callback);
    }

    /**
     * 恢复临时投票
     *
     * @param vote_id
     * @param callback
     */
    public static void voteRestart(String vote_id, ZBCloudApiCallback callback) {
        Map<String, Object> params = new HashMap<>();
        params.put("vote_id", vote_id);

        ZBCloudApiClient.getInstance().sendZBCloudApiRequest(API_VOTE_RESTART, params, callback);
    }

    /**
     * 重新设置投票时长（0：永久有效）
     *
     * @param vote_id
     * @param time
     * @param callback
     */
    public static void updateVoteTime(String vote_id, long time, ZBCloudApiCallback callback) {
        Map<String, Object> params = new HashMap<>();
        params.put("vote_id", vote_id);
        params.put("time", time);

        ZBCloudApiClient.getInstance().sendZBCloudApiRequest(API_VOTE_RESETTIME, params, callback);
    }

    /**
     * 加密
     *
     * @param string
     * @return
     */
    public static String MD5encode(String string) {
        byte[] hash = new byte[0];
        try {
            hash = MessageDigest.getInstance("MD5").digest(
                    string.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

}
