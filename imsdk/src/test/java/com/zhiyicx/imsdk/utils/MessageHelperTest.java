package com.zhiyicx.imsdk.utils;

import com.google.gson.Gson;
import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.imsdk.entity.MessageContainer;
import com.zhiyicx.imsdk.entity.MessageExt;

import org.junit.Assert;
import org.junit.Test;
import org.msgpack.MessagePack;
import org.msgpack.template.Templates;
import org.msgpack.type.Value;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.zhiyicx.imsdk.utils.MessageHelper.BINARY_TITLE_TYPE_PING;
import static com.zhiyicx.imsdk.utils.MessageHelper.TITLE_TYPE_MESSAGE;
import static com.zhiyicx.imsdk.utils.MessageHelper.TITLE_TYPE_MESSAGE_ACK;
import static com.zhiyicx.imsdk.utils.MessageHelper.TITLE_TYPE_MESSAGE_ERR_ACK;
import static com.zhiyicx.imsdk.utils.MessageHelper.TITLE_TYPE_PING;

/**
 * @Describe IM 数据解析测试
 * @Author Jungle68
 * @Date 2017/1/13
 * @Contact master.jungle68@gmail.com
 */
public class MessageHelperTest {
    /**
     * 文本 ping，首字节是 0
     *
     * @throws Exception
     */
    @Test
    public void getPingText() throws Exception {
        Assert.assertTrue(("" + TITLE_TYPE_PING).equals(MessageHelper.getPingText()));
    }

    /**
     * 文本消息,首字节是 2
     *
     * @throws Exception
     */
    @Test
    public void getSendText() throws Exception {
        Assert.assertTrue(MessageHelper.getSendText("{\"phone\":text}").startsWith("" + TITLE_TYPE_MESSAGE));
    }

    /**
     * 获取二进制的 ping 消息
     *
     * @throws Exception
     */
    @Test
    public void getPingBinary() throws Exception {
        Assert.assertArrayEquals(new byte[]{BINARY_TITLE_TYPE_PING}, MessageHelper.getPingBinary());
    }

    /**
     * 测试将消息转换层 msgpcak 格式
     *
     * @throws Exception
     */
    @Test
    public void getMessageForMsgpack() throws Exception {
        MessageContainer messageContainer = new MessageContainer();
        Message message = new Message();
        messageContainer.msg = message;
        messageContainer.mEvent = "msg";
        ArrayList<Integer> rooms = new ArrayList<>();
        rooms.add(10);
        messageContainer.roomIds = rooms;
        message.to = rooms;
        MessageExt messageExt = new MessageExt("123", new HashMap());
        message.setExt(messageExt);
        List<Object> data = new ArrayList<>();
        data.add("msg");
        data.add(message);
        data.add(999);
        List<Value> dst1 = null;
        Assert.assertTrue(MessageHelper.getPackageType(MessageHelper.getMessageForMsgpack(messageContainer, 999)) == TITLE_TYPE_MESSAGE);
        try {
            dst1 = new MessagePack().read(MessageHelper.getRecievedBodyByte(MessageHelper.getMessageForMsgpack(messageContainer, 999)), Templates.tList(Templates.TValue));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException r) {
            r.printStackTrace();
        }
        Assert.assertTrue("msg".equals(dst1.get(0).toString().replaceAll("\"", "")));
        Assert.assertTrue(new Gson().toJson(message).equals(new Gson().toJson(new Gson().fromJson(dst1.get(1).toString(), Message.class))));
        Assert.assertTrue("999".equals(dst1.get(2).toString().replaceAll("\"", "")));
    }

    /**
     * 测试将消息转换层 json 格式
     *
     * @throws Exception
     */
    @Test
    public void getSendBinaryForJson() throws Exception {
        MessageContainer messageContainer = new MessageContainer();
        Message message = new Message();
        messageContainer.msg = message;
        messageContainer.mEvent = "msg";
        ArrayList<Integer> rooms = new ArrayList<>();
        rooms.add(10);
        messageContainer.roomIds = rooms;
        message.to = rooms;
        MessageExt messageExt = new MessageExt("123", new HashMap());
        message.setExt(messageExt);
        List<Object> data = new ArrayList<>();
        data.add("msg");
        data.add(message);
        data.add(999);
        byte[] jsonData = MessageHelper.getSendBinaryForJson(new Gson().toJson(messageContainer.msg).getBytes("utf-8"), messageContainer.mEvent, 999);
        Assert.assertTrue(MessageHelper.getPackageType(jsonData) == TITLE_TYPE_MESSAGE);
        Assert.assertTrue(new Gson().toJson(data).equals(MessageHelper.getRecievedBody(jsonData)));
    }

    /**
     * 测试获取消息的消息体
     *
     * @throws Exception
     */
    @Test
    public void getRecievedBody() throws Exception {
        getSendBinaryForJson();
    }

    /**
     * 测试首字节
     * 0010 0000 对应 空格 十进制 32
     * '0' 对应  0011 0000 对应 十进制 48
     *
     * @throws Exception
     */
    @Test
    public void getPackageType() throws Exception {

        byte[] dataMsg = new byte[]{32};
        Assert.assertTrue(MessageHelper.getPackageType(dataMsg) == TITLE_TYPE_MESSAGE);
        char dataMsgAck = '0';
        byte te = (byte) dataMsgAck;
        byte[] dataMsgAckb = new byte[]{te};
        System.out.println("dataMsgAckb = " + dataMsgAckb[0]);
        System.out.println(MessageHelper.getPackageType(dataMsgAckb));
        Assert.assertTrue(MessageHelper.getPackageType(dataMsgAckb) == TITLE_TYPE_MESSAGE_ACK);
        byte[] dataError = new byte[]{64};
        Assert.assertTrue(MessageHelper.getPackageType(dataError) == TITLE_TYPE_MESSAGE_ERR_ACK);

    }

    @Test
    public void getPackageSerilType() throws Exception {

    }

    @Test
    public void binarySplit() throws Exception {

    }

    /**
     * 解析返回的数据
     *
     * @throws Exception
     */
    @Test
    public void testGetRecievedBody() throws Exception {
        String input_notsoupport = "T['msg',{'data':'nihao']";
        String resut = "['msg',{'data':'nihao']";
        Assert.assertEquals(resut, MessageHelper.getRecievedBody(input_notsoupport.getBytes()));
        System.out.println("111111-------" + MessageHelper.getRecievedBody(input_notsoupport.getBytes()));
        String input_zlib = "54['msg',{'data':'nihao']";
        Assert.assertNotEquals(resut, MessageHelper.getRecievedBody(ZipHelper.compressForZlib(input_zlib)));
        System.out.println("2222-------" + MessageHelper.getRecievedBody(ZipHelper.compressForZlib(input_zlib)));
        String input_gizp = "53['msg',{'data':'nihao']";
        Assert.assertNotEquals(resut, MessageHelper.getRecievedBody(ZipHelper.compressForGzip(input_zlib)));
        System.out.println("3333-------" + MessageHelper.getRecievedBody(ZipHelper.compressForGzip(input_zlib)));
    }


}