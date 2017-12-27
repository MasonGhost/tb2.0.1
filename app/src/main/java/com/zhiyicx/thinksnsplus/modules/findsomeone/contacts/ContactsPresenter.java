package com.zhiyicx.thinksnsplus.modules.findsomeone.contacts;

import android.text.TextUtils;

import com.github.tamir7.contacts.Contact;
import com.github.tamir7.contacts.Contacts;
import com.github.tamir7.contacts.PhoneNumber;
import com.github.tamir7.contacts.Query;
import com.zhiyicx.baseproject.base.SystemConfigBean;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.ContactsBean;
import com.zhiyicx.thinksnsplus.data.beans.ContactsContainerBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.SystemRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/7/10
 * @Contact master.jungle68@gmail.com
 */

public class ContactsPresenter extends AppBasePresenter<ContactsContract.View> implements ContactsContract.Presenter {


    @Inject
    UserInfoRepository mUserInfoRepository;
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;
    @Inject
    SystemRepository mSystemRepository;

    @Inject
    public ContactsPresenter(ContactsContract.View rootView) {
        super(rootView);
    }


    @Override
    public void getContacts() {
        mRootView.showLoading();
        Subscription subscription = Observable.create((Observable.OnSubscribe<List<ContactsBean>>) subscriber -> {
            if (!subscriber.isUnsubscribed()) {
                try {
                    // 获取有电话号码的联系人
                    Query q = Contacts.getQuery();
                    q.hasPhoneNumber();
                    List<Contact> contacts = q.find();
                    List<ContactsBean> reuslt = new ArrayList<>();
                    for (Contact contact : contacts) {
                        ContactsBean contactsBean = new ContactsBean();
                        List<PhoneNumber> phones = contact.getPhoneNumbers();
                        for (PhoneNumber phone : phones) {
                            if (phone.getType() == PhoneNumber.Type.MOBILE) {
                                String phoneStr = phone.getNumber();
                                phoneStr = phoneStr.replaceAll(" ", "");
                                phoneStr = phoneStr.replaceAll("-", "");
                                contactsBean.setPhone(phoneStr);
                                contactsBean.setContact(contact);
                                reuslt.add(contactsBean);
                                break;
                            }
                        }
                    }
                    subscriber.onNext(reuslt);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(contacts -> {
                    ArrayList<String> phones = new ArrayList<>();
                    for (ContactsBean contact : contacts) {
                        phones.add(contact.getPhone());
                    }
                    return mUserInfoRepository.getUsersByPhone(phones)
                            .observeOn(Schedulers.io())
                            .map(userInfoBeen -> {
                                mUserInfoBeanGreenDao.insertOrReplace(userInfoBeen);
                                List<ContactsContainerBean> contactsContainerBeens = new ArrayList<>();
                                ContactsContainerBean hadAdd = new ContactsContainerBean();
                                hadAdd.setContacts(new ArrayList<>());
                                hadAdd.setTitle(mContext.getString(R.string.contact_had_add_ts_plust));
                                ContactsContainerBean notAdd = new ContactsContainerBean();
                                notAdd.setContacts(new ArrayList<>());
                                notAdd.setTitle(mContext.getString(R.string.contact_not_add_ts_plust));
                                contactsContainerBeens.add(hadAdd);
                                contactsContainerBeens.add(notAdd);

                                for (ContactsBean contact : contacts) {
                                    List<UserInfoBean> tmp = mUserInfoBeanGreenDao.getUserInfoByPhone(contact.getPhone());
                                    if (!tmp.isEmpty()) {
                                        contact.setUser(tmp.get(0));
                                        hadAdd.getContacts().add(contact);
                                    } else {
                                        notAdd.getContacts().add(contact);
                                    }
                                }
                                return contactsContainerBeens;
                            });

                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<List<ContactsContainerBean>>() {
                    @Override
                    protected void onSuccess(List<ContactsContainerBean> data) {
                        mRootView.hideLoading();
                        mRootView.updateContacts((ArrayList<ContactsContainerBean>) data);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        LogUtils.e("contact load fail : " + message);
                        mRootView.hideLoading();
                        mRootView.updateContacts(new ArrayList<>());
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        LogUtils.e("contact load error : ");
                        mRootView.hideLoading();
                        mRootView.updateContacts(new ArrayList<>());
                    }
                });
        addSubscrebe(subscription);
    }


    @Override
    public void followUser(int index, UserInfoBean followFansBean) {
        mUserInfoRepository.handleFollow(followFansBean);

    }

    @Override
    public void cancleFollowUser(int index, UserInfoBean followFansBean) {
        mUserInfoRepository.handleFollow(followFansBean);
    }

    /**
     * @return 邀请模板
     */
    @Override
    public String getInviteSMSTip() {
        String tip = null;
        SystemConfigBean systemConfigBean = mSystemRepository.getBootstrappersInfoFromLocal();
        if (systemConfigBean != null && systemConfigBean.getSite() != null) {
            tip = systemConfigBean.getSite().getUser_invite_template();
        }
        if (TextUtils.isEmpty(tip)) {
            tip = mContext.getString(R.string.invite_friend);
        }
        return tip;
    }
}