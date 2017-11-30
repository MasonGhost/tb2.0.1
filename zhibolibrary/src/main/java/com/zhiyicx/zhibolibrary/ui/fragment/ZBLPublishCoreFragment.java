package com.zhiyicx.zhibolibrary.ui.fragment;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jess.camerafilters.base.FilterManager;
import com.jess.camerafilters.entity.FilterInfo;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.old.imsdk.de.tavendo.autobahn.DataDealUitls;
import com.zhiyicx.old.imsdk.entity.ChatRoomContainer;
import com.zhiyicx.old.imsdk.entity.ChatRoomDataCount;
import com.zhiyicx.old.imsdk.entity.GiftMessage;
import com.zhiyicx.old.imsdk.entity.Message;
import com.zhiyicx.old.imsdk.entity.MessageExt;
import com.zhiyicx.old.imsdk.entity.MessageType;
import com.zhiyicx.old.imsdk.entity.Zan;
import com.zhiyicx.votesdk.entity.OptionDetail;
import com.zhiyicx.votesdk.entity.VoteInfo;
import com.zhiyicx.votesdk.entity.VoteOption;
import com.zhiyicx.votesdk.listener.OnAudienceListener;
import com.zhiyicx.votesdk.listener.OnPresenterListener;
import com.zhiyicx.votesdk.manage.VoteManager;
import com.zhiyicx.votesdk.ui.popupwindow.VoteCreatePopupWindow;
import com.zhiyicx.votesdk.ui.popupwindow.VotePollPopupWindow;
import com.zhiyicx.votesdk.ui.view.VoteEndView;
import com.zhiyicx.votesdk.ui.view.VoteOnView;
import com.zhiyicx.zhibolibrary.R;
import com.zhiyicx.zhibolibrary.app.ZhiboApplication;
import com.zhiyicx.zhibolibrary.di.component.DaggerPublishCoreComponent;
import com.zhiyicx.zhibolibrary.di.module.PublishCoreModule;
import com.zhiyicx.zhibolibrary.di.module.UserHomeModule;
import com.zhiyicx.zhibolibrary.model.api.service.GoldService;
import com.zhiyicx.zhibolibrary.model.api.service.ServiceManager;
import com.zhiyicx.zhibolibrary.model.api.service.UserService;
import com.zhiyicx.zhibolibrary.model.entity.BaseJson;
import com.zhiyicx.zhibolibrary.model.entity.Icon;
import com.zhiyicx.zhibolibrary.model.entity.SearchResult;
import com.zhiyicx.zhibolibrary.model.entity.UserInfo;
import com.zhiyicx.zhibolibrary.model.entity.UserMessage;
import com.zhiyicx.zhibolibrary.model.entity.ZanTag;
import com.zhiyicx.zhibolibrary.presenter.PublishCorePresenter;
import com.zhiyicx.zhibolibrary.presenter.UserHomePresenter;
import com.zhiyicx.zhibolibrary.ui.Transformation.GlideCircleTrasform;
import com.zhiyicx.zhibolibrary.ui.activity.ZBLGoldRankActivity;
import com.zhiyicx.zhibolibrary.ui.adapter.LiveChatListAdapter;
import com.zhiyicx.zhibolibrary.ui.common.ZBLBaseFragment;
import com.zhiyicx.zhibolibrary.ui.components.FavorLayout;
import com.zhiyicx.zhibolibrary.ui.components.FllowButtonView;
import com.zhiyicx.zhibolibrary.ui.components.RankHeadView;
import com.zhiyicx.zhibolibrary.ui.components.popup.CommonPopupWindow;
import com.zhiyicx.zhibolibrary.ui.components.popup.CustomPopupWindow;
import com.zhiyicx.zhibolibrary.ui.components.sweetsheet.entity.MenuEntity;
import com.zhiyicx.zhibolibrary.ui.components.sweetsheet.sweetpick.DimEffect;
import com.zhiyicx.zhibolibrary.ui.components.sweetsheet.sweetpick.SweetSheet;
import com.zhiyicx.zhibolibrary.ui.components.sweetsheet.sweetpick.ViewPagerDelegate;
import com.zhiyicx.zhibolibrary.ui.view.PublishCoreParentView;
import com.zhiyicx.zhibolibrary.ui.view.PublishCoreView;
import com.zhiyicx.zhibolibrary.ui.view.PublishView;
import com.zhiyicx.zhibolibrary.ui.view.RotateLayout;
import com.zhiyicx.zhibolibrary.ui.view.UserHomeView;
import com.zhiyicx.zhibolibrary.util.Anim;
import com.zhiyicx.zhibolibrary.util.DataHelper;
import com.zhiyicx.zhibolibrary.util.DeviceUtils;
import com.zhiyicx.zhibolibrary.util.LogUtils;
import com.zhiyicx.zhibolibrary.util.UiUtils;
import com.zhiyicx.zhibosdk.manage.ZBPlayClient;
import com.zhiyicx.zhibosdk.manage.ZBStreamingClient;
import com.zhiyicx.zhibosdk.manage.listener.OnCommonCallbackListener;
import com.zhiyicx.zhibosdk.model.entity.ZBApiImInfo;
import com.zhiyicx.zhibosdk.model.entity.ZBEndStreamJson;
import com.zhiyicx.zhibosdk.model.entity.ZBGift;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zhy.autolayout.utils.AutoUtils;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.inject.Inject;

/**
 * Created by jess on 16/5/11.
 */
