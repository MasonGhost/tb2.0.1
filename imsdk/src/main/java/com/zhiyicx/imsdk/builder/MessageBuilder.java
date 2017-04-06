package com.zhiyicx.imsdk.builder;

import android.text.TextUtils;

import com.zhiyicx.imsdk.core.autobahn.DataDealUitls;
import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.imsdk.entity.MessageExt;
import com.zhiyicx.imsdk.entity.MessageType;
import com.zhiyicx.imsdk.entity.Zan;

import java.util.List;
import java.util.Map;

/**
 * Created by jungle on 16/7/27.
 * com.zhiyicx.imsdk.builder
 * zhibo_android
 * email:335891510@qq.com
 */
public class MessageBuilder {
    /**
     * 创建非实时文本消息
     *
     * @param cid    房间id
     * @param ZBUSID
     * @param text   内容
     * @return Message
     */
    public static Message createTextMessage(int cid, String ZBUSID, String text) {
        if (TextUtils.isEmpty(text)) return null;
        return createMessage(0, cid, ZBUSID, null, MessageType.MESSAGE_TYPE_TEXT, false, text);
    }

    /**
     * 创建文本消息
     *
     * @param cid    房间id
     * @param ZBUSID
     * @param text   内容
     * @param rt     是否实时消息
     * @return Message
     */
    public static Message createTextMessage(int cid, String ZBUSID, String text, boolean rt) {
        if (TextUtils.isEmpty(text)) return null;
        return createMessage(0, cid, ZBUSID, null, MessageType.MESSAGE_TYPE_TEXT, rt, text);
    }

    /**
     * 创建文本消息
     *
     * @param msgid
     * @param cid    房间id
     * @param ZBUSID
     * @param text   内容
     * @param rt     是否实时消息
     * @return Message
     */
    public static Message createTextMessage(int msgid, int cid, String ZBUSID, String text, boolean rt) {
        if (TextUtils.isEmpty(text)) return null;
        return createMessage(msgid, cid, ZBUSID, null, MessageType.MESSAGE_TYPE_TEXT, rt, text);
    }

    /**
     * 创建非实时指定目标文本消息
     *
     * @param cid    房间id
     * @param ZBUSID
     * @param uids
     * @param text   内容
     * @return Message
     */
    public static Message createTextMessage(int msgid, int cid, String ZBUSID, List<Integer> uids, String text) {
        if (TextUtils.isEmpty(text)) return null;
        return createMessage(msgid, cid, ZBUSID, uids, MessageType.MESSAGE_TYPE_TEXT, false, text);
    }

    /**
     * 创建指定目标文本消息
     *
     * @param cid    房间id
     * @param ZBUSID
     * @param uids
     * @param text   内容
     * @param rt     是否实时消息
     * @return Message
     */
    public static Message createTextMessage(int msgid, int cid, String ZBUSID, List<Integer> uids, String text, boolean rt) {
        if (TextUtils.isEmpty(text)) return null;
        return createMessage(msgid, cid, ZBUSID, uids, MessageType.MESSAGE_TYPE_TEXT, rt, text);
    }

    /**
     * @param cid
     * @param ZBUSID
     * @return
     */
    public static Message createImageMessage(int cid, String ZBUSID) {
        return createMessage(0, cid, ZBUSID, null, MessageType.MESSAGE_TYPE_IMAGE, false, null);
    }

    /**
     * @param cid
     * @param ZBUSID
     * @param rt
     * @return
     */
    public static Message createImageMessage(int msgid, int cid, String ZBUSID, boolean rt) {
        return createMessage(msgid, cid, ZBUSID, null, MessageType.MESSAGE_TYPE_IMAGE, rt, null);
    }

    /**
     * @param cid
     * @param ZBUSID
     * @param uids
     * @param rt
     * @return
     */
    public static Message createImageMessage(int msgid, int cid, String ZBUSID, List<Integer> uids, boolean rt) {
        return createMessage(msgid, cid, ZBUSID, uids, MessageType.MESSAGE_TYPE_IMAGE, rt, null);
    }

    /**
     * 赞
     *
     * @param cid
     * @param ZBUSID
     * @param gitType
     * @return Message
     */
    public static Message createZanMessage(int cid, String ZBUSID, int gitType) {
        return createCustomMessage(0, cid, ZBUSID, MessageType.MESSAGE_CUSTOM_ID_ZAN, true, DataDealUitls.transBean2Map(getZanType(gitType)), true);
    }

