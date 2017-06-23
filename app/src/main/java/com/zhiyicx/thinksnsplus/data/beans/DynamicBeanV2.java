package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;

import java.util.List;

/**
 * @Author Jliuer
 * @Date 2017/06/23/15:40
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class DynamicBeanV2 extends BaseListBean{

    /**
     * ad : null
     * pinned : null
     * feeds : [{"id":173,"created_at":"2017-06-23 07:33:56","updated_at":"2017-06-23 07:33:56","deleted_at":null,"user_id":3,"feed_title":"","feed_content":null,"feed_from":4,"feed_digg_count":0,"feed_view_count":0,"feed_comment_count":0,"feed_latitude":null,"feed_longtitude":null,"feed_geohash":null,"audit_status":1,"feed_mark":31498203236395,"has_digg":false,"has_collect":false,"comments":[],"images":[{"file":21,"size":"3120x4208","amount":5,"type":"download","paid":false}],"diggs":[]},{"id":172,"created_at":"2017-06-23 07:12:15","updated_at":"2017-06-23 07:12:15","deleted_at":null,"user_id":3,"feed_title":"","feed_content":null,"feed_from":4,"feed_digg_count":0,"feed_view_count":0,"feed_comment_count":0,"feed_latitude":null,"feed_longtitude":null,"feed_geohash":null,"audit_status":1,"feed_mark":31498201934364,"has_digg":false,"has_collect":false,"comments":[],"images":[{"file":20,"size":"1080x1080","amount":0,"type":"download","paid":false}],"diggs":[]},{"id":171,"created_at":"2017-06-23 06:12:49","updated_at":"2017-06-23 06:12:49","deleted_at":null,"user_id":3,"feed_title":"","feed_content":null,"feed_from":4,"feed_digg_count":0,"feed_view_count":0,"feed_comment_count":0,"feed_latitude":null,"feed_longtitude":null,"feed_geohash":null,"audit_status":1,"feed_mark":31498198367854,"has_digg":false,"has_collect":false,"comments":[],"images":[{"file":10,"size":null,"amount":0,"type":"download","paid":false}],"diggs":[]},{"id":170,"created_at":"2017-06-23 02:10:11","updated_at":"2017-06-23 06:46:06","deleted_at":null,"user_id":19,"feed_title":"","feed_content":"我要置顶！！！！！","feed_from":3,"feed_digg_count":0,"feed_view_count":2,"feed_comment_count":0,"feed_latitude":"","feed_longtitude":"","feed_geohash":"","audit_status":1,"feed_mark":191498183812540,"has_digg":false,"has_collect":false,"comments":[],"images":[],"diggs":[]},{"id":168,"created_at":"2017-06-22 10:25:49","updated_at":"2017-06-22 14:03:59","deleted_at":null,"user_id":3,"feed_title":"","feed_content":"看看","feed_from":4,"feed_digg_count":1,"feed_view_count":2,"feed_comment_count":0,"feed_latitude":"","feed_longtitude":"","feed_geohash":"","audit_status":1,"feed_mark":31498127151350,"has_digg":false,"has_collect":false,"comments":[],"images":[],"diggs":[3]},{"id":139,"created_at":"2017-06-20 08:54:42","updated_at":"2017-06-23 01:40:34","deleted_at":null,"user_id":3,"feed_title":null,"feed_content":"","feed_from":4,"feed_digg_count":1,"feed_view_count":9,"feed_comment_count":5,"feed_latitude":"","feed_longtitude":"","feed_geohash":"","audit_status":1,"feed_mark":31497948885028,"has_digg":false,"has_collect":false,"comments":[{"id":76,"created_at":"2017-06-22 06:06:35","updated_at":"2017-06-22 06:06:35","deleted_at":null,"user_id":19,"to_user_id":3,"reply_to_user_id":0,"feed_id":139,"comment_content":"I have a lot","comment_mark":191498111596137},{"id":77,"created_at":"2017-06-22 06:06:41","updated_at":"2017-06-22 06:06:41","deleted_at":null,"user_id":19,"to_user_id":3,"reply_to_user_id":0,"feed_id":139,"comment_content":"I have a lot of","comment_mark":191498111601696}],"images":[],"diggs":[]},{"id":138,"created_at":"2017-06-20 08:52:35","updated_at":"2017-06-20 08:54:12","deleted_at":null,"user_id":3,"feed_title":null,"feed_content":"","feed_from":4,"feed_digg_count":1,"feed_view_count":0,"feed_comment_count":0,"feed_latitude":"","feed_longtitude":"","feed_geohash":"","audit_status":1,"feed_mark":31497948387304,"has_digg":false,"has_collect":false,"comments":[],"images":[],"diggs":[3]},{"id":137,"created_at":"2017-06-19 02:43:44","updated_at":"2017-06-19 02:57:39","deleted_at":null,"user_id":3,"feed_title":null,"feed_content":"","feed_from":4,"feed_digg_count":1,"feed_view_count":0,"feed_comment_count":0,"feed_latitude":"","feed_longtitude":"","feed_geohash":"","audit_status":1,"feed_mark":31497840215793,"has_digg":false,"has_collect":false,"comments":[],"images":[],"diggs":[3]},{"id":136,"created_at":"2017-06-16 08:01:31","updated_at":"2017-06-19 02:57:40","deleted_at":null,"user_id":3,"feed_title":null,"feed_content":"","feed_from":4,"feed_digg_count":1,"feed_view_count":1,"feed_comment_count":0,"feed_latitude":"","feed_longtitude":"","feed_geohash":"","audit_status":1,"feed_mark":31497600084767,"has_digg":false,"has_collect":false,"comments":[],"images":[],"diggs":[3]},{"id":135,"created_at":"2017-06-16 07:52:24","updated_at":"2017-06-22 06:01:34","deleted_at":null,"user_id":3,"feed_title":null,"feed_content":"","feed_from":4,"feed_digg_count":1,"feed_view_count":1,"feed_comment_count":0,"feed_latitude":"","feed_longtitude":"","feed_geohash":"","audit_status":1,"feed_mark":31497599543375,"has_digg":false,"has_collect":false,"comments":[],"images":[],"diggs":[3]},{"id":134,"created_at":"2017-06-16 07:37:13","updated_at":"2017-06-22 02:13:38","deleted_at":null,"user_id":3,"feed_title":null,"feed_content":"","feed_from":4,"feed_digg_count":1,"feed_view_count":1,"feed_comment_count":0,"feed_latitude":"","feed_longtitude":"","feed_geohash":"","audit_status":1,"feed_mark":31497598631709,"has_digg":false,"has_collect":false,"comments":[],"images":[],"diggs":[3]},{"id":133,"created_at":"2017-06-14 06:54:07","updated_at":"2017-06-16 08:34:25","deleted_at":null,"user_id":5,"feed_title":null,"feed_content":"","feed_from":4,"feed_digg_count":0,"feed_view_count":1,"feed_comment_count":0,"feed_latitude":"","feed_longtitude":"","feed_geohash":"","audit_status":1,"feed_mark":51497423247137,"has_digg":false,"has_collect":false,"comments":[],"images":[],"diggs":[]},{"id":132,"created_at":"2017-06-14 06:11:27","updated_at":"2017-06-14 06:11:27","deleted_at":null,"user_id":19,"feed_title":null,"feed_content":"","feed_from":3,"feed_digg_count":0,"feed_view_count":0,"feed_comment_count":0,"feed_latitude":"","feed_longtitude":"","feed_geohash":"","audit_status":1,"feed_mark":191497420660729,"has_digg":false,"has_collect":false,"comments":[],"images":[],"diggs":[]},{"id":131,"created_at":"2017-06-13 09:55:37","updated_at":"2017-06-13 09:55:37","deleted_at":null,"user_id":3,"feed_title":null,"feed_content":"","feed_from":4,"feed_digg_count":0,"feed_view_count":0,"feed_comment_count":0,"feed_latitude":"","feed_longtitude":"","feed_geohash":"","audit_status":0,"feed_mark":31497347731432,"has_digg":false,"has_collect":false,"comments":[],"images":[],"diggs":[]},{"id":130,"created_at":"2017-06-13 01:11:29","updated_at":"2017-06-15 09:43:21","deleted_at":null,"user_id":3,"feed_title":null,"feed_content":"","feed_from":4,"feed_digg_count":2,"feed_view_count":21,"feed_comment_count":0,"feed_latitude":"","feed_longtitude":"","feed_geohash":"","audit_status":1,"feed_mark":31497316287293,"has_digg":false,"has_collect":false,"comments":[],"images":[],"diggs":[19,3]},{"id":129,"created_at":"2017-06-06 09:56:08","updated_at":"2017-06-13 07:00:40","deleted_at":null,"user_id":3,"feed_title":null,"feed_content":"ghgff","feed_from":4,"feed_digg_count":1,"feed_view_count":13,"feed_comment_count":0,"feed_latitude":"","feed_longtitude":"","feed_geohash":"","audit_status":1,"feed_mark":31496742969584,"has_digg":false,"has_collect":false,"comments":[],"images":[],"diggs":[3]},{"id":127,"created_at":"2017-06-06 02:29:36","updated_at":"2017-06-22 02:07:21","deleted_at":null,"user_id":3,"feed_title":null,"feed_content":"","feed_from":4,"feed_digg_count":3,"feed_view_count":22,"feed_comment_count":0,"feed_latitude":"","feed_longtitude":"","feed_geohash":"","audit_status":1,"feed_mark":31496716170903,"has_digg":false,"has_collect":false,"comments":[],"images":[],"diggs":[3,7,5]},{"id":126,"created_at":"2017-06-06 02:19:38","updated_at":"2017-06-22 07:26:46","deleted_at":null,"user_id":3,"feed_title":null,"feed_content":"","feed_from":4,"feed_digg_count":2,"feed_view_count":4,"feed_comment_count":2,"feed_latitude":"","feed_longtitude":"","feed_geohash":"","audit_status":1,"feed_mark":31496715575292,"has_digg":false,"has_collect":false,"comments":[{"id":60,"created_at":"2017-06-12 08:25:47","updated_at":"2017-06-12 08:25:47","deleted_at":null,"user_id":3,"to_user_id":3,"reply_to_user_id":0,"feed_id":126,"comment_content":"fggg","comment_mark":31497255952008}],"images":[],"diggs":[3,5]},{"id":125,"created_at":"2017-06-06 02:19:20","updated_at":"2017-06-21 04:01:54","deleted_at":null,"user_id":3,"feed_title":null,"feed_content":"","feed_from":4,"feed_digg_count":0,"feed_view_count":3,"feed_comment_count":0,"feed_latitude":"","feed_longtitude":"","feed_geohash":"","audit_status":1,"feed_mark":31496715532057,"has_digg":false,"has_collect":false,"comments":[],"images":[],"diggs":[]},{"id":124,"created_at":"2017-06-06 02:18:22","updated_at":"2017-06-06 02:18:22","deleted_at":null,"user_id":3,"feed_title":null,"feed_content":"","feed_from":4,"feed_digg_count":0,"feed_view_count":0,"feed_comment_count":0,"feed_latitude":"","feed_longtitude":"","feed_geohash":"","audit_status":1,"feed_mark":31496715488766,"has_digg":false,"has_collect":false,"comments":[],"images":[],"diggs":[]}]
     */

    private List<DynamicDetailBeanV2> ad;
    private List<DynamicDetailBeanV2> pinned;
    private List<DynamicDetailBeanV2> feeds;

    public List<DynamicDetailBeanV2> getAd() {
        return ad;
    }

    public void setAd(List<DynamicDetailBeanV2> ad) {
        this.ad = ad;
    }

    public List<DynamicDetailBeanV2> getPinned() {
        return pinned;
    }

    public void setPinned(List<DynamicDetailBeanV2> pinned) {
        this.pinned = pinned;
    }

    public List<DynamicDetailBeanV2> getFeeds() {
        return feeds;
    }

    public void setFeeds(List<DynamicDetailBeanV2> feeds) {
        this.feeds = feeds;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedList(this.ad);
        dest.writeTypedList(this.pinned);
        dest.writeTypedList(this.feeds);
    }

    public DynamicBeanV2() {
    }

    protected DynamicBeanV2(Parcel in) {
        super(in);
        this.ad = in.createTypedArrayList(DynamicDetailBeanV2.CREATOR);
        this.pinned = in.createTypedArrayList(DynamicDetailBeanV2.CREATOR);
        this.feeds = in.createTypedArrayList(DynamicDetailBeanV2.CREATOR);
    }

    public static final Creator<DynamicBeanV2> CREATOR = new Creator<DynamicBeanV2>() {
        @Override
        public DynamicBeanV2 createFromParcel(Parcel source) {
            return new DynamicBeanV2(source);
        }

        @Override
        public DynamicBeanV2[] newArray(int size) {
            return new DynamicBeanV2[size];
        }
    };
}
