2017年1月7日11:12:34
# 基础列表Adapter

 基于[baseAdapter](https://github.com/hongyangAndroid/baseAdapter),


 ## 修改
 `ViewHolder`,增加`INVISIABLE`
 ```
  public ViewHolder setVisible(int viewId, int visible)
     {
         View view = getView(viewId);
         view.setVisibility(visible);
         return this;
     }
 ```