package com.garfield.download.dialog.builder;

import android.content.Context;
import com.garfield.download.R;
import com.garfield.download.dialog.TipDialog;

/**
 * Author chunliangwang
 * Date 2017/9/22
 * Description 提示弹窗Builder
 */

public class TipDialogBuilder {
    private Context mContext;

    private String mTitle;
    private String mContent;
    private TipDialog.TipDialogListener mListener;
    private String mSureText;
    private String mCancelText;
    private TipDialogBuilder(Context context) {
        this.mContext = context;

        mSureText = context.getString(R.string.tip_sure);
        mCancelText = context.getString(R.string.tip_sure);
    }

    public static TipDialogBuilder newInstance(Context context) {
        return new TipDialogBuilder(context);
    }

    public TipDialogBuilder setContext(Context context) {
        this.mContext = context;
        return this;
    }

    public TipDialogBuilder setTitle(String title) {
        this.mTitle = title;
        return this;
    }

    public TipDialogBuilder setContent(String content) {
        this.mContent = content;
        return this;
    }

    public TipDialogBuilder setListener(TipDialog.TipDialogListener listener) {
        this.mListener = listener;
        return this ;
    }

    public TipDialogBuilder setSureText(String sureText) {
        this.mSureText = sureText;
        return this;
    }

    public TipDialogBuilder setCancelText(String cancelText) {
        this.mCancelText = cancelText;
        return this;
    }

    public TipDialog create() {
        TipDialog dlg = new TipDialog(mContext);
        dlg.setTitle(mTitle);
        dlg.setContent(mContent);
        dlg.setDialogListener(mListener);
        dlg.setSureText(mSureText);
        dlg.setCancelText(mCancelText);
        return dlg;
    }
}
