package com.zhiyicx.imsdk.policy.timeout;

/**
 * Created by jungle on 16/8/9.
 * com.zhiyicx.imsdk.policy.soupport
 * zhibo_android
 * email:335891510@qq.com
 */

import com.zhiyicx.imsdk.entity.MessageContainer;

import java.util.LinkedList;

public class MessageTaskManager {
    private static final String TAG = "MessageTaskManager";
    // UI请求队列
    private LinkedList<MessageContainer> messageTasks;
    // 任务不能重复

    private static MessageTaskManager messageTaskManager;

    private MessageTaskManager() {

        messageTasks = new LinkedList<MessageContainer>();

    }

    public static synchronized MessageTaskManager getInstance() {
        if (messageTaskManager == null) {
            messageTaskManager = new MessageTaskManager();
        }
        return messageTaskManager;
    }


    public void addMessageContanier(MessageContainer messageContainer) {
        synchronized (messageTasks) {
            messageTasks.addLast(messageContainer);
        }

    }


    public MessageContainer getMessageContanier() {
        synchronized (messageTasks) {
            if (messageTasks.size() > 0) {
                return messageTasks.removeFirst();
            }
        }
        return null;
    }
}