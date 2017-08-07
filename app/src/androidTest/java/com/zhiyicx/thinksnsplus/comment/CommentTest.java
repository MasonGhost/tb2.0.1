package com.zhiyicx.thinksnsplus.comment;

import android.app.Application;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.MusicCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.CommonMetadataBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.modules.RxUnitTestTools;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static com.zhiyicx.thinksnsplus.data.beans.MusicCommentListBean.SEND_ING;

/**
 * @Author Jliuer
 * @Date 2017/04/28/11:32
 * @Email Jliuer@aliyun.com
 * @Description
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class CommentTest {

    private CommonMetadataBeanGreenDaoImpl mCommonMetadataBeanGreenDao;
    private TCommonMetadataProvider metadataProvider;

    @Before
    public void setUp() throws Exception {
        RxUnitTestTools.openRxTools();
        mCommonMetadataBeanGreenDao = new CommonMetadataBeanGreenDaoImpl((Application) AppApplication.getContext());
        metadataProvider = new TCommonMetadataProvider(null);
        AuthBean testAuthBean = new AuthBean();
        testAuthBean.setUser_id(18);
        AppApplication.setmCurrentLoginAuth(testAuthBean);
    }

    @Test
    public void testtSendComment() throws Exception {
        MusicCommentListBean createComment = new MusicCommentListBean();
        createComment.setMusic_id(6);
        createComment.setComment_content("COMMENT_TEST");

        String comment_mark = AppApplication.getmCurrentLoginAuth().getUser_id()
                + "" + System.currentTimeMillis();
        createComment.setComment_mark(Long.parseLong(comment_mark));

        //新的评论模块
        CommentCore.getInstance(CommentCore.CommentState.SEND, null)
                .set$$Comment_(createComment, metadataProvider)
                .handleComment();

    }

    @Test
    public void testDeleteComment() throws Exception {
        MusicCommentListBean createComment;

        List<MusicCommentListBean> commonMetadataBeanList =
                metadataProvider.getCacheCommonComments(CommentTypeConfig.TS_MUSIC_COMMENT, 6);
        if (commonMetadataBeanList.isEmpty()) {
            return;
        }
        createComment = commonMetadataBeanList.get(0);

        //新的评论模块
        CommentCore.getInstance(CommentCore.CommentState.DELETE, null)
                .set$$Comment_(createComment, metadataProvider)
                .handleComment();
    }

    @Test
    public void saveComment() throws Exception {
        List<MusicCommentListBean> datas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            datas.add(buildComment());
        }
        metadataProvider.setComments(datas).convertAndSave();
    }

    private MusicCommentListBean buildComment() throws Exception {
        MusicCommentListBean createComment = new MusicCommentListBean();
        createComment.setState(SEND_ING);
        createComment.setReply_user(0);
        createComment.setComment_content("COMMENT_TEST");
        String comment_mark = AppApplication.getmCurrentLoginAuth().getUser_id()
                + "" + System.currentTimeMillis();
        createComment.setComment_mark(Long.parseLong(comment_mark));
        createComment.setUser_id((int) AppApplication.getmCurrentLoginAuth().getUser_id());
        createComment.setCreated_at(TimeUtils.getCurrenZeroTimeStr());

        UserInfoBean userInfoBean = new UserInfoBean();
        userInfoBean.setUser_id((long) 0);
        createComment.setToUserInfoBean(userInfoBean);
        return createComment;
    }



}
