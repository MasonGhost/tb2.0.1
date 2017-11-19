package com.zhiyi.richtexteditorlib.view.menuitem;

import android.content.Context;
import android.graphics.Color;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.widget.TextView;

import com.zhiyi.richtexteditorlib.view.logiclist.MenuItem;
/**
 * @Author Jliuer
 * @Date 17/11/15 15:58
 * @Email Jliuer@aliyun.com
 * @Description
 */
@SuppressWarnings("WeakerAccess")
public class TextViewItem extends AbstractBottomMenuItem<TextView> {
    private String text;

    public TextViewItem(Context context,MenuItem menuItem, String text) {
        super(context,menuItem);
        this.text = text;
    }

    @NonNull
    @Override
    public TextView createView() {
        TextView textView =  new TextView(getContext());
        textView.setText(text);

        //textView.setClickable(true);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    @Override
    public void settingAfterCreate(boolean isSelected, TextView view) {
        onSelectChange(isSelected);
    }

    @Override
    public void onSelectChange(boolean isSelected) {
        TextView textView = (TextView) getMainView();
        if(textView != null) {
            if (isSelected) {
                textView.setBackgroundColor(Color.YELLOW);
            } else {
                textView.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.text);
    }

    protected TextViewItem(Parcel in) {
        super(in);
        this.text = in.readString();
    }

    public static final Creator<TextViewItem> CREATOR = new Creator<TextViewItem>() {
        @Override
        public TextViewItem createFromParcel(Parcel source) {
            return new TextViewItem(source);
        }

        @Override
        public TextViewItem[] newArray(int size) {
            return new TextViewItem[size];
        }
    };
}
