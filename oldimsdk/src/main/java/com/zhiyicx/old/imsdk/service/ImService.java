package com.zhiyicx.old.imsdk.service;

import android.text.TextUtils;
import android.util.Log;

import com.zhiyicx.old.imsdk.de.tavendo.autobahn.WebSocket;
import com.zhiyicx.old.imsdk.de.tavendo.autobahn.WebSocketConnection;
import com.zhiyicx.old.imsdk.de.tavendo.autobahn.WebSocketConnectionHandler;
import com.zhiyicx.old.imsdk.entity.Message;
import com.zhiyicx.old.imsdk.entity.MessageContainer;
import com.zhiyicx.old.imsdk.utils.MessageHelper;
import com.zhiyicx.old.imsdk.utils.common.LogUtils;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by jess on 16/5/11.
 */
public class ImService {
    private final String TAG = this.getClass().getSimpleName();
    /**
     * websocket连接建立
     */
    public static final String WEBSOCKET_CONNECTED = "websocket.connected";
    public static final String WEBSOCKET_DISCONNECTED = "websocket.disconnected";
    public static final String WEBSOCKET_CONNECTED_ERR = "websocket.connected_err";
    public static final String WEBSOCKET_SENDMESSAGE_TIMEOUT = "websocket.sendmessage.timeout";
    /**
     * 聊天室请求事件
     */
    public static final String CHATROOM_MC = "chatroom.mc";
    public static final String CONVERSATION_LEAVE = "convr.leave";
    public static final String CONVERSATION_JOIN = "convr.join";

    /**
     * 聊天室请求事件返回
     */
    public static final String CHATROOM_MC_ACK = "chatroom.mc.ack";
    public static final String CONVERSATION_LEAVE_ACK = "convr.leave.ack";
    public static final String CONVERSATION_JOIN_ACK = "convr.join.ack";
    /**
     * 消息请求事件
     */
    public static final String MSG = "convr.msg";
    /**
     * 获取指定序号消息
     */
    public static final String CONVR_MSG_PLUCK = "convr.msg.pluck";
    public static final String CONVR_MSG_SYNC = "convr.msg.sync";

    /**
     * 获取会话信息
     */
    public static final String GET_CONVERSATON_INFO = "convr.get";
    public static final String GET_CONVERSATON_INFO_TIMEOUT = "convr.get.timeout";


    /**
     * 消息事件返回
     */
    public static final String MSG_ACK = "msg.ack";

    /**
     * 会话
     */
    public static final String CONVR_START = "convr.start";
    public static final String CONVR_OPEN = "convr.open";
    public static final String CONVR_PAUSE = "convr.pause";
    public static final String CONVR_END = "convr.end";
    public static final String CONVR_MBR_JOIN = "convr.mbr.join";
    public static final String CONVR_MBR_QUIT = "convr.mbr.quit";//当会话中有成员退出时，会话中的其他成员会收到此事件,被踢和主动退出均会收到此事件，区别于一个操作者uid
    public static final String CONVR_MBR_KICK = "convr.mbr.kick";

    private static final String WEB_SOCKET_PORT = "9900";
    private static final String WEB_SOCKET_HOST = "218.244.149.144";//ws://:9900/
    private static final String WEB_SOCKET_SITE_SSL = "wss://";
    private static final String WEB_SOCKET_SITE = "ws://";
    private static String WEB_SOCKET_AUTHORITY = WEB_SOCKET_SITE + WEB_SOCKET_HOST + ":" + WEB_SOCKET_PORT;
    /**
     * 返回数据类型
     * 0、代表json
     * 1、代表msgpack
     */
    public static final int BIN_JSON = 0;
    public static final int BIN_MSGPACK = 1;

    /**
     * 返回数据类型
     * 0、代表json
     * 1、代表msgpack
     */
    public static final int COMPRS_NOT_SOUPPORT = 0;
    public static final int COMPRS_DEFLATE = 1;
    public static final int COMPRS_ZLIB = 2;
    public static final int COMPRS_GZIP = 3;


    private ImListener mListener;
    private String mUri;
    /**
     * 请求参数
     */
    private String mParams;

    public String getParams() {
        return mParams;
    }

    public void setParams(String web_socket_authority, String token, int serial, int comprs) {
        mParams = "token=" + token + "&serial=" + serial + "&comprs=" + comprs;
        setUrlParams(mParams, web_socket_authority);
    }

    private WebSocket mConnection;//autobaha

    public ImService(WebSocket webSocket) {

        this.mConnection = webSocket;
        initService();
    }

    public ImService() {

        this.mConnection = new WebSocketConnection();
        initService();
    }

    private void initService() {

        // TODO: 16/5/18 初始化设置

    }

    public void setUrlParams(String params, String web_socket_authority) {
        if (!TextUtils.isEmpty(web_socket_authority))
            WEB_SOCKET_AUTHORITY = web_socket_authority;
        this.mUri = WEB_SOCKET_AUTHORITY + "/?" + params;
        LogUtils.warnInfo(TAG, mUri + ".....");
    }

    /**
     * 设置url地址
     *
     * @param url
     */
    public void setUrl(String url) {
        this.mUri = url;

    }

    /**
     * 连接IM服务器
     */

    public void connect() {
        LogUtils.debugInfo("SocketService", mUri);
        try {
            mConnection.connect(mUri, new WebSocketConnectionHandler() {
                @Override
                public void onOpen() {
                    if (mListener != null) {
                        mListener.onConnected();
                    }
                }

                @Override
                public void onTextMessage(String payload) {
                    if (mListener != null) {
                        mListener.onMessage(payload);

                    }
                }

                @Override
                public void onBinaryMessage(byte[] payload) {
                    if (mListener != null) {
                        mListener.onMessage(payload);
                    }
                }

                @Override
                public void onClose(int code, String reason) {
                    if (mListener != null) {
                        mListener.onDisconnect(code, reason);
                    }
                }
            });

        } catch (Exception e) {
            if (mListener != null) {
                mListener.onError(e);
            }
            Log.d("ImService", e.toString());
        }


    }

