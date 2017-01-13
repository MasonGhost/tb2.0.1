# 图片选择器


[github上的地址](https://github.com/donglua/PhotoPicker)

类似微信的图片选择器，从本地相册中单选或者多选图片，能够对图片进行预览

# 配置
在baseProject的AndroidManifest.xml中添加：图片选择界面和图片预览界面
```
    <activity android:name="me.iwf.photopicker.PhotoPickerActivity"
      android:theme="@style/Theme.AppCompat.NoActionBar"
       />

    <activity android:name="me.iwf.photopicker.PhotoPagerActivity"
      android:theme="@style/Theme.AppCompat.NoActionBar"/>
```

图片单张选择：多张选择只要设置PhotoCount
```
 // 选择相册，单张
                        PhotoPicker.builder()
                                .setPreviewEnabled(true)
                                .setGridColumnCount(3)
                                .setPhotoCount(1)
                                .setShowCamera(true)
                                .start(getActivity(), UserInfoFragment.this);
```

处理回调结果:
```
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            if ((requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {
                List<String> photos = null;
                if (data != null) {
                    photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                }
                ...
            }
     }
```

在PhotoPickerActivity和PhotoPagerActivity中，如有必要，我们可以对图片选择界面和预览界面进行调整

2017年1月12日14:17:22