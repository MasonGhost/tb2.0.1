package com.zhiyi.richtexteditorlib.view.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.zhiyi.richtexteditorlib.R;
import com.zhiyi.richtexteditorlib.view.dialogs.base.BaseDialogFragment;

/**
 * @Author Jliuer
 * @Date 2017/11/19/18:52
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class PictureHandleDialog extends BaseDialogFragment {
    public static final String Tag = "delete_dialog_fragment";
    private Long imageId;
    private CharSequence[] items;
    private OnDialogClickListener listener;

    public static PictureHandleDialog createDeleteDialog(Long imageId) {
        final PictureHandleDialog newDialog = new PictureHandleDialog();
        newDialog.setImageId(imageId);
        return newDialog;
    }

    public PictureHandleDialog() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (listener != null) {
                            switch (which) {
                                case 0:
                                    listener.onDeleteButtonClick(imageId);
                                    break;
                                case 1:
                                    listener.onReloadButtonClick(imageId);
                                    break;
                                default:
                            }
                        }

                    }
                })
                .setTitle(R.string.handles).create();
    }

    @SuppressWarnings("unused")
    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public void setListener(OnDialogClickListener listener) {
        this.listener = listener;
    }

    public void setItems(CharSequence[] items) {
        this.items = items;
    }

    public interface OnDialogClickListener {
        void onDeleteButtonClick(Long id);

        void onReloadButtonClick(Long id);
    }
}
