package fifty.fiftyhouse.com.fifty.activty;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import fifty.fiftyhouse.com.fifty.GlobalApplication;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;

public class StrContentActivity extends AppCompatActivity {

    View ui_StrContent_TopBar;
    TextView tv_TopBar_Title;
    ImageView iv_TopBar_Back;
    Context mContext;
    TextView tv_StrContent_Desc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_str_content);
        mContext = getApplicationContext();

        Intent intent = getIntent(); //getIntent()로 받을준비
        String title = getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content");

        ui_StrContent_TopBar = findViewById(R.id.ui_StrContent_TopBar);
        tv_TopBar_Title = ui_StrContent_TopBar.findViewById(R.id.tv_TopBar_Title);
        iv_TopBar_Back = ui_StrContent_TopBar.findViewById(R.id.iv_TopBar_Back);

        tv_StrContent_Desc = findViewById(R.id.tv_StrContent_Desc);

        tv_TopBar_Title.setText(title);
        GlobalApplication.getGlobalApplicationContext().SetMainMenuFont(tv_TopBar_Title);
        GlobalApplication.getGlobalApplicationContext().SetContentFont(tv_StrContent_Desc);
        iv_TopBar_Back.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                finish();
            }
        });

        tv_StrContent_Desc.setMovementMethod(new ScrollingMovementMethod());
        tv_StrContent_Desc.setText(content);
    }
}
