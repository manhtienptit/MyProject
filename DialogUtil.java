package com.jingle.firebase.plugin.online.util;

/**
 * Created by manhtien on 4/22/16.
 */
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jingle.firebase.plugin.online.JingleOnlineService;
import com.jingle.firebase.plugin.online.R;
import com.jingle.firebase.plugin.online.interfaces.JingleDialogCallbackListener;
//import com.google.firebase.quickstart.database.R;


public class DialogUtil {

    private static final int PROGRESS_TIMEOUT = 20000;

    private static volatile Dialog progressDialog;
    private static volatile Dialog progressDialogNetwork;
    private static Toast toast;
    private static JingleDialogCallbackListener dialogCallback;
    private static JingleDialogCallbackListener dialogCallbackNetwork;
    private static Handler progressDismissTimer;
    private static Handler progressDismissTimerWaitInHall;
    private static Handler progressDismissTimerWait;
    private static Handler progressDismissTimerNetwork;

    private static Runnable progressDismissTask = new Runnable() {
        public void run() {
            dialogCallback = null;
            progressDialog.dismiss();
            JingleOnlineService.isCheckCreatRoom = false;
        }
    };

    private static Runnable progressDismissTaskWaitInHome = new Runnable() {
        public void run() {
            progressDismissTimerWaitInHall = null;
            progressDialog.dismiss();
        }
    };

    private static Runnable progressDismissTaskWait = new Runnable() {
        public void run() {
            progressDialog.dismiss();
            dialogCallback.OnTimeOutDialogFinish(1);
            dialogCallback = null;

        }
    };

    private static Runnable progressDismissTaskNetwork  = new Runnable() {
        public void run() {
            progressDialogNetwork.dismiss();
            JingleOnlineService.callbackDialog.OnTimeOutDialogFinish(2);
//            dialogCallbackNetwork.OnTimeOutDialogFinish(2);
//            dialogCallbackNetwork = null;

        }
    };




    public static void showErrorMessage(Context ctx, String msg, int drawable ) {
        showToast(ctx, msg, Toast.LENGTH_LONG, drawable);
    }

//    public static void showErrorMessage(Context ctx, String msg, int duration) {
//        showToast(ctx, msg, duration, R.drawable.ic_image_edit);
//    }
//
//    public static void showWarningMessage(Context ctx, String msg) {
//        showToast(ctx, msg, Toast.LENGTH_LONG, R.drawable.ic_image_edit);
//    }

    public static void showWaitingDialog(Context context, JingleDialogCallbackListener jdcl) {
        showWaitHallDialog(context, "Loading...",jdcl);
    }

    public static void showProgressDialog(Context context) {
        showProgressDialog(context, "Loading...");
    }

    public static void showWaitFriendDialog(Context context) {
        showProgressDialog(context, "Waitting Opponent...");
    }

    public static void showWaitHostDialog(Context context, JingleDialogCallbackListener jdcl) {
        showProgressWaitDialog(context, "Waitting Opponent...", jdcl);
    }

    public static void showToast(Context ctx, String msg, int lengthConstant, int iconResource) {

        if (toast == null) {
            toast = Toast.makeText(ctx, msg, lengthConstant);
            toast.setGravity(Gravity.CENTER, 0, -30);
            LinearLayout toastView = (LinearLayout) toast.getView();
            toastView.setOrientation(LinearLayout.HORIZONTAL);
            toastView.setGravity(Gravity.CENTER);
            toastView.setPadding(18, 14, 18, 22);
            Bitmap iconImage = BitmapFactory.decodeResource(ctx.getResources(), iconResource);
            ImageView iconView = new ImageView(ctx);
            iconView.setImageBitmap(iconImage);
            iconView.setPadding(0, 0, 6, 0);
            toastView.addView(iconView, 0);
        } else {
            toast.setText(msg);
            toast.setDuration(lengthConstant);
        }
        toast.show();
    }

