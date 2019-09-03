package fifty.fiftyhouse.com.fifty.activty;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.fragment.SignUpFragment;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;

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

        DialogFunc.getInstance().DismissLoadingPage();

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

                if(mSignUpFragment.isSignUp())
                {
                    DialogFunc.getInstance().ShowLoadingPage(SignUpActivity.this);
                    final String strPassword = mSignUpFragment.getPassword();
                    TKManager.getInstance().MyData.SetUserPassWord(strPassword);

                    FirebaseManager.CheckFirebaseComplete firebaseAuthListener = new FirebaseManager.CheckFirebaseComplete() {

                        @Override
                        public void CompleteListener() {

                            FirebaseManager.CheckFirebaseComplete Innerlistener = new FirebaseManager.CheckFirebaseComplete() {
                                @Override
                                public void CompleteListener() {
                                    DialogFunc.MsgPopupListener listener = new DialogFunc.MsgPopupListener()
                                    {
                                        @Override
                                        public void Listener()
                                        {

                                            CommonFunc.getInstance().SortByDistance(TKManager.getInstance().UserList_Dist, TKManager.getInstance().View_UserList_Dist, true);
                                            //CommonFunc.getInstance().SortByDistance(TKManager.getInstance().UserList_New, TKManager.getInstance().View_UserList_New, true);
                                            TKManager.getInstance().View_UserList_New = TKManager.getInstance().UserList_New;
                                            TKManager.getInstance().View_UserList_Hot = TKManager.getInstance().UserList_Hot;

                                            long seed = System.nanoTime();
                                            Collections.shuffle(TKManager.getInstance().View_UserList_Hot, new Random(seed));


                                            SharedPreferences sharedPreferences = getSharedPreferences("userFile",MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString("Index",TKManager.getInstance().MyData.GetUserIndex());
                                            editor.commit();
                                            CommonFunc.getInstance().MoveMainActivity(SignUpActivity.this, true);

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

                    /*        FirebaseManager.CheckFirebaseComplete FavoriteListener = new   FirebaseManager.CheckFirebaseComplete() {
                                @Override
                                public void CompleteListener() {

                                }

                                @Override
                                public void CompleteListener_Yes() {

                                }

                                @Override
                                public void CompleteListener_No() {
                                    FirebaseManager.CheckFirebaseComplete Innerlistener = new FirebaseManager.CheckFirebaseComplete() {
                                        @Override
                                        public void CompleteListener() {
                                            DialogFunc.MsgPopupListener listener = new DialogFunc.MsgPopupListener()
                                            {
                                                @Override
                                                public void Listener()
                                                {

                                                    CommonFunc.getInstance().SortByDistance(TKManager.getInstance().UserList_Dist, TKManager.getInstance().View_UserList_Dist, true);
                                                    CommonFunc.getInstance().SortByDistance(TKManager.getInstance().UserList_New, TKManager.getInstance().View_UserList_New, true);
                                                    //CommonFunc.getInstance().SortByDistance(TKManager.getInstance().UserList_Hot, TKManager.getInstance().View_UserList_Hot, true);

                                                    SharedPreferences sharedPreferences = getSharedPreferences("userFile",MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    editor.putString("Index",TKManager.getInstance().MyData.GetUserIndex());
                                                    editor.commit();

                                                    CommonFunc.getInstance().MoveMainActivity(SignUpActivity.this, true);
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

                            FirebaseManager.getInstance().GetDailyFavorite(FavoriteListener);*/
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
