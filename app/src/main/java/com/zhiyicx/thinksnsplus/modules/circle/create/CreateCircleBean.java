package com.zhiyicx.thinksnsplus.modules.circle.create;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * @author Jliuer
 * @Date 2017/12/06/15:17
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CreateCircleBean {
    private String name;
    private String location;
    private String longitude;
    private String latitude;
    private String geo_hash;
    private String summary;
    private String notice;
    private String mode;
    @Expose(serialize = false)
    private String filePath;
    @Expose(serialize = false)
    private String fileName = "avatar";
    @Expose(serialize = false)
    private String fileType = "multipart/form-data";
    @Expose(serialize = false)
    private long categoryId;
    private int allow_feed;
    private String money;
    private List<TagId> tags;

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public static class TagId {
        private long id;

        public TagId(long id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "{" +
                    "id=" + id +
                    '}';
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getGeo_hash() {
        return geo_hash;
    }

    public void setGeo_hash(String geo_hash) {
        this.geo_hash = geo_hash;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public int getAllow_feed() {
        return allow_feed;
    }

    public void setAllow_feed(int allow_feed) {
        this.allow_feed = allow_feed;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public List<TagId> getTags() {
        return tags;
    }

    public void setTags(List<TagId> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", geo_hash='" + geo_hash + '\'' +
                ", summary='" + summary + '\'' +
                ", notice='" + notice + '\'' +
                ", mode='" + mode + '\'' +
                ", allow_feed=" + allow_feed +
                ", money='" + money + '\'' +
//                ", tags=" + tags +
                '}';
    }
}
