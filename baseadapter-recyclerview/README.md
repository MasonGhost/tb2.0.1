2017年1月17日 14:30:55
# 基础列表Adapter

 基于[baseAdapter](https://github.com/hongyangAndroid/baseAdapter),


 ## 修改
1. `ViewHolder`,增加`INVISIABLE`
 ```
  public ViewHolder setVisible(int viewId, int visible)
     {
         View view = getView(viewId);
         view.setVisibility(visible);
         return this;
     }
 ```
 2. `MultiItemTypeAdapter`增加`getItem(int position)`方法