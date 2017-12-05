package com.zhiyicx.zhibolibrary.model.entity;

import com.google.gson.Gson;
import com.zhiyicx.baseproject.base.SystemConfigBean;
import com.zhiyicx.baseproject.config.PayConfig;
import com.zhiyicx.baseproject.config.SystemConfig;
import com.zhiyicx.common.utils.SharePreferenceUtils;
import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.app.ZhiboApplication;
import com.zhiyicx.zhibolibrary.util.UiUtils;

import java.io.Serializable;

import static com.zhiyicx.zhibolibrary.model.api.ZBLApi.STR_SHARE_ME;
import static com.zhiyicx.zhibolibrary.model.api.ZBLApi.STR_SHARE_NAME;
import static com.zhiyicx.zhibolibrary.model.api.ZBLApi.STR_SHARE_USID;

/**
 * @author zhiyicx
 * @date 2016/3/15
 */
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 636871008;
    private static final String SHAREPREFERENCE_TAG_SYSTEM_BOOTSTRAPPERS = "system_bootstrappers"; // 和 app 下的SharePreferenceTagConfig
    // .SHAREPREFERENCE_TAG_SYSTEM_BOOTSTRAPPERS 一样
    public String password;
    public String uid;
    public String usid;
    public String uname;
    public String phone;
    public Integer sex;
    public String intro;
    public String first_letter;
    public String location;
    public String last_login_time;
    public Integer phone_status;
    public String reg_time;
    public int is_verified;
    private int gold;
    public int follow_count;
    public int fans_count;
    public int zan_count;
    public int zan_remain;
    public int is_follow;
    public String auth_accesskey;
    public String auth_secretkey;
    public Icon cover;
    public Icon avatar;
    public Integer live_time;
    public ImInfo im;
    public String ticket;//智播sdk票据

    @Override
    public String toString() {
        return "UserInfo{" +
                "password='" + password + '\'' +
                ", uid='" + uid + '\'' +
                ", usid='" + usid + '\'' +
                ", uname='" + uname + '\'' +
                ", phone='" + phone + '\'' +
                ", sex=" + sex +
                ", intro='" + intro + '\'' +
                ", first_letter='" + first_letter + '\'' +
                ", location='" + location + '\'' +
                ", last_login_time='" + last_login_time + '\'' +
                ", phone_status=" + phone_status +
                ", reg_time='" + reg_time + '\'' +
                ", is_verified=" + is_verified +
                ", gold=" + getGold() +
                ", follow_count=" + follow_count +
                ", fans_count=" + fans_count +
                ", zan_remain=" + zan_remain +
                ", is_follow=" + is_follow +
                ", auth_accesskey='" + auth_accesskey + '\'' +
                ", auth_secretkey='" + auth_secretkey + '\'' +
                ", cover=" + cover +
                ", avatar=" + avatar +
                ", live_time=" + live_time +
                ", im=" + im +
                ", ticket='" + ticket + '\'' +
                '}';
    }

    public UserInfo(String uid, String uname, int is_verified, String location, Icon avatar) {
        this.uid = uid;
        this.uname = uname;
        this.location = location;
        this.avatar = avatar;
        this.is_verified = is_verified;
    }

    public UserInfo(String phone) {
        this.phone = phone;
    }

    public UserInfo(Icon avatar) {
        this.avatar = avatar;
    }

    public UserInfo() {
    }

    public UserInfo(String uid, String uname) {
        this.uid = uid;
        this.uname = uname;
    }

    /**
     * 通过用户信息获取分享数据
     *
     * @param userInfo
     * @return
     */
    public static com.zhiyicx.common.thridmanager.share.ShareContent getShareContentByUserInfo(UserInfo userInfo) throws NullPointerException {

        com.zhiyicx.common.thridmanager.share.ShareContent shareContent = new com.zhiyicx.common.thridmanager.share.ShareContent();
        if (userInfo.uname != null) {
            shareContent.setTitle(ZhiboApplication.getShareContent().getTitle());
            shareContent.setTitle(shareContent.getTitle().replace(STR_SHARE_NAME, userInfo.uname));
        }
        if (userInfo.usid != null) {
            shareContent.setUrl(ZhiboApplication.getShareContent().getUrl());
            shareContent.setUrl(shareContent.getUrl().replace(STR_SHARE_USID, userInfo.usid));
        }
        if (userInfo.usid.equals(ZhiboApplication.getUserInfo().usid)) {
            shareContent.setContent(STR_SHARE_ME + ZhiboApplication.getShareContent().getContent());
        } else {
            shareContent.setContent(userInfo.uname + ZhiboApplication.getShareContent().getContent());
        }
        shareContent.setImage(userInfo.avatar.origin);

        return shareContent;
    }

    /**
     * 获取配置信息
     *
     * @return
     */
    public SystemConfigBean getAppConfigInfoFromLocal() {
        SystemConfigBean systemConfigBean = null;
        try {
            systemConfigBean = SharePreferenceUtils.getObject(UiUtils.getContext(),
                    SHAREPREFERENCE_TAG_SYSTEM_BOOTSTRAPPERS);
        } catch (Exception ignored) {
        }
        if (systemConfigBean == null) { // 读取本地默认配置
            systemConfigBean = new Gson().fromJson(SystemConfig.DEFAULT_SYSTEM_CONFIG, SystemConfigBean.class);
        }
        return systemConfigBean;
    }


    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    /**
     * 显示金币信息带单位
     *
     * @return
     */
    public String getDisPlayGoldWithuUnit() {
        try {
            return UiUtils.getResources().getString(R.string.money_format_with_unit, PayConfig.realCurrency2GameCurrency(gold,
                    getAppConfigInfoFromLocal()
                            .getWallet_ratio())
                    , UiUtils.getString(R.string.str_gold));
        } catch (Exception e) {
            e.printStackTrace();
            return "0.00" + UiUtils.getString(R.string.str_gold);
        }

    }

    /**
     * 显示金币信息，不带单位
     *
     * @return
     */
    public String getDisPlayGold() {
///  本地处理比例，现在是服务器处理，故不转换
//        try {
//            return UiUtils.getResources().getString(R.string.money_format, PayConfig.realCurrency2GameCurrency(gold, getAppConfigInfoFromLocal()
//                    .getWallet_ratio()));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "0.00";
//        }
        return UiUtils.getResources().getString(R.string.money_format, (float) gold);
    }

}
