package com.zhiyicx.imsdk.core.autobahn;

import org.msgpack.template.builder.beans.BeanInfo;
import org.msgpack.template.builder.beans.Introspector;
import org.msgpack.template.builder.beans.PropertyDescriptor;

import java.lang.reflect.Method;
import java.util.HashMap;
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

        return;

    }

    // Bean --> Map 1: 利用Introspector和PropertyDescriptor 将Bean --> Map
    public static Map<String, Object> transBean2Map(Object obj) {

        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
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
                    if (value != null)
                        map.put(key, value);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;

    }

    }
