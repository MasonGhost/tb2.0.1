package com.zhiyicx.baseproject.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhiyicx.baseproject.R;

import org.jetbrains.annotations.NotNull;

/**
 * @Describe 空置页面或错误提示
 * @Author Jungle68
 * @Date 2017/2/7
 * @Contact master.jungle68@gmail.com
 */
public class EmptyView extends LinearLayout {

    // view 当前状态
    /**
     * net error
     */
    public static final int NETWORK_ERROR = 1;
    /**
     * loading
     */
    public static final int NETWORK_LOADING = 2;
    /**
     * no data and clickable is false
     */
    public static final int NODATA = 3;
    /**
     * no data and clickable is true
     */
    public static final int NODATA_ENABLE_CLICK = 4;
    /**
     * hide this view
     */
    public static final int HIDE_LAYOUT = 5;


    private ProgressBar mAnimProgress;
    public ImageView mIvError;
    private TextView mTvError;
    private View mLlContent;

    private OnClickListener mOnClickListener;
    private int mErrorState;
    private boolean mClickEnable = true;
    private Context mContext;

    public EmptyView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private void init() {
        View view = View.inflate(mContext, R.layout.view_empty, null);
        mLlContent = view.findViewById(R.id.ll_content);
        mIvError = (ImageView) view.findViewById(R.id.iv_error_layout);
        mTvError = (TextView) view.findViewById(R.id.tv_error_layout);
        mAnimProgress = (ProgressBar) view.findViewById(R.id.pb_animation_bar);
        mLlContent.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mClickEnable) {
                    setErrorType(NETWORK_LOADING);
                    if (mOnClickListener != null)
                        mOnClickListener.onClick(v);
                }
            }
        });

        addView(view);
    }

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    @Override
    public void setVisibility(int visibility) {
        if (visibility == View.GONE) {
            mErrorState = HIDE_LAYOUT;
        }
        super.setVisibility(visibility);
    }

    public void setTvNoDataContent(@NotNull String text) {
        mTvError.setText(text);
    }

    /**
     * 隐藏当前 view
     * public void dismiss() {
     * mErrorState = HIDE_LAYOUT;
     * setVisibility(View.GONE);
     * }
     * <p>
     * public int getErrorState() {
     * return mErrorState;
     * }
     * <p>
     * /**
     *
     * @param msg   信息内容，not null
     *              public void setErrorMessage(@NotNull String msg) {
     *              mTvError.setText(msg);
     *              }
     *              <p>
     *              /**
     *              设置提示图片
     * @param resId 资源引用 id
     * @return true, 加载中
     * public boolean isLoading() {
     * return mErrorState == NETWORK_LOADING;
     * }
     * <p>
     * public void setDayNight(boolean flag) {
     * }
     * <p>
     * /**
     * 设置错误提示信息内容
     */
    public void setErrorImag(int resId) {
        mIvError.setImageResource(resId);
    }

    /**
     * 设置当前状态
     *
     * @param type 当前状态类型
     */
    public void setErrorType(int type) {
        setVisibility(View.VISIBLE);
        switch (type) {
            case NETWORK_ERROR:
                mErrorState = NETWORK_ERROR;
                mAnimProgress.setVisibility(View.GONE);
                setTvNoDataContent(mContext.getString(R.string.err_net_not_work));
                mClickEnable = true;
                break;
            case NETWORK_LOADING:
                mErrorState = NETWORK_LOADING;
                mAnimProgress.setVisibility(View.VISIBLE);
                mIvError.setVisibility(View.GONE);
                setTvNoDataContent("");
                mClickEnable = false;
                break;
            case NODATA:
                mErrorState = NODATA;
                mAnimProgress.setVisibility(View.GONE);
                setTvNoDataContent(mContext.getString(R.string.no_data));
                mClickEnable = false;
                break;
            case HIDE_LAYOUT:
                setVisibility(View.GONE);
                break;
            case NODATA_ENABLE_CLICK:
                mErrorState = NODATA_ENABLE_CLICK;
                mAnimProgress.setVisibility(View.GONE);
                setTvNoDataContent(mContext.getString(R.string.no_data));
                mClickEnable = true;
                break;
            default:
        }
    }


}
