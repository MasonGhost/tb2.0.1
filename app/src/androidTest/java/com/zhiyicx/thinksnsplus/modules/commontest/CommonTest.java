package com.zhiyicx.thinksnsplus.modules.commontest;

import com.zhiyicx.common.base.BaseApplication;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.modules.AcitivityTest;

import org.junit.Test;

import static junit.framework.Assert.assertTrue;

/**
 * @author LiuChao
 * @describe
 * @date 2017/1/14
 * @contact email:450127106@qq.com
 */

public class CommonTest extends AcitivityTest {
    /**
     * summary 测试通过标识符获取资源，没找到返回0
     * steps
     * expected
     */
    @Test
    public void getResourceByName() throws Exception {
        String codeName = "code_" + 999;
        int id = UIUtils.getResourceByName(codeName, "string", BaseApplication.getContext());
        assertTrue(id == 0);
    }
}
