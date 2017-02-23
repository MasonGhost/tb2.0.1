2017年2月23日 18:13:25
# app module

项目结构目录：
```shell
--base              // 基类
    ...
--config            // 常量配置信息
--data              // 数据
    --beans              // 数据模型
    --source             // 数据来源
        --local                 // 本地数据
        --remote                // 远程数据
        --repository            // 数据操作
--modules           // 功能模块
    --chat               // 聊天
    --crop               // 图片剪切
    --dynamic            // 动态
    --edit_userinfo      // 用户编辑
    --follow_fans        // 关注、粉丝
    --gallery            // 图片查看器
    --guide              // 引导页
    --home               // 主页
    --login              // 登录
    --music_fm           // 音乐FM
    --password           // 忘记密码、找回密码
    --photopicker        // 图片选择器
    --register           // 注册
    --settings           // 设置
--service           // 服务相关
--widget            // 自定义组件
--wxapi             // 微信回调
```
项目主工程，实现ts的所有功能模块

###base目录：
`TS Plus`的基类，继承自`baseproject、common`包下的`base`基类

###config目录：
    ts中的一些配置参数

###data目录：
    数据源管理
 - beans目录：包含了app所有的javaBean
 - local目录:  greenDao实现类
 - remote目录: retrofit的接口
 - repository目录: 数据源，或者称为model层的实现

###modules目录
    ts项目的功能模块

当前项目所使用的所有接口[API.md](API.md)
