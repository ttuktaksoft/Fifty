package fifty.fiftyhouse.com.fifty.activty;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
public class MemoEditActivity extends AppCompatActivity {

    ImageView iv_Story_Edit_Save, iv_Story_Edit_Back;
    TextView tv_Story_Edit_Count;
    EditText et_Story_Edit_Story;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_edit);

        iv_Story_Edit_Back = findViewById(R.id.iv_Story_Edit_Back);
        iv_Story_Edit_Save = findViewById(R.id.iv_Story_Edit_Save);
        tv_Story_Edit_Count = findViewById(R.id.tv_Story_Edit_Count);
        et_Story_Edit_Story = findViewById(R.id.et_Story_Edit_Story);

        iv_Story_Edit_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        iv_Story_Edit_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tempMemo = et_Story_Edit_Story.getText().toString();
                if(tempMemo.getBytes().length > 0)
                {
                    TKManager.getInstance().MyData.SetUserMemo(tempMemo);
                    FirebaseManager.getInstance().SetUserMemo();
                }

                startActivity(new Intent(MemoEditActivity.this, MyProfileEditActivity.class));
                finish();
            }
        });

    }
}
