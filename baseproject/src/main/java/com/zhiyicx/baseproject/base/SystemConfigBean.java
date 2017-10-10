package com.zhiyicx.baseproject.base;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/5/18
 * @Contact master.jungle68@gmail.com
 */

public class SystemConfigBean implements Serializable {

    private static final long serialVersionUID = -2767044631905981596L;
    /**
     * wallet:ratio: 200 // 转换显示余额的比例，百分比。（200 就表示 200%）
     * im_serve : 127.0.0.1:9900
     * im_helper : [{"uid":"1","url":"https://plus.io/users/1"}]
     * "ad": [],
     * "question:apply_amount": 200,  //  申请精选所需支付金额
     * "question:onlookers_amount": 100  //  围观答案所需支付金额
     * "checkin": false
     */
    @SerializedName("wallet:ratio")
    private int wallet_ratio;
    @SerializedName("im:serve")
    private String im_serve;
    @SerializedName("im:helper")
    private ArrayList<ImHelperBean> im_helper;
    @SerializedName("ad")
    private ArrayList<Advert> mAdverts;
    @SerializedName("wallet:recharge-type")
    private String[] mWalletTtype;
    @SerializedName("news:contribute")
    private String[] mNewsContribute;
    @SerializedName("plus-appversion")
    private Appversion mAppversion;
    @SerializedName("question:apply_amount")
    private int excellentQuestion;
    @SerializedName("question:onlookers_amount")
    private int onlookQuestion;
    @SerializedName("news:pay_conyribute")
    private int newsPayContribute;
    @SerializedName("feed")
    private Feed mFeed;
    private boolean checkin;

    /**
     * registerSettings : {"showTerms":false,"registerMode":"all","completeData":true,"accountType":"all","content":"# 服务条款及隐私政策"}
     */

    private RegisterSettingsBean registerSettings;
    /**
     * site : {"status":true,"off_reason":"站点维护中请稍后再访问","app":{"status":true},"h5":{"status":true},"reserved_nickname":"root,admin",
     * "client_email":"admin@123.com","gold":{"status":true},"reward":{"status":true},"user_invite_template":"我发现了一个全平台社交系统ThinkSNS+，快来加入吧：http://t
     * .cn/RpFfbbi","gold_name":{"name":"金币","unit":"枚"}}
     */

    private SiteBean site;

    public String[] getNewsContribute() {
        return mNewsContribute;
    }

    public void setNewsContribute(String[] newsContribute) {
        mNewsContribute = newsContribute;
    }

    public Appversion getAppversion() {
        return mAppversion;
    }

    public void setAppversion(Appversion appversion) {
        mAppversion = appversion;
    }

    public int getNewsPayContribute() {
        return newsPayContribute;
    }

    public void setNewsPayContribute(int newsPayContribute) {
        this.newsPayContribute = newsPayContribute;
    }

    public Feed getFeed() {
        return mFeed;
    }

    public void setFeed(Feed feed) {
        mFeed = feed;
    }

    public int getExcellentQuestion() {
        return excellentQuestion;
    }

    public void setExcellentQuestion(int excellentQuestion) {
        this.excellentQuestion = excellentQuestion;
    }

    public int getOnlookQuestion() {
        return onlookQuestion;
    }

    public void setOnlookQuestion(int onlookQuestion) {
        this.onlookQuestion = onlookQuestion;
    }

    public String[] getWalletTtype() {
        return mWalletTtype;
    }

    public void setWalletTtype(String[] walletTtype) {
        mWalletTtype = walletTtype;
    }

    public int getWallet_ratio() {
        return wallet_ratio;
    }

    public void setWallet_ratio(int wallet_ratio) {
        this.wallet_ratio = wallet_ratio;
    }

    public String getIm_serve() {
        return im_serve;
    }

    public void setIm_serve(String im_serve) {
        this.im_serve = im_serve;
    }

    public ArrayList<ImHelperBean> getIm_helper() {
        return im_helper;
    }

    public void setIm_helper(ArrayList<ImHelperBean> im_helper) {
        this.im_helper = im_helper;
    }

    public ArrayList<Advert> getAdverts() {
        return mAdverts;
    }

    public void setAdverts(ArrayList<Advert> adverts) {
        mAdverts = adverts;
    }

    public boolean isCheckin() {
        return checkin;
    }

    public void setCheckin(boolean checkin) {
        this.checkin = checkin;
    }

    public RegisterSettingsBean getRegisterSettings() {
        return registerSettings;
    }

