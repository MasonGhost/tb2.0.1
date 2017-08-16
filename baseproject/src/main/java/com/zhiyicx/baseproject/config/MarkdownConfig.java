package com.zhiyicx.baseproject.config;

/**
 * @Author Jliuer
 * @Date 2017/08/08/10:08
 * @Email Jliuer@aliyun.com
 * @Description 自定义 markdown 标签
 */
public class MarkdownConfig {
    public static final String IMAGE_TAG = "@![%s](%d)";//@![title](file id)
    public static final String IMAGE_TITLE = "image";
    public static final String IMAGE_FORMAT = "(@!\\[image]\\((\\d+)\\))";
    public static final String IMAGE_RESULT = "![image](%s)";
}