    /**
     * 赞
     *
     * @param cid
     * @param ZBUSID
     * @param gitType
     * @param rt
     * @return Message
     */
    public static Message createZanMessage(int msgid, int cid, String ZBUSID, int gitType, boolean rt) {
        return createCustomMessage(msgid, cid, ZBUSID, MessageType.MESSAGE_CUSTOM_ID_ZAN, true, DataDealUitls.transBean2Map(getZanType(gitType)), rt);
    }

    /**
     * 关注
     *
     * @param cid
     * @param ZBUSID
     * @return Message
     */
    public static Message createAttentionMessage(int cid, String ZBUSID) {
        return createCustomMessage(0, cid, ZBUSID, MessageType.MESSAGE_CUSTOM_ID_FLLOW, true, null, true);
    }

    /**
     * 关注
     *
     * @param cid
     * @param ZBUSID
     * @param rt
     * @return Message
     */
    public static Message createAttentionMessage(int msgid, int cid, String ZBUSID, boolean rt) {
        return createCustomMessage(msgid, cid, ZBUSID, MessageType.MESSAGE_CUSTOM_ID_FLLOW, true, null, rt);
    }

    /**
     * 礼物消息
     *
     * @param cid
     * @param ZBUSID
     * @param jsonstr
     * @param rt
     * @return Message
     */
    public static Message createGiftMessage(int msgid, int cid, String ZBUSID, Map jsonstr, boolean rt) {
        return createCustomMessage(msgid, cid, ZBUSID, MessageType.MESSAGE_CUSTOM_ID_GIFT, true, jsonstr, rt);
    }

    /**
     * 自定义消息
     *
     * @param cid
     * @param ZBUSID
     * @param isEnable
     * @param jsonstr
     * @return Message
     */
    public static Message createCustomMessage(int cid, String ZBUSID, int customID, boolean isEnable, Map jsonstr) {
        if (isEnable)
            return createMessage(0, cid, null, MessageType.MESSAGE_TYPE_CUSTOM_ENAABLE, false, null, ZBUSID, customID, jsonstr);
        else
            return createMessage(0, cid, null, MessageType.MESSAGE_TYPE_CUSTOM_DISABLE, false, null, ZBUSID, customID, jsonstr);

    }

    /**
     * 自定义消息
     *
     * @param cid
     * @param isEnable
     * @param ext
     * @return Message
     */
    public static Message createCustomMessage(int msgid, int cid, boolean isEnable, MessageExt ext, boolean rt) {
        if (isEnable)
            return createMessage(msgid, cid, MessageType.MESSAGE_TYPE_CUSTOM_ENAABLE, rt, ext);
        else
            return createMessage(msgid, cid, MessageType.MESSAGE_TYPE_CUSTOM_DISABLE, rt, ext);

    }


    /**
     * 自定义消息
     *
     * @param cid
     * @param ZBUSID
     * @param isEnable
     * @param jsonstr
     * @param rt
     * @return Message
     */
    public static Message createCustomMessage(int msgid, int cid, String ZBUSID, int customID, boolean isEnable, Map jsonstr, boolean rt) {
        if (isEnable)
            return createMessage(msgid, cid, null, MessageType.MESSAGE_TYPE_CUSTOM_ENAABLE, rt, null, ZBUSID, customID, jsonstr);
        else
            return createMessage(msgid, cid, null, MessageType.MESSAGE_TYPE_CUSTOM_DISABLE, rt, null, ZBUSID, customID, jsonstr);

    }

    /**
     * 完全自定义消息
     *
     * @param msgid
     * @param cid
     * @param uids
     * @param rt
     * @param text
     * @param ZBUSID
     * @param customID
     * @param ext
     * @param isEnable
     * @return
     */
    public static Message createMessage(int msgid, int cid, List<Integer> uids, boolean rt, String text, String ZBUSID, int customID, Map ext, boolean isEnable) {
        if (isEnable)
            return createMessage(msgid, cid, uids, MessageType.MESSAGE_TYPE_CUSTOM_ENAABLE, rt, text, ZBUSID, customID, ext);
        else
            return createMessage(msgid, cid, uids, MessageType.MESSAGE_TYPE_CUSTOM_DISABLE, rt, text, ZBUSID, customID, ext);
    }