    public void setRegisterSettings(RegisterSettingsBean registerSettings) {
        this.registerSettings = registerSettings;
    }

    public SiteBean getSite() {
        return site;
    }

    public void setSite(SiteBean site) {
        this.site = site;
    }

    /**
     * uid : 1
     * url : https://plus.io/users/1
     */

    public static class ImHelperBean implements Serializable {
        private static final long serialVersionUID = 2932201693891980990L;
        private String uid;
        private String url;
        private boolean isDelete;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isDelete() {
            return isDelete;
        }

        public void setDelete(boolean delete) {
            isDelete = delete;
        }

        @Override
        public String toString() {
            return "ImHelperBean{" +
                    "uid='" + uid + '\'' +
                    ", url='" + url + '\'' +
                    ", isDelete=" + isDelete +
                    '}';
        }
    }

    /**
     * {
     * "id":1,
     * "title":"广告1",
     * "type":"image",
     * "data":{
     * "image":"https://avatars0.githubusercontent.com/u/5564821?v=3&s=460",
     * "link":"https://github.com/zhiyicx/thinksns-plus"
     * }
     */
    public static class Advert implements Serializable {
        private static final long serialVersionUID = -261781358771084800L;
        private int id;
        private String title;
        private String type;
        private Object data;
        private ImageAdvert mImageAdvert;

        public ImageAdvert getImageAdvert() {
            return mImageAdvert;
        }

        public void setImageAdvert(ImageAdvert imageAdvert) {
            mImageAdvert = imageAdvert;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }


    public static class RegisterSettingsBean implements Serializable {
        private static final long serialVersionUID = 9161708076383134140L;
        /**
         * showTerms : false
         * registerMode : all
         * completeData : true
         * accountType : all
         * content : # 服务条款及隐私政策
         */

        private boolean showTerms;
        private boolean open;
        private String registerMode;
        private String method;// 注册类型
        private String type;// 注册方式
        private String fixed;
        private boolean completeData;
        private String accountType;
        private String content;

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getFixed() {
            return fixed;
        }

        public void setFixed(String fixed) {
            this.fixed = fixed;
        }

        public boolean hasOpen() {
            return open;
        }

        public void setOpen(boolean open) {
            this.open = open;
        }

        public boolean isShowTerms() {
            return showTerms;
        }

        public void setShowTerms(boolean showTerms) {
            this.showTerms = showTerms;
        }

        public String getRegisterMode() {
            return registerMode;
        }

        public void setRegisterMode(String registerMode) {
            this.registerMode = registerMode;
        }

        public boolean isCompleteData() {
            return completeData;
        }

