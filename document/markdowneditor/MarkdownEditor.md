2018-1-9 15:34:27
# 项目中的markdown富文本编辑器

##  1.概述
1. 富文本编辑器，支持常见markdown格式内容；
2. 支持插入链接、分割线、图片等快捷操作；
3. 支持撤销&反撤销；
4. 支持H标签(h1~h4),支持加粗、斜体、删除线、引用等；
5. 支持自定义，详情参见richtexteditorlib；
6. html文件，js文件，css文件在.\app\src\main\assets\markdown目录下。



## 2.定义
- 继承`MarkdownFragment`,诸多方法详见注释，可参考 PublishPostFragment。
- 一定要看注释！！！


## 3.使用
- 重点方法如下，具体使用请见 MarkdownFragment 中注释
```java
    initBundleDataWhenOnCreate();
    preHandlePublish();
    handlePublish();
    onActivityResultForChooseCircle();
    onVisibleChange();
    editorPreLoad();
    openDraft();
    loadDraft();
    getDraftData();
    onMarkdownWordResult();
    canSaveDraft();
```
ps:
editor.getResultWords(boolean isPublish) 会触发编辑器内容回调 onMarkdownWordResult();
