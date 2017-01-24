# app module

项目结构目录：
```shell
--base
    ...
--config
--data
    --beans
    --source
        --local
        -- remote
        --repository
--modules
    ...
```
项目主工程，实现ts的所有功能模块

###base目录：
    ts的基类，继承自common包下的base基类

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

2017年1月24日15:32:33