package com.zhiyicx.imsdk.policy.timeout;

import com.zhiyicx.imsdk.core.ImService;
import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.imsdk.entity.MessageContainer;

import org.junit.Assert;
import org.junit.Test;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/1/16
 * @Contact master.jungle68@gmail.com
 */
public class TimeOutTaskTest {
    /**
     * 超时任务测试
     *
     * @throws Exception
     */
    @Test
    public void testTimeOut() throws Exception {
        //并启动TimeOut线程池
        TimeOutTaskPool timeOutTaskPool = new TimeOutTaskPool();
        new Thread(timeOutTaskPool).start();

        TimeOutTask timeOutTask = new TimeOutTask(new MessageContainer(ImService.CONVR_MSG_PLUCK, new Message(123), 100, null), System.currentTimeMillis(), new TimeOutListener() {
            @Override
            public void timeOut(MessageContainer messageContainer) {
                Assert.assertTrue(messageContainer.msg.id == 123);
            }
        });
        TimeOutTaskManager.getInstance().addTimeoutTask(timeOutTask);
        Thread.sleep(TimeOutTask.OUT_TIME + 10);
    }

    /**
     * 取消超时任务测试
     *
     * @throws Exception
     */
    @Test
    public void testCancleTimeOut() throws Exception {
        //并启动TimeOut线程池
        TimeOutTaskPool timeOutTaskPool = new TimeOutTaskPool();
        new Thread(timeOutTaskPool).start();

        TimeOutTask timeOutTask = new TimeOutTask(new MessageContainer(ImService.CONVR_MSG_PLUCK, new Message(123), 100, null), System.currentTimeMillis(), new TimeOutListener() {
            @Override
            public void timeOut(MessageContainer messageContainer) {
                Assert.assertTrue(false);
            }
        });
        TimeOutTaskManager.getInstance().addTimeoutTask(timeOutTask);
        TimeOutTaskManager.getInstance().cancleTimeoutTask(123 + "");
        Thread.sleep(TimeOutTask.OUT_TIME + 10);
    }

    /**
     * 消息自动重复次数测试
     *
     * @throws Exception
     */
    @Test
    public void testReSend() throws Exception {
        //并启动TimeOut线程池
        TimeOutTaskPool timeOutTaskPool = new TimeOutTaskPool();
        new Thread(timeOutTaskPool).start();

        TimeOutTask timeOutTask = new TimeOutTask(new MessageContainer(ImService.CONVR_MSG_PLUCK, new Message(123), 100, null), System.currentTimeMillis(), new TimeOutListener() {
            @Override
            public void timeOut(MessageContainer messageContainer) {
                Assert.assertTrue(messageContainer.reSendCounts == 1);
            }
        });
        TimeOutTaskManager.getInstance().addTimeoutTask(timeOutTask);
        Thread.sleep(TimeOutTask.OUT_TIME + 10);
    }

}