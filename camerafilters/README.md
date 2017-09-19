#滤镜场景
##Introduction
内置10多种滤镜,快速接入`直播流`，支持扩展滤镜，目前只支持硬编码
## 参数及方法介绍:
###构建`FilterManager `需要传入的参数
* 必要参数
 * **Context**: 上下文
* 非必要参数
 * **isEnable**: 是否使用此**SDK**,默认为true
 * **DefaultFilter**: **SDK**启动时默认的滤镜(默认没有滤镜)
 * **OnExtFilterListener**: 如果要扩展滤镜则要实现此监听器，返回自定义滤镜

##主要方法:
* **Initialize**: 初始化参数
* **UpdateSurfaceSize**: 根据Size的变化，重置一些参数
* **Release**: 释放资源
* **DrawFrame**: 渲染纹理到屏幕上
* **ChangeFilter**: 动态的切换过滤器
* **setIsCapture**：设置当前可截屏，截屏完成会调用OnGlSurfaceShotListener
## FilterInfo structure
使用默认滤镜和切换滤镜需要传入FilterInfo对象

Field      | Description
:---------:|:---------:
 isExt     |是否使用扩展的滤镜
 index     |滤镜所在的索引，如果isExt为true，则使用内置滤镜列表，false索引对应扩展滤镜

##示例代码
`FilterManager`构建

    
    mFilterManager = FilterManager
                .builder()
                .context(mApplicationContext)
                .addExtFilterListener(new onExtFilterListener() {//添加扩展的滤镜,因为滤镜创建必须在render的回调中,所以统一在这里管理滤镜
                    @Override
                    public IFilter onCreateExtFilter(Context context, int index) {
                        switch (index) {
                            case 0://继承于cameraFitlter后可自定义filter,此Filter可任意添加一张图片到界面上
                                return new CameraFilterBlend(context, R.mipmap.
                                        pic_addpic);
                            default:
                                return new CameraFilter(context, false);
                        }
                    }
                })
                .addGlSurfaceShotListener(new OnGlSurfaceShotListener(){
                     @Override
                    public void onGlSurfaceShot(final Bitmap bitmap, final String path) {//截屏的回调，path：图片保存路径，bitmap：截图

                    }
                })
                .defaultFilter(new FilterInfo(false, 0))//设置默认滤镜(false为使用内置滤镜,角标范围是0-13,0为透明滤镜)
                .build();

在ZBStreamingClient中的`SurfaceTextureCallback`回调中使用`FilterManager`对应的方法
    
    mZBStreamingClient.setZBSurfaceTextureListener(new SurfaceTextureCallback() {
            @Override
            public void onSurfaceCreated() {
               mFilterManager.initialize();
            }

            @Override
            public void onSurfaceChanged(int i, int i1) {
               mFilterManager.updateSurfaceSize(i, i1);
            }

            @Override
            public void onSurfaceDestroyed() {
                mFilterManager.release();
            }

            @Override
            public int onDrawFrame(int i, int i1, int i2, float[] floats) {			//第三个参数必须传null
               return mFilterManager.drawFrame(var1, null, var2, var3);
            }
        });

切换滤镜 `mFilterManager.changeFilter(new FilterInfo(false, mFilterIndex + 1))`;
