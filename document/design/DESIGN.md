2017年3月16日14:30:07
# 视觉规范定义

本应用UI设计遵照TS+ Android设计文档1.1编写.
> **注：** 为了方便二次的使用修改和组件复用,本应用所有二次使用和可修改的控件及配置都位于`baseprojece`下
## 目录
> - [颜色](#color)
- [字体大小](#font_size)
- [按钮](#button)
- [头像大小](#headpic_size)
- [图标大小](#icon_size)
- [间距](#spacing)
- [UI常量](#ui_constant)
- [动作表单](#action_form)

## <span id = "color">1.颜色</span> 
文件位于`project/src/main/res/values-zh-rCN/colors_tsp_style.xml`
```java
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <!--#########################################      重要的颜色       ################################-->

    <!--
    主题色-需要特别强调的文字、icon、按钮:
    例如底部导航栏高亮按钮、顶部标题栏右侧可点击状态文字按钮
    、文章标题、填充类色块按钮、粉丝/点赞/关注/喜欢数 等
    -->
    <color name="important_for_theme">#59b6d7</color>
    <!--
    重要的列表标题文字、正文内容:
    例如标题栏标题、个人中心类列表项文字、用户昵称、聊天详情页聊天内容
    、弹窗内重要文字、详情页正文文字 等
    -->
    <color name="important_for_content">#333333</color>
    <!--
    少量使用-点赞心形图标高亮、新消息提示、警告
    -->
    <color name="important_for_note">#f4504d</color>

    <!--#########################################    普通的颜色         ################################-->

    <!--
     首页 动态列表 动态内容文字
       -->
    <color name="normal_for_dynamic_list_content">#666666</color>
    <!--
    用于辅助、次要的信息、图标等：
    例如用户列表页用户简介、个人中心/主页 简介、评论内容、不重要的按钮（取消）、消息列表消息内容、粉丝/关注列表
    粉丝数、相册列表照片张数 等
      -->
    <color name="normal_for_assist_text">#999999</color>
    <!--
    浏览量、点赞图标及其文字、动态详情页底部操作栏图标及文字、不可点击状态下文字按钮等
    -->
    <color name="normal_for_disable_button_text">#b2b2b2</color>

    <!--#########################################      一般的颜色       ################################-->

    <!--
    时间、提示文字、色块类按钮不可点击状态 等次要信息
    -->
    <color name="general_for_hint">#cccccc</color>
    <!--
    稍明显的分割线，少量使用
    顶部标题栏、底部操作栏灰色分割线、键盘升起的分割线、拍照框的描边
    -->
    <color name="general_for_line">#dedede</color>
    <!--
    稍低调的分割线，广泛使用,占位图
    页面内的浅色分割线
    -->
    <color name="general_for_line_light">#ededed</color>
    <!--
    页面的浅灰色背景，标签、热门城市背景色
    -->
    <color name="general_for_bg_light">#f4f5f5</color>
    <color name="general_for_bg_light_alpha_0.5">#80f4f5f5</color>


    <!--#########################################      特殊颜色       ################################-->

    <!--20%蒙层颜色-->
    <color name="masked_color">#33000000</color>
</resources>
```
## <span id = "font_size">2.字体大小</span>
文件位于`project/src/main/res/values-zh-rCN/dimens_tsp_font_size.xml`
```java
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <!--个人主页动态左侧的显示天的文字大小-->
    <dimen name="size_dynamic_day">22sp</dimen>
    <!-- 标题栏标题；动态详情页标题 -->
    <dimen name="size_primary_title">18sp</dimen>
    <!--动态列表动态标题；发现列表文字；
    顶部标题栏右侧文字按钮；
    详情页顶部标题栏昵称；
    消息/相册/点赞/粉丝/关注/频道/资讯/列表标题;
    个人中心/个人主页用户昵称；
    选择城市页面省市文字(非标签内文字)
    音乐专辑标题；音乐播放页面顶部操作栏歌曲名称；
    -->
    <dimen name="size_sub_title">16sp</dimen>
    <!--
    设置/修改资料/修改密码/意见反馈页面文字；
    动态详情页正文/聊天详情页聊天内容
    收到的赞列表的用户昵称;
    发布动态编辑标题和内容文字；活动列表标题;
    歌曲列表歌曲标题
    -->
    <dimen name="size_content">15sp</dimen>
    <!--
    频道/消息/相册/点赞/粉丝/关注/频道列表中标题下文字及粉丝数；
    详情页点赞人数；城市等标签内文字；
    个人中心/个人主页简介；
    音乐播放界面歌词内容；专辑/频道简介；
    报名弹窗内容文字；找人筛选文字
    -->
    <dimen name="size_content_assist">14sp</dimen>
    <!--
    首页动态/聊天详情的列表用户昵称;
    首页动态列表评论及昵称，详情页评论内容；
    订阅按钮内文字
    -->
    <dimen name="size_content_comment">13sp</dimen>
    <!--
    时间、点赞数、评论、收藏、播放量等图标标签辅助信息;
    分享弹窗图标下文字；
    详情页底部操作栏，详情页评论用户昵称
    新消息数量提示圈内的数字
    资讯列表资讯来源；活动列表的时间、地址、状态；
    音乐播放界面歌手/作者
    -->
    <dimen name="size_icon_assist">12sp</dimen>
    <!--
    底部导航栏下边小字
    输入评论字数限制提示；
    聊天详情页时间;
    音乐播放页面评论数计歌曲进度条两端的时间
    -->
    <dimen name="size_note_assist">10sp</dimen>
</resources>
```
## <span id = "button">3.按钮</span>
### 1.按钮大小圆角，位于`baseproject/src/main/res/values-zh-rCN/dimens_tsp_button.xml;`
```java
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <!--  圆角  -->
    <dimen name="button_corner_big">6dp</dimen>
    <dimen name="button_corner_small">4dp</dimen>
    <!--  大小  -->
    <dimen name="button_text_size_big">@dimen/size_sub_title</dimen>
    <dimen name="button_text_size_small">@dimen/size_content_assist</dimen>
</resources>
```
### 2.按钮颜色，位于`baseproject/src/main/res/values-zh-rCN/colors_tsp_button.xml`
```java
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <color name="bt_normal">@color/themeColor</color>
    <color name="bt_disable">@color/general_for_hint</color>
    <color name="bt_pressed">#4ab2ce</color>
    <color name="bt_pressed_hollow">#4ab2ce</color>
</resources>

```
## <span id = "headpic_size">4.头像大小</span>
位于`baseproject/src/main/res/values-zh-rCN/dimens_tsp_headpic.xml`
```java
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <!--#########################################      头像尺寸       ################################-->

    <!--
    用户个人主页头像
    -->
    <dimen name="headpic_for_user_home">70dp</dimen>
    <!--
    个人中心头像
    -->
    <dimen name="headpic_for_user_center">60dp</dimen>
    <!--
    动态/消息/赞过的/排行榜/粉丝/评论的 列表头像、
    聊详情页头像、修改资料-更换头像
    -->
    <dimen name="headpic_for_list">38dp</dimen>
    <!--
    动态/消息/赞过的/排行榜/粉丝/评论的 列表头像、
    聊详情页头像、修改资料-更换头像
    -->
    <dimen name="headpic_for_assist">26dp</dimen>
</resources>
```
## <span id = "icon_size">5.图标大小</span>
位于`baseproject/src/main/res/values-zh-rCN/dimens_tsp_icon.xml`
```java
<?xml version="1.0" encoding="utf-8"?>
<resources>
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <!--#########################################      图标尺寸       ################################-->

    <!-- 底部操作栏图标  -->
    <dimen name="icon_for_home_bottom">24dp</dimen>
    <dimen name="icon_for_home_bottom_text_margin">2dp</dimen>
    <!-- 顶部标题栏图标  -->
    <dimen name="icon_for_toolbar">24dp</dimen>
    <!-- 个人中心图标  发现列表图标 -->
    <dimen name="icon_for_user_center">20dp</dimen>
    <!--  其他辅助图标  -->
    <dimen name="icon_for_assist_big">33dp</dimen>
    <dimen name="icon_for_assist_big_text_margin">12dp</dimen>
    <dimen name="icon_for_assist_small">16dp</dimen>
    <dimen name="icon_for_assist_state_small">14dp</dimen>


    <!--#########################################      方形图片尺寸       ################################-->

    <dimen name="rec_image_for_photo">70dp</dimen>
    <dimen name="rec_image_for_list">50dp</dimen>
</resources>
```
## <span id = "spacing">6.间距</span>
位于`baseproject/src/main/res/values-zh-rCN/dimens_tsp_spacing.xml`
```java
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <!--#########################################      tsp 间距定义       ################################-->

    <dimen name="spacing_huge">50dp</dimen>
    <dimen name="spacing_big_large">30dp</dimen>
    <dimen name="spacing_large">20dp</dimen>
    <dimen name="spacing_mid">15dp</dimen>
    <dimen name="spacing_normal">10dp</dimen>
    <dimen name="spacing_small">5dp</dimen>
    <dimen name="spacing_tiny">2dp</dimen>
    <dimen name="spacing_line">@dimen/divider_line</dimen>

    <!--#########################################      分割线       ################################-->
    <dimen name="divider_line">1px</dimen>

    <!--#########################################      行间距       ################################-->
    <!--
    一定行数的文字采用此间距，是文字内容看起来紧凑。例如：
    动态列表的动内容;
    个人中心和个人主页的简介；
    聊天详情页的聊天内容；
    输入框输入大量文字而引起换行；
    除了a中的情况及其他特殊情况外其他时候均按1.3倍行间距处理
    -->
    <item name="line_spacing_Multiplier_normal" format="float" type="dimen"> 1.3</item>
    <!--
    有大段落的文字出现的情况，此时需要稍大的行间距，利于阅读。例如：
    动态详情页；
    文章详情页；
    资讯详情页等
    -->
    <item name="line_spacing_Multiplier_big" format="float" type="dimen">1.7</item>

</resources>
```

## <span id = "ui_constant">7.密码长度、动画时长、名字长度等常量定义</span>
位于`baseproject/src/main/res/values-zh-rCN/integers_tsp.xml`
```java
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <!--手机号长度-->
    <integer name="phone_number_length">11</integer>
    <!--密码最大长度-->
    <integer name="password_maxlenght">16</integer>
    <!--密码最小长度-->
    <integer name="password_min_length">2</integer>
    <!--用户名最大长度-->
    <integer name="username_max_length">20</integer>
    <!--用户名最小长度-->
    <integer name="username_min_length">6</integer>
    <!--验证码长度-->
    <integer name="vertiry_code_lenght">4</integer>
    <!--动画执行时长-->
    <integer name="animation_default_duration">300</integer>
</resources>
```
## <span id = "action_form">8.动作表单</span>
统一使用`ActionPopupWindow`