    /**
     * 断开连接IM服务器
     */

    public void disconnect() {
        mConnection.disconnect();
    }


    public boolean isConnected() {
        return mConnection.isConnected();
    }

    /**
     * ping服务器(自定义心跳包）
     */
    public void ping() {
        mConnection.sendBinaryMessage(MessageHelper.getPingBinary());

    }

    /**
     * 获取对话信息
     *
     * @param cid
     */
    public void sendGetConversatonInfo(int cid, String field) {
        if (cid == 0) return;
//        String tmp = "{\"cid\":" + cid + "}";
        Map<String, Object> params = new HashMap<>();
        params.put("cid", cid);
        if (!TextUtils.isEmpty(field))
            params.put("field", field);

        sendJsonData(new JSONObject(params).toString(), GET_CONVERSATON_INFO, 0);

    }

    /**
     * 获取对话信息
     *
     * @param cid
     */
    public void sendGetConversatonInfo(List<Integer> cid, String field) {
        if (cid == null || cid.size() == 0) return;
//        String tmp = "{\"cid\":" + cid + "}";
        Map<String, Object> params = new HashMap<>();
        String cidstr = "";
        for (int i = 0; i < cid.size(); i++) {
            cidstr += cid.get(i) + ",";
        }
        if (cidstr.length() > 0)
            cidstr = cidstr.substring(0, cidstr.length() - 1);
        params.put("cid", cid);
        if (!TextUtils.isEmpty(field))
            params.put("field", field);
        sendJsonData(new JSONObject(params).toString(), GET_CONVERSATON_INFO, 0);

    }

    /**
     * 查询丢失的消息
     *
     * @param cid
     * @param seq
     */
    public void sendPluckMessage(int cid, List<Integer> seq, int msgid) {

        Map<String, Object> params = new HashMap<>();
        params.put("cid", cid);
        params.put("seq", seq);
        sendJsonData(new JSONObject(params).toString(), GET_CONVERSATON_INFO, msgid);
    }

    /**
     * 查询丢失的消息
     *
     * @param cid
     * @param gt
     * @param lt
     * @param limit
     * @param msgid
     */
    public void sendSyncMessage(int cid, int gt, int lt, int limit, int msgid) {

        Map<String, Object> params = new HashMap<>();
        params.put("cid", cid);
        params.put("gt", gt);
        if (lt > 0)
            params.put("lt", lt);
        else
            params.put("limit", limit);
        sendJsonData(new JSONObject(params).toString(), CONVR_MSG_SYNC, msgid);
    }

    /**
     * 向IM服务器发送json类型数据
     */
    public void sendJsonData(String json, String mEvent, int msgid) {
        try {
            mConnection.sendBinaryMessage(MessageHelper.getSendBinaryForJson(json.getBytes("UTF-8"), mEvent, msgid));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 向IM服务器发送json类型数据
     */
    public void sendJsonData(byte[] json, String mEvent, int msgid) {
        mConnection.sendBinaryMessage(MessageHelper.getSendBinaryForJson(json, mEvent, msgid));
    }

    /**
     * 向IM服务器发送msgpack类型数据
     */
    public void sendMsgpackData(Message msg, String mEvent) {
        mConnection.sendBinaryMessage(MessageHelper.getMessageForMsgpack(new MessageContainer(mEvent, msg), msg.id));
    }

    /**
     * 向IM服务器发送msgpack类型数据
     */
    public void sendMsgpackData(MessageContainer messageContainer) {
        mConnection.sendBinaryMessage(MessageHelper.getMessageForMsgpack(messageContainer, messageContainer.msg.id));
    }

    /**
     * 加入会话
     *
     * @param data  聊天室id
     * @param msgid
     * @param pwd   会话密钥
     */
    public void joinConversation(int data, int msgid, String pwd) {
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("cid", data);
            if (!TextUtils.isEmpty(pwd))
                jsonObject.put("pwd", pwd);
            mConnection.sendBinaryMessage(MessageHelper.getSendBinaryForJson((jsonObject.toString()).getBytes("UTF-8"), CONVERSATION_JOIN, msgid));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException js) {
            js.printStackTrace();
        }
    }

    /**
     * 离开聊天室
     *
     * @param data  聊天室id
     * @param msgid
     * @param pwd   会话密钥
     */
    public void leaveConversation(int data, int msgid, String pwd) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("cid", data);
            if (!TextUtils.isEmpty(pwd))
                jsonObject.put("pwd", pwd);
            mConnection.sendBinaryMessage(MessageHelper.getSendBinaryForJson((jsonObject.toString()).getBytes("UTF-8"), CONVERSATION_LEAVE, msgid));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException js) {
            js.printStackTrace();
        }
    }

    /**
     * 设置额外的headers
     *
     * @param extraHeaders
     */
    public void setExtraHeaders(List<BasicNameValuePair> extraHeaders) {
//        mWebSocketClient.setExtraHeaders(extraHeaders);
    }

    /**
     * 设置监听器
     *
     * @param listener
     */
    public void setListener(ImListener listener) {
        if (this.mListener != null) {
            this.mListener = null;
            Runtime.getRuntime().gc();//回收
        }
        this.mListener = listener;
    }

    /**
     * 监听器
     */
    public interface ImListener {
        public void onConnected();

        public void onMessage(String message);

        public void onMessage(byte[] data);

        public void onDisconnect(int code, String reason);

        public void onError(Exception error);
    }

}
