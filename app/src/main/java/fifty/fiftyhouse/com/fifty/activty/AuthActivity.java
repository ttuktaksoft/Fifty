package fifty.fiftyhouse.com.fifty.activty;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.annotations.SerializedName;
import com.google.protobuf.Any;
import com.kakao.auth.ApiResponseCallback;
import com.kakao.auth.authorization.accesstoken.AccessToken;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.MainActivity;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class AuthActivity extends AppCompatActivity {


    WebView activity_auth;
    Context mContext;
    String FileUri;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        activity_auth = (WebView) findViewById(R.id.activity_auth_webview);
        mContext = getApplicationContext();

        WebSettings settings = activity_auth.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDefaultTextEncodingName("UTF-8");

        activity_auth.setWebChromeClient(new WebChromeClient());
        activity_auth.setWebViewClient(new WebViewClient());
        activity_auth.addJavascriptInterface(new JsHandler(), "Android");
        activity_auth.loadUrl("file:///android_asset/auth.html");
        //activity_auth.loadUrl("http://google.com");

    }

    private class JsHandler {
        String API_URL = "https://api.iamport.kr";
        Retrofit retrofit = new Retrofit.Builder().baseUrl(API_URL).addConverterFactory(GsonConverterFactory.create()).build();
        IamportClient iamportClient = retrofit.create(IamportClient.class);

        @JavascriptInterface
        public void getData(final String impUid) {
            String apiKey = "asd";
            String apiSecretKey = "asdasd";

            Object getData = iamportClient.token(new AuthData(apiKey, apiSecretKey));
            ((Call) getData).enqueue(new Callback<AccessToken>() {
                @Override
                public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                    if (response.isSuccessful()) {
                        String token = response.body().response.accessToken;
                        Log.d("#### IAM ", token);

                        Object getAuth = iamportClient.certification_by_imp_uid(token, impUid);
                        ((Call) getAuth).enqueue(new Callback<Certification>() {
                            @Override
                            public void onResponse(Call<Certification> call, final Response<Certification> response) {
                                if (response.isSuccessful()) {
                                    Log.d("#### IAM ", String.valueOf(response.body().response));

                                    final int i = Integer.parseInt(String.valueOf(response.body().response.birth));
                                    if (i >= 50) {

                                        FirebaseManager.CheckFirebaseComplete IndexListen = new FirebaseManager.CheckFirebaseComplete() {
                                            @Override
                                            public void CompleteListener() {

                                                final Map<String, String> properties = new HashMap<String, String>();
                                                properties.put("Index", TKManager.getInstance().MyData.GetUserIndex());

                                                UserManagement.getInstance().requestUpdateProfile(new ApiResponseCallback<Long>() {
                                                    @Override
                                                    public void onSuccess(Long result) {

                                                        String tempGender = String.valueOf(response.body().response.gender);
                                                        TKManager.getInstance().MyData.SetUserName(response.body().response.name);

                                                        TKManager.getInstance().MyData.SetUserGender(Integer.parseInt(tempGender));
                                                        TKManager.getInstance().MyData.SetUserAge(i);

                                                        String strPhoneNumber;
                                                        TelephonyManager mgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                                                        try {
                                                            if (ActivityCompat.checkSelfPermission(AuthActivity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AuthActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                                                                // TODO: Consider calling
                                                                return;
                                                            }

                                                            String tmpPhoneNumber = mgr.getLine1Number();
                                                            strPhoneNumber = tmpPhoneNumber.replace("+82", "0");

                                                        } catch (Exception e) {
                                                            strPhoneNumber = "";
                                                        }

                                                        TKManager.getInstance().MyData.SetUserPhone(strPhoneNumber);
                                                        MoveSignUpActivity();

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
                                    else
                                    {
                                        DialogFunc.getInstance().ShowMsgPopup(AuthActivity.this, CommonFunc.getInstance().getStr(getResources(), R.string.tv_Auth_Age));
                                    }
                               }
                           }

                           @Override
                           public void onFailure(Call call, Throwable t) {

                           }
                       });
                    }
                }

                @Override
                public void onFailure(Call<AccessToken> call, Throwable t) {

                }
            });
        }

    }

    protected void MoveSignUpActivity() {
        final Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    class AuthData{
        @SerializedName("imp_key") String api_key;
        @SerializedName("imp_secret") String api_secret;
        public AuthData(String key, String secret)
        {
            api_key = key;
            api_secret = secret;
        }
    }

    class AccessToken{
        @SerializedName("code") Integer code;
        @SerializedName("message") Object message;
        @SerializedName("response") AccessTokenResponse response;

        public AccessToken(Integer Code, Object msg, AccessTokenResponse Response)
        {
            code = Code;
            message = msg;
            response = Response;
        }

    }

    class AccessTokenResponse
    {
        @SerializedName("access_token") String accessToken;
        @SerializedName("now") Integer now;
        @SerializedName("expired_at") Integer expiredAt;
    }

    class Certification{
        @SerializedName("code") Integer code;
        @SerializedName("message") Object message;
        @SerializedName("response") CertificationResponse response;
    }

    class CertificationResponse{
        @SerializedName("imp_uid") String impUid ;
        @SerializedName("merchant_uid") String merchantUid ;
        @SerializedName("pg_tid") String pgTid ;
        @SerializedName("pg_provider") String pgProvider;
        @SerializedName("name") String name;
        @SerializedName("gender") String gender;
        @SerializedName("birth") Integer birth;
        @SerializedName("foreigner") Boolean foreigner;
        @SerializedName("certified") Boolean certified;
        @SerializedName("certified_at") Integer certifiedAt;
        @SerializedName("unique_key") String uniqueKey;
        @SerializedName("unique_in_site") String uniqueInSite;
        @SerializedName("origin") String origin;
    }
}

    interface IamportClient {
        @POST("/users/getToken")
        Call<AccessToken> token(@Body AuthActivity.AuthData auth);

        @GET("/certifications/{imp_uid}")
        Call<AuthActivity.Certification> certification_by_imp_uid(
                @Header("Authorization") String token,
                @Path("imp_uid") String imp_uid
        );
}