    /**
     * 完全自定义消息
     *
     * @param cid
     * @param uids
     * @param type
     * @param rt
     * @param text
     * @param ZBUSID
     * @param ext
     * @return
     */
    private static Message createMessage(int msgid, int cid, List<Integer> uids, int type, boolean rt, String text, String ZBUSID, int customID, Map ext) {
        return getMessage(msgid, cid, uids, type, rt, text, ZBUSID, customID, ext);
    }

    /**
     * 指定目标自定义消息
     *
     * @param cid
     * @param ZBUSID
     * @param uids
     * @param isEnable
     * @param text
     * @param jsonstr
     * @return Message
     */
    private static Message createCustomMessage(int cid, String ZBUSID, int customID, List<Integer> uids, boolean isEnable, String text, Map jsonstr) {
        if (isEnable)
            return createMessage(0, cid, uids, MessageType.MESSAGE_TYPE_CUSTOM_ENAABLE, false, text, ZBUSID, customID, jsonstr);
        else
            return createMessage(0, cid, uids, MessageType.MESSAGE_TYPE_CUSTOM_DISABLE, false, text, ZBUSID, customID, jsonstr);

    }


    /**
     * 指定目标自定义消息
     *
     * @param cid
     * @param ZBUSID
     * @param uids
     * @param isEnable
     * @param text
     * @param jsonstr
     * @param rt
     * @return Message
     */
    private static Message createCustomMessage(int msgid, int cid, String ZBUSID, int customID, List<Integer> uids, boolean isEnable, String text, Map jsonstr, boolean rt) {
        if (isEnable)
            return createMessage(msgid, cid, uids, MessageType.MESSAGE_TYPE_CUSTOM_ENAABLE, rt, text, ZBUSID, customID, jsonstr);
        else
            return createMessage(msgid, cid, uids, MessageType.MESSAGE_TYPE_CUSTOM_DISABLE, rt, text, ZBUSID, customID, jsonstr);

    }

    private static Zan getZanType(int msgType) {
        switch (msgType) {
            case MessageType.MESSAGE_CUSTOM_ID_ZAN_BLUE:
            case MessageType.MESSAGE_CUSTOM_ID_ZAN_GREEN:
            case MessageType.MESSAGE_CUSTOM_ID_ZAN_RED:
            case MessageType.MESSAGE_CUSTOM_ID_ZAN_YELLOW:
            case MessageType.MESSAGE_CUSTOM_ID_ZAN_ORANGE:
            case MessageType.MESSAGE_CUSTOM_ID_ZAN_PURPLE:
                break;
            default:
                msgType = (int) (0 + Math.random() * (5 - 0 + 1)) + 200;
                break;

        }
        return new Zan(msgType);
    }

    private static Message createMessage(int msgid, int cid, String ZBUSID, int type, boolean rt) {
        Message msg = new Message();
        msg.id = msgid;
        msg.ext = new MessageExt(ZBUSID, null);
        msg.cid = cid;
        msg.rt = rt;
        msg.type = type;
        msg.create_time=System.currentTimeMillis();
        return msg;
    }

    private static Message createMessage(int msgid, int cid, int type, boolean rt, MessageExt ext) {

        Message msg = new Message();
        msg.id = msgid;
        msg.ext = ext;
        msg.cid = cid;
        msg.rt = rt;
        msg.type = type;
        msg.create_time=System.currentTimeMillis();
        return msg;

    }

    private static Message createMessage(int msgid, int cid, String ZBUSID, List<Integer> uids, int type, boolean rt, String text) {

        Message msg = createMessage(msgid, cid, ZBUSID, type, rt);
        msg.txt = text;
        msg.to = uids;
        msg.create_time=System.currentTimeMillis();
        return msg;
    }


    private static Message getMessage(int msgid, int cid, List<Integer> uids, int type, boolean rt, String text, String ZBUSID, int customID, Map ext) {
        Message msg = createMessage(msgid, cid, ZBUSID, type, rt);
        msg.ext.custom = ext;
        msg.ext.customID = customID;
        msg.txt = text;
        msg.to = uids;
        msg.create_time=System.currentTimeMillis();
        return msg;
    }
}
