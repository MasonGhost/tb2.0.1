package com.zhiyicx.thinksnsplus.data.source.local.db;

import android.content.Context;

import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.data.beans.AccountBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.AllAdverListBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.AnswerCommentListBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.AnswerDraftBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.AnswerInfoBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.ChannelInfoBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.ChannelSubscripBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfoDao;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostCommentBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.CircleSearchHistoryBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.CircleTypeBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.CommentedBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.CommonMetadataBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.DaoMaster;
import com.zhiyicx.thinksnsplus.data.beans.DigRankBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.DigedBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2Dao;
import com.zhiyicx.thinksnsplus.data.beans.DynamicToolBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.FlushMessagesDao;
import com.zhiyicx.thinksnsplus.data.beans.FollowFansBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicCommentListBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicListBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.GroupInfoBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.InfoCommentListBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.InfoListBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.InfoRecommendBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeCatesBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.JpushMessageBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumDetailsBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.MusicAlbumListBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.MusicCommentListBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.PostDigListBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.PostDraftBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.QAListInfoBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.QAPublishBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.QASearchHistoryBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBeanDao;
import com.zhiyicx.thinksnsplus.data.source.local.InfoListDataBeanGreenDaoImpl;

import org.greenrobot.greendao.database.Database;

/**
 * @author LiuChao
 * @describe 数据库升级
 * @date 2017/2/18
 * @contact email:450127106@qq.com
 */

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
        MigrationHelper.getInstance().migrate(db, UserInfoBeanDao.class);
        MigrationHelper.getInstance().migrate(db, DynamicBeanDao.class);
        MigrationHelper.getInstance().migrate(db, BackgroundRequestTaskBeanDao.class);
        MigrationHelper.getInstance().migrate(db, FollowFansBeanDao.class);
        MigrationHelper.getInstance().migrate(db, InfoListBeanDao.class);
        MigrationHelper.getInstance().migrate(db, InfoCommentListBeanDao.class);
        MigrationHelper.getInstance().migrate(db, MusicCommentListBeanDao.class);
        MigrationHelper.getInstance().migrate(db, ChannelSubscripBeanDao.class);
        MigrationHelper.getInstance().migrate(db, ChannelInfoBeanDao.class);
        MigrationHelper.getInstance().migrate(db, FlushMessagesDao.class);
        MigrationHelper.getInstance().migrate(db, DigedBeanDao.class);
        MigrationHelper.getInstance().migrate(db, CommentedBeanDao.class);
        MigrationHelper.getInstance().migrate(db, MusicAlbumDetailsBeanDao.class);

        MigrationHelper.getInstance().migrate(db, AccountBeanDao.class);
        MigrationHelper.getInstance().migrate(db, AllAdverListBeanDao.class);

        MigrationHelper.getInstance().migrate(db, AnswerCommentListBeanDao.class);
        MigrationHelper.getInstance().migrate(db, AnswerDraftBeanDao.class);
        MigrationHelper.getInstance().migrate(db, AnswerInfoBeanDao.class);
        MigrationHelper.getInstance().migrate(db, ChatGroupBeanDao.class);
        MigrationHelper.getInstance().migrate(db, CircleInfoDao.class);
        MigrationHelper.getInstance().migrate(db, CirclePostCommentBeanDao.class);
        MigrationHelper.getInstance().migrate(db, CirclePostListBeanDao.class);
        MigrationHelper.getInstance().migrate(db, CircleSearchHistoryBeanDao.class);
        MigrationHelper.getInstance().migrate(db, CircleTypeBeanDao.class);
        MigrationHelper.getInstance().migrate(db, CommonMetadataBeanDao.class);
        MigrationHelper.getInstance().migrate(db, DigRankBeanDao.class);
        MigrationHelper.getInstance().migrate(db, DynamicCommentBeanDao.class);
        MigrationHelper.getInstance().migrate(db, DynamicDetailBeanDao.class);
        MigrationHelper.getInstance().migrate(db, DynamicDetailBeanV2Dao.class);



        MigrationHelper.getInstance().migrate(db, DynamicToolBeanDao.class);
        MigrationHelper.getInstance().migrate(db, GroupDynamicCommentListBeanDao.class);
        MigrationHelper.getInstance().migrate(db, GroupDynamicListBeanDao.class);
        MigrationHelper.getInstance().migrate(db, GroupInfoBeanDao.class);
        MigrationHelper.getInstance().migrate(db, InfoListDataBeanDao.class);
        MigrationHelper.getInstance().migrate(db, InfoRecommendBeanDao.class);
        MigrationHelper.getInstance().migrate(db, InfoTypeCatesBeanDao.class);
        MigrationHelper.getInstance().migrate(db, JpushMessageBeanDao.class);
        MigrationHelper.getInstance().migrate(db, MusicAlbumListBeanDao.class);
        MigrationHelper.getInstance().migrate(db, PostDigListBeanDao.class);
        MigrationHelper.getInstance().migrate(db, PostDraftBeanDao.class);
        MigrationHelper.getInstance().migrate(db, QAListInfoBeanDao.class);
        MigrationHelper.getInstance().migrate(db, QAPublishBeanDao.class);
        MigrationHelper.getInstance().migrate(db, QASearchHistoryBeanDao.class);










    }
}
