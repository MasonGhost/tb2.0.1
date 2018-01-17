package com.zhiyicx.thinksnsplus.modules.chat.item;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMNormalFileMessageBody;
import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.hyphenate.util.TextFormater;
import com.zhiyicx.thinksnsplus.R;

import java.io.File;

/**
 * @author Catherine
 * @describe 文件item
 * @date 2018/1/17
 * @contact email:648129313@qq.com
 */

public class ChatRowFile extends ChatBaseRow{

    private TextView mTvFileName;
    private TextView mTvFileLength;

    public ChatRowFile(Context context, EMMessage message, int position, BaseAdapter adapter, ChatUserInfoBean userInfoBean) {
        super(context, message, position, adapter, userInfoBean);
    }

    @Override
    protected void onInflateView() {
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                R.layout.item_chat_list_receive_file : R.layout.ease_row_sent_file, this);
    }

    @Override
    protected void onFindViewById() {
        super.onFindViewById();
        mTvFileName = (TextView) findViewById(R.id.tv_file_name);
        mTvFileLength = (TextView) findViewById(R.id.tv_file_length);
    }

    @Override
    protected void onSetUpView() {
        super.onSetUpView();
        EMNormalFileMessageBody fileMessageBody = (EMNormalFileMessageBody) message.getBody();
        String filePath = fileMessageBody.getLocalUrl();
        mTvFileName.setText(fileMessageBody.getFileName());
        mTvFileLength.setText(TextFormater.getDataSize(fileMessageBody.getFileSize()));
//        if (message.direct() == EMMessage.Direct.RECEIVE) {
//            File file = new File(filePath);
//            if (file.exists()) {
//                mTvFileLength.setText(com.hyphenate.easeui.R.string.Have_downloaded);
//            } else {
//                mTvFileLength.setText(com.hyphenate.easeui.R.string.Did_not_download);
//            }
//        }
    }

    @Override
    protected void onViewUpdate(EMMessage msg) {
        super.onViewUpdate(msg);
    }
}
