package com.zhiyicx.zhibolibrary.ui.components.sweetsheet.sweetpick;

import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.ui.components.sweetsheet.adapter.ViewpagerAdapter;
import com.zhiyicx.zhibolibrary.ui.components.sweetsheet.entity.MenuEntity;
import com.zhiyicx.zhibolibrary.ui.components.sweetsheet.viewhandler.MenuListViewHandler;
import com.zhiyicx.zhibolibrary.ui.components.sweetsheet.widget.FreeGrowUpParentRelativeLayout;
import com.zhiyicx.zhibolibrary.ui.components.sweetsheet.widget.IndicatorView;
import com.zhiyicx.zhibolibrary.ui.components.sweetsheet.widget.SweetView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zzz40500
 * @version 1.0
 * @date 2015/8/5.
 * @github: https://github.com/zzz40500
 *
 */
public class ViewPagerDelegate extends Delegate {

    private ArrayList<MenuListViewHandler> mMenuListViewHandlers;
    private IndicatorView mIndicatorView;
    private ViewPager mViewPager;
    private View bt_send;

    public ViewPager getViewPager() {
        return mViewPager;
    }

    private SweetView mSweetView;
    private int  last_mMenuListViewHandler_position;
    private MenuListViewHandler mMenuListViewHandler;
    private FreeGrowUpParentRelativeLayout mFreeGrowUpParentRelativeLayout;
    private SweetSheet.OnMenuItemClickListener mOnMenuItemClickListener;
    private List<MenuEntity> mMenuEntities;

    private int mNumColumns=3;
    private int mContentViewHeight;

    public void setTvGlodNumText(String tvGlodNum) {
        mTvGlodNum.setText("x "+tvGlodNum);
    }

    private TextView mTvGlodNum;

    public void setOnBuyGoldClickListener(OnBuyGoldClickListener onBuyGoldClickListener) {
        mOnBuyGoldClickListener = onBuyGoldClickListener;
    }

    public void setOnSendGiftClickListener(OnSendGiftClickListener onSendGiftClickListener) {
        mOnSendGiftClickListener = onSendGiftClickListener;
    }

    private OnBuyGoldClickListener mOnBuyGoldClickListener;
    private OnSendGiftClickListener mOnSendGiftClickListener;
    public ViewPagerDelegate() {
    }
    public ViewPagerDelegate(int numColumns ) {
        mNumColumns=numColumns;
    }
    public ViewPagerDelegate(int numColumns  ,int contentViewHeight) {
        mNumColumns=numColumns;
        mContentViewHeight=contentViewHeight;
    }

    @Override
    protected View createView() {
        View rootView = LayoutInflater.from(mParentVG.getContext()).inflate(R.layout.zb_layout_vp_sweet, null, false);
        mSweetView = (SweetView) rootView.findViewById(R.id.sv);
        mFreeGrowUpParentRelativeLayout = (FreeGrowUpParentRelativeLayout) rootView.findViewById(R.id.freeGrowUpParentF);

        mIndicatorView = (IndicatorView) rootView.findViewById(R.id.indicatorView);
        mIndicatorView.alphaDismiss(false);
        mSweetView.setAnimationListener(new AnimationImp());
        mViewPager = (ViewPager) rootView.findViewById(R.id.vp);

        if(mContentViewHeight > 0){
            mFreeGrowUpParentRelativeLayout.setContentHeight(mContentViewHeight);
        }
        mTvGlodNum= (TextView) rootView.findViewById(R.id.tv_gold_num);
        rootView.findViewById(R.id.bt_buy_gold).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnBuyGoldClickListener!=null){
                    mOnBuyGoldClickListener.onBuyGoldClick();
                }
            }
        });
        bt_send=rootView.findViewById(R.id.bt_send);
        bt_send .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnSendGiftClickListener!=null){
                    mOnSendGiftClickListener.onSendGiftClick();
                }
            }
        });
        return rootView;
    }

    public void setSendBtText(String btText) {
        ((Button)bt_send).setText(btText);
    }

    public interface OnBuyGoldClickListener{
        void onBuyGoldClick();
    }
    public interface OnSendGiftClickListener{
        void onSendGiftClick();
    }
    public  void setSendBtEnable(boolean isEnable){
        bt_send.setEnabled(isEnable);
    }

    public  ViewPagerDelegate setContentHeight(int height){

        if(height >0 && mFreeGrowUpParentRelativeLayout != null){
            mFreeGrowUpParentRelativeLayout.setContentHeight(height);
        }else{
            mContentViewHeight=height;
        }
        return this;

    }


    @Override
    protected void setMenuList(List<MenuEntity> menuEntities) {

        mMenuEntities=menuEntities;
        mMenuListViewHandlers = new ArrayList<>();
        int fragmentCount = menuEntities.size() / (mNumColumns*2);
        if (menuEntities.size() % (mNumColumns*2) != 0) {
            fragmentCount += 1;
        }
        for (int i = 0; i < fragmentCount; i++) {

            int lastIndex = Math.min((i + 1) * (mNumColumns*2), menuEntities.size());
            MenuListViewHandler menuListViewHandler = MenuListViewHandler.getInstant
                    (i,mNumColumns, menuEntities.subList(i*(mNumColumns*2),lastIndex));
            menuListViewHandler.setOnMenuItemClickListener(new OnFragmentInteractionListenerImp());
            mMenuListViewHandlers.add(menuListViewHandler);
        }
        mViewPager.setAdapter(new ViewpagerAdapter(mMenuListViewHandlers));
        mIndicatorView.setViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectPosition(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(state==ViewPager.SCROLL_STATE_IDLE)
                mMenuListViewHandler.refreshList();
            }

        });
        selectPosition(0);

    }


    protected void show() {
        super.show();
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mParentVG.addView(mRootView, lp);
        mSweetView.show();
    }


    @Override
    protected void setOnMenuItemClickListener(SweetSheet.OnMenuItemClickListener onItemClickListener) {
        mOnMenuItemClickListener = onItemClickListener;

    }


    private void selectPosition(int position) {
        if(mMenuListViewHandlers.isEmpty()){
            return;
        }
        mMenuListViewHandler = mMenuListViewHandlers.get(position);
    }



    class OnFragmentInteractionListenerImp implements  MenuListViewHandler.OnFragmentInteractionListener{

        @Override
        public void onFragmentInteraction(AdapterView<?> parent, View view, int index) {
            if (mOnMenuItemClickListener != null) {
                mMenuEntities.get(index);
                if (mOnMenuItemClickListener.onItemClick(parent,view,index,mMenuEntities.get(index))) {
//                    delayedDismiss();
                }
            }
        }

    }

    class AnimationImp implements SweetView.AnimationListener {

        @Override
        public void onStart() {
            mStatus = SweetSheet.Status.SHOWING;
            mIndicatorView.setVisibility(View.INVISIBLE);
            if (mMenuListViewHandler != null) {
                mMenuListViewHandler.animationOnStart();
            }

        }

        @Override
        public void onEnd() {
            mStatus = SweetSheet.Status.SHOW;
            mIndicatorView.alphaShow(true);

            mIndicatorView.setVisibility(View.VISIBLE);

            mIndicatorView.circularReveal(
                    mIndicatorView.getWidth() / 2,
                    mIndicatorView.getHeight() / 2,
                    0,
                    mIndicatorView.getWidth(), 2000, new DecelerateInterpolator());

        }

        @Override
        public void onContentShow() {

            if (mMenuListViewHandler != null) {
                mMenuListViewHandler.notifyAnimation();
            }
        }
    }

}
