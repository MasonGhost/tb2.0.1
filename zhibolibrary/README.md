# 直播集成说明

### 一、导入直播相关的依赖包：
- `zhibolibrary`
- `zhibosdk`
- `votesdk`
- `camerafilters`
- `carousel`
- `pickerview`
- `croplib`
- `baseproject`
- `oldimsdk`
- `imcommon`

### 二、权限配置，在相关的包的`AndroidManifest.xml` 中都含有；
### 三、混淆配置，在相关的包的`proguard-rules.pro` 中都含有；
### 四、直播域名与路径、版本号修改修改：`ZBLApi`中进行修改

```
 // 直播服务器地址，更具自己的修改
    public static final String ZHIBO_BASE_URL = "http://zts.zhibocloud.cn";
 // 版本号，更具需要修改
    public static final String ZHIBO_BASE_VERSION = "1.0";
     // 应用服务器对接直播服务器的协议地址：更具需求修改
    public static String CONFIG_EXTRAL_URL = "api";
```

### 五、初始化
1. `Application` 的`oncreate`中调用
	
	```
	   ZhiboApplication.init(this);
	```
2. 获取票据，应用服务器提供接口返回票据信息，然后通过票据去初始化直播功能。
3. 票据初始化, 在使用直播功能之前必须调用成功一次。然后就可以使用直播相关的界面与功能了
	
	```
	   //直播初始化
	                    IConfigManager configManager = ConfigManager.getInstance(UiUtils.getContext());
	                    // 域名为当前应用的域名
	                    configManager.init(ApiConfig.APP_DOMAIN, AppApplication.getmCurrentLoginAuth().getLiveTicket(), new
	                            OnCommonCallbackListener() {
	                        @Override
	                        public void onSuccess() {
	                            Intent intent=new Intent(getContext(),ZBLHomeActivity.class);
	                            startActivity(intent);
	
	                        }
	
	                        @Override
	                        public void onError(Throwable throwable) {
	                            throwable.printStackTrace();
	                            showSnackErrorMessage(getString(R.string.err_net_not_work));
	                        }
	
	                        @Override
	                        public void onFail(String code, String message) {
	                            showSnackErrorMessage(message);
	
	                        }
	                    });
	
	```