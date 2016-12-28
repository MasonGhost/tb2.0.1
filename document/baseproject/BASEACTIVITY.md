# 关于TSActivity和TSFragment

本项目中，所有的实际功能和ui都放在TSFragment中，而TSActivity只是针对fragment进行控制，
这样做的好处是：面对页面变化很大的需求时，在不改变fragment逻辑结构情况下，只需
要简单的处理activity和fragment的关系，就可以达到我们需要的效果；

- 单个fragment的页面： TSActivity -> TSFragment
- 多个fragment的页面： TSActivity -> TSFragment -> viewPager -> 多个TSFragment

**我们只需要对TSFragment着重进行封装处理，TSActivtiy简单处理即可；**

**除非需要改变fragment的内容，否则可以把fragment放到任何地方；**

**ToolBar放在TSFragment中，控制显示隐藏，TSActivity则没有ToolBar；**

