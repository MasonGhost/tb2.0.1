# 刷新控件

使用第三方库[SwipeToLoadLayout](https://github.com/Aspsine/SwipeToLoadLayout)

## 自定义刷新头和上拉加载样式

下拉刷新实现SwipeTrigger和SwipeRefreshTrigger接口：
```
public class RefreshHeaderView extends LinearLayout implements SwipeTrigger, SwipeRefreshTrigger {
...
    // 下拉或者上拉准备状态
    void onPrepare();
    // 下拉或者上拉移动状态
    void onMove(int y, boolean isComplete, boolean automatic);
    // 下拉或者上拉释放手指
    void onRelease();
    // 下拉或者上拉完成
    void onComplete();
    // 下拉或者上拉复位状态
    void onReset();
    // 下拉正在刷新
    void onRefresh();
}
```


上拉加载实现SwipeTrigger和SwipeLoadMoreTrigger接口：
```
public class RefreshFooterView extends LinearLayout implements SwipeTrigger, SwipeLoadMoreTrigger {
...
        // 下拉或者上拉准备状态
        void onPrepare();
        // 下拉或者上拉移动状态
        void onMove(int y, boolean isComplete, boolean automatic);
        // 下拉或者上拉释放手指
        void onRelease();
        // 下拉或者上拉完成
        void onComplete();
        // 下拉或者上拉复位状态
        void onReset();
        // 上啦正在加载
        void onLoadMore();
}
```

其中需要关注onMove方法的参数：

y表示控件的偏移量（距离顶部或者底部的距离）；
isComplete表示move是否完成
automatic表示当前的move触发条件，是否是自动触发，否则是手指的滑动导致；

通过监听上拉或者下拉的不同状态，能够完成我们需要的刷新定制

2017年3月2日11:46:36