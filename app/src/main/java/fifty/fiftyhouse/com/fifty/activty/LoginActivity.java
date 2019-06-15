package fifty.fiftyhouse.com.fifty.activty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;

import java.util.HashMap;
import java.util.Map;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.MainActivity;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.R;

public class LoginActivity extends AppCompatActivity {

    ImageView iv_Icon;
    ImageView iv_kakao_login;
    private LoginButton btn_kakao_login;


    private static LoginActivity _Instance;
    private SessionCallback callback;

    public static LoginActivity getInstance() {
        if (_Instance == null)
        {
            _Instance = new LoginActivity();
        }
        return _Instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        CommonFunc.getInstance().mCurActivity = this;

        iv_Icon = findViewById(R.id.iv_login);
        iv_kakao_login = findViewById(R.id.iv_kakao_login);
        btn_kakao_login = (LoginButton) findViewById(R.id.btn_kakao_login);


        iv_kakao_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO 로그인 처리
                FirebaseManager.getInstance().SignInAnonymously(LoginActivity.this);
                Session session = Session.getCurrentSession();

                session.addCallback(new SessionCallback());

                session.open(AuthType.KAKAO_LOGIN_ALL, LoginActivity.this);

                //startActivity(new Intent(getApplicationContext(),SignUpActivity.class));
                //startActivity(new Intent(getApplicationContext(),MainActivity.class));
                //finish();
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

            final Map<String, String> properties = new HashMap<String, String>();
            properties.put("nickname", "leo");
            properties.put("age", "33");

            UserManagement.getInstance().requestMe(new MeResponseCallback() {

                @Override
                public void onSuccess(UserProfile result) {
                    Log.e("SessionCallback :: ", "onSuccess");


                    String nickname = result.getNickname();

                    String email = result.getEmail();

                    String profileImagePath = result.getProfileImagePath();

                    String thumnailPath = result.getThumbnailImagePath();

                    String UUID = result.getUUID();


                    long id = result.getId();


                    Log.e("Profile : ", nickname + "");

                    Log.e("Profile : ", email + "");

                    Log.e("Profile : ", profileImagePath  + "");

                    Log.e("Profile : ", thumnailPath + "");

                    Log.e("Profile : ", UUID + "");

                    Log.e("Profile : ", id + "");

                    redirectSignupActivity();
                }

                @Override

                public void onSessionClosed(ErrorResult errorResult) {

                    Log.e("SessionCallback :: ", "onSessionClosed : " + errorResult.getErrorMessage());
                }

                // 회원이 아닌 경우,
                @Override
                public void onNotSignedUp() {
                    Log.e("SessionCallback :: ", "onNotSignedUp");
                }
            });

        }
    }

    protected void redirectSignupActivity() {
        final Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