        public void setCompleteData(boolean completeData) {
            this.completeData = completeData;
        }

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    public static class SiteBean implements Serializable {
        private static final long serialVersionUID = -5372367518165433839L;
        /**
         * status : true
         * off_reason : 站点维护中请稍后再访问
         * app : {"status":true}
         * h5 : {"status":true}
         * reserved_nickname : root,admin
         * client_email : admin@123.com
         * gold : {"status":true}
         * reward : {"status":true}
         * user_invite_template : 我发现了一个全平台社交系统ThinkSNS+，快来加入吧：http://t.cn/RpFfbbi
         * gold_name : {"name":"金币","unit":"枚"}
         */

        private boolean status;
        private String off_reason;
        private AppBean app;
        private H5Bean h5;
        private String reserved_nickname;
        private String client_email;
        private GoldBean gold;
        private RewardBean reward;
        private String user_invite_template;
        private GoldNameBean gold_name;

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public String getOff_reason() {
            return off_reason;
        }

        public void setOff_reason(String off_reason) {
            this.off_reason = off_reason;
        }

        public AppBean getApp() {
            return app;
        }

        public void setApp(AppBean app) {
            this.app = app;
        }

        public H5Bean getH5() {
            return h5;
        }

        public void setH5(H5Bean h5) {
            this.h5 = h5;
        }

        public String getReserved_nickname() {
            return reserved_nickname;
        }

        public void setReserved_nickname(String reserved_nickname) {
            this.reserved_nickname = reserved_nickname;
        }

        public String getClient_email() {
            return client_email;
        }

        public void setClient_email(String client_email) {
            this.client_email = client_email;
        }

        public GoldBean getGold() {
            return gold;
        }

        public void setGold(GoldBean gold) {
            this.gold = gold;
        }

        public RewardBean getReward() {
            return reward;
        }

        public void setReward(RewardBean reward) {
            this.reward = reward;
        }

        public String getUser_invite_template() {
            return user_invite_template;
        }

        public void setUser_invite_template(String user_invite_template) {
            this.user_invite_template = user_invite_template;
        }

        public GoldNameBean getGold_name() {
            return gold_name;
        }

        public void setGold_name(GoldNameBean gold_name) {
            this.gold_name = gold_name;
        }

        public static class AppBean implements Serializable {
            private static final long serialVersionUID = -2995455975855810530L;
            /**
             * status : true
             */

            private boolean status;

            public boolean isStatus() {
                return status;
            }

            public void setStatus(boolean status) {
                this.status = status;
            }

            @Override
            public String toString() {
                return "AppBean{" +
                        "status=" + status +
                        '}';
            }
        }

        public static class H5Bean implements Serializable {
            private static final long serialVersionUID = -4543706356197674563L;
            /**
             * status : true
             */

            private boolean status;

            public boolean isStatus() {
                return status;
            }

            public void setStatus(boolean status) {
                this.status = status;
            }

            @Override
            public String toString() {
                return "H5Bean{" +
                        "status=" + status +
                        '}';
            }
        }

        public static class GoldBean implements Serializable {
            private static final long serialVersionUID = 175809906769541494L;
            /**
             * status : true
             */

            private boolean status;

            public boolean isStatus() {
                return status;
            }

            public void setStatus(boolean status) {
                this.status = status;
            }

            @Override
            public String toString() {
                return "GoldBean{" +
                        "status=" + status +
                        '}';
            }
        }

        public static class RewardBean implements Serializable {
            private static final long serialVersionUID = 8123957693947760523L;
            /**
             * status : true
             */

            private boolean open;

            private String amounts;

            public boolean hasOpen() {
                return open;
            }

            public void setOpen(boolean open) {
                this.open = open;
            }

            public String getAmounts() {
                return amounts;
            }

            public void setAmounts(String amounts) {
                this.amounts = amounts;
            }

            @Override
            public String toString() {
                return "RewardBean{" +
                        "open=" + open +
                        ", amounts='" + amounts + '\'' +
                        '}';
            }
        }

        public static class GoldNameBean implements Serializable {
            private static final long serialVersionUID = 1993148057604147543L;
            /**
             * name : 金币
             * unit : 枚
             */

            private String name;
            private String unit;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUnit() {
                return unit;
            }

            public void setUnit(String unit) {
                this.unit = unit;
            }

            @Override
            public String toString() {
                return super.toString();
            }
        }

        @Override
        public String toString() {
            return "SiteBean{" +
                    "status=" + status +
                    ", off_reason='" + off_reason + '\'' +
                    ", app=" + app +
                    ", h5=" + h5 +
                    ", reserved_nickname='" + reserved_nickname + '\'' +
                    ", client_email='" + client_email + '\'' +
                    ", gold=" + gold +
                    ", reward=" + reward +
                    ", user_invite_template='" + user_invite_template + '\'' +
                    ", gold_name=" + gold_name +
                    '}';
        }
    }

    public static class Appversion implements Serializable{
        private static final long serialVersionUID = 8919908013556136434L;
        private boolean open;

        public boolean hasOpen() {
            return open;
        }

        public void setOpen(boolean open) {
            this.open = open;
        }
    }

    public static class Feed implements Serializable{

        private static final long serialVersionUID = 2393545893640479534L;
        private boolean reward;
        private boolean paycontrol;

        public boolean hasReward() {
            return reward;
        }

        public void setReward(boolean reward) {
            this.reward = reward;
        }

        public boolean hasPaycontrol() {
            return paycontrol;
        }

        public void setPaycontrol(boolean paycontrol) {
            this.paycontrol = paycontrol;
        }

        @Override
        public String toString() {
            return "Feed{" +
                    "reward=" + reward +
                    ", paycontrol=" + paycontrol +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "SystemConfigBean{" +
                "wallet_ratio=" + wallet_ratio +
                ", im_serve='" + im_serve + '\'' +
                ", im_helper=" + im_helper +
                ", mAdverts=" + mAdverts +
                ", mWalletTtype=" + Arrays.toString(mWalletTtype) +
                ", excellentQuestion=" + excellentQuestion +
                ", onlookQuestion=" + onlookQuestion +
                ", checkin=" + checkin +
                ", registerSettings=" + registerSettings +
                ", site=" + site +
                '}';
    }
}
