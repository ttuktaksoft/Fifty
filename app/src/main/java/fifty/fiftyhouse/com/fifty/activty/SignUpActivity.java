package fifty.fiftyhouse.com.fifty.activty;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.gravity.IChildGravityResolver;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.MainActivity;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.adapter.FavoriteViewAdapter;
import fifty.fiftyhouse.com.fifty.fragment.SignUpFragment;
import fifty.fiftyhouse.com.fifty.fragment.UserProfileFragment;
import fifty.fiftyhouse.com.fifty.util.ImageResize;
import fifty.fiftyhouse.com.fifty.util.OnRecyclerItemClickListener;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

// 닉네임 입니다
public class SignUpActivity extends AppCompatActivity {

    View ui_SignUp_TopBar;
    TextView tv_TopBar_Title;
    ImageView iv_TopBar_Back;
    TextView tv_SignUp_Save;
    ArrayList<ImageView> iv_SignUp_Save_Check_List = new ArrayList<>();

    Context mContext;
    Activity mActivity;
    FragmentManager mFragmentMgr;
    SignUpFragment mSignUpFragment;

    boolean mIsSignUpEnable = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mActivity = this;
        mContext = getApplicationContext();
        mFragmentMgr = getSupportFragmentManager();
        CommonFunc.getInstance().mCurActivity = this;

