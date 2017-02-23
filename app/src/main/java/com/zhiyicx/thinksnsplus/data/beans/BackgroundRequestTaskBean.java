<<<<<<< HEAD
package com.zhiyicx.thinksnsplus.data.beans;


import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.HashMap;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @Describe 后台任务模型
 * @Author Jungle68
 * @Date 2017/1/22
 * @Contact master.jungle68@gmail.com
 */
@Entity
public class BackgroundRequestTaskBean {
    private static final int DEFAULT_MAX_RETRY_COUNT = 5;
    @Id(autoincrement = true)
    private Long backgroundtask_id;


    private int max_retry_count = DEFAULT_MAX_RETRY_COUNT; // 最大重新请求次数
    /**
     * @see <a herf="http://greenrobot.org/objectbox/documentation/custom-types/">
     * http://greenrobot.org/objectbox/documentation/custom-types/
     * </a>
     */
    @Convert(converter = BackgroundTaskRequestMethodConfigConverter.class, columnType = Integer.class)
    private BackgroundTaskRequestMethodConfig methodType = BackgroundTaskRequestMethodConfig.POST;
    private String path;// 接口请求路径
    @Convert(converter = ParamsConverter.class, columnType = String.class)
    private HashMap<String, Object> params;

    public BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig methodType) {
        this.methodType = methodType;
    }


    public BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig methodType, HashMap<String, Object> params) {
        this.methodType = methodType;
        this.params = params;
    }

    public BackgroundRequestTaskBean(String path, HashMap<String, Object> params) {
        this.path = path;
        this.params = params;
    }

    public BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig methodType, String path, HashMap<String, Object> params) {
        this.methodType = methodType;
        this.path = path;
        this.params = params;
    }

    public BackgroundRequestTaskBean(int max_retry_count, BackgroundTaskRequestMethodConfig methodType, String path,
                                     HashMap<String, Object> params) {
        this.max_retry_count = max_retry_count;
        this.methodType = methodType;
        this.path = path;
        this.params = params;
    }

    public BackgroundRequestTaskBean() {
    }

    @Keep
    public BackgroundRequestTaskBean(Long backgroundtask_id, int max_retry_count, BackgroundTaskRequestMethodConfig methodType, String path,
                                     HashMap<String, Object> params) {
        this.backgroundtask_id = backgroundtask_id;
        this.max_retry_count = max_retry_count;
        this.methodType = methodType;
        this.path = path;
        this.params = params;
    }

    public BackgroundTaskRequestMethodConfig getMethodType() {
        return methodType;
    }

    public void setMethodType(BackgroundTaskRequestMethodConfig methodType) {
        this.methodType = methodType;
    }

    public Long getBackgroundtask_id() {
        return backgroundtask_id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public HashMap<String, Object> getParams() {
        return params;
    }

    public void setParams(HashMap<String, Object> params) {
        this.params = params;
    }

    public int getMax_retry_count() {
        return max_retry_count;
    }

    public void setMax_retry_count(int max_retry_count) {
        this.max_retry_count = max_retry_count;
    }

    public void setBackgroundtask_id(Long backgroundtask_id) {
        this.backgroundtask_id = backgroundtask_id;
    }

    /**
     * enum 转 Integer
     */
    public static class BackgroundTaskRequestMethodConfigConverter implements PropertyConverter<BackgroundTaskRequestMethodConfig, Integer> {
        @Override
        public BackgroundTaskRequestMethodConfig convertToEntityProperty(Integer databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            for (BackgroundTaskRequestMethodConfig role : BackgroundTaskRequestMethodConfig.values()) {
                if (role.id == databaseValue) {
                    return role;
                }
            }
            return BackgroundTaskRequestMethodConfig.POST;
        }

        @Override
        public Integer convertToDatabaseValue(BackgroundTaskRequestMethodConfig entityProperty) {
            return entityProperty == null ? null : entityProperty.id;
        }
    }

    /**
     * hashmap 转 String 形式存入数据库
     */
    public static class ParamsConverter implements PropertyConverter<HashMap<String, Object>, String> {
        @Override
        public HashMap<String, Object> convertToEntityProperty(String databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            return ConvertUtils.base64Str2Object(databaseValue);
        }

        @Override
        public String convertToDatabaseValue(HashMap<String, Object> params) {
            if (params == null) {
                return null;
            }
            return ConvertUtils.object2Base64Str(params);
        }
    }

    @Override
    public String toString() {
        return "BackgroundRequestTaskBean{" +
                "backgroundtask_id=" + backgroundtask_id +
                ", max_retry_count=" + max_retry_count +
                ", methodType=" + methodType +
                ", path='" + path + '\'' +
                ", params=" + params +
                '}';
    }
}
=======
package com.zhiyicx.thinksnsplus.data.beans;


