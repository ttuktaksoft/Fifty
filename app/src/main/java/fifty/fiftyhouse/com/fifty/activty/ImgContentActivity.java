package fifty.fiftyhouse.com.fifty.activty;

import androidx.appcompat.app.AppCompatActivity;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ImgContentActivity extends AppCompatActivity {

    View ui_ImgContent_TopBar;
    TextView tv_TopBar_Title;
    ImageView iv_TopBar_Back;
    Context mContext;
    ImageView iv_ImgContent_Img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_content);
        mContext = getApplicationContext();

        Intent intent = getIntent(); //getIntent()로 받을준비
        String title = getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content");

        ui_ImgContent_TopBar = findViewById(R.id.ui_ImgContent_TopBar);

        tv_TopBar_Title = ui_ImgContent_TopBar.findViewById(R.id.tv_TopBar_Title);
        iv_TopBar_Back = ui_ImgContent_TopBar.findViewById(R.id.iv_TopBar_Back);

        iv_ImgContent_Img = findViewById(R.id.iv_ImgContent_Img);

        tv_TopBar_Title.setText(title);
        iv_TopBar_Back.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                finish();
            }
        });

        CommonFunc.getInstance().DrawImageByGlide(mContext, iv_ImgContent_Img, content, false);
    }
}
