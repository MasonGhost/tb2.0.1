package com.zhiyicx.thinksnsplus.modules.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.InputLimitView;
import com.zhiyicx.baseproject.widget.chat.ChatMessageList;
import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.thinksnsplus.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/01/06
 * @Contact master.jungle68@gmail.com
 */
public class ChatFragment extends TSFragment<ChatContract.Presenter> implements ChatContract.View {
    public static final String BUNDLE_USERID = "userId";

    protected List<Message> mDatas;
    @BindView(R.id.message_list)
    ChatMessageList mMessageList;
    @BindView(R.id.ilv_container)
    InputLimitView mIlvContainer;

    private String mTochatUsreId;// 聊天对方用户的id

    public static ChatFragment newInstance(String userId) {

        Bundle args = new Bundle();
        args.putString(BUNDLE_USERID, userId);
        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_chat;
    }

    @Override
    protected int setToolBarBackgroud() {
        return R.color.white;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.message);
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initData() {
        mDatas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Message message = new Message();
            message.setMid(System.currentTimeMillis());
            message.setId(i);
            message.setCreate_time(System.currentTimeMillis());
            message.setTxt("测试消息，我的看了个的空间广阔疯狂的疯狂付款的流沙看到了 " + i);
            if(i%2==0){
                message.setType(-1);
            }
            mDatas.add(message);
        }
        mMessageList.init("张三", 0, mDatas);

    }

    @Override
    public void setPresenter(ChatContract.Presenter presenter) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
