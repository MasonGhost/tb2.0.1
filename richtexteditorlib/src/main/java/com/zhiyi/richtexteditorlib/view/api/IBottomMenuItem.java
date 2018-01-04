package com.zhiyi.richtexteditorlib.view.api;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import com.zhiyi.richtexteditorlib.view.logiclist.MenuItem;
/**
 * @Author Jliuer
 * @Date 17/11/15 13:27
 * @Email Jliuer@aliyun.com
 * @Description
 */
public interface IBottomMenuItem {
    Long getItemId();
    View getMainView();

    interface OnItemClickListenerParcelable extends Parcelable {
        void onItemClick(MenuItem item);

        @Override
        int describeContents();

        @Override
        void writeToParcel(Parcel dest, int flags);
    }

    interface OnBottomItemClickListener{
        boolean onItemClick(MenuItem item,boolean isSelected);
    }

}
