package com.zhiyicx.zhibolibrary.ui.holder;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.model.entity.SearchResult;
import com.zhiyicx.zhibolibrary.ui.Transformation.GlideCircleBoundTrasform;
import com.zhiyicx.zhibolibrary.util.LogUtils;
import com.zhiyicx.zhibolibrary.util.UiUtils;

/**
 * Created by zhiyicx on 2016/3/31.
 */
public class LiveListHolder extends ZBLBaseHolder<SearchResult> {
    TextView mName;
    TextView mUserCount;
    TextView mTitle;
    TextView mLocation;
    ImageView mCover;
    ImageView mIcon;
    ImageView mVerified;
    ImageView mLocationIV;
    ImageView mBlackCoverIV;

    private SearchResult mData;

    public LiveListHolder(View itemView) {
        super(itemView);
        mName = (TextView) itemView.findViewById(R.id.tv_live_item_user_name);
        mUserCount = (TextView) itemView.findViewById(R.id.tv_live_item_user_count);
        mTitle = (TextView) itemView.findViewById(R.id.tv_live_item_title);
        mLocation = (TextView) itemView.findViewById(R.id.tv_live_item_location);
        mCover = (ImageView) itemView.findViewById(R.id.iv_live_item_cover);
        mIcon = (ImageView) itemView.findViewById(R.id.iv_live_item_user_icon);
        mVerified = (ImageView) itemView.findViewById(R.id.iv_live_item_verified);
        mLocationIV = (ImageView) itemView.findViewById(R.id.iv_live_item_location);
        mBlackCoverIV = (ImageView) itemView.findViewById(R.id.iv_live_item_black_cover);


    }

    @Override
    public void setData(SearchResult data) {
        this.mData = data;
        mBlackCoverIV.setVisibility(View.GONE);//默认不显示黑色遮罩
        mName.setText(data.user.uname);
        if (data.user != null && data.user.avatar != null)
            UiUtils.glideDisplayWithTrasform(data.user.avatar.getOrigin(), mIcon, new GlideCircleBoundTrasform(UiUtils.getContext()));
        mVerified.setVisibility(data.user.is_verified == 1 ? View.VISIBLE : View.GONE);

        if (data.stream == null) {

            mTitle.setText("主播离线");
            mBlackCoverIV.setVisibility(View.VISIBLE);
            mCover.setImageResource(R.mipmap.pic_photo_340);
            return;

        }

        mTitle.setText(data.stream.title);
        mUserCount.setText(data.stream.online_count + "");
        mLocationIV.setVisibility(TextUtils.isEmpty(data.stream.location) ? View.GONE : View.VISIBLE);
        mLocation.setText(data.stream.location);
        if (data.stream.icon.getOrigin() == null) showBlackCover();//如果地址位空显示黑色遮罩
        mUserCount.setText(data.stream.online_count + "");
        UiUtils.glideWrap(data.stream.icon.getOrigin())
                .placeholder(R.mipmap.pic_photo_340)
                .listener(new RequestListener() {
                    @Override
                    public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {
                        LogUtils.warnInfo(TAG, "display.......");
                        showBlackCover();//如果图片加载成功则显示黑色遮罩
                        return false;
                    }
                })
                .into(mCover);

//        List<Integer> ids = new ArrayList<>();
//        ids.add(data.im.cid);
//        ZBPlayClient.getInstance().mc(ids);
//        ZBPlayClient.getInstance().setOnImListener(new OnImListener() {
//            @Override
//            public void onBanned(long gag) {
//
//            }
//
//            @Override
//            public void onMessageReceived(Message message) {
//
//            }
//
//            @Override
//            public void onMessageACK(Message message) {
//
//            }
//
//            @Override
//            public void onMcReceived(ChatRoomContainer chatRoomContainer) {
//                if (chatRoomContainer.mChatRooms.size() == 0 || chatRoomContainer.mChatRooms.get(0).cid != mData.im.cid)
//                    return;//丢去不是当前房间的消息
//                if (chatRoomContainer.err == SocketService.SUCCESS_CODE) {//没有发生错误
//                    mUserCount.setText(Math.max(chatRoomContainer.mChatRooms.get(0).mc - 1, 0) + "");
//                }
//                else {
//                    LogUtils.debugInfo(TAG, chatRoomContainer.err + "");
//                }
//            }
//
//            @Override
//            public void onConvrEnd(Conver conver) {
//
//            }
//        });

    }

    private void showBlackCover() {
        mBlackCoverIV.setVisibility(View.VISIBLE);
    }


}
