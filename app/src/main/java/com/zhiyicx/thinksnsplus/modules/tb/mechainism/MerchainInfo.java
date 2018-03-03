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

    private int photo;
    private String nickname;
    private String remarks;
    private String introduce;
    private String other_info;
    private int white_paper;
    private String created_at;
    private String updated_at;

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
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
}
