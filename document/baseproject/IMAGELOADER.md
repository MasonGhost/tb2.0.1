# 图片加载实现

## 1.概述

谷歌官方推荐使用glide进行图片加载，因此项目当前使用[gilde图片框架](https://github.com/bumptech/glide)
加载图片。

## 2.定义
在[common包](../common/COMMON.md)下，我们定义了下面的接口和类：
- ImageConfig类：图片加载的配置信息
```
public class ImageConfig {
    protected String url; //图片路径
    protected ImageView imageView; // 图片控件
    protected int placeholder; // 加载中的缺省图
    protected int errorPic;// 加载错误的缺省图
...
}
```
    我们定义了：实现图片加载功能所需要的资源

- ImageLoader类：图片加载的
```
public class ImageLoader {
    private ImageLoaderStrategy mStrategy;

    public ImageLoader(ImageLoaderStrategy strategy) {
        setLoadImgStrategy(strategy);
    }

    public <T extends ImageConfig> void loadImage(Context context, T config) {
        this.mStrategy.loadImage(context, config);
    }


    public void setLoadImgStrategy(ImageLoaderStrategy strategy) {
        this.mStrategy = strategy;
    }

}
```
    图片加载的核心类，通过传入不同的图片加载实现策略，在项目中统一调用，实现了功能使用与功能实现的解耦
- ImageLoaderStrategy接口：
   图片加载功能的实现接口，不同的图片加载框架(例如：fresco，glide，UIL)，实现相对应的功能；
```
public interface ImageLoaderStrategy<T extends ImageConfig> {
    void loadImage(Context ctx, T config);
}
```
## 3.使用说明
   主项目中的ImageLoader是TSApplication中创建，dagger进行管理
   ```
   Inject
   ImageLoader imageLoader;
   imageLoader.loadImage(Context context, GlideConfiguration config);
   ```

## 4.逻辑描述
 以本项目为例，使用[gilde图片框架](https://github.com/bumptech/glide)：
 - 定义自己的ImageConfig---GlideImageConfig：
    模仿glide加载方法，使用builder模式，保持一致性，提高代码可读性
 ```
 public class GlideImageConfig extends ImageConfig {

     private GlideImageConfig(Buidler builder) {
         this.url = builder.url;
         this.imageView = builder.imageView;
         this.placeholder = builder.placeholder;
         this.errorPic = builder.errorPic;
     }

     public static Buidler builder() {
         return new Buidler();
     }


     public static final class Buidler {
         private String url;
         private ImageView imageView;
         private int placeholder;
         protected int errorPic;

         private Buidler() {
         }

         public Buidler url(String url) {
             this.url = url;
             return this;
         }

         public Buidler placeholder(int placeholder) {
             this.placeholder = placeholder;
             return this;
         }

         public Buidler errorPic(int errorPic){
             this.errorPic = errorPic;
             return this;
         }

         public Buidler imagerView(ImageView imageView) {
             this.imageView = imageView;
             return this;
         }

         public GlideImageConfig build() {
             if (url == null) throw new IllegalStateException("url is required");
             if (imageView == null) throw new IllegalStateException("imageview is required");
             return new GlideImageConfig(this);
         }
     }
 }
 ```
- 实现ImageLoaderStrategy接口---GlideImageLoaderStrategy

    在loadImage方法中，使用glide的图片加载功能
```
public class GlideImageLoaderStrategy implements ImageLoaderStrategy<GlideImageConfig> {
    @Override
    public void loadImage(Context ctx, GlideImageConfig config) {
        RequestManager manager = null;
        if (ctx instanceof Activity)//如果是activity则可以使用Activity的生命周期
            manager = Glide.with((Activity) ctx);
        else
            manager = Glide.with(ctx);

        DrawableRequestBuilder<String> requestBuilder = manager.load(config.getUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .centerCrop();
        if (config.getPlaceholder() != 0)//设置占位符
            requestBuilder.placeholder(config.getPlaceholder());

        if (config.getErrorPic() != 0)//设置错误的图片
            requestBuilder.error(config.getErrorPic());

        requestBuilder
                .into(config.getImageView());
    }
}
```


