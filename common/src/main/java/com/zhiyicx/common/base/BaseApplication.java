package com.zhiyicx.common.base;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.zhiyicx.common.dagger.module.AppModule;
import com.zhiyicx.common.dagger.module.ImageModule;
import com.zhiyicx.common.net.intercept.GlobeHttpHandler;

import java.util.LinkedList;

import okhttp3.Interceptor;

/**
 * @Describe  Applicaiton 基类
 * @Author Jungle68
 * @Date 2016/12/14
 * @Contact 335891510@qq.com
 */

public abstract class BaseApplication extends Application{
    protected final String TAG = this.getClass().getSimpleName();

    static private BaseApplication mApplication;
    public LinkedList<BaseActivity> mActivityList;
    private ClientModule mClientModule;
    private AppModule mAppModule;
    private ImageModule mImagerModule;



    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        this.mClientModule = ClientModule//用于提供okhttp和retrofit的单列
                .buidler()
                .baseurl(getBaseUrl())
                .globeHttpHandler(getHttpHandler())
                .interceptors(getInterceptors())
                .responseErroListener(getResponseErroListener())
                .build();
        this.mAppModule = new AppModule(this);//提供application
        this.mImagerModule = new ImageModule();//图片加载框架默认使用glide


    }


    /**
     * 提供基础url给retrofit
     *
     * @return
     */
    protected abstract String getBaseUrl();


    /**
     * 返回一个存储所有存在的activity的列表
     *
     * @return
     */
    public LinkedList<BaseActivity> getActivityList() {
        if (mActivityList == null) {
            mActivityList = new LinkedList<BaseActivity>();
        }
        return mActivityList;
    }


    public ClientModule getClientModule() {
        return mClientModule;
    }

    public AppModule getAppModule() {
        return mAppModule;
    }

    public ImageModule getImageModule() {
        return mImagerModule;
    }


    /**
     * 这里可以提供一个全局处理http响应结果的处理类,
     * 这里可以比客户端提前一步拿到服务器返回的结果,可以做一些操作,比如token超时,重新获取
     * 默认不实现,如果有需求可以重写此方法
     *
     * @return
     */
    protected GlobeHttpHandler getHttpHandler() {
        return null;
    }

    /**
     * 用来提供interceptor,如果要提供额外的interceptor可以让子application实现此方法
     *
     * @return
     */
    protected Interceptor[] getInterceptors() {
        return null;
    }


    /**
     * 用来提供处理所有错误的监听
     * 如果要使用ErrorHandleSubscriber(默认实现Subscriber的onError方法)
     * 则让子application重写此方法
     * @return
     */
    protected ResponseErroListener getResponseErroListener() {
        return new ResponseErroListener() {
            @Override
            public void handleResponseError(Context context, Exception e) {

            }
        };
    }

    /**
     * 返回上下文
     *
     * @return
     */
    public static Context getContext() {
        return mApplication;
    }


    /**
     * 退出所有activity
     */
    public static void killAll() {
        Intent intent = new Intent(BaseActivity.ACTION_RECEIVER_ACTIVITY);
        intent.putExtra("type", "killAll");
        getContext().sendBroadcast(intent);
    }
}
