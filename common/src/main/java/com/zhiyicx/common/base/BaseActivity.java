package com.zhiyicx.common.base;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;

import com.zhiyicx.common.base.i.IBaseActivity;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.utils.ActivityHandler;

import org.simple.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import skin.support.app.SkinCompatActivity;

/**
 * @Describe Activity 基类
 * @Author Jungle68
 * @Date 2016/12/14
 * @Contact 335891510@qq.com
 */

public abstract class BaseActivity<P extends BasePresenter> extends SkinCompatActivity implements
        IBaseActivity {
    protected final String TAG = this.getClass().getSimpleName();

    protected BaseApplication mApplication;
    @Inject
    protected P mPresenter;
    private Unbinder mUnbinder;
    protected LayoutInflater mLayoutInflater;
    /**
     * 用于应用是否处于前台还是后台的判断；
     */
    public boolean mIsForeground;

    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            restoreData(savedInstanceState);
        }
        mApplication = (BaseApplication) getApplication();
        ActivityHandler.getInstance().addActivity(this);
        mLayoutInflater = LayoutInflater.from(this);
        // 如果要使用 eventbus 请将此方法返回 true
        if (useEventBus()) {
            EventBus.getDefault().register(this);
        }
        setContentView(getLayoutId());
        // 绑定到 butterknife
        mUnbinder = ButterKnife.bind(this);
        initView(savedInstanceState);
        componentInject();// 依赖注入，必须放在initview后
        initData();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsForeground = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIsForeground = false;
    }

    /**
     * 子类获取contentView
     *
     * @return
     */
    protected abstract int getLayoutId();
//
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            View v = getCurrentFocus();
//            if (isShouldHideKeyboard(v, ev)) {
//                hideKeyboard(v.getWindowToken());
//            }
//        }
//        return super.dispatchTouchEvent(ev);
//    }
//
//    /**
//     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
//     *
//     * @param v
//     * @param event
//     * @return
//     */
//    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
//        if (v != null && (v instanceof EditText)) {
//            int[] l = {0, 0};
//            v.getLocationInWindow(l);
//            int left = l[0],
//                    top = l[1],
//                    bottom = top + v.getHeight(),
//                    right = left + v.getWidth();
//            if (event.getX() > left && event.getX() < right
//                    && event.getY() > top && event.getY() < bottom) {
//                // 点击EditText的事件，忽略它。
//                return false;
//            } else {
//                return true;
//            }
//        }
//        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
//        return false;
//    }
//
//    /**
//     * 获取InputMethodManager，隐藏软键盘
//     * @param token
//     */
//    private void hideKeyboard(IBinder token) {
//        if (token != null) {
//            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityHandler.getInstance().removeActivity(this);
        if (mUnbinder != Unbinder.EMPTY) {
            mUnbinder.unbind();
        }
        if (useEventBus())// 如果要使用 eventbus 请将此方法返回 true
        {
            EventBus.getDefault().unregister(this);
        }
    }

    /**
     * 是否使用 eventBus,默认为使用(true)，
     *
     * @return
     */
    protected boolean useEventBus() {
        return false;
    }

    /**
     * 依赖注入的入口
     */
    protected abstract void componentInject();

    /**
     * view 初始化
     * @param savedInstanceState
     */
    protected abstract void initView(Bundle savedInstanceState);

    /**
     * 数据初始化
     */
    protected abstract void initData();

    /**
     * 读取关闭时保存的数据
     *
     * @param savedInstanceState
     */
    protected void restoreData(Bundle savedInstanceState) {

    }

    /**
     * 关闭时保存数据
     *
     * @param savedInstanceState
     * @return
     */
    protected Bundle saveData(Bundle savedInstanceState) {
        return savedInstanceState;
    }


}
