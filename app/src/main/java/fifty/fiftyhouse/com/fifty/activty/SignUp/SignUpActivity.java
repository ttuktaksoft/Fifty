package fifty.fiftyhouse.com.fifty.activty.SignUp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.R;

public class SignUpActivity extends AppCompatActivity {

    private EditText et_NickName;
    private Button bt_Check;

    private Button bt_SignUp_Back, bt_SignUp_Next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        et_NickName = (EditText)findViewById(R.id.et_SignUp_NickName);
        final String strNickName = et_NickName.getText().toString();

        bt_Check = (Button)findViewById(R.id.bt_SignUp_CheckNickName);
        bt_Check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(strNickName))
                {
                    // TODO 닉네임 입력 해달라는 팝업 표시
                }
                else
                {
                    FirebaseManager.getInstance().CheckNickName(strNickName);

                    // TODO 중복체크 결과
                }
            }
        });

        bt_SignUp_Back = (Button)findViewById(R.id.bt_SignUp_BackButton);
        bt_SignUp_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        bt_SignUp_Next = (Button)findViewById(R.id.bt_SignUp_NextButton);
        bt_SignUp_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BirthActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }



}
