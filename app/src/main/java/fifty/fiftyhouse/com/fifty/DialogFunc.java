package fifty.fiftyhouse.com.fifty;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;

import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;

import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.adapter.DialogMenuListAdapter;
import fifty.fiftyhouse.com.fifty.util.OnRecyclerItemClickListener;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;
import fifty.fiftyhouse.com.fifty.viewPager.MainTodayViewPager;

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

        LottieAnimationView animationView = (LottieAnimationView) mProgressDialog.findViewById(R.id.animation_view);
        animationView.setAnimation("loading.json");
        animationView.loop(true);
        animationView.playAnimation();

        /*ProgressBar pgsBar = mProgressDialog.findViewById(R.id.pBar);
        pgsBar.setIndeterminate(true);
        pgsBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context, R.color.loading_progress_bar), PorterDuff.Mode.MULTIPLY);*/


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

    public void ShowMenuListPopup(Context context, final ArrayList<String> menuStr, final ArrayList<MsgPopupListener> menuListener, String desc) {

        RecyclerView rv_Menu_List;
        TextView tv_Menu_List_Desc;
        DialogMenuListAdapter mAdapter;

        View v = LayoutInflater.from(context).inflate(R.layout.dialog_menu_list_popup, null, false);

        rv_Menu_List = v.findViewById(R.id.rv_Menu_List);
        tv_Menu_List_Desc = v.findViewById(R.id.tv_Menu_List_Desc);

        final AlertDialog dialog = new AlertDialog.Builder(context).setView(v).create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        mAdapter = new DialogMenuListAdapter(context);
        mAdapter.setItemCount(menuStr.size());
        mAdapter.setItemData(menuStr);
        mAdapter.setHasStableIds(true);

        if(CommonFunc.getInstance().CheckStringNull(desc))
            tv_Menu_List_Desc.setVisibility(View.GONE);
        else
        {
            tv_Menu_List_Desc.setVisibility(View.VISIBLE);
            tv_Menu_List_Desc.setText(desc);
        }

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

    public void ShowFavoriteSearchPopup(final Context context, final Activity activity) {
        final EditText et_User_Search_Popup_Name;
        TextView tv_User_Search_Popup_Buttons_OK, tv_User_Search_Popup_Buttons_Cancel;
        TextView tv_User_Search_Popup_Title;

        View v = LayoutInflater.from(context).inflate(R.layout.dialog_user_search_popup, null, false);

        et_User_Search_Popup_Name = v.findViewById(R.id.et_User_Search_Popup_Name);
        tv_User_Search_Popup_Buttons_OK = v.findViewById(R.id.tv_User_Search_Popup_Buttons_OK);
        tv_User_Search_Popup_Buttons_Cancel =  v.findViewById(R.id.tv_User_Search_Popup_Buttons_Cancel);
        tv_User_Search_Popup_Title = v.findViewById(R.id.tv_User_Search_Popup_Title);

        tv_User_Search_Popup_Title.setText("관심사 찾기");
        et_User_Search_Popup_Name.setHint("관심사를 입력해주세요");

        final AlertDialog dialog = new AlertDialog.Builder(context).setView(v).create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        tv_User_Search_Popup_Buttons_OK.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                if(CommonFunc.getInstance().CheckStringNull(et_User_Search_Popup_Name.getText().toString()))
                {
                    DialogFunc.getInstance().ShowMsgPopup(context, CommonFunc.getInstance().getStr(context.getResources(), R.string.FAVORITE_EMPTY));
                }
                else
                {
                    DialogFunc.getInstance().ShowLoadingPage(context);
                    FirebaseManager.CheckFirebaseComplete findListener = new FirebaseManager.CheckFirebaseComplete() {
                        @Override
                        public void CompleteListener() {
                            DialogFunc.getInstance().DismissLoadingPage();

                            CommonFunc.getInstance().MoveMainActivity(activity, false);
                           // TKManager.getInstance().View_UserList_Hot = TKManager.getInstance().UserList_Hot;
                        }

                        @Override
                        public void CompleteListener_Yes() {

                        }

                        @Override
                        public void CompleteListener_No() {
                            DialogFunc.getInstance().DismissLoadingPage();
                            DialogFunc.getInstance().ShowMsgPopup(context, CommonFunc.getInstance().getStr(context.getResources(), R.string.FAVORITE_FIND_EMPTY));
                        }
                    };
                    FirebaseManager.getInstance().FindFavoriteList(et_User_Search_Popup_Name.getText().toString(), findListener);
                }

                dialog.dismiss();
            }
        });


        tv_User_Search_Popup_Buttons_Cancel.setOnClickListener(new OnSingleClickListener(){
            @Override
            public void onSingleClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void ShowUserSearchPopup(final Context context, final  Activity activity) {
        final EditText et_User_Search_Popup_Name;
        TextView tv_User_Search_Popup_Buttons_OK, tv_User_Search_Popup_Buttons_Cancel;

        View v = LayoutInflater.from(context).inflate(R.layout.dialog_user_search_popup, null, false);

        et_User_Search_Popup_Name = v.findViewById(R.id.et_User_Search_Popup_Name);
        tv_User_Search_Popup_Buttons_OK = v.findViewById(R.id.tv_User_Search_Popup_Buttons_OK);
        tv_User_Search_Popup_Buttons_Cancel =  v.findViewById(R.id.tv_User_Search_Popup_Buttons_Cancel);

        final AlertDialog dialog = new AlertDialog.Builder(context).setView(v).create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        tv_User_Search_Popup_Buttons_OK.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                if(CommonFunc.getInstance().CheckStringNull(et_User_Search_Popup_Name.getText().toString()))
                {
                    DialogFunc.getInstance().ShowMsgPopup(context, CommonFunc.getInstance().getStr(context.getResources(), R.string.NICKNAME_EMPTY));
                }
                else
                {
                    FirebaseManager.CheckFirebaseComplete findListener = new FirebaseManager.CheckFirebaseComplete() {
                        @Override
                        public void CompleteListener() {

                        }

                        @Override
                        public void CompleteListener_Yes() {

                        }

                        @Override
                        public void CompleteListener_No() {
                            DialogFunc.getInstance().ShowMsgPopup(context, CommonFunc.getInstance().getStr(context.getResources(), R.string.NICKNAME_FIND_EMPTY));
                        }
                    };
                    FirebaseManager.getInstance().FindUserByNickName(et_User_Search_Popup_Name.getText().toString(), activity, findListener);
                }

                dialog.dismiss();
            }
        });


        tv_User_Search_Popup_Buttons_Cancel.setOnClickListener(new OnSingleClickListener(){
            @Override
            public void onSingleClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void ShowUserInvitePopup(final Context context, final  Activity activity) {
        final EditText et_User_Search_Popup_Name;
        TextView tv_User_Search_Popup_Buttons_OK, tv_User_Search_Popup_Buttons_Cancel;
        TextView tv_User_Search_Popup_Title;

        View v = LayoutInflater.from(context).inflate(R.layout.dialog_user_search_popup, null, false);

        et_User_Search_Popup_Name = v.findViewById(R.id.et_User_Search_Popup_Name);
        tv_User_Search_Popup_Buttons_OK = v.findViewById(R.id.tv_User_Search_Popup_Buttons_OK);
        tv_User_Search_Popup_Buttons_Cancel =  v.findViewById(R.id.tv_User_Search_Popup_Buttons_Cancel);
        tv_User_Search_Popup_Title = v.findViewById(R.id.tv_User_Search_Popup_Title);

        tv_User_Search_Popup_Title.setText("클럽 초대");

        final AlertDialog dialog = new AlertDialog.Builder(context).setView(v).create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        tv_User_Search_Popup_Buttons_OK.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                if(CommonFunc.getInstance().CheckStringNull(et_User_Search_Popup_Name.getText().toString()))
                {
                    DialogFunc.getInstance().ShowMsgPopup(context, CommonFunc.getInstance().getStr(context.getResources(), R.string.NICKNAME_EMPTY));
                }
                else
                {
                    FirebaseManager.CheckFirebaseComplete findListener = new FirebaseManager.CheckFirebaseComplete() {
                        @Override
                        public void CompleteListener() {

                        }

                        @Override
                        public void CompleteListener_Yes() {

                        }

                        @Override
                        public void CompleteListener_No() {
                            DialogFunc.getInstance().ShowMsgPopup(context, CommonFunc.getInstance().getStr(context.getResources(), R.string.NICKNAME_FIND_EMPTY));
                        }
                    };
                    FirebaseManager.getInstance().FindUserByNickName(et_User_Search_Popup_Name.getText().toString(), activity, findListener);
                }

                dialog.dismiss();
            }
        });


        tv_User_Search_Popup_Buttons_Cancel.setOnClickListener(new OnSingleClickListener(){
            @Override
            public void onSingleClick(View view) {
                dialog.dismiss();
            }
        });
    }
}