    public static void showWaitHallDialog(Context context, String msg, JingleDialogCallbackListener jdcl) {
        synchronized (DialogUtil.class) {
            if(dialogCallback == null){
                dialogCallback = jdcl;
            }
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.hide();
            }
            progressDialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
            progressDialog.setContentView(R.layout.dialog_waitting);
            progressDialog.setTitle(msg);
            progressDialog.setCancelable(false);
            progressDialog.show();
            setProgressDismissTimerWaitInHall(5000);
        }
    }

    public static void showWaitDialog(Context context, String msg, JingleDialogCallbackListener jdcl) {
        synchronized (DialogUtil.class) {
            if(dialogCallback == null){
                dialogCallback = jdcl;
            }
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.hide();
            }
            progressDialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
            progressDialog.setContentView(R.layout.dialog_waitting);
            progressDialog.setTitle(msg);
            progressDialog.setCancelable(false);
            progressDialog.show();
            setProgressDismissTimerWaitInHall(30000);
        }
    }

    public static void showProgressWaitDialog(Context context, String msg, JingleDialogCallbackListener jdcl) {
        synchronized (DialogUtil.class) {
            if(dialogCallback == null){
                dialogCallback = jdcl;
            }
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.hide();
//                return;
            }
            progressDialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
            progressDialog.setContentView(R.layout.dialog_waitting);
//            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialog.setTitle(msg);
            progressDialog.setCancelable(false);
//            progressDialog.setMax(100);
            progressDialog.show();
            setProgressDismissTimerWait(10000);
        }
    }

    public static void showProgressDialog(Context context, String msg) {
        synchronized (DialogUtil.class) {
            if (progressDialog != null && progressDialog.isShowing()) {
                  progressDialog.hide();
            }
            progressDialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
            progressDialog.setContentView(R.layout.dialog_waitting);
            progressDialog.setTitle(msg);
            progressDialog.setCancelable(false);
            progressDialog.show();
            setProgressDismissTimer(PROGRESS_TIMEOUT);
        }
    }

    public static void showProgressDialogConnectNetwork(Context context, String msg) {
        synchronized (DialogUtil.class) {
//            if(dialogCallback == null){
//            dialogCallbackNetwork = jdcl;
//            }
            if (progressDialogNetwork != null && progressDialogNetwork.isShowing()) {
                progressDialogNetwork.hide();
            }
            progressDialogNetwork = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
            progressDialogNetwork.setContentView(R.layout.dialog_connect_network);
            ImageView iv = (ImageView) progressDialogNetwork.findViewById(R.id.imageView2);
            Animation myFadeInAnimation = AnimationUtils.loadAnimation(context, R.anim.alpha);
            iv.setAnimation(myFadeInAnimation);
            progressDialogNetwork.setTitle(msg);
            progressDialogNetwork.setCancelable(false);
            progressDialogNetwork.show();
            setProgressDismissNetwork(10000);
        }
    }

    public static void showProgressDialog(Context context, String msg, long timeoutMillis) {
        synchronized (DialogUtil.class) {
            if (progressDialog != null && progressDialog.isShowing()) {
                return;
            }
            progressDialog = new ProgressDialog(context, android.R.style.Theme_Translucent_NoTitleBar);
            progressDialog.setTitle(msg);
            progressDialog.setCancelable(false);
//            progressDialog.setMax(100);
            progressDialog.show();
            setProgressDismissTimer(timeoutMillis);
        }
    }

    private static void setProgressDismissTimer(long timeoutMillis) {
        if (progressDismissTimer == null) {
            progressDismissTimer = new Handler();
        }
        progressDismissTimer.postDelayed(progressDismissTask, timeoutMillis);
    }

    private static void setProgressDismissTimerWaitInHall(long timeoutMillis) {
        if (progressDismissTimerWaitInHall == null) {
            progressDismissTimerWaitInHall = new Handler();
        }
        progressDismissTimerWaitInHall.postDelayed(progressDismissTaskWaitInHome, timeoutMillis);
    }
    private static void setProgressDismissTimerWait(long timeoutMillis) {
        if (progressDismissTimerWait == null) {
            progressDismissTimerWait = new Handler();
        }
        progressDismissTimerWait.postDelayed(progressDismissTaskWait, timeoutMillis);
    }

    private static void setProgressDismissNetwork(long timeoutMillis) {
        if (progressDismissTimerNetwork == null) {
            progressDismissTimerNetwork = new Handler();
        }
        progressDismissTimerNetwork.postDelayed(progressDismissTaskNetwork, timeoutMillis);
    }

    private static void cancelProgressDismissTimer() {
        if(progressDismissTimer != null)
        progressDismissTimer.removeCallbacks(progressDismissTask);
    }

    private static void cancelProgressDismissTimerWait() {
        if(progressDismissTimerWait != null)
        progressDismissTimerWait.removeCallbacks(progressDismissTaskWait);
    }

    private static void cancelProgressDismissTimerNetwork() {
        if(progressDismissTimerNetwork != null)
            progressDismissTimerNetwork.removeCallbacks(progressDismissTaskNetwork);
    }

    private static void cancelProgressDismissHall() {
        if(progressDismissTimerWaitInHall != null)
            progressDismissTimerWaitInHall.removeCallbacks(progressDismissTaskWaitInHome);
    }

    public static void hideProgressDialog() {
        synchronized (DialogUtil.class) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                cancelProgressDismissTimer();
                cancelProgressDismissTimerWait();
            }
        }
    }
    public static void hideProgressDialogNetwork() {
        synchronized (DialogUtil.class) {
            if (progressDialogNetwork != null && progressDialogNetwork.isShowing()) {
                progressDialogNetwork.dismiss();
                cancelProgressDismissTimerNetwork();
            }
        }
    }

    public static void hideProgressDialogHall() {
        synchronized (DialogUtil.class) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                cancelProgressDismissHall();
            }
        }
    }

