package com.zhiyicx.thinksnsplus.modules.tb.mechainism;

/**
 * @author Jungle68
 * @describe
 * @date 2018/3/3
 * @contact master.jungle68@gmail.com
 */
public class MerchainInfo {


    /**
     * photo : 12
     * nickname : vincent
     * remarks : makes
     * introduce : 这个是简介
     * other_info : ['aa':123]
     * white_paper : 13
     * created_at : 2018-03-01 05:49:52
     * updated_at : 2018-03-01 05:50:15
     */

    private String photo;
    private String nickname;
    private String remarks;
    private String introduce;
    private String other_info;
    private int white_paper;
    private String white_paper_name;
    private String created_at;
    private String updated_at;
    private String url; // 官网
    private String android_download_url;

    public String getWhite_paper_name() {
        return white_paper_name;
    }

    public void setWhite_paper_name(String white_paper_name) {
        this.white_paper_name = white_paper_name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getOther_info() {
        return other_info;
    }

    public void setOther_info(String other_info) {
        this.other_info = other_info;
    }

    public int getWhite_paper() {
        return white_paper;
    }

    public void setWhite_paper(int white_paper) {
        this.white_paper = white_paper;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAndroid_download_url() {
        return android_download_url;
    }

    public void setAndroid_download_url(String android_download_url) {
        this.android_download_url = android_download_url;
    }
}
