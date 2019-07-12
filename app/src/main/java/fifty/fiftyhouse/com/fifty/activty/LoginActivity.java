package fifty.fiftyhouse.com.fifty.activty;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.kakao.auth.ApiResponseCallback;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.MainActivity;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;

public class LoginActivity extends AppCompatActivity {

    ImageView iv_kakao_login, iv_Login;
    private static final int REQUEST_LOCATION = 1;
    private SessionCallback callback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        CommonFunc.getInstance().mCurActivity = this;

        iv_kakao_login = findViewById(R.id.iv_kakao_login);
        iv_Login = findViewById(R.id.iv_Login);

        callback= new SessionCallback();
        FirebaseManager.getInstance().SignInAnonymously(LoginActivity.this);


        iv_kakao_login.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                DialogFunc.getInstance().ShowLoadingPage(LoginActivity.this);

                // TODO 로그인 처리
                Session.getCurrentSession().addCallback(callback);
             //   Session.getCurrentSession().checkAndImplicitOpen();
                Session.getCurrentSession().open(AuthType.KAKAO_LOGIN_ALL, LoginActivity.this);
            }
        });

        iv_Login.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                DialogFunc.LoginPopupListener listener =  new DialogFunc.LoginPopupListener()
                {
                    @Override
                    public void Listener(String nickname, String pw, final AlertDialog dialog)
                    {
                        if(CommonFunc.getInstance().CheckStringNull(nickname))
                        {
                            DialogFunc.getInstance().ShowMsgPopup(LoginActivity.this, CommonFunc.getInstance().getStr(getResources(), R.string.NICKNAME_EMPTY));
                        }
                        if(CommonFunc.getInstance().CheckStringNull(pw))
                        {
                            DialogFunc.getInstance().ShowMsgPopup(LoginActivity.this, CommonFunc.getInstance().getStr(getResources(), R.string.PASSWORD_EMPTY));
                        }

                        else
                        {
                            FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                                @Override
                                public void CompleteListener() {
                                    dialog.dismiss();

                                    SharedPreferences sharedPreferences = getSharedPreferences("userFile",MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("Index",TKManager.getInstance().MyData.GetUserIndex());
                                    editor.commit();
                                    
                                    FirebaseManager.CheckFirebaseComplete FavoriteListener = new   FirebaseManager.CheckFirebaseComplete() {
                                        @Override
                                        public void CompleteListener() {
                                            CommonFunc.getInstance().GetUserList(LoginActivity.this);
                                        }

                                        @Override
                                        public void CompleteListener_Yes() {

                                        }

                                        @Override
                                        public void CompleteListener_No() {
                                            CommonFunc.getInstance().GetUserList(LoginActivity.this);
                                        }
                                    };

                                    FirebaseManager.getInstance().GetDailyFavorite(FavoriteListener);
                                }

                                @Override
                                public void CompleteListener_Yes() {
                                    DialogFunc.getInstance().ShowToast(LoginActivity.this, "닉네임이 잘못되었습니다", true);
                                }

                                @Override
                                public void CompleteListener_No() {
                                    DialogFunc.getInstance().ShowToast(LoginActivity.this, "비밀번호가 잘못되었습니다", true);
                                }
                            };

                            FirebaseManager.getInstance().SignInNickName(nickname, pw, listener);
                        }
                        // 로그인 완료 되면
                        //dialog.dismiss();
                    }
                };

               DialogFunc.getInstance().ShowLoginPopup(LoginActivity.this, listener);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    private class SessionCallback implements ISessionCallback {

        // 로그인에 성공한 상태

        @Override
        public void onSessionOpened() {
            requestMe();
        }

        // 로그인에 실패한 상태
        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.e("SessionCallback :: ", "onSessionOpenFailed : " + exception.getMessage());
        }

        // 사용자 정보 요청

        public void requestMe() {

         /*   final Map<String, String> properties = new HashMap<String, String>();
            properties.put("Index", "67");

            UserManagement.getInstance().requestUpdateProfile(new ApiResponseCallback<Long>() {
                @Override
                public void onSuccess(Long result) {
                    Log.i("Test", "MainActivity onSuccess");
                }

                @Override
                public void onSessionClosed(final ErrorResult errorResult) {
                    Log.i("Test", "MainActivity onSessionClosed");
                }

                @Override
                public void onFailure(final ErrorResult errorResult) {
                    Log.i("Test", "MainActivity onFailure ErrorResult = " + errorResult);
                }

                @Override
                public void onNotSignedUp() {
                    Log.i("Test", "MainActivity onNotSignedUp");
                }
            }, properties);*/

        List<String> keys = new ArrayList<>();
            keys.add("properties.nickname");
            keys.add("properties.Index");
            keys.add("properties.profile_image");
            keys.add("kakao_account.email");
            keys.add("kakao_account.gender");
            keys.add("kakao_account.age_range");

           UserManagement.getInstance().me(keys, new MeV2ResponseCallback() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    String message = "failed to get user info. msg=" + errorResult;
                    Logger.d(message);
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Logger.d("onSessionClosed");
                }

                @Override
                public void onSuccess(MeV2Response response) {
                    Logger.d("user id : " + response.getId());
                    Logger.d("email: " + response.getKakaoAccount().getEmail());
                    Logger.d("email: " + response.getKakaoAccount().getAgeRange());
                    Logger.d("email: " + response.getKakaoAccount().getGender());
                    Map<String, String> properties = new HashMap<String, String>();
                    properties = response.getProperties();
                    //Logger.d("profile image: " + response.getKakaoAccount().getProfileImagePath());
                    String tempUid = properties.get("Index");

                    if(CommonFunc.getInstance().CheckStringNull(tempUid))
                    {
                        DialogFunc.MsgPopupListener AuthListener = new DialogFunc.MsgPopupListener()
                        {
                            @Override
                            public void Listener()
                            {
                                DialogFunc.getInstance().DismissLoadingPage();
                                CommonFunc.getInstance().MoveAuthActivity(LoginActivity.this);
                            }
                        };
                        DialogFunc.getInstance().ShowMsgPopup(LoginActivity.this, AuthListener, null, CommonFunc.getInstance().getStr(getResources(), R.string.MSG_ME_CONFIRM_DESC), CommonFunc.getInstance().getStr(getResources(), R.string.MSG_ME_CONFIRM), CommonFunc.getInstance().getStr(getResources(), R.string.MSG_CANCEL));

                    }
                    else
                    {
                        TKManager.getInstance().MyData.SetUserIndex(tempUid);

                        CommonFunc.CheckLocationComplete listener = new CommonFunc.CheckLocationComplete() {
                            @Override
                            public void CompleteListener() {


                                FirebaseManager.CheckFirebaseComplete FavoriteListener = new   FirebaseManager.CheckFirebaseComplete() {
                                    @Override
                                    public void CompleteListener() {
                                        DialogFunc.getInstance().DismissLoadingPage();
                                        CommonFunc.getInstance().GetUserList(LoginActivity.this);
                                    }

                                    @Override
                                    public void CompleteListener_Yes() {

                                    }

                                    @Override
                                    public void CompleteListener_No() {
                                        DialogFunc.getInstance().DismissLoadingPage();
                                        CommonFunc.getInstance().GetUserList(LoginActivity.this);
                                    }
                                };

                                FirebaseManager.getInstance().GetDailyFavorite(FavoriteListener);
                            }

                            @Override
                            public void CompleteListener_Yes() {

                            }

                            @Override
                            public void CompleteListener_No() {
                                FirebaseManager.CheckFirebaseComplete FavoriteListener = new   FirebaseManager.CheckFirebaseComplete() {
                                    @Override
                                    public void CompleteListener() {
                                        DialogFunc.getInstance().DismissLoadingPage();
                                        CommonFunc.getInstance().GetUserList(LoginActivity.this);
                                    }

                                    @Override
                                    public void CompleteListener_Yes() {

                                    }

                                    @Override
                                    public void CompleteListener_No() {
                                        DialogFunc.getInstance().DismissLoadingPage();
                                        CommonFunc.getInstance().GetUserList(LoginActivity.this);
                                    }
                                };

                                FirebaseManager.getInstance().GetDailyFavorite(FavoriteListener);
                            }
                        };

                        CommonFunc.getInstance().GetUserLocation(LoginActivity.this, listener);

                    }


                 /*DialogFunc.MsgPopupListener listener = new DialogFunc.MsgPopupListener()
                    {
                        @Override
                        public void Listener()
                        {
                            // TODO 본인인증
                            CommonFunc.getInstance().MoveSignUpActivity(LoginActivity.this);
                            //MoveAuthActivity();
                        }
                    };
                    DialogFunc.getInstance().ShowMsgPopup(LoginActivity.this, listener, null, CommonFunc.getInstance().getStr(getResources(), R.string.MSG_ME_CONFIRM), CommonFunc.getInstance().getStr(getResources(), R.string.MSG_ME_CONFIRM), CommonFunc.getInstance().getStr(getResources(), R.string.MSG_CANCEL));*/

                }
            });

        }
    }

}
