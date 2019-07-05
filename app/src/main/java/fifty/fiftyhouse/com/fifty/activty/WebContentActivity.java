package fifty.fiftyhouse.com.fifty.activty;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;

public class WebContentActivity extends AppCompatActivity {
    Context mContext;
    TextView tv_TopBar_Title;
    View ui_WebContent_TopBar;
    ImageView iv_TopBar_Back;
    WebView wv_WebContent_Desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_content);

        mContext = getApplicationContext();

        Intent intent = getIntent(); //getIntent()로 받을준비
        String title = getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content");

        ui_WebContent_TopBar = findViewById(R.id.ui_WebContent_TopBar);
        tv_TopBar_Title = ui_WebContent_TopBar.findViewById(R.id.tv_TopBar_Title);
        iv_TopBar_Back = ui_WebContent_TopBar.findViewById(R.id.iv_TopBar_Back);

        wv_WebContent_Desc = findViewById(R.id.wv_WebContent_Desc);


        WebSettings settings = wv_WebContent_Desc.getSettings();
        settings.setJavaScriptEnabled(true);
        wv_WebContent_Desc.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        DialogFunc.getInstance().ShowLoadingPage(WebContentActivity.this);
        wv_WebContent_Desc.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                DialogFunc.getInstance().DismissLoadingPage();
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                DialogFunc.getInstance().DismissLoadingPage();
            }
        });
        wv_WebContent_Desc.loadUrl(content);


        tv_TopBar_Title.setText(title);
        iv_TopBar_Back.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                finish();
            }
        });

    }
}
