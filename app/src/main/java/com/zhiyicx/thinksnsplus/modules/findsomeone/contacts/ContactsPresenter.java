package com.zhiyicx.thinksnsplus.modules.findsomeone.contacts;

import com.github.tamir7.contacts.Contact;
import com.github.tamir7.contacts.Contacts;
import com.github.tamir7.contacts.PhoneNumber;
import com.github.tamir7.contacts.Query;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.ContactsBean;
import com.zhiyicx.thinksnsplus.data.beans.ContactsContainerBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/7/10
 * @Contact master.jungle68@gmail.com
 */

public class ContactsPresenter extends BasePresenter<ContactsContract.Repository, ContactsContract.View> implements ContactsContract.Presenter {


    @Inject
    UserInfoRepository mUserInfoRepository;
    @Inject
    public ContactsPresenter(ContactsContract.Repository repository, ContactsContract.View rootView) {
        super(repository, rootView);
    }


    @Override
    public void getContacts() {

        Observable.create((Observable.OnSubscribe<List<Contact>>) subscriber -> {
            if (!subscriber.isUnsubscribed()) {
                try {
                    // 获取有电话号码的联系人
                    Query q = Contacts.getQuery();
                    q.hasPhoneNumber();
                    List<Contact> contacts = q.find();
                    subscriber.onNext(contacts);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(contacts -> {
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
                    System.out.println("Thread.currentThread() = " + Thread.currentThread());
                    return reuslt;
                })
                .flatMap(new Func1<List<ContactsBean>, Observable<List<ContactsContainerBean>>>() {
                    @Override
                    public Observable<List<ContactsContainerBean>> call(List<ContactsBean> contacts) {
                        ArrayList<String> phones =new ArrayList<>();
                        for (ContactsBean contact : contacts) {
                            phones.add(contact.getPhone());
                        }
                     return    mUserInfoRepository.getUsersByPhone(phones)
                                .map(new Func1<List<UserInfoBean>, List<ContactsContainerBean>>() {
                                    @Override
                                    public List<ContactsContainerBean> call(List<UserInfoBean> userInfoBeen) {

                                        return null;
                                    }
                                });

                    }
                })
                .subscribe(new BaseSubscribeForV2<List<ContactsContainerBean>>() {
                    @Override
                    protected void onSuccess(List<ContactsContainerBean> data) {

                    }
                });
    }
}