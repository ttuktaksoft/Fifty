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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;

// 닉네임 입니다
public class SignUpActivity extends AppCompatActivity {

    private EditText et_SignUp_NickName;
    ImageView iv_SignUp_BG, iv_SignUp_CheckNickName, iv_SignUp_Next;

    boolean mIsCheckNickName = false;
    InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

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
                final String strNickName = et_SignUp_NickName.getText().toString();
                if(TextUtils.isEmpty(strNickName))
                {
                    // TODO 닉네임 입력 해달라는 팝업 표시
                    DialogFunc.getInstance().ShowMsgPopup(SignUpActivity.this, CommonFunc.getInstance().getStr(getResources(), R.string.MSG_NOTICE), CommonFunc.getInstance().getStr(getResources(), R.string.SIGN_UP_NICKNAME_EMPTY));
                }
                else
                {
                    imm.hideSoftInputFromWindow(et_SignUp_NickName.getWindowToken(), 0);
                    FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                        @Override
                        public void CompleteListener() {

                        }

                        @Override
                        public void CompleteListener_Yes() {
                            DialogFunc.getInstance().ShowMsgPopup(SignUpActivity.this, CommonFunc.getInstance().getStr(getResources(), R.string.MSG_NOTICE), CommonFunc.getInstance().getStr(getResources(), R.string.SIGN_UP_NICKNAME_CHECK_SUCCESS));
                            mIsCheckNickName = true;
                            ImageViewCompat.setImageTintList(iv_SignUp_Next, ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.button_enable)));
                        }

                        @Override
                        public void CompleteListener_No() {
                            DialogFunc.getInstance().ShowMsgPopup(SignUpActivity.this, CommonFunc.getInstance().getStr(getResources(), R.string.MSG_NOTICE), CommonFunc.getInstance().getStr(getResources(), R.string.SIGN_UP_NICKNAME_CHECK_FAIL));
                        }
                    };

                    FirebaseManager.getInstance().CheckNickName(strNickName, listener);

                    // TODO 중복체크 결과
                    // 중복 안됨

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
            }
            }
        });
    }




}
