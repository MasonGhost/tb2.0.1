package com.zhiyicx.imsdk.utils;

import android.text.TextUtils;
import android.util.Base64;

import com.zhiyicx.imsdk.core.ImService;
import com.zhiyicx.imsdk.core.autobahn.DataDealUitls;
import com.zhiyicx.imsdk.entity.MessageContainer;

import org.msgpack.MessagePack;
import org.msgpack.packer.Packer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * des:http://www.kancloud.cn/xiew/webim/150376
 * Created by jungle on 16/5/17.
 * com.zhiyicx.zhibo.util
 * zhibo_android
 * email:335891510@qq.com
 */
public class MessageHelper {

    /**
     * 0 客户端ping服务端，此类型数据包没有消息主体
     * 1 服务端pong客户端，此类型数据包没有消息主体
     * 2 消息包，此类型必须包含消息主体
     * 3 应答消息包，此类型必须包含消息主体
     * 4 错误应答消息包，此类型必须包含消息主体
     * 5~f 预留类型，暂未启用
     */
    public static final byte TITLE_TYPE_PING = 0;
    public static final byte TITLE_TYPE_PONG = 1;
    public static final byte TITLE_TYPE_MESSAGE = 2;
    public static final byte TITLE_TYPE_MESSAGE_ACK = 3;
    public static final byte TITLE_TYPE_MESSAGE_ERR_ACK = 4;

    public static final byte BINARY_TITLE_TYPE_PING = 0;
    public static final byte BINARY_TITLE_TYPE_PONG = 1 << 4;
    public static final byte BINARY_TITLE_TYPE_MESSAGE = 1 << 5;
    public static final byte BINARY_TITLE_TYPE_MESSAGE_ACK = 1 << 5 | 1 << 4;
    public static final byte BINARY_TITLE_TYPE_MESSAGE_ERR_ACK = 1 << 6;
    /**
     * 消息体序列化类型:
     * 0代表json
     * 1代表msgpack
     * 2、3预留
     */
    public static final byte BODY_SERILIZE_TYPE_JSON = 0;
    public static final byte BODY_SERILIZE_TYPE_MSGPACK = 1 << 2;
    /**
     * gzip压缩
     */
    public static final byte BODY_COMPRS_TYPE_GZIP = 3;
    /**
     * zlib压缩
     */
    public static final byte BODY_COMPRS_TYPE_ZLIB = 1 << 1;
    /**
     * deflate压缩
     */
    public static final byte BODY_COMPRS_TYPE_NOT_DEFLATE = 1;
    /**
     * 不压缩
     */
    public static final byte BODY_COMPRS_TYPE_NOT_SOUPPORT = 0;

    /**
     * 消息体序列化类型:
     * 0代表json
     * 1代表msgpack
     * 2、3预留
     */
    public static final byte SERILIZE_TYPE_JSON = 0;
    public static final byte SERILIZE_TYPE_MSGPACK = 1;
    public static final byte TYPE_UNKNOW = -1;

    /**
     * 文本方式ping
     *
     * @return
     */
    public static String getPingText() {
        return TITLE_TYPE_PING + "";
    }

    /**
     * body 满足json序列化
     *
     * @param body
     * @return
     */
    public static String getSendText(String body) {

        return TITLE_TYPE_MESSAGE + body;
    }

    /**
     * 二进制方式ping
     *
     * @return
     */
    public static byte[] getPingBinary() {
        return new byte[]{BINARY_TITLE_TYPE_PING};
    }

    /**
     * 消息数据类容和消息事件名都拼接在一起都放在body数组中
     *
     * @param body
     * @param bodySerriType
     * @return
     */
    public static byte[] getSendBinary(byte[] body, int bodySerriType) {
        if (body.length > 1024) {
            return getSendBinary(body, bodySerriType, ImService.COMPRS_ZLIB);
        }
        return getSendBinary(body, bodySerriType, ImService.COMPRS_NOT_SOUPPORT);

    }

    /**
     * 消息数据类容和消息事件名都拼接在一起都放在body数组中
     *
     * @param body
     * @param bodySerriType
     * @param comprs
     * @return
     */
    public static byte[] getSendBinary(byte[] body, int bodySerriType, int comprs) {
        byte title = BINARY_TITLE_TYPE_MESSAGE;
        switch (comprs) {
            case ImService.COMPRS_ZLIB:
                title |= BODY_COMPRS_TYPE_ZLIB;
                body = ZipHelper.compressForZlib(body);
                break;
            case ImService.COMPRS_GZIP:
                title |= BODY_COMPRS_TYPE_GZIP;
                body = ZipHelper.compressForGzip(body);
                break;

            case ImService.COMPRS_DEFLATE:
            case ImService.COMPRS_NOT_SOUPPORT:
                break;

        }

        switch (bodySerriType) {
            case SERILIZE_TYPE_JSON:
                title |= BODY_SERILIZE_TYPE_JSON;
                break;
            case SERILIZE_TYPE_MSGPACK:
                title |= BODY_SERILIZE_TYPE_MSGPACK;
                break;
        }
        return binarySplit(new byte[]{title}, body);

    }

