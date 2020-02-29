package fifty.fiftyhouse.com.fifty.activty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DataBase.AccessToken;
import fifty.fiftyhouse.com.fifty.DataBase.AuthData;
import fifty.fiftyhouse.com.fifty.DataBase.Certification;
import fifty.fiftyhouse.com.fifty.DataBase.Payment;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.Interface.IamportClient;
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

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class PurchaseActivity extends AppCompatActivity {

    WebView activity_Purchase;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        activity_Purchase = (WebView) findViewById(R.id.activity_purchase_webview);
        mContext = getApplicationContext();

        DialogFunc.getInstance().DismissLoadingPage();

        WebSettings settings = activity_Purchase.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDefaultTextEncodingName("UTF-8");

        activity_Purchase.setWebChromeClient(new WebChromeClient());
        activity_Purchase.setWebViewClient(new WebViewClient());
        activity_Purchase.addJavascriptInterface(new PurchaseActivity.JsHandler(), "Android");
        activity_Purchase.loadUrl("file:///android_asset/purchase.html");

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

                        Object getAuth = iamportClient.payment_by_imp_uid(token, impUid);
                        ((Call) getAuth).enqueue(new Callback<Payment>() {
                            @Override
                            public void onResponse(Call<Payment> call, final Response<Payment> response) {
                                if (response.isSuccessful()) {
                                    Log.d("#### IAM ", String.valueOf(response.body().getBankName()));

                                    TKManager.getInstance().MyData.SetUserVip(CommonFunc.getInstance().GetCurrentDate());
                                    FirebaseManager.getInstance().PurchaseVip();
                                    DialogFunc.getInstance().ShowToast(getApplicationContext(), "변경 되었습니다.", false);
                                    finish();
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


}


