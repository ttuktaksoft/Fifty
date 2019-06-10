package fifty.fiftyhouse.com.fifty;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class DialogFunc {
    private static DialogFunc _Instance;
    public static DialogFunc getInstance() {
        if (_Instance == null)
            _Instance = new DialogFunc();

        return _Instance;
    }
    private DialogFunc() {
    }

    private AppCompatDialog mProgressDialog = null;

    public interface MsgPopupListener {
        void Listener();
    }

    public void ShowMsgPopup(Context context, String title, String centerDesc) {
        ShowMsgPopup(context, null, null, title, centerDesc, null, null);
    }

    public void ShowMsgPopup(Context context, final MsgPopupListener listenerYes, final MsgPopupListener listenerNo, String title, String centerDesc, String yesDesc, String noDesc) {
        TextView Title, CenterDesc;
        ImageView YesButton, NoButton;
        TextView YesButtonDesc, NoButtonDesc;

        View v = LayoutInflater.from(context).inflate(R.layout.dialog_msg_popup, null, false);

        Title = (TextView) v.findViewById(R.id.tv_Msg_Popup_Title);
        CenterDesc = (TextView) v.findViewById(R.id.tv_Msg_Popup_Desc);
        YesButton = v.findViewById(R.id.iv_Msg_Popup_Buttons_OK);
        YesButtonDesc = v.findViewById(R.id.tv_Msg_Popup_Buttons_OK);
        NoButton =  v.findViewById(R.id.iv_Msg_Popup_Buttons_Cancel);
        NoButtonDesc =  v.findViewById(R.id.tv_Msg_Popup_Buttons_Cancel);

        ImageViewCompat.setImageTintList(YesButton, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.button_ok)));
        ImageViewCompat.setImageTintList(NoButton, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.button_cancel)));
        Title.setText(title);
        CenterDesc.setText(centerDesc);


        final AlertDialog dialog = new AlertDialog.Builder(context).setView(v).create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        YesButton.setVisibility(View.VISIBLE);
        YesButtonDesc.setVisibility(View.VISIBLE);
        NoButton.setVisibility(View.VISIBLE);
        NoButtonDesc.setVisibility(View.VISIBLE);

        if(noDesc == null || noDesc.equals(""))
        {
            NoButton.setVisibility(View.GONE);
            NoButtonDesc.setVisibility(View.GONE);
        }

        if(yesDesc == null)
            YesButtonDesc.setText(CommonFunc.getInstance().getStr(context.getResources(), R.string.MSG_OK));
        else
            YesButtonDesc.setText(yesDesc);

        if(noDesc == null)
            NoButtonDesc.setText(CommonFunc.getInstance().getStr(context.getResources(), R.string.MSG_CANCEL));
        else
            NoButtonDesc.setText(noDesc);

        YesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listenerYes != null)
                    listenerYes.Listener();
                dialog.dismiss();
            }
        });
        NoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listenerNo != null)
                    listenerNo.Listener();
                dialog.dismiss();
            }
        });
    }

    public void ShowSignUpCompletePopup(Context context, final MsgPopupListener listenerYes) {
        ImageView YesButton;

        View v = LayoutInflater.from(context).inflate(R.layout.dialog_signup_complete_popup, null, false);

        YesButton = v.findViewById(R.id.iv_SignUp_Com_Popup_Buttons_OK);
        ImageViewCompat.setImageTintList(YesButton, ColorStateList.valueOf(ContextCompat.getColor(context, R.color.button_ok)));

        final AlertDialog dialog = new AlertDialog.Builder(context).setView(v).create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        YesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listenerYes != null)
                    listenerYes.Listener();
                dialog.dismiss();
            }
        });
    }

    public void ShowLoadingPage(Context context) {

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            SetShowLoadingPageMsg(context);
        } else {

            mProgressDialog = new AppCompatDialog(context);
            mProgressDialog.setCancelable(false);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mProgressDialog.setContentView(R.layout.dialog_default_loading);
            mProgressDialog.show();

        }

        TextView tv_progress_message = (TextView) mProgressDialog.findViewById(R.id.tv_progress_message);



        ImageView iv_progress_loading = (ImageView) mProgressDialog.findViewById(R.id.iv_loading_logo);
        Glide.with(context)
                .asGif()
                .load(R.raw.progressbar)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(iv_progress_loading);


        //tv_progress_message.setText(message);
    }

    public void SetShowLoadingPageMsg(Context context) {

        if (mProgressDialog == null || !mProgressDialog.isShowing()) {
            return;
        }
        TextView tv_progress_message = (TextView) mProgressDialog.findViewById(R.id.tv_progress_message);


        ImageView iv_progress_loading = (ImageView) mProgressDialog.findViewById(R.id.iv_loading_logo);
        Glide.with(context)
                .asGif()
                .load(R.raw.progressbar)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(iv_progress_loading);
        //tv_progress_message.setText(message);
    }

    public void DismissLoadingPage() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

}
