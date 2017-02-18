# GreenDao3.0+的使用



## 数据库升级

```
public class UpDBHelper extends DaoMaster.OpenHelper {

    public UpDBHelper(Context context, String name) {
        super(context, name);
    }

    // 注意选择GreenDao参数的onUpgrade方法
    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        LogUtils.i("greenDAO",
                "Upgrading schema from version " + oldVersion + " to " + newVersion + " by migrating all tables data");

        // 每次升级，将需要更新的表进行更新，第二个参数为要升级的Dao文件.
        //MigrationHelper.getInstance().migrate(db, UserInfoBeanDao.class);
        //MigrationHelper.getInstance().migrate(db, AuthBeanDao.class);
        //MigrationHelper.getInstance().migrate(db, BackgroundRequestTaskBeanDao.class);
        //MigrationHelper.getInstance().migrate(db, FollowFansBeanDao.class);
    }
}
```

1.每次对某个表结构进行修改后，需要修改greenDao数据库版本号schemaVersion：
    app的buildgradle.xml文件
   ```
   greendao {
       /**
        * daoPackage 生成的DAO，DaoMaster和DaoSession的包名。默认是实体的包名。
        * targetGenDir 生成源文件的路径。默认源文件目录是在build目录中的(build/generated/source/greendao)。
        * generateTests 设置是否自动生成单元测试。
        * targetGenDirTest 生成的单元测试的根目录。
        */
       schemaVersion 1
       //daoPackage 'com.zhiyicx.common.greendao.gen'
       //targetGenDir 'src/main/java'
   }
   ```
2.通知在UpDBHelper类的onUpgrade()方法中，对该表进行数据迁移；
    xxx表示该表的Dao
      MigrationHelper.getInstance().migrate(db, xxxDao.class);

2017年2月18日10:42:47