package com.garfield.download.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.garfield.download.R;
import com.garfield.download.base.CustomBaseDialog;

/**
 * Author chunliangwang
 * Date 2018/4/12
 * Description 通用Dialog的继承类
 */

public class TipDialog extends CustomBaseDialog {
    private TextView txt_title;//标题
    private TextView txt_content;//内容

    private Button btn_cancel;//取消
    private Button btn_sure;//确定

    public TipDialog(Context context) {
        super(context, R.style.CustomDialog);
        setContentView(R.layout.dialog_tip);
        initViews();
    }


    private void initViews() {
        txt_title = findViewById(R.id.txt_title);
        txt_content = findViewById(R.id.txt_content);

        btn_cancel = findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);

        btn_sure = findViewById(R.id.btn_sure);
        btn_sure.setOnClickListener(this);
    }

    public void setTitle(String text) {
        if (txt_title == null) return;
        txt_title.setText(text);
        txt_title.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
    }

    public void setContent(String text) {
        if (txt_content == null) return;
        txt_content.setText(text);
        txt_content.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
    }

    public void setContentSize(float size) {
        if (txt_content != null) {
            txt_content.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        }
    }

    public void setCancelText(String text) {
        if (TextUtils.isEmpty(text)) return;
        btn_cancel.setText(text);
    }

    public void setSureText(String text) {
        if (TextUtils.isEmpty(text)) return;
        btn_sure.setText(text);
    }

    private TipDialogListener mDialogListener = null;
    public void setDialogListener(TipDialogListener listener) {
        mDialogListener = listener;
    }

    public interface TipDialogListener {
        void onConfirm(TipDialog dialog);
        void onCancel(TipDialog dialog);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_sure) {
            if (mDialogListener != null) {
                mDialogListener.onConfirm(this);
            }
            dismiss();
        } else if (id == R.id.btn_cancel) {
            if (mDialogListener != null) {
                mDialogListener.onCancel(this);
            }
            dismiss();
        }
    }

}
