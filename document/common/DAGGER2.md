# dagger2说明

##  1.概述
Dagger是一个完全静态的编译时依赖注入框架，用于Java和Android。 它是由Square创建的早期版本的改编，现在由Google维护。

Dagger旨在解决基于反射的许多开发和性能问题。

## 2.定义

基础最核心:

- @Module	Modules类里面的方法专门提供依赖，所以我们定义一个类，用@Module注解，这样Dagger在构造类的实例的时候，就知道从哪里去找到需要的 依赖。modules的一个重要特征是它们设计为分区并组合在一起（比如说，在我们的app中可以有多个组成在一起的modules
- @Provide	在modules中，我们定义的方法是用这个注解，以此来告诉Dagger我们想要构造对象并提供这些依赖。
- @Component 用于接口，这个接口被Dagger2用于生成用于模块注入的代码,是Module和Inject的连接器
- @Inject	在需要依赖的地方使用这个注解。（你用它告诉Dagger这个 构造方法，成员变量或者函数方法需要依赖注入。这样，Dagger就会构造一个这个类的实例并满足他们的依赖。）
- @Scope	Dagger2可以通过自定义注解限定注解作用域，ActivityScope限定作用于Activity的生命周期
- @Singleton	当前提供的对象将是单例模式 ,一般配合@Provides一起出现

## 3.使用
### 配置

### 使用说明




2016年12月22日 18:59:39



