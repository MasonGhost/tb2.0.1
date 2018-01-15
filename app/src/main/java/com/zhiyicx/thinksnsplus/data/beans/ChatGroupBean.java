package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;

/**
 * @author Catherine
 * @describe 群组会话的bean
 * @date 2018/1/15
 * @contact email:648129313@qq.com
 */

public class ChatGroupBean extends BaseListBean{

    private String im_group_id;

    public String getIm_group_id() {
        return im_group_id;
    }

    public void setIm_group_id(String im_group_id) {
        this.im_group_id = im_group_id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.im_group_id);
    }

    public ChatGroupBean() {
    }

    protected ChatGroupBean(Parcel in) {
        super(in);
        this.im_group_id = in.readString();
    }

    public static final Creator<ChatGroupBean> CREATOR = new Creator<ChatGroupBean>() {
        @Override
        public ChatGroupBean createFromParcel(Parcel source) {
            return new ChatGroupBean(source);
        }

        @Override
        public ChatGroupBean[] newArray(int size) {
            return new ChatGroupBean[size];
        }
    };
}
