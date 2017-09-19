package com.zhiyicx.zhibolibrary.ui.components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.ui.adapter.DefaultAdapter;
import com.zhiyicx.zhibolibrary.ui.holder.ZBLBaseHolder;

import butterknife.ButterKnife;

/**
 * Created by jess on 16/5/1.
 */
public class CarouselView extends RelativeLayout {
    protected View rootView;
    protected LinearLayout mLeftContent;
    protected LinearLayout ll_right;
    protected LinearLayout mBetweenContent;
    protected RelativeLayout mRoot;

    LinearLayout mRightContent;
    private View mRootView;
    private int mChildWidth;
    private int mChildHeight;
    private int mRevealWidth = 80;
    private float mChildScale = 0.8f;
    private int mScaleWidth;
    private int mScaleHeidth;
    private int mCurrentIndex;
    private DefaultAdapter mAdapter;
    private ZBLBaseHolder mBetweenHolder;
    private ZBLBaseHolder mLeftHolder;
    private ZBLBaseHolder mRightHolder;
    private GestureDetector mGestureDetector;
    private OnItemClickListener mOnItemClickListener;

    public CarouselView(Context context) {
        this(context, null);
    }

    public CarouselView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CarouselView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mRootView = View.inflate(this.getContext(), R.layout.zb_component_carousel, this);
        mLeftContent = (LinearLayout) mRootView.findViewById(R.id.ll_left);
        ll_right = (LinearLayout) mRootView.findViewById(R.id.ll_right);
        mBetweenContent = (LinearLayout) mRootView.findViewById(R.id.ll_between);
        mRoot = (RelativeLayout) mRootView.findViewById(R.id.rl_root);
        //设置手势监听
        mGestureDetector = new GestureDetector(getContext(), new MyDestureListener());
    }

    private void initView() {
        ButterKnife.bind(this);//绑定
        if (mAdapter.getItemCount() < 3 && mAdapter.getItemCount() > 0) {//如果数据不够三个自动补齐
            for (int x = 0; x < (3 - mAdapter.getItemCount()); x++) {
                mAdapter.getInfos().add(mAdapter.getInfos().get(mAdapter.getItemCount() - 1));
            }
        }

        mBetweenHolder = mAdapter.onCreateViewHolder(mBetweenContent, 0);//只创建三个holder
        mLeftHolder = mAdapter.onCreateViewHolder(mLeftContent, 0);
        mRightHolder = mAdapter.onCreateViewHolder(mRightContent, 0);

        mChildWidth = mBetweenHolder.getRootView().getLayoutParams().width;//确定view的大小
        mChildHeight = mBetweenHolder.getRootView().getLayoutParams().height;


        //根据view的大小布局
        LayoutParams mBetweenLayoutParams = (LayoutParams) mBetweenContent.getLayoutParams();
        mBetweenLayoutParams.width = mChildWidth;
        mBetweenLayoutParams.height = mChildHeight;
        mBetweenLayoutParams.leftMargin = mRevealWidth;
        mBetweenContent.requestLayout();

        LayoutParams mLeftLayoutParams = (LayoutParams) mLeftContent.getLayoutParams();
        mScaleWidth = mLeftLayoutParams.width = (int) (mChildWidth * mChildScale + 0.5f);
        mScaleHeidth = mLeftLayoutParams.height = (int) (mChildHeight * mChildScale + 0.5f);
        mLeftContent.requestLayout();

        LayoutParams mRightLayoutParams = (LayoutParams) mRightContent.getLayoutParams();
        mRightLayoutParams.width = (int) (mChildWidth * mChildScale + 0.5f);
        mRightLayoutParams.height = (int) (mChildHeight * mChildScale + 0.5f);
        mRightLayoutParams.leftMargin = mRevealWidth + mChildWidth + mRevealWidth - mScaleWidth;
        mRightContent.requestLayout();

        //将传入的holder加入
        mBetweenContent.addView(mBetweenHolder.getRootView(), -1, -1);
        mLeftContent.addView(mLeftHolder.getRootView(), -1, -1);
        mRightContent.addView(mRightHolder.getRootView(), -1, -1);

        if (mAdapter.getItemCount() > 0) {
            setCurrentItem(mCurrentIndex);//设置当前的item,默认为0
        }
        else {
            mRoot.setVisibility(View.GONE);//没有数据则隐藏界面
        }

    }


    /**
     * 切换item
     *
     * @param position
     */
    public void setCurrentItem(int position) {
        if (position >= mAdapter.getItemCount()) {
            return;//position不能大于或等于总长度
        }
        this.mCurrentIndex = position;
        int previousIndex;
        int nextIndex;
        if (position == 0) {
            previousIndex = mAdapter.getItemCount() - 1;
            nextIndex = position + 1;
        }
        else if (position == mAdapter.getItemCount() - 1) {
            previousIndex = position - 1;
            nextIndex = 0;
        }
        else {
            previousIndex = position - 1;
            nextIndex = position + 1;
        }
        mAdapter.onBindViewHolder(mBetweenHolder, position);
        mAdapter.onBindViewHolder(mLeftHolder, previousIndex);
        mAdapter.onBindViewHolder(mRightHolder, nextIndex);
    }


    public void setAdapter(DefaultAdapter adapter) {
        this.mAdapter = adapter;
        initView();
    }


    public void setRevealWidth(int revealWidth) {
        this.mRevealWidth = revealWidth;
    }

    public void setChildScale(float childScale) {
        this.mChildScale = childScale;
    }


    class MyDestureListener extends GestureDetector.SimpleOnGestureListener {
        // 监听滑动手势，e1滑动的起点，e2滑动的终点,velocityX水平速度，垂直速度
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
//            // 判断纵向坐标是否超过100，提示手势不正确
//            if (Math.abs(e2.getRawY() - e1.getRawY()) > 100) {
//                Toast.makeText(getContext(), "滑动的角度不能太斜哦！", Toast.LENGTH_SHORT).show();
//                return true;
//            }
//
//            // 判断横向滑动速度是否太慢了
//            if (Math.abs(velocityX) < 100) {
//                Toast.makeText(getContext(), "滑动的太慢了哦！", Toast.LENGTH_SHORT).show();
//                return true;
//            }
            // 向右滑动，展示上一页
            if (e2.getRawX() - e1.getRawX() > 100) {
                onNext();
                return true;
            }
            // 向左滑动，展示下一页
            if (e1.getRawX() - e2.getRawX() > 100) {
                onPrevious();

                return true;
            }

            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (e.getX() <= mRevealWidth) {//点击左边的item
                onNext();//下一页
            }
            else if (e.getX() >= mRevealWidth + mChildWidth) {//点击右边的item
                onPrevious();//返回上一页
            }
            else {//点击中间的item
                mOnItemClickListener.onItemClick(mAdapter.getItem(mCurrentIndex));
            }
            return super.onSingleTapConfirmed(e);
        }
    }


    /**
     * 切换到上一页
     */
    private void onPrevious() {
        int i = ++mCurrentIndex;
        if (i >= mAdapter.getItemCount()) {
            i = 0;
        }
        setCurrentItem(i);
    }

    /**
     * 切换到下一页
     */
    private void onNext() {
        int i = --mCurrentIndex;
        if (i <= -1) {
            i = mAdapter.getItemCount() - 1;
        }
        setCurrentItem(i);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = 0;
        // 将组件触摸事件传递给gesture
        mGestureDetector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = (int) (x - event.getX());

                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return true;
    }


    public interface OnItemClickListener<T> {
        void onItemClick(T data);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}
