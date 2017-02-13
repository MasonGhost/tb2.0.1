# 信息弹框
提示信息弹框，包括发送失败，发送成功，正在发送的状态提示。

包括了4个公开的静态方法：
```
 /**
     * 显示错误的状态
     *
     * @param context
     * @param text    错误或失败状态的提示消息
     */
    public static void showStateError(Context context, String text) {
        initDialog(R.mipmap.msg_box_remind, text, context, false);
        sendHideMessage(context);
    }

    /**
     * 显示成功的状态
     *
     * @param context
     * @param text    正确或成功状态的提示消息
     */
    public static void showStateSuccess(Context context, String text) {
        initDialog(R.mipmap.msg_box_succeed, text, context, false);
        sendHideMessage(context);
    }

    /**
     * 显示进行中的状态
     *
     * @param context
     * @param text    进行中的提示消息
     */
    public static void showStateIng(Context context, String text) {
        initDialog(R.drawable.frame_loading_grey, text, context, false);
        handleAnimation(true);
    }

    /**
     * 进行中的状态变为结束
     *
     * @param context
     */
    public static void showStateEnd(Context context) {
        handleAnimation(false);
        hideDialog(context);
    }

```

- showStateError()和showStateSuccess()方法，通过发送一个延迟1s的消息，实现1s的信息弹框显示；
- showStateIng()和showStateEnd()是一组对应的方法，控制信息弹框长时间显示和关闭。

2017年2月13日10:41:21