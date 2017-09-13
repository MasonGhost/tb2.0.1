package com.zhiyicx.thinksnsplus.data.beans;

/**
 * @author Catherine
 * @describe 更新信息的model
 * @date 2017/7/13
 * @contact email:648129313@qq.com
 */

public class UpdateInfoBean {

    private String version; // 版本号
    private long length;
    private String name;
    private String progress; //  进度
    private String content; // 更新的内容
    private boolean hasNewVersion; // 是否有新版本

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isHasNewVersion() {
        return hasNewVersion;
    }

    public void setHasNewVersion(boolean hasNewVersion) {
        this.hasNewVersion = hasNewVersion;
    }
}
