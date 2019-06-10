package fifty.fiftyhouse.com.fifty.activty;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import fifty.fiftyhouse.com.fifty.R;
public class StoryEditActivity extends AppCompatActivity {

    ImageView iv_Story_Edit_Save, iv_Story_Edit_Back;
    TextView tv_Story_Edit_Count;
    EditText et_Story_Edit_Story;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_edit);

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
    }
}