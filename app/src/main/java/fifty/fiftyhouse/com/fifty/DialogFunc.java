package fifty.fiftyhouse.com.fifty;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.activty.SettingAccountActivity;
import fifty.fiftyhouse.com.fifty.adapter.DialogMenuListAdapter;
import fifty.fiftyhouse.com.fifty.adapter.SettingAdapter;
import fifty.fiftyhouse.com.fifty.util.OnRecyclerItemClickListener;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

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

    public void ShowToast(Context context, String msg, boolean shortView)
    {
        if(shortView)
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public interface MsgPopupListener {
        void Listener();
    }

    public void ShowMsgPopup(Context context, String centerDesc) {
        ShowMsgPopup(context, null, null, centerDesc, null, null);
    }

    public void ShowMsgPopup(Context context, final MsgPopupListener listenerYes, final MsgPopupListener listenerNo, String centerDesc, String yesDesc, String noDesc) {
        TextView CenterDesc;
        TextView YesButtonDesc, NoButtonDesc;

        View v = LayoutInflater.from(context).inflate(R.layout.dialog_msg_popup, null, false);

        CenterDesc = (TextView) v.findViewById(R.id.tv_Msg_Popup_Desc);
        YesButtonDesc = v.findViewById(R.id.tv_Msg_Popup_Buttons_OK);
        NoButtonDesc =  v.findViewById(R.id.tv_Msg_Popup_Buttons_Cancel);

        CenterDesc.setText(centerDesc);

        final AlertDialog dialog = new AlertDialog.Builder(context).setView(v).create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        GlobalApplication.getGlobalApplicationContext().SetContentFont(CenterDesc);
        GlobalApplication.getGlobalApplicationContext().SetMainMenuFont(YesButtonDesc, NoButtonDesc);

        YesButtonDesc.setVisibility(View.VISIBLE);
        NoButtonDesc.setVisibility(View.VISIBLE);

        if(noDesc == null || noDesc.equals(""))
        {
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

        YesButtonDesc.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                if (listenerYes != null)
                    listenerYes.Listener();
                dialog.dismiss();
            }
        });
        NoButtonDesc.setOnClickListener(new OnSingleClickListener(){
            @Override
            public void onSingleClick(View view) {
                if (listenerNo != null)
                    listenerNo.Listener();
                dialog.dismiss();
            }
        });
    }

    public void ShowSignUpCompletePopup(Context context, final MsgPopupListener listenerYes) {
        TextView YesButton;

        View v = LayoutInflater.from(context).inflate(R.layout.dialog_signup_complete_popup, null, false);

        YesButton = v.findViewById(R.id.tv_SignUp_Com_Popup_Buttons_OK);

        final AlertDialog dialog = new AlertDialog.Builder(context).setView(v).create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        GlobalApplication.getGlobalApplicationContext().SetMainMenuFont(YesButton);

        YesButton.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                dialog.dismiss();
            }
        });

        dialog.setOnDismissListener(

                new DialogInterface.OnDismissListener() {

                    @Override

                    public void onDismiss(DialogInterface dialogInterface) {
                        if (listenerYes != null)
                            listenerYes.Listener();
                    }

                });
    }

    public void ShowLoadingPage(Context context) {

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            return;
        } else {

            mProgressDialog = new AppCompatDialog(context);
            mProgressDialog.setCancelable(false);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mProgressDialog.setContentView(R.layout.dialog_default_loading);
            mProgressDialog.show();

        }
        ProgressBar pgsBar = mProgressDialog.findViewById(R.id.pBar);
        pgsBar.setIndeterminate(true);
        pgsBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context, R.color.topbar_bg), PorterDuff.Mode.MULTIPLY);
/*
        ImageView iv_progress_loading = (ImageView) mProgressDialog.findViewById(R.id.iv_Loading_Icon);
        Glide.with(context)
                .asGif()
                .load(R.raw.progressbar)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(iv_progress_loading);
*/


        //tv_progress_message.setText(message);
    }

    public void DismissLoadingPage() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void ShowMenuListPopup(Context context, final ArrayList<String> menuStr, final ArrayList<MsgPopupListener> menuListener) {

        RecyclerView rv_Menu_List;
        DialogMenuListAdapter mAdapter;

        View v = LayoutInflater.from(context).inflate(R.layout.dialog_menu_list_popup, null, false);

        rv_Menu_List = v.findViewById(R.id.rv_Menu_List);

        final AlertDialog dialog = new AlertDialog.Builder(context).setView(v).create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        mAdapter = new DialogMenuListAdapter(context);
        mAdapter.setItemCount(menuStr.size());
        mAdapter.setItemData(menuStr);
        mAdapter.setHasStableIds(true);

        rv_Menu_List.setAdapter(mAdapter);
        rv_Menu_List.setLayoutManager(new GridLayoutManager(context, 1));
        rv_Menu_List.addOnItemTouchListener(new RecyclerItemClickListener(context, rv_Menu_List, new OnRecyclerItemClickListener() {
            @Override
            public void onSingleClick(View view, int position) {

                if(menuListener.size() > position && menuListener.get(position) != null)
                    menuListener.get(position).Listener();

                dialog.dismiss();
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }

    public interface LoginPopupListener {
        void Listener(String nickname, String pw, AlertDialog dialog);
    }

    public void ShowLoginPopup(Context context, final LoginPopupListener listenerYes) {
        TextView tv_Login_Popup_Buttons_OK, tv_Login_Popup_Buttons_Cancel;
        final EditText et_Login_Nickname,et_Login_Password;

        View v = LayoutInflater.from(context).inflate(R.layout.dialog_login_popup, null, false);

        tv_Login_Popup_Buttons_OK = v.findViewById(R.id.tv_Login_Popup_Buttons_OK);
        tv_Login_Popup_Buttons_Cancel = v.findViewById(R.id.tv_Login_Popup_Buttons_Cancel);
        et_Login_Nickname = v.findViewById(R.id.et_Login_Nickname);
        et_Login_Password = v.findViewById(R.id.et_Login_Password);

        final AlertDialog dialog = new AlertDialog.Builder(context).setView(v).create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        tv_Login_Popup_Buttons_OK.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                if (listenerYes != null)
                    listenerYes.Listener(et_Login_Nickname.getText().toString(), et_Login_Password.getText().toString(), dialog);
                else
                    dialog.dismiss();
            }
        });

        tv_Login_Popup_Buttons_Cancel.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                dialog.dismiss();
            }
        });
    }
}
