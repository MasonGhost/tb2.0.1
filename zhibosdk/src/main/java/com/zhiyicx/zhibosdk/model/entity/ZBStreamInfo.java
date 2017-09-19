package com.zhiyicx.zhibosdk.model.entity;

/**
 * Created by zhiyicx on 2016/3/23.
 */
public class ZBStreamInfo {
//            "id":创建的stream_id,
//            "createdat":创建时间,//2016-03-21T11:57:54.433+08:00
//            "updatedat":更新时间,//2016-03-21T11:57:54.433+08:00
//            "title":直播间title,
//            "hub":所在应用,
//            "disabledtill":禁播时长,//0 表示未禁播,其他时间戳表示禁播截止时间
//            "disabled":是否禁播,//boolean值:true表示禁播
//            "publishkey":推流key,//c8dac84f-999a-410e-a970-753f752d357d
//            "publishsecurity":鉴权方式,//static | dynamic
//            "icon":直播间封面地址,
//            "location":地理位置信息,
    public String id;
    public String createdat;
    public String updatedat;
    public String title;
    public String hub;
    public String disabledtill;
    public String disabled;
    public String publishkey;
    public String publishsecurity;
    public String icon;
    public String location;
    public String online_count;
    public Hosts hosts;
    public Status status;
    public ZBApiImInfo im;


    public class Hosts {
        public Publish publish;
        public Live live;
        public Playback playback;
    }

    public class Publish {
        public String rtmp;
    }

    public class Live {
        public String rtmp;
        public String hls;
        public String hdl;
    }

    public class Playback {
        public String hls;
    }


    public class Status {
        public String reqid;
        public String hub;
        public String stream;
        public String startfrom;
        public String updatedat;
        public String addr;
        public String status;
        public int bytespersecond;
        public Framespersecond framespersecond;

    }

    public class Framespersecond {
        public int audio;
        public int video;
        public int data;
    }
}
