package com.zhiyicx.thinksnsplus;

import com.zhiyicx.common.utils.ConvertUtils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/3/17
 * @Contact master.jungle68@gmail.com
 */

public class JavaTest {

    /**
     * utc 时间测试
     */
    @Test
    public void collectionSortTest() {
        List<Integer> data = new ArrayList<>();
        data.add(10);
        data.add(19);
        data.add(12);
        data.add(15);
        data.add(3);
        data.add(1);
        data.add(2);
        data.add(20);
        data.add(50);
        data.add(5);
        Collections.sort(data, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;
            }
        });
        System.out.println("data = " + data.toString());

        List<Integer> data2 = new ArrayList<>();

        Collections.sort(data2, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        });
        System.out.println("data2 = " + data2.toString());
    }

    @Test
    public void testList() {
        List<Long> userids = new ArrayList<>();
        String test = "12,14";
        String[] testarry = test.split(",");
        userids.addAll(Arrays.asList(testarry));
        System.out.println("testarry = " + userids.toString());
    }

    /**
     * 去除头部符号
     */
    @Test
    public void removeSymbolStartWith() {
        String test = ",,2,3";
        System.out.println("ConvertUtils.removeSymbolStartWith(test,\",\") = " + ConvertUtils.removeSymbolStartWith(test, ","));
    }
    /**
     * 去除wei部符号
     */
    @Test
    public void removeSymbolEndWith() {
        String test = ",,2,3,,";
        System.out.println("ConvertUtils.removeSymbolStartWith(test,\",\") = " + ConvertUtils.removeSymbolEndWith(test, ","));
    }

}