public class ZBLPublishCoreFragment extends ZBLBaseFragment implements PublishCoreView, UserHomeView, View.OnClickListener, ViewTreeObserver
        .OnGlobalLayoutListener {
    private static final int COLUMN_NUMS = 4;
    private static final int RANK_SHOW_NUM = 3;
    private static final int RECIEVED_ZAN = 101;
    private static final int RECIECED_GIFT = 102;
    private int mCid;
    @Inject
    PublishCorePresenter mPresenter;
    @Inject
    UserHomePresenter mPopPresenter;

    FavorLayout mFavorLayout;

    RecyclerView mMessageRecyclerView;

    AutoLinearLayout mInputContainer;

    AutoRelativeLayout mNotInputContainer;

    EditText mEditText;


    TextView mPeopleTV;


    AutoRelativeLayout mRoot;

    Button mSendBT;

    ImageView ivPresenterHeadpic;

    ImageView ivPresenteVerified;


    TextView tvPresenterName;

    TextView tvPresenterEnglishname;

    AutoLinearLayout rlPubLishcoreRanks;

    ImageView ivCamearChange;
    AutoLinearLayout rlPublishCorePresenterInfo;
    AutoLinearLayout rlPublishCorePresentGiftrank;
    ImageView ivHeadpic3;
    TextView tvName3;
    TextView tvDes3;
    TextView tvGoldNums3;
    AutoLinearLayout llGitf3;
    ImageView ivHeadpic2;
    TextView tvName2;
    TextView tvDes2;
    TextView tvGoldNums2;
    AutoLinearLayout llGitf2;
    ImageView ivHeadpic1;
    TextView tvName1;
    TextView tvDes1;
    TextView tvGoldNums1;
    AutoLinearLayout llGitf1;
    ImageView ivGiftVerified3;
    ImageView ivGiftVerified2;
    ImageView ivGiftVerified1;
    ImageView ivGift1;
    ImageView ivGift2;
    ImageView ivGift3;
    ImageButton ibPublishCoreKeyboard;
    ImageButton mPollIb;//创建投票按钮


    TextView tvVoteKey1;
    TextView tvVoteKey2;
    TextView tvVoteKey3;

    ImageView mFilterIV;
    ImageButton mGiftButton;
    ImageButton mGiftSendButton;

    AutoRelativeLayout mVoteShowContainerRl;
    TextView mFilterNameTv;


    private VotePollPopupWindow mVoteOptionPop;//投票弹窗
    private VoteCreatePopupWindow voteCreatePop;
    private boolean isVote = false;//区分是投票送礼物
    private PublishCoreParentView mPublishView;
    public final static int PUBLISH_VIEW = 0;
    public final static int LIVE_VIEW = 1;
    private int currentView;//判断当前是否是主播、0主播，1观众
    private LinearLayoutManager mLayoutManager;
    private InputMethodManager mInputMethodManager = (InputMethodManager) UiUtils.getContext()
            .getSystemService(Context.INPUT_METHOD_SERVICE);//输入管理器;
    public boolean flag;//隐藏软键盘和点击的标识
    private ArrayList<UserMessage> mChatList;
    private ArrayList<SearchResult> mGiftranks;


    private Map<String, UserInfo> mUserMessageCach = new HashMap();//用来缓存发送信息人的个人信息

    private LiveChatListAdapter mMessageAdapter;
    private CustomPopupWindow userInfoPop = null;//用户信息框\主播信息框
    private View mUserInfoPopView = null;
    private CustomPopupWindow BottomPop = null;//底部弹框，禁言、拉黑
    private View mBottomPopView = null;
    private AlertDialog.Builder mChargeDialog;//系统充值弹框
    private AlertDialog.Builder mDisableSendMsgDialog;//系统禁言弹框


    private View newsLayout;//用来作为禁言pop的父布局
    private long mTime;
    private UserInfo mClickUserInfo;//被点击用户的信息
    private boolean clickIsPresenter;//被点击用户的信息
    private Map<String, ZBGift> mGiftConfigCach = new HashMap<>();//显示礼物时查询当前礼物的配置
    private Queue<ZanTag> mZans = new ConcurrentLinkedQueue<>();//线程安全的队列,用户显示赞
    private Queue<Message> mMessages = new ConcurrentLinkedQueue<>();//线程安全的队列,用户显示礼物
    private SweetSheet mGiftSheet;
    private ViewPagerDelegate vpd;
    private int lastposiotn = -1;//上次点击的item
    private boolean isExit = false;

    private long showGiftTime = 0L;//
    private final static long GIFT_SHOW_TIME = 100 * 5;//显示礼物间隔时间

    private final static long DELAY_SHOW_TIME = 30;//ui刷新延迟时间
    private final static long DELAY_ZAN_TIME = 100;//点赞的间隔时间
    private long last_zan_click_time;

    private SearchResult data;//主播信息
    private UserInfo presenterUser;//主播信息
    private List<MenuEntity> list;

    private boolean isBanneded = false;//是否被禁言了
    private long gag = -1;//禁言时长
    private ProgressDialog mLoading;
    private ZBEndStreamJson mEndStreamJson = new ZBEndStreamJson();//本地保存收到礼物和赞
    private ChatRoomDataCount mChatRoomDataCount = new ChatRoomDataCount();//聊天室人数 浏览量 统计
    private final int MSG_REFRESHGOLD = 0x1;

    @Inject
    ServiceManager mManager;
    private CommonPopupWindow mFilterPop;//滤镜选择器，暂时隐藏
    private int mFilterIndex = 0;
    private int mInutLimitHeight;
    private int mCurrentRootInvisibleHeight;


    public static ZBLPublishCoreFragment newInstance(int currentView) {
        Bundle args = new Bundle();
        ZBLPublishCoreFragment fragment = new ZBLPublishCoreFragment();
        args.putInt("currentView", currentView);
        fragment.setArguments(args);
        return fragment;
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_REFRESHGOLD:
                    updatedGold();
                    break;
                default:
            }
        }
    };

    Handler mUIHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case RECIEVED_ZAN:
                    showZan(msg.getData().getInt("zan_type"), msg.getData().getBoolean("isOwn"));//保证和常量一直，所以+200

                    break;
                case RECIECED_GIFT:
                    showGift();
                    break;
                default:

            }

        }
    };

    private Runnable giftRunnable = new Runnable() {
        @Override
        public void run() {
            while (!isExit) {
                if (System.currentTimeMillis() - showGiftTime >= GIFT_SHOW_TIME && mUIHandler != null) {
                    mUIHandler.sendEmptyMessageDelayed(RECIECED_GIFT, DELAY_SHOW_TIME);
                    showGiftTime = System.currentTimeMillis();
                }
                if (!mZans.isEmpty()) {
                    ZanTag zanTag = mZans.poll();
                    android.os.Message msg = new android.os.Message();
                    msg.what = RECIEVED_ZAN;
                    Bundle bundle = new Bundle();
                    bundle.putInt("zan_type", zanTag.type);
                    bundle.putBoolean("isOwn", zanTag.isOwn);
                    msg.setData(bundle);
                    mUIHandler.sendMessage(msg);
                }
                //防止cpu占用过高
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    /**
     * 初始化界面
     *
     * @return
     */
    @Override
    protected View initView() {
        mInutLimitHeight = (int) (DeviceUtils.getScreenHeight(getContext()) / 3);
        this.currentView = getArguments().getInt("currentView", 0);//当前要显示的页面,当前页面可以复用,用来区分主播直播页面和用户直播页面;
        newsLayout = UiUtils.inflate(R.layout.zb_fragment_publish_core);
        mFavorLayout = (FavorLayout) newsLayout.findViewById(R.id.fl_publish_core);
        mMessageRecyclerView = (RecyclerView) newsLayout.findViewById(R.id.rv_publish_core);
        mInputContainer = (AutoLinearLayout) newsLayout.findViewById(R.id.ll_publish_core_input_container);
        mNotInputContainer = (AutoRelativeLayout) newsLayout.findViewById(R.id.rl_publish_core_not_input_container);
        mEditText = (EditText) newsLayout.findViewById(R.id.et_publish_core);
        mPeopleTV = (TextView) newsLayout.findViewById(R.id.tv_publish_core_online_people);
        mRoot = (AutoRelativeLayout) newsLayout.findViewById(R.id.publish_core_root);
        mSendBT = (Button) newsLayout.findViewById(R.id.bt_publish_core_send);
        mSendBT.setOnClickListener(this);
        ivPresenterHeadpic = (ImageView) newsLayout.findViewById(R.id.iv_presenter_headpic);
        ivPresenteVerified = (ImageView) newsLayout.findViewById(R.id.iv_presente_verified1);
        tvPresenterName = (TextView) newsLayout.findViewById(R.id.tv_presenter_name);
        tvPresenterEnglishname = (TextView) newsLayout.findViewById(R.id.tv_presenter_englishname);
        rlPubLishcoreRanks = (AutoLinearLayout) newsLayout.findViewById(R.id.rl_publish_core_ranks);
        ivCamearChange = (ImageView) newsLayout.findViewById(R.id.iv_camear_change);
        ivCamearChange.setOnClickListener(this);
        rlPublishCorePresenterInfo = (AutoLinearLayout) newsLayout.findViewById(R.id.rl_publish_core_presenter_info);
        rlPublishCorePresenterInfo.setOnClickListener(this);
        rlPublishCorePresentGiftrank = (AutoLinearLayout) newsLayout.findViewById(R.id.rl_publish_core_present_giftrank);
        rlPublishCorePresentGiftrank.setOnClickListener(this);
        ivHeadpic3 = (ImageView) newsLayout.findViewById(R.id.iv_headpic3);
        tvName3 = (TextView) newsLayout.findViewById(R.id.tv_name3);
        tvDes3 = (TextView) newsLayout.findViewById(R.id.tv_des3);
        tvGoldNums3 = (TextView) newsLayout.findViewById(R.id.tv_gold_nums3);
        llGitf3 = (AutoLinearLayout) newsLayout.findViewById(R.id.ll_gitf3);
        ivHeadpic2 = (ImageView) newsLayout.findViewById(R.id.iv_headpic2);
        tvName2 = (TextView) newsLayout.findViewById(R.id.tv_name2);
        tvDes2 = (TextView) newsLayout.findViewById(R.id.tv_des2);
        tvGoldNums2 = (TextView) newsLayout.findViewById(R.id.tv_gold_nums2);
        llGitf2 = (AutoLinearLayout) newsLayout.findViewById(R.id.ll_gitf2);
        ivHeadpic1 = (ImageView) newsLayout.findViewById(R.id.iv_headpic1);
        tvName1 = (TextView) newsLayout.findViewById(R.id.tv_name1);
        tvDes1 = (TextView) newsLayout.findViewById(R.id.tv_des1);
        tvGoldNums1 = (TextView) newsLayout.findViewById(R.id.tv_gold_nums1);
        llGitf1 = (AutoLinearLayout) newsLayout.findViewById(R.id.ll_gitf1);
        ivGiftVerified3 = (ImageView) newsLayout.findViewById(R.id.iv_gift_verified3);
        ivGiftVerified2 = (ImageView) newsLayout.findViewById(R.id.iv_gift_verified2);
        ivGiftVerified1 = (ImageView) newsLayout.findViewById(R.id.iv_gift_verified1);
        ivGift1 = (ImageView) newsLayout.findViewById(R.id.iv_gift_1);
        ivGift2 = (ImageView) newsLayout.findViewById(R.id.iv_gift_2);
        ivGift3 = (ImageView) newsLayout.findViewById(R.id.iv_gift_3);
        ibPublishCoreKeyboard = (ImageButton) newsLayout.findViewById(R.id.ib_publish_core_keyboard);
        ibPublishCoreKeyboard.setOnClickListener(this);
        mPollIb = (ImageButton) newsLayout.findViewById(R.id.ib_publish_core_poll);
        mPollIb.setOnClickListener(this);
        tvVoteKey1 = (TextView) newsLayout.findViewById(R.id.tv_option_key1);
        tvVoteKey2 = (TextView) newsLayout.findViewById(R.id.tv_option_key2);
        tvVoteKey3 = (TextView) newsLayout.findViewById(R.id.tv_option_key3);
        mFilterIV = (ImageView) newsLayout.findViewById(R.id.ib_publish_core_filter);
        mFilterIV.setOnClickListener(this);
        mGiftButton = (ImageButton) newsLayout.findViewById(R.id.ib_publish_core_gift);
        mGiftButton.setOnClickListener(this);
        mGiftSendButton = (ImageButton) newsLayout.findViewById(R.id.ib_publish_core_gift_send);
        mGiftSendButton.setOnClickListener(this);
        mVoteShowContainerRl = (AutoRelativeLayout) newsLayout.findViewById(R.id.rl_publish_core_vote_show);
        mFilterNameTv = (TextView) newsLayout.findViewById(R.id.tv_publish_core_filter_name);
        newsLayout.findViewById(R.id.ib_publish_core_share).setOnClickListener(this);
        newsLayout.findViewById(R.id.ib_publish_core_receive_list).setOnClickListener(this);


        return newsLayout;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        this.mPublishView = (PublishCoreParentView) getActivity();
        /**
         * 把自己的用户信息加入到缓存中，防止请求网络查询
         */
        mUserMessageCach.put(ZhiboApplication.getUserInfo().usid, ZhiboApplication.getUserInfo());
        data = getData();//是充直播页面进来

        if (data == null) {
            presenterUser = getUser();//重搜索页面进入
            if (presenterUser == null) {
                presenterUser = new UserInfo(new Icon());
            }
        } else {
            presenterUser = new UserInfo();
            if (data.user != null) {
                presenterUser.uid = data.user.uid;
                presenterUser.usid = data.user.usid;
                presenterUser.uname = data.user.uname;
                presenterUser.avatar = data.user.avatar;
            }
            presenterUser.location = data.stream.location;
        }


        DaggerPublishCoreComponent
                .builder()
                .clientComponent(ZhiboApplication.getZhiboClientComponent())
                .publishCoreModule(new PublishCoreModule(this))
                .userHomeModule(new UserHomeModule(this))
                .build()
                .inject(this);

//      主播和观众使用的cid不一样
        if (currentView == 0) {//主播端
            mCid = ZBStreamingClient.getInstance().getPresenterImInfo().cid;
            initVoteManager(currentView, "");
        } else if (currentView == 1) {//观众端
            mCid = ZBPlayClient.getInstance().getPresenterImInfo().cid;
            initVoteManager(currentView, presenterUser.usid);

        }
        doQueryLatestVoteInfo();
        initRecycleView();

        initGiftsView();//发送的礼物
        mPresenter.getList(false, 1, presenterUser.usid); //获取排行榜数据
        mPresenter.initZhiboRules();//添加直播间规则
        initDialog();
        settleView();//布局页面


    }

    @Override
    protected void initListener() {
    }


    private void initDialog() {
        mLoading = new ProgressDialog(getActivity());
        mLoading.setMessage(UiUtils.getString(R.string.str_loading));
        mLoading.setCanceledOnTouchOutside(false);
    }


    /**
     * 初始化礼物弹框数据
     */
    private void initGiftsView() {

        mGiftSheet = new SweetSheet((ViewGroup) newsLayout);
        list = mPresenter.getGiftPic(getContext());
        mGiftSheet.setMenuList(list);
        vpd = new ViewPagerDelegate(COLUMN_NUMS);
        mGiftSheet.setDelegate(vpd);
        mGiftSheet.setBackgroundEffect(new DimEffect(0.2f));
        mGiftSheet.setOnMenuItemClickListener(new SweetSheet.OnMenuItemClickListener() {
            @Override
            public boolean onItemClick(AdapterView<?> parent, View view, int position, MenuEntity menuEntity1) {
                /**
                 * 当前金币不足，提醒充值
                 */
                if (ZhiboApplication.getUserInfo().gold < menuEntity1.gold) {
                    showChargeDialog();
                    return false;

                }
                setGifgCheckState(position);

                return true;
            }

        });

        vpd.setTvGlodNumText(ZhiboApplication.getUserInfo().gold + "");
        vpd.setOnBuyGoldClickListener(new ViewPagerDelegate.OnBuyGoldClickListener() {
            @Override
            public void onBuyGoldClick() {
                goCharge();
            }
        });
        vpd.setOnSendGiftClickListener(new ViewPagerDelegate.OnSendGiftClickListener() {
            @Override
            public void onSendGiftClick() {
                doSendGift();
//                mGiftSheet.dismiss();

            }
        });
        //开启礼物接收显示功能
        new Thread(giftRunnable).start();
    }

    /**
     * 送礼物
     */
    private void doSendGift() {
        if (lastposiotn == -1) {
            UiUtils.makeText(getString(R.string.str_please_choose_gift));
            return;
        }
        MenuEntity meut = list.get(lastposiotn);

        if (ZhiboApplication.getUserInfo().gold < meut.gold) {
            UiUtils.makeText(getString(R.string.str_gold_not_enough));
            return;
        }
        if (isVote) {
            doVotePoll(meut, mVoteOptionPop.getVoteId(), mVoteOptionPop.getSelectOptionKey());
        } else {
            sendGiftSuccessAndShow();
//        mPresenter.exchange(1, presenterUser.uid, meut.gift_code, GoldService.EXCHANGE_TYPE_GIFT);
        }
    }

    /**
     * 设置礼物选择状态
     *
     * @param position
     */
    private void setGifgCheckState(int position) {

        /**
         * 清除上次选择状态,选中当前
         */


        if (lastposiotn != -1) {
            list.get(lastposiotn).isChecked = false;
        }
        list.get(position).isChecked = true;

        lastposiotn = position;
        vpd.setSendBtEnable(true);
    }

    /**
     * 系统充值弹框
     */
    private void showChargeDialog() {
        if (mChargeDialog == null) {


            mChargeDialog = new AlertDialog.Builder(getActivity());
            UiUtils.getDialog(mChargeDialog, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            /**
                             * 跳转充值
                             */
                            goCharge();

                        }
                    }, UiUtils.getString(R.string.charge_title_tip), UiUtils.getString(R.string.charge_message_tip), UiUtils.getString(R.string
                            .cancel),
                    UiUtils.getString(R.string.str_top_up));
        }
        mChargeDialog.show();
    }

    /**
     * 系统禁言弹框
     */
    private void showDisableSendMsgDialog() {
        if (mDisableSendMsgDialog == null) {


            mDisableSendMsgDialog = new AlertDialog.Builder(getActivity());
            UiUtils.getDialog(mDisableSendMsgDialog, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }, UiUtils.getString(R.string.str_tip), UiUtils.getString(R.string.str_you_has_be_banned), null, UiUtils.getString(R.string.str_i_konw));
        }
        mDisableSendMsgDialog.show();
    }

    /**
     * 去充值页面
     */
    private void goCharge() {
        Intent intent = new Intent(ZhiboApplication.INTENT_ACTION_RECHARGE);
        UiUtils.startActivity(intent);
    }

    /**
     * 根据不同的页面安排界面布局
     */
    private void settleView() {
//        mEditText.setFilters(new InputFilter[]{CharactorHandler.emojiFilter});//emoji过滤

        if (currentView == PUBLISH_VIEW) {//主播直播页面

            if (mGiftButton != null) {
                mGiftButton.setVisibility(View.GONE);
            }
            if (mGiftSendButton != null) {
                mGiftSendButton.setImageResource(R.mipmap.ico_live_share);
            }

            rlPublishCorePresenterInfo.setVisibility(View.GONE);
            ivCamearChange.setVisibility(View.VISIBLE);

            if (mRoot != null) {
                initFilterTouch(mRoot);
            }

//            if (mFilterIV != null)
//                mFilterIV.setVisibility(View.VISIBLE);

            if (mMessageRecyclerView != null) {
                mMessageRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {

                        AutoRelativeLayout.LayoutParams messageParams =
                                new AutoRelativeLayout.LayoutParams(((AutoRelativeLayout
                                        .LayoutParams) mMessageRecyclerView.getLayoutParams())
                                        .width, ((AutoRelativeLayout
                                        .LayoutParams) mMessageRecyclerView.getLayoutParams()).height);
                        messageParams.topMargin = AutoUtils.getPercentHeightSize(70);
                        mMessageRecyclerView.setLayoutParams(messageParams);


                    }
                });
            }

            if (mPollIb != null) {
                mPollIb.setVisibility(View.VISIBLE);
            }
            if (mFilterNameTv != null) {
                AutoRelativeLayout.LayoutParams params = new AutoRelativeLayout.LayoutParams(AutoRelativeLayout.LayoutParams.WRAP_CONTENT,
                        AutoRelativeLayout.LayoutParams.WRAP_CONTENT);
                params.addRule(AutoRelativeLayout.CENTER_HORIZONTAL);
                params.topMargin = (int) (UiUtils.getScreenHeidth() / 3);
                mFilterNameTv.setLayoutParams(params);
            }
