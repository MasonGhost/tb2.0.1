2017年5月11日 10:33:05
# 接口说明
请参考[后台开发库](https://github.com/zhiyicx/thinksns-plus/blob/master/documents/api/v1/overview.md)
## 接口外层结构
```
{
    "status": false,
    "code": 0,
    "message": "",
    "data": null
}
```
## `Rx` 的`subscribe` 请求服务器数据时，请务必使用`BaseSubscribe`

### why ?

由于服务器返回的`http status code` 使用的是`RESETFUL API` 规范，如果不使用，那么服务器返回`status = false`时，`retrofit` 将调用`onError` 方法
，
所以进行了数据解析改装后返回处理结果，
结果函数：
```java
    /**
     * 服务器正确处理返回正确数据
     * @param data 正确的数据
     */
    protected abstract void onSuccess(T data);

    /**
     * 服务器正确接收到请求，主动返回错误状态以及数据
     * @param message 错误信息
     * @param code 错误编码
     */
    protected abstract void onFailure(String message, int code);

    /**
     *  系统级错误，网络错误，系统内核错误等
     * @param throwable
     */
    protected abstract void onException(Throwable throwable);

```

### 示例
```java
     Subscription getVertifySub = mRepository.getVertifyCode(phone, CommonClient.VERTIFY_CODE_TYPE_REGISTER)
                .subscribe(new BaseSubscribe<CacheBean>() {
                    @Override
                    protected void onSuccess(CacheBean data) {
                        mRootView.hideLoading();//隐藏loading
//                                   timer.start();//开始倒计时
                        mRootView.setVertifyCodeLoadin(false);
                    }

                    @Override
                    protected void onFailure(String message,int code) {
                        mRootView.showMessage(message);
                        mRootView.setVertifyCodeBtEnabled(true);
                        mRootView.setVertifyCodeLoadin(false);
                    }

                    @Override
                    protected void onException(Throwable e) {
                        e.printStackTrace();
                        mRootView.showMessage(mContext.getString(R.string.err_net_not_work));
                        mRootView.setVertifyCodeBtEnabled(true);
                        mRootView.setVertifyCodeLoadin(false);
                    }
                });

```

