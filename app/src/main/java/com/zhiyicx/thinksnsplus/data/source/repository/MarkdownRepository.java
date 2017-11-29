package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.source.remote.InfoMainClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.markdown_editor.MarkdownContract;

import javax.inject.Inject;

/**
 * @author Jliuer
 * @Date 2017/08/07/9:56
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MarkdownRepository extends BaseCircleRepository implements MarkdownContract.Repository {
    private InfoMainClient mInfoMainClient;

    @Inject
    public MarkdownRepository(ServiceManager serviceManager) {
        super(serviceManager);
        mInfoMainClient = serviceManager.getInfoMainClient();
    }
}