import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.HashMap;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @Describe 后台任务模型
 * @Author Jungle68
 * @Date 2017/1/22
 * @Contact master.jungle68@gmail.com
 */
@Entity
public class BackgroundRequestTaskBean {
    private static final int DEFAULT_MAX_RETRY_COUNT = 5;
    @Id(autoincrement = true)
    private Long backgroundtask_id;


    private int max_retry_count = DEFAULT_MAX_RETRY_COUNT; // 最大重新请求次数
    /**
     * @see <a herf="http://greenrobot.org/objectbox/documentation/custom-types/">
     * http://greenrobot.org/objectbox/documentation/custom-types/
     * </a>
     */
    @Convert(converter = BackgroundTaskRequestMethodConfigConverter.class, columnType = Integer.class)
    private BackgroundTaskRequestMethodConfig methodType = BackgroundTaskRequestMethodConfig.POST;
    private String path;// 接口请求路径
    @Convert(converter = ParamsConverter.class, columnType = String.class)
    private HashMap<String, Object> params;

    public BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig methodType) {
        this.methodType = methodType;
    }


    public BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig methodType, HashMap<String, Object> params) {
        this.methodType = methodType;
        this.params = params;
    }

    public BackgroundRequestTaskBean(String path, HashMap<String, Object> params) {
        this.path = path;
        this.params = params;
    }

    public BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig methodType, String path, HashMap<String, Object> params) {
        this.methodType = methodType;
        this.path = path;
        this.params = params;
    }

    public BackgroundRequestTaskBean(int max_retry_count, BackgroundTaskRequestMethodConfig methodType, String path,
                                     HashMap<String, Object> params) {
        this.max_retry_count = max_retry_count;
        this.methodType = methodType;
        this.path = path;
        this.params = params;
    }

    public BackgroundRequestTaskBean() {
    }

    @Keep
    public BackgroundRequestTaskBean(Long backgroundtask_id, int max_retry_count, BackgroundTaskRequestMethodConfig methodType, String path,
                                     HashMap<String, Object> params) {
        this.backgroundtask_id = backgroundtask_id;
        this.max_retry_count = max_retry_count;
        this.methodType = methodType;
        this.path = path;
        this.params = params;
    }

    public BackgroundTaskRequestMethodConfig getMethodType() {
        return methodType;
    }

    public void setMethodType(BackgroundTaskRequestMethodConfig methodType) {
        this.methodType = methodType;
    }

    public Long getBackgroundtask_id() {
        return backgroundtask_id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public HashMap<String, Object> getParams() {
        return params;
    }

    public void setParams(HashMap<String, Object> params) {
        this.params = params;
    }

    public int getMax_retry_count() {
        return max_retry_count;
    }

    public void setMax_retry_count(int max_retry_count) {
        this.max_retry_count = max_retry_count;
    }

    public void setBackgroundtask_id(Long backgroundtask_id) {
        this.backgroundtask_id = backgroundtask_id;
    }

    /**
     * enum 转 Integer
     */
    public static class BackgroundTaskRequestMethodConfigConverter implements PropertyConverter<BackgroundTaskRequestMethodConfig, Integer> {
        @Override
        public BackgroundTaskRequestMethodConfig convertToEntityProperty(Integer databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            for (BackgroundTaskRequestMethodConfig role : BackgroundTaskRequestMethodConfig.values()) {
                if (role.id == databaseValue) {
                    return role;
                }
            }
            return BackgroundTaskRequestMethodConfig.POST;
        }

        @Override
        public Integer convertToDatabaseValue(BackgroundTaskRequestMethodConfig entityProperty) {
            return entityProperty == null ? null : entityProperty.id;
        }
    }

    /**
     * hashmap 转 String 形式存入数据库
     */
    public static class ParamsConverter implements PropertyConverter<HashMap<String, Object>, String> {
        @Override
        public HashMap<String, Object> convertToEntityProperty(String databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            return ConvertUtils.base64Str2Object(databaseValue);
        }

        @Override
        public String convertToDatabaseValue(HashMap<String, Object> params) {
            if (params == null) {
                return null;
            }
            return ConvertUtils.object2Base64Str(params);
        }
    }

    @Override
    public String toString() {
        return "BackgroundRequestTaskBean{" +
                "backgroundtask_id=" + backgroundtask_id +
                ", max_retry_count=" + max_retry_count +
                ", methodType=" + methodType +
                ", path='" + path + '\'' +
                ", params=" + params +
                '}';
    }
}
>>>>>>> 5eb1174523744bea0c0756f5af31310a1467fb94