        ui_SignUp_TopBar = findViewById(R.id.ui_SignUp_TopBar);
        tv_TopBar_Title = ui_SignUp_TopBar.findViewById(R.id.tv_TopBar_Title);
        iv_TopBar_Back = ui_SignUp_TopBar.findViewById(R.id.iv_TopBar_Back);
        iv_TopBar_Back.setVisibility(View.GONE);
        tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.TITLE_SIGNUP));
        tv_SignUp_Save = findViewById(R.id.tv_SignUp_Save);

        iv_SignUp_Save_Check_List.add((ImageView)findViewById(R.id.iv_SignUp_Save_Check_1));
        iv_SignUp_Save_Check_List.add((ImageView)findViewById(R.id.iv_SignUp_Save_Check_2));
        iv_SignUp_Save_Check_List.add((ImageView)findViewById(R.id.iv_SignUp_Save_Check_3));
        iv_SignUp_Save_Check_List.add((ImageView)findViewById(R.id.iv_SignUp_Save_Check_4));

        mSignUpFragment = new SignUpFragment();

        mIsSignUpEnable = false;
        mFragmentMgr.beginTransaction().replace(R.id.fl_SignUp_FrameLayout, mSignUpFragment, "SignUpFragment").commit();
        SignUpCheckListener listener = new SignUpCheckListener()
        {
            @Override
            public void UpdateSignCheck(int count) {
                for(int i = 0 ; i < iv_SignUp_Save_Check_List.size() ; ++i)
                {
                    if(i + 1 > count)
                        iv_SignUp_Save_Check_List.get(i).setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.signup_check_1));
                    else
                        iv_SignUp_Save_Check_List.get(i).setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.signup_check_2));
                }

                if(count == iv_SignUp_Save_Check_List.size())
                    mIsSignUpEnable = true;
                else
                    mIsSignUpEnable = false;

                int selectBGColor = ContextCompat.getColor(mContext, R.color.button_enable);
                int selectSrtColor = ContextCompat.getColor(mContext, R.color.button_enable_str);
                int disableBGColor = ContextCompat.getColor(mContext, R.color.button_disable);
                int disableSrtColor = ContextCompat.getColor(mContext, R.color.button_disable_str);

                tv_SignUp_Save.setBackgroundColor(mIsSignUpEnable ? selectBGColor : disableBGColor);
                tv_SignUp_Save.setTextColor(mIsSignUpEnable ? selectSrtColor : disableSrtColor);
            }
        };

        mSignUpFragment.SetCheckListener(listener);

        tv_SignUp_Save.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                if(mIsSignUpEnable == false)
                    return;

                if(mSignUpFragment.isSignUp())
                {
                    DialogFunc.getInstance().ShowLoadingPage(SignUpActivity.this);
                    final String strPassword = mSignUpFragment.getPassword();
                    TKManager.getInstance().MyData.SetUserPassWord(strPassword);

                    FirebaseManager.CheckFirebaseComplete firebaseAuthListener = new FirebaseManager.CheckFirebaseComplete() {

                        @Override
                        public void CompleteListener() {
                            FirebaseManager.CheckFirebaseComplete firebaseListener = new FirebaseManager.CheckFirebaseComplete() {
                                @Override
                                public void CompleteListener() {


                                    FirebaseManager.CheckFirebaseComplete FavoriteListener = new   FirebaseManager.CheckFirebaseComplete() {
                                        @Override
                                        public void CompleteListener() {
                                            FirebaseManager.CheckFirebaseComplete Innerlistener = new FirebaseManager.CheckFirebaseComplete() {
                                                @Override
                                                public void CompleteListener() {
                                                    DialogFunc.getInstance().DismissLoadingPage();

                                                    DialogFunc.MsgPopupListener listener = new DialogFunc.MsgPopupListener()
                                                    {
                                                        @Override
                                                        public void Listener()
                                                        {

                                                            CommonFunc.getInstance().SortByDistance(TKManager.getInstance().UserList_Dist, true);
                                                            CommonFunc.getInstance().SortByDistance(TKManager.getInstance().UserList_New, true);
                                                            CommonFunc.getInstance().SortByDistance(TKManager.getInstance().UserList_Hot, true);

                                                            SharedPreferences sharedPreferences = getSharedPreferences("userFile",MODE_PRIVATE);
                                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                                            editor.putString("Index",TKManager.getInstance().MyData.GetUserIndex());
                                                            editor.commit();
                                                            CommonFunc.getInstance().MoveMainActivity(SignUpActivity.this);

                                                        }
                                                    };
                                                    DialogFunc.getInstance().ShowSignUpCompletePopup(SignUpActivity.this, listener);
                                                }

                                                @Override
                                                public void CompleteListener_Yes() {
                                                }

                                                @Override
                                                public void CompleteListener_No() {
                                                    DialogFunc.getInstance().DismissLoadingPage();
                                                    DialogFunc.getInstance().ShowToast(SignUpActivity.this, CommonFunc.getInstance().getStr(getResources(), R.string.MSG_RETRY), true);
                                                }
                                            };

                                            FirebaseManager.getInstance().GetUserList(Innerlistener);
                                        }

                                        @Override
                                        public void CompleteListener_Yes() {

                                        }

                                        @Override
                                        public void CompleteListener_No() {
                                            FirebaseManager.CheckFirebaseComplete Innerlistener = new FirebaseManager.CheckFirebaseComplete() {
                                                @Override
                                                public void CompleteListener() {
                                                    DialogFunc.getInstance().DismissLoadingPage();

                                                    DialogFunc.MsgPopupListener listener = new DialogFunc.MsgPopupListener()
                                                    {
                                                        @Override
                                                        public void Listener()
                                                        {

                                                            CommonFunc.getInstance().SortByDistance(TKManager.getInstance().UserList_Dist, true);
                                                            CommonFunc.getInstance().SortByDistance(TKManager.getInstance().UserList_New, true);
                                                            CommonFunc.getInstance().SortByDistance(TKManager.getInstance().UserList_Hot, true);

                                                            SharedPreferences sharedPreferences = getSharedPreferences("userFile",MODE_PRIVATE);
                                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                                            editor.putString("Index",TKManager.getInstance().MyData.GetUserIndex());
                                                            editor.commit();

                                                            CommonFunc.getInstance().MoveMainActivity(SignUpActivity.this);
                                                        }
                                                    };
                                                    DialogFunc.getInstance().ShowSignUpCompletePopup(SignUpActivity.this, listener);
                                                }

                                                @Override
                                                public void CompleteListener_Yes() {
                                                }

                                                @Override
                                                public void CompleteListener_No() {
                                                    DialogFunc.getInstance().DismissLoadingPage();
                                                    DialogFunc.getInstance().ShowToast(SignUpActivity.this, CommonFunc.getInstance().getStr(getResources(), R.string.MSG_RETRY), true);
                                                }
                                            };

                                            FirebaseManager.getInstance().GetUserList(Innerlistener);
                                        }
                                    };

                                    FirebaseManager.getInstance().GetDailyFavorite(FavoriteListener);
                                }

                                @Override
                                public void CompleteListener_Yes() {
                                }

                                @Override
                                public void CompleteListener_No() {
                                    DialogFunc.getInstance().DismissLoadingPage();
                                    DialogFunc.getInstance().ShowToast(SignUpActivity.this, CommonFunc.getInstance().getStr(getResources(), R.string.MSG_RETRY), true);
                                }
                            };

                            FirebaseManager.getInstance().SetMyDataOnFireBase(firebaseListener);
                        }

                        @Override
                        public void CompleteListener_Yes() {

                        }

                        @Override
                        public void CompleteListener_No() {
                            DialogFunc.getInstance().DismissLoadingPage();
                            DialogFunc.getInstance().ShowToast(SignUpActivity.this, CommonFunc.getInstance().getStr(getResources(), R.string.MSG_RETRY), true);
                        }
                    };

                    FirebaseManager.getInstance().SetMyAuthData(firebaseAuthListener);
                }
            }
        });
    }

    public interface SignUpCheckListener {
        void UpdateSignCheck(int count);
    }

}
