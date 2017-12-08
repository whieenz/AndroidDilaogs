package com.whieenz.androiddialog.base;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whieenz.androiddialog.R;


/**
 * Created by heziwen on 2017/8/8.
 * Dialog 基类
 */

public class BaseDialog extends Dialog {

    TextView tv_title;

    Activity context;
    String title;
    LinearLayout close_layout;
    ImageButton imb_closeBtn;
    LinearLayout contentLayout;
    OnDialogMultiClickListener onDialogMultiClickListener;
    OnDialogSingleClickListener onDialogSingleClickListener;
    Button btn_no;
    Button btn_yes;

    /**
     * 构造方法
     *
     * @param context 只接受Activity的context
     */
    BaseDialog(Activity context) {
        super(context, R.style.hzwDialog);
        init(context);
        setLayoutParams();
    }

    private void init(Activity context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_base, null);
        tv_title = (TextView) view.findViewById(R.id.tv_dialog_title);
        imb_closeBtn = (ImageButton) view.findViewById(R.id.imb_base_close);
        close_layout = (LinearLayout) view.findViewById(R.id.id_hzwdialog_close_layout);
        contentLayout = (LinearLayout) view.findViewById(R.id.dialog_content);
        btn_no = (Button) view.findViewById(R.id.btn_dialog_request_no);
        btn_yes = (Button) view.findViewById(R.id.btn_dialog_request_yes);
        btn_no.setText("否");
        btn_yes.setText("是");
        super.setContentView(view);
    }

    /**
     * 默认的对话框尺寸
     * height wrap_content
     * width  占手机屏幕的90%
     * gravity 居中显示
     *
     * @return
     */
    private void setLayoutParams() {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        WindowManager manager = context.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        lp.gravity = Gravity.CENTER;
        lp.width = (int) (width * 0.9);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) close_layout.getLayoutParams();
        layoutParams.topMargin = (int) (width * 0.13);
        close_layout.setLayoutParams(layoutParams);
        this.getWindow().setAttributes(lp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(title);
        imb_closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDialogSingleClickListener != null) {
                    onDialogSingleClickListener.onClose();
                }
                dismiss();
            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDialogMultiClickListener != null){
                    onDialogMultiClickListener.onCancel();
                }
                dismiss();
            }
        });
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDialogMultiClickListener != null){
                    onDialogMultiClickListener.onYes();
                }
                dismiss();
            }
        });
    }

    /**
     * 设置Dialog对话框尺寸大小
     *
     * @param X 宽度比
     * @param Y 高度比
     */
    public void setSizeByScale(double X, double Y) {
        if (X < 0 || X > 1 || Y < 0 || Y > 1) {
            return;
        }
        Window window = this.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        WindowManager manager = context.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;
        lp.gravity = Gravity.CENTER;
        if (X == 1) {
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        } else {
            lp.width = (int) (width * X);
        }
        if (Y == 1) {
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        } else {
            lp.height = (int) (height * Y);
        }
        this.getWindow().setAttributes(lp);
    }

    protected void addContentView(View child) {
        contentLayout.addView(child);
    }

    public void setTitle(String title) {
        this.title = title;
        tv_title.setText(title);
    }

    public BaseDialog setOnDialogMultiClickListener(OnDialogMultiClickListener onDialogClickListener) {
        this.onDialogMultiClickListener = onDialogClickListener;
        return this;
    }

    public BaseDialog setOnDialogSingleClickListener(OnDialogSingleClickListener onDialogClickListener) {
        this.onDialogSingleClickListener = onDialogClickListener;
        return this;
    }
    public BaseDialog setBottomLayout(boolean isShow) {
        if (isShow) {
            close_layout.setVisibility(View.VISIBLE);
        } else {
            close_layout.setVisibility(View.GONE);
        }
        return this;
    }

    public interface OnDialogSingleClickListener {
        void onClose();
    }

    public interface OnDialogMultiClickListener {
        void onCancel();

        void onYes();
    }
}
