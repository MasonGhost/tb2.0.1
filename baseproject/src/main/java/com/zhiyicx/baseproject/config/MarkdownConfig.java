package com.zhiyicx.baseproject.config;

/**
 * @Author Jliuer
 * @Date 2017/08/08/10:08
 * @Email Jliuer@aliyun.com
 * @Description 自定义 markdown 标签
 */
public class MarkdownConfig {
    //@![title](file id)
    /**
     * 用于发布资讯 拼装图片
     */
    public static final String IMAGE_TAG = "@![%s](%d)";

    /**
     * 用于判断是否存在 scheme
     */
    public static final String SCHEME_TAG = "[\\s\\S]+:[\\s\\S]+";
    public static final String SCHEME_ZHIYI = "zhiyi:";
    public static final String SCHEME_HTTP = "http://";

    /**
     * 短链接 标识
     */
    public static final String LINK_EMOJI = "\uD83D\uDCCE";
    public static final String IMAGE_TITLE = "image";

    /**
     * 用于 提取 markdown 格式 图片id
     */
    public static final String IMAGE_FORMAT = "@!\\[.*?]\\((\\d+)\\)";

    /**
     * 用于，编辑器内容转换
     */
    public static final String IMAGE_FORMAT_HTML = "@!(\\[(.*?)])\\(((\\d+))\\)";

    /**
     * 用于 提取 markdown 格式 链接
     */
    public static final String LINK_FORMAT = "\\[(.*?)]\\((.*?)\\)";

    // <(span|/span)("[^"]*"|'[^']*'|[^'">])*>    <("[^"]*"|'[^']*'|[^'">])*>
    public static final String HTML_FORMAT = "<(\"[^\"]*\"|'[^']*'|[^'\">])*>";
    public static final String TEST_HTML_FORMAT = "<((div)|/(div))(\"[^\"]*\"|'[^']*'|[^'\">])*>";

    /**
     * 匹配 匹配中文，英文字母和数字
     */
    public static final String NORMAL_FORMAT = "<[^\\u4e00-\\u9fa5]+>|[^\\u4e00-\\u9fa5a-zA-Z0-9]+";

    /**
     * 用于提取 短链接
     */
    public static final String NETSITE_FORMAT = "<{0,1}((http|ftp|https)://)(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[#a-zA-Z0-9\\&%_\\./-~-]*)?>{0,1}";

    public static final String NETSITE_A_FORMAT = "(?<!<a href=\")<{0,1}((http|ftp|https)://)(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[#a-zA-Z0-9\\&%_\\./-~-]*)?>{0,1}";

}