//            if (mPollIb != null)
//                mPollIb.setVisibility(View.VISIBLE);

        } else if (currentView == LIVE_VIEW) {//用户直播页面
            initLisenter();//点击屏幕,点赞动画
            showPresenterInfo();
        }
        setEditorActiongListener();
        controlKeyboardLayout(mRootView, mRoot);//点击输入框页面上移
    }

    /**
     * 滑动执行切换过滤器初始化
     *
     * @param root
     */
    private void initFilterTouch(View root) {


        final GestureDetector gestureDetector = new GestureDetector(UiUtils.getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                // 向右滑动，切换上一个滤镜
                if (e2.getRawX() - e1.getRawX() > 100) {
                    if (mFilterIndex <= 0) {

                    } else {
                        mFilterIndex--;
                        ((PublishView) getActivity()).getFilterManager()
                                .changeFilter(new FilterInfo(false, mFilterIndex));
                        refreshFilterName(mFilterIndex);
                    }
                    return true;
                }
                // 向左滑动，切换下一个滤镜
                if (e1.getRawX() - e2.getRawX() > 100) {

//                    if (mFilterIndex >= 10) {
//
//                    } else {
                    mFilterIndex++;
                    ((PublishView) getActivity()).getFilterManager()
                            .changeFilter(new FilterInfo(false, mFilterIndex));
                    refreshFilterName(mFilterIndex);
                    //  }
                    return true;
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                try {
                    ZBStreamingClient.getInstance().setFocusAreaIndicator((RotateLayout) (getActivity()).findViewById(R.id
                            .focus_indicator_rotate_layout), R.id.focus_indicator);
                    ZBStreamingClient.getInstance().doSingleTapUp((int) e.getX(), (int) e.getY());
                } catch (Exception e1) {
                    e1.printStackTrace();
                    return false;
                }

                return true;
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if (flag) {
                    hidekeyboard();//隐藏软键盘
                    flag = false;
                    return true;
                } else {
                    return false;
                }
            }
        });

        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

    }

    /**
     * 主播切换滤镜效果，刷新显示滤镜描述
     *
     * @param mFilterIndex
     */
    private void refreshFilterName(int mFilterIndex) {
        mFilterNameTv.setText(FilterManager.FILETER_NAME.get(mFilterIndex % 8));
        ObjectAnimator animator = ObjectAnimator.ofFloat(mFilterNameTv, "alpha", 1.0f, 0f);
        animator.setDuration(800);
        animator.start();
    }

    /**
     * 显示主播头像和名字
     */
    private void showPresenterInfo() {
        if (presenterUser.avatar != null) {
            UiUtils.glideDisplayWithTrasform(presenterUser.avatar.origin, ivPresenterHeadpic, new GlideCircleTrasform(UiUtils.getContext()));
        }
        tvPresenterName.setText(presenterUser.uname);
        if (!TextUtils.isEmpty(presenterUser.location)) {
            tvPresenterEnglishname.setText(presenterUser.location);
        } else {
            tvPresenterEnglishname.setText("火星");
        }
        ivPresenteVerified.setVisibility(presenterUser.is_verified == 1 ? View.VISIBLE : View.GONE);

    }

    /**
     * 设置监听，是点赞还是隐藏软键盘
     */
    private void initLisenter() {
        mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickBlank();
            }
        });


    }

    /**
     * 点击了空白处
     */
    private void clickBlank() {
        hidekeyboard();//隐藏软键盘
        if (!flag && System.currentTimeMillis() - last_zan_click_time > DELAY_ZAN_TIME) {//点赞效果
            last_zan_click_time = System.currentTimeMillis();
            mPresenter.sendZan(mFavorLayout.getSelfColor() + 200);//保证和常量一直，所以+200
            mZans.add(new ZanTag(mFavorLayout.getSelfColor(), true));
        }
    }

    /**
     * 设置输入法右下角按钮响应事件
     */
    private void setEditorActiongListener() {
        if (mEditText != null) {
            mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent event) {
                    if (i == EditorInfo.IME_ACTION_SEARCH
                            || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode()
                            && KeyEvent.ACTION_DOWN == event.getAction())) {
                        mPresenter.sendTextmsg(mEditText.getText().toString().trim());//发送消息
                        return true;
                    }
                    return false;
                }
            });
        }
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    mSendBT.setEnabled(false);
                } else {
                    mSendBT.setEnabled(true);
                }
            }
        });
    }


    /**
     * 显示收到的礼物
     */
    private void showGift() {
        if (!mMessages.isEmpty()) {
            Message tmp = mMessages.poll();
            boolean isVote = (tmp.ext.customID == VoteManager.MESSAGE_VOTE_RECEIVED ? true : false);//true:收到的投票
            UserInfo userInfo = mUserMessageCach.get(tmp.ext.ZBUSID);
            ZBGift gift = null;
            OptionDetail detail = null;
            try {
                Gson gson = new Gson();
                if (!isVote) {
                    GiftMessage giftMessage = gson.fromJson(tmp.ext.custom.toString(), GiftMessage.class);
                    gift = mGiftConfigCach.get(giftMessage.type);
                } else {
                    VoteInfo voteInfo = gson.fromJson(gson.toJson(tmp.ext.custom), VoteInfo.class);
                    detail = voteInfo.getVoteDetail();
                    gift = mGiftConfigCach.get(detail.getGift_code());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (gift == null) {
                return;
            }
            if (llGitf1.getVisibility() != View.VISIBLE) {
                UiUtils.glideDisplayWithTrasform(userInfo.avatar.getOrigin(), ivHeadpic1, new GlideCircleTrasform(UiUtils.getContext()));

                tvName1.setText(userInfo.uname);
                ivGiftVerified1.setVisibility(userInfo.is_verified == 1 ? View.VISIBLE : View.GONE);
                if (!isVote) {
                    tvDes1.setText(getString(R.string.str_send_presenter) + gift.name);
                    tvGoldNums1.setVisibility(View.GONE);
                } else {
                    tvDes1.setText(gift.name);
                    tvGoldNums1.setVisibility(View.VISIBLE);
                }
                tvGoldNums1.setText("x" + gift.gold);
                mEndStreamJson.data.gold += gift.gold;
                Anim.showGiftAnimate(llGitf1, tvVoteKey1, getActivity());
                llGitf1.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(DataHelper.getStringSF(gift.image, getActivity().getApplicationContext()))) {
                    try {
                        ivGift1.setImageBitmap(UiUtils.getLoacalBitmap(gift.image));
                    } catch (Exception e) {
                        e.printStackTrace();
                        UiUtils.glideWrap(gift.image).into(ivGift1);
                    }
                } else {
                    UiUtils.glideWrap(gift.image).into(ivGift1);
                }

                if (isVote && null != detail) {
                    tvVoteKey1.setVisibility(View.VISIBLE);
                    tvVoteKey1.setText("投" + detail.getVote_key());
                }

                return;
            }
            if (llGitf2.getVisibility() != View.VISIBLE) {
                UiUtils.glideDisplayWithTrasform(userInfo.avatar.getOrigin(), ivHeadpic2, new GlideCircleTrasform(UiUtils.getContext()));

                tvName2.setText(userInfo.uname);
                if (!isVote) {
                    tvGoldNums2.setVisibility(View.GONE);
                    tvDes2.setText(getString(R.string.str_send_presenter) + gift.name);
                } else {
                    tvGoldNums2.setVisibility(View.VISIBLE);
                    tvDes2.setText(gift.name);
                }
                tvGoldNums2.setText("x" + gift.gold);
                mEndStreamJson.data.gold += gift.gold;
                Anim.showGiftAnimate(llGitf2, tvVoteKey2, getActivity());
                ivGiftVerified2.setVisibility(userInfo.is_verified == 1 ? View.VISIBLE : View.GONE);
                llGitf2.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(DataHelper.getStringSF(gift.image, getActivity().getApplicationContext()))) {
                    try {
                        ivGift2.setImageBitmap(UiUtils.getLoacalBitmap(gift.image));
                    } catch (Exception e) {
                        e.printStackTrace();
                        UiUtils.glideWrap(gift.image).into(ivGift2);
                    }
                } else {
                    UiUtils.glideWrap(gift.image).into(ivGift2);
                }

                if (isVote && null != detail) {
                    tvVoteKey2.setVisibility(View.VISIBLE);
                    tvVoteKey2.setText("投" + detail.getVote_key());
                }
                return;
            }
            if (llGitf3.getVisibility() != View.VISIBLE) {
                UiUtils.glideDisplayWithTrasform(userInfo.avatar.getOrigin(), ivHeadpic3, new GlideCircleTrasform(UiUtils.getContext()));

                tvName3.setText(userInfo.uname);
                if (!isVote) {
                    tvDes3.setText(getString(R.string.str_send_presenter) + gift.name);
                    tvGoldNums3.setVisibility(View.GONE);
                } else {
                    tvGoldNums3.setVisibility(View.VISIBLE);
                    tvDes3.setText(gift.name);
                }
                ivGiftVerified3.setVisibility(userInfo.is_verified == 1 ? View.VISIBLE : View.GONE);
                tvGoldNums3.setText("x" + gift.gold);
                mEndStreamJson.data.gold += gift.gold;
                Anim.showGiftAnimate(llGitf3, tvVoteKey3, getActivity());
                llGitf3.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(DataHelper.getStringSF(gift.image, getActivity().getApplicationContext()))) {
                    try {
                        ivGift3.setImageBitmap(UiUtils.getLoacalBitmap(gift.image));
                    } catch (Exception e) {
                        e.printStackTrace();
                        UiUtils.glideWrap(gift.image).into(ivGift3);
                    }
                } else {
                    UiUtils.glideWrap(gift.image).into(ivGift3);
                }
                if (isVote && null != detail) {
                    tvVoteKey3.setVisibility(View.VISIBLE);
                    tvVoteKey3.setText("投" + detail.getVote_key());
                }
                return;
            }

        }
    }

    private void initRecycleView() {
        //创建默认的线性LayoutManager
        mLayoutManager = new LinearLayoutManager(UiUtils.getContext(), LinearLayoutManager.VERTICAL, true) {
            @Override
            public PointF computeScrollVectorForPosition(int targetPosition) {
                return super.computeScrollVectorForPosition(targetPosition);
            }

            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, final int position) {
                super.smoothScrollToPosition(recyclerView, state, position);
            }
        };