//    /**
//     * A convenient method to display the error dialog when given a list of
//     * errors
//     *
//     * @param ctx
//     * @param errors
//     */
//    public static void showNetworkErrors(Context ctx, String err, int dr ) {
//
//        if (err != null && !err.isEmpty()) {
//            StringBuilder errorMsg = new StringBuilder();
//            errorMsg.append(err);
//
//            showErrorMessage(ctx, errorMsg.toString(), dr);
//        }
//    }

    public static void showProgressDialog(final Context ctx, String msg, long timeout, Runnable timeoutAction) {
        showProgressDialog(ctx, msg);
        Handler handler = new Handler();
        handler.postDelayed(timeoutAction, timeout);
    }

    public static void showSingleChoiceDialog(final Context ctx, String title, String[] choices,
                                              OnClickListener choiceListener) {

        Builder builder = new Builder(ctx);
        builder.setTitle(title);
        builder.setItems(choices, choiceListener);
        builder.show();
    }

    public static void showMultiChoiceDialog(final Context ctx, String title, String[] choices, View additionalView,
                                             OnClickListener okListener, DialogInterface.OnMultiChoiceClickListener choiceListener) {

        Builder builder = new Builder(ctx);
        builder.setTitle(title);
        builder.setPositiveButton("OK", okListener);
        builder.setNegativeButton("Cancel", new OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });

        if (additionalView != null) {
            builder.setView(additionalView);
        }

        if (choiceListener != null) {
            builder.setMultiChoiceItems(choices, null, choiceListener);
        }

        builder.show();
    }

    public static void showInfoMessage(Context ctx, String msg) {
        showInfoMessage(ctx, msg, Toast.LENGTH_LONG);
    }

    public static void showInfoMessage(final Context ctx, String msg, int duration) {
        Toast.makeText(ctx, msg, duration).show();
    }

    private static Builder createDialog(final Activity act, String title, int icon, String msg, final boolean finishContext) {
        Builder builder = new Builder(act);
        builder.setMessage(msg);
        builder.setTitle(title);
        if (icon != -1) {
            builder.setIcon(icon);
        }
        builder.setPositiveButton("OK", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (finishContext) {
                    act.finish();
                }
            }
        });
        return builder;
    }

    private static Builder createDialog(final Activity act, String title, int icon, String msg, OnClickListener positiveListener) {
        Builder builder = new Builder(act);
        builder.setMessage(msg);
        builder.setTitle(title);
        if (icon != -1) {
            builder.setIcon(icon);
        }
        builder.setPositiveButton("OK", positiveListener);
        return builder;
    }

//    public static Builder createInfoDialog(final Activity act, String msg, final boolean finishContext) {
//        Builder dialog = createDialog(act, "Information", R.drawable.ic_image_edit, msg, finishContext);
//        return dialog;
//    }

//    public static Builder createWarningDialog(final Activity act, String msg, final boolean finishContext) {
//        Builder dialog = createDialog(act, "Warning", R.drawable.ic_image_edit, msg, finishContext);
//        return dialog;
//    }

//    public static Builder createErrorDialog(final Activity act, String msg, final boolean finishContext) {
//        Builder dialog = createDialog(act, "Error", R.drawable.ic_image_edit, msg, finishContext);
//        return dialog;
//    }

//    public static void showInfoDialog(final Activity act, String msg, final boolean finishContext) {
//        hideProgressDialog();
//        Builder dialog = createInfoDialog(act, msg, finishContext);
//        dialog.show();
//    }

//    public static void showWarningDialog(final Activity act, String title, String msg, final boolean finishContext) {
//        hideProgressDialog();
//        Builder dialog = createDialog(act, title, R.drawable.ic_image_edit, msg, finishContext);
//        dialog.show();
//    }
//
//    public static void showErrorDialog(final Activity act, String msg, final boolean finishContext) {
//        hideProgressDialog();
//        Builder dialog = createDialog(act, "Error", R.drawable.ic_image_edit, msg, finishContext);
//        dialog.show();
//    }

//    public static void showInfoDialog(final Activity act, String msg) {
//        showInfoDialog(act, msg, false);
//    }

//    public static void showWarningDialog(final Activity act, String msg) {
//        showWarningDialog(act, "Warning", msg, false);
//    }
//
//    public static void showErrorDialog(final Activity act, String msg) {
//        showErrorDialog(act, msg, false);
//    }
//
//    public static AlertDialog showInfoDialog(Activity act, String msg, OnClickListener positiveListener) {
//        Builder dialog = createDialog(act, "Info", R.drawable.ic_image_edit, msg, positiveListener);
//        return dialog.show();
//    }
}
