package com.zhiyicx.baseproject.config;

/**
 * @Author Jliuer
 * @Date 2017/08/08/10:08
 * @Email Jliuer@aliyun.com
 * @Description 自定义 markdown 标签
 */
public class MarkdownConfig {
    //@![title](file id)
    public static final String IMAGE_TAG = "@![%s](%d)";
    //    public static final String LINK_EMOJI = "\uD83D\uDD17";
    public static final String LINK_EMOJI = "\uD83D\uDCCE";
    public static final String IMAGE_TITLE = "image";
    public static final String IMAGE_RESULT = "![image](%s)";
    public static final String IMAGE_FORMAT = "@!\\[.*?]\\((\\d+)\\)";
    public static final String NETSITE_FORMAT = "<{0,1}((http|ftp|https)://)(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[#a-zA-Z0-9\\&%_\\./-~-]*)?>{0,1}";
    public static final String NETSITE_FORMAT_ = "\\S*\\s*((http|ftp|https)://)(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\\&%_\\./-~-]*)?\\s*\\S*";

}