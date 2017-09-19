package com.zhiyicx.zhibolibrary.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.model.entity.UserMessage;
import com.zhiyicx.zhibolibrary.ui.Transformation.GlideCircleTrasform;
import com.zhiyicx.zhibolibrary.util.ColorPhrase;
import com.zhiyicx.zhibolibrary.util.UiUtils;

import org.simple.eventbus.EventBus;

/**
 * Created by zhiyicx on 2016/4/6.
 */
public class LiveChatFollowListHolder extends ZBLBaseHolder<UserMessage> {
    public static final String EVENT_HEADPIC_CLICK = "event_headpic_click";
    ImageView mIconIV;
    TextView mContentTV;

    ImageView iVerifiedTV;
    View rlContainer;

    public LiveChatFollowListHolder(View itemView) {
        super(itemView);
        mIconIV = (ImageView) itemView.findViewById(R.id.iv_live_chat_icon);
        mContentTV = (TextView) itemView.findViewById(R.id.tv_live_chat_content);
        iVerifiedTV = (ImageView) itemView.findViewById(R.id.iv_ietm_verified);
        rlContainer = itemView.findViewById(R.id.rl_container);
    }

    @Override
    public void setData(UserMessage data) {

        UiUtils.glideDisplayWithTrasform(data.mUserInfo.avatar.origin, mIconIV, new GlideCircleTrasform(UiUtils.getContext()));

        String content = "<" + data.mUserInfo.uname + ">" + "  " + data.msg.txt;


        try {
            CharSequence chars = ColorPhrase.from(content).withSeparator("<>").innerColor(0xff64d7fe).outerColor(0xfffebf17).format();
            mContentTV.setText(chars);
        } catch (Exception e) {
            e.printStackTrace();
            mContentTV.setText(data.mUserInfo.uname + "  " + data.msg.txt);
        }

        iVerifiedTV.setVisibility(data.mUserInfo.is_verified == 1 ? View.VISIBLE : View.GONE);
        rlContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(getPosition(), EVENT_HEADPIC_CLICK);
            }
        });


    }


}
