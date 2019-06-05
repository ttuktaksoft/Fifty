package fifty.fiftyhouse.com.fifty.activty;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import fifty.fiftyhouse.com.fifty.R;
public class StrContentActivity extends AppCompatActivity {

    ImageView iv_StrContent_Back;
    TextView tv_StrContent_Title, tv_StrContent_Desc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_str_content);

        iv_StrContent_Back = findViewById(R.id.iv_StrContent_Back);
        tv_StrContent_Title = findViewById(R.id.tv_StrContent_Title);
        tv_StrContent_Desc = findViewById(R.id.tv_StrContent_Desc);

        iv_StrContent_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tv_StrContent_Desc.setMovementMethod(new ScrollingMovementMethod());
    }
}
