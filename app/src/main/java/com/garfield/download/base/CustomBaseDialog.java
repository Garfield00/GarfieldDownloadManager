package com.garfield.download.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import com.garfield.download.R;

/**
 * Author chunliangwang
 * Date 2017/9/22
 * Description 通用Dialog的继承类
 */
public class CustomBaseDialog extends Dialog implements OnClickListener {
    public CustomBaseDialog(Context context) {
        super(context, R.style.custom_base_dialog);

        if (!delayInitDialogAttrs()) {
            initDialogAttrs(context);
        }
    }

    protected boolean isDetached=false;
    public CustomBaseDialog(Context context, int theme) {
        super(context, theme);

        if (!delayInitDialogAttrs()) {
            initDialogAttrs(context);
        }
    }

    protected boolean delayInitDialogAttrs() {
        return false;
    }
    /**
     * 初始化Dialog的属性
     */
    protected void initDialogAttrs(Context context){
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        if (context instanceof Activity){
            setOwnerActivity((Activity) context);
        }
        if(getWindow()==null) return;
        getWindow().getAttributes().width = getWidth();
        getWindow().getAttributes().height = getHeight();
        getWindow().getAttributes().x = 0;
        getWindow().getAttributes().y = 0;
        getWindow().setGravity(getGravity());
        getWindow().setAttributes(getWindow().getAttributes());
    }

    protected int getWidth() {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }

    protected int getHeight() {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }

    protected int getGravity() {
        return Gravity.CENTER_VERTICAL;
    }

    @Override
    public void onBackPressed() {
        if (mOnBackPressedListener != null&&mOnBackPressedListener.onBackPressed()) return;
        super.onBackPressed();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isDetached=true;
        if (mAttachListener != null) {
            mAttachListener.onDetachedFromWindow();
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mAttachListener != null) {
            mAttachListener.onAttachedToWindow();
        }
    }

    @Override
    public void onClick(View v) {}

    private AttachListener mAttachListener;
    public void setAttachListener(AttachListener attachListener) {
        this.mAttachListener=attachListener;
    }
    public interface AttachListener {
        void onAttachedToWindow();
        void onDetachedFromWindow();
    }

    private OnBackPressedListener mOnBackPressedListener;
    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener){
        this.mOnBackPressedListener=onBackPressedListener;
    }

    public interface OnBackPressedListener{
        boolean onBackPressed();
    }

}
