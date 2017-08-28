package com.zhiyicx.thinksnsplus.modules.findsomeone.contacts;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.vladsch.flexmark.ast.Text;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleBorderTransform;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleTransform;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.baseproject.widget.recycleview.stickygridheaders.StickyHeaderGridAdapter;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.ContactsBean;
import com.zhiyicx.thinksnsplus.data.beans.ContactsContainerBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.follow_fans.FollowFansListContract;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/8/14
 * @Contact master.jungle68@gmail.com
 */

public class ContactsAdapter extends StickyHeaderGridAdapter {
    public static final int DEFAULT_MAX_ADD_SHOW_NUMS = 5;

    private List<ContactsContainerBean> mDatas;

    public void setOnMoreClickLitener(OnMoreClickLitener onMoreClickLitener) {
        mOnMoreClickLitener = onMoreClickLitener;
    }

    private OnMoreClickLitener mOnMoreClickLitener;
    private ContactsContract.Presenter mPresenter;


    ContactsAdapter(List<ContactsContainerBean> categoryBeanList, ContactsContract.Presenter presenter) {
        this.mPresenter = presenter;

        mDatas = categoryBeanList;

    }

    private boolean isNeedHeader(){
        return  mDatas.size() != 1;
    }
    @Override
    public int getSectionCount() {
        return mDatas.size();
    }

