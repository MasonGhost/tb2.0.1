package com.zhiyicx.thinksnsplus.modules.findsomeone.contacts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.recycleview.stickygridheaders.StickyHeaderGridLayoutManager;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.ContactsContainerBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @Describe 通讯录
 * @Author Jungle68
 * @Date 2017/1/9
 * @Contact master.jungle68@gmail.com
 */
public class ContactsFragment extends TSFragment<ContactsContract.Presenter> implements ContactsContract.View, ContactsAdapter.OnItemClickListener {

    private static final int SPAN_SIZE = 1;

    @BindView(R.id.rv_contacts)
    RecyclerView mRvTagClass;

    private StickyHeaderGridLayoutManager mTagClassLayoutManager;

    private List<ContactsContainerBean> mListData = new ArrayList<>();

    private ContactsAdapter mTagClassAdapter;


    /**
     * 通讯录
     */
    public static void startToEditTagActivity(Context context) {

        Intent intent = new Intent(context, ContactsActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, 100);
        } else {
            throw new IllegalAccessError("context must instance of Activity");
        }
    }


    public static ContactsFragment newInstance(Bundle bundle) {
        ContactsFragment editUserTagFragment = new ContactsFragment();
        editUserTagFragment.setArguments(bundle);
        return editUserTagFragment;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_contacts;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.contacts);
    }

    @Override
    protected int setToolBarBackgroud() {
        return R.color.white;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected boolean setUseCenterLoading() {
        return true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    protected void initView(View rootView) {
        updateChooseTip();
        initRvTagClass();
        initListener();


    }

    @Override
    public void showLoading() {
        showLoadingView();
    }

    @Override
    public void hideLoading() {
        closeLoadingView();
    }

    private void updateChooseTip() {
    }


    @Override
    protected void initData() {
        mPresenter.getContacts();
    }


    private void initRvTagClass() {
        mTagClassLayoutManager = new StickyHeaderGridLayoutManager(SPAN_SIZE);
        mTagClassLayoutManager.setHeaderBottomOverlapMargin(getResources().getDimensionPixelSize(R.dimen.spacing_small));
        mTagClassLayoutManager.setSpanSizeLookup(new StickyHeaderGridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int section, int position) {
                return 1;
            }
        });

        // Workaround RecyclerView limitation when playing remove animations. RecyclerView always
        // puts the removed item on the top of other views and it will be drawn above sticky header.
        // The only way to fix this, abandon remove animations :(
        mRvTagClass.setItemAnimator(new DefaultItemAnimator() {
            @Override
            public boolean animateRemove(RecyclerView.ViewHolder holder) {
                dispatchRemoveFinished(holder);
                return false;
            }
        });
        mRvTagClass.setLayoutManager(mTagClassLayoutManager);
        mTagClassAdapter = new ContactsAdapter(mListData);
        mTagClassAdapter.setOnItemClickListener(this);
        mRvTagClass.setAdapter(mTagClassAdapter);

    }

    private void initListener() {


    }


    @Override
    public void onItemClick(int categoryPosition, int tagPosition) {

    }

    @Override
    public void updateContacts(List<ContactsContainerBean> data) {
        mListData.clear();
        mListData.addAll(data);
        mTagClassAdapter.notifyAllSectionsDataSetChanged();
    }
}
