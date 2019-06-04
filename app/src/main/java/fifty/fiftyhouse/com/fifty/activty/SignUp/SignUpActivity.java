package fifty.fiftyhouse.com.fifty.activty.SignUp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.R;

public class SignUpActivity extends AppCompatActivity {

    private EditText et_SignUp_NickName;
    ImageView iv_SignUp_BG, iv_SignUp_CheckNickName, iv_SignUp_Next;

    boolean mIsCheckNickName = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        iv_SignUp_BG = findViewById(R.id.iv_SignUp_BG);
        et_SignUp_NickName = findViewById(R.id.et_SignUp_NickName);
        iv_SignUp_CheckNickName = findViewById(R.id.iv_SignUp_CheckNickName);
        iv_SignUp_Next = findViewById(R.id.iv_SignUp_Next);


        ImageViewCompat.setImageTintList(iv_SignUp_Next, ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.gray_light)));
        et_SignUp_NickName.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View view) {
                                                      // 입력을 시도 했을경우 중복체크 안함으로 수정
                                                      mIsCheckNickName = false;
                                                      ImageViewCompat.setImageTintList(iv_SignUp_Next, ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.gray_light)));
                                                  }
                                              });

        iv_SignUp_CheckNickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strNickName = et_SignUp_NickName.getText().toString();
                if(TextUtils.isEmpty(strNickName))
                {
                    // TODO 닉네임 입력 해달라는 팝업 표시
                    DialogFunc.getInstance().ShowMsgPopup(SignUpActivity.this, CommonFunc.getInstance().getStr(getResources(), R.string.MSG_NOTICE), CommonFunc.getInstance().getStr(getResources(), R.string.SIGN_UP_NICKNAME_EMPTY));
                }
                else
                {
                    FirebaseManager.getInstance().CheckNickName(strNickName);

                    // TODO 중복체크 결과
                    // 중복 안됨
                    mIsCheckNickName = true;
                    ImageViewCompat.setImageTintList(iv_SignUp_Next, ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.iv_SignUp_Next)));
                }
            }
        });


        iv_SignUp_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mIsCheckNickName == false)
                {
                    // TODO 중복체크 요청 팝업
                    DialogFunc.getInstance().ShowMsgPopup(SignUpActivity.this, CommonFunc.getInstance().getStr(getResources(), R.string.MSG_NOTICE), CommonFunc.getInstance().getStr(getResources(), R.string.SIGN_UP_NICKNAME_CHECK));
                }
                else
                {
                    Intent intent = new Intent(getApplicationContext(), BirthActivity.class);
                    startActivity(intent);
                    finish();
            }
            }
        });
    }



}
