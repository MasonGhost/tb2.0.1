package com.zhiyicx.imsdk.core.autobahn;

import org.msgpack.template.builder.beans.BeanInfo;
import org.msgpack.template.builder.beans.IntrospectionException;
import org.msgpack.template.builder.beans.Introspector;
import org.msgpack.template.builder.beans.PropertyDescriptor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jungle on 16/7/28.
 * com.zhiyicx.imsdk.core.autobahn
 * zhibo_android
 * email:335891510@qq.com
 */
public class DataDealUitls {


    // Map --> Bean 1: 利用Introspector,PropertyDescriptor实现 Map --> Bean
    public static void transMap2Bean(Map<String, Object> map, Object obj) {

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();

                if (map.containsKey(key)) {
                    Object value = map.get(key);
                    // 得到property对应的setter方法
                    Method setter = property.getWriteMethod();
                    setter.invoke(obj, value);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Bean --> Map 1: 利用Introspector和PropertyDescriptor 将Bean --> Map
    public static Map<String, Object> transBean2Map(Object obj) {

        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    if (value != null) {
                        map.put(key, value);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static Map<String, Object> transBean2MapWithArray(Object obj) {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        Field[] fields = obj.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.get(obj) instanceof List) {
                    List<Object> list = (List) field.get(obj);
                    int i = 0;
                    for (Object innerObj : list) {
                        // "tags[0][id]"
                        // "tags[0][name]"
                        StringBuilder test = new StringBuilder();
                        test.append(field.getName() + "[" + i + "]");
                        Field[] innerfields = innerObj.getClass().getDeclaredFields();
                        for (Field innerfield : innerfields) {
                            innerfield.setAccessible(true);
                            test.append("[" + innerfield.getName() + "]");
                            // 对应的单个
                            if (innerfield.get(innerObj) == null) {
                                continue;
                            }
                            map.put(test.toString(), innerfield.get(innerObj));
                        }
                        i++;
                    }
                } else {
                    map.put(field.getName(), field.get(obj));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 使用java.beans.Introspector转换
     *
     * @param object
     * @return map
     * @throws Exception
     */
    public static Map<String, Object> obj2Map(Object object) {
        Map<String, Object> map = new HashMap<String, Object>();
        // 获取Object对象
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(object.getClass());
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        // 获取Object属性描述
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

        for (PropertyDescriptor property : propertyDescriptors) {
            String key = property.getName(); //获取属性名
            if (key.compareToIgnoreCase("class") == 0) {
                continue;
            }
            Method getter = property.getReadMethod();
            Object value = null;
            try {
                value = getter != null ? getter.invoke(object) : null;//获取值
                map.put(key, value);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

}