//        mLayoutManager.setReverseLayout(true);
//        mLayoutManager.setStackFromEnd(true);//列表再底部开始展示，反转后由上面开始展示
        mChatList = new ArrayList<>();
        mMessageAdapter = new LiveChatListAdapter(mChatList);
        configRecycleView(mMessageRecyclerView, mLayoutManager, mMessageAdapter);
    }

    /**
     * 配置recycleview
     *
     * @param recyclerView
     * @param layoutManager
     * @param adapter
     */
    private void configRecycleView(RecyclerView recyclerView
            , RecyclerView.LayoutManager layoutManager
            , RecyclerView.Adapter adapter) {
        recyclerView.setLayoutManager(layoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    /**
     * 查看关注信息
     *
     * @param userInfo
     * @param isPresenter
     */
    private void checkFollow(UserInfo userInfo, boolean isPresenter) {
        clickIsPresenter = isPresenter;
        mClickUserInfo = userInfo;//保存当前点击的用户
        mPopPresenter.queryFollow();//检查当前选中的用户是否被关注
    }

    /**
     * 显示选中用户的信息
     *
     * @param posiotn
     */
    @Override
    public void getClickPresenterInfo(int posiotn) {
        UserMessage tmp = mChatList.get(posiotn);
        //是否已经保存了用户的说有信息
        getEveryBodyInfo(tmp.msg);

    }

    private void getEveryBodyInfo(Message message) {
        if (mUserMessageCach.containsKey(message.ext.ZBUSID) && !TextUtils.isEmpty(mUserMessageCach.get(message.ext.ZBUSID).uid)) {
            mClickUserInfo = mUserMessageCach.get(message.ext.ZBUSID);//保存当前点击的用户
            showPresenterInfo(mUserMessageCach.get(message.ext.ZBUSID), false);
        } else {

            long currentTime = System.currentTimeMillis();//频繁请求提示
            if ((currentTime - mTime) < UserService.FOLLOW_SPACING_TIME) {
                showMessage(UiUtils.getString("str_frequently_follow_prompt"));
                return;
            } else {
                mTime = System.currentTimeMillis();
            }
            mPresenter.getLocalUserInfo(message, "");
        }

    }

    /**
     * 用户加入了房间
     *
     * @param chatRoomContainer
     */
    @Override
    public void joinedChatroom(ChatRoomContainer chatRoomContainer) {
        /**
         * 减去主播自己
         */
//        mPeopleTV.setText(Math.max(chatRoomContainer.mChatRooms.get(0).mc - 1, 0) + "");
    }

    /**
     * 用户离开了房间
     *
     * @param chatRoomContainer
     */
    @Override
    public void leavedChatroom(ChatRoomContainer chatRoomContainer) {

    }

    /**
     * 主播离开了房间
     */
    @Override
    public void convrEnd(int cid) {
        hidekeyboard();
        if (currentView != PUBLISH_VIEW) {//观众直播页面
            /**
             * 跳转推荐页面
             */

            mPresenter.getRecomList(presenterUser.uid);
        } else {//主播直播页面
            if (mPublishView.isSelfClose()) {
                return;
            }
            getEndStream().isException = true;
            Bundle bundle = new Bundle();
            bundle.putSerializable("endStream", getEndStream());
            bundle.putSerializable("presenter", presenterUser);
            EventBus.getDefault().post(bundle, "close_stearm");
            killMyself();
        }

    }


    /**
     * launchActivity
     * 刷新房间人数
     */
    @Override
    public void getChatroomMc(ChatRoomDataCount chatRoomDataCount) {
        mChatRoomDataCount = chatRoomDataCount;
        mPeopleTV.setText(chatRoomDataCount.getViererCount() + "");

    }

    /**
     * 刷新个人金币数量
     */
    @Override
    public void updatedGold() {
        if (vpd != null) {
            vpd.setTvGlodNumText(ZhiboApplication.getUserInfo().gold + "");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updatedGold();
    }

    /**
     * 赞，送礼物
     *
     * @param type
     */
    @Override
    public void sendZanOrgift(int type) {
        switch (type) {
            case GoldService.EXCHANGE_TYPE_GIFT:
                sendGiftSuccessAndShow();
                break;
            case GoldService.EXCHANGE_TYPE_ZAN_PRESENTER:
                break;

        }

    }


    /**
     * 送礼物
     */
    private void sendGiftSuccessAndShow() {
        final MenuEntity meut = list.get(lastposiotn);
        final GiftMessage giftMessage = new GiftMessage(meut.gift_code, 1);
        mPresenter.sendGiftMessage(DataDealUitls.transBean2Map(giftMessage), meut.gift_code, 1 + "", new OnCommonCallbackListener() {
            @Override
            public void onSuccess() {
                /**
                 * 显示赠送的礼物
                 */
                MessageExt messageext = new MessageExt(ZhiboApplication.getUserInfo().uid, DataDealUitls.transBean2Map(giftMessage));
                messageext.ZBUSID = ZhiboApplication.getUserInfo().uid;
                Message msg = new Message();
                msg.type = MessageType.MESSAGE_CUSTOM_ID_GIFT;
                msg.ext = messageext;
                mUserMessageCach.put(msg.ext.ZBUSID, ZhiboApplication.getUserInfo());
                mMessages.add(msg);
                mPresenter.refreshGoldCountReduce(meut.gold);
                mHandler.sendEmptyMessage(MSG_REFRESHGOLD);
            }

            @Override
            public void onError(Throwable throwable) {
                showMessage(UiUtils.getString("str_network_error_action"));

            }

            @Override
            public void onFail(String code, String message) {
                showMessage(message);
            }
        });

    }

    /**
     * 刷新主播的个人信息
     *
     * @param userInfo
     */
    @Override
    public void updatePresenterInfo(UserInfo userInfo) {
        LogUtils.debugInfo(userInfo.toString());
        presenterUser = userInfo;
        checkFollow(presenterUser, true);

    }

    @Override
    public void setClickable(boolean isClickable, int type) {
        rlPublishCorePresenterInfo.setClickable(isClickable);
    }

    @Override
    public void saveGiftConfigCach(ZBGift gift) {
        mGiftConfigCach.put(gift.gift_code, gift);
    }

    /**
     * 永久禁言
     */
    @Override
    public void disableSendMsgEver() {
        disableMsg();
    }

    /**
     * 时段禁言
     *
     * @param time
     */
    @Override
    public void disableSendMsgSomeTime(long time) {
        disableMsg();
    }

    @Override
    public void setbanneded(boolean isBanneded, long gag) {
        this.isBanneded = isBanneded;
        this.gag = gag;
    }


    /**
     * 获取用于在结束页面显示金币赞个数的实体
     */
    @Override
    public ZBEndStreamJson getEndStream() {
        mEndStreamJson.data.view_count = mChatRoomDataCount.getReviewCount();
        return mEndStreamJson;
    }

    @Override
    public ChatRoomDataCount getChatRoomDataCount() {
        return mChatRoomDataCount;
    }

    @Override
    public void dimissImDisablePop() {
        if (BottomPop != null && BottomPop.isShowing()) {
            BottomPop.dismiss();
        }
    }

    @Override
    public boolean isPresenter() {
        if (currentView == PUBLISH_VIEW) {
            return true;
        }
        return false;
    }


    @Override
    public UserInfo getPresenterInfo() {
        return presenterUser;
    }

    @Override
    public Activity getCurrentActivity() {
        return getActivity();
    }

    private void disableMsg() {
        if (isBanneded) {
            return;
        }
        hidekeyboard();//隐藏软键盘
        ibPublishCoreKeyboard.setEnabled(false);
        showDisableSendMsgDialog();
    }


    /**
     * 刷新排行榜数据
     *
     * @param apiList
     * @param isMore
     */
    @Override
    public void giftRankrefresh(BaseJson apiList, boolean isMore) {
        SearchResult[] datas = (SearchResult[]) apiList.data;
        mGiftranks = new ArrayList<>();
        for (SearchResult data : datas) {//添加数据
            mGiftranks.add(data);
        }
        int size = mGiftranks.size();
        if (size == 0) {
            rlPublishCorePresentGiftrank.setVisibility(View.INVISIBLE);
            return;
        }
        if (size > RANK_SHOW_NUM) {
            size = RANK_SHOW_NUM;
        }
        for (int i = 0; i < size; i++) {
            RankHeadView iv = new RankHeadView(getActivity());
            iv.setData(mGiftranks.get(i));
            rlPubLishcoreRanks.addView(iv);
        }
        rlPublishCorePresentGiftrank.setVisibility(View.VISIBLE);

    }

    /**
     * 显示主播个人信息
     */
    private void showPresenterInfo(final UserInfo userInfotmp, final boolean isPresenter) {

        if (userInfotmp == null) {
            return;
        }
        if (mUserInfoPopView == null) {
            mUserInfoPopView = LayoutInflater.from(getActivity()).inflate(R.layout.zb_pop_userinfo, null);
        }
        try {
            userInfoPop = CustomPopupWindow.newInstance(mUserInfoPopView, null, new CustomPopupWindow.CustomPopupWindowListener() {
                        @Override
                        public void initPopupView(View contentView) {
                            ((TextView) contentView.findViewById(R.id.tv_userinfo_name)).setText(userInfotmp.uname);
                            String location;
                            if (TextUtils.isEmpty(userInfotmp.location)) {
                                location = getString(R.string.str_default_location);
                            } else {
                                location = ConvertUtils.convertLocation(userInfotmp.location);
                            }

                            ((TextView) contentView.findViewById(R.id.tv_userinfo_city)).setText(location);
                            UiUtils.glideDisplayWithTrasform(userInfotmp.avatar.getOrigin()
                                    , (ImageView) contentView.findViewById(R.id.iv_userinfo_item_icon)
                                    , new GlideCircleTrasform(getActivity().getApplicationContext()));
                            contentView.findViewById(R.id.iv_userinfo_item_verified)
                                    .setVisibility(userInfotmp.is_verified == 1 ? View.VISIBLE : View.GONE);
                            ((TextView) contentView.findViewById(R.id.tv_userinfo_intro)).setText(
                                    TextUtils.isEmpty(userInfotmp.intro) ? getString(R.string.str_default_intro) : userInfotmp.intro
                            );
                            ((TextView) contentView.findViewById(R.id.tv_userinfo_zan_nums)).setText(userInfotmp.zan_count + "");
                            ((TextView) contentView.findViewById(R.id.tv_userinfo_fans)).setText(userInfotmp.fans_count + "");
                            ((TextView) contentView.findViewById(R.id.tv_userinfo_attention)).setText(userInfotmp.follow_count + "");
                            if (currentView == PUBLISH_VIEW && userInfotmp.usid != ZhiboApplication.getUserInfo().usid) {
                                contentView.findViewById(R.id.bt_userinfo_set).setVisibility(View.VISIBLE);
                                contentView.findViewById(R.id.bt_userinfo_set).setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        // TODO: 16/5/25 禁言 /拉黑
                                        if (mBottomPopView == null) {
                                            mBottomPopView = LayoutInflater.from(getActivity())
                                                    .inflate(R.layout.zb_pop_bottom_choosebuttom, null);
                                        }
                                        BottomPop = CustomPopupWindow.newInstance(mBottomPopView, newsLayout
                                                , new CustomPopupWindow.CustomPopupWindowListener() {
                                                    @Override
                                                    public void initPopupView(View contentView) {

                                                        contentView.findViewById(R.id.bt_do).setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                // TODO: 16/5/25 拉黑禁言
                                                                if (currentView == PUBLISH_VIEW) {//禁言
                                                                    mPresenter.imDisable(userInfotmp.usid, 120);//禁言两小时
                                                                }

                                                            }
                                                        });
                                                        contentView.findViewById(R.id.bt_cancle).setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                CustomPopupWindow.dismissPop(BottomPop);
                                                            }
                                                        });
                                                        contentView.findViewById(R.id.rl_container).setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                CustomPopupWindow.dismissPop(BottomPop);
                                                            }
                                                        });


                                                    }
                                                }, null);

                                    }
                                });
                            }
                            contentView.findViewById(R.id.bt_userinfo_talk).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // TODO: 16/5/25 聊天
                                }
                            });
                            final FllowButtonView btAttention = (FllowButtonView) contentView.findViewById(R.id.bt_userinfo_attention);
                            if (userInfotmp.usid.equals(ZhiboApplication.getUserInfo().usid)) {
                                btAttention.setEnabled(false);
                                btAttention.setNameLeftDrawable(null);
                                btAttention.setName(UiUtils.getString("str_cant_follow_self"));
                                btAttention.setBackgroundResource(R.drawable.shape_follow_button_enable);

                            } else {
                                btAttention.setVisibility(View.VISIBLE);
                                btAttention.setNameSize(14);
                                btAttention.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // TODO: 16/5/25 关注
                                        long currentTime = System.currentTimeMillis();//频繁请求提示
                                        if ((currentTime - mTime) < UserService.FOLLOW_SPACING_TIME) {
                                            showMessage(UiUtils.getString("str_frequently_follow_prompt"));
                                            return;
                                        } else {
                                            mTime = System.currentTimeMillis();
                                        }
                                        mPopPresenter.follow(UserHomePresenter.isFollow(!(userInfotmp.is_follow == 1)));
                                        userInfotmp.is_follow = userInfotmp.is_follow == 1 ? 2 : 1;
                                        if (isPresenter && (btAttention.getNameString()).equals(getString(R.string.str_follow))) {
                                            mPresenter.sendAttention();//发送IM关注信息
                                        }


                                    }
                                });
                                setAtteionStatus(userInfotmp.is_follow == 1, btAttention);
                            }

                        }
                    }

                    , null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 保存个人信息，并显示数据
     *
     * @param result
     * @param msg
     */
    @Override
    public void saveAndAddChat(BaseJson result, Message msg) {
        UserInfo[] userInfos = (UserInfo[]) result.data;
        if (userInfos != null && userInfos.length > 0) {
            UserMessage userMessage1 = new UserMessage(userInfos[0], msg);
            /**
             * 加入缓存
             */
            mUserMessageCach.put(msg.ext.ZBUSID, userInfos[0]);


            /**
             * 如果请求了全部信息，则是查看资料框
             */
//            if (!TextUtils.isEmpty(userInfos[0].uid)) {
//                checkFollow(userInfos[0], false);
//            }
//            else {
            switch (msg.type) {
                case MessageType.MESSAGE_TYPE_TEXT:
                    /**
                     * 刷新列表
                     */
                    addChat(userMessage1);
                    break;

                case MessageType.MESSAGE_CUSTOM_ID_GIFT:
                case VoteManager.MESSAGE_VOTE_RECEIVED:
                    mMessages.add(msg);
                    break;
                case MessageType.MESSAGE_TYPE_CUSTOM_ENAABLE:
                    switch (msg.ext.customID) {
                        case MessageType.MESSAGE_CUSTOM_ID_GIFT:
                            mMessages.add(msg);

                            break;
                        case MessageType.MESSAGE_CUSTOM_ID_FLLOW:
                            userMessage1.msg.txt = getString(R.string.str_follow_presenter);
                            addChat(userMessage1);
                            break;

                    }

                    break;
            }
        }


//        }


    }

    /**
     * 添自己输入的信息到聊天列表
     */
    @Override
    public void addSelfChat(boolean isSuccess, Message message) {
        mSendBT.setEnabled(true);
        try {
            if (isSuccess) {//发送成功
                UserMessage userMessage = new UserMessage(ZhiboApplication.getUserInfo(), message);
                addChat(userMessage);
                mEditText.setText("");//清除聊天框
            } else {//发送失败
                showMessage("发送失败~");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * 消息添加到列表
     *
     * @param userMessage
     */

    @Override
    public void addChat(UserMessage userMessage) {
        if (!TextUtils.isEmpty(userMessage.msg.txt)) {
            mChatList.add(0, userMessage);
            mMessageAdapter.notifyDataSetChanged();
            mMessageRecyclerView.scrollToPosition(0);
        }
    }


    /**
     * @param root         最外层布局，需要调整的布局
     * @param scrollToView 被键盘遮挡的scrollToView，滚动root,使scrollToView在root可视区域的底部
     */
    public void controlKeyboardLayout(final View root, final View scrollToView) {
        root.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        Rect rect = new Rect();
        //获取root在窗体的可视区域
        mRoot.getWindowVisibleDisplayFrame(rect);
        //获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
        int rootInvisibleHeight = mRoot.getRootView().getHeight() - rect.bottom;
        if (rootInvisibleHeight != mCurrentRootInvisibleHeight) {
            //若不可视区域高度大于屏幕 1/3，则键盘显示
            if (rootInvisibleHeight > mInutLimitHeight) {
                flag = true;
                showInput();//显示输入框
            } else {
                //键盘隐藏
                flag = false;
                hideInput();//隐藏输入框
            }
            mMessageRecyclerView.scrollToPosition(0);
            mCurrentRootInvisibleHeight = rootInvisibleHeight;
            System.out.println("rootInvisibleHeight = " + rootInvisibleHeight);
        }

    }

    /**
     * 显示输入框
     */
    private void showInput() {
        mNotInputContainer.setVisibility(View.GONE);
        mInputContainer.setVisibility(View.VISIBLE);
        mEditText.requestFocus();
    }

    /**
     * 隐藏输入框
     */
    private void hideInput() {
        mNotInputContainer.setVisibility(View.VISIBLE);
        mInputContainer.setVisibility(View.GONE);
    }

    @Override
    public void showLoading() {
        mLoading.show();
    }

    @Override
    public void hideLoading() {
        mLoading.hide();
    }

    @Override
    public void showMessage(String message) {
        UiUtils.makeText(message);
    }

    @Override
    public void launchActivity(Intent intent) {
        UiUtils.startActivity(getActivity(), intent);
    }

    @Override
    public void killMyself() {
        getActivity().finish();

    }

    @Override
    public ZBLBaseFragment getCoreFragment() {
        return this;
    }

    @Override
    public void hidekeyboard() {
        if (flag) {
            mInputMethodManager.hideSoftInputFromWindow(mRootView.getApplicationWindowToken(), 0);
        }
    }

    @Override
    public ZBApiImInfo getImInfo() {
        return mPublishView.getImInfo();
    }

    @Override
    public SearchResult getData() {
        return mPublishView.getData();
    }

    @Override
    public UserInfo getUser() {
        return mPublishView.getUserInfo();
    }


    /**
     * 收到文本消息
     *
     * @param message
     */
    @Override
    public void recievedTextMessage(Message message) {

        /**
         * 在缓存中查询当前信息的用户信息是否存在
         */
        if (mUserMessageCach.containsKey(message.ext.ZBUSID)) {
            //addChat(new UserMessage(mUserMessageCach.get(message.ext.ZBUSID).mUserInfo, message));
            addChat(new UserMessage(mUserMessageCach.get(message.ext.ZBUSID), message));
        } else {
            mPresenter.getLocalUserInfo(message, "uname,is_verified,avatar");
        }

    }

    /**
     * 收到礼物消息
     *
     * @param message
     */
    @Override
    public void recievedGiftMessage(Message message) {
        Log.v("Taglei", "msg" + message + "    " + message.ext.custom.toString());
        /**
         * 在缓存中查询当前信息的用户信息是否存在
         */
        if (mUserMessageCach.containsKey(message.ext.ZBUSID)) {
            mMessages.add(message);
        } else {
            mPresenter.getLocalUserInfo(message, "uname,is_verified,avatar");
        }


    }

    /**
     * 收到赞消息
     *
     * @param message
     */
    @Override
    public void recievedZanMessage(Message message) {

        mEndStreamJson.data.zan_count++;
        mZans.add(new ZanTag(new Gson().fromJson(message.getExt().custom.toString(), Zan.class).comment - 200, false));
    }

    /**
     * 显示赞
     *
     * @param type
     * @param isOwn
     */
    private void showZan(int type, boolean isOwn) {
        mFavorLayout.setOhterColor(type);
        mFavorLayout.addFavor(isOwn);
    }


    /**
     * 收到关注消息
     *
     * @param message
     */
    @Override
    public void recievedFllowMessage(Message message) {
        /**
         * 在缓存中查询当前信息的用户信息是否存在
         */
        if (mUserMessageCach.containsKey(message.ext.ZBUSID)) {
            message.txt = getString(R.string.str_follow_presenter);
            //  addChat(new UserMessage(mUserMessageCach.get(message.ext.ZBUSID).mUserInfo, message));
            addChat(new UserMessage(mUserMessageCach.get(message.ext.ZBUSID), message));
        } else {
            mPresenter.getLocalUserInfo(message, "uname,is_verified,avatar");
        }


    }

    /**
     * 给各个场景分发消息
     *
     * @param message
     */
    @Override
    public void handleMessage(Message message) {
        Log.v("taglei", "handleMessage" + String.valueOf(message));
    }

    @Override
    public void receivedTimeOutMessage(Message message) {
        Log.v("taglei", "frg  receivedTimeOutMessage");
        if (mVoteManager != null) {
            mVoteManager.handeTimeOutMessage(message);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        endVote();
        mPresenter.onDestroy();
        isExit = true;
        mUIHandler.removeCallbacksAndMessages(null);
        mUIHandler = null;
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
        CustomPopupWindow.dismissPop(BottomPop);
        DeviceUtils.fixInputMethodManagerLeak(getActivity().getApplicationContext());
        if (mLoading != null && mLoading.isShowing()) {
            mLoading.dismiss();
        }
        if (onView != null) {
            onView.destoryTimeCounter();
        }
        mRoot.getViewTreeObserver().removeOnGlobalLayoutListener(this);

    }

    @Override
    public SearchResult getUserInfo() {
        return new SearchResult(mClickUserInfo);
    }

    @Override
    public void setFollowStatus(boolean isFollow) {


    }

    @Override
    public void setFollowEnable(boolean isEnable) {

    }

    @Override
    public void setFollow(boolean isFollow) {

        mClickUserInfo.is_follow = Integer.valueOf(UserHomePresenter.isFollow(isFollow));
        if (userInfoPop != null && userInfoPop.isShowing()) {
            FllowButtonView btAttention = (FllowButtonView) mUserInfoPopView.findViewById(R.id.bt_userinfo_attention);
            btAttention.setNameSize(14);
            setAtteionStatus(isFollow, btAttention);
            TextView tv_follows = (TextView) mUserInfoPopView.findViewById(R.id.tv_userinfo_fans);
            try {
                if (isFollow) {
                    tv_follows.setText(Integer.valueOf(tv_follows.getText().toString()) + 1 + "");

                } else {

                    tv_follows.setText(Integer.valueOf(tv_follows.getText().toString()) - 1 + "");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            showPresenterInfo(mClickUserInfo, clickIsPresenter);
        }
    }

    /**
     * 设置关注按钮的状态
     *
     * @param isFollow
     * @param btAttention
     */
    private void setAtteionStatus(boolean isFollow, FllowButtonView btAttention) {
        if (isFollow) {//是否关注
            btAttention.setName(UiUtils.getString("str_already_follow"));
            btAttention.setNameLeftDrawable(R.mipmap.ico_added_gz);
            btAttention.setBackgroundResource(R.drawable.shape_follow_button_enable);
        } else {
            btAttention.setName(UiUtils.getString("str_follow"));
            btAttention.setNameLeftDrawable(null);
            btAttention.setBackgroundResource(R.drawable.shape_blue_solid);
        }
    }

    @Override
    public boolean getFollow() {
        return UserHomePresenter.isFollow(mClickUserInfo.is_follow);
    }

    @Override
    public void showCompleted() {
        super.showCompleted();
        /**
         * 查看是否被禁言
         */
        if (isBanneded && gag != -1) {
            disableMsg();
        }

    }

    private VoteManager mVoteManager;

    @Override
    public void receivedVoteMessage(Message message) {
        Log.v(TAG, "receivedVoteMessage");
        if (mVoteManager != null) {
            mVoteManager.handleMessage(message);
        }
    }

    private void initVoteManager(int currentView, String usid) {
        String currentUsid = ZhiboApplication.getUserInfo().usid;
        if (currentView == 0) {//主播
            initVoteCreatePop(currentUsid);
            mVoteManager = VoteManager.newBuilder().cid(mCid).userType(VoteManager.TYPE_PRESENTER).setListener(preseterListener).build();
        } else if (currentView == 1) {//观众
            mVoteManager = VoteManager.newBuilder().cid(mCid).userType(VoteManager.TYPE_AUDIENCE).presenterUsid(usid).setListener(audienceListener)
                    .build();
        }

    }

    /**
     * 查询最近一次的投票
     */
    private void doQueryLatestVoteInfo() {
        mVoteManager.queryLatestVote();
    }


    private void initVoteCreatePop(String usid) {
        if (currentView == 0) {//主播
            voteCreatePop = new VoteCreatePopupWindow(getActivity(), mCid);
            voteCreatePop.setPresenterListener(preseterListener);
        }
    }

    private void initVotePollPop(VoteInfo info) {
        if (mVoteOptionPop == null) {
            mVoteOptionPop = new VotePollPopupWindow(getActivity());
            mVoteOptionPop.setVoteInfo(info);
            mVoteOptionPop.setOnVoteListener(new VotePollPopupWindow.OnVoteOptionListener() {
                @Override
                public void vote(String optionKey) {
                    isVote = true;
                    if (lastposiotn != -1) {
                        vpd.getViewPager().findViewWithTag(lastposiotn)
                                .setBackgroundDrawable(null);
                        lastposiotn = -1;
                    }
                    vpd.setSendBtText("投票");
                    vpd.setSendBtEnable(false);
                    mGiftSheet.toggle();//弹出礼物框
                }
            });
        } else {
            mVoteOptionPop.setVoteInfo(info);
        }
    }

    private VoteOnView onView;

    /**
     * 投票显示
     */
    private void initVoteView(VoteInfo voteInfo) {
        //清空view、数据
        mVoteShowContainerRl.removeAllViews();
        mVoteShowContainerRl.setVisibility(View.VISIBLE);
        onView = null;
        //0: 结束 1：投票中 2：投票暂停
        if (voteInfo.getStatus() == 0) {
            mVoteShowContainerRl.addView(new VoteEndView(getActivity(), voteInfo));
        } else {
            addVoteOnView(voteInfo);
        }

        if (currentView == 0) {
            if (voteInfo.getStatus() == 0) {
                mPollIb.setImageResource(R.mipmap.host_btn_vote_normal);
                mPollIb.setEnabled(true);

            } else {
                mPollIb.setImageResource(R.mipmap.host_btn_vote_unable);
                mPollIb.setEnabled(false);
            }

        }

    }


    private void addVoteOnView(final VoteInfo info) {
        mVoteShowContainerRl.removeAllViews();
        if (onView != null) {
            onView.destoryTimeCounter();
        }
        onView = new VoteOnView(getActivity(), info);
        //onView.setUserType(type);
        AutoRelativeLayout.LayoutParams params = new AutoRelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams
                .WRAP_CONTENT);
        params.leftMargin = UiUtils.dip2px(10);
        onView.setLayoutParams(params);
        onView.setOnVoteEndListener(new VoteOnView.OnVoteEndListener() {
            @Override
            public void voteEnd() {
                mVoteManager.queryNewstVote(info.getVote_id());
            }
        });
        onView.setOnVoteOptionClickListener(new VoteOnView.OnVoteOptionClickListener() {
            @Override
            public void click(VoteOption option, VoteInfo voteInfo) {
                Log.v("taglei", "addVoteOnView  click...");
                if (lastposiotn != -1) {
                    list.get(lastposiotn).isChecked = false;
                    lastposiotn = -1;

                }
                if (null != mVoteOptionPop) {
                    mVoteOptionPop.show(mRootView);
                }
            }
        });

        mVoteShowContainerRl.addView(onView);
    }

    MenuEntity gift;

    /**
     * 观众投票
     *
     * @param vote_id
     * @param optionKey
     */
    private void doVotePoll(final MenuEntity gift, String vote_id, String optionKey) {
        this.gift = gift;
        mVoteManager.sendPoll(vote_id, optionKey, gift.gift_code, gift.gold);

    }

    private OnPresenterListener preseterListener = new OnPresenterListener() {

        @Override
        public void onQueryLastSuccess(VoteInfo voteInfo) {
            Log.v(TAG, "preseterListener onQueryLastSuccess<<  " + String.valueOf(voteInfo));
            if (null != voteInfo) {
                if (voteInfo.getStatus() != 0) {
                    mVoteManager.voteStop(voteInfo.getVote_id());
                    voteInfo.setStatus(0);
                    //initVoteView(voteInfo);
                }

            }
        }

        @Override
        public void onQueryLastFailure(String code, String msg) {
            super.onQueryLastFailure(code, msg);
            Log.v(TAG, " PresenterListener onQueryLastFailure<<  " + code + msg);
        }

        @Override
        public void onVoteCreateSuccess(VoteInfo voteInfo) {
            Log.v(TAG, "PresenterListener  onVoteCreateSuccess<<");
            initVoteView(voteInfo);
        }

        @Override
        public void onQueryNewestSuccess(VoteInfo voteInfo) {
            super.onQueryNewestSuccess(voteInfo);
            initVoteView(voteInfo);
        }

        @Override
        public void onVoteCreateFailure(String code, String message) {
            super.onVoteCreateFailure(code, message);
            UiUtils.SnackbarText(message);
        }

        @Override
        public void receiveAudienceVote(Message message, VoteInfo info, OptionDetail detail) {
            Log.v("taglei", "fragment-- OnPresenterListener receiveAudienceVote" + String.valueOf(info));
            message.is_del = true;
            mEndStreamJson.data.gold += detail.getGold();
            if (onView != null) {
                onView.refreshOptionItemValue(info);
            }
            if (mUserMessageCach.containsKey(message.ext.ZBUSID)) {
                mMessages.add(message);
            } else {
                mPresenter.getLocalUserInfo(message, "uname,is_verified,avatar");
            }
        }
    };

    private OnAudienceListener audienceListener = new OnAudienceListener() {


        @Override
        public void onQueryLastSuccess(VoteInfo voteInfo) {
            Log.v(TAG, "AudienceListener onSuccess<<  " + String.valueOf(voteInfo));
            if (null != voteInfo) {
                if (voteInfo.getStatus() != 0) {
                    initVoteView(voteInfo);
                    initVotePollPop(voteInfo);
                }
            }
        }

        @Override
        public void onQueryLastFailure(String code, String msg) {
            Log.v(TAG, "AudienceListener onQueryLastFailure<<  ");
        }

        @Override
        public void onQueryNewestSuccess(VoteInfo voteInfo) {
            super.onQueryNewestSuccess(voteInfo);
            initVoteView(voteInfo);
        }

        @Override
        public void onVoteSuccess(VoteInfo voteInfo, OptionDetail detail) {
            refreshGoldCount(gift.gold);
            updatedGold();
            if (voteInfo != null) {
                if (onView != null) {
                    onView.refreshOptionItemValue(voteInfo);
                } else {
                    initVoteView(voteInfo);
                }
                MessageExt messageext = new MessageExt(ZhiboApplication.getUserInfo().uid, DataDealUitls.transBean2Map(voteInfo));
                messageext.ZBUSID = ZhiboApplication.getUserInfo().uid;
                messageext.customID = VoteManager.MESSAGE_VOTE_RECEIVED;
                Message msg = new Message();
                msg.is_del = true;
                msg.type = MessageType.MESSAGE_CUSTOM_ID_GIFT;
                msg.ext = messageext;
                mUserMessageCach.put(msg.ext.ZBUSID, ZhiboApplication.getUserInfo());
                mMessages.add(msg);
                mPresenter.refreshGoldCountReduce(detail.getGold());
                mHandler.sendEmptyMessage(MSG_REFRESHGOLD);
            }
        }

        @Override
        public void onVoteFailure(String code, String message) {
            super.onVoteFailure(code, message);
            UiUtils.SnackbarText(message);
        }

        @Override
        public void receivePresenterMessage(Message msg, VoteInfo info) {
            Log.v(TAG, "audienceListener receivePresenterMessage" + String.valueOf(msg));

        }

        @Override
        public void receivePresenterVoteCreate(Message msg, VoteInfo info) {
            super.receivePresenterVoteCreate(msg, info);
            initVoteView(info);
            initVotePollPop(info);
        }

        @Override
        public void receiveAudienceVote(Message msg, VoteInfo info, OptionDetail detail) {
            msg.is_del = true;
            if (mUserMessageCach.containsKey(msg.ext.ZBUSID)) {
                msg.is_del = true;
                mMessages.add(msg);
            } else {
                mPresenter.getLocalUserInfo(msg, "uname,is_verified,avatar");
            }


            if (onView != null) {
                onView.refreshOptionItemValue(info);
            }
        }

        @Override
        public void receivePresenterVoteStop(Message msg, VoteInfo info) {
            super.receivePresenterVoteStop(msg, info);
            if (onView != null) {
                initVoteView(info);
            }
        }
    };


    private void refreshGoldCount(int decrice) {
        UserInfo info = ZhiboApplication.getUserInfo();
        info.gold = info.gold - decrice;
        ZhiboApplication.setUserInfo(info);
    }


    private void endVote() {
        if (currentView == 0) {
            if (onView != null) {
                mVoteManager.voteStop(onView.getVoteId());
            }
        }
    }

    /**
     * @param view
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ib_publish_core_keyboard) {
            /**
             * 点击发消息图标
             */
            mInputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED
                    , InputMethodManager.HIDE_IMPLICIT_ONLY);//显示软键盘
        } else if (
                view.getId() == R.id.bt_publish_core_send) {
            /**
             * 点击发送按钮
             */
            mPresenter.sendTextmsg(mEditText.getText().toString().trim());
        } else if (view.getId() == R.id.rl_publish_core_presenter_info) {
            /**
             * 点击主播头像
             */
            mPresenter.getUserInfo(presenterUser.usid);
        } else if (view.getId() == R.id.ib_publish_core_gift_send) {
            /**
             * 点击礼物图标
             */

            if (currentView == PUBLISH_VIEW) {//主播端
                mPresenter.showshare(presenterUser, getActivity());
                return;
            } else if (currentView == LIVE_VIEW) {//观众端
                hidekeyboard();//隐藏软键盘
                mGiftSheet.toggle();
            }
        } else if (view.getId() == R.id.ib_publish_core_gift) {
            /**
             * 弹出礼物匡
             */
            isVote = false;
            /**
             * 清除上次选择状态,选中当前
             */
            if (lastposiotn != -1) {
                list.get(lastposiotn).isChecked = false;
                lastposiotn = -1;
            }
            vpd.setSendBtText("送出");
            vpd.setSendBtEnable(false);
            mGiftSheet.toggle();
        } else if (view.getId() == R.id.rl_publish_core_present_giftrank) {
            /**
             * 跳转到礼物排行榜
             */
            Intent to = new Intent(getActivity(), ZBLGoldRankActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(ZBLGoldRankActivity.KEY_MUID, presenterUser.usid);
            to.putExtras(bundle);
            startActivity(to);
        } else if (view.getId() == R.id.ib_publish_core_share) {
            /**
             * 分享
             */
            mPresenter.showshare(presenterUser, getActivity());
        } else if (view.getId() == R.id.iv_camear_change) {
            /**
             * 切换摄像头
             */
            mPublishView.switchCamera();
        } else if (view.getId() == R.id.ib_publish_core_receive_list) {

        } else if (view.getId() == R.id.ib_publish_core_poll) {
            if (!mPresenter.isJoinedChatRoom()) {
                UiUtils.SnackbarText("服务连接中，暂不能发起投票，请稍后再试");
                return;
            }
            if (voteCreatePop != null)//发起投票弹窗
            {
                voteCreatePop.show(mRootView);
            }
        } else if (view.getId() == R.id.ib_publish_core_filter) {
            mFilterPop.show();
        }
    }


}


