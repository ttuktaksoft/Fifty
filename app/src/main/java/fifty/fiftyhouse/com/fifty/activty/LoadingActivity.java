package fifty.fiftyhouse.com.fifty.activty;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Point;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.Display;

import com.airbnb.lottie.LottieAnimationView;
import com.kakao.auth.ApiResponseCallback;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;

import static fifty.fiftyhouse.com.fifty.CommonData.NONE_STATE;

public class LoadingActivity extends AppCompatActivity {

    private Activity mActivity;
    private static final int REQUEST_LOCATION = 1;
    private static final int REQUEST_KAKAO = 2;
    String userIndex;
    private SessionCallback LoadingCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        mActivity = this;
        LoadingCallback = new SessionCallback();

        try {
            PackageInfo info = getPackageManager().getPackageInfo("fifty.fiftyhouse.com.fifty", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        final Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        CommonFunc.getInstance().setWidthByDevice(size.x);
        CommonFunc.getInstance().setHeightByDevice(size.y);




        if(true)
        {


        if(CommonFunc.getInstance().getWhatKindOfNetwork(mActivity).equals(NONE_STATE))
        {
            final DialogFunc.MsgPopupListener listenerYes = new DialogFunc.MsgPopupListener() {
                @Override
                public void Listener() {
                    mActivity.finish();
                    System.exit(0);
                    int pid = android.os.Process.myPid();
                    android.os.Process.killProcess(pid);
                }
            };

            DialogFunc.getInstance().ShowMsgPopup(mActivity, listenerYes, listenerYes, "인터넷 연결을 확인 해 주세요", "확인", null);

        }
        else
        {

            DialogFunc.getInstance().ShowLoadingPage(LoadingActivity.this);

            final LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


            FirebaseManager.CheckFirebaseComplete signListener = new FirebaseManager.CheckFirebaseComplete() {
                @Override
                public void CompleteListener() {

                    int permissionCamera = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
                    int permissionPhone = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE);
                    int permissionSMS = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_SMS);


                    SharedPreferences sf = getSharedPreferences("userFile",MODE_PRIVATE);
                    //text라는 key에 저장된 값이 있는지 확인. 아무값도 들어있지 않으면 ""를 반환
                    userIndex = sf.getString("Index","");
                 //   userIndex = null;
                     //userIndex = "71";

                    //Log.d("#@!!",  userIndex);
                    if(CommonFunc.getInstance().CheckStringNull(userIndex))
                    {
                        if(permissionCamera == PackageManager.PERMISSION_DENIED || permissionPhone == PackageManager.PERMISSION_DENIED || permissionSMS == PackageManager.PERMISSION_DENIED) {
                            ActivityCompat.requestPermissions(LoadingActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_SMS}, REQUEST_LOCATION);

                        } else {

                            CommonFunc.getInstance().MoveLoginActivity(LoadingActivity.this);

                        }
                    }
                    else
                    {


                        TKManager.getInstance().MyData.SetUserIndex(userIndex);

                        if(permissionCamera == PackageManager.PERMISSION_DENIED || permissionPhone == PackageManager.PERMISSION_DENIED || permissionSMS == PackageManager.PERMISSION_DENIED) {
                            ActivityCompat.requestPermissions(LoadingActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_SMS}, REQUEST_KAKAO);
                            Log.e("#@!!",  " requestPermissions KAKAO_LOGIN_ALL");
                        } else {
                            Session.getCurrentSession().addCallback(LoadingCallback);
                            // Session.getCurrentSession().checkAndImplicitOpen();
                            Session.getCurrentSession().open(AuthType.KAKAO_LOGIN_ALL, LoadingActivity.this);
                            Log.e("#@!!", "KAKAO_LOGIN_ALL");
                        }
                    }
                }

                @Override
                public void CompleteListener_Yes() {

                }

                @Override
                public void CompleteListener_No() {

                }
            };
            FirebaseManager.getInstance().SignInAnonymously(LoadingActivity.this, signListener);

            }
        }
        CommonFunc.getInstance().mCurActivity = this;
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
        Session.getCurrentSession().removeCallback(LoadingCallback);
    }

    private class SessionCallback implements ISessionCallback {

        // 로그인에 성공한 상태

        @Override
        public void onSessionOpened() {

            Log.e("#@!!", "requestMe");

            requestMe();
        }

        // 로그인에 실패한 상태
        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.e("SessionCallback :: ", "onSessionOpenFailed : " + exception.getMessage());
        }

        // 사용자 정보 요청
        public void requestMe() {

            List<String> keys = new ArrayList<>();
            keys.add("properties.nickname");
            keys.add("properties.Index");
            keys.add("properties.profile_image");
            keys.add("kakao_account.email");
            keys.add("kakao_account.gender");
            keys.add("kakao_account.age_range");

            Log.e("#@!!", "requestMe 1");

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
                //    Log.e("#@!!", tempUid);
                    //tempUid = null;

                    DialogFunc.getInstance().DismissLoadingPage();

                    if(CommonFunc.getInstance().CheckStringNull(tempUid))
                    {

                        DialogFunc.MsgPopupListener AuthListener = new DialogFunc.MsgPopupListener()
                        {
                            @Override
                            public void Listener()
                            {

                                //  CommonFunc.getInstance().MoveAuthActivity(LoginActivity.this);

                                DialogFunc.getInstance().ShowLoadingPage(LoadingActivity.this);
                                FirebaseManager.CheckFirebaseComplete IndexListen = new FirebaseManager.CheckFirebaseComplete() {
                                    @Override
                                    public void CompleteListener() {

                                        final Map<String, String> properties = new HashMap<String, String>();
                                        properties.put("Index", TKManager.getInstance().MyData.GetUserIndex());

                                        UserManagement.getInstance().requestUpdateProfile(new ApiResponseCallback<Long>() {
                                            @Override
                                            public void onSuccess(Long result) {

                                                //String tempGender = String.valueOf(response.body().response.gender);
                                                TKManager.getInstance().MyData.SetUserName("테스트");

                                                TKManager.getInstance().MyData.SetUserGender(1);
                                                TKManager.getInstance().MyData.SetUserAge(50);

                                                String strPhoneNumber;
                                                TelephonyManager mgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                                                try {
                                                    if (ActivityCompat.checkSelfPermission(LoadingActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LoadingActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                                                        // TODO: Consider calling
                                                        ActivityCompat.requestPermissions(LoadingActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.READ_PHONE_STATE}, REQUEST_LOCATION);
                                                        return;
                                                    }

                                                    String tmpPhoneNumber = mgr.getLine1Number();
                                                    strPhoneNumber = tmpPhoneNumber.replace("+82", "0");

                                                } catch (Exception e) {
                                                    strPhoneNumber = "";
                                                }

                                                TKManager.getInstance().MyData.SetUserPhone(strPhoneNumber);


                                                CommonFunc.CheckLocationComplete listener = new CommonFunc.CheckLocationComplete() {

                                                    @Override
                                                    public void CompleteListener() {
                                                        CommonFunc.getInstance().MoveSignUpActivity(LoadingActivity.this);
                                                    }

                                                    @Override
                                                    public void CompleteListener_Yes() {

                                                    }

                                                    @Override
                                                    public void CompleteListener_No() {

                                                    }
                                                };

                                                CommonFunc.getInstance().GetUserLocation(LoadingActivity.this, listener);

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
                                        }, properties);
                                    }

                                    @Override
                                    public void CompleteListener_Yes() {
                                    }

                                    @Override
                                    public void CompleteListener_No() {
                                        DialogFunc.getInstance().DismissLoadingPage();
                                    }
                                };

                                FirebaseManager.getInstance().GetUserIndex(IndexListen);

                            }
                        };
                        DialogFunc.getInstance().ShowMsgPopup(LoadingActivity.this, AuthListener, null, CommonFunc.getInstance().getStr(getResources(), R.string.MSG_ME_CONFIRM_DESC), CommonFunc.getInstance().getStr(getResources(), R.string.MSG_ME_CONFIRM), CommonFunc.getInstance().getStr(getResources(), R.string.MSG_CANCEL));

                    }
                    else
                    {
                        TKManager.getInstance().MyData.SetUserIndex(tempUid);

                        DialogFunc.getInstance().ShowLoadingPage(LoadingActivity.this);
                        CommonFunc.getInstance().GetUserList(LoadingActivity.this);
/*
                        CommonFunc.CheckLocationComplete listener = new CommonFunc.CheckLocationComplete() {
                            @Override
                            public void CompleteListener() {

                                FirebaseManager.CheckFirebaseComplete FavoriteListener = new   FirebaseManager.CheckFirebaseComplete() {
                                    @Override
                                    public void CompleteListener() {
                                        CommonFunc.getInstance().GetUserList(LoadingActivity.this);
                                    }

                                    @Override
                                    public void CompleteListener_Yes() {

                                    }

                                    @Override
                                    public void CompleteListener_No() {
                                        CommonFunc.getInstance().GetUserList(LoadingActivity.this);
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
                                        CommonFunc.getInstance().GetUserList(LoadingActivity.this);
                                    }

                                    @Override
                                    public void CompleteListener_Yes() {

                                    }

                                    @Override
                                    public void CompleteListener_No() {
                                        CommonFunc.getInstance().GetUserList(LoadingActivity.this);
                                    }
                                };

                                FirebaseManager.getInstance().GetDailyFavorite(FavoriteListener);
                            }
                        };

                        CommonFunc.getInstance().GetUserLocation(LoadingActivity.this, listener);
                        */

                    }
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        int PermissionCnt = 0;

        switch (requestCode) {
            case REQUEST_LOCATION:
                DialogFunc.getInstance().DismissLoadingPage();
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    int grantResult = grantResults[i];

                    if (permission.equals(Manifest.permission.READ_PHONE_STATE))
                    {
                        if(grantResult == PackageManager.PERMISSION_GRANTED)
                        {
                            PermissionCnt++;
                        }
                    }

                    if (permission.equals(Manifest.permission.READ_SMS))
                    {
                        if(grantResult == PackageManager.PERMISSION_GRANTED)
                        {
                            PermissionCnt++;
                        }
                    }
                    if (permission.equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        if(grantResult == PackageManager.PERMISSION_GRANTED) {
                            PermissionCnt++;
                        }
                    }
                }

                if(PermissionCnt == 3)
                {
                    CommonFunc.getInstance().MoveLoginActivity(LoadingActivity.this);
                }
                else
                {

                    DialogFunc.MsgPopupListener AuthListener = new DialogFunc.MsgPopupListener()
                    {
                        @Override
                        public void Listener()
                        {
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                    };
                    DialogFunc.getInstance().ShowMsgPopup(LoadingActivity.this, AuthListener, null, "권한 동의를 하지 않으셨습니다", CommonFunc.getInstance().getStr(getResources(), R.string.MSG_ME_CONFIRM), CommonFunc.getInstance().getStr(getResources(), R.string.MSG_CANCEL));                }
                break;

            case REQUEST_KAKAO:
                Log.e("#@!!",  " REQUEST_KAKAO");
                DialogFunc.getInstance().DismissLoadingPage();
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    int grantResult = grantResults[i];

                    if (permission.equals(Manifest.permission.READ_PHONE_STATE))
                    {
                        if(grantResult == PackageManager.PERMISSION_GRANTED)
                        {
                            PermissionCnt++;
                        }
                    }

                    if (permission.equals(Manifest.permission.READ_SMS))
                    {
                        if(grantResult == PackageManager.PERMISSION_GRANTED)
                        {
                            PermissionCnt++;
                        }
                    }
                    if (permission.equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        if(grantResult == PackageManager.PERMISSION_GRANTED) {
                            PermissionCnt++;
                        }
                    }
                }

                if(PermissionCnt == 3)
                {
                    Log.e("#@!!",  " PermissionCnt" + PermissionCnt);
                    Session.getCurrentSession().addCallback(LoadingCallback);
                    //   Session.getCurrentSession().checkAndImplicitOpen();
                    Session.getCurrentSession().open(AuthType.KAKAO_LOGIN_ALL, LoadingActivity.this);
                }
                else
                {

                    DialogFunc.MsgPopupListener AuthListener = new DialogFunc.MsgPopupListener()
                    {
                        @Override
                        public void Listener()
                        {
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                    };
                    DialogFunc.getInstance().ShowMsgPopup(LoadingActivity.this, AuthListener, null, "권한 동의를 하지 않으셨습니다", CommonFunc.getInstance().getStr(getResources(), R.string.MSG_ME_CONFIRM), CommonFunc.getInstance().getStr(getResources(), R.string.MSG_CANCEL));                }
                break;
        }
    }





}