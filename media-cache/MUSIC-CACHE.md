# 音乐FM缓存策略
#### 一. 概述：
[音乐播放的缓存实现](https://github.com/danikula/AndroidVideoCache)，通过利用本地代理实现了边下边播，[并且做了一些优化](http://www.cnblogs.com/alexthecoder/p/5082992.html)。
#### 二. 定义：
标签: 音频/视频缓存
#### 三.[使用](http://www.cnblogs.com/alexthecoder/p/5082470.html) ：
#####1.构建(建议全局单例)：
* 默认实现：
```
private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer(this);
    }
```
* builder构造：
```
private HttpProxyCacheServer newProxy() {
    return new HttpProxyCacheServer.Builder(this)
            .maxCacheSize(1024 * 1024 * 1024) // 1 Gb for cache
            .build();
}

//其它可配置项如下：
//配置缓存目录
public Builder cacheDirectory(File file);

//配置缓存文件命名规则
public Builder fileNameGenerator(FileNameGenerator fileNameGenerator) ;

//配置缓存文件大小
public Builder maxCacheSize(long maxSize) ;

//配置缓存文件数量
public Builder maxCacheFilesCount(int count) ;
}
```
#####2.使用：
```
HttpProxyCacheServer proxy = getProxy();
String proxyUrl = proxy.getProxyUrl(resourceUrl);
videoView.setVideoPath(proxyUrl);
MediaPlayer.setDataSource(proxyUrl);
```
更新时间：2017年2月23日15:51:36




