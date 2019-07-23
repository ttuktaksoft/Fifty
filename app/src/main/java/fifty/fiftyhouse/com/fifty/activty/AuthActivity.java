package fifty.fiftyhouse.com.fifty.activty;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.GregorianCalendar;
import android.os.Bundle;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
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

        DialogFunc.getInstance().DismissLoadingPage();

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
            String apiKey = "3437818520863680";
            String apiSecretKey = "yX1N2tEmW34OcUorUd5iTWllF4YK7s0Fq1stv7PraUgnPEPKSU2MQn7ws6gzzgf0iItNQjf1evo3adN2";

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


                                    Date tempDate = new Date(response.body().response.birth * 1000L);
                                    final int nBirthYear = tempDate.getYear();

                                    Calendar current = Calendar.getInstance();
                                    int currentYear  = current.get(Calendar.YEAR);
                                    currentYear -= 1900;
                                    int nAge = currentYear - nBirthYear + 1;

                                  //  final int i = Integer.parseInt(String.valueOf(response.body().response.birth));
                                    //if (nAge >= 50) {
                                    if (nAge >= 20) {

                                        String tempGender = String.valueOf(response.body().response.gender);
                                        if(tempGender.equals("male"))
                                            TKManager.getInstance().MyData.SetUserGender(0);
                                        else
                                            TKManager.getInstance().MyData.SetUserGender(1);

                                        TKManager.getInstance().MyData.SetUserName(response.body().response.name);

                                        TKManager.getInstance().MyData.SetUserAge(nAge);

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
                                    }
                                    else
                                    {
                                        DialogFunc.getInstance().ShowMsgPopup(AuthActivity.this, CommonFunc.getInstance().getStr(getResources(), R.string.tv_Auth_Age));
                                        android.os.Process.killProcess(android.os.Process.myPid());
                                    }
                               }
                           }

                           @Override
                           public void onFailure(Call call, Throwable t) {

                                Log.d("####", t.toString());
                           }
                       });
                    }
                }

                @Override
                public void onFailure(Call<AccessToken> call, Throwable t) {
                    Log.d("####", t.toString());
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

        public AccessTokenResponse(String Code, Integer msg, Integer Response)
        {
            accessToken  = Code;
            now = msg;
            expiredAt = Response;
        }

    }

    class Certification{
        @SerializedName("code") Integer code;
        @SerializedName("message") String message;
        @SerializedName("response") CertificationResponse response;

        public Certification(Integer Code, String msg, CertificationResponse Response)
        {
            code = Code;
            message = msg;
            response = Response;
        }

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

        public CertificationResponse(String impuid, String merchantuid, String pgtid, String pgprovider, String Name, String Gender, Integer Birth, Boolean Foreigner, Boolean Certified, Integer CertifiedAt, String UniqueKey, String UniqueInSite, String Origin)
        {
            impUid = impuid;
            merchantUid = merchantuid;
            pgTid = pgtid;
            pgProvider = pgprovider;
            name = Name;
            gender = Gender;
            birth = Birth;
            foreigner = Foreigner;
            certified = Certified;
            certifiedAt = CertifiedAt;
            uniqueKey = UniqueKey;
            uniqueInSite = UniqueInSite;
            origin = Origin;


        }

    }
}

    interface IamportClient {
        @POST("/users/getToken")
        Call<AuthActivity.AccessToken> token(@Body AuthActivity.AuthData auth);

        @GET("/certifications/{imp_uid}")
        Call<AuthActivity.Certification> certification_by_imp_uid(
                @Header("Authorization") String token,
                @Path("imp_uid") String imp_uid
        );
}