    @Override
    public int getSectionItemCount(int section) {
        return mDatas.get(section).getContacts().size();
    }

    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int headerType) {
        if (isNeedHeader()) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_contacts, parent, false);
            return new MyHeaderViewHolder(view);
        } else {
            return new MyHeaderViewHolder(new View(parent.getContext()));
        }

    }

    @Override
    public ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int itemType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contacts_unadd, parent, false);
        return new MyItemViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(HeaderViewHolder viewHolder, int categoryPosition) {
        final MyHeaderViewHolder holder = (MyHeaderViewHolder) viewHolder;
        if (isNeedHeader()) {
            holder.mContainer.setVisibility(View.VISIBLE);
            holder.mTvTitle.setText(mDatas.get(categoryPosition).getTitle());
            if (mDatas.get(categoryPosition).getContacts().size() >= DEFAULT_MAX_ADD_SHOW_NUMS) {
                holder.mTvMore.setVisibility(View.VISIBLE);

                RxView.clicks(holder.mTvMore)
                        .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(aVoid -> {
                            if (mOnMoreClickLitener != null) {
                                mOnMoreClickLitener.onMoreClick(categoryPosition);
                            }
                        });

            } else {
                holder.mTvMore.setVisibility(View.INVISIBLE);
            }
        } else {

        }

    }

    @Override
    public void onBindItemViewHolder(ItemViewHolder viewHolder, final int categoryPosition, final int tagPosition) {
        final MyItemViewHolder holder = (MyItemViewHolder) viewHolder;
        ContactsBean userTagBean = mDatas.get(categoryPosition).getContacts().get(tagPosition);
        holder.mTvName.setText(userTagBean.getContact().getDisplayName());

        if (userTagBean.getUser() != null) { // TS+
            holder.mTvTsName.setVisibility(View.VISIBLE);
            holder.mTvTsName.setText(holder.mTvTsName.getResources().getString(R.string.user_name) + ": " + userTagBean.getUser().getName());
            ImageUtils.loadCircleUserHeadPic(userTagBean.getUser(), holder.mUserAvatarView);
            holder.mTvInvite.setVisibility(View.GONE);
            holder.mIvFollow.setVisibility(View.VISIBLE);
            if (userTagBean.getUser().isFollowing() && userTagBean.getUser().isFollower()) {
                holder.mIvFollow.setImageResource(R.mipmap.ico_me_followed_eachother);
            } else if (userTagBean.getUser().isFollower()) {
                holder.mIvFollow.setImageResource(R.mipmap.ico_me_followed);
            } else {
                holder.mIvFollow.setImageResource(R.mipmap.ico_me_follow);
            }

            RxView.clicks(holder.mIvFollow)
                    .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Void>() {
                        @Override
                        public void call(Void aVoid) {
                            // 添加关注，或者取消关注
                            // 关注列表的逻辑操作：关注，互相关注 ---》未关注
                            // 粉丝列表的逻辑操作：互相关注 ---》未关注

                            if (userTagBean.getUser().isFollowing() && userTagBean.getUser().isFollower()) {
                                mPresenter.cancleFollowUser(tagPosition, userTagBean.getUser());
                            } else if (userTagBean.getUser().isFollower()) {
                                mPresenter.cancleFollowUser(tagPosition, userTagBean.getUser());
                            } else {
                                mPresenter.followUser(tagPosition, userTagBean.getUser());
                            }
                            notifySectionDataSetChanged(categoryPosition);
                        }
                    });


            /**
             * 如果关注粉丝列表中出现了自己，需要隐藏关注按钮
             */
            holder.mIvFollow.setVisibility(
                    userTagBean.getUser().getUser_id() == AppApplication.getmCurrentLoginAuth().getUser_id() ? View.GONE : View.VISIBLE);


        } else {
            holder.mTvTsName.setVisibility(View.GONE);
            holder.mIvFollow.setVisibility(View.GONE);
            holder.mTvInvite.setVisibility(View.VISIBLE);
            Glide.with(holder.mIvFollow.getContext())
                    .load(userTagBean.getContact().getPhotoUri())
                    .transform(false ?
                            new GlideCircleBorderTransform(holder.mIvFollow.getContext().getApplicationContext(), holder.mIvFollow.getResources().getDimensionPixelSize(R.dimen.spacing_tiny), ContextCompat.getColor(holder.mIvFollow.getContext(), R.color.white))
                            : new GlideCircleTransform(holder.mIvFollow.getContext().getApplicationContext()))
                    .error(R.mipmap.pic_default_portrait1)
                    .placeholder(R.mipmap.pic_default_portrait1)
                    .into(holder.mUserAvatarView.getIvAvatar());
            //        // 跳过
            RxView.clicks(holder.mTvInvite)
                    .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                    .subscribe(aVoid -> {
                        DeviceUtils.openSMS(holder.mTvInvite.getContext(), holder.mTvInvite.getResources().getString(R.string.find_someone), userTagBean.getPhone());
                    });

        }
        // 添加点击事件
        RxView.clicks(holder.mContainer)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(aVoid -> {
                    if (userTagBean.getUser() != null) {
                        toUserCenter(holder.mContainer.getContext(), userTagBean.getUser());
                    }
                });

    }

    /**
     * 前往用户个人中心
     */
    private void toUserCenter(Context context, UserInfoBean userInfoBean) {
        PersonalCenterFragment.startToPersonalCenter(context, userInfoBean);
    }

    public static class MyHeaderViewHolder extends HeaderViewHolder {
        View mContainer;
        TextView mTvTitle;
        TextView mTvMore;

        MyHeaderViewHolder(View itemView) {
            super(itemView);
            mContainer = itemView.findViewById(R.id.ll_container);
            mTvTitle = (TextView) itemView.findViewById(R.id.label);
            mTvMore = (TextView) itemView.findViewById(R.id.tv_more);
        }
    }

    public static class MyItemViewHolder extends ItemViewHolder {
        View mContainer;
        UserAvatarView mUserAvatarView;
        TextView mTvName;
        TextView mTvTsName;
        TextView mTvInvite;
        ImageView mIvFollow;

        MyItemViewHolder(View itemView) {
            super(itemView);
            mContainer = itemView.findViewById(R.id.ll_container);
            mUserAvatarView = (UserAvatarView) itemView.findViewById(R.id.iv_headpic);
            mTvName = (TextView) itemView.findViewById(R.id.tv_name);
            mTvTsName = (TextView) itemView.findViewById(R.id.tv_user_signature);
            mTvInvite = (TextView) itemView.findViewById(R.id.tv_invite);
            mIvFollow = (ImageView) itemView.findViewById(R.id.iv_user_follow);

        }
    }

    public interface OnMoreClickLitener {

        void onMoreClick(int categoryPosition);
    }
}