    /**
     * 把消息转化为服务器可识别的msgpack序列化数据
     *
     * @param body
     * @param mEvent
     * @return
     */
    public static byte[] getSendBinaryForMsgpack(String body, String mEvent, int id) {
        if (body.length() > 1024) {
            return getSendBinaryForMsgpack(body, mEvent, ImService.COMPRS_ZLIB, id);
        }
        return getSendBinaryForMsgpack(body, mEvent, ImService.COMPRS_NOT_SOUPPORT, id);

    }

    /**
     * 把消息转化为服务器可识别的msgpack序列化数据
     *
     * @param body
     * @param mEvent
     * @param comprs
     * @return
     */
    public static byte[] getSendBinaryForMsgpack(String body, String mEvent, int comprs, int id) {
        byte title = BINARY_TITLE_TYPE_MESSAGE | BODY_SERILIZE_TYPE_MSGPACK;
        MessagePack msgPack = new MessagePack();
        byte[] result = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Packer packer = msgPack.createPacker(out);
        List<String> src = new ArrayList<String>();
        src.add(mEvent);
        src.add(body);
        src.add(id + "");
        try {
            packer.write(src);
            result = out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        switch (comprs) {
            case ImService.COMPRS_ZLIB:
                title |= BODY_COMPRS_TYPE_ZLIB;
                result = ZipHelper.compressForZlib(body);
                break;
            case ImService.COMPRS_GZIP:
                title |= BODY_COMPRS_TYPE_GZIP;
                result = ZipHelper.compressForGzip(body);
                break;

            case ImService.COMPRS_DEFLATE:
            case ImService.COMPRS_NOT_SOUPPORT:

                break;

        }

        return binarySplit(new byte[]{title}, result);

    }

    /**
     * 把消息转化为服务器可识别的msgpack序列化数据
     * 数据大于1k自动压缩
     *
     * @param body
     * @param id   消息标识
     * @return
     */

    public static byte[] getMessageForMsgpack(MessageContainer body, int id) throws IOException {
        byte title = BINARY_TITLE_TYPE_MESSAGE | BODY_SERILIZE_TYPE_MSGPACK;
        MessagePack msgPack = new MessagePack();
        byte[] result = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Packer packer = msgPack.createPacker(out);
        List<Object> src = new ArrayList();
        src.add(body.mEvent);//事件名
        Map<String, Object> map = new HashMap();

        map = DataDealUitls.transBean2Map(body.msg);
        /**
         * "msg" 对应Message 里面的属性msg名字
         */
        try {
            if (map.containsKey("ext") && body.msg != null && body.msg.ext != null) {
                Map<String, Object> msgMap = DataDealUitls.transBean2Map(body.msg.ext);
                map.put("ext", msgMap);
//                if (msgMap.containsKey("gift") && body.ext.msg.gift != null) {
//                    msgMap.put("gift", transBean2Map(body.ext.msg.gift));
//                    map.put("msg", msgMap);
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        src.add(map);//消息数据
        if (id != 0) {
            src.add(id);//自定义消息id
        }
        packer.write(src);
        result = out.toByteArray();
//        msgpack 数据读取
//        try {
//            Value value = msgPack.read(result);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        if (result.length > 1024) {
            title |= BODY_COMPRS_TYPE_ZLIB;
            result = ZipHelper.compressForZlib(result);
        }


        return binarySplit(new byte[]{title}, result);

    }


    /**
     * 把消息转化为服务器可识别的msgpack序列化数据
     *
     * @param body
     * @param comprs
     * @return
     */
    public static byte[] getSendBinaryForMsgpack(MessageContainer body, int comprs) throws IOException {
        byte title = BINARY_TITLE_TYPE_MESSAGE | BODY_SERILIZE_TYPE_MSGPACK;
        MessagePack msgPack = new MessagePack();
        byte[] result = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Packer packer = msgPack.createPacker(out);
        packer.write(body);
        result = out.toByteArray();
        switch (comprs) {
            case ImService.COMPRS_ZLIB:
                title |= BODY_COMPRS_TYPE_ZLIB;
                result = ZipHelper.compressForZlib(result);
                break;
            case ImService.COMPRS_GZIP:
                title |= BODY_COMPRS_TYPE_GZIP;
                result = ZipHelper.compressForGzip(result);
                break;

            case ImService.COMPRS_DEFLATE:
            case ImService.COMPRS_NOT_SOUPPORT:

                break;

        }
        return binarySplit(new byte[]{title}, result);

    }

    /**
     * 把消息转化为服务器可识别的json序列化数据
     *
     * @param body
     * @param mEvent
     * @return
     */
    public static byte[] getSendBinaryForJson(byte[] body, String mEvent, int id) {
        if (body.length > 1024) {
            return getSendBinaryForJson(body, mEvent, ImService.COMPRS_ZLIB, id);
        }

        return getSendBinaryForJson(body, mEvent, ImService.COMPRS_NOT_SOUPPORT, id);

    }

    /**
     * 把消息转化为服务器可识别的json序列化数据
     *
     * @param body
     * @param mEvent
     * @param comprs
     * @return
     */
    public static byte[] getSendBinaryForJson(byte[] body, String mEvent, int comprs, int id) {
        byte title = BINARY_TITLE_TYPE_MESSAGE;
        switch (comprs) {
            case ImService.COMPRS_ZLIB:
                title |= BODY_COMPRS_TYPE_ZLIB;
                body = ZipHelper.compressForZlib(body);
                break;
            case ImService.COMPRS_GZIP:
                title |= BODY_COMPRS_TYPE_GZIP;
                body = ZipHelper.compressForGzip(body);
                break;
            case ImService.COMPRS_DEFLATE:
            case ImService.COMPRS_NOT_SOUPPORT:

                break;

        }


        String event = "[\"" + mEvent + "\",";
        String end = "," + id + "]";
        try {
            body = MessageHelper.binarySplit(event.getBytes("UTF-8"), body);
            body = MessageHelper.binarySplit(body, end.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return binarySplit(new byte[]{title}, body);

    }

    /**
     * 文本返回时去除首位包类型标识，返回body
     *
     * @param data
     * @return
     */
    public static String getRecievedBody(String data) {
        if (TextUtils.isEmpty(data)) return null;
        String title = data.substring(0, 1);
        switch (title) {
            case TITLE_TYPE_PING + "":
                return TITLE_TYPE_PING + "";
            case TITLE_TYPE_PONG + "":
                return TITLE_TYPE_PONG + "";
            case TITLE_TYPE_MESSAGE + "":
                return data.substring(1);
        }
        return null;
    }

    /**
     * 二进制返回去除首位包类型标识，返回body
     *
     * @param data
     * @return
     */
    public static String getRecievedBody(byte[] data) {
        return new String(getRecievedBodyByte(data));
    }

    /**
     * 二进制返回去除首位包类型标识，返回body
     *
     * @param data
     * @return
     */
    public static byte[] getRecievedBodyByte(byte[] data) {
        if (data == null) return null;
        byte title = data[0];

        byte packType = (byte) (title >> 4);
        switch (packType) {
            case TITLE_TYPE_PING:
                return new byte[]{48};//48ascii码对应0
            case TITLE_TYPE_PONG:
                return new byte[]{49};
            case TITLE_TYPE_MESSAGE:


                break;
            default:
                break;
        }
        byte[] bodyByte = new byte[data.length - 1];
        System.arraycopy(data, 1, bodyByte, 0, data.length - 1);
        /**
         * 如果为0表示消息主体未压缩
         * 如果为1表示消息主体压缩格式为deflate
         * 如果为2表示消息主体压缩格式为zlib
         * 如果为3表示消息主体压缩格式为gzip
         */
        switch (3 & title) {
            case 0:

                break;
            case 1:

                break;
            case 2:
                bodyByte = ZipHelper.decompressForZlib(bodyByte);
                break;
            case 3:
                try {
                    bodyByte = ZipHelper.decompressForGzip(bodyByte).getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;


        }
        return bodyByte;
    }

    /**
     * 检查返回的消息包类型
     *
     * @param data
     * @return
     */
    public static byte getPackageType(byte[] data) {
        if (data == null) return TYPE_UNKNOW;
        byte title = data[0];

        return (byte) ((title & 255) >> 4);
    }

    /**
     * 检查返回的消息体的序列化格式
     *
     * @param data
     * @return
     */
    public static byte getPackageSerilType(byte[] data) {
        if (data == null) return TYPE_UNKNOW;
        byte title = data[0];
        return (byte) ((title & 15) >> 2);
    }


    /**
     * 数组拼接（二进制数据凭借）
     *
     * @param a
     * @param b
     * @return
     */
    public static byte[] binarySplit(byte[] a, byte[] b) {
        if (a == null && b == null) {
            return null;
        }
        if (a != null && b == null) return a;
        if (a == null && b != null) return b;
        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    public static <T> T base64Str2Object(String productBase64) {
        T device = null;
        if (productBase64 == null) {
            return null;
        }
        // 读取字节
        byte[] base64 = Base64.decode(productBase64.getBytes(), Base64.DEFAULT);

        // 封装到字节流
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        try {
            // 再次封装
            ObjectInputStream bis = new ObjectInputStream(bais);
            // 读取对象
            device = (T) bis.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return device;
    }

    public static <T> String object2Base64Str(T object) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {   //Device为自定义类
            // 创建对象输出流，并封装字节流
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            // 将对象写入字节流
            oos.writeObject(object);
            // 将字节流编码成base64的字符串
            return new String(Base64.encode(baos
                    .toByteArray(), Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}

