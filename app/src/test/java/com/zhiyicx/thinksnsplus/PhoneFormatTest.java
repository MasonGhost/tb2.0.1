package com.zhiyicx.thinksnsplus;

import com.zhiyicx.common.utils.RegexUtils;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author LiuChao
 * @describe 测试手机号的合法性
 * @date 2017/1/5
 * @contact email:450127106@qq.com
 */

public class PhoneFormatTest {
    /**
     * summary    手机号码不足11位
     * steps
     * expected   
     */
    @Test
    public void testPhoneLengthLess() throws Exception {
        boolean isFormat = RegexUtils.isMobileExact("159288");
        assertFalse(isFormat);
    }

    /**
     * summary 手机号码超过11位
     * steps
     * expected
     */
    @Test
    public void testPhoneLengthMore() throws Exception {
        boolean isFormat = RegexUtils.isMobileExact("159288565966");
        assertFalse(isFormat);
    }

    /**
     * summary 手机号码11位
     * steps
     * expected
     */
    @Test
    public void testPhoneLengthOk() throws Exception {
        boolean isFormat = RegexUtils.isMobileExact("15928856596");
        assertTrue(isFormat);
    }

    /**
     * summary 手机号码格式不正确
     * steps
     * expected
     */
    @Test
    public void testPhoneWrongFormat() throws Exception {
        boolean isFormat = RegexUtils.isMobileExact("12328856596");
        assertFalse(isFormat);
    }

}